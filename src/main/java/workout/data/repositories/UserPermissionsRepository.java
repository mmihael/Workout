package workout.data.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import workout.data.UserPermissions;

/**
 * Created by mihael on 1.2.2017..
 */
public interface UserPermissionsRepository extends PagingAndSortingRepository<UserPermissions, Long> {

    UserPermissions findByUserId (long id);

}
