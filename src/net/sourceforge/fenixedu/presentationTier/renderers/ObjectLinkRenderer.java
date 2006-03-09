package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.actions.NavigationAction;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * This renderer can only be used to present a domain object's slot. It will generate a link to the
 * object's details. In the following example we are imitating the presentation of a person where the
 * slot <code>name</code> is presented by this renderer.
 * 
 * <p>
 * Example: <a href="#">Jane Doe</a>
 * 
 * @author cfgi
 */
public class ObjectLinkRenderer extends OutputRenderer {

    // TODO: remove constant action path
    private static final String DEFAULT_PAGE = "/domain/view.do";

    private String viewPage;

    private String subSchema;

    private String subLayout;

    private String destinySchema;

    private String destinyLayout;

    private String text;

    private boolean isKey;

    public ObjectLinkRenderer() {
        super();

        this.isKey = false;
    }

    public String getViewPage() {
        return this.viewPage;
    }

    /**
     * This property allows you to override the page or action that is used to show the object's details.
     * The default page is {@value #DEFAULT_PAGE}. This renderer will generate a link for the specified
     * page passing as arguments:
     * <dl>
     *   <dt>type</dt>
     *   <dd>the the object type (as given by {@link NavigationAction#getTypeName(Class)})</dd>
     * 
     *   <dt>oid</dt>
     *   <dd>the object id</dd>
     * 
     *   <dt>layout</dt>
     *   <dd>the layout to be used in the presentation and specified by
     *   {@link #setDestinyLayout(String) destinyLayout}. It may not be present.</dd>
     * 
     *   <dt>schema</dt>
     *   <dd>the schema to be used in the presentation and specified by
     *   {@link #setDestinySchema(String) destinySchema}. It may not be present.</dd>
     * </dl>
     * 
     * @property
     */
    public void setViewPage(String viewPage) {
        this.viewPage = viewPage;
    }

    public String getDestinyLayout() {
        return destinyLayout;
    }

    /**
     * The layout to be used in the detailed presentation of the object.
     * 
     * @property
     */
    public void setDestinyLayout(String destinyLayout) {
        this.destinyLayout = destinyLayout;
    }

    public String getDestinySchema() {
        return destinySchema;
    }

    /**
     * The schema to be used in the detailed presentation of the object.
     * 
     * @property
     */
    public void setDestinySchema(String destinySchema) {
        this.destinySchema = destinySchema;
    }

    public String getSubLayout() {
        return subLayout;
    }

    /**
     * The layout in which the slot's value should be presented.
     * 
     * @property
     */
    public void setSubLayout(String subLayout) {
        this.subLayout = subLayout;
    }

    public String getSubSchema() {
        return subSchema;
    }

    /**
     * If the slot's value is a complex value, that is, it's an object with slots, then this property can
     * be used to indicate the layout in which it should be presented.
     * 
     * @property
     */
    public void setSubSchema(String subSchema) {
        this.subSchema = subSchema;
    }

    public String getText() {
        return text;
    }

    /**
     * If you specify this slot then the sub object's presentation is ignored and this text is shown
     * instead.
     * 
     * @property
     */
    public void setText(String text) {
        this.text = text;
    }

    public boolean isKey() {
        return isKey;
    }

    /**
     * This property indicates that the value given in the {@link #setText(String) text} property is to
     * be treated as a resource key.
     * 
     * @property
     */
    public void setKey(boolean isKey) {
        this.isKey = isKey;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                HtmlLink link = new HtmlLink();

                if (getText() != null) {
                    link.setText(getLinkText());
                } else {
                    if (object == null) {
                        return link;
                    } else {
                        link.setBody(renderLinkBody(object));
                    }
                }

                DomainObject domainObject = getDomainObject(object);
                setupLink(link, domainObject);

                return link;
            }

        };
    }

    protected DomainObject getDomainObject(Object object) {
        return (DomainObject) getContext().getParentContext().getMetaObject().getObject();
    }

    private void setupLink(HtmlLink link, DomainObject domainObject) {
        link.setUrl(getViewUrl());

        link.setParameter("oid", domainObject.getIdInternal().toString());
        link.setParameter("type", NavigationAction.getTypeName(domainObject.getClass()));

        if (getDestinyLayout() != null) {
            link.setParameter("layout", getDestinyLayout());
        }

        if (getDestinySchema() != null) {
            link.setParameter("schema", getDestinySchema());
        }
    }

    private String getViewUrl() {
        if (getViewPage() != null) {
            return getViewPage();
        } else {
            return DEFAULT_PAGE;
        }
    }

    private String getLinkText() {
        if (isKey()) {
            return RenderUtils.getResourceString(getText());
        }

        return getText();
    }

    private HtmlComponent renderLinkBody(Object object) {
        Schema schema = RenderKit.getInstance().findSchema(getSubSchema());
        return renderValue(object, schema, getSubLayout());
    }
}
