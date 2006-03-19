package net.sourceforge.fenixedu.domain.cms.website;

import java.util.Date;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class WebsiteType extends WebsiteType_Base {
    
    public WebsiteType() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setCreationDate(new Date());
        setCreator(AccessControl.getUserView().getPerson());
    }

    public void delete() {
        if (getWebsitesCount() > 0) {
            throw new DomainException("websiteType.error.hasWebsites");
        }
        
        removeCreator();
        getMandatoryContents().clear();
        
        deleteDomainObject();
    }
    
}
