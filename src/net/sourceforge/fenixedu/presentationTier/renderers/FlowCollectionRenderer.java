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
 * This renderer allows you to present a collection of objects using
 * a flow layout, that is, every object is presented just after the previous
 * without any special interface organization.
 * 
 * <p>
 * Example:
 * <em>&lt;object A presentation&gt;</em> <em>&lt;object B presentation&gt;</em>
 * <em>&lt;object C presentation&gt;</em>
 * 
 * @author naat
 */
public class FlowCollectionRenderer extends OutputRenderer {

    private String eachLayout;

    private String eachSchema;

    private String eachClasses;
    
    private String eachStyle;
    
    private String htmlSeparator;

    private String emptyMessageKey;

    private String emptyMessageClasses;

    private String emptyMessageBundle;

    private String eachInline;
    
    private boolean indented = true;
    
    public boolean isIndented() {
        return this.indented;
    }

    /**
     * Allows to prevent identation of the generated markup. This is specially
     * usefull when you need to prevent extra spaces between elements and the
     * separator.
     * 
     * @property
     */
    public void setIndented(boolean indented) {
        this.indented = indented;
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

    public String getEachClasses() {
        return this.eachClasses;
    }

    /**
     * The css classes to be set in each sub object's presentation.
     * 
     * @property
     */
    public void setEachClasses(String eachClasses) {
        this.eachClasses = eachClasses;
    }

    public String getEachStyle() {
        return this.eachStyle;
    }

    /**
     *  The css style to be used for each sub object's presentation.
     * 
     * @property
     */
    public void setEachStyle(String eachStyle) {
        this.eachStyle = eachStyle;
    }

    /**
     * The schema to be used when presenting each sub object.
     * 
     * @property
     */
    public void setEachSchema(String eachSchema) {
        this.eachSchema = eachSchema;
    }

    public String getHtmlSeparator() {
        return htmlSeparator;
    }

    /**
     * If you specify this property then value given will be inserted between 
     * any two object's presentations. This, depending on the individual 
     * presentation of each object, can be used to force line breaks or just
     * provide a visual separator.
     * 
     * @property
     */
    public void setHtmlSeparator(String htmlSeparator) {
        this.htmlSeparator = htmlSeparator;
    }

    public String getEmptyMessageKey() {
        return emptyMessageKey;
    }

    /**
     * When the given collection is empty this key will be used
     * to fecth an according message for the situation. The bundle
     * used is the module's default bundle. 
     * 
     * @property
     */
    public void setEmptyMessageKey(String emptyMessageKey) {
        this.emptyMessageKey = emptyMessageKey;
    }

    public String getEmptyMessageClasses() {
        return emptyMessageClasses;
    }

    /**
     * This property can be used to costumize the css classes of the 
     * message shown when the given collection is empty.
     * 
     * @property
     */
    public void setEmptyMessageClasses(String emptyMessageClasses) {
        this.emptyMessageClasses = emptyMessageClasses;
    }

    public String getEmptyMessageBundle() {
		return emptyMessageBundle;
	}

    /**
     * When <code>emptyMessageKey</code> is used to specify wich resource key will be used
     * when the collection is empty, this property allows to specify wich bundle
     * will be used to fetch the key.
     * 
     * @property
     */
    public void setEmptyMessageBundle(String emptyMessageBundle) {
		this.emptyMessageBundle = emptyMessageBundle;
	}

	public String getEachInline() {
        return this.eachInline;
    }

    /**
     * Indicates if the each object's presentation should be placed 
     * inside a <code>&lt;div&gt;</code> or a <code>&lt;span&gt;</code>.
     * 
     * @property
     */
    public void setEachInline(String eachInline) {
        this.eachInline = eachInline;
    }

    @Override
    protected Layout getLayout(final Object object, Class type) {
        return new FlowCollectionLayout(object,type);
    }


    public class FlowCollectionLayout extends FlowLayout {

        protected boolean insertSeparator;
        
        protected Iterator iterator;
        
        private boolean empty;
        
        public FlowCollectionLayout(final Object object, Class type) {
        	iterator = ((Collection) object).iterator();   	
        }
        
        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            HtmlComponent component = null;

            if ((getEmptyMessageKey() != null) && (hasMoreComponents() == false)) {
                component = new HtmlText(RenderUtils.getResourceString(getEmptyMessageBundle(), getEmptyMessageKey()));
                this.empty = true;
            } else {
                component = super.createComponent(object, type);
                component.setIndented(isIndented());
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
                return new HtmlText(getHtmlSeparator(),false);
            } else if (hasMoreComponents() && getHtmlSeparator() != null) {
                this.insertSeparator = true;
            }

            return renderValue(iterator.next(), RenderKit.getInstance().findSchema(getEachSchema()),
                    getEachLayout());
        }
    };


}
