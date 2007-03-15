package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * This renderer protects the presentation of the <code>null</code> value. If
 * you need to present the values of some slots but the holder object is null
 * you can use this renderer to present a message instead. This render can also
 * be used to done the unexistance of some value. Instead of having a blank
 * presentation you can change the presentation of the value of a slot into a
 * small marker when the slot's value is <code>null</code>.
 * 
 * <p>
 * Example:
 * <table>
 *  <tr>
 *      <td>Name:</td><td>John Doe</td>
 *  </tr>
 *  <tr>
 *      <td>Age:</td><td>67</td>
 *  </tr>
 *  <tr>
 *      <td>Address:</td><td>--</td>
 *  </tr>
 * </table>
 * 
 * @author cfgi
 */
public class NullAsLabelRenderer extends OutputRenderer {

    private String subLayout;
    private String subSchema;

    private String label;
    private boolean key;
    private String bundle;

    public String getBundle() {
        return this.bundle;
    }

    /**
     * The bundle used.
     * 
     * @property
     */
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public boolean isKey() {
        return this.key;
    }

    /**
     * Indicates if the label is a resource is a key.
     * 
     * @property
     */
    public void setKey(boolean key) {
        this.key = key;
    }

    public String getLabel() {
        return this.label;
    }

    /**
     * The label to show when the value is <code>null</code>.
     * 
     * @property
     */
    public void setLabel(String label) {
        this.label = label;
    }

    public String getSubLayout() {
        return this.subLayout;
    }

    /**
     * The layout used to represent the object.
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
     * The schema used when representing the object.
     * 
     * @property
     */
    public void setSubSchema(String subSchema) {
        this.subSchema = subSchema;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                if (object == null || object.equals("")) {
                    if (isKey()) {
                        return new HtmlText(RenderUtils.getResourceString(getBundle(), getLabel()), false);
                    } else {
                        return new HtmlText(getLabel());
                    }
                }
                else {
                    Schema schema = RenderKit.getInstance().findSchema(getSubSchema());
                    return renderValue(object, type, schema, getSubLayout());
                }
            }

        };
    }

}
