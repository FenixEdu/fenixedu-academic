package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.curriculumLine.CurriculumLineLocationBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurriculumGroupsProviderForMoveCurriculumLines implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final CurriculumLineLocationBean bean = (CurriculumLineLocationBean) source;

	final Student student = bean.getStudent();
	final Set<CurriculumGroup> result = new HashSet<CurriculumGroup>();

	for (final Registration registration : student.getRegistrations()) {

	    if (!registration.isBolonha()) {
		result.addAll(registration.getLastStudentCurricularPlan().getAllCurriculumGroups());
		continue;
	    }

	    final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
	    result.addAll(studentCurricularPlan.getNoCourseGroupCurriculumGroups());

	    for (final CycleCurriculumGroup cycle : studentCurricularPlan.getCycleCurriculumGroups()) {

		if (bean.isWithRules() && isConcluded(student, cycle)) {
		    continue;
		}

		result.addAll(cycle.getAllCurriculumGroups());
	    }
	}

	return result;
    }

    private boolean isConcluded(final Student student, final CycleCurriculumGroup cycle) {
	return cycle.hasConclusionProcess()
		|| (cycle.isExternal() && student.hasRegistrationFor(cycle.getDegreeCurricularPlanOfDegreeModule()));
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
