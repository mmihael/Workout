package workout.data.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import workout.data.UsersWorkout;
import workout.data.Workout;

import java.util.List;

/**
 * Created by mihael on 1.2.2017..
 */
public interface UsersWorkoutRepository extends PagingAndSortingRepository<UsersWorkout, Long> {

    List<UsersWorkout> findByUserAndWorkout(long userId, long workoutId);

    List<UsersWorkout> findByUserOrderByCreatedAtDesc(long userId);

}
