package net.sourceforge.fenixedu.domain.teacher.evaluation;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public abstract class TeacherEvaluation extends TeacherEvaluation_Base {

    public TeacherEvaluation() {
	super();
	setCreatedDate(new DateTime());
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public TeacherEvaluationState getState() {
	if (getAutoEvaluationMark() != null) {
	    if (getTeacherEvaluationProcess().getFacultyEvaluationProcess().getEvaluationInterval().containsNow()) {
		return TeacherEvaluationState.AUTO_EVALUATION;
	    } else {
		return null;
	    }
	} else if (getEvaluationMark() != null) {
	    return TeacherEvaluationState.EVALUATION;
	} else {
	    return TeacherEvaluationState.EVALUATED;
	}
    }

    public abstract TeacherEvaluationType getType();
}
