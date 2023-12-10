package ca.gbc.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer_uri;

    /***
    * This method is used to decode the JWT token
     * @return ReactiveJwtDecoder
    **/
    @Bean
    public ReactiveJwtDecoder JwtDecoder() {
        return ReactiveJwtDecoders.fromIssuerLocation(issuer_uri);
    }


    /***
     * This method is used to configure the security for the application
     * @return SecurityWebFilterChain
     */

    @Bean
    public SecurityWebFilterChain springSecurityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity.csrf(csrf -> csrf.disable())
                .authorizeExchange(exchange -> exchange.pathMatchers("/eureka/**")
                        .permitAll()
                        .anyExchange()
                        .authenticated())
                .oauth2ResourceServer(configurer -> configurer.jwt(jwt -> jwt.jwtDecoder(JwtDecoder())));

        return serverHttpSecurity.build();
    }
}
