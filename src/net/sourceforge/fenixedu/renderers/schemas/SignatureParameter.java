package net.sourceforge.fenixedu.renderers.schemas;

public class SignatureParameter {
    private SchemaSlotDescription slotDescription;
    private Class type;
    
    public SignatureParameter(SchemaSlotDescription slotDescription, Class type) {
        super();

        this.slotDescription = slotDescription;
        this.type = type;
    }

    public SchemaSlotDescription getSlotDescription() {
        return this.slotDescription;
    }
    
    public Class getType() {
        return this.type;
    }
    
}
