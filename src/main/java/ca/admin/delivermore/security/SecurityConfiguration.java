package ca.admin.delivermore.security;

import ca.admin.delivermore.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    public static class RestSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable()
                    // make sure to only make rules for /api/**  - everything else will be defined in WebSecurityConfigurationAdapter
                    .antMatcher("/giftcardcreate/**").authorizeRequests().anyRequest().permitAll();
                    //.antMatcher("/giftcardcreate").authorizeRequests().anyRequest().authenticated();

                    // these requests come with an "Authorization" header with a token
                    // Use this token to create an Authentication for this request.
                    //.and().addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        }
    }

    /**
     * FOR ANYTHING EXCEPT those patterns listed above
     * for any UI request, redirect to /login if user is not yet authenticated.
     */
    @Configuration
    @Order(2)
    public static class WebSecurityConfigurationAdapter extends VaadinWebSecurityConfigurerAdapter {

        @Value("${security.enabled:true}")
        private boolean securityEnabled;
        private Logger log = LoggerFactory.getLogger(WebSecurityConfigurationAdapter.class);

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            super.configure(http);
            log.info("configure http: securityEnabled:" + securityEnabled);

            if(securityEnabled){
                setLoginView(http, LoginView.class, LOGOUT_URL);
            }
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            super.configure(web);
            log.info("configure web: securityEnabled:" + securityEnabled);
            if(securityEnabled){
                web.ignoring().antMatchers("/images/*.png");
            }else{
                web.ignoring().antMatchers("/**");
            }

        }

    }



}
