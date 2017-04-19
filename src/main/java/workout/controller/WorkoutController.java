package workout.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import workout.data.Exercise;
import workout.data.User;
import workout.data.Workout;
import workout.data.WorkoutExerciseOrder;
import workout.data.repositories.ExerciseRepository;
import workout.data.repositories.WorkoutExerciseOrderRepository;
import workout.data.repositories.WorkoutRepository;
import workout.http.CrudResponseJson;

import java.util.ArrayList;
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

    @Autowired
    ExerciseRepository exerciseRepository;

    @Autowired
    WorkoutExerciseOrderRepository workoutExerciseOrderRepository;

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<ArrayNode> getAll(@AuthenticationPrincipal User user) {
        ObjectMapper om = new ObjectMapper();
        ArrayNode resBody = om.createArrayNode();

        Iterable<Workout> workouts = workoutRepository.findByCreatedBy(user.getId());
        Iterable<Exercise> exercises = exerciseRepository.findByCreatedBy(user.getId());

        Map<Long, Exercise> exercisesMap = new HashMap<>();
        for (Exercise exercise : exercises) { exercisesMap.put(exercise.getId(), exercise); }

        for (Workout workout : workouts) {
            ObjectNode workoutData = om.createObjectNode();
            workoutData.putPOJO("workout", workout);
            List<WorkoutExerciseOrder> workoutExercises = workoutExerciseOrderRepository.findByWorkout(workout.getId());
            ArrayNode exerciseArray = workoutData.putArray("exercises");
            for (WorkoutExerciseOrder weo : workoutExercises) {
                ObjectNode on = om.createObjectNode();
                on.put("name", exercisesMap.get(weo.getExercise()).getName());
                on.put("id", weo.getExercise());
                on.put("order", weo.getOrder());
                exerciseArray.add(on);
            }
            resBody.add(workoutData);
        }
        return new ResponseEntity<>(resBody, HttpStatus.OK);
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
        List<WorkoutExerciseOrder> workoutExerciseOrder = new ArrayList<>();
        for (CreateReq.WorkoutExerciseOrder reqExercise : req.getWorkoutExerciseOrder()) {
            WorkoutExerciseOrder newExerciseOrder = new WorkoutExerciseOrder();
            BeanUtils.copyProperties(reqExercise, newExerciseOrder);
            newExerciseOrder.setWorkout(workout.getId());
            workoutExerciseOrder.add(newExerciseOrder);
        }
        workoutExerciseOrderRepository.save(workoutExerciseOrder);
        res.setEntity(workout);
        res.setMessage("Workout " + workout.getName() + " created");
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    public Map<String, String> validate(CreateReq req) {
        Map<String, String> errors = new HashMap<>();
        if (StringUtils.isBlank(req.getName())) { errors.put("name", "Name can't be empty"); }
        if (CollectionUtils.isEmpty(req.getWorkoutExerciseOrder())) { errors.put("workoutExerciseOrder", "Exercises can't be empty"); }
        else {
            for (CreateReq.WorkoutExerciseOrder exercise : req.getWorkoutExerciseOrder()) {
                if (exercise.getExercise() == 0 || exercise.getOrder() == 0) {
                    errors.put("workoutExerciseOrder", "Exercise contains invalid data");
                    break;
                }
            }
        }
        return errors;
    }

    @Data
    public static class CreateReq {
        private String name;
        private List<WorkoutExerciseOrder> workoutExerciseOrder;

        @Data
        public static class WorkoutExerciseOrder {
            long exercise;
            long order;
        }
    }

}
