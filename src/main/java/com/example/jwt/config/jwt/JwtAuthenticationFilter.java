package com.example.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwt.config.auth.PrincipalDetails;
import com.example.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있음.
// /login 요청해서 username, password 전송하면 (post)
// UsernamePasswordAuthenticationFilter가 동작을 함.
// 원래 UsernamePasswordAuthenticationFilter는 formLogin()으로 작동하지만 formLogin()을 꺼뒀기 때문에 이 클래스를 securitConfig()에 add해서 추가해준다.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

   // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter: 로그인 시도중");

        // 1.username,password 받아서
        try {
//            BufferedReader br = request.getReader();
//            String input = null;
//            while((input = br.readLine()) != null){
//                System.out.println(input);  // stream안에 username,password가 담겨있음

            ObjectMapper om = new ObjectMapper();  //json으로 받아온 데이터를 처리하기 위하여 ObjectMapper생성
            User user = om.readValue(request.getInputStream(), User.class);   // json받아온 user데이터를 각각 파싱
            System.out.println(user);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());  // 원래는 forimLogin()이 해주지만 JWT를 쓰기위해서 따로생성

            //PrincipalDetailsService의 loadUserByUsername()함수가 실행된 후 정상이면 authentication이 리턴됨.
            // DB에 있는 username과 password가 일치한다.
            Authentication authentication =
                    authenticationManager.authenticate(authenticationToken); //authenticationManager에 토큰을 넣어서 던지면 인증을 해줌 -> 인증이되면 authentication에 담김
                                                                                //여기서 authentication에 로그인 정보가 담김

            // 로그인이 되었다는 뜻.
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인 완료됨:"+principalDetails.getUser().getUsername());  //콘솔에 찍힌다는건 로그인이 되었다는 뜻

            // authentication 객체가 session 영역에 저장을 해야하고 그 방법이 return 해주면 됨.
            // 리턴의 이유는 권한 관리를 security 가 대신 해주기 때문에 편하려고 하는것
            // 굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없음. 그러나 단지 권한 처리때문에 session을 넣어 준다.

            return authentication;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // attemptAuthenitcation실행 후 인증이 정상적으로 되었으면 successfulAuthenticatino 함수가 실행된다.
    // JWT 토큰을 만들어서 request요청한 사용자에게 JWT 토큰을 response 해주면 된다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // JWTToken 생성
        //RSA 방식이 아닌 Hash암호방식
        String jwtToken = JWT.create()
                        .withSubject("cos토큰")
                        .withExpiresAt(new Date(System.currentTimeMillis()+(60000*10)))
                        .withClaim("id",principalDetails.getUser().getId())
                        .withClaim("username",principalDetails.getUser().getUsername())
                        .sign(Algorithm.HMAC512("cos"));    // secret키 설정 여기선 cos로 설정

        System.out.println("successfulAuthentication이 실행됨: 인증이 완료되었다는 뜻임 ");
       response.addHeader("Authorization", "Bearer " + jwtToken);
    }
}
