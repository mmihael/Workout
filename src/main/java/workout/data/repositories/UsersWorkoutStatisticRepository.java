package workout.data.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import workout.data.UsersWorkoutStatistic;

import java.util.List;

/**
 * Created by mihael on 1.2.2017..
 */
public interface UsersWorkoutStatisticRepository extends PagingAndSortingRepository<UsersWorkoutStatistic, Long> {

    List<UsersWorkoutStatistic> findByUsersWorkout(long id);

}
