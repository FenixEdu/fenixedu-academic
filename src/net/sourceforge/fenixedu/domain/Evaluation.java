package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Tânia Pousão 24 de Junho de 2003
 */
public class Evaluation extends Evaluation_Base {
    
    public List<IExecutionCourse> getAttendingExecutionCoursesFor(final IStudent student) {
        final List<IExecutionCourse> result = new ArrayList<IExecutionCourse>();
        for (final IExecutionCourse executionCourse : this.getAssociatedExecutionCourses()) {
            if (student.attends(executionCourse)) {
                result.add(executionCourse);
            }
        }
        if (result.isEmpty()) { //Then user does not attend any executioncourse
            result.addAll(this.getAssociatedExecutionCourses());            
        }
        return result;
    }    
}
