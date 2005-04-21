/*
 * Created on 9/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IRoomOccupation;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoExamWithRoomOccupations extends InfoExam {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoExam#copyFromDomain(Dominio.IExam)
     */
    public void copyFromDomain(IExam exam) {
        super.copyFromDomain(exam);
        if (exam != null) {
            setAssociatedRoomOccupation(copyIRoomOccupation2InfoRoomOccupation(exam
                    .getAssociatedRoomOccupation()));
        }
    }

    /**
     * @param associatedRoomOccupation
     * @return
     */
    private List copyIRoomOccupation2InfoRoomOccupation(List associatedRoomOccupation) {
        final List infoRoomOccupations = new ArrayList(associatedRoomOccupation.size());
        for (final Iterator iterator = associatedRoomOccupation.iterator(); iterator.hasNext(); ) {
            final IRoomOccupation roomOccupation = (IRoomOccupation) iterator.next();
            final InfoRoomOccupation infoRoomOccupation = InfoRoomOccupationWithInfoRoom.newInfoFromDomain(roomOccupation);
            if (infoRoomOccupation != null) {
                infoRoomOccupations.add(infoRoomOccupation);
            }
        }
        return infoRoomOccupations;
    }

    public static InfoExam newInfoFromDomain(IExam exam) {
        InfoExamWithRoomOccupations infoExam = null;
        if (exam != null) {
            infoExam = new InfoExamWithRoomOccupations();
            infoExam.copyFromDomain(exam);
        }
        return infoExam;
    }
}