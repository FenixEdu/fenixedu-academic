package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.contents.Container;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class SectionBean implements Serializable {

    private DomainReference<Container> container;

    private MultiLanguageString name;

    private boolean visible;

    private DomainReference<Section> nextSection;

    public SectionBean(Container container) {
	setContainer(container);
	setNextSection(null);
    }
    
    public void setContainer(Container container) {
	this.container = new DomainReference<Container>(container);
    }
    
    public Container getContainer() {
	return this.container.getObject();
    }

    public MultiLanguageString getName() {
        return name;
    }

    public void setName(MultiLanguageString name) {
        this.name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setNextSection(Section section) {
	this.nextSection = new DomainReference<Section>(section);
    }
    
    public Section getNextSection() {
	return this.nextSection.getObject();
    }
    
    
}
