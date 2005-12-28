package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlFormComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlTextInput;
import net.sourceforge.fenixedu.renderers.contexts.InputContext;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;

public abstract class TextFieldRenderer extends InputRenderer {

    private boolean disabled;

    private boolean readOnly;

    private String size;

    private Integer maxLength;

    public boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new TextFieldLayout();
    }

    protected abstract HtmlComponent createTextField(Object object, Class type);

    class TextFieldLayout extends Layout {

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
