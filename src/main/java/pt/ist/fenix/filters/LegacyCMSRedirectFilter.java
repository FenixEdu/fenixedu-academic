package pt.ist.fenix.filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter({ "/departments/*", "/ccad/*", "/ccad", "/assembleiaescola/*", "/assembleiaescola", "/cgq/*", "/cgq", "/CCBE/*",
        "/CCBE", "/npgfc/*", "/npgfc", "/RecursosHumanos/*", "/RecursosHumanos", "/conselhoEscola/*", "/conselhoEscola" })
public class LegacyCMSRedirectFilter implements Filter {

    private final Map<String, String> map = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        map.put("departments", "/departamentos");
        map.put("ccad", "/units/2482491971449");
        map.put("assembleiaescola", "/units/2482491887286");
        map.put("cgq", "/units/2482492473777");
        map.put("CCBE", "/units/2482492045984");
        map.put("npgfc", "/units/2482492000046");
        map.put("RecursosHumanos", "/units/2482491891565");
        map.put("conselhoEscola", "/units/2482491886913");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String[] parts = req.getRequestURI().substring(req.getContextPath().length() + 1).split("/", 2);
        if (map.containsKey(parts[0])) {
            resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            if (parts.length > 1) {
                resp.setHeader("Location", req.getContextPath() + map.get(parts[0]) + "/" + parts[1]);
            } else {
                resp.setHeader("Location", req.getContextPath() + map.get(parts[0]));
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }

}
