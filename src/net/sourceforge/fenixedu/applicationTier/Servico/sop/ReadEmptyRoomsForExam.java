package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ReadEmptyRoomsForExam extends Service {

    public List run(InfoExam infoExam) throws FenixServiceException, ExcepcaoPersistencia {

        List availableInfoRooms = null;

        Transformer TRANSFORM_TO_INFOROOM = new Transformer() {
            public Object transform(Object input) {
                return InfoRoom.newInfoFromDomain((Room) input);
            }
        };

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ISalaPersistente persistentRoom = sp.getISalaPersistente();
        List availableRooms = persistentRoom.readAvailableRooms(infoExam.getIdInternal());
        availableInfoRooms = (List) CollectionUtils.collect(availableRooms, TRANSFORM_TO_INFOROOM);
        return availableInfoRooms;
    }

}
