package workout.data.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import workout.data.Permission;
import workout.data.User;

/**
 * Created by mihael on 1.2.2017..
 */
public interface PermissionRepository extends PagingAndSortingRepository<Permission, Long> {

    Permission findByName(String name);

}
