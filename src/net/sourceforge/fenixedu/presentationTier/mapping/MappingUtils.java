package net.sourceforge.fenixedu.presentationTier.mapping;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class MappingUtils {

    public static String currentURL(HttpServletRequest request) {
        // customize to match parameters
        String queryString = constructQueryString(request);

        StringBuffer id = new StringBuffer(request.getRequestURL());

        if (queryString != null) {
            id.append("?");
            
            id.append(queryString);
        }
        return id.toString();
    }

    private static String constructQueryString(HttpServletRequest request) {
        StringBuffer queryString = new StringBuffer();

        String requestQueryString = request.getQueryString();
        if (requestQueryString != null) {
            queryString.append(requestQueryString);
        }

        Enumeration parameterNames = request.getParameterNames();
        if (parameterNames != null) {
            while (parameterNames.hasMoreElements()) {
                String parameterName = (String) parameterNames.nextElement();
     
                String[] parameterValues = request.getParameterValues(parameterName);
                for (int i = 0; i < parameterValues.length; i++) {
                    String parameterValue = parameterValues[i];
  
                    if (queryString.length() != 0) {
                        queryString.append("&");
                    }
                  
              
                     queryString.append("&");
                        
                    
                    queryString.append(parameterName);
                    queryString.append("=");
                    queryString.append(parameterValue);
                }
            }
        }

        if (queryString.length() != 0) {
            return queryString.toString();
        }
        return null;

    }

}
