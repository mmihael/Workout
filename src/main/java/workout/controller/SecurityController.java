package workout.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import workout.data.User;
import workout.data.repositories.UserRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mihae on 2.2.2017..
 */
@RestController
@RequestMapping(path="/api")
public class SecurityController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(path="/auth-status", method=RequestMethod.GET)
    ResponseEntity<ObjectNode> authStatus() {
        ObjectNode json = new ObjectMapper().createObjectNode();
        json.put("status", "authenticated");
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @RequestMapping(path="/register", method=RequestMethod.POST)
    ResponseEntity<ObjectNode> register(@RequestBody RegisterRequest req) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode res = mapper.createObjectNode();

        Map<String, String> errors = validateRegisterRequest(req);

        if (!errors.isEmpty()) {
            res.putPOJO("errors", errors);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        } else {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            req.setPassword(encoder.encode(req.getPassword()));
            User user = new User();
            BeanUtils.copyProperties(req, user);
            user.setEnabled(true);
            userRepository.save(user);
            res.putPOJO("user", user);
            res.put("message", "User " + user.getUsername() + " created");
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        }
    }

    private Map<String, String> validateRegisterRequest(RegisterRequest req) {
        Map<String, String> errors = new HashMap<>();

        if (req.getUsername() == null || StringUtils.isBlank(req.getUsername())) {
            errors.put("username", "Username is required");
        } else if (userRepository.findByUsername(req.getUsername()) != null) {
            errors.put("username", "Username already taken");
        }

        if (req.getEmail() == null || StringUtils.isBlank(req.getEmail())) {
            errors.put("email", "Email is required");
        } else if (!EmailValidator.getInstance().isValid(req.getEmail())) {
            errors.put("email", "Invalid email format");
        } else if (userRepository.findByEmail(req.getEmail()) != null) {
            errors.put("email", "Email already in use");
        }

        if (req.getPassword() == null || req.getPasswordRepeat() == null || StringUtils.isBlank(req.getPassword()) || StringUtils.isBlank(req.getPasswordRepeat())) {
            errors.put("password", "Passwords are required");
        } else if (!req.getPassword().equals(req.getPasswordRepeat())) {
            errors.put("password", "Passwords don't match");
        }

        return errors;
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
        private String passwordRepeat;
        private String email;
    }

//    @RequestMapping(path="/test2", method=RequestMethod.POST)
//    ResponseEntity<ObjectNode> login(@RequestBody LoginReq req) {
//        System.out.println(req.toString());
//        ObjectMapper mapper = new ObjectMapper();
//        ObjectNode json = mapper.createObjectNode();
//        json.put("response", "Value");
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
//        return new ResponseEntity<>(json, HttpStatus.OK);
//    }
//
//    @Data
//    public static class LoginReq {
//        String username;
//        String password;
//    }
}
