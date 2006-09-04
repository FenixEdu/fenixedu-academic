/*
 * Created on 8/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoWrittenEvaluationWithRoomOcupations extends InfoWrittenEvaluation {

    public void copyFromDomain(WrittenEvaluation writtenEvaluation) {
        super.copyFromDomain(writtenEvaluation);
        if (writtenEvaluation != null) {
            setAssociatedRoomOccupation(copyIRoomOccupation2InfoRoomOccupation(writtenEvaluation
                    .getAssociatedRoomOccupation()));
        }
    }

    private List copyIRoomOccupation2InfoRoomOccupation(List associatedRoomOccupation) {
        List infoRoomOccupation = null;

        infoRoomOccupation = (List) CollectionUtils.collect(associatedRoomOccupation, new Transformer() {

            public Object transform(Object arg0) {
                RoomOccupation roomOccupation = (RoomOccupation) arg0;
                return InfoRoomOccupation.newInfoFromDomain(roomOccupation);
            }
        });

        return infoRoomOccupation;
    }

    public static InfoWrittenEvaluation newInfoFromDomain(WrittenEvaluation writtenEvaluation) {
        InfoWrittenEvaluationWithRoomOcupations infoWrittenEvaluation = null;
        if (writtenEvaluation != null) {
            infoWrittenEvaluation = new InfoWrittenEvaluationWithRoomOcupations();
            infoWrittenEvaluation.copyFromDomain(writtenEvaluation);
        }
        return infoWrittenEvaluation;
    }
}