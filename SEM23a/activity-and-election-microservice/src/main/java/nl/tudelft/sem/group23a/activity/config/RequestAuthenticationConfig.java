package nl.tudelft.sem.group23a.activity.config;

import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.activity.authentication.JwtAuthenticationEntryPoint;
import nl.tudelft.sem.group23a.activity.authentication.JwtRequestFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * The type Web Security Config.
 */
@Configuration
@RequiredArgsConstructor
public class RequestAuthenticationConfig extends WebSecurityConfigurerAdapter {

    private final transient JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final transient JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.PUT, "/elections/{hoaId}").permitAll()
                .antMatchers(HttpMethod.PUT, "/elections/{hoaId}/application").permitAll()
                .antMatchers(HttpMethod.PUT, "/elections/{hoaId}/vote").permitAll()
                .antMatchers(HttpMethod.PUT, "/activities/hoa/{hoaId}/proposals/{proposalId}/vote").permitAll()
                .antMatchers(HttpMethod.PUT, "/activities/hoa/{hoaId}/gatherings/{gatheringId}/vote").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}