package workout.http;

import lombok.Data;

import java.util.Map;

/**
 * Created by mihael on 19.2.2017..
 */
@Data
public class CrudResponseJson<T> {

    String message;
    Map<String, String> errors;
    T entity;

}
