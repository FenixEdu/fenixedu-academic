/*
 * Created on 9/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IRoomOccupation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

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
        List infoRoomOccupation = null;

        infoRoomOccupation = (List) CollectionUtils.collect(associatedRoomOccupation, new Transformer() {

            public Object transform(Object arg0) {
                IRoomOccupation roomOccupation = (IRoomOccupation) arg0;
                return InfoRoomOccupationWithInfoRoom.newInfoFromDomain(roomOccupation);
            }
        });

        return infoRoomOccupation;
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