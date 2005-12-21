package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.util.EvaluationType;


/**
 * @author Tânia Pousão 24 de Junho de 2003
 */
public abstract class Evaluation extends Evaluation_Base {
	
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
    
    public void delete() {        
        this.getAssociatedExecutionCourses().clear();        
        super.deleteDomainObject();
    }

	public IMark addNewMark(IAttends attends, String markValue) {
		IMark mark =  new Mark();
		mark.setAttend(attends);
		mark.setEvaluation(this);
		mark.setMark(markValue);
		return mark;
	}

	public abstract EvaluationType getEvaluationType() ;
}
