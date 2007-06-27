/*
 * Created on 8/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;

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
            setWrittenEvaluationSpaceOccupations(copyIRoomOccupation2InfoRoomOccupation(writtenEvaluation.getWrittenEvaluationSpaceOccupations()));
        }
    }

    private List<InfoRoomOccupation> copyIRoomOccupation2InfoRoomOccupation(List<WrittenEvaluationSpaceOccupation> associatedRoomOccupation) {
        List<InfoRoomOccupation> infoRoomOccupation = null;

        infoRoomOccupation = (List<InfoRoomOccupation>) CollectionUtils.collect(associatedRoomOccupation, new Transformer() {

            public Object transform(Object arg0) {
        	WrittenEvaluationSpaceOccupation roomOccupation = (WrittenEvaluationSpaceOccupation) arg0;
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