package com.parent.ws.app.security.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parent.ws.app.SpringApplicationContext;
import com.parent.ws.app.security.SecurityConstants;
import com.parent.ws.app.service.protocols.UserService;
import com.parent.ws.app.shared.dto.UserDto;
import com.parent.ws.app.ui.models.request.UserLoginRequestModel;

import org.springframework.security.core.Authentication;
import org.springframework.core.env.Environment;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private Environment environment;

    public AuthenticationFilter(AuthenticationManager authenticationManager, Environment environment) {
        this.authenticationManager = authenticationManager;
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
            HttpServletResponse res)
            throws AuthenticationException {

        try {
            UserLoginRequestModel credentials = new ObjectMapper().readValue(req.getInputStream(),
                    UserLoginRequestModel.class);

            String userEmail = credentials.getEmail();
            String userPlainTextPassword = credentials.getPassword();

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userEmail, userPlainTextPassword, new ArrayList<>());

            return authenticationManager.authenticate(authenticationToken);

        } catch (IOException error) {
            throw new RuntimeException(error);
        }

    }

    public void successfulAuthentication(HttpServletRequest req,
            HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {

        User principal = ((User) auth.getPrincipal());
        String userName = principal.getUsername();

        Date tokenExpirationDate = new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_DATE);

        String token = Jwts.builder()
                .setSubject(userName)
                .setExpiration(tokenExpirationDate)
                .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
                .compact();

        UserService userService = ((UserService) SpringApplicationContext.getBean("userServiceImpl"));
        UserDto userDto = userService.getUser(userName);

        String tokenWithPrefix = SecurityConstants.TOKEN_PREFIX + token;
        res.addHeader(SecurityConstants.HEADER_STRING, tokenWithPrefix);
        res.addHeader("UserID", userDto.getUserId());

    }

}
