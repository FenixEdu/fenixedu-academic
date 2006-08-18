package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.state.ViewDestination;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * This render is used to create a link out of an object. You choose the link format and
 * some properties can be used to configure the link. You can also specify the link indirectly
 * by specifing a destination and then defining a destination with that name in the place
 * were ou use this renderer.
 * 
 * <p>
 * The link body is configured through a sub rendering of the object with the specified
 * layout and schema.
 * 
 * <p>
 * Example: <a href="#">Jane Doe</a>
 * 
 * @author cfgi
 */
public class ObjectLinkRenderer extends OutputRenderer {

    private boolean useParent;
    
    private String linkFormat;

    private boolean contextRelative;
    
    private String destination;
    
    private String subSchema;

    private String subLayout;

    private String key;
    
    private String bundle;
    
    private String text;

    public String getLinkFormat() {
        return this.linkFormat;
    }


    /**
     * This property allows you to specify the format of the final link.
     * In this format you can use properties of the object being presented.
     * For example:
     * 
     * <code>
     *  format="/some/action.do?oid=${id}"
     * </code>
     * 
     * @see RenderUtils#getFormattedProperties(String, Object)
     * @property
     */
    public void setLinkFormat(String linkFormat) {
        this.linkFormat = linkFormat;
    }

    public boolean isContextRelative() {
        return this.contextRelative;
    }

    /**
     * Indicates that th link specified should be relative to the context of the
     * application and not to the current module. This also overrides the module
     * if a destination is specified.
     * 
     * @property
     */
    public void setContextRelative(boolean contextRelative) {
        this.contextRelative = contextRelative;
    }


    public boolean isUseParent() {
        return this.useParent;
    }

    /**
     * This property can be used when presenting an object's slot.
     * If this property is true the object that will be considered
     * when replacing the properties in the link will be the parent object,
     * that is, the object that contains the slot being presented.
     * 
     * <p>
     * Off course, if this property is false (the default) the object
     * that will be considered is the object initialy being presented.
     * 
     * @property
     */
    public void setUseParent(boolean useParent) {
        this.useParent = useParent;
    }

    public String getDestination() {
        return this.destination;
    }

    /**
     * This property is an alternative to the use of the {@link #setLinkFormat(String) linkFormat}.
     * With this property you can specify the name of the view state destination that will be used.
     * This property allows you to select the concrete destination in each context were this
     * configuration is used.
     * 
     * @property
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getText() {
        return this.text;
    }

    /**
     * The text to appear as the link text. This is a simple alternative to the full presentation
     * of the object.
     * 
     * @property
     */
    public void setText(String text) {
        this.text = text;
    }

    public String getKey() {
        return this.key;
    }

    /**
     * Instead of specifying thr {@link #setText(String) text} property you can specify a key,
     * with this property, and a bundle with the {@link #setBundle(String) bundle}. 
     * 
     * @property
     */
    public void setKey(String key) {
        this.key = key;
    }

    public String getBundle() {
        return this.bundle;
    }

    /**
     * The bundle were the {@link #setKey(String) key} will be fetched.
     * 
     * @property
     */
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getSubLayout() {
        return this.subLayout;
    }

    /**
     * Specifies the sub layout that will be used for the body of the link, that is,
     * the object will be presented using the layout specified and the result of that
     * presentation will be the body of the link.
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
     * The name of the schema to use in the presentation of the object for the body
     * of the link.
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
                Object usedObject = getTargetObject(object);
                
                if (usedObject == null) {
                    return new HtmlText();
                }
                
                HtmlLink link = getLink(usedObject);
                
                String text = getLinkText();
                if (text != null) {
                    link.setText(text);
                } else {
                    link.setBody(getLinkBody(object));
                }

                return link;
            }

            private HtmlComponent getLinkBody(Object object) {
                Schema findSchema = RenderKit.getInstance().findSchema(getSubSchema());
                return renderValue(object, findSchema, getSubLayout());
            }

            private String getLinkText() {
                if (getText() != null) {
                    return getText();
                }
                
                if (getKey() == null) {
                    return null;
                }
                
                return RenderUtils.getResourceString(getBundle(), getKey());
            }

            private HtmlLink getLink(Object usedObject) {
                HtmlLink link = new HtmlLink();
                
                String url;
                
                if (getDestination() != null) {
                    ViewDestination destination = getContext().getViewState().getDestination(getDestination());
                    
                    if (destination != null) {
                        link.setModule(destination.getModule());
                        url = destination.getPath();
                    }
                    else {
                        url = "#";
                    }
                }
                else {
                    if (getLinkFormat() != null) {
                        url = getLinkFormat();    
                    }
                    else {
                        url = "#";
                    }
                }
                
                link.setUrl(RenderUtils.getFormattedProperties(url, usedObject));
                
                if (isContextRelative()) {
                    link.setModule(null);
                    link.setModuleRelative(false);
                }
                
                return link;
            }

            private Object getTargetObject(Object object) {
                if (isUseParent()) {
                    if (getContext().getParentContext() != null) {
                        return getContext().getParentContext().getMetaObject().getObject();
                    }
                    else {
                        return null;
                    }
                }
                else {
                    return object;
                }
            }

        };
    }
}
