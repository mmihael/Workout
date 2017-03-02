package workout.data.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import workout.data.User;

/**
 * Created by mihael on 1.2.2017..
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

}
