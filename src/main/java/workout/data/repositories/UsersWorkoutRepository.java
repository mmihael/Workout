package workout.data.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import workout.data.UsersWorkout;
import workout.data.Workout;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by mihael on 1.2.2017..
 */
public interface UsersWorkoutRepository extends PagingAndSortingRepository<UsersWorkout, Long> {

    List<UsersWorkout> findByUserAndWorkout(long userId, long workoutId);

    List<UsersWorkout> findByUserOrderByCreatedAtDesc(long userId);

    UsersWorkout findTop1ByIdAndUser(long id, long user);

    UsersWorkout findTop1ByWorkoutAndUserAndCreatedAtBeforeOrderByCreatedAtDesc(long workoutId, long userId, Timestamp createdAt);

}
