package net.sourceforge.fenixedu.renderers.components.state;

import java.io.IOException;
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
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.controllers.Controllable;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.contexts.InputContext;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectCollection;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.validators.HtmlValidator;

import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.taglib.html.Constants;

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
            
            if (component != null) {
                collect(component);
                
                InputContext context = (InputContext) viewState.getContext();
                if (context != null) {
                    collect(context.getForm().getSubmitButton());
                    collect(context.getForm().getCancelButton());
                }
                
                addHiddenComponents(viewState);
            }
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
    
        private void collect(HtmlComponent component) {
            Predicate isFormComponent = new Predicate() {
                public boolean evaluate(Object component) {
                    if (component instanceof HtmlFormComponent) {
                        HtmlFormComponent formComponent = (HtmlFormComponent) component;
    
                        if (formComponent.getName() != null) {
                            return true;
                        }
                    }
    
                    return false;
                }
            };

            List<HtmlComponent> components = HtmlFormComponent.getComponents(component, isFormComponent);
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
    
            components = HtmlFormComponent.getComponents(component, hasController);
            for (HtmlComponent comp : components) {
                this.controllers.add(((Controllable) comp).getController());
            }
        }
    }

    //
    // Main
    //
    
    private static ComponentLifeCycle instance = new ComponentLifeCycle();
    
    public static ComponentLifeCycle getInstance() {
        return ComponentLifeCycle.instance;
    }
    
    public static ActionForward execute(HttpServletRequest request) throws Exception {
        return instance.doLifeCycle(request);
    }
    
    public ActionForward doLifeCycle(HttpServletRequest request) throws Exception {
        
        EditRequest editRequest = new EditRequest(request);
        List<IViewState> viewStates = editRequest.getAllViewStates();

        boolean allValid = true;
        boolean anySkip = false;
        boolean anyCanceled = false;
        
        for (IViewState viewState : viewStates) {
            HtmlComponent component = restoreComponent(viewState);
            
            viewState.setValid(true);
            viewState.setSkipUpdate(false);
            viewState.setSkipValidation(false);
            viewState.setCurrentDestination((ViewDestination) null);
            
            if (cancelRequested(editRequest)) {
                doCancel(viewState);
                anyCanceled = true;
                continue;
            }
            
            ComponentCollector collector = null;
            
            viewState.setUpdateComponentTree(true);
            while (viewState.getUpdateComponentTree()) {
                viewState.setUpdateComponentTree(false);
                
                collector = new ComponentCollector(viewState, component);
                updateComponent(collector, editRequest);
                
                runControllers(collector, viewState);
                component = viewState.getComponent();
            }
    
            if (viewState.isVisible() && !viewState.skipUpdate()) { 
                if (! viewState.skipValidation()) {
                    viewState.setValid(validateComponent(viewState, component, viewState.getMetaObject()));
                }
            }
            
            if (viewState.isVisible() || isHiddenSlot(viewState)) {
                if (viewState.isValid()) {
                    // updateMetaObject can get conversion errors
                    viewState.setValid(updateMetaObject(collector, editRequest, viewState));
                }
            }
            
            allValid = allValid && viewState.isValid();
            anySkip = anySkip || viewState.skipUpdate();
        }

        ViewDestination destination;
        try {
            if (allValid && !anySkip && !anyCanceled) { 
                updateDomain(viewStates);
            }
        } 
        finally {
            destination = getDestination(viewStates);
            prepareDestination(viewStates, editRequest);
        }

        return buildForward(destination);
    }

    public static void doCancel(IViewState viewState) {
        viewState.setCurrentDestination("cancel");
        viewState.cancel();
    }
    
    private boolean cancelRequested(EditRequest editRequest) {
        return editRequest.getParameter(Constants.CANCEL_PROPERTY) != null 
            || editRequest.getParameter(Constants.CANCEL_PROPERTY_X) != null;
    }

    private boolean isHiddenSlot(IViewState viewState) {
        return viewState.getMetaObject() instanceof MetaSlot && viewState.getHiddenSlots().size() > 0;
    }
    
    private ViewDestination getDestination(List<IViewState> viewStates) {
        ViewDestination destination = null;
        
        for (IViewState viewState : viewStates) {
            // Invisible viewstates have no influence in the destination
            // because they were not validated and no controller was run
            // for them
            if (! viewState.isVisible()) {
                continue;
            }
            
            if (viewState.isCanceled()) {
                destination = viewState.getCurrentDestination();
            }
            else if (viewState.skipUpdate()) {
                destination = viewState.getCurrentDestination();
    
                if (destination == null) {
                    destination = viewState.getInputDestination();
                }
            }
            else {
                destination = viewState.getCurrentDestination();
            
                if (destination == null) {
                    // TODO: remove hardcoded?
                    destination = viewState.getDestination(viewState.isValid() ? "success" : "invalid");
                }
                
                if (destination == null && !viewState.isValid()) {
                    destination = viewState.getInputDestination();
                }
            }
            
            if (destination != null) {
                break;
            }
        }

        return destination;
    }

    private boolean validateComponent(IViewState viewState, HtmlComponent component, MetaObject metaObject) {
        boolean valid = true;
        
        List<HtmlComponent> validators = component.getChildren(new Predicate() {

            public boolean evaluate(Object component) {
                return component instanceof HtmlValidator;
            }
            
        });
        
        if (validators.isEmpty()) {
            List<HtmlComponent> formComponents = HtmlComponent.getComponents(component, new Predicate() {

                public boolean evaluate(Object object) {
                    if (! (object instanceof HtmlFormComponent)) {
                        return false;
                    }
                    
                    HtmlFormComponent formComponent = (HtmlFormComponent) object;
                    return formComponent.getTargetSlot() != null;
                }
                
            });
            
            if (! formComponents.isEmpty()) {
                for (HtmlComponent boundComponent : formComponents) {
                    HtmlFormComponent formComponent = (HtmlFormComponent) boundComponent;
                    HtmlValidator validator = formComponent.getValidator();
                    
                    if (validator == null) {
                        MetaSlotKey key = formComponent.getTargetSlot();
                        MetaSlot slot = getMetaSlot(metaObject, key);
                        
                        formComponent.setValidator(slot);
                        validator = formComponent.getValidator();
                    }
                    
                    if (validator != null) {
                        validators.add(validator);
                    }
                }
            }
        }

        for (HtmlComponent validator : validators) {
            HtmlValidator htmlValidator = (HtmlValidator) validator;
            
            htmlValidator.performValidation();
            valid = valid && htmlValidator.isValid();
            
            if (! htmlValidator.isValid()) { // validator message
                if (metaObject instanceof MetaSlot) {
                    viewState.addMessage(new ValidationMessage((MetaSlot) metaObject, htmlValidator.getErrorMessage()));
                }
                else {
                    HtmlFormComponent validatedFormComponent = (HtmlFormComponent) htmlValidator.getComponent();
                    MetaSlotKey key = validatedFormComponent.getTargetSlot();
                    
                    if (key != null) {
                        MetaSlot slot = getMetaSlot(metaObject, key);
                        
                        if (slot != null) {
                            viewState.addMessage(new ValidationMessage(slot, htmlValidator.getErrorMessage()));
                        }
                    }
                }
            }
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

    public void prepareDestination(List<IViewState> viewStates, HttpServletRequest request)
            throws IOException, ClassNotFoundException {
        request.setAttribute(LifeCycleConstants.VIEWSTATE_PARAM_NAME, viewStates);
    }

    public HtmlComponent restoreComponent(IViewState viewState) throws InstantiationException, IllegalAccessException {
        viewState.setPostBack(true);
        
        MetaObject metaObject = viewState.getMetaObject();
        
        if (metaObject == null) {
            viewState.setMetaObject(MetaObjectFactory.createObject(null, null));
            metaObject = viewState.getMetaObject();
        }
        
        metaObject.setUser(viewState.getUser());
    
        Class contextClass = viewState.getContextClass();
        if (contextClass != null) {
            String layout = viewState.getLayout();
            Properties properties = viewState.getProperties();

            InputContext context = (InputContext) contextClass.newInstance();
            context.setLayout(layout);
            context.setProperties(properties);
            
            context.setViewState(viewState);
            viewState.setContext(context);
        
            if (! viewState.isVisible()) {
                return new HtmlText();
            }
        
            if (isHiddenSlot(viewState)) {
                viewState.setComponent(new HtmlText());
            }
            else {
                Object object = metaObject.getObject();
                viewState.setComponent(RenderKit.getInstance().render(context, object, metaObject.getType()));
            }
        }
        
        HtmlComponent component = viewState.getComponent();
        return component != null ? component : new HtmlText();
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

    private void updateDomain(List<IViewState> viewStates) {
        List<MetaObject> metaObjectsToCommit = new ArrayList<MetaObject>();
        MetaObjectCollection metaObjectCollection = MetaObjectFactory.createObjectCollection();
        
        // TODO: check if should update viewstates that are not visible
        for (IViewState state : viewStates) {
            MetaObject metaObject = state.getMetaObject();
         
            if (metaObject instanceof MetaSlot) {
                metaObject = ((MetaSlot) metaObject).getMetaObject();
            }
            
            if (! metaObjectsToCommit.contains(metaObject)) {
                metaObjectsToCommit.add(metaObject);
            }

            metaObjectCollection.setUser(state.getUser());
        }
        
        for (MetaObject object : metaObjectsToCommit) {
            metaObjectCollection.add(object);
        }

        metaObjectCollection.commit();
    }
    
    /**
     * @return true if no conversion error occurs
     */
    private boolean updateMetaObject(ComponentCollector collector, EditRequest editRequest, IViewState viewState) throws Exception {
        boolean hasConvertError = false;

        List<HtmlFormComponent> formComponents = collector.getFormComponents();       
        for (HtmlFormComponent formComponent : formComponents) {
            MetaSlotKey targetSlot = formComponent.getTargetSlot();
            
            if (targetSlot == null) {
                continue;
            }

            MetaSlot metaSlot = getMetaSlot(viewState.getMetaObject(), targetSlot);

            if (metaSlot == null) {
                continue;
            }

            // ensure that slots marked as read-only are not changed
            if (metaSlot.isReadOnly()) {
                continue;
            }
            
            try {
                Object finalValue = formComponent.getConvertedValue(metaSlot);
                metaSlot.setObject(finalValue);
            } catch (Exception e) {
                logger.warn("failed to do conversion for slot " + metaSlot.getName() + ": " + e);
                addConvertError(viewState, metaSlot, e);
                hasConvertError = true;
            }
        }
        
        // TODO: this is confuse, it means it's valid if it does not have a conversion error
        return !hasConvertError;
    }

    private MetaSlot getMetaSlot(MetaObject metaObject, MetaSlotKey targetSlot) {
        if (metaObject instanceof MetaSlot) {
            if (metaObject.getKey().equals(targetSlot)) {
                return (MetaSlot) metaObject;
            }
            else {
                metaObject = ((MetaSlot) metaObject).getMetaObject(); 
            }
        }

        for (MetaSlot slot : metaObject.getAllSlots()) {
            if (slot.getKey().equals(targetSlot)) {
                return slot;
            }
        }
        
        return null;
    }

    private void addConvertError(IViewState viewState, MetaSlot metaSlot, Exception exception) {
        viewState.addMessage(new ConversionMessage(metaSlot, exception.getLocalizedMessage()));
    }

    private ActionForward buildForward(ViewDestination destination) {
        if (destination == null) {
            return null;
        }
        
        return destination.getActionForward();
    }

}

