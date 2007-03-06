package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceMultipleException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.InvalidMarkDomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class WriteMarks extends Service {

    public void run(final Integer executioCourseOID, final Integer evaluationOID,
	    final Map<Integer, String> marks) throws ExcepcaoPersistencia, FenixServiceException {
	final List<DomainException> exceptionList = new ArrayList<DomainException>();

	final Evaluation evaluation = rootDomainObject.readEvaluationByOID(evaluationOID);
	final ExecutionCourse executionCourse = rootDomainObject
		.readExecutionCourseByOID(executioCourseOID);

	for (final Entry<Integer, String> entry : marks.entrySet()) {
	    final Integer studentNumber = entry.getKey();
	    final String markValue = entry.getValue();

	    final Attends attends = findStudentAttends(executionCourse, studentNumber);

	    if (attends != null) {
		if(attends.isEnrolledOrWithActiveSCP()) {
		    final Mark mark = findExistingMark(attends.getAssociatedMarks(), evaluation);

		    if (markValue == null || markValue.length() == 0) {
			if (mark != null) {
			    mark.delete();
			}
		    } else {
			try {
			    if (mark == null) {
				evaluation.addNewMark(attends, markValue);
			    } else {
				mark.setMark(markValue);
			    }
			} catch (InvalidMarkDomainException e) {
			    exceptionList.add(e);
			}
		    }
		} else {
		    exceptionList.add(new DomainException("errors.student.not.active", studentNumber.toString()));
		}
	    } else {
		exceptionList.add(new DomainException("errors.noStudent", studentNumber.toString()));
	    }
	}
	if (!exceptionList.isEmpty()) {
	    throw new FenixServiceMultipleException(exceptionList);
	}
    }

    private Attends findStudentAttends(final ExecutionCourse executionCourse, final Integer studentNumber) {
	for (final Attends attends : executionCourse.getAttends()) {
	    if (attends.getRegistration().getNumber().equals(studentNumber)) {
		return attends;
	    }
	}
	return null;
    }

    private Mark findExistingMark(final List<Mark> marks, Evaluation evaluation) {
	for (final Mark mark : marks) {
	    final Evaluation markEvaluation = mark.getEvaluation();
	    if (markEvaluation.equals(evaluation)) {
		return mark;
	    }
	}
	return null;
    }

}
