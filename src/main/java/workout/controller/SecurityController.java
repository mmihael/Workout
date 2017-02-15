package workout.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mihae on 2.2.2017..
 */
@RestController
public class SecurityController {

    @Autowired
    AuthenticationManager authenticationManager;

    @RequestMapping(path="/test2", method=RequestMethod.POST)
    ResponseEntity<ObjectNode> login(@RequestBody LoginReq req) {
        System.out.println(req.toString());
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        json.put("response", "Value");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @Data
    public static class LoginReq {
        String username;
        String password;
    }
}
