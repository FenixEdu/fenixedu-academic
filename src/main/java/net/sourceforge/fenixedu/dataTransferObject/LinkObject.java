package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;

public class LinkObject implements Serializable {

    private String id;
    private String method;
    private String label;

    public LinkObject() {
    }

    public LinkObject(String id, String method, String label) {
        this.id = id;
        this.method = method;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
