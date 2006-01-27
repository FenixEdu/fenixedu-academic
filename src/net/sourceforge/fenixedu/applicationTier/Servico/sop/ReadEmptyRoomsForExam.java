package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ReadEmptyRoomsForExam extends Service {

    public List run(InfoExam infoExam) throws FenixServiceException, ExcepcaoPersistencia {

        List availableInfoRooms = null;

        Transformer TRANSFORM_TO_INFOROOM = new Transformer() {
            public Object transform(Object input) {
                return InfoRoom.newInfoFromDomain((OldRoom) input);
            }
        };

        ISalaPersistente persistentRoom = persistentSupport.getISalaPersistente();
        List availableRooms = persistentRoom.readAvailableRooms(infoExam.getIdInternal());
        availableInfoRooms = (List) CollectionUtils.collect(availableRooms, TRANSFORM_TO_INFOROOM);
        return availableInfoRooms;
    }

}
