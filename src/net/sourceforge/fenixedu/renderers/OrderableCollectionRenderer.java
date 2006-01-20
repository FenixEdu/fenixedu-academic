package net.sourceforge.fenixedu.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.renderers.components.HtmlActionLink;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlImage;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlTable;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell;
import net.sourceforge.fenixedu.renderers.components.HtmlTableHeader;
import net.sourceforge.fenixedu.renderers.components.HtmlTableRow;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.contexts.InputContext;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderMode;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;

public class OrderableCollectionRenderer extends InputRenderer {
    private static String ORDER_ATTRIBUTE_NAME = OrderableCollectionRenderer.class.getName() + "/ascending";
    private static String COLUMN_ATTRIBUTE_NAME = OrderableCollectionRenderer.class.getName() + "/column";
    
    private String ascendingClasses;
    
    private String descendingClasses;
    
    private String ascendingImage;
    
    private String descendingImage;

    private boolean contextRelative;
    
    private CollectionRenderer collectionRenderer;
    
    public OrderableCollectionRenderer() {
        this.collectionRenderer = new CollectionRenderer();
        
        setContextRelative(true);
    }
    
    public String getCaption() {
        return this.collectionRenderer.getCaption();
    }

    public String getClasses() {
        return this.collectionRenderer.getClasses();
    }

    public String getColumnClasses() {
        return this.collectionRenderer.getColumnClasses();
    }

    public String getHeaderClasses() {
        return this.collectionRenderer.getHeaderClasses();
    }

    public String getPrefixes() {
        return this.collectionRenderer.getPrefixes();
    }

    public String getRowClasses() {
        return this.collectionRenderer.getRowClasses();
    }

    public String getStyle() {
        return this.collectionRenderer.getStyle();
    }

    public String getSuffixes() {
        return this.collectionRenderer.getSuffixes();
    }

    public String getTitle() {
        return this.collectionRenderer.getTitle();
    }

    public void setCaption(String caption) {
        this.collectionRenderer.setCaption(caption);
    }

    public void setClasses(String classes) {
        this.collectionRenderer.setClasses(classes);
    }

    public void setColumnClasses(String columnClasses) {
        this.collectionRenderer.setColumnClasses(columnClasses);
    }

    public void setHeaderClasses(String headerClasses) {
        this.collectionRenderer.setHeaderClasses(headerClasses);
    }

    public void setPrefixes(String prefixes) {
        this.collectionRenderer.setPrefixes(prefixes);
    }

    public void setRowClasses(String rowClasses) {
        this.collectionRenderer.setRowClasses(rowClasses);
    }

    public void setStyle(String style) {
        this.collectionRenderer.setStyle(style);
    }

    public void setSuffixes(String suffixes) {
        this.collectionRenderer.setSuffixes(suffixes);
    }

    public void setTitle(String title) {
        this.collectionRenderer.setTitle(title);
    }

    public String getAscendingClasses() {
        return this.ascendingClasses;
    }

    public void setAscendingClasses(String ascendingClasses) {
        this.ascendingClasses = ascendingClasses;
    }

    public String getDescendingClasses() {
        return this.descendingClasses;
    }

    public void setDescendingClasses(String descendingClasses) {
        this.descendingClasses = descendingClasses;
    }

    public String getAscendingImage() {
        return this.ascendingImage;
    }

    public void setAscendingImage(String ascendingImage) {
        this.ascendingImage = ascendingImage;
    }

    public String getDescendingImage() {
        return this.descendingImage;
    }

    public void setDescendingImage(String descendingImage) {
        this.descendingImage = descendingImage;
    }

    public boolean isContextRelative() {
        return this.contextRelative;
    }

    public void setContextRelative(boolean contextRelative) {
        this.contextRelative = contextRelative;
    }

    private String getImagePath(String path) {
        if (isContextRelative()) {
            return getContext().getViewState().getRequest().getContextPath() + path;    
        }
        else {
            return path;
        }
    }
    
    @Override
    protected Layout getLayout(Object object, Class type) {
        InputContext inputContext = getInputContext();
        inputContext.getForm().getSubmitButton().setVisible(false);
        inputContext.getForm().getResetButton().setVisible(false);
        
        InputContext context = inputContext.createSubContext(getContext().getMetaObject());
        context.setRenderMode(RenderMode.getMode("output"));

        CollectionRenderer collectionRenderer = new CollectionRenderer();
        collectionRenderer.setContext(context);

        return collectionRenderer.new CollectionTabularLayout((Collection) object) {

            @Override
            protected HtmlComponent getHeaderComponent(int columnIndex) {
                MetaObject metaObject = getObject(0);
                MetaSlot slot = metaObject.getSlots().get(columnIndex);
                
                HtmlActionLink actionLink = new HtmlActionLink(slot.getLabel());
                actionLink.setName(slot.getKey().toString());
                actionLink.setController(new SortColumnController(getTable(), metaObject, slot, columnIndex));
                
                HtmlComponent component =  super.getHeaderComponent(columnIndex);
                actionLink.setBody(component);
                
                return actionLink;
            }

        };
    }
    
    private class SortColumnController extends HtmlController {

        private HtmlTable table;
        private MetaObject metaObject;
        private MetaSlot slot;
        private int index;
        
        public SortColumnController(HtmlTable table, MetaObject metaObject, MetaSlot slot, int index) {
            super();

            setTable(table);
            setMetaObject(metaObject);
            setSlot(slot);
            setIndex(index);
        }
        
        public HtmlTable getTable() {
            return this.table;
        }

        public void setTable(HtmlTable table) {
            this.table = table;
        }

        public int getIndex() {
            return this.index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public MetaObject getMetaObject() {
            return this.metaObject;
        }

        public void setMetaObject(MetaObject metaObject) {
            this.metaObject = metaObject;
        }

        public MetaSlot getSlot() {
            return this.slot;
        }

        public void setSlot(MetaSlot slot) {
            this.slot = slot;
        }

        @Override
        public void execute(IViewState viewState) {
            HtmlActionLink link = (HtmlActionLink) getControlledComponent();
            
            if (link.isActivated()) {
                viewState.setSkipUpdate(true);
                
                Boolean ascending = (Boolean) viewState.getAttribute(ORDER_ATTRIBUTE_NAME);
                Integer index = (Integer) viewState.getAttribute(COLUMN_ATTRIBUTE_NAME);
                
                if (ascending != null && getIndex() == index.intValue()) {
                    ascending = new Boolean(! ascending.booleanValue());
                }
                else {
                    ascending = new Boolean(true);
                    index = getIndex();
                }
                
                viewState.setAttribute(ORDER_ATTRIBUTE_NAME, ascending);
                viewState.setAttribute(COLUMN_ATTRIBUTE_NAME, index);
                
                List objects = new ArrayList((Collection) viewState.getMetaObject().getObject());
                List sortedObjects = new ArrayList(objects);
                
                if (ascending) {
                    Collections.sort(sortedObjects, new BeanComparator(getSlot().getName()));
                }
                else {
                    Collections.sort(sortedObjects, new ReverseComparator(new BeanComparator(getSlot().getName())));
                }
                
                Schema schema = RenderKit.getInstance().findSchema(getMetaObject().getSchema());
                viewState.setMetaObject(MetaObjectFactory.createObject(sortedObjects, schema));
                
                List<HtmlTableRow> rows = getTable().getRows();
                Collections.sort(rows, new RowComparator(rows, objects, sortedObjects));
                
                applySelectedStyle(getTable(), ascending, index);
            }
        }
        
        private void applySelectedStyle(HtmlTable table, Boolean ascending, Integer index) {
            HtmlTableHeader header = table.getHeader();
            
            HtmlTableCell cell = header.getRows().get(0).getCells().get(index);
            
            if (ascending) {
                if (getAscendingClasses() != null) {
                    cell.setClasses(getAscendingClasses());
                }
                
                if (getAscendingImage() != null) {
                    addImage(cell, getAscendingImage());
                }
            }
            else {
                if (getDescendingClasses() != null) {
                    cell.setClasses(getDescendingClasses());
                }
                
                if (getDescendingImage() != null) {
                    addImage(cell, getDescendingImage());
                }
            }
        }
        
        private void addImage(HtmlTableCell cell, String imageSource) {
            HtmlActionLink link = (HtmlActionLink) cell.getBody();
            HtmlComponent body = link.getBody();
            
            HtmlImage image = new HtmlImage();
            image.setSource(getImagePath(imageSource));
            
            HtmlInlineContainer container = new HtmlInlineContainer();
            container.addChild(body);
            container.addChild(image);
            
            link.setBody(container);
        }

        private class RowComparator implements Comparator<HtmlTableRow> {

            private List<HtmlTableRow> allRows;
            private List unsortedObjects;
            private List sortedObjects;

            public RowComparator(List<HtmlTableRow> rows, List objects, List sortedObjects) {
                this.allRows = rows;
                this.unsortedObjects = objects;
                this.sortedObjects = sortedObjects;
            }

            public int compare(HtmlTableRow row1, HtmlTableRow row2) {
                int index1 = getIndex(row1);
                int index2 = getIndex(row2);
                
                if (index1 == index2) {
                    return 0;
                }
                else {
                    return index1 < index2 ? -1 : 1;
                }
            }

            private int getIndex(HtmlTableRow row) {
                int index = this.allRows.indexOf(row);
                Object object = this.unsortedObjects.get(index);
                
                return this.sortedObjects.indexOf(object);
            }
            
        }
        
    }
}
