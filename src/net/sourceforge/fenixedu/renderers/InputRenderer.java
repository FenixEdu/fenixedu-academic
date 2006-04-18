package net.sourceforge.fenixedu.renderers;

import java.lang.reflect.Constructor;
import java.util.List;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlFormComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlSimpleValueComponent;
import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.contexts.InputContext;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderMode;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.renderers.validators.HtmlValidator;

import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;

/**
 * The base renderer for every input renderer.
 * 
 * @author cfgi
 */
public abstract class InputRenderer extends Renderer {
    private static final Logger logger = Logger.getLogger(InputRenderer.class);
    
    public InputContext getInputContext() {
        return (InputContext) getContext();
    }
    
    protected HtmlSimpleValueComponent findValidatableComponent(HtmlComponent component) {
        if (component instanceof HtmlSimpleValueComponent) {
            return (HtmlSimpleValueComponent) component;
        } else {
            List<HtmlComponent> children = component.getChildren(new Predicate() {

                public boolean evaluate(Object component) {
                    if (! (component instanceof HtmlFormComponent)) {
                        return false;
                    }
                    
                    HtmlFormComponent formComponent = (HtmlFormComponent) component;
                    return formComponent.hasTargetSlot();
                }

            });

            if (children.size() > 0) {
                return (HtmlSimpleValueComponent) children.get(0);
            }
        }
        
        return null;
    }
    
    protected HtmlValidator getValidator(Validatable inputComponent, MetaSlot slot) {
        Class<HtmlValidator> validatorType = slot.getValidator();
    
        if (validatorType == null) {
            return null;
        }
    
        Constructor<HtmlValidator> constructor;
        try {
            constructor = validatorType.getConstructor(new Class[] { Validatable.class });
    
            HtmlValidator validator = constructor.newInstance(inputComponent);
            RenderUtils.setProperties(validator, slot.getValidatorProperties());
            
            return validator;
        } catch (Exception e) {
            logger.warn("could not create validator '" + validatorType.getName() + "' for slot '"
                    + slot.getName() + "': " + e);
            return null;
        }
    }
    
    protected HtmlValidator getValidator(MetaSlot slot, HtmlSimpleValueComponent component) {
        Class<HtmlValidator> validatorType = slot.getValidator();

        if (validatorType == null) {
            return null;
        }

        Constructor<HtmlValidator> constructor;
        try {
            constructor = validatorType.getConstructor(new Class[] { Validatable.class });

            HtmlValidator validator = constructor.newInstance(component);
            RenderUtils.setProperties(validator, slot.getValidatorProperties());
            
            return validator;
        } catch (Exception e) {
            logger.warn("could not create validator '" + validatorType.getName() + "' for slot '"
                    + slot.getName() + "': " + e);
            return null;
        }
    }

    @Override
    protected HtmlComponent renderSlot(MetaSlot slot) {
        PresentationContext newContext = getContext().createSubContext(slot);
        newContext.setLayout(slot.getLayout());
        newContext.setProperties(slot.getProperties());
        
        if (slot.isReadOnly()) {
            newContext.setRenderMode(RenderMode.getMode("output"));
        }
        
        Object value = slot.getObject();
        Class type = slot.getType();
        
        RenderKit kit = RenderKit.getInstance(); 
        return kit.render(newContext, value, type);
    }
}
