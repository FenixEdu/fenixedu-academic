package net.sourceforge.fenixedu.renderers;

import java.util.Collection;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;

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
    
    public TabularListRenderer() {
        super();
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
     * Specifies the schema that will be used when presenting
     * each sub object.
     * 
     * @property
     */
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
