package com.team1.investsim.components;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.team1.investsim.entities.TokenEntity;
import com.team1.investsim.entities.UserEntity;
import com.team1.investsim.exceptions.AuthenticationFailedException;
import com.team1.investsim.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static com.team1.investsim.utils.DateUtil.ZONEOFFSET;

@Component
public class JwtManager {
    private final String secret_key = "catapimbas";

    private final Algorithm algorithm = Algorithm.HMAC256(secret_key);

    private final String ISSUER = "InvestSim";

    @Autowired
    private TokenRepository tokenRepository;

    public String generateToken(UserEntity user) throws AuthenticationFailedException {
        try {
            Instant expirationDate = this.generateExpirationDate();
            String token = JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getEmail())
                    .withExpiresAt(expirationDate)
                    .sign(this.algorithm);

            TokenEntity tokenEntity = new TokenEntity(token, expirationDate, user);
            tokenRepository.saveAndFlush(tokenEntity);
            return token;
        } catch (JWTCreationException e) {
            throw new AuthenticationFailedException("Error while authenticating");
        }
    }

    public Optional<String> validateToken(String token) {
        try {
            Optional<TokenEntity> tokenEntityOpt = tokenRepository.findByToken(token);

            if (tokenEntityOpt.isPresent()) {
                TokenEntity tokenEntity = tokenEntityOpt.get();

                if (tokenEntity.getExpirationDate().isAfter(Instant.now())) {
                    return Optional.ofNullable(JWT.require(this.algorithm)
                            .withIssuer(this.ISSUER)
                            .build()
                            .verify(token)
                            .getSubject());
                }
            }
            return Optional.empty();
        } catch (JWTCreationException e) {
            return Optional.empty();
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.of(ZONEOFFSET));
    }
}