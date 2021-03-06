package workout.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by mihael on 1.2.2017..
 */
@Data
@ToString(exclude="userPermissions")
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Permission {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    long id;
    String name;
    boolean deleted;

    @JsonIgnore
    @OneToMany(fetch=FetchType.LAZY, targetEntity=UserPermissions.class, mappedBy="permissionId")
    List<UserPermissions> userPermissions;

}
