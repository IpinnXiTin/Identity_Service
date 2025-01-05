package com.ipinxitin.identity_service.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ipinxitin.identity_service.dto.request.AuthenticationRequest;
import com.ipinxitin.identity_service.dto.request.IntrospectRequest;
import com.ipinxitin.identity_service.dto.request.LogoutRequest;
import com.ipinxitin.identity_service.dto.request.RefreshRequest;
import com.ipinxitin.identity_service.dto.response.AuthenticationResponse;
import com.ipinxitin.identity_service.dto.response.IntrospectResponse;
import com.ipinxitin.identity_service.entity.InvalidatedToken;
import com.ipinxitin.identity_service.entity.UserEntity;
import com.ipinxitin.identity_service.exception.AppException;
import com.ipinxitin.identity_service.exception.ErrorCode;
import com.ipinxitin.identity_service.repository.LogoutTokenRepository;
import com.ipinxitin.identity_service.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    
    UserRepository userRepository;
    LogoutTokenRepository logoutTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    String signerKey;

    @NonFinal
    @Value("${jwt.valid-duration}")
    int validDuration;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    int refreshableDuration;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        
        var user = userRepository.findByUserName(request.getUserName())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        
        boolean authenticated = passwordEncoder.matches(request.getPassWord(), user.getPassWord());

        if (!authenticated) 
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(user);

        return AuthenticationResponse.builder()
            .token(token)
            .build();
    }

    public String generateToken(UserEntity user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
            .subject(user.getUserName())
            .issuer("ipin")
            .issueTime(new Date())
            .expirationTime(
                new Date(Instant.now().plus(validDuration, ChronoUnit.SECONDS).toEpochMilli())
            )
            .claim("role", buildScope(user))
            .jwtID(UUID.randomUUID().toString())
            .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
        }
        return jwsObject.serialize();
    }

    public IntrospectResponse introspect(IntrospectRequest request) 
            throws ParseException, JOSEException {

        boolean isValid = true;
        try {
            verifyToken(request.getToken(), false);
        }
        catch(AppException ex) {
            isValid = false;
        }

        return IntrospectResponse.builder()
            .valid(isValid)
            .build();
    }

    public String buildScope(UserEntity user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        var roles = user.getRoles();

        if (!CollectionUtils.isEmpty(roles))
            roles.forEach(role -> stringJoiner.add("ROLE_" + role.getRoleName()));

        return stringJoiner.toString();
    }

    public SignedJWT verifyToken(String token, boolean isRefresh) 
                throws ParseException, JOSEException {

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh) 
            ? new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                .toInstant().plus(refreshableDuration, ChronoUnit.SECONDS).toEpochMilli())
            : signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(new MACVerifier(signerKey.getBytes()));

        if (!(verified && expiryTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (logoutTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.TOKEN_LOGGED_OUT);

        return signedJWT;
    }

    public void logout(LogoutRequest request) 
            throws ParseException, JOSEException {

        var signToken = verifyToken(request.getToken(), true);

        String jwtId = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
            .id(jwtId)
            .expiryTime(expiryTime)
            .build();

        logoutTokenRepository.save(invalidatedToken);
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) 
            throws ParseException, JOSEException {

        var signedToken = verifyToken(request.getToken(), true);

        var jwtId = signedToken.getJWTClaimsSet().getJWTID();
        var expiryTime = signedToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
            .id(jwtId)
            .expiryTime(expiryTime)
            .build();

        logoutTokenRepository.save(invalidatedToken);

        var user = userRepository.findByUserName(signedToken.getJWTClaimsSet().getSubject())
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var token = generateToken(user);

        return AuthenticationResponse.builder()
            .token(token)
            .build();
    }
}
