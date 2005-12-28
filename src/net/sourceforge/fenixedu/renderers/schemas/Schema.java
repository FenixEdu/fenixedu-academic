package net.sourceforge.fenixedu.renderers.schemas;

import java.util.ArrayList;
import java.util.List;


public class Schema {

    private String name;

    private Class type;

    private List<SchemaSlotDescription> slotDescriptions;
    
    public Schema(String name, Class type) {
        this.name = name;
        this.type = type;
        this.slotDescriptions = new ArrayList<SchemaSlotDescription>();
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

    public List<SchemaSlotDescription> getSlotDescriptions() {
        return slotDescriptions;
    }

    public void addSlotDescription(SchemaSlotDescription slotDescription) {
        this.slotDescriptions.add(slotDescription);
    }
    
//    public void addSlotDescription(String slotName, String layout, String key, Properties properties) {
//        this.slotDescriptions.add(new SchemaSlotDescription(slotName, layout, key, properties));
//    }
    
//    public void addSlotDescription(String slotName) {
//        addSlotDescription(slotName, null, null, new Properties());
//    }
}
