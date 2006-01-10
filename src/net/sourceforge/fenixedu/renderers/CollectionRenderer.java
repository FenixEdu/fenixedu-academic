package net.sourceforge.fenixedu.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.layouts.TabularLayout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;

public class CollectionRenderer extends OutputRenderer {
    private String caption;

    private String rowClasses;

    private String columnClasses;

    private String headerClasses;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getColumnClasses() {
        return columnClasses;
    }

    public void setColumnClasses(String columnClasses) {
        this.columnClasses = columnClasses;
    }

    public String getHeaderClasses() {
        return headerClasses;
    }

    public void setHeaderClasses(String headerClasses) {
        this.headerClasses = headerClasses;
    }

    public String getRowClasses() {
        return rowClasses;
    }

    public void setRowClasses(String rowClasses) {
        this.rowClasses = rowClasses;
    }
    
    @Override
    protected Layout getLayout(Object object, Class type) {
        String schemaName = getContext().getMetaObject().getSchema();
        Schema schema = RenderKit.getInstance().findSchema(schemaName);
        
        List<MetaObject> metaObjects = new ArrayList<MetaObject>();
        
        Collection collection = (Collection) object;
        for (Iterator iter = collection.iterator(); iter.hasNext();) {
            Object collectionObject = (Object) iter.next();
            
            metaObjects.add(MetaObjectFactory.createObject(collectionObject, schema));
        }
        
        return new CollectionTabularLayout(metaObjects);
    }
    
    class CollectionTabularLayout extends TabularLayout {

        List<MetaObject> metaObjects;

        public CollectionTabularLayout(List<MetaObject> metaObjects) {
            this.metaObjects = metaObjects;
        }

        @Override
        protected boolean hasHeader() {
            return this.metaObjects.size() > 0;
        }
        
        @Override
        protected int getNumberOfColumns() {
            if (this.metaObjects.size() > 0) {
                MetaObject metaObject = this.metaObjects.get(0);
                return metaObject.getSlots().size();
            }
            else {
                return 0;
            }
        }

        @Override
        protected int getNumberOfRows() {
            return this.metaObjects.size();
        }

        private MetaObject getObject(int index) {
            return this.metaObjects.get(index);
        }
        
        @Override
        protected HtmlComponent getHeaderComponent(int columnIndex) {
            String slotLabel = getObject(0).getSlots().get(columnIndex).getLabel();
            return new HtmlText(slotLabel);
        }

        @Override
        protected HtmlComponent getComponent(int rowIndex, int columnIndex) {
            MetaObject object = getObject(rowIndex);
            getContext().setMetaObject(object);
            
            return renderSlot(object.getSlots().get(columnIndex));
        }

    }
}
