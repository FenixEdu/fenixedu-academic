package net.sourceforge.fenixedu.renderers;

import java.util.Iterator;
import java.util.Properties;

import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.FlowLayout;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;

public class ValuesRenderer extends OutputRenderer {

    private String eachClasses;

    private String eachStyle;

    private boolean eachInline = true;

    private String eachLayout;
    
    private String eachSchema;
    
    private String htmlSeparator;
    
    public void setEachClasses(String classes) {
        this.eachClasses = classes;
    }

    public String getEachClasses() {
        return this.eachClasses;
    }

    public void setEachStyle(String style) {
        this.eachStyle = style;
    }

    public String getEachStyle() {
        return this.eachStyle;
    }

    public boolean isEachInline() {
        return eachInline;
    }

    public void setEachInline(boolean eachInline) {
        this.eachInline = eachInline;
    }

    public String getEachLayout() {
        return eachLayout;
    }

    public void setEachLayout(String eachLayout) {
        this.eachLayout = eachLayout;
    }

    public String getEachSchema() {
        return eachSchema;
    }

    public void setEachSchema(String eachSchema) {
        this.eachSchema = eachSchema;
    }

    public ValuesRenderer() {
        super();
    }

    public String getHtmlSeparator() {
        return htmlSeparator;
    }

    public void setHtmlSeparator(String htmlSeparator) {
        this.htmlSeparator = htmlSeparator;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new ValuesLayout(getContext().getMetaObject());
    }

    class ValuesLayout extends FlowLayout {

        private Iterator<MetaSlot> slotsIterator;
        private boolean insertSeparator;
        
        public ValuesLayout(MetaObject object) {
            super();
            
            this.slotsIterator = object.getSlots().iterator();
            this.insertSeparator = false;
        }

        @Override
        protected HtmlComponent getContainer() {
            return new HtmlInlineContainer();
        }

        @Override
        protected boolean hasMoreComponents() {
            return slotsIterator.hasNext() || this.insertSeparator;
        }

        @Override
        protected HtmlComponent getNextComponent() {
            if (this.insertSeparator) {
                this.insertSeparator = false;
                return new HtmlText(getHtmlSeparator());
            }
            else if (hasMoreComponents() && getHtmlSeparator() != null) {
                this.insertSeparator = true;
            }
            
            MetaSlot slot = this.slotsIterator.next();
            Schema schema = schema = RenderKit.getInstance().findSchema(slot.getSchema());
            String layout = slot.getLayout();
            
            if (schema == null) {
                schema = RenderKit.getInstance().findSchema(getEachSchema());
            }
            
            if (layout == null) {
                layout = getEachLayout();
            }
            
            HtmlComponent component = ValuesRenderer.this.renderValue(slot.getObject(), schema, layout, slot.getProperties());
            HtmlContainer container;
            
            if (isEachInline()) {
                container = new HtmlInlineContainer();
            }
            else {
                container = new HtmlBlockContainer();
            }
            
            container.addChild(component);
            return container;
        }

        @Override
        protected void addComponent(HtmlComponent container, HtmlComponent component) {
            ((HtmlContainer) container).addChild(component);
        }
    }
}
