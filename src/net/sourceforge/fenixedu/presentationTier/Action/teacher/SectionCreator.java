package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class SectionCreator implements Serializable {

    private static final long serialVersionUID = 1L;

    private MultiLanguageString name;
    private DomainReference<Section> nextSection;

    private DomainReference<Site> site;
    private DomainReference<Section> superiorSection;

    private Group permittedGroup;
    
    public SectionCreator(Site site) {
        super();

        this.site = new DomainReference<Site>(site);
        this.superiorSection = new DomainReference<Section>(null);
        this.nextSection = new DomainReference<Section>(null);
    }

    public SectionCreator(Section section) {
        this(section.getSite());
        
        setSuperiorSection(section);
    }

    public MultiLanguageString getName() {
        return this.name;
    }

    public void setName(MultiLanguageString name) {
        this.name = name;
    }

    public Section getNextSection() {
        return this.nextSection.getObject();
    }

    public void setNextSection(Section nextSection) {
        this.nextSection = new DomainReference<Section>(nextSection);
    }

    public Site getSite() {
        return this.site.getObject();
    }

    public Section getSuperiorSection() {
        return this.superiorSection.getObject();
    }

    public void setSuperiorSection(Section superiorSection) {
        this.superiorSection = new DomainReference<Section>(superiorSection);
    }

    public Group getPermittedGroup() {
        return this.permittedGroup;
    }

    public void setPermittedGroup(Group permittedGroup) {
        this.permittedGroup = permittedGroup;
    }

    public void createSection() {
        Section section = new Section(getSite(), getName());
        
        section.setSuperiorSection(getSuperiorSection());
        section.setNextSection(getNextSection());
        section.setPermittedGroup(getPermittedGroup());
    }
}
