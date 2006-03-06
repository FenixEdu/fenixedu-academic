package net.sourceforge.fenixedu.renderers;

import java.util.Iterator;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.FlowLayout;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.validators.HtmlValidator;

public class FlowInputRenderer extends InputRenderer {
    private String eachClasses;

    private String eachStyle;

    private String eachSchema;

    private String eachLayout;

    private boolean eachInline = true;
    
    private boolean labelExcluded = false;
    
    public String getEachClasses() {
        return this.eachClasses;
    }

    /**
     * @property
     */
    public void setEachClasses(String eachClasses) {
        this.eachClasses = eachClasses;
    }

    public boolean isEachInline() {
        return this.eachInline;
    }

    /**
     * @property
     */
    public void setEachInline(boolean eachInline) {
        this.eachInline = eachInline;
    }

    public String getEachLayout() {
        return this.eachLayout;
    }

    /**
     * @property
     */
    public void setEachLayout(String eachLayout) {
        this.eachLayout = eachLayout;
    }

    public String getEachSchema() {
        return this.eachSchema;
    }

    /**
     * @property
     */
    public void setEachSchema(String eachSchema) {
        this.eachSchema = eachSchema;
    }

    public String getEachStyle() {
        return this.eachStyle;
    }

    /**
     * @property
     */
    public void setEachStyle(String eachStyle) {
        this.eachStyle = eachStyle;
    }

    public boolean isLabelExcluded() {
        return this.labelExcluded;
    }

    /**
     * @property
     */
    public void setLabelExcluded(boolean labelExcluded) {
        this.labelExcluded = labelExcluded;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new FlowObjectInputRenderer(getInputContext().getMetaObject());
    }

    private class FlowObjectInputRenderer extends FlowLayout {

        private Iterator<MetaSlot> iterator;

        public FlowObjectInputRenderer(MetaObject object) {
            this.iterator = object.getSlots().iterator();
        }
        
        @Override
        protected boolean hasMoreComponents() {
            return iterator.hasNext();
        }

        @Override
        protected HtmlComponent getNextComponent() {
            MetaSlot slot = iterator.next();
            
            HtmlText label = new HtmlText(slot.getLabel());
            HtmlComponent component = renderSlot(slot);
            HtmlValidator validator = getValidator(slot, findValidatableComponent(component)); 

            if (isLabelExcluded() && validator == null) {
                return component;
            }

            return createContainer(label, component, validator);
        }

        private HtmlInlineContainer createContainer(HtmlText label, HtmlComponent component, HtmlValidator validator) {
            HtmlInlineContainer container = new HtmlInlineContainer();
            
            if (! isLabelExcluded()) {
                container.addChild(label);
            }
            
            container.addChild(component);
            
            if (validator != null) {
                container.addChild(validator);
            }
            return container;
        }
    }
}
