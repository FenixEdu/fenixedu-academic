package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

public class CurricularCoursesProvider extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
	ExecutionCourseSearchBean bean = (ExecutionCourseSearchBean) source;

	if (bean.getSemester() == null) {
	    return new ArrayList<CurricularCourse>();
	}

	if (bean.getDegreeCurricularPlan() == null) {
	    return new ArrayList<CurricularCourse>();
	}

	DegreeCurricularPlan degreeCurricularPlan = bean.getDegreeCurricularPlan();
	ExecutionSemester semester = bean.getSemester();

	return degreeCurricularPlan.getActiveCurricularCourses(semester);
    }

}
