package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.cms.Content;
import net.sourceforge.fenixedu.domain.cms.FunctionalityLink;
import net.sourceforge.fenixedu.domain.cms.website.WebsiteType;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class FunctionalLinksForWebsiteType implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        WebsiteType type = (WebsiteType) source;
        List<FunctionalityLink> links = new ArrayList<FunctionalityLink>();

        for (final Content content : RootDomainObject.getInstance().getContentsSet()) {
            if (content instanceof Content) {
                FunctionalityLink link = (FunctionalityLink) content;
                if (link.getTypes().contains(type)) {
                    links.add(link);
                }
            }
        }

        return links;
    }

    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

}
