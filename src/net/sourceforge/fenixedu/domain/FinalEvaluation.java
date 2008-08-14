package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.EvaluationType;

/**
 * @author T�nia Pous�o
 * 
 */
public class FinalEvaluation extends FinalEvaluation_Base {

    public FinalEvaluation() {
	super();
    }

    public Integer getGradesListVersion() {
	int lastVersion = 0;
	for (Mark mark : getMarks()) {
	    FinalMark finalMark = (FinalMark) mark;
	    if (finalMark.getGradeListVersion() > lastVersion) {
		lastVersion = finalMark.getGradeListVersion();
	    }
	}
	if (lastVersion == 99) {
	    throw new DomainException("grades list version cannot be higher than 99");
	}
	return Integer.valueOf(++lastVersion);
    }

    public FinalMark addNewMark(Attends attends, String markValue) {
	FinalMark mark = new FinalMark();
	mark.setAttend(attends);
	mark.setEvaluation(this);
	mark.setMark(markValue);
	return mark;
    }

    public EvaluationType getEvaluationType() {
	return EvaluationType.FINAL_TYPE;
    }

    public List<FinalMark> getAlreadySubmitedMarks(ExecutionCourse executionCourse) {
	List<FinalMark> result = new ArrayList<FinalMark>();
	for (Mark mark : getMarks()) {
	    FinalMark finalMark = (FinalMark) mark;
	    if (finalMark.getAttend().getExecutionCourse().equals(executionCourse) && finalMark.getGradeListVersion() != 0
		    && finalMark.getSubmitedMark() != null) {
		result.add(finalMark);
	    }
	}
	return result;
    }

    public List<Attends> getNotSubmitedMarkAttends(ExecutionCourse executionCourse) {
	List<Attends> result = new ArrayList<Attends>();

	for (Attends attends : executionCourse.getAttends()) {
	    if (attends.getEnrolment() != null && attends.getRegistration().getDegreeType().equals(DegreeType.DEGREE)) {
		FinalMark mark = getFinalMark(attends);
		if (mark == null || (mark.getGradeListVersion() == 0 && mark.getSubmitedMark() == null)) {
		    result.add(attends);
		}
	    }
	}
	return result;
    }

    private FinalMark getFinalMark(Attends attends) {
	for (Mark mark : attends.getAssociatedMarks()) {
	    if (mark.getEvaluation().equals(this)) {
		return (FinalMark) mark;
	    }
	}
	return null;
    }

    private void checkRulesToDelete() {
	if (hasAnyMarks()) {
	    throw new DomainException("error.existing.marks");
	}
    }

    @Override
    public void delete() {
	checkRulesToDelete();
	super.delete();
    }

    @Override
    public boolean isFinal() {
	return true;
    }
}