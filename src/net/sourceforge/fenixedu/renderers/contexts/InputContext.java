package net.sourceforge.fenixedu.renderers.contexts;

import net.sourceforge.fenixedu.renderers.components.HtmlForm;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.ViewStateWrapper;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectKey;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.model.UserIdentity;
import net.sourceforge.fenixedu.renderers.utils.RenderMode;

public class InputContext extends PresentationContext {

    private HtmlForm form;

    public InputContext() {
        super();
        
        setRenderMode(RenderMode.getMode("input"));
    }

    protected InputContext(InputContext parent) {
        super(parent);
    }
    
    @Override
    public IViewState getViewState() {
        IViewState viewState = super.getViewState();
        
        if (getMetaObject() instanceof MetaSlot) {
            MetaObjectKey key = getMetaObject().getKey();
            
            if (key != null) {
                String prefix = key.toString();
                
                if (viewState instanceof ViewStateWrapper) {
                    ViewStateWrapper wrapper = (ViewStateWrapper) viewState;
                    
                    if (prefix.equals(wrapper.getPrefix())) {
                        return wrapper;
                    }
                    else {
                        return new ViewStateWrapper(viewState, prefix);
                    }
                }
                else {
                    return new ViewStateWrapper(viewState, prefix);
                }
            }
            else {
                return viewState;
            }
        }
        else {
            return viewState;
        }
    }

    protected UserIdentity getUser() {
        return getViewState().getUser();
    }
    
    public HtmlForm getForm() {
        if (getParentContext() == null || !(getParentContext() instanceof InputContext)) {
            if (this.form == null) {
                this.form = new HtmlForm();
            }
            
            return this.form;
        }
        else {
            return ((InputContext) getParentContext()).getForm();
        }
    }

    @Override
    public InputContext createSubContext(MetaObject metaObject) {
        InputContext context = new InputContext(this);
        
        // TODO: check this and compare with the version in OutputContext
        context.setLayout(getLayout());
        context.setMetaObject(metaObject);
        context.setProperties(metaObject.getProperties());

        context.setRenderMode(getRenderMode());

        return context;
    }
}
