package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Portal;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PortalBean implements Serializable {

    private MetaDomainObject metaDomainObject;

    private Container container;

    private Portal portal;

    private MultiLanguageString name;

    private String prefix;

    private String contentId;

    public PortalBean(Portal portal) {
        setPortal(portal);
        setName(portal.getName());
        setPrefix(portal.getPrefix());
        setContentId(portal.getContentId());
        setMetaDomainObject(null);
        setContainer(null);
    }

    public PortalBean(Container container) {
        setMetaDomainObject(null);
        setPortal(null);
        setContainer(container);
    }

    public PortalBean(MetaDomainObject metaDomainObject) {
        setPortal(null);
        setMetaDomainObject(metaDomainObject);
        setContainer(null);
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public MetaDomainObject getMetaDomainObject() {
        return metaDomainObject;
    }

    public void setMetaDomainObject(MetaDomainObject metaDomainObject) {
        this.metaDomainObject = metaDomainObject;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public MultiLanguageString getName() {
        return name;
    }

    public void setName(MultiLanguageString name) {
        this.name = name;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public void setPortal(Portal portal) {
        this.portal = portal;
    }

    public Portal getPortal() {
        return this.portal;
    }
}
