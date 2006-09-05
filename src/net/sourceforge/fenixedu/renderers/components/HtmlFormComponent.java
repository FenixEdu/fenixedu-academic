package net.sourceforge.fenixedu.renderers.components;

import java.lang.reflect.Constructor;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.controllers.Controllable;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.components.converters.Convertible;
import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.renderers.validators.HtmlValidator;

public abstract class HtmlFormComponent extends HtmlComponent implements Convertible, Controllable, SlotChanger, Validatable {

    public static int COMPONENT_NUMBER = 0;
    
    private String name;
    
    private Converter converter;

    private HtmlController controller;

    private MetaSlotKey slotKey;

    private boolean disabled;
    
    private HtmlValidator validator;
    
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
    
    public Object getConvertedValue() {
        return null;
    }
    
    public Object getConvertedValue(MetaSlot slot) {
        return null;
    }

    public HtmlValidator getValidator() {
        return this.validator;
    }

    public void setValidator(HtmlValidator validator) {
        this.validator = validator;
    }

    public void setValidator(MetaSlot slot) {
        Class<HtmlValidator> validatorType = slot.getValidator();
        
        if (validatorType == null) {
            setValidator((HtmlValidator) null);
            return;
        }
    
        Constructor<HtmlValidator> constructor;
        try {
            constructor = validatorType.getConstructor(new Class[] { Validatable.class });
    
            HtmlValidator validator = constructor.newInstance(this);
            RenderUtils.setProperties(validator, slot.getValidatorProperties());
            
            setValidator(validator);
        } catch (Exception e) {
            throw new RuntimeException("could not create validator '" + validatorType.getName() + "' for slot '"
                    + slot.getName() + "': ", e);
        }
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
        return this.slotKey != null;
    }

    public void setTargetSlot(MetaSlotKey key) {
        setName(key != null ? key.toString() : null);
        this.slotKey = key;
    }

    public void bind(MetaSlot slot) {
        if (slot != null) {
            setTargetSlot(slot.getKey());
        }
    }
    
    public void bind(MetaObject object, String slotName) {
        bind(object.getSlot(slotName));
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
