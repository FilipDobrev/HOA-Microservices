package nl.tudelft.sem.group23a.hoa.infrastructure;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestExtensions {

    public static String getBearerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return  header != null ? header.substring(7) : null;
    }
}
