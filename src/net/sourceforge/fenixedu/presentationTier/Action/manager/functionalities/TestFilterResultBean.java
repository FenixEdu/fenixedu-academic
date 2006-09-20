package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import java.io.Serializable;
import java.util.Map;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;

public class TestFilterResultBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private DomainReference<Functionality> functionality;
    private boolean accessible;

    private Map<String, String[]> parameters;
    
    public TestFilterResultBean(Functionality functionality, boolean accessible, Map<String, String[]> parameters) {
        super();
    
        this.functionality = new DomainReference<Functionality>(functionality);
        this.accessible = accessible;
        this.parameters = parameters;
    }

    public boolean isAccessible() {
        return this.accessible;
    }

    public Functionality getFunctionality() {
        return this.functionality.getObject();
    }
    
    public String getPublicPath() {
        StringBuilder publicPath = new StringBuilder(getFunctionality().getPublicPath());
        
        boolean firstParameter = true;

        if (! publicPath.toString().contains("?")) {
            if (this.parameters.keySet().size() > 0) {
                publicPath.append("?");
            }
        }
        else {
            firstParameter = false;
        }
        
        for (String parameter : this.parameters.keySet()) {
            String[] values = this.parameters.get(parameter);
            
            if (! firstParameter) {
                publicPath.append("&");
            }
            
            firstParameter = false;
            
            for (String value : values) {
                publicPath.append(parameter + "=" + value);
            }
        }
        
        return publicPath.toString();
    }
}
