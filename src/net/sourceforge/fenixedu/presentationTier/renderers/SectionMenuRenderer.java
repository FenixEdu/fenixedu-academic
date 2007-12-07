package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.contents.MenuEntry;

public class SectionMenuRenderer extends SiteMenuRenderer {

    private Section getSection(Object object) {
	return (Section) object;
    }

    @Override
    protected Site getSite(Object object) {
	return getSection(object).getSite();
    }

    @Override
    protected Collection<MenuEntry> getEntries(Object object) {
	return getSection(object).getMenu();
    }

    @Override
    protected boolean accessTemplate() {
	return false;
    }
}
