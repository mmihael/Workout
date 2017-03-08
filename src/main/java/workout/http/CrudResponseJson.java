package workout.http;

import lombok.Data;

import java.util.Map;

/**
 * Created by mihael on 19.2.2017..
 */
@Data
public class CrudResponseJson<T> {

    int status;
    String message;
    Map<String, String> errors;
    T entity;

    public static enum CrudStatus {
        SUCCESS(0),
        ERROR(1);
        private int id;
        CrudStatus(int id) { this.id = id; }
        public int getId() { return this.id; }
    }

}
