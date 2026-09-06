package hr.algebra.concertcrew.config;

import hr.algebra.concertcrew.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**")
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/concerts/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/concerts/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/concerts/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/concerts/**").hasRole("ADMIN")
                    .requestMatchers("/api/database/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp.policyDirectives(
                                "default-src 'self'; frame-ancestors 'none'")))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain mvcFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(new NegatedRequestMatcher(
                        PathPatternRequestMatcher.withDefaults().matcher("/api/**")))            .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/auth/**", "/css/**", "/js/**", "/webjars/**"
                        ).permitAll()
                        .requestMatchers(
                                "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**"
                        ).hasRole("ADMIN")
                .requestMatchers(
                    "/concerts/new", "/concerts/edit/**", "/concerts/delete/**"
                ).hasRole("ADMIN")
                .requestMatchers("/concerts/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .defaultSuccessUrl("/concerts", true)
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
                .headers(headers -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                        .contentSecurityPolicy(csp -> csp.policyDirectives(
                                "default-src 'self'; style-src 'self' 'unsafe-inline'; "
                                        + "script-src 'self' 'unsafe-inline'; img-src 'self' data:; "
                                        + "font-src 'self'; frame-ancestors 'self'"))
                        .referrerPolicy(ref -> ref.policy(
                                org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN))
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
