package workout.component;

import org.springframework.security.web.AuthenticationEntryPoint;

import org.springframework.security.core.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by mihae on 13.2.2017..
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException exception) throws IOException, ServletException {
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED");
    }

}
