package net.sourceforge.fenixedu.renderers;

import java.util.Collection;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * This renderer allows you to present a collection of objects as a
 * list but at the same time allows you to add control links to 
 * each item like you can in {@link net.sourceforge.fenixedu.renderers.CollectionRenderer}.
 * A table is used to accomplish this. Each object will be presented
 * in a row but the left column will contain the entire presentation of the object
 * and further column will contain the defined control links.
 * 
 * <p>
 * Example:
 * <table border="1">
 *  <thead>
 *      <th>List title</th>
 *      <th></th>
 *      <th></th>
 *  </thead> 
 *  <tr>
 *      <td><em>&lt;object A presentation&gt;</em></td>
 *      <td><a href="#">Edit</a></td>
 *      <td><a href="#">Delete</a></td>
 *  </tr>
 *  <tr>
 *      <td><em>&lt;object B presentation&gt;</em></td>
 *      <td><a href="#">Edit</a></td>
 *      <td><a href="#">Delete</a></td>
 *  </tr>
 *  <tr>
 *      <td><em>&lt;object C presentation&gt;</em></td>
 *      <td><a href="#">Edit</a></td>
 *      <td><a href="#">Delete</a></td>
 *  </tr>
 * </table>
 * 
 * @author cfgi
 */
public class TabularListRenderer extends CollectionRenderer {

    private String subSchema;
    
    private String subLayout;
    
    private String listTitle;
    
    private boolean listTitleKey;
    
    private String listTitleBundle;
    
    private Boolean displayHeaders = Boolean.TRUE;
    
    public TabularListRenderer() {
        super();
        
        this.listTitleKey = false;
    }
    
    public String getSubLayout() {
        return this.subLayout;
    }

    /**
     * With this property you can choose the layout to be used
     * in each object's presentation. 
     * 
     * @property
     */
    public void setSubLayout(String subLayout) {
        this.subLayout = subLayout;
    }

    public String getSubSchema() {
        return this.subSchema;
    }

    /**
     * With this property you can choose if you want to
     * display headers or not in the table
     * @property
     */
    public boolean getDisplayHeaders() {
        return displayHeaders;
    }

    public void setDisplayHeaders(boolean displayHeaders) {
        this.displayHeaders = displayHeaders;
    }
    
    /**
     * Specifies the schema that will be used when presenting
     * each sub object.
     * 
     * @property
     */
    public void setSubSchema(String subSchema) {
        this.subSchema = subSchema;
    }

    public String getListTitle() {
        return this.listTitle;
    }

    /**
     * Allows you to set the title of this listing.
     * 
     * @property
     */
    public void setListTitle(String listTitle) {
        this.listTitle = listTitle;
    }

    public boolean isListTitleKey() {
        return this.listTitleKey;
    }

    /**
     * Indicates the the value of the property {@link #setListTitle(String) listTitle} is a resource key. 
     * 
     * @property
     */
    public void setListTitleKey(boolean listTitleKey) {
        this.listTitleKey = listTitleKey;
    }

    public String getListTitleBundle() {
        return this.listTitleBundle;
    }

    /**
     * The name of the resource bundle to use for the list title.
     * 
     * @property
     */
    public void setListTitleBundle(String listTitleBundle) {
        this.listTitleBundle = listTitleBundle;
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
            return 1 + getNumberOfLinks() + (isCheckable() ? 1 : 0);
        }

        @Override
        protected boolean hasHeader() {
            return (displayHeaders) ? getListTitle() != null : false;
        }
        
        @Override
        protected HtmlComponent getHeaderComponent(int columnIndex) {
            if (columnIndex == (isCheckable() ? 1 : 0)) {
                String description;
                
                if (isListTitleKey()) {
                    description = RenderUtils.getResourceString(getListTitleBundle(), getListTitle());
                }
                else {
                    description = getListTitle();
                }
                
                return new HtmlText(description);
            }
            else {
                return new HtmlText();
            }
        }

        @Override
        protected HtmlComponent generateObjectComponent(int columnIndex, MetaObject object) {
            Schema schema = RenderKit.getInstance().findSchema(getSubSchema());
            String layout = getSubLayout();
            
            return renderValue(object.getObject(), schema, layout);
        }
    }


}
