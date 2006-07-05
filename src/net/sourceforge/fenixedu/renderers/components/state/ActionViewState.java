package net.sourceforge.fenixedu.renderers.components.state;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;

public class ActionViewState extends ViewState {

    private HtmlComponent component;

    public ActionViewState() {
        super();
    }

    public ActionViewState(String id) {
        super(id);
    }
    
    public ActionViewState(String id, Object object) {
        this(id, object, (Schema) null);
    }

    public ActionViewState(String id, Object object, Schema schema) {
        this(id);
        
        setMetaObject(MetaObjectFactory.createObject(object, schema));
    }

    public ActionViewState(String id, Object object, String schemaName) {
        this(id, object, RenderKit.getInstance().findSchema(schemaName));
    }

    @Override
    public HtmlComponent getComponent() {
        return this.component;
    }

    @Override
    public void setComponent(HtmlComponent component) {
        this.component = component;
    }

}
