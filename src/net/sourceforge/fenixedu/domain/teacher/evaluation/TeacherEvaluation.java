package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public abstract class TeacherEvaluation extends TeacherEvaluation_Base {

    public TeacherEvaluation() {
	super();
	setCreatedDate(new DateTime());
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public TeacherEvaluationState getState() {
	if (getTeacherEvaluationProcess().getFacultyEvaluationProcess().getAutoEvaluationInterval().getStart().isAfterNow()) {
	    return null;
	} else if (getAutoEvaluationLock() == null) {
	    return TeacherEvaluationState.AUTO_EVALUATION;
	} else if (getEvaluationLock() == null) {
	    return TeacherEvaluationState.EVALUATION;
	} else {
	    return TeacherEvaluationState.EVALUATED;
	}
    }

    public abstract TeacherEvaluationType getType();

    public abstract Set<TeacherEvaluationFileType> getAutoEvaluationFileSet();

    public abstract Set<TeacherEvaluationFileType> getEvaluationFileSet();

    public TeacherEvaluationMark getApprovedEvaluationMark() {
	return getTeacherEvaluationProcess().getApprovedEvaluationMark();
    }

    public void setApprovedEvaluationMark(TeacherEvaluationMark mark) {
	getTeacherEvaluationProcess().setApprovedEvaluationMark(mark);
    }

    @Service
    public void lickAutoEvaluationStamp() {
	setAutoEvaluationLock(new DateTime());
    }

    @Service
    public void lickEvaluationStamp() {
	setEvaluationLock(new DateTime());
    }
}
