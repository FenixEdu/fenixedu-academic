package pt.ist.fenix.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.thesis.Thesis;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

@WebFilter(ThesisSiteRedirectFilter.FILTER_PATH + "*")
public class ThesisSiteRedirectFilter implements Filter {

    static final String FILTER_PATH = "/dissertacoes/";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        Thesis thesis = getThesisFromURL(req.getRequestURI());
        if (thesis == null) {
            resp.sendError(404);
        } else {
            resp.sendRedirect(req.getContextPath() + "/thesis/" + thesis.getExternalId());
        }
    }

    public static Thesis getThesisFromURL(String url) {
        try {
            // Remove trailing path, and split the tokens
            String[] parts = url.substring(url.indexOf(FILTER_PATH)).replace(FILTER_PATH, "").split("\\/");
            if (parts.length == 0) {
                return null;
            }
            String id = parts[0];
            DomainObject object = FenixFramework.getDomainObject(id);
            if (object instanceof Thesis) {
                return ((Thesis) object);
            } else {
                // Identifier should be an IdInternal, let's try to process it...
                long oid = 2353642078208l + Integer.parseInt(id);
                Thesis thesis = FenixFramework.getConfig().getBackEnd().fromOid(oid);
                // To force the object load to fail if it is not valid
                thesis.getComment();
                return thesis;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void destroy() {
    }

}
