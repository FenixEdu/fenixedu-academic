package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;

public class SectionMenuRenderer extends SiteMenuRenderer {

	private Section getSection(Object object) {
		return (Section) object;
	}

	@Override
	protected Site getSite(Object object) {
		return getSection(object).getSite();
	}

	@Override
	protected List<Section> getSections(Object object) {
		return new ArrayList<Section>(getSection(object).getOrderedSubSections());
	}
	
	@Override
	protected List<Section> getSubSections(Object object, Section section) {
		return new ArrayList<Section>();
	}
	
}
