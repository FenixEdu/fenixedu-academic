package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.FunctionalitySection;
import net.sourceforge.fenixedu.domain.SiteTemplate;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FunctionalitySectionCreator;

public class UnitSiteFunctionalityInputTreeRenderer extends FunctionalityInputTreeRenderer {

	@Override
	protected List<Functionality> getFunctionalities(Object object) {
		Object parentObject = getInputContext().getParentContext().getMetaObject().getObject();
		
		UnitSite unitSite = null;
		if (parentObject instanceof FunctionalitySection) {
			unitSite = (UnitSite) ((FunctionalitySection) parentObject).getSite();
		}
		else if (parentObject instanceof FunctionalitySectionCreator) {
			unitSite = (UnitSite) ((FunctionalitySectionCreator) parentObject).getSite();
		}
		
		SiteTemplate template = (SiteTemplate) unitSite.getTemplate();

		if (template == null) {
			return Collections.emptyList();
		}
		
		Module module = template.getModule();
		if (module == null) {
			return Collections.emptyList();
		}
		
		return module.getOrderedFunctionalities();
	}
	
}
