package com.springbootlibrary.config;

//Make this class a config file
import com.okta.spring.boot.oauth.Okta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

@Configuration
//@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //Disable cross site request forgery
        http.csrf().disable();

        //***This will cause Okta to verify/validate the token before the endpoint/service uses the token***
        // Protect endpoints at /api/<type>/secure
        //Had to replace authorizeRequests (deprecated) with authorizeHttpRequests
        //Had to replace antMatchers (not available) with requestMatchers
        //"/api/reviews/search/findByBookId",
        // "/api/reviews/search/findByBookId"
        http.authorizeHttpRequests(configurer -> configurer
                                .requestMatchers("/api/books",
                                        "/api/books/*?",
                                        "/api/messages/**",
                                        "/api/reviews",
                                        "/api/checkouts",
                                        "/api/histories/search/**",
                                        "/api/reviews/search/**",
                                        "api/messages/search/findByClosed").permitAll()
                                .requestMatchers("/api/books/secure/**",
                                        "/api/reviews/secure/**",
                                        "/api/messages/secure/**",
                                        "/api/admin/secure/**")
                                .authenticated())
                                .oauth2ResourceServer()
                                .jwt();

        //Add CORS filters to API endpoints
        http.cors();

        //Add content to negotiation strategy
        http.setSharedObject(ContentNegotiationStrategy.class, new HeaderContentNegotiationStrategy());

        //Force a non-empty body for 401 to make the response user friendly
        //Had issues with this. Had to add a newer version of Okta (3.0.2) to Maven along with a
        //spring-boot-starter-validation
        Okta.configureResourceServer401ResponseBody(http);

        //Security configuration is using the build design pattern so use that for the return.
        return http.build();
    }
}
