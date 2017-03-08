package workout.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Created by mihael on 23.2.2017..
 */
@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Where(clause="deleted=0")
public class UsersWorkoutStatistic {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    long id;
    long usersWorkout;
    long workoutExerciseOrder;
    Double weight;
    Long reps;
    boolean deleted;
    Timestamp createdAt;
    Timestamp editedAt;
    Long createdBy;

}
