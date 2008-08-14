package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class AddSectionToContainer extends Service {

    public void run(Container container, MultiLanguageString name, Boolean visible, Section nextSection) {
	Section section = (nextSection == null) ? new Section(container, name) : new Section(container, name, nextSection
		.getSectionOrder());
	section.setVisible(visible);
	((Content) section).getParentNode(container).setVisible(visible);
    }
}
