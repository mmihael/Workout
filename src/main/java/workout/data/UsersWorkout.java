package workout.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by mihael on 23.2.2017..
 */
@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Where(clause="deleted=0")
public class UsersWorkout {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    long id;
    long workout;
    long user;
    boolean deleted;
    Timestamp createdAt;
    Timestamp editedAt;
    Long createdBy;

}
