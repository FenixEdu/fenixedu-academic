package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * This renderer allows you to handle cases were you don't have an enum
 * value to differentiate between object types and you want to present 
 * the type of the object.
 * <p>
 * For example, suppose you have the objects <code>Graphic</code>, <code>Rectangle</code>, 
 * <code>Square</code>, and <code>Circle</code>.
 * Rectangle and Circle are subclasses of Graphic and Square is a subclass of Rectangle.
 * If you want to present the type of each object then you have to make a choice
 * based on each object's type (assuming they don't have a suitable method to return 
 * that text).
 * <p>
 * In this cases, this renderer can be used to present each object's class and create
 * a label from it. The default label format is <code>label.&lt;class name&gt;</code>
 * 
 * @author cfgi
 */
public class ClassAsLabelRenderer extends OutputRenderer {

    private String bundle;
    private String labelFormat;
    
    public String getBundle() {
        return this.bundle;
    }

    /**
     * The bundle were to look for the label.
     * 
     * @property
     */
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getLabelFormat() {
        return this.labelFormat;
    }

    /**
     * The format of the label. This property allows you to alter the 
     * default format of the label. You can use a format like the one supported
     * by {@link net.sourceforge.fenixedu.renderers.FormatRenderer} and it will be
     * applied to the class object.
     * <p>
     * For example a format of <code>"label.${superclass.simpleName}.${simpleName}"</code>
     * would produce the label <code>"label.Rectangle.Square"</code> when applied to the
     * class of a <code>Square</code> object.
     * 
     * @property 
     */
    public void setLabelFormat(String labelFormat) {
        this.labelFormat = labelFormat;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                Class targetClass = (Class) object;

                if (type == null) {
                    return new HtmlText();
                }
                
                String label = RenderUtils.getResourceString(getBundle(), getLabelName(targetClass));
                return new HtmlText(label);
            }

            protected String getLabelName(Class targetClass) {
                if (getLabelFormat() == null) {
                    return "label." + targetClass.getName();
                }
                else {
                    return RenderUtils.getFormattedProperties(getLabelFormat(), targetClass);
                }
            }
            
        };
    }

}
