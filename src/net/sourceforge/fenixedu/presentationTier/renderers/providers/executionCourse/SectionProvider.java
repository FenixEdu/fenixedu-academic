package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionCourse;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SectionCreator;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public abstract class SectionProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        Section self;
        Section superiorSection;
        Site site;
        
        if (source instanceof Section) {
            self = (Section) source;
            
            superiorSection = self.getSuperiorSection();
            site = self.getSite();
        }
        else if (source instanceof SectionCreator) {
            SectionCreator creator = (SectionCreator) source;
            
            self = null;
            
            superiorSection = creator.getSuperiorSection();
            site = creator.getSite();
        }
        else {
            throw new RuntimeException("type not supported");
        }
        
        return provideForContext(site, superiorSection, self);
    }

    public abstract Object provideForContext(Site site, Section superiorSection, Section self);

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
