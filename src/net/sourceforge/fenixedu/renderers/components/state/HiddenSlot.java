package net.sourceforge.fenixedu.renderers.components.state;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;

public class HiddenSlot implements Serializable {
    private String name;
    private Class<Converter> converter;
    private MetaSlotKey key;
    private List<String> values;
    private boolean multiple;

    public HiddenSlot(String name, Class<Converter> converter) {
        super();
        
        this.name = name;
        this.converter = converter;
        this.values = new ArrayList<String>();
        this.multiple = false;
    }
    
    public HiddenSlot(String slot, String value, Class<Converter> converter) {
        this(slot, converter);
        
        addValue(value);
    }

    public Class<Converter> getConverter() {
        return this.converter;
    }
    
    public void setConverter(Class<Converter> converter) {
        this.converter = converter;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<String> getValues() {
        return this.values;
    }
    
    public void addValue(String value) {
        this.values.add(value);
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public boolean isMultiple() {
        return this.multiple || getValues().size() > 1;
    }
    
    public MetaSlotKey getKey() {
        return this.key;
    }

    public void setKey(MetaSlotKey key) {
        this.key = key;
    }
}