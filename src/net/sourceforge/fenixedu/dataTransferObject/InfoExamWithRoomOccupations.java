/*
 * Created on 9/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoExamWithRoomOccupations extends InfoExam {

    public void copyFromDomain(Exam exam) {
        super.copyFromDomain(exam);
        if (exam != null) {
            setAssociatedRoomOccupation(copyIRoomOccupation2InfoRoomOccupation(exam
                    .getAssociatedRoomOccupation()));
        }
    }
    
    private List<InfoRoomOccupation> copyIRoomOccupation2InfoRoomOccupation(List associatedRoomOccupation) {
        final List<InfoRoomOccupation> infoRoomOccupations = new ArrayList<InfoRoomOccupation>(associatedRoomOccupation.size());
        for (final Iterator iterator = associatedRoomOccupation.iterator(); iterator.hasNext(); ) {
            final RoomOccupation roomOccupation = (RoomOccupation) iterator.next();
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