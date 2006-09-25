package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.InvalidMarkDomainException;
import net.sourceforge.fenixedu.util.EvaluationType;

public class Mark extends Mark_Base {

    public Mark() {
    	super();
    	setRootDomainObject(RootDomainObject.getInstance());
    	setOjbConcreteClass(this.getClass().getName());
    }

    public Mark(final Attends attends, final Evaluation evaluation, final String mark) {
    	this();
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

    public void delete() {
    	removeAttend();
    	removeEvaluation();
        removeRootDomainObject();
    	deleteDomainObject();
    }
    
    private boolean validateMark(String mark) {
    	GradeScale gradeScale;
    	if(getAttend().getEnrolment() == null) {
    		gradeScale = getAttend().getAluno().getActiveStudentCurricularPlan().getDegreeCurricularPlan().getGradeScaleChain();
    	} else {
    		gradeScale = getAttend().getEnrolment().getCurricularCourse().getGradeScaleChain();
    	}
        if(gradeScale==null && getEvaluation().getEvaluationType().getType() == EvaluationType.ONLINE_TEST) {
            return GradeScale.TYPE20.isValid(mark, getEvaluation().getEvaluationType());
        }
    	return gradeScale.isValid(mark, getEvaluation().getEvaluationType());
    }

}
