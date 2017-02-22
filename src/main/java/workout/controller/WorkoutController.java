package workout.controller;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import workout.data.User;
import workout.data.Workout;
import workout.data.repositories.WorkoutRepository;
import workout.http.CrudResponseJson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mihael on 19.2.2017..
 */
@RestController
@RequestMapping(path="/api/workout")
public class WorkoutController {

    @Autowired
    WorkoutRepository workoutRepository;

    @RequestMapping(method=RequestMethod.GET)
    public Iterable<Workout> getAll() {
        return workoutRepository.findAll();
    }

    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<CrudResponseJson> create(@RequestBody CreateReq req, @AuthenticationPrincipal User user) {
        CrudResponseJson<Workout> res = new CrudResponseJson<>();
        Map<String, String> errors = validate(req);
        if (!errors.isEmpty()) {
            res.setErrors(errors);
            res.setMessage("Workout not created due to validation errors");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
        Workout workout = new Workout();
        BeanUtils.copyProperties(req, workout);
        if (user != null) { workout.setCreatedBy(user.getId()); }
        try {
            workoutRepository.save(workout);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            errors.put("name", "Workout name must be unique");
            res.setErrors(errors);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
        res.setEntity(workout);
        res.setMessage("Workout " + workout.getName() + " created");
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    public Map<String, String> validate(CreateReq req) {
        Map<String, String> errors = new HashMap<>();
        if (StringUtils.isBlank(req.getName())) { errors.put("name", "Name can't be empty"); }
        return errors;
    }

    @Data
    public static class CreateReq {
        private String name;
    }

}
