package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.student.OptionalCurricularCoursesLocationBean.EnrolmentLocationBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurriculumGroupsProviderForEnrolmentsLocationManagement implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final EnrolmentLocationBean bean = (EnrolmentLocationBean) source;
	final StudentCurricularPlan studentCurricularPlan = bean.getEnrolment().getStudentCurricularPlan();
	final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();

	final List<DegreeModule> dcpDegreeModules = degreeCurricularPlan.getDcpDegreeModules(OptionalCurricularCourse.class,
		ExecutionYear.readCurrentExecutionYear());
	final Iterator<DegreeModule> degreeModulesIter = dcpDegreeModules.iterator();
	while (degreeModulesIter.hasNext()) {
	    final CurricularCourse curricularCourse = (CurricularCourse) degreeModulesIter.next();
	    if (studentCurricularPlan.isApproved(curricularCourse)
		    || studentCurricularPlan.getCurricularCoursePossibleGroupsWithoutNoCourseGroupCurriculumGroups(
			    curricularCourse).isEmpty()) {
		degreeModulesIter.remove();
	    }
	}

	Collections.sort(dcpDegreeModules, DegreeModule.COMPARATOR_BY_NAME);

	return dcpDegreeModules;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
