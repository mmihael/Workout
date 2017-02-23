package workout.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by mihael on 23.2.2017..
 */
@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class WorkoutExerciseOrder {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    long id;
    long workout;
    long exercise;
    @Column(name="`order`")
    long order;
    boolean deleted;
    Timestamp createdAt;
    Timestamp editedAt;
    Long createdBy;

}
