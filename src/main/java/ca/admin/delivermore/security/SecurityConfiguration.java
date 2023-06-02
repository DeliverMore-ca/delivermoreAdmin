package ca.admin.delivermore.security;

import ca.admin.delivermore.views.login.LoginView;
import ca.admin.delivermore.views.report.PeriodSummaryView;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurityConfigurerAdapter {

    private Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Value("${security.enabled:true}")
    private boolean securityEnabled;

    public static final String LOGOUT_URL = "/login";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if(securityEnabled){
            super.configure(http);
            setLoginView(http, LoginView.class, LOGOUT_URL);
        }
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        if(securityEnabled){
            web.ignoring().antMatchers("/images/*.png");
            //web.ignoring().antMatchers("/.well-known/acme-challenge/*");
        }else{
            web.ignoring().antMatchers("/**");
        }
    }
}
