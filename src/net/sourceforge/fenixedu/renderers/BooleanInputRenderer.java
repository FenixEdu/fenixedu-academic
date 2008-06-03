package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlCheckBox;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.contexts.InputContext;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;

/**
 * This renderer allows you to do the input of a boolean value. A checkbox is
 * presented and is checked accordingly with the slot's value.
 * 
 * <p>
 * Example: <input type="checkbox"/>
 * 
 * @author cfgi
 */
public class BooleanInputRenderer extends InputRenderer {
    private boolean disabled;

    private boolean defaultValue = false;

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		HtmlCheckBox checkBox = new HtmlCheckBox();
		checkBox.setChecked(object == null ? getDefaultValue() : (Boolean) object);

		InputContext context = getInputContext();
		checkBox.setTargetSlot((MetaSlotKey) context.getMetaObject().getKey());

		return checkBox;
	    }

	    @Override
	    public void applyStyle(HtmlComponent component) {
		super.applyStyle(component);

		HtmlCheckBox checkBox = (HtmlCheckBox) component;
		checkBox.setDisabled(getDisabled());
	    }

	};
    }

    public boolean getDisabled() {
	return disabled;
    }

    public void setDisabled(boolean disabled) {
	this.disabled = disabled;
    }

    public boolean getDefaultValue() {
	return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
	this.defaultValue = defaultValue;
    }

}
