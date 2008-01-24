package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan.StudentEquivalencyPlanEntryCreator;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class OriginDegreeModuleForStudentEquivalencyPlanEntryCreatorProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final StudentEquivalencyPlanEntryCreator studentEquivalencyPlanEntryCreator = (StudentEquivalencyPlanEntryCreator) source;
	final StudentCurricularPlanEquivalencePlan studentCurricularPlanEquivalencePlan = studentEquivalencyPlanEntryCreator.getStudentCurricularPlanEquivalencePlan();
	final StudentCurricularPlan studentCurricularPlan = studentCurricularPlanEquivalencePlan.getOldStudentCurricularPlan();
	final Set<DegreeModule> degreeModules = studentCurricularPlan.getAllDegreeModules();
	return degreeModules;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
