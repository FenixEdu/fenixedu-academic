package net.sourceforge.fenixedu.presentationTier.servlets.ajax;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.WrapDynaBean;
import org.apache.commons.lang.StringUtils;

public class AutoCompleteServlet extends HttpServlet {

    private static final String JAVASCRIPT_LIBRARY_ENCODING = "UTF-8";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String serviceName = request.getParameter("serviceName");
        Map<String, String> serviceArgsMap = getServiceArgsMap(request.getParameter("serviceArgs"));
        String inputTextArgumentName = request.getParameter("inputTextArgName");
        String valueToSearch = new String(request.getParameter("value").getBytes(),
                JAVASCRIPT_LIBRARY_ENCODING);
        String domainClass = request.getParameter("class");

        serviceArgsMap.put(inputTextArgumentName, valueToSearch);
        serviceArgsMap.put("class", domainClass);

        IUserView userView = getUserView(request);
        List result = executeService(userView, serviceName, serviceArgsMap);
        String labelField = request.getParameter("labelField");
        String valueField = request.getParameter("valueField");
        String styleClass = request.getParameter("styleClass");

        response.setContentType("text/html");
        response.getWriter().write(getResponseHtml(result, labelField, valueField, styleClass));

    }

    private List executeService(IUserView userView, String serviceName,
            Map<String, String> serviceArgsMap) throws ServletException {

        List result = new ArrayList();

        try {
            List resultFromService = (List) ServiceUtils.executeService(userView, serviceName,
                    new Object[] { serviceArgsMap });

            result.addAll(resultFromService);

        } catch (Exception e) {
            throw new ServletException("Error executing service", e);
        }

        return result;

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
        HttpSession session = request.getSession(false);
        return (IUserView) session.getAttribute(SessionConstants.U_VIEW);
    }

    private String getResponseHtml(List result, String labelField, String valueField, String styleClass) {
        StringBuilder responseHtml = new StringBuilder();
        responseHtml.append("<ul ").append("class=\"").append(styleClass).append("\">");

        try {

            for (int i = 0; i < result.size(); i++) {
                Object element = result.get(i);

                responseHtml.append("<li id=\"").append(BeanUtils.getProperty(element, valueField))
                        .append("\">").append(BeanUtils.getProperty(element, labelField))
                        .append("</li>");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error getting field property (see label and value fields)");

        }

        responseHtml.append("</ul>");

        return responseHtml.toString();
    }
}
