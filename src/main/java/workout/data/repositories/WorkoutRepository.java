package workout.data.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import workout.data.Permission;
import workout.data.Workout;

/**
 * Created by mihael on 1.2.2017..
 */
public interface WorkoutRepository extends PagingAndSortingRepository<Workout, Long> {

    Workout findByName(String name);

}
