package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;

public class LinkObject implements Serializable {

    private Integer id;
    private String method;
    private String label;
    
    public LinkObject() {
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
}
