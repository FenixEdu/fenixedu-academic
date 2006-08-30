package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.ImportContentBean;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ExecutionCoursesToImportLessonPlanningsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        ImportContentBean bean = (ImportContentBean) source;                
        ExecutionPeriod executionPeriod = bean.getExecutionPeriod();
        CurricularYear curricularYear = bean.getCurricularYear();
        DegreeCurricularPlan degreeCurricularPlan = bean.getExecutionDegree().getDegreeCurricularPlan();        
        if(degreeCurricularPlan != null && executionPeriod != null && curricularYear != null) {
            List<ExecutionCourse> executionCourses = degreeCurricularPlan.getExecutionCoursesByExecutionPeriodAndSemesterAndYear(executionPeriod, curricularYear.getYear(), executionPeriod.getSemester());
            Collections.sort(executionCourses, ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);
            return executionCourses;
        }        
        return new ArrayList<ExecutionCourse>();
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();  
    }

}
