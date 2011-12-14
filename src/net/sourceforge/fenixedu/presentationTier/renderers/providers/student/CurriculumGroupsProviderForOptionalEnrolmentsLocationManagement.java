package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import java.util.Collection;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.student.OptionalCurricularCoursesLocationBean.OptionalEnrolmentLocationBean;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurriculumGroupsProviderForOptionalEnrolmentsLocationManagement implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final OptionalEnrolmentLocationBean bean = (OptionalEnrolmentLocationBean) source;

	final Collection<CurriculumGroup> result = new TreeSet<CurriculumGroup>(
		CurriculumGroup.COMPARATOR_BY_FULL_PATH_NAME_AND_ID);
	for (final Registration registration : bean.getStudent().getRegistrations()) {

	    if (!registration.isBolonha()) {
		continue;
	    }

	    final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
	    result.addAll(studentCurricularPlan.getCurricularCoursePossibleGroups(bean.getEnrolment().getCurricularCourse()));
	}
	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
