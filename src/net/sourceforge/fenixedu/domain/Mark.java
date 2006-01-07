package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.InvalidMarkDomainException;

public class Mark extends Mark_Base {

    public Mark() {
    	setOjbConcreteClass(this.getClass().getName());
    }

    public Mark(final Attends attends, final Evaluation evaluation, final String mark) {
        setAttend(attends);
        setEvaluation(evaluation);
        setMark(mark);
        setPublishedMark(null);
    }

    public void setMark(String mark) {
    	if(validateMark(mark)) {
    		super.setMark(mark);
    	} else {
    		throw new InvalidMarkDomainException("errors.invalidMark", mark, getAttend().getAluno().getNumber().toString());
    	}
    }
    
    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "idInternal = " + getIdInternal() + "; ";
        result += "mark = " + getMark() + "; ";
        result += "published mark = " + getPublishedMark() + "; ";
        result += "evaluation= " + getEvaluation().getIdInternal() + "; ";
        result += "attend = " + getAttend().getIdInternal() + "; ]";

        return result;
    }
    
    public void delete() {
    	setAttend(null);
    	setEvaluation(null);
    	deleteDomainObject();
    }
    
    private boolean validateMark(String mark) {
    	GradeScale gradeScale;
    	if(getAttend().getEnrolment() == null) {
    		gradeScale = getAttend().getAluno().getActiveStudentCurricularPlan().getDegreeCurricularPlan().getGradeScaleChain();
    	} else {
    		gradeScale = getAttend().getEnrolment().getCurricularCourse().getGradeScaleChain();
    	}
    	return gradeScale.isValid(mark, getEvaluation().getEvaluationType());
    	
    }

}
