package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.contents.Content;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PortalProvider implements DataProvider {

    private static final String PUBLIC_CONTENT = "Conteudos Publicos";

    //it only gives the private portals and only one public portal
    @Override
    public Object provide(Object source, Object currentValue) {
        final Set<Content> portalSet = new TreeSet<Content>(new BeanComparator("name"));

        for (Section portal : Bennu.getInstance().getRootPortal().getChildren(Section.class)) {
            if (portal.hasAvailabilityPolicy()) {
                if (portal.getAvailabilityPolicy().getTargetGroup()
                        .isMember(Person.readPersonByUsername(Authenticate.getUser().getUsername()))) {
                    portalSet.add(portal);
                }
            } else {
                if (portal.getName().toString().equals(PUBLIC_CONTENT)) {
                    portalSet.add(portal);
                }
            }
        }
        return portalSet;
    }

    @Override
    public Converter getConverter() {

        return new DomainObjectKeyConverter();
    }

}
