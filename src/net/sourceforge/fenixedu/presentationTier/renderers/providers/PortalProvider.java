package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PortalProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final Set<Content> portalSet = new TreeSet<Content>(new BeanComparator("name"));

	for (Section portal : RootDomainObject.getInstance().getRootPortal().getChildren(Section.class)) {
	    portalSet.add(portal);
	}
	return portalSet;
    }

    public Converter getConverter() {

	return new DomainObjectKeyConverter();
    }

}
