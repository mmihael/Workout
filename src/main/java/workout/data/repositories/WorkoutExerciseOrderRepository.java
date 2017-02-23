package workout.data.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import workout.data.WorkoutExerciseOrder;

import java.util.List;

/**
 * Created by mihael on 1.2.2017..
 */
public interface WorkoutExerciseOrderRepository extends PagingAndSortingRepository<WorkoutExerciseOrder, Long> {

    public List<WorkoutExerciseOrder> findByWorkout(long workout);

}
