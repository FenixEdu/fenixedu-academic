package net.sourceforge.fenixedu.renderers.components.state;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlFormComponent;
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
import org.apache.struts.action.ActionForward;

public class ComponentLifeCycle {
    private static ComponentLifeCycle instance = new ComponentLifeCycle();
    
    public static ActionForward execute(HttpServletRequest request) throws Exception {
        return instance.doLifeCycle(request);
    }
    
    public ActionForward doLifeCycle(HttpServletRequest request) throws Exception {
        
        EditRequest editRequest = new EditRequest(request);
        IViewState viewState = editRequest.getViewState();

        HtmlComponent component = restoreComponent(editRequest);
        
        viewState.setSkipUpdate(false);
        viewState.setCurrentDestination((ViewDestination) null);
        
        ComponentCollector collector = null;
        
        viewState.setUpdateComponentTree(true);
        while (viewState.getUpdateComponentTree()) {
            viewState.setUpdateComponentTree(false);
            
            collector = new ComponentCollector(component);
            updateComponent(collector, editRequest);
            
            runControllers(collector, viewState);
            component = viewState.getComponent();
        }

        viewState.setValid(validateComponent(component));

        if (viewState.isValid() && !viewState.skipUpdate()) {
            // updateDomain can get convert errors
            viewState.setValid(updateDomain(collector, editRequest));
        }

        ViewDestination destination = getDestination(viewState);
        prepareDestination(component, editRequest);

        return buildForward(destination);
    }

    private ViewDestination getDestination(IViewState viewState) {
        ViewDestination destination = viewState.getCurrentDestination();
        
        if (destination == null) {
            // TODO: remove hardcoded?
            destination = viewState.getDestination(viewState.isValid() ? "success" : "invalid");
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
            controller.execute(viewState);
        }
    }

    private void prepareDestination(HtmlComponent component, EditRequest editRequest)
            throws IOException, ClassNotFoundException {
        editRequest.setAttribute(LifeCycleConstants.VIEWSTATE_PARAM_NAME, editRequest.getViewState());
    }

    private HtmlComponent restoreComponent(EditRequest editRequest) throws IOException, ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        IViewState viewState = editRequest.getViewState();
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
            } else {
                String value = editRequest.getParameter(name);

                ((HtmlSimpleValueComponent) formComponent).setValue(value);
            }
        }
    }

    /**
     * @return true if no conversion error occurs
     */
    private boolean updateDomain(ComponentCollector collector, EditRequest editRequest) throws Exception {
        boolean hasConvertError = false;

        List<MetaObject> objectsToCommit = new ArrayList<MetaObject>();

        List<HtmlFormComponent> formComponents = collector.getFormComponents();
        for (HtmlFormComponent formComponent : formComponents) {
            MetaSlotKey targetSlot = formComponent.getTargetSlot();
            
            if (targetSlot == null){
                continue;
            }
            
            MetaSlot metaSlot = getMetaSlot(editRequest.getViewState().getMetaObject(), targetSlot);

            try {
                Object finalValue = formComponent.getConvertedValue(metaSlot.getType());
                metaSlot.setObject(finalValue);
                
                objectsToCommit.add(metaSlot.getMetaObject());
            } catch (Exception e) {
                e.printStackTrace();

                addConvertError(editRequest);
                hasConvertError = true;
            }
        }
        
        if (!hasConvertError) {
            List<MetaObject> commitedObjects = new ArrayList<MetaObject>();

            for (MetaObject object : objectsToCommit) {
                if (! commitedObjects.contains(object)) {
                    object.commit();
                    commitedObjects.add(object);
                }
            }
        }
        
        // TODO: this is confuse, it means it's valid if it does not have a convertion error
        return !hasConvertError;
    }

    private MetaSlot getMetaSlot(MetaObject metaObject, MetaSlotKey targetSlot) {
        for (MetaSlot slot : metaObject.getSlots()) {
            if (slot.getKey().equals(targetSlot)) {
                return slot;
            }
        }
        
        return null;
    }

    private void addConvertError(EditRequest editRequest)
            throws IOException, ClassNotFoundException {
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

//
// Utility classes
//

class ComponentCollector {

    private List<HtmlFormComponent> formComponents;

    private List<HtmlController> controllers;

    public ComponentCollector(HtmlComponent component) {
        this.formComponents = new ArrayList<HtmlFormComponent>();
        this.controllers = new ArrayList<HtmlController>();

        visit(component);
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

