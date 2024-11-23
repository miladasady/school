package isc.nps.school.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    //private final JwtTokenUtil jwtTokenUtil;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig( JwtRequestFilter jwtRequestFilter) {
        //this.jwtTokenUtil = jwtTokenUtil;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/classes/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/classes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/classes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/classes/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
