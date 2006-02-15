package net.sourceforge.fenixedu.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.components.HtmlActionLink;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlMultipleHiddenField;
import net.sourceforge.fenixedu.renderers.components.HtmlTable;
import net.sourceforge.fenixedu.renderers.components.HtmlTableRow;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlActionLinkController;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.layouts.TabularLayout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderMode;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.collections.Predicate;

public class ManageableListRenderer extends InputRenderer {

    private String destination;
    
    private String eachSchema;
    
    private String eachLayout;
    
    public String getEachLayout() {
        return this.eachLayout;
    }

    public void setEachLayout(String eachLayout) {
        this.eachLayout = eachLayout;
    }

    public String getEachSchema() {
        return this.eachSchema;
    }

    public void setEachSchema(String eachSchema) {
        this.eachSchema = eachSchema;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        MetaObject metaObject = getInputContext().getMetaObject();
        
        return new ManageableListLayout((Collection) metaObject.getObject()); 
    }

    private class ManageableListLayout extends TabularLayout {

        private List objects;
        private HtmlMultipleHiddenField hiddenValues;

        public ManageableListLayout(Collection collection) {
            this.objects = new ArrayList(collection);
            
            this.hiddenValues = new HtmlMultipleHiddenField();
            this.hiddenValues.setTargetSlot((MetaSlotKey) getInputContext().getMetaObject().getKey());
            
            // HACK: severe dependecy with fénix project
            this.hiddenValues.setConverter(new DomainObjectKeyArrayConverter());
        }

        @Override
        protected int getNumberOfColumns() {
            return 2;
        }

        @Override
        protected int getNumberOfRows() {
            return this.objects == null ? 0 : this.objects.size();
        }

        @Override
        protected HtmlComponent getHeaderComponent(int columnIndex) {
            return null;
        }

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            HtmlComponent component = super.createComponent(object, type);
            
            HtmlContainer container = new HtmlBlockContainer();
            
            container.addChild(component);
            container.addChild(this.hiddenValues);
            
            HtmlActionLink link = new HtmlActionLink();
            link.setName(getInputContext().getMetaObject().getKey().toString() + "/add");
            link.setText(RenderUtils.getResourceString("list.management.add")); 
            link.setController(new FollowDestinationController());
            
            container.addChild(link);
            
            return container;
        }

        @Override
        public void applyStyle(HtmlComponent component) {
            HtmlContainer container = (HtmlContainer) component;
            
            super.applyStyle(container.getChildren().get(0));
        }

        @Override
        protected HtmlComponent getComponent(int rowIndex, int columnIndex) {
            Object object = this.objects.get(rowIndex);
            
            Schema schema = RenderKit.getInstance().findSchema(getEachSchema());
            String layout = getEachLayout();
            
            MetaObject metaObject = MetaObjectFactory.createObject(object, schema);

            if (columnIndex == 0) { 
                this.hiddenValues.addValue(metaObject.getKey().toString());
                
                PresentationContext newContext = getContext().createSubContext(metaObject);
                newContext.setLayout(layout);
                newContext.setRenderMode(RenderMode.getMode("output"));
                
                RenderKit kit = RenderKit.getInstance();
                return kit.render(newContext, object);                
            }
            else {
                HtmlActionLink link = new HtmlActionLink();
                
                String prefix = getInputContext().getMetaObject().getKey().toString();
                link.setName(prefix + "/delete/" + rowIndex);
                link.setText(RenderUtils.getResourceString("list.management.delete"));
                
                HtmlTableRow row = getTable().getRows().get(rowIndex);
                link.setController(new RemoveLineController(getTable(), this.hiddenValues, row, prefix));
                return link;
            }

        }
        
    }
    
    class RemoveLineController extends HtmlActionLinkController {

        private HtmlTable table;
        private HtmlTableRow row;
        private HtmlMultipleHiddenField values;
        private String prefix;
        
        public RemoveLineController(HtmlTable table, HtmlMultipleHiddenField values, HtmlTableRow row, String prefix) {
            this.table = table;
            this.values = values;
            this.row = row;
            this.prefix = prefix;
        }

        @Override
        public void linkPressed(IViewState viewState, HtmlActionLink link) {
            int index = this.table.getRows().indexOf(row);
            
            this.table.removeRow(this.row);
            this.values.removeValue(index);
            
            renameLinks();
        }

        private void renameLinks() {
            List<HtmlComponent> links = this.table.getChildren(new Predicate() {

                public boolean evaluate(Object object) {
                    return object instanceof HtmlActionLink;
                }
                
            });
            
            int pos = 0;
            for (HtmlComponent component : links) {
                ((HtmlActionLink) component).setName(this.prefix + "/delete/" + pos++);
            }
        }

    }
    
    class FollowDestinationController extends HtmlActionLinkController {

        @Override
        public void linkPressed(IViewState viewState, HtmlActionLink link) {
            if (getDestination() != null) {
                viewState.setCurrentDestination(getDestination());
            }
        }
        
    }
}
