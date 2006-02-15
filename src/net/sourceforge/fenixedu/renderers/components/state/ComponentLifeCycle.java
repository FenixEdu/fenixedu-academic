package net.sourceforge.fenixedu.renderers.components.state;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlFormComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlHiddenField;
import net.sourceforge.fenixedu.renderers.components.HtmlMultipleHiddenField;
import net.sourceforge.fenixedu.renderers.components.HtmlMultipleValueComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlSimpleValueComponent;
import net.sourceforge.fenixedu.renderers.components.controllers.Controllable;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.contexts.InputContext;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.validators.HtmlValidator;

import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;

public class ComponentLifeCycle {
    private static final Logger logger = Logger.getLogger(ComponentLifeCycle.class);
    
    //
    // Utility classes
    //
    
    private class ComponentCollector {
    
        private List<HtmlFormComponent> formComponents;
    
        private List<HtmlController> controllers;
    
        public ComponentCollector(IViewState viewState, HtmlComponent component) {
            this.formComponents = new ArrayList<HtmlFormComponent>();
            this.controllers = new ArrayList<HtmlController>();
    
            visit(component);
            addHiddenComponents(viewState);

        }

        private void addHiddenComponents(IViewState viewState) {
            // add hidden fields that were rendered by the framework
            for (HiddenSlot hiddenSlot : viewState.getHiddenSlots()) {
                HtmlFormComponent hiddenField;
                
                if (hiddenSlot.isMultiple()) {
                    hiddenField = new HtmlMultipleHiddenField(hiddenSlot.getName());
                }
                else {
                    hiddenField = new HtmlHiddenField(hiddenSlot.getName(), null);
                }

                hiddenField.setTargetSlot(hiddenSlot.getKey());
                this.formComponents.add(hiddenField);
            }
        }
    
        public List<HtmlFormComponent> getFormComponents() {
            return this.formComponents;
        }
    
        public List<HtmlController> getControllers() {
            return this.controllers;
        }
    
        private void visit(HtmlComponent component) {
            List<HtmlComponent> components = component.getChildren(new Predicate() {
                public boolean evaluate(Object component) {
                    if (component instanceof HtmlFormComponent) {
                        HtmlFormComponent formComponent = (HtmlFormComponent) component;
    
                        if (formComponent.getName() != null) {
                            return true;
                        }
                    }
    
                    return false;
                }
            });
    
            for (HtmlComponent comp : components) {
                this.formComponents.add((HtmlFormComponent) comp);
            }
    
            Predicate hasController = new Predicate() {
                public boolean evaluate(Object component) {
                    if (component instanceof Controllable) {
                        Controllable controllabelComponent = (Controllable) component;
    
                        if (controllabelComponent.hasController()) {
                            return true;
                        }
                    }
    
                    return false;
                }
            };
    
            if (hasController.evaluate(component)) {
                this.controllers.add(((Controllable) component).getController());
            }
    
            components = component.getChildren(hasController);
            for (HtmlComponent comp : components) {
                this.controllers.add(((Controllable) comp).getController());
            }
        }
    }

    private static ComponentLifeCycle instance = new ComponentLifeCycle();
    
    public static ActionForward execute(HttpServletRequest request) throws Exception {
        return instance.doLifeCycle(request);
    }
    
    public static ComponentLifeCycle getInstance() {
        return ComponentLifeCycle.instance;
    }
    
    public ActionForward doLifeCycle(HttpServletRequest request) throws Exception {
        
        EditRequest editRequest = new EditRequest(request);
        IViewState viewState = editRequest.getViewState();

        HtmlComponent component = restoreComponent(viewState);
        
        viewState.setSkipUpdate(false);
        viewState.setCurrentDestination((ViewDestination) null);
        
        ComponentCollector collector = null;
        
        viewState.setUpdateComponentTree(true);
        while (viewState.getUpdateComponentTree()) {
            viewState.setUpdateComponentTree(false);
            
            collector = new ComponentCollector(viewState, component);           
            updateComponent(collector, editRequest);
            
            runControllers(collector, viewState);
            component = viewState.getComponent();
        }

        viewState.setValid(validateComponent(component));

        if (viewState.isValid()) {
            // updateMetaObject can get convert errors
            viewState.setValid(updateMetaObject(collector, editRequest));
        }
        
        if (viewState.isValid() && !viewState.skipUpdate()) {
            // updateDomain can get convert errors
            updateDomain(collector, editRequest);
        }

        ViewDestination destination = getDestination(viewState);
        prepareDestination(viewState, editRequest);

        return buildForward(destination);
    }
    
    private ViewDestination getDestination(IViewState viewState) {
        ViewDestination destination = viewState.getCurrentDestination();
        
        if (destination == null) {
            // TODO: remove hardcoded?
            destination = viewState.getDestination(viewState.isValid() ? "success" : "invalid");
        }
        
        if (destination == null && !viewState.isValid()) {
            destination = viewState.getInputDestination();
        }
        
        return destination;
    }

    private boolean validateComponent(HtmlComponent component) {
        boolean valid = true;

        List<HtmlComponent> validators = component.getChildren(new Predicate() {

            public boolean evaluate(Object component) {
                return component instanceof HtmlValidator;
            }
            
        });
        
        for (HtmlComponent validator : validators) {
            HtmlValidator htmlValidator = (HtmlValidator) validator;
            
            htmlValidator.performValidation();
            valid = valid && htmlValidator.isValid();
        }
        
        return valid;
    }

    private void runControllers(ComponentCollector collector, IViewState viewState) {
        for (HtmlController controller : collector.getControllers()) {
            HtmlFormComponent formComponent = (HtmlFormComponent) controller.getControlledComponent();
            
            if (formComponent != null) {
                controller.execute(new ViewStateWrapper(viewState, formComponent.getName()));
            }
            else {
                controller.execute(viewState);
            }
        }
    }

    public void prepareDestination(IViewState viewState, HttpServletRequest request)
            throws IOException, ClassNotFoundException {
        request.setAttribute(LifeCycleConstants.VIEWSTATE_PARAM_NAME, viewState);
    }

    public HtmlComponent restoreComponent(IViewState viewState) throws InstantiationException, IllegalAccessException {
        viewState.setPostBack(true);
        
        String layout = viewState.getLayout();
        Properties properties = viewState.getProperties();
        Class contextClass = viewState.getContextClass();
        
        InputContext context = (InputContext) contextClass.newInstance();
        context.setLayout(layout);
        context.setProperties(properties);
        context.setViewState(viewState);
        
        MetaObject metaObject = viewState.getMetaObject();
        metaObject.setUser(viewState.getUser());

        Object object = viewState.getMetaObject().getObject();
        viewState.setComponent(RenderKit.getInstance().render(context, object));
        viewState.setContext(context);

        return viewState.getComponent();
    }

    private void updateComponent(ComponentCollector collector, EditRequest editRequest) {
        List<HtmlFormComponent> formComponents = collector.getFormComponents();

        for (HtmlFormComponent formComponent : formComponents) {
            String name = formComponent.getName();

            if (formComponent instanceof HtmlMultipleValueComponent) {
                String[] values = editRequest.getParameterValues(name);

                if (values == null) {
                    values = new String[0];
                }

                ((HtmlMultipleValueComponent) formComponent).setValues(values);
            } else if (formComponent instanceof HtmlSimpleValueComponent) {
                String value = editRequest.getParameter(name);

                ((HtmlSimpleValueComponent) formComponent).setValue(value);
            }
        }
    }

    private void updateDomain(ComponentCollector collector, EditRequest editRequest) throws IOException, ClassNotFoundException {
        IViewState viewState = editRequest.getViewState();
        MetaObject metaObject = viewState.getMetaObject();
        
        metaObject.commit();
    }
    
    /**
     * @return true if no conversion error occurs
     */
    private boolean updateMetaObject(ComponentCollector collector, EditRequest editRequest) throws Exception {
        boolean hasConvertError = false;

        List<HtmlFormComponent> formComponents = collector.getFormComponents();       
        for (HtmlFormComponent formComponent : formComponents) {
            MetaSlotKey targetSlot = formComponent.getTargetSlot();
            
            if (targetSlot == null) {
                continue;
            }
            
            MetaSlot metaSlot = getMetaSlot(editRequest.getViewState().getMetaObject(), targetSlot);

            try {
                Object finalValue = formComponent.getConvertedValue(metaSlot);
                metaSlot.setObject(finalValue);
            } catch (Exception e) {
                logger.warn("failed to do conversion for slot " + metaSlot.getName() + ": " + e);

                addConvertError(editRequest, formComponent, metaSlot, e);
                hasConvertError = true;
            }
        }
        
        // TODO: this is confuse, it means it's valid if it does not have a convertion error
        return !hasConvertError;
    }

    private MetaSlot getMetaSlot(MetaObject metaObject, MetaSlotKey targetSlot) {
        // search in normal slots
        for (MetaSlot slot : metaObject.getSlots()) {
            if (slot.getKey().equals(targetSlot)) {
                return slot;
            }
        }
        
        // search in hidden slots
        for (MetaSlot slot : metaObject.getHiddenSlots()) {
            if (slot.getKey().equals(targetSlot)) {
                return slot;
            }
        }
        
        return null;
    }

    private void addConvertError(EditRequest editRequest, HtmlFormComponent formComponent, MetaSlot metaSlot, Exception exception) {
        // TODO: cfgi, really add errors to request
//        String message = RenderUtils.getResourceString("convert.error");
//
//        ActionMessages messages = getErrors(editRequest);
//        messages.add(editRequest.getViewState().getId(), new ActionMessage(message, false));
//
//        saveErrors(editRequest, messages);
    }

    private ActionForward buildForward(ViewDestination destination) {
        if (destination == null) {
            return null;
        }
        
        ActionForward forward = new ActionForward();

        forward.setPath(destination.getPath());
        forward.setModule(destination.getModule());
        forward.setRedirect(destination.getRedirect());

        return forward;
    }
}

