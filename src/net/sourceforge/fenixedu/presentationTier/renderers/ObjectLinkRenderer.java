package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.actions.ViewObjectAction;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class ObjectLinkRenderer extends OutputRenderer {

    private String text;
    private boolean isKey;

    private String subSchema;
    private String subLayout;
    
    private String destinySchema;
    private String destinyLayout;
    
    public ObjectLinkRenderer() {
        super();
        
        this.isKey = false;
    }
    
    public boolean isKey() {
        return isKey;
    }

    public void setKey(boolean isKey) {
        this.isKey = isKey;
    }

    public String getDestinyLayout() {
        return destinyLayout;
    }

    public void setDestinyLayout(String destinyLayout) {
        this.destinyLayout = destinyLayout;
    }

    public String getDestinySchema() {
        return destinySchema;
    }

    public void setDestinySchema(String destinySchema) {
        this.destinySchema = destinySchema;
    }
    
    public String getSubLayout() {
        return subLayout;
    }

    public void setSubLayout(String subLayout) {
        this.subLayout = subLayout;
    }

    public String getSubSchema() {
        return subSchema;
    }

    public void setSubSchema(String subSchema) {
        this.subSchema = subSchema;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                HtmlLink link = new HtmlLink();

                if (getText() != null) {
                    link.setText(getLinkText());
                }
                else {
                    if (object == null) {
                        return link;
                    }
                    else {            
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
        link.setParameter("type", ViewObjectAction.getTypeName(domainObject.getClass()));
        
        if (getDestinyLayout() != null) {
            link.setParameter("layout", getDestinyLayout());
        }

        if (getDestinySchema() != null) {
            link.setParameter("schema", getDestinySchema());
        }
    }

    private String getViewUrl() {
        // TODO: remove constant action path
        return RenderUtils.getModuleRelativePath(getOutputContext().getViewState().getRequest(), "/domain/view.do");
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
