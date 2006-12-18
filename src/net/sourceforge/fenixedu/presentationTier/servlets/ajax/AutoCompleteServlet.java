package net.sourceforge.fenixedu.presentationTier.servlets.ajax;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

public class AutoCompleteServlet extends HttpServlet {

    private static final String JAVASCRIPT_LIBRARY_ENCODING = "UTF-8";
    private static final int DEFAULT_MAX_COUNT = 20;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	request.setCharacterEncoding("UTF-8");
    	
        String service = request.getParameter("serviceName");
        Class type = getConcreteType(request.getParameter("class"));
        String value = new String(request.getParameter("value").getBytes(), JAVASCRIPT_LIBRARY_ENCODING);
        
        Map<String, String> serviceArgsMap = getServiceArgsMap(request.getParameter("serviceArgs"));
        int maxCount = getNumber(request.getParameter("maxCount"), DEFAULT_MAX_COUNT);

        IUserView userView = getUserView(request);
        List result = executeService(userView, service, type, value, maxCount, serviceArgsMap);
        
        String labelField = request.getParameter("labelField");
        String format     = request.getParameter("format");
        String valueField = request.getParameter("valueField");
        String styleClass = request.getParameter("styleClass");
        

        response.setContentType("text/html");
        response.getWriter().write(getResponseHtml(limitResult(result, maxCount), labelField, format, valueField, styleClass));
    }

    private Class getConcreteType(String className) throws ServletException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ServletException("could not find type " + className, e);
        }
    }

    private List limitResult(List result, int maxCount) {
        return result.subList(0, Math.min(maxCount, result.size()));
    }

    private int getNumber(String parameter, int defaultValue) {
        if (parameter == null) {
            return defaultValue;
        }
        
        try {
            return Integer.parseInt(parameter);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private List executeService(IUserView userView, String serviceName,
            Class type, String value, int maxCount, Map<String, String> arguments) throws ServletException {
        try {
            return (List) ServiceUtils.executeService(userView, serviceName,
                    new Object[] { type, value, maxCount, arguments });
        } catch (Exception e) {
            throw new ServletException("Error executing service", e);
        }
    }

    private Map<String, String> getServiceArgsMap(String encodedServiceArgs) {
        String[] serviceArgsArray = StringUtils.split(encodedServiceArgs, ',');
        Map<String, String> serviceArgsMap = new HashMap<String, String>();

        for (String serviceArg : serviceArgsArray) {
            String[] argNameArgValue = StringUtils.split(serviceArg, '=');
            serviceArgsMap.put(argNameArgValue[0], argNameArgValue[1]);
        }

        return serviceArgsMap;
    }

    private IUserView getUserView(HttpServletRequest request) {
        return SessionUtils.getUserView(request);
    }

    private String getResponseHtml(List result, String labelField, String format, String valueField, String styleClass) {
        StringBuilder responseHtml = new StringBuilder();
        responseHtml.append("<ul ").append("class=\"").append(styleClass).append("\">");

        try {

            for (int i = 0; i < result.size(); i++) {
                Object element = result.get(i);

                responseHtml.append("<li id=\"")
                        .append(BeanUtils.getProperty(element, valueField))
                        .append("\" name=\"" + BeanUtils.getProperty(element, labelField))
                        .append("\">");
                
                if (format == null) {
                    responseHtml.append(BeanUtils.getProperty(element, labelField));
                }
                else {
                    responseHtml.append(RenderUtils.getFormattedProperties(format, element));
                }
                
                responseHtml.append("</li>");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error getting field property (see label and value fields)", ex);

        }

        responseHtml.append("</ul>");

        return responseHtml.toString();
    }
}
