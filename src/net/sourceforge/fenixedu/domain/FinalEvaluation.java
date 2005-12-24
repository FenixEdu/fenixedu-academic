package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.EvaluationType;

/**
 * @author Tânia Pousão
 * 
 */
public class FinalEvaluation extends FinalEvaluation_Base {

	
    public FinalEvaluation() {
        this.setOjbConcreteClass(FinalEvaluation.class.getName());
    }

    public boolean deleteFrom(IExecutionCourse executionCourse) {
        removeMarks();
        return remove(executionCourse);
    }

    private boolean remove(IExecutionCourse executionCourse) {
        if (this.getAssociatedExecutionCoursesCount() == 1
                && this.getAssociatedExecutionCourses().contains(executionCourse)){
                /*&& this.getEvaluationExecutionCoursesCount() == 1) {
            final IEvaluationExecutionCourse evaluationExecutionCourse = this
                    .getEvaluationExecutionCourses(0);
            if (evaluationExecutionCourse.getExecutionCourse().equals(executionCourse)) {
                this.getEvaluationExecutionCourses().remove(evaluationExecutionCourse);*/
                this.getAssociatedExecutionCourses().remove(executionCourse);
                return true;
            //}
        }
        return false;
    }

    private void removeMarks() {
        if (this.getMarksCount() > 0) {
            throw new DomainException("error.existing.marks");
        }
    }
    
    public Integer getGradesListVersion() {
    	int lastVersion = 0;
    	for (IMark mark : getMarks()) {
    		IFinalMark finalMark = (IFinalMark) mark;
    		if(finalMark.getGradeListVersion() > lastVersion) {
    			lastVersion = finalMark.getGradeListVersion();
    		}
		}
    	if(lastVersion == 99) {
    		throw new DomainException("grades list version cannot be higher than 99");
    	}
    	return Integer.valueOf(++lastVersion);
    }
    
	public IFinalMark addNewMark(IAttends attends, String markValue) {
		IFinalMark mark =  new FinalMark();
		mark.setAttend(attends);
		mark.setEvaluation(this);
		mark.setMark(markValue);
		return mark;
	}   
	
	public EvaluationType getEvaluationType() {
		return EvaluationType.FINAL_TYPE;
	}
	
	public List<IFinalMark> getAlreadySubmitedMarks(IExecutionCourse executionCourse){
		List<IFinalMark> result = new ArrayList<IFinalMark>();
		for (IMark mark : getMarks()) {
			IFinalMark finalMark = (IFinalMark) mark;
			if(finalMark.getAttend().getDisciplinaExecucao().equals(executionCourse) 
					&& finalMark.getGradeListVersion() != 0 && finalMark.getSubmitedMark() != null) {
				result.add(finalMark);
			}
		}
		return result;
	}
	
	public List<IAttends> getNotSubmitedMarkAttends(IExecutionCourse executionCourse){
		List<IAttends> result = new ArrayList<IAttends>();
		
		for (IAttends attends : executionCourse.getAttends()) {
			if(attends.getEnrolment() != null && attends.getAluno().getDegreeType().equals(DegreeType.DEGREE)) {
				IFinalMark mark = getFinalMark(attends);
				if(mark == null || (mark.getGradeListVersion() == 0 && mark.getSubmitedMark() == null)) {
					result.add(attends);
				}
			}
		}
		return result;
	}

	private IFinalMark getFinalMark(IAttends attends) {
		for (IMark mark : attends.getAssociatedMarks()) {
			if(mark.getEvaluation().equals(this)) {
				return (IFinalMark) mark;
			}
		}
		return null;
	}
}