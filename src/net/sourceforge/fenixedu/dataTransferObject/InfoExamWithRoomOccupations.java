/*
 * Created on 9/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;

/**
 * @author Tânia Pousão
 * 
 */
public class InfoExamWithRoomOccupations extends InfoExam {

    public void copyFromDomain(Exam exam) {
	super.copyFromDomain(exam);
	if (exam != null) {
	    setWrittenEvaluationSpaceOccupations(copyIRoomOccupation2InfoRoomOccupation(exam
		    .getWrittenEvaluationSpaceOccupations()));
	}
    }

    private List<InfoRoomOccupation> copyIRoomOccupation2InfoRoomOccupation(
	    List<WrittenEvaluationSpaceOccupation> associatedRoomOccupation) {
	final List<InfoRoomOccupation> infoRoomOccupations = new ArrayList<InfoRoomOccupation>(associatedRoomOccupation.size());
	for (final Iterator<WrittenEvaluationSpaceOccupation> iterator = associatedRoomOccupation.iterator(); iterator.hasNext();) {
	    final WrittenEvaluationSpaceOccupation roomOccupation = iterator.next();
	    final InfoRoomOccupation infoRoomOccupation = InfoRoomOccupation.newInfoFromDomain(roomOccupation);
	    if (infoRoomOccupation != null) {
		infoRoomOccupations.add(infoRoomOccupation);
	    }
	}
	return infoRoomOccupations;
    }

    public static InfoExam newInfoFromDomain(Exam exam) {
	InfoExamWithRoomOccupations infoExam = null;
	if (exam != null) {
	    infoExam = new InfoExamWithRoomOccupations();
	    infoExam.copyFromDomain(exam);
	}
	return infoExam;
    }
}