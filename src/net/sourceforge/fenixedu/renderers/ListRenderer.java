package net.sourceforge.fenixedu.renderers;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.layouts.ListLayout;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * This renderer provides a basic presentation for a {@link java.util.List}. There is
 * a direct translation from the list to an html list. As such, each object in the list
 * will be presented in a list item.
 * 
 * <p>
 * Example:
 * <ul>
 *  <li><em>&lt;object A presentation&gt;</em></li>
 *  <li><em>&lt;object B presentation&gt;</em></li>
 *  <li><em>&lt;object C presentation&gt;</em></li>
 * </ul>
 * 
 * @author cfgi
 */
public class ListRenderer extends OutputRenderer {
    private String eachClasses;

    private String eachStyle;

    private String eachSchema;

    private String eachLayout;

    private String sortBy;
    
    /**
     * The css classes to apply in each object's presentation.
     * 
     * @property
     */
    public void setEachClasses(String classes) {
        this.eachClasses = classes;
    }

    public String getEachClasses() {
        return this.eachClasses;
    }

    /**
     * The style to apply to each object's presentation.
     * 
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
     * The layout to be used when presenting each sub object.
     * 
     * @property
     */
    public void setEachLayout(String eachLayout) {
        this.eachLayout = eachLayout;
    }

    public String getEachSchema() {
        return eachSchema;
    }

    /**
     * The schema to be used in each sub object presentation.
     * 
     * @property
     */
    public void setEachSchema(String eachSchema) {
        this.eachSchema = eachSchema;
    }

    public String getSortBy() {
        return this.sortBy;
    }

    /**
     * Allows you to choose the order in wich the elements will be presented.-
     * See {@link net.sourceforge.fenixedu.renderers.utils.RenderUtils#sortCollectionWithCriteria(Collection, String)}
     * for more details.
     * 
     * @property
     */
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        Collection sortedCollection = RenderUtils.sortCollectionWithCriteria((Collection) object, getSortBy());
        
        return new ListRendererLayout((Collection) sortedCollection);
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
