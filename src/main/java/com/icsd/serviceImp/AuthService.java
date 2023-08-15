package com.icsd.serviceImp;


import com.icsd.auth.AuthenticationResponse;
import com.icsd.auth.config.JwtService;
import com.icsd.auth.token.Token;
import com.icsd.auth.token.TokenRepository;
import com.icsd.auth.token.TokenType;
import com.icsd.dto.CustomerLoginDTO;
import com.icsd.model.Customer;
import com.icsd.repo.CustomerRepo;

import lombok.Data;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;


@Data
@Service
public class AuthService {
    private final CustomerRepo repository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
//    public AuthenticationResponse register(Customer customer) {
//
//        String jwtToken = jwtService.generateToken(customer);
//        String refreshToken = jwtService.generateRefreshToken(customer);
//        saveUserToken(customer, jwtToken);
//        return AuthenticationResponse.builder()
//                .accessToken(jwtToken)
//                .refreshToken(refreshToken)
//                .build();
//    }

    public AuthenticationResponse authenticate(CustomerLoginDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmailId(),
                        request.getPwd()
                )
        );
        Customer user = repository.findByEmailId(request.getEmailId())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(Customer user, String jwtToken) {
        Token token = Token.builder()
                .customer(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Customer user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getCustomerId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}
