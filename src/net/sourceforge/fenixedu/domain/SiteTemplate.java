package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class SiteTemplate extends SiteTemplate_Base {
    
    protected SiteTemplate() {
        super();
    }
    
    public SiteTemplate(Class type) {
        this();
        
        setSiteType(type.getName());
    }
    
    public Class getType() {
        try {
            return Class.forName(getSiteType());
        } catch (ClassNotFoundException e) {
            throw new DomainException("site.type.notFound", e);
        }
    }

    /**
     * Searches for an instance associated with the given type.
     * 
     * @param type
     *            the type of the website
     * @return the instance associated to the given type or <code>null</code>
     */
    public static SiteTemplate getTemplateForType(Class type) {
        for (SiteTemplate template : readAllSiteTemplates()) {
            if (template.getType().equals(type)) {
                return template;
            }
        }
        
        return null;
    }

    private static Collection<SiteTemplate> readAllSiteTemplates() {
        List<SiteTemplate> sites = new ArrayList<SiteTemplate>();
        
        for (Site site : RootDomainObject.getInstance().getSites()) {
            if (site instanceof SiteTemplate) {
                sites.add((SiteTemplate) site);
            }
        }
        
        return sites;
    }

    @Override
    public IGroup getOwner() {
        return new RoleGroup(Role.getRoleByRoleType(RoleType.MANAGER));
    }
    
}
