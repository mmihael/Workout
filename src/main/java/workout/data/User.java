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
@ToString(exclude="permissions")
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    long id;
    String username;
    String password;
    String email;
    boolean enabled;
    boolean deleted;
    Timestamp createdAt;
    Timestamp editedAt;
    Long createdBy;

    @JsonIgnore
    @OneToMany(fetch=FetchType.LAZY, targetEntity=UserPermissions.class, mappedBy="userId")
    List<UserPermissions> permissions;

}
