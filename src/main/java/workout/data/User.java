package workout.data;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Created by mihae on 1.2.2017..
 */
@Data
@Entity
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

}
