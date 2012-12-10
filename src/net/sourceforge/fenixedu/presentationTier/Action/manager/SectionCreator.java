package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class SectionCreator implements Serializable {

    private static final long serialVersionUID = 1L;

    private MultiLanguageString name;

    private boolean visible;

    private Section nextSection;

    private Site site;

    private Section superiorSection;

    private Group permittedGroup;

    public SectionCreator(Site site) {
	super();

	this.site = site;
	this.superiorSection = null;
	this.nextSection = null;
	this.visible = true;
	this.permittedGroup = new EveryoneGroup();
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

    public boolean getVisible() {
	return this.visible;
    }

    public void setVisible(boolean visible) {
	this.visible = visible;
    }

    public Section getNextSection() {
	return this.nextSection;
    }

    public void setNextSection(Section nextSection) {
	this.nextSection = nextSection;
    }

    public Site getSite() {
	return this.site;
    }

    public Section getSuperiorSection() {
	return this.superiorSection;
    }

    public void setSuperiorSection(Section superiorSection) {
	this.superiorSection = superiorSection;
    }

    public Group getPermittedGroup() {
	return this.permittedGroup;
    }

    public void setPermittedGroup(Group permittedGroup) {
	this.permittedGroup = permittedGroup;
    }

    public void createSection() {
	Section section = new Section((getSuperiorSection() == null) ? getSite() : getSuperiorSection(), getName());
	section.setNextSection(getNextSection());
	section.setPermittedGroup(getPermittedGroup());
	section.setVisible(getVisible());
	site.logCreateSection(section);
    }
}
