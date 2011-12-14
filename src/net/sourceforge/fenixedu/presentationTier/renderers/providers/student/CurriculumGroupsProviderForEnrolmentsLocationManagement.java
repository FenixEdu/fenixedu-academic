package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.student.OptionalCurricularCoursesLocationBean.EnrolmentLocationBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurriculumGroupsProviderForEnrolmentsLocationManagement implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	final EnrolmentLocationBean bean = (EnrolmentLocationBean) source;
	final Set<DegreeModule> result = new TreeSet<DegreeModule>(DegreeModule.COMPARATOR_BY_NAME);

	for (final Registration registration : bean.getStudent().getRegistrations()) {

	    if (!registration.isBolonha()) {
		continue;
	    }

	    final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
	    for (final CycleCurriculumGroup cycle : studentCurricularPlan.getCycleCurriculumGroups()) {

		if (cycle.hasConclusionProcess()) {
		    continue;
		}

		final DegreeCurricularPlan degreeCurricularPlan = cycle.getDegreeCurricularPlanOfDegreeModule();
		final List<DegreeModule> modules = degreeCurricularPlan.getDcpDegreeModules(OptionalCurricularCourse.class,
			ExecutionYear.readCurrentExecutionYear());

		final Iterator<DegreeModule> degreeModulesIter = modules.iterator();
		while (degreeModulesIter.hasNext()) {
		    final CurricularCourse curricularCourse = (CurricularCourse) degreeModulesIter.next();
		    if (studentCurricularPlan.isApproved(curricularCourse)
			    || studentCurricularPlan.getCurricularCoursePossibleGroupsWithoutNoCourseGroupCurriculumGroups(
				    curricularCourse).isEmpty()) {
			degreeModulesIter.remove();
		    }
		}

		result.addAll(modules);
	    }
	}

	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
