package net.sourceforge.fenixedu.renderers.contexts;

import net.sourceforge.fenixedu.renderers.components.HtmlForm;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.ViewStateWrapper;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.model.UserIdentity;
import net.sourceforge.fenixedu.renderers.utils.RenderMode;

public class InputContext extends PresentationContext {

    private HtmlForm form;

    public InputContext() {
        super();
    }

    protected InputContext(InputContext parent) {
        super(parent);
    }
    
    @Override
    public IViewState getViewState() {
        IViewState viewState = super.getViewState();
        
        if (getMetaObject() instanceof MetaSlot) {
            return new ViewStateWrapper(viewState, getMetaObject().getKey().toString());
        }
        else {
            return viewState;
        }
    }

    @Override
    public RenderMode getRenderMode() {
        return RenderMode.getMode("input");
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
        
        context.setLayout(getLayout());
        context.setMetaObject(metaObject);
        context.setProperties(metaObject.getProperties());
        
        return context;
    }
}
