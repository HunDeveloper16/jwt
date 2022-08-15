package com.example.jwt.config.jwt;

import com.example.jwt.config.auth.PrincipalDetails;
import com.example.jwt.config.auth.PrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@RequiredArgsConstructor
public class FormLoginProvider implements AuthenticationProvider {

    private final PrincipalDetailsService principalDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        System.out.println("로그인 Provider 를 거침");

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        PrincipalDetails userDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(username);

        if(passwordEncoder.matches(password, userDetails.getPassword())){
            return new UsernamePasswordAuthenticationToken(userDetails, null);
        }else{
            throw new BadCredentialsException("잘못된 로그인 정보입니다.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {                    // token 타입에 따라서 언제 provider를 사용할지 조건을 지정할 수 있다.
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
