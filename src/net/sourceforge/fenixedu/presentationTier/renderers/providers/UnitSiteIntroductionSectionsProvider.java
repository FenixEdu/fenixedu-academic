package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

/**
 * Provides as possibilities all sections contained in the Side section if
 * available.
 * 
 * @author cfgi
 */
public class UnitSiteIntroductionSectionsProvider implements DataProvider {

	public Object provide(Object source, Object currentValue) {
		UnitSite unitSite = (UnitSite) source;
		
		for (Section section : unitSite.getAssociatedSections()) {
			if (isSideSection(section)) {
				return section.getOrderedSubSections();
			}
		}
		
		return new ArrayList<Section>();
	}

	private boolean isSideSection(Section section) {
		String pt = section.getName().getContent(Language.pt);
		String en = section.getName().getContent(Language.en);
		
		return (pt != null && pt.equalsIgnoreCase("lateral")) || (en != null && en.equalsIgnoreCase("side"));
	}

	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}
	
}
