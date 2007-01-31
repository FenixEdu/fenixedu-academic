package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * The default output renderer for a boolean value. The value is used to search
 * for the corresponding message in the resources. The key <tt>TRUE</tt> and
 * <tt>FALSE</tt> are used to retrieve the messages for the <tt>true</tt>
 * and <tt>false</tt> values.
 * 
 * @author cfgi
 */
public class BooleanRenderer extends OutputRenderer {

    private String trueLabel;
    private String falseLabel;
    private String bundle;

    public String getBundle() {
        return this.bundle;
    }

    /**
     * Chooses the bundle in wich the labels will be searched.
     * 
     * @property
     */
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getFalseLabel() {
        return this.falseLabel;
    }

    /**
     * The label to be used when presenting a <code>false</code> value.
     * 
     * @property
     */
    public void setFalseLabel(String falseLabel) {
        this.falseLabel = falseLabel;
    }

    public String getTrueLabel() {
        return this.trueLabel;
    }

    /**
     * The label to be used when presenting the <code>true</code> value.
     * 
     * @property
     */
    public void setTrueLabel(String trueLabel) {
        this.trueLabel = trueLabel;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                Boolean booleanValue = (Boolean) object;

                if (booleanValue == null) {
                    return new HtmlText();
                }

                String booleanResourceKey = getBooleanLabel(booleanValue);
                return new HtmlText(RenderUtils.getResourceString(getBundle(), booleanResourceKey));
            }

            private String getBooleanLabel(Boolean booleanValue) {
                String label = booleanValue ? getTrueLabel() : getFalseLabel();

                if (label != null) {
                    return label;
                } else {
                    return booleanValue.toString().toUpperCase();
                }
            }

        };
    }

}
