package ca.admin.delivermore.security;

import ca.admin.delivermore.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    public static final String LOGOUT_URL = "/login";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * ONLY FOR listed endpoints in the antMatcher pattern below such as giftcardcreate
     */
    @Configuration
    @Order(1)
    public static class RestSecurityConfigurationAdapter extends VaadinWebSecurity {

        private Logger log = LoggerFactory.getLogger(WebSecurityConfigurationAdapter.class);

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            log.info("****SECURITY...here 1 - before new code for GC");

            //testing
            //http.rememberMe().alwaysRemember(false);

            http.cors(withDefaults()).csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.ignoringRequestMatchers("/giftcardcreate/**"));
            log.info("****SECURITY...here 1.1 - after new code for GC");
            
            // Configure your static resources with public access before calling
            // super.configure(HttpSecurity) as it adds final anyRequest matcher

            /*
            http.cors().and().csrf().disable().authorizeHttpRequests(auth -> {
                auth.requestMatchers(new AntPathRequestMatcher("/giftcardcreate/**")).permitAll();
            });
            */

            /* from sample
            http.authorizeHttpRequests(auth -> {
                auth.requestMatchers(new AntPathRequestMatcher("/admin-only/**"))
                        .hasAnyRole("admin")
                        .requestMatchers(new AntPathRequestMatcher("/public/**"))
                        .permitAll();
            });

             */

            log.info("****SECURITY...here 1.2 - before super");
            super.configure(http);
            log.info("****SECURITY...here 1.3 - after super");
            
            // This is important to register your login view to the
            // view access checker mechanism:
            setLoginView(http, LoginView.class, LOGOUT_URL);
            log.info("****SECURITY...here 1.4 - after setLoginView");
            //end testing

            //test 0 below
            //http.authorizeHttpRequests(auth -> auth.requestMatchers(new AntPathRequestMatcher("/giftcardcreate/**")).permitAll());
            //test 0 above

            //original below
            /*
            http.authorizeHttpRequests().requestMatchers("/giftcardcreate/**").permitAll();
                    // make sure to only make rules for /api/**  - everything else will be defined in WebSecurityConfigurationAdapter
                    //.antMatcher("/giftcardcreate/**").authorizeRequests().anyRequest().permitAll();
                    //.antMatcher("/giftcardcreate").authorizeRequests().anyRequest().authenticated();

                    // these requests come with an "Authorization" header with a token
                    // Use this token to create an Authentication for this request.
                    //.and().addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

             */
        }
    }

    /**
     * FOR ANYTHING EXCEPT those patterns listed above
     * for any UI request, redirect to /login if user is not yet authenticated.
     */
    @Configuration
    @Order(2)
    public static class WebSecurityConfigurationAdapter extends VaadinWebSecurity {

        @Value("${security.enabled:true}")
        private boolean securityEnabled;
        private Logger log = LoggerFactory.getLogger(WebSecurityConfigurationAdapter.class);

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            super.configure(http);
            log.info("****SECURITY...here 2");
            log.info("configure http: securityEnabled:" + securityEnabled);

            if(securityEnabled){
                setLoginView(http, LoginView.class, LOGOUT_URL);
            }

            /*
            //testing
            http.rememberMe().alwaysRemember(false);

            // Configure your static resources with public access before calling
            // super.configure(HttpSecurity) as it adds final anyRequest matcher
            http.authorizeHttpRequests(auth -> {
                auth.requestMatchers(new AntPathRequestMatcher("/admin-only/**"))
                        .hasAnyRole("admin")
                        .requestMatchers(new AntPathRequestMatcher("/public/**"))
                        .permitAll();
            });
            super.configure(http);

            // This is important to register your login view to the
            // view access checker mechanism:
            setLoginView(http, LoginView.class, LOGOUT_URL);
            //end testing
             */

        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            super.configure(web);
            log.info("****SECURITY...here 3");
            log.info("configure web: securityEnabled:" + securityEnabled);

            if(securityEnabled){
                web.ignoring().requestMatchers("/images/*.png");
                //web.ignoring().antMatchers("/images/*.png");
            }else{
                web.ignoring().requestMatchers("/**");
                //web.ignoring().antMatchers("/**");
            }

        }

    }



}
