package net.sourceforge.fenixedu.presentationTier.renderers.providers.contents;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.presentationTier.Action.person.SectionBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class SectionProviderForContents implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	if (source instanceof SectionBean) {
	    SectionBean bean = (SectionBean) source;
	    return bean.getContainer().getOrderedChildren(Section.class);
	} else {
	    Section section = (Section) source;
	    return section.getOrderedChildren(Section.class);
	}
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }
}
