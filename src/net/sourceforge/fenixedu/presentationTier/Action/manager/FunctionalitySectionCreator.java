package net.sourceforge.fenixedu.presentationTier.Action.manager;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.FunctionalitySection;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;

public class FunctionalitySectionCreator extends SectionCreator {

    private DomainReference<Functionality> functionality = new DomainReference<Functionality>(null);
    
    public FunctionalitySectionCreator(Section section) {
        super(section);
    }

    public FunctionalitySectionCreator(Site site) {
        super(site);
    }

    public Functionality getFunctionality() {
        return this.functionality.getObject();
    }

    public void setFunctionality(Functionality functionality) {
        this.functionality = new DomainReference<Functionality>(functionality);
    }

    @Override
    public void createSection() {
        Section section = new FunctionalitySection(getSite(), getFunctionality());
        
        section.setSuperiorSection(getSuperiorSection());
        section.setNextSection(getNextSection());
    }
}
