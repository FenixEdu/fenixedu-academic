package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionCourse;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Section.SectionFactoryCreator;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.SectionOrderConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class SectionOrderProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final Site site;
	final Section superiorSection;
	if (source instanceof SectionFactoryCreator) { 
	    final SectionFactoryCreator sectionFactoryCreator = (SectionFactoryCreator) source;
	    site = sectionFactoryCreator.getSite();
	    superiorSection = sectionFactoryCreator.getSuperiorSection();
	} else if (source instanceof Section) {
	    final Section section = (Section) source;
	    site = section.getSite();
	    superiorSection = section.getSuperiorSection();
	} else {
	    throw new Error("Unknown section type: " + source);
	}

	return superiorSection == null ? site.getOrderedTopLevelSections() : superiorSection.getOrderedSubSections();
    }

    public Converter getConverter() {
        return new SectionOrderConverter();
    }

}
