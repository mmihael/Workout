package workout.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import workout.data.User;
import workout.data.repositories.UserRepository;

import java.util.Arrays;

/**
 * Created by mihae on 2.2.2017..
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication != null) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            System.out.println(token);
            System.out.println(token.getPrincipal().toString());
            System.out.println(token.getCredentials().toString());
            User user = userRepository.findByUsername(token.getPrincipal().toString());
            BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
            if (user != null && bcrypt.matches(token.getCredentials().toString(), user.getPassword())) {
                return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), Arrays.asList(() -> "USER"));
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class == aClass;
    }
}