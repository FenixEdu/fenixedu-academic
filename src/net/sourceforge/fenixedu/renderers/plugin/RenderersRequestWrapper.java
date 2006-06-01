package net.sourceforge.fenixedu.renderers.plugin;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class RenderersRequestWrapper extends HttpServletRequestWrapper {

    private Hashtable<String, List<String>> parameters;
    
    public RenderersRequestWrapper(HttpServletRequest request) {
        super(request);
        
        this.parameters = new Hashtable<String, List<String>>();
    }

    public void addParameter(String name, String value) {
        ensureParameterPresent(name);
        addParameterValue(name, value);
    }

    private void ensureParameterPresent(String name) {
        if (! parameterExists(name)) {
            this.parameters.put(name, new ArrayList<String>());
        }
    }

    private boolean parameterExists(String name) {
        return this.parameters.containsKey(name);
    }

    private void addParameterValue(String name, String value) {
        this.parameters.get(name).add(value);
    }

    @Override
    public String getParameter(String name) {
        if (! parameterExists(name)) {
            return null;
        }
        
        return this.parameters.get(name).get(0);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> resultMap = new Hashtable<String, String[]>();
        
        for (String parameter : this.parameters.keySet()) {
            resultMap.put(parameter, parameters.get(parameter).toArray(new String[0]));
        }
        
        return resultMap;
    }

    @Override
    public Enumeration getParameterNames() {
        return this.parameters.keys();
    }

    @Override
    public String[] getParameterValues(String name) {
        return getParameterMap().get(name);
    }
    
}
