package net.sourceforge.fenixedu.renderers;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.layouts.ListLayout;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;

public class ListRenderer extends OutputRenderer {
    private String eachClasses;

    private String eachStyle;

    private String eachSchema;

    private String eachLayout;

    private boolean eachInline = true;

    /**
     * @property
     */
    public void setEachClasses(String classes) {
        this.eachClasses = classes;
    }

    public String getEachClasses() {
        return this.eachClasses;
    }

    /**
     * @property
     */
    public void setEachStyle(String style) {
        this.eachStyle = style;
    }

    public String getEachStyle() {
        return this.eachStyle;
    }

    public String getEachLayout() {
        return eachLayout;
    }

    /**
     * @property
     */
    public void setEachLayout(String eachLayout) {
        this.eachLayout = eachLayout;
    }

    public String getEachSchema() {
        return eachSchema;
    }

    /**
     * @property
     */
    public void setEachSchema(String eachSchema) {
        this.eachSchema = eachSchema;
    }

    public boolean isEachInline() {
        return eachInline;
    }

    /**
     * @property
     */
    public void setEachInline(boolean eachInline) {
        this.eachInline = eachInline;
    }
    
    @Override
    protected Layout getLayout(Object object, Class type) {
        return new ListRendererLayout((Collection) object);
    }

    class ListRendererLayout extends ListLayout {
        
        private Iterator iterator;
        
        public ListRendererLayout(Collection collection) {
            iterator = collection == null ? null : collection.iterator();
        }

        @Override
        protected boolean hasMoreComponents() {
            return this.iterator != null && this.iterator.hasNext();
        }

        @Override
        protected HtmlComponent getNextComponent() {
            Object object = this.iterator.next(); 
            
            Schema schema = RenderKit.getInstance().findSchema(getEachSchema());
            String layout = getEachLayout();
            
            return renderValue(object, schema, layout);
        }
    }
}
