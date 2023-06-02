package ca.admin.delivermore.security;

import ca.admin.delivermore.collector.data.service.DriversRepository;
import ca.admin.delivermore.collector.data.tookan.Driver;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final DriversRepository driversRepository;

    @Autowired
    public UserDetailsServiceImpl(DriversRepository driversRepository) {
        this.driversRepository = driversRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Driver user = driversRepository.findDriverByEmailAndLoginAllowed(username, Boolean.TRUE);
        if (user == null) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getHashedPassword(),
                    getAuthorities(user));
        }
    }



    private static List<GrantedAuthority> getAuthorities(Driver user) {
        return user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

    }

}
