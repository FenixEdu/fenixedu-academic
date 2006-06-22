package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.cms.FunctionalityLink;
import net.sourceforge.fenixedu.domain.cms.website.WebsiteType;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class FunctionalLinksForWebsiteType implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        WebsiteType type = (WebsiteType) source;
        List<FunctionalityLink> links = new ArrayList<FunctionalityLink>();
        
        try {
            IUserView userView = AccessControl.getUserView();
            Collection<FunctionalityLink> allLinks = (Collection<FunctionalityLink>) ServiceUtils.executeService(userView, "ReadAllDomainObjects", new Object[] { FunctionalityLink.class });
            
            for (FunctionalityLink link : allLinks) {
                if (link.getTypes().contains(type)) {
                    links.add(link);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("could not retrieve all functionality links", e);
        }
        
        return links;
    }

    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

}
