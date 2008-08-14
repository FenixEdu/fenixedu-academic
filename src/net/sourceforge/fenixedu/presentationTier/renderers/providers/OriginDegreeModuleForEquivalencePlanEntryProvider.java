package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.EquivalencePlanEntry.EquivalencePlanEntryCreator;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class OriginDegreeModuleForEquivalencePlanEntryProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final EquivalencePlanEntryCreator equivalencePlanEntryCreator = (EquivalencePlanEntryCreator) source;
	final DegreeCurricularPlanEquivalencePlan equivalencePlan = (DegreeCurricularPlanEquivalencePlan) equivalencePlanEntryCreator
		.getEquivalencePlan();
	final DegreeCurricularPlan degreeCurricularPlan = equivalencePlan.getSourceDegreeCurricularPlan();
	final Set<DegreeModule> degreeModules = degreeCurricularPlan.getAllDegreeModules();
	return degreeModules;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
