package dbservermentoria.Teste.CORS;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter {

    private String permittedOrigin = "http://localhost:808"; //Todo configurara para diferentes ambientes

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        resp.setHeader("Access-Control-Allow-origin", permittedOrigin);
        resp.setHeader("Access-Control-Allow-Credentials", "true");

        if("OPTIONS".equals(req.getMethod())&& permittedOrigin.equals(req.getHeader("Origin"))) {
            resp.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, PATCH, OPTIONS");
            resp.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
            resp.setHeader("Access-Control-Max-Age", "3600");
            resp.setStatus(HttpServletResponse.SC_OK);
        }
        else{
            chain.doFilter(request, response);
        }



    }
}
