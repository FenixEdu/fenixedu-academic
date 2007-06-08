package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlFormComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlTextInput;
import net.sourceforge.fenixedu.renderers.contexts.InputContext;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;

/**
 * This renderer serves as a base for all input renderers that are
 * based in a text input field.
 * 
 * @author cfgi
 */
public abstract class TextFieldRenderer extends InputRenderer {

    private boolean disabled;

    private boolean readOnly;

    private String size;

    private Integer maxLength;

    public boolean getDisabled() {
        return disabled;
    }

    /**
     * Indicates that the field is to be disabled, that is, the user
     * won't be able to change it's value and it wont be submited.
     * 
     * @property
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    /**
     * The max length of the field's input. 
     * 
     * @property
     */
    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public boolean getReadOnly() {
        return readOnly;
    }

    /**
     * Indicates that the field is read only. The user cannot change
     * the field's value but the field is submited.
     * 
     * @property
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getSize() {
        return size;
    }

    /**
     * The size of the field.
     * 
     * @property
     */
    public void setSize(String size) {
        this.size = size;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new TextFieldLayout();
    }

    protected abstract HtmlComponent createTextField(Object object, Class type);

    protected class TextFieldLayout extends Layout {

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            HtmlComponent component = createTextField(object, type);

            InputContext context = getInputContext();
            setContextSlot(component, (MetaSlotKey) context.getMetaObject().getKey());

            return component;
        }

        protected void setContextSlot(HtmlComponent component, MetaSlotKey slotKey) {
            ((HtmlFormComponent) component).setTargetSlot(slotKey);
        }

        @Override
        public void applyStyle(HtmlComponent component) {
            super.applyStyle(component);

            HtmlTextInput textInput = (HtmlTextInput) component;

            textInput.setMaxLength(getMaxLength());
            textInput.setSize(getSize());
            textInput.setReadOnly(getReadOnly());
            textInput.setDisabled(getDisabled());
        }
    }
}
