package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.ImportContentBean;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionCoursesToImportLessonPlanningsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        ImportContentBean bean = (ImportContentBean) source;                
        ExecutionSemester executionSemester = bean.getExecutionPeriod();
        CurricularYear curricularYear = bean.getCurricularYear();
        DegreeCurricularPlan degreeCurricularPlan = bean.getExecutionDegree().getDegreeCurricularPlan();        
        if(degreeCurricularPlan != null && executionSemester != null && curricularYear != null) {
            List<ExecutionCourse> executionCourses = degreeCurricularPlan.getExecutionCoursesByExecutionPeriodAndSemesterAndYear(executionSemester, curricularYear.getYear(), executionSemester.getSemester());
            Collections.sort(executionCourses, ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);
            return executionCourses;
        }        
        return new ArrayList<ExecutionCourse>();
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();  
    }

}
