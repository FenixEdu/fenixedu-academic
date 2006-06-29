package net.sourceforge.fenixedu.renderers.schemas;

import java.util.ArrayList;
import java.util.List;

public class Signature {
    private String name;
    private List<SignatureParameter> parameters;
    
    public Signature(String name) {
        super();
    
        this.name = name;
        this.parameters = new ArrayList<SignatureParameter>();
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<SignatureParameter> getParameters() {
        return this.parameters;
    }

    public void addParameter(SchemaSlotDescription slotDescription, Class slotType) {
        getParameters().add(new SignatureParameter(slotDescription, slotType));
    }
}
