package demo;

import lombok.Builder;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Builder
public class HttpRequestProperties {

    public static HttpRequestProperties fromRequest(HttpServletRequest request) {
        // TODO
        return null;
    }

}
