package ca.admin.delivermore.security;

import ca.admin.delivermore.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity{

    private Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    public static final String LOGOUT_URL = "/login";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        // Defining our Vaadin Flow based login view for the application
        setLoginView(http, LoginView.class, LOGOUT_URL);
        log.info("****SECURITY...here 0.1 - after new setLoginView");
    }
    
    // Then open anything for the public API for the application
    // Note: need the following in application.properties   vaadin.exclude-urls=/api/**
    @Order(20)
    @Bean
    SecurityFilterChain configurePublicApi(HttpSecurity http) throws Exception {
         http
                 .securityMatcher(AntPathRequestMatcher.antMatcher("/api/**"))
                 .csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/api/**")))
                 .authorizeHttpRequests(authz -> authz.anyRequest().permitAll());
        log.info("****SECURITY...here 1.1 - after new public api setup");
        return http.build();
    }
    
}
