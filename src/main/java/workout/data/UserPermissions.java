package workout.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by mihael on 1.2.2017..
 */
@Data
@ToString(exclude={"permission", "user"})
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserPermissions {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    long id;
    long userId;
    long permissionId;
    boolean deleted;
    Timestamp createdAt;
    Long createdBy;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="permissionId", updatable=false, insertable=false)
    Permission permission;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userId", updatable=false, insertable=false)
    User user;

}
