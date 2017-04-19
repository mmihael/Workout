package workout.data.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import workout.data.Exercise;

/**
 * Created by mihael on 1.2.2017..
 */
public interface ExerciseRepository extends PagingAndSortingRepository<Exercise, Long> {

    Iterable<Exercise> findByCreatedBy(long createdBy);

    Exercise findByName(String name);

}
