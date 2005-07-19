package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ReadEmptyRoomsForExam implements IService {

    public List run(InfoExam infoExam) throws FenixServiceException, ExcepcaoPersistencia {

        List availableInfoRooms = null;

        Transformer TRANSFORM_TO_INFOROOM = new Transformer() {
            public Object transform(Object input) {
                return Cloner.copyRoom2InfoRoom((IRoom) input);
            }
        };

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ISalaPersistente persistentRoom = sp.getISalaPersistente();
        List availableRooms = persistentRoom.readAvailableRooms(infoExam.getIdInternal());
        availableInfoRooms = (List) CollectionUtils.collect(availableRooms, TRANSFORM_TO_INFOROOM);
        return availableInfoRooms;
    }

}
