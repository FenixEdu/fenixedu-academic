package net.sourceforge.fenixedu.renderers.components;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.jsp.PageContext;

import pt.ist.utl.fenix.utils.Pair;

import net.sourceforge.fenixedu.renderers.components.controllers.Controllable;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.components.converters.Convertible;
import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.renderers.validators.HtmlChainValidator;
import net.sourceforge.fenixedu.renderers.validators.HtmlValidator;

public abstract class HtmlFormComponent extends HtmlComponent implements Convertible, Controllable, SlotChanger, Validatable {

    public static int COMPONENT_NUMBER = 0;

    private String name;

    private Converter converter;

    private HtmlController controller;

    private MetaSlotKey slotKey;

    private boolean disabled;

    private HtmlChainValidator chainValidator;

    public HtmlFormComponent() {
	super();

	this.name = getNewName();
	this.disabled = false;
    }

    public String getName() {
	return name;
    }

    /**
     * Sets the name of this form component. If the name does not obey the rules
     * for component names then it may be transformed to become conformat. Thus
     * {@link #getName()} may return a value that is not equal to the name given
     * in this method.
     * 
     * @param name
     *                the desired name
     */
    public void setName(String name) {
	this.name = getValidIdOrName(name);
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

    public HtmlChainValidator getChainValidator() {
	return this.chainValidator;
    }

    public void setChainValidator(HtmlChainValidator validator) {
	this.chainValidator = validator;
    }

    public void setChainValidator(MetaSlot slot) {
	HtmlChainValidator htmlChainValidator = new HtmlChainValidator(this);
	List<HtmlValidator> validators = new ArrayList<HtmlValidator>();
	for (Pair<Class<HtmlValidator>, Properties> validatorPair : slot.getValidators()) {
	    Constructor<HtmlValidator> constructor;
	    try {
		constructor = validatorPair.getKey().getConstructor(new Class[] { HtmlChainValidator.class });

		HtmlValidator validator = constructor.newInstance(htmlChainValidator);
		RenderUtils.setProperties(validator, validatorPair.getValue());

		validators.add(validator);
	    } catch (Exception e) {
		throw new RuntimeException("could not create validator '" + validatorPair.getKey().getName() + "' for slot '"
			+ slot.getName() + "': ", e);
	    }
	}
	setChainValidator(htmlChainValidator);
    }

    public void addValidator(HtmlValidator htmlValidator) {
	if (this.chainValidator == null) {
	    this.chainValidator = new HtmlChainValidator(this);
	}
	this.chainValidator.addValidator(htmlValidator);
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

    public static String getNewName() {
	int number;

	synchronized (HtmlFormComponent.class) {
	    number = HtmlFormComponent.COMPONENT_NUMBER++;
	}

	String name = Integer.toHexString(number);
	return "C" + name.toUpperCase();
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
	HtmlTag tag = super.getOwnTag(context);

	tag.setAttribute("name", getName());

	return tag;
    }
}
