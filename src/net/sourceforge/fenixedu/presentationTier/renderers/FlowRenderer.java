package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.FlowLayout;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * 
 * @author naat
 */
public class FlowRenderer extends OutputRenderer {

    private String eachLayout;

    private String eachSchema;

    private String htmlSeparator;

    private String emptyMessageKey;

    private String emptyMessageClasses;

    public FlowRenderer() {
        super();
    }

    public String getEachLayout() {
        return eachLayout;
    }

    /**
     * @property
     */
    public void setEachLayout(String eachLayout) {
        this.eachLayout = eachLayout;
    }

    public String getEachSchema() {
        return eachSchema;
    }

    /**
     * @property
     */
    public void setEachSchema(String eachSchema) {
        this.eachSchema = eachSchema;
    }

    public String getHtmlSeparator() {
        return htmlSeparator;
    }

    /**
     * @property
     */
    public void setHtmlSeparator(String htmlSeparator) {
        this.htmlSeparator = htmlSeparator;
    }

    public String getEmptyMessageKey() {
        return emptyMessageKey;
    }

    /**
     * @property
     */
    public void setEmptyMessageKey(String emptyMessageKey) {
        this.emptyMessageKey = emptyMessageKey;
    }

    public String getEmptyMessageClasses() {
        return emptyMessageClasses;
    }

    /**
     * @property
     */
    public void setEmptyMessageClasses(String emptyMessageClasses) {
        this.emptyMessageClasses = emptyMessageClasses;
    }

    @Override
    protected Layout getLayout(final Object object, Class type) {
        final Iterator iterator = ((Collection) object).iterator();

        return new FlowLayout() {

            private boolean insertSeparator;

            private boolean empty;

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                HtmlComponent component = null;

                if ((getEmptyMessageKey() != null) && (hasMoreComponents() == false)) {
                    component = new HtmlText(RenderUtils.getResourceString(getEmptyMessageKey()));
                    this.empty = true;
                } else {
                    component = super.createComponent(object, type);
                    this.empty = false;
                }

                return component;

            }

            @Override
            public void applyStyle(HtmlComponent component) {
                if (this.empty) {
                    component.setClasses(getEmptyMessageClasses());
                } else {
                    super.applyStyle(component);
                }
            }

            @Override
            protected boolean hasMoreComponents() {
                return iterator.hasNext();
            }

            @Override
            protected HtmlComponent getNextComponent() {
                if (this.insertSeparator) {
                    this.insertSeparator = false;
                    return new HtmlText(getHtmlSeparator());
                } else if (hasMoreComponents() && getHtmlSeparator() != null) {
                    this.insertSeparator = true;
                }

                return renderValue(iterator.next(), RenderKit.getInstance().findSchema(getEachSchema()),
                        getEachLayout());
            }
        };
    }

}
