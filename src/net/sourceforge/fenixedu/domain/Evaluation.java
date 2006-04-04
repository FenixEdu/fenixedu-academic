package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.util.EvaluationType;


/**
 * @author T�nia Pous�o 24 de Junho de 2003
 */
public abstract class Evaluation extends Evaluation_Base {

    public Evaluation() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public List<ExecutionCourse> getAttendingExecutionCoursesFor(final Student student) {
        final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        for (final ExecutionCourse executionCourse : this.getAssociatedExecutionCourses()) {
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
		for (; !getMarks().isEmpty(); getMarks().get(0).delete());
        removeRootDomainObject();
        super.deleteDomainObject();
    }

    public static List<Exam> readExams() {
        List<Exam> result = new ArrayList<Exam>();
        
        for (Evaluation evaluation : RootDomainObject.getInstance().getEvaluations()) {
            if (evaluation instanceof Exam) {
                result.add((Exam) evaluation);
            }
        }
        
        return result;
    }
    
    public static List<WrittenEvaluation> readWrittenEvaluations() {
        List<WrittenEvaluation> result = new ArrayList<WrittenEvaluation>();
        
        for (Evaluation evaluation : RootDomainObject.getInstance().getEvaluations()) {
            if (evaluation instanceof Evaluation) {
                result.add((WrittenEvaluation) evaluation);
            }
        }
        
        return result;
    }
    
    public static List<OnlineTest> readOnlineTests() {
        List<OnlineTest> result = new ArrayList<OnlineTest>();
        
        for (Evaluation evaluation : RootDomainObject.getInstance().getEvaluations()) {
            if (evaluation instanceof OnlineTest) {
                result.add((OnlineTest) evaluation);
            }
        }
        
        return result;
    }
    
    public Mark addNewMark(Attends attends, String markValue) {
		Mark mark =  new Mark();
		mark.setAttend(attends);
		mark.setEvaluation(this);
		mark.setMark(markValue);
		return mark;
	}

	public abstract EvaluationType getEvaluationType() ;
	
	public Mark getMarkByAttend(Attends attends) {
		for (Mark mark : getMarks()) {
			if(mark.getAttend().equals(attends)) {
				return mark;
			}
		}
		return null;
	}
}
