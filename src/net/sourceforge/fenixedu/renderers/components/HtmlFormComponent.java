package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.controllers.Controllable;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.components.converters.Convertible;
import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;

public abstract class HtmlFormComponent extends HtmlComponent implements Convertible, Controllable, SlotChanger, Validatable {

    public static int COMPONENT_NUMBER = 0;
    
    private String name;
    
    private Converter converter;

    private HtmlController controller;

    private MetaSlotKey slotKey;

    private boolean disabled;
    
    public HtmlFormComponent() {
        super();
        
        this.name = getNewName();
        this.disabled = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    
    public boolean isDisabled() {
        return this.disabled;
    }

    public boolean hasConverter() {
        return this.converter != null;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }
    
    public Object getConvertedValue(MetaSlot slot) {
        return null;
    }

    public boolean hasController() {
        return controller != null;
    }

    public HtmlController getController() {
        return controller;
    }

    public void setController(HtmlController controller) {
        this.controller = controller;
        this.controller.setControlledComponent(this);
    }
 
    public MetaSlotKey getTargetSlot() {
        return this.slotKey;
    }

    public boolean hasTargetSlot() {
        return this.slotKey == null;
    }

    public void setTargetSlot(MetaSlotKey key) {
        setName(key != null ? key.toString() : null);
        this.slotKey = key;
    }

    public String getNewName() {
        int number;
        
        synchronized (getClass()) {
            number = HtmlFormComponent.COMPONENT_NUMBER++;
        }
        
        String name = Integer.toHexString(number);
        return "C" + name.toUpperCase();
    }
    
    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setAttribute("name", this.name);
        
        return tag;
    }
}
