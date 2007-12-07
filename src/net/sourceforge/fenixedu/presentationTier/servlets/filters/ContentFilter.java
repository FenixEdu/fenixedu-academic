package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.Portal;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;

public class ContentFilter implements Filter {

    public static String FUNCTIONALITY_PARAMETER = "_f";
    
    public void init(FilterConfig config) throws ServletException {
        // nothing
    }

    public void destroy() {
        // nothing
    }

    public void doFilter(ServletRequest initialRequest, ServletResponse initialResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) initialRequest;
        HttpServletResponse response = (HttpServletResponse) initialResponse;      

        String path = request.getServletPath();
        
        if (!isInterestingPath(path) || !process(chain, request, response, path)) {
            chain.doFilter(request, response);
        }
    }

    private boolean isInterestingPath(String path) {
        return path.endsWith(".do") || path.endsWith(".faces") || path.endsWith(".jsp") || !path.contains(".");
    }

    private boolean process(FilterChain chain, HttpServletRequest request, HttpServletResponse response, String path) throws IOException, ServletException {
        Portal root = RootDomainObject.getInstance().getRootPortal();
        
        Content content = root.getChildByPath(path);
        String contentPath = getContentPath(request, content);

        return contentPath != null && doForward(request, response, content, contentPath);
    }

    private String getContentPath(HttpServletRequest request, Content content) {
        if (content == null) {
            return null;
        }
        
        String path = content.getPath();

        if (path != null) {
            return path;
        }
        
        Functionality functionality = getFunctionality(request, content);
        return functionality == null ? null : functionality.getPath();
    }

    private Functionality getFunctionality(HttpServletRequest request, Content content) {
        Integer id = getParameter(request, FUNCTIONALITY_PARAMETER);
        return id == null ? null : (Functionality) RootDomainObject.getInstance().readContentByOID(id);
    }

    private Integer getParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? null : new Integer(value);
    }

    private boolean doForward(HttpServletRequest request, HttpServletResponse response, Content content, String path) throws IOException, ServletException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(path);

        if (dispatcher == null) {
            response.sendRedirect(request.getContextPath() + path);
        }
        else {
            dispatcher.forward(request, response);
        }
        
        return true;
    }

}
