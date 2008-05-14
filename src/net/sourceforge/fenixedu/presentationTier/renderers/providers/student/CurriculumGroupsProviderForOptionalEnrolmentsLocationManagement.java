package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.student.OptionalCurricularCoursesLocationBean.OptionalEnrolmentLocationBean;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class CurriculumGroupsProviderForOptionalEnrolmentsLocationManagement implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final OptionalEnrolmentLocationBean bean = (OptionalEnrolmentLocationBean) source;
	final StudentCurricularPlan studentCurricularPlan = bean.getEnrolment().getStudentCurricularPlan();
	final Collection<? extends CurriculumGroup> curricularCoursePossibleGroups = studentCurricularPlan
		.getCurricularCoursePossibleGroupsWithoutNoCourseGroupCurriculumGroups(bean.getEnrolment().getCurricularCourse());
	curricularCoursePossibleGroups.remove(bean.getEnrolment().getCurriculumGroup());
	return curricularCoursePossibleGroups;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
