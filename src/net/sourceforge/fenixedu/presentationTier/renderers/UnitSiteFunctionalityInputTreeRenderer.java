package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;

public class UnitSiteFunctionalityInputTreeRenderer extends FunctionalityInputTreeRenderer {

	@Override
	protected List<Content> getFunctionalities(Object object) {
		Object parentObject = getInputContext().getParentContext().getMetaObject().getObject();
		
		UnitSite unitSite = null;
//		if (parentObject instanceof FunctionalitySection) {
//			unitSite = (UnitSite) ((FunctionalitySection) parentObject).getSite();
//		}
//		else if (parentObject instanceof FunctionalitySectionCreator) {
//			unitSite = (UnitSite) ((FunctionalitySectionCreator) parentObject).getSite();
//		}
		
		MetaDomainObjectPortal template = (MetaDomainObjectPortal) unitSite.getTemplate();

		if (template == null) {
			return Collections.emptyList();
		}
		
		Container container = template;
		if (container == null) {
			return Collections.emptyList();
		}
		
		return new ArrayList<Content>(container.getOrderedChildren(Functionality.class));
	}
	
}
