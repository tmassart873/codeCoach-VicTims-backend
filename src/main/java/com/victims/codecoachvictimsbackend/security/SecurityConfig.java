package com.victims.codecoachvictimsbackend.security;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.stream.Collectors;

@KeycloakConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile("!test")
class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    /**
     * This is meant to add the role mapper to keycloak allowing you to map keycloak roles to your own features
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(collection ->
                collection.stream()
                        .map(keycloakRole -> Role.valueOf(keycloakRole.getAuthority().toUpperCase()))
                        .flatMap(role -> role.getFeatures().stream())
                        .map(feature -> new SimpleGrantedAuthority(feature.name()))
                        .collect(Collectors.toSet())
        );
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    public KeycloakConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    /**
     * Spring security configuration
     * This is the default security configuration and shouldn't be changed
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);

        http
                .cors()
                .and()
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .anyRequest()
                .authenticated().and().csrf().disable();
    }

    @Bean
    CorsFilter corsFilter() {

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setMaxAge(8000L);
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedOrigin("http://localhost:4200");
        corsConfig.addAllowedOrigin("http://localhost:8081/swagger-ui/index.html");
        corsConfig.addAllowedOrigin("https://codecoach-victims.netlify.app");
        corsConfig.addAllowedOrigin("https://codecoach-victims-dev.netlify.app");
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedHeader("Access-Control-Allow-Origin");
        corsConfig.addAllowedMethod("GET");
        corsConfig.addAllowedMethod("POST");
        corsConfig.addAllowedMethod("PUT");
        corsConfig.addAllowedMethod("PATCH");
        corsConfig.addAllowedMethod("DELETE");


        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsFilter(source);
    }

    /**
     * Configure here the endpoints that need to be anonymously available
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.POST, "/users")
                .antMatchers(HttpMethod.GET, "/swagger-ui/index.html")
                .antMatchers(HttpMethod.POST,"/error")
                .antMatchers(HttpMethod.GET, "/v3/**/*");

    }


}
