package net.sourceforge.fenixedu.presentationTier.renderers.converters;

import net.sourceforge.fenixedu.domain.Section;

public class SectionOrderConverter extends DomainObjectKeyConverter {

    @Override
    public Object convert(Class type, Object value) {
	final Section section = (Section) super.convert(type, value);
	return section.getSectionOrder();
    }  

}
