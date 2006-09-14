package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.ShowSummariesBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SummaryTeacherBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ListExecutionCourseTeachersToShowSummariesInDepartmentAdmOfficeProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	ExecutionCourse executionCourse = ((ShowSummariesBean) source).getExecutionCourse();
	Professorship professorshipLogged = ((ShowSummariesBean) source).getProfessorshipLogged();    	
	List<SummaryTeacherBean> teachers = new ArrayList<SummaryTeacherBean>();    	
    	if(executionCourse != null && professorshipLogged != null) {    	        	       	   
    	    teachers.add(teachers.size(), new SummaryTeacherBean(professorshipLogged));	    
    	    teachers.add(teachers.size(), new SummaryTeacherBean(Boolean.TRUE));
    	}
    	return teachers;
    }

    public Converter getConverter() {
	return null;
    }
}
