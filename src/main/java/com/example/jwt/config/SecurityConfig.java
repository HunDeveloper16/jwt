package com.example.jwt.config;

import com.example.jwt.config.auth.PrincipalDetailsService;
import com.example.jwt.config.jwt.FormLoginProvider;
import com.example.jwt.config.jwt.JwtAuthenticationFilter;
import com.example.jwt.config.jwt.JwtAuthorizationFilter;
import com.example.jwt.filter.MyFilter1;
import com.example.jwt.filter.MyFilter3;
import com.example.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration //IOC할수있게 만듦
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final UserRepository userRepository;
    private final PrincipalDetailsService principalDetailsService;
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FormLoginProvider customAuthenticationProvider() {
        return new FormLoginProvider(principalDetailsService,bCryptPasswordEncoder());
    }


    @Override
    public void configure(WebSecurity web) {
// h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        web
                .ignoring()
                .antMatchers("/h2-console/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception{
//        http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class); // Basic...filter가 실행되기전에 MyFilter를 건다.
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 사용하지 않음
                .and()
                .addFilter(corsFilter) // @CrossOrigin(인증X), 시큐리티 필터에 등록 인증(O)
                .formLogin().disable() // 기본적인 formLogin방식을 쓰지않음 -> JWT를 쓰려면 필수 위 세션허용,cors등록,formLogin방식을 꺼야함
                .httpBasic().disable() // httpbasic방식(기본인증방식) : authorization에 id,pw를 담아서 보내는 방식(여기서 id,pw가 노출될수 있음)
                                    // httpbasic <-> Bearer token 방식 : httpbasic은 id,pw를 담는다면 bearer토큰 방식은 토큰을 담고, 유효시간을 저장할수있음
                                    //  토큰도 노출될수있긴하나,id,pw로 로그인할때마다 서버쪽에서 계속 만들어주기 때문에 한번 노출이 됐다고 크게 위험하지가 않음. 또한 유효시간이 있음
                .addFilter(new JwtAuthenticationFilter(authenticationManager())) // AuthenticationManager을 던져줘야함(UsernamePasswordAuthenticationFilter를 상속하고 있기때문)
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();
    }


}