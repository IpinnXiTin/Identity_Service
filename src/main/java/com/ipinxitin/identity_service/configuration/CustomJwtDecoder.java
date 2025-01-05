package com.ipinxitin.identity_service.configuration;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.ipinxitin.identity_service.dto.request.IntrospectRequest;
import com.ipinxitin.identity_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.text.ParseException;
import java.util.Objects;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomJwtDecoder implements JwtDecoder {
    
    @Value("${jwt.signerKey}")
    String signerKey;
    
    final AuthenticationService authenticationService;
    NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        
        try {
            var response = authenticationService.introspect(IntrospectRequest.builder()
                .token(token)
                .build()); 

            if (!response.isValid()) 
                throw new JwtException("Token invalid");
        }
        catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }

        if (Objects.isNull(nimbusJwtDecoder)) {

            nimbusJwtDecoder = NimbusJwtDecoder
                .withSecretKey(new SecretKeySpec(signerKey.getBytes(), "HS512"))
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
        }

        return nimbusJwtDecoder.decode(token);
    }
    
    
}
