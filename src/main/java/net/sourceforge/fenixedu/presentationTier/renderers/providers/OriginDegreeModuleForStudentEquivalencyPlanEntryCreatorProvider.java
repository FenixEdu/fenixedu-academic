package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan.StudentEquivalencyPlanEntryCreator;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class OriginDegreeModuleForStudentEquivalencyPlanEntryCreatorProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final StudentEquivalencyPlanEntryCreator studentEquivalencyPlanEntryCreator = (StudentEquivalencyPlanEntryCreator) source;
        final StudentCurricularPlanEquivalencePlan studentCurricularPlanEquivalencePlan =
                studentEquivalencyPlanEntryCreator.getStudentCurricularPlanEquivalencePlan();
        final StudentCurricularPlan studentCurricularPlan = studentCurricularPlanEquivalencePlan.getOldStudentCurricularPlan();
        final Set<DegreeModule> degreeModules = studentCurricularPlan.getAllDegreeModules();
        return degreeModules;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
