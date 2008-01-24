package net.sourceforge.fenixedu.presentationTier.renderers.providers.contents;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.domain.contents.Node;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import net.sourceforge.fenixedu.presentationTier.Action.person.SectionBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class SectionProviderForContents implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	if (source instanceof SectionBean) {
	    SectionBean bean = (SectionBean) source;
	    return bean.getContainer().getOrderedChildren();
	} else {
	    Section section = (Section) source;
	    Container container = getParent(section);
	    return container == null ? Collections.EMPTY_LIST : container.getOrderedChildren(new Class[] {Section.class, MetaDomainObjectPortal.class});
	}
    }

    private Container getParent(Section section) {
	for(Node node : section.getParents()) {
	    Container container = node.getParent();
	    if(!(container instanceof Module)) {
		return container;
	    }
	}
	return null;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }
}
