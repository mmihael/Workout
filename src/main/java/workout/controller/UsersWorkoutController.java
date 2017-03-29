package workout.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import workout.data.*;
import workout.data.repositories.*;
import workout.http.CrudResponseJson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * Created by mihael on 19.2.2017..
 */
@RestController
@RequestMapping(path="/api/user/workout")
public class UsersWorkoutController {

    @Autowired
    WorkoutRepository workoutRepository;

    @Autowired
    ExerciseRepository exerciseRepository;

    @Autowired
    WorkoutExerciseOrderRepository workoutExerciseOrderRepository;

    @Autowired
    UsersWorkoutStatisticRepository usersWorkoutStatisticRepository;

    @Autowired
    UsersWorkoutRepository usersWorkoutRepository;

    @RequestMapping(path="/{id:^[1-9]+[0-9]*$}", method=RequestMethod.GET)
    public ResponseEntity<ObjectNode> getUsersWorkout(@PathVariable(name="id") long id) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        UsersWorkout usersWorkout = usersWorkoutRepository.findOne(id);
        UsersWorkout lastWorkout = usersWorkoutRepository.findTop1ByWorkoutAndUserAndCreatedAtBeforeOrderByCreatedAtDesc(
            usersWorkout.getWorkout(),
            usersWorkout.getUser(),
            usersWorkout.getCreatedAt()
        );
        List<UsersWorkoutStatistic> usersWorkoutStatistics = usersWorkoutStatisticRepository.findByUsersWorkout(usersWorkout.getId());
        Workout workout = workoutRepository.findOne(usersWorkout.getWorkout());
        List<WorkoutExerciseOrder> workoutExerciseOrder = workoutExerciseOrderRepository.findByWorkout(workout.getId());
        Iterable<Exercise> exercises = exerciseRepository
                .findAll(workoutExerciseOrder.stream().map(WorkoutExerciseOrder::getExercise).collect(Collectors.toList()));
        Map<Long, Exercise> exercisesMap = new HashMap<>();
        for (Exercise exercise : exercises) { exercisesMap.put(exercise.getId(), exercise); }
        json.putPOJO("usersWorkout", usersWorkout);
        json.putPOJO("workout", workout);
        json.putPOJO("exercises", exercisesMap);
        json.putPOJO("workoutExerciseOrder", workoutExerciseOrder.stream()
                .sorted((a, b) -> (a.getOrder() > b.getOrder()) ? 1 : (a.getOrder() < b.getOrder()) ? -1 : 0)
                .collect(Collectors.toMap(e -> e.getId(), e -> e))
        );
        json.putPOJO("usersWorkoutStatistics", usersWorkoutStatistics.stream().collect(Collectors.toMap(e -> e.getWorkoutExerciseOrder(), e -> e)));
        if (lastWorkout != null) {
            List<UsersWorkoutStatistic> lastStatistics = usersWorkoutStatisticRepository.findByUsersWorkout(lastWorkout.getId());
            if (CollectionUtils.isNotEmpty(lastStatistics)) {
                json.putPOJO("lastStatistics", lastStatistics.stream().collect(Collectors.toMap(e -> e.getWorkoutExerciseOrder(), e -> e)));
            }
        }
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<CreateUsersWorkoutResponse> createUsersWorkout(@RequestBody CreateUsersWorkoutRequest req, @AuthenticationPrincipal User user) {
        CreateUsersWorkoutResponse res = new CreateUsersWorkoutResponse();
        UsersWorkout usersWorkout = null;
        Workout workout = workoutRepository.findOne(req.getWorkout());
        if (workout != null) {
            usersWorkout = new UsersWorkout();
            usersWorkout.setWorkout(workout.getId());
            usersWorkout.setUser(user.getId());
            usersWorkout.setCreatedBy(user.getId());
            usersWorkoutRepository.save(usersWorkout);
        }

        List<UsersWorkoutStatistic> usersWorkoutStatistics = new ArrayList<>();

        if (usersWorkout != null) {
            List<WorkoutExerciseOrder> workoutExerciseOrder = workoutExerciseOrderRepository.findByWorkout(workout.getId());
            for (WorkoutExerciseOrder item : workoutExerciseOrder) {
                UsersWorkoutStatistic usersWorkoutStatistic = new UsersWorkoutStatistic();
                usersWorkoutStatistic.setUsersWorkout(usersWorkout.getId());
                usersWorkoutStatistic.setWorkoutExerciseOrder(item.getId());
                usersWorkoutStatistic.setCreatedBy(user.getId());
                usersWorkoutStatistics.add(usersWorkoutStatistic);
            }
            usersWorkoutStatisticRepository.save(usersWorkoutStatistics);
        }

        res.setUsersWorkout(usersWorkout);
        res.setUsersWorkoutStatistics(usersWorkoutStatistics);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Data
    public static class CreateUsersWorkoutRequest {
        long workout;
    }

    @Data
    public static class CreateUsersWorkoutResponse {
        UsersWorkout usersWorkout;
        List<UsersWorkoutStatistic> usersWorkoutStatistics;
    }

    @RequestMapping(path="/statistic/{id:^[1-9]+[0-9]*$}", method=RequestMethod.PATCH)
    public ResponseEntity<UsersWorkoutStatistic> updateWorkoutStatistic(@RequestBody PatchUsersWorkoutStatisticRequest req, @PathVariable(name="id") long id, @AuthenticationPrincipal User user) {
        UsersWorkoutStatistic usersWorkoutStatistic = usersWorkoutStatisticRepository.findOne(id);
        BeanUtils.copyProperties(req, usersWorkoutStatistic);
        usersWorkoutStatisticRepository.save(usersWorkoutStatistic);
        return new ResponseEntity<>(usersWorkoutStatistic, HttpStatus.OK);
    }

    @Data
    public static class PatchUsersWorkoutStatisticRequest {
        Double weight;
        Long reps;
    }

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<ArrayNode> getUsersWorkouts(@AuthenticationPrincipal User user) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode json = mapper.createArrayNode();

        List<UsersWorkout> usersWorkouts = usersWorkoutRepository.findByUserOrderByCreatedAtDesc(user.getId());
        Map<Long, Workout> workouts = StreamSupport.stream(workoutRepository
                .findAll(usersWorkouts.stream().map(UsersWorkout::getWorkout).collect(Collectors.toList())).spliterator()
                , false
        ).collect(Collectors.toMap(Workout::getId, e -> e));

        for (UsersWorkout usersWorkout : usersWorkouts) {
            ObjectNode object = mapper.createObjectNode();
            object.put("id", usersWorkout.getId());
            object.put("workoutName", workouts.get(usersWorkout.getWorkout()).getName());
            object.put("createdAt", usersWorkout.getCreatedAt().getTime());
            json.add(object);
        }

        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @RequestMapping(path="/{id:^[1-9]+[0-9]*$}", method=RequestMethod.DELETE)
    public ResponseEntity<CrudResponseJson> deleteUsersWorkout(@PathVariable(name="id") long id, @AuthenticationPrincipal User user) {
        CrudResponseJson res = new CrudResponseJson();
        UsersWorkout usersWorkout = usersWorkoutRepository.findTop1ByIdAndUser(id, user.getId());
        if (usersWorkout != null) {
            List<UsersWorkoutStatistic> usersWorkoutStatistics = usersWorkoutStatisticRepository.findByUsersWorkout(usersWorkout.getId());
            if (CollectionUtils.isNotEmpty(usersWorkoutStatistics)) {
                for (UsersWorkoutStatistic usersWorkoutStatistic : usersWorkoutStatistics) {
                    usersWorkoutStatistic.setDeleted(true);
                }
                usersWorkoutStatisticRepository.save(usersWorkoutStatistics);
            }
            usersWorkout.setDeleted(true);
            usersWorkoutRepository.save(usersWorkout);
            res.setMessage("User's workout deleted");
        } else {
            res.setStatus(CrudResponseJson.CrudStatus.ERROR.getId());
            res.setMessage("Failed deleting workout");
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @RequestMapping(path="/latest/{id:^[1-9]+[0-9]*$}", method=RequestMethod.GET)
    public ResponseEntity<ObjectNode> getLatestUserWorkouts(@PathVariable(name="id") long workoutId, @AuthenticationPrincipal User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        Workout workout = workoutRepository.findOne(workoutId);
        json.putPOJO("workout", workout);
        List<UsersWorkout> usersWorkouts = usersWorkoutRepository.findTop10ByUserAndWorkoutOrderByCreatedAtDesc(user.getId(), workoutId);
        Map<Long, List<UsersWorkoutStatistic>> usersWorkoutStatistics = usersWorkoutStatisticRepository
                .findByUsersWorkoutIn(usersWorkouts.stream().map(UsersWorkout::getId).collect(Collectors.toList()))
                .stream().collect(Collectors.groupingBy(UsersWorkoutStatistic::getUsersWorkout));
        ArrayNode arrayNode = mapper.createArrayNode();
        for (UsersWorkout usersWorkout : usersWorkouts) {
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.putPOJO("usersWorkout", usersWorkout);
            objectNode.putPOJO("usersWorkoutStatistics", usersWorkoutStatistics.get(usersWorkout.getId()));
            arrayNode.add(objectNode);
        }
        json.putPOJO("statistics", arrayNode);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

}
