package net.sourceforge.fenixedu.presentationTier.renderers.providers.contents;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.contents.Element;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class InitialContentForSection implements DataProvider {


    public Object provide(Object source, Object currentValue) {
	Section section = (Section) source;
	return section.getChildren(Element.class);
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }
}
