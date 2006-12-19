package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class TestFilterContext implements FunctionalityContext {

    private HttpServletRequest request;
    private IUserView userView;
    private Functionality functionality;
    
    public TestFilterContext(HttpServletRequest request, TestFilterBean bean, Functionality functionality) {
        this.request = new TestFilterRequestWrapper(request, bean.getParametersMap());
        this.userView = AccessControl.getUserView();
        this.functionality = functionality;
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    public IUserView getUserView() {
        return this.userView;
    }

    public User getLoggedUser() {
        if (this.userView.isPublicRequester()) {
            return null;
        }
        else {
            return AccessControl.getPerson().getUser();
        }
    }

    public Module getSelectedModule() {
        if (this.functionality instanceof Module) {
            return (Module) this.functionality; 
        }
        else {
            return this.functionality.getModule();
        }
    }

    public Functionality getSelectedFunctionality() {
        return this.functionality;
    }

    private static class TestFilterRequestWrapper extends HttpServletRequestWrapper {

        private Map<String, String[]> parameters;

        public TestFilterRequestWrapper(HttpServletRequest request, Map<String, String[]> parameters) {
            super(request);
            
            this.parameters = parameters;
        }

        @Override
        public String getQueryString() {
            if (this.parameters.isEmpty()) {
                return null;
            }
            
            StringBuilder builder = new StringBuilder();

            for (String key : this.parameters.keySet()) {
                String[] values = this.parameters.get(key);
                
                for (int i = 0; i < values.length; i++) {
                    String value = values[i];
                
                    builder.append("&" + key + "=" + value);
                }
            }
            
            return builder.substring(1).toString();
        }

        @Override
        public String getParameter(String name) {
            String[] values = this.parameters.get(name);
            
            if (values == null) {
                return null;
            }
            else {
                return values[0];
            }
        }

        @Override
        public Map getParameterMap() {
            return this.parameters;
        }

        @Override
        public Enumeration getParameterNames() {
            return new Vector<String>(this.parameters.keySet()).elements();
        }

        @Override
        public String[] getParameterValues(String name) {
            return this.parameters.get(name);
        }

        
    }
}
