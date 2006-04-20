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

/**
 * This input renderer allows several slots to be edited without any
 * special formating. Each slot's editor is placed after the previous. 
 * You should note that this technique does not work well for more than 
 * two or three slots. You can decreese the space taken by suppresing
 * the slots labels but then you need other way of identifing wich slot
 * is beeing edited.
 * 
 * <p>
 * Example:
 *  Name: <input type="text"/> Age: <input type="text"/>
 * 
 * @author cfgi
 */
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
     * Allows to specify the class attribute for each one
     * of the slot's presentation. 
     * 
     * @property
     */
    public void setEachClasses(String eachClasses) {
        this.eachClasses = eachClasses;
    }

    public boolean isEachInline() {
        return this.eachInline;
    }

    /**
     * This property selects if each slot should be presented as an inline
     * element or as a block element. By default slots are presented inline,
     * that is, they will be added inside <tt>span</tt> elements. If 
     * <tt>eachInline</tt> is <tt>false</tt> then a <tt>div</tt> will be used.
     * 
     * @property
     */
    public void setEachInline(boolean eachInline) {
        this.eachInline = eachInline;
    }

    public String getEachLayout() {
        return this.eachLayout;
    }

    /**
     * The layout to be used in each slot's presentation.
     * 
     * @property
     */
    public void setEachLayout(String eachLayout) {
        this.eachLayout = eachLayout;
    }

    public String getEachSchema() {
        return this.eachSchema;
    }

    /**
     * The schema to be used in each of the slot's presentation.
     * 
     * @property
     */
    public void setEachSchema(String eachSchema) {
        this.eachSchema = eachSchema;
    }

    public String getEachStyle() {
        return this.eachStyle;
    }

    /**
     * The value of the style attribute of each slot's presentation.
     * 
     * @property
     */
    public void setEachStyle(String eachStyle) {
        this.eachStyle = eachStyle;
    }

    public boolean isLabelExcluded() {
        return this.labelExcluded;
    }

    /**
     * Whether the label should be presented before each editor or not. 
     * 
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
            HtmlValidator validator = getValidator(findValidatableComponent(component), slot); 

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
