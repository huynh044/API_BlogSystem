package com.apimobilestore.service;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.apimobilestore.dto.request.AuthenticationRequest;
import com.apimobilestore.dto.request.IntrospectRequest;
import com.apimobilestore.dto.request.LogoutRequest;
import com.apimobilestore.dto.request.RefreshRequest;
import com.apimobilestore.dto.response.AuthenticationResponse;
import com.apimobilestore.dto.response.IntrospectResponse;
import com.apimobilestore.entity.InvalidatedToken;
import com.apimobilestore.entity.Permission;
import com.apimobilestore.entity.User;
import com.apimobilestore.exception.AppException;
import com.apimobilestore.exception.ErrorCode;
import com.apimobilestore.repository.InvalidatedTokenRepository;
import com.apimobilestore.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

public interface AuthenticationService {
    AuthenticationResponse Authenticate(AuthenticationRequest request);
    IntrospectResponse introspectResponse(IntrospectRequest request) throws ParseException, JOSEException;
    void logout(LogoutRequest request) throws ParseException, JOSEException;
    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
}

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository repository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
    
    @NonFinal
    @Value("${jwt.expirationTime}")
    protected int EXPIRATION_TIME;
    
    @Override
    public AuthenticationResponse Authenticate(AuthenticationRequest request) {
    	PasswordEncoder encoder = new BCryptPasswordEncoder(10);
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean authenticated = encoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token = generateToken(user);
       
        
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(authenticated)
                .build();
    }

    
    private String generateToken(User user) {
        try {
            // Tạo header với thuật toán HS256
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

            // Tạo claims set với thông tin của token
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer("https://your-issuer.com")
                    .issueTime(new Date())
                    .jwtID(UUID.randomUUID().toString())
                    .expirationTime(Date.from(new Date().toInstant().plus(EXPIRATION_TIME, ChronoUnit.HOURS))) // Hết hạn sau EXPIRATION_TIME giờ
                    .claim("scope", buildScope(user))
                    .build();

            // Tạo payload từ claims set
            Payload payload = new Payload(claimsSet.toJSONObject());

            // Tạo JWS object từ header và payload
            JWSObject jwsObject = new JWSObject(header, payload);

            // Ký JWS object với khóa bí mật
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

            // Trả về token dưới dạng chuỗi
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public IntrospectResponse introspectResponse(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);
        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

        Date expiryTime = isRefresh
                ? Date.from(claimsSet.getIssueTime().toInstant().plus(EXPIRATION_TIME, ChronoUnit.HOURS))
                : claimsSet.getExpirationTime();

        boolean verified = signedJWT.verify(verifier);

        if (!verified || expiryTime.before(new Date())) {
        
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidatedTokenRepository.existsById(claimsSet.getJWTID())) {
        	
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }


	
    @Override
	public void logout(LogoutRequest request) throws ParseException, JOSEException {
		try {
            var signToken = verifyToken(request.getToken(), true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException exception){
            log.info("Token already expired");
        }
		
	}


	
    @Override
	public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
		var signedJWT = verifyToken(request.getToken(), true);

        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        var username = signedJWT.getJWTClaimsSet().getSubject();

        var user =
                repository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
	}
    private String buildScope(User user) {

        StringJoiner stringJoiner = new StringJoiner(" ");

        Optional.ofNullable(user.getRoles())
                .orElse(Set.of())
                .stream()
                .forEach(role -> {
                    stringJoiner.add("ROLE_" + role.getName().toUpperCase());
                    Optional.ofNullable(role.getPermissions())
                            .orElse(Set.of())
                            .stream()
                            .map(Permission::getName)
                            .forEach(stringJoiner::add);
                });

        return stringJoiner.toString();
    }

}
