package net.sourceforge.fenixedu.renderers.schemas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Schema {

    private String name;
    private Class type;
    
    private List<SchemaSlotDescription> slotDescriptions;

    private Signature constructor;
    private List<Signature> setters;
    
    public Schema(String name, Class type) {
        this.name = name;
        this.type = type;
        this.slotDescriptions = new ArrayList<SchemaSlotDescription>();
        this.setters = new ArrayList<Signature>();
    }
    
    public Schema(String name, Class type, Schema baseSchema) {
        this(name, type);
        
        if (baseSchema != null) {
            for (SchemaSlotDescription slotDescription : baseSchema.getSlotDescriptions()) {
                addSlotDescription(slotDescription);
            }
            
            for (Signature signature : baseSchema.getSpecialSetters()) {
                addSpecialSetter(signature);
            }
        }
    }

    public Schema(Class type) {
        this(null, type);
    }

    public String getName() {
        return name;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public Signature getConstructor() {
        return this.constructor;
    }

    public void setConstructor(Signature constructor) {
        this.constructor = constructor;
    }

    public List<SchemaSlotDescription> getSlotDescriptions() {
        return slotDescriptions;
    }

    public SchemaSlotDescription getSlotDescription(String slotName) {
        for (SchemaSlotDescription slotDescription : getSlotDescriptions()) {
            if (slotDescription.getSlotName().equals(slotName)) {
                return slotDescription;
            }
        }
        
        return null;
    }

    public void addSlotDescription(SchemaSlotDescription slotDescription) {
        int index = findSlotIndex(slotDescription.getSlotName());

        if (index != -1) {
            this.slotDescriptions.set(index, slotDescription);
        }
        else {
            this.slotDescriptions.add(slotDescription);
        }
    }
    
    private int findSlotIndex(String name) {
        int i = 0;
        for (Iterator iterator = this.slotDescriptions.iterator(); iterator.hasNext(); i++) {
            SchemaSlotDescription slotDescription = (SchemaSlotDescription) iterator.next();
            
            if (slotDescription.getSlotName().equals(name)) {
                return i;
            }
        }
        
        return -1;
    }
    
    public void removeSlotDescription(SchemaSlotDescription slotDescription) {
        if (this.slotDescriptions.remove(slotDescription)) {
            for (Iterator<Signature> iter = getSpecialSetters().iterator(); iter.hasNext();) {
                Signature signature = iter.next();

                for (SignatureParameter parameter : signature.getParameters()) {
                    if (parameter.getSlotDescription().equals(slotDescription)) {
                        iter.remove();
                        break;
                    }
                }
            }
        }
    }

    public void addSpecialSetter(Signature setterSignature) {
        this.setters.add(setterSignature);
    }
    
    public List<Signature> getSpecialSetters() {
        return this.setters;
    }
}
