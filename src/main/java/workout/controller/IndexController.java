package workout.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import workout.data.User;
import workout.data.repositories.UserRepository;

import java.util.List;

/**
 * Created by mihae on 31.1.2017..
 */
@RestController
public class IndexController {

    @RequestMapping("/test")
    public String indexAction() {
        return "Welcome to the index action!";
    }

    @RequestMapping("/test3")
    public String testAction() {
        return "OMG";
    }

}
