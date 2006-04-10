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
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlActionLinkController;
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

/**
 * This renderer provides a tabular presentation for a collection that allows
 * rows of the table to be sorted by clicking the table headers. The behaviour
 * of this renderer is very similar to the one of {@link net.sourceforge.fenixedu.renderers.CollectionRenderer}.
 * The main difference is that the rows can be ordered and it does not support
 * control links.
 * 
 * <p>
 * Example:
 *  <table border="1">
 *      <thead>
 *          <th><a href="#">Name</a></th>
 *          <th>V <a href="#">Age</a></th>
 *      </thead>
 *      <tr>
 *          <td>Name A</td>
 *          <td>20</td>
 *      </tr>
 *      <tr>
 *          <td>Name C</td>
 *          <td>21</td>
 *      </tr>
 *      <tr>
 *          <td>Name B</td>
 *          <td>22</td>
 *      </tr>
 *  </table>
 * 
 * @author cfgi
 */
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

    public String getAscendingClasses() {
        return this.ascendingClasses;
    }
    
    public String getDescendingClasses() {
        return this.descendingClasses;
    }
    
    public String getAscendingImage() {
        return this.ascendingImage;
    }
    
    public String getDescendingImage() {
        return this.descendingImage;
    }
    
    public boolean isContextRelative() {
        return this.contextRelative;
    }

    /**
     * The table caption.
     * 
     * @property
     */
    public void setCaption(String caption) {
        this.collectionRenderer.setCaption(caption);
    }

    /**
     * {@inheritDoc}
     * 
     * @property
     */
    public void setClasses(String classes) {
        this.collectionRenderer.setClasses(classes);
    }

    /**
     * The classes to be used in each column of the table. See
     * {@link CollectionRenderer#setColumnClasses(String)} for 
     * more details.
     * 
     * @property
     */
    public void setColumnClasses(String columnClasses) {
        this.collectionRenderer.setColumnClasses(columnClasses);
    }

    /**
     * The classes to be used in each header cell. See
     * {@link CollectionRenderer#setHeaderClasses(String)} for 
     * more details.
     * 
     * @property
     */
    public void setHeaderClasses(String headerClasses) {
        this.collectionRenderer.setHeaderClasses(headerClasses);
    }

    /**
     * The string prefix to include in each cell of the table. See
     * {@link CollectionRenderer#setPrefixes(String)} for 
     * more details.
     * 
     * @property
     */
    public void setPrefixes(String prefixes) {
        this.collectionRenderer.setPrefixes(prefixes);
    }

    /**
     * The classes for the table's rows. See
     * {@link CollectionRenderer#setRowClasses(String)} for 
     * more details.
     * 
     * @property
     */
    public void setRowClasses(String rowClasses) {
        this.collectionRenderer.setRowClasses(rowClasses);
    }

    /**
     * The table style. 
     * 
     * @property
     */
    public void setStyle(String style) {
        this.collectionRenderer.setStyle(style);
    }

    /**
     * The string suffix to include in each cell of the table. See
     * {@link CollectionRenderer#setPrefixes(String)} for 
     * more details.
     * 
     * @property
     */
    public void setSuffixes(String suffixes) {
        this.collectionRenderer.setSuffixes(suffixes);
    }

    /**
     * The table html title.
     * 
     * @property
     */
    public void setTitle(String title) {
        this.collectionRenderer.setTitle(title);
    }

    /**
     * The classes to use in a header when the corresponding column is 
     * ordered in <strong>ascending</strong> mode. This property can be used 
     * to use a custom style that denotes an ascending ordering.
     * 
     * @property
     */
    public void setAscendingClasses(String ascendingClasses) {
        this.ascendingClasses = ascendingClasses;
    }

    /**
     * The classes to use in a header when the corresponding column is 
     * ordered in <strong>descending</strong> mode. This property can be 
     * used to use a custom style that denotes an descending ordering.
     * 
     * @property
     */
    public void setDescendingClasses(String descendingClasses) {
        this.descendingClasses = descendingClasses;
    }

    /**
     * If this property is specified an image will be placed to the left
     * of the header title. This image will be used when the header is
     * clicked and the ordering is <strong>ascending</strong>.
     * 
     * @property
     */
    public void setAscendingImage(String ascendingImage) {
        this.ascendingImage = ascendingImage;
    }

    /**
     * If this property is specified an image will be placed to the left
     * of the header title. This image will be used when the header is
     * clicked and the ordering is <strong>descending</strong>.
     * 
     * @property
     */
    public void setDescendingImage(String descendingImage) {
        this.descendingImage = descendingImage;
    }

    /**
     * This property specifies whether the image url given in 
     * {@link #setAscendingImage(String) ascendingImage} or 
     * {@link #setDescendingImage(String) descendingImage} is relative
     * to the application context or not. 
     * 
     * @property
     */
    public void setContextRelative(boolean contextRelative) {
        this.contextRelative = contextRelative;
    }

    /**
     * Allows you to choose the initial sorting of the table. See {@link CollectionRenderer#setSortBy(String)}
     * for details about the syntax accepted.
     * 
     * Note that although the you can specify an initial sorting based one several columns
     * the user will only be able to sort by one column at a time.
     * 
     * @property
     */
    public void setSortBy(String sortBy) {
        this.collectionRenderer.setSortBy(sortBy);
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
        inputContext.getForm().getCancelButton().setVisible(false);
        
        InputContext context = inputContext.createSubContext(getContext().getMetaObject());
        context.setRenderMode(RenderMode.getMode("output"));
        this.collectionRenderer.setContext(context);

        return this.collectionRenderer.new CollectionTabularLayout((Collection) object) {

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
    
    private class SortColumnController extends HtmlActionLinkController {

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
        public void linkPressed(IViewState viewState, HtmlActionLink link) {
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
