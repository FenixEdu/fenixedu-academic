package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.ShowSummariesBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SummaryTeacherBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ListExecutionCourseTeachersToShowSummariesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	ExecutionCourse executionCourse = ((ShowSummariesBean) source).getExecutionCourse();
    	List<SummaryTeacherBean> teachers = new ArrayList<SummaryTeacherBean>();
    	Set<Professorship> professorships = new TreeSet<Professorship>(Professorship.COMPARATOR_BY_PERSON_NAME);
    	if(executionCourse != null) {    	    
    	    professorships.addAll(executionCourse.getProfessorshipsSet());
    	    for (Professorship professorship : professorships) {
		teachers.add(teachers.size(), new SummaryTeacherBean(professorship));
	    }
    	    teachers.add(teachers.size(), new SummaryTeacherBean(Boolean.TRUE));
    	}
    	return teachers;
    }

    public Converter getConverter() {
	return null;
    }
}
