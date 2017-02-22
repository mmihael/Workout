package workout.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import workout.data.Permission;
import workout.data.User;
import workout.data.UserPermissions;
import workout.data.repositories.PermissionRepository;
import workout.data.repositories.UserPermissionsRepository;
import workout.data.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mihael on 16.2.2017..
 */
@RestController
@RequestMapping("/dev")
public class DevController {

    @Autowired
    UserPermissionsRepository userPermissionsRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping("/user")
    public User getUser() {
        return userRepository.findByUsername("demo");
    }

    @RequestMapping("/permission")
    public List<UserPermissions> getPermission() {
        return permissionRepository.findByName("ADMIN").getUserPermissions();
    }

    @RequestMapping("/up")
    public UserPermissions getUserPermissions() {
        return userPermissionsRepository.findByUserId(1);
    }

    @RequestMapping("/up2")
    public List<Permission> getUserPermissions2() {
        return userRepository.findByUsername("demo").
                getPermissions().stream().map(UserPermissions::getPermission).collect(Collectors.toList());
    }

}
