package net.sourceforge.fenixedu.renderers;

import java.util.Collection;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;

public class TabularListRenderer extends CollectionRenderer {

    private String subSchema;
    
    private String subLayout;
    
    public TabularListRenderer() {
        super();
    }
    
    public String getSubLayout() {
        return this.subLayout;
    }

    public void setSubLayout(String subLayout) {
        this.subLayout = subLayout;
    }

    public String getSubSchema() {
        return this.subSchema;
    }

    public void setSubSchema(String subSchema) {
        this.subSchema = subSchema;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new TabularListRendererLayout((Collection) object);
    }
    
    public class TabularListRendererLayout extends CollectionTabularLayout {

        public TabularListRendererLayout(Collection object) {
            super(object);
        }

        @Override
        protected int getNumberOfColumns() {
            return 1 + getNumberOfLinks();
        }

        @Override
        protected boolean hasHeader() {
            return false;
        }
        
        @Override
        protected HtmlComponent generateObjectComponent(int columnIndex, MetaObject object) {
            Schema schema = RenderKit.getInstance().findSchema(getSubSchema());
            String layout = getSubLayout();
            
            return renderValue(object.getObject(), schema, layout);
        }
    }
}
