package com.study.web2.controller;

import com.study.web2.dto.CommonRespDto;
import com.study.web2.dto.user.LoginReqDto;
import com.study.web2.dto.user.JwtRespDto;
import com.study.web2.security.jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/login")
    public JwtRespDto login(@RequestBody LoginReqDto loginReqDto, HttpServletResponse response) {
        JwtRespDto jwtRespDto = new JwtRespDto();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(
                new UsernamePasswordAuthenticationToken(loginReqDto.getUsername(), loginReqDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.createToken(authentication);

        Cookie cookie = new Cookie("accessToken", jwt);
        cookie.setMaxAge(24 * 60 * 60);
//        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        jwtRespDto.setUsername(jwtUtils.getUsernameFromJwtToken(jwt));
        return jwtRespDto;
    }

    @PostMapping("/logout")
    public CommonRespDto logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setMaxAge(0);
//        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        SecurityContextHolder.clearContext();
        return new CommonRespDto();
    }

    @GetMapping("/check")
    public JwtRespDto check(@CookieValue(required = false) String accessToken) {
        JwtRespDto jwtRespDto = new JwtRespDto();
        String username = jwtUtils.getUsernameFromJwtToken(accessToken);
        jwtRespDto.setUsername(username);
        return jwtRespDto;
    }
}
