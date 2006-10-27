package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionCourse;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;

public class SectionOrderProvider extends SectionProvider {

    @Override
    public Object provideForContext(Site site, Section superiorSection, Section self) {
        Collection<Section> siblings;
        
        if (superiorSection != null) {
            siblings = superiorSection.getOrderedSubSections();
        }
        else {
            siblings = site.getOrderedTopLevelSections();
        }
        
        siblings = new ArrayList<Section>(siblings);
        siblings.remove(self);

        return siblings;
    }

}
