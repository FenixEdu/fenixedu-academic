package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ReadEmptyRoomsForExam implements IServico {
    private static ReadEmptyRoomsForExam serviceInstance = new ReadEmptyRoomsForExam();

    /**
     * The singleton access method of this class.
     */
    public static ReadEmptyRoomsForExam getService() {
        return serviceInstance;
    }

    /**
     * The actor of this class.
     */
    private ReadEmptyRoomsForExam() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadEmptyRoomsForExam";
    }

    public List run(InfoExam infoExam) throws FenixServiceException {

        List availableInfoRooms = null;

        Transformer TRANSFORM_TO_INFOROOM = new Transformer() {
            public Object transform(Object input) {
                return Cloner.copyRoom2InfoRoom((IRoom) input);
            }
        };

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            ISalaPersistente persistentRoom = sp.getISalaPersistente();
            IExam exam = Cloner.copyInfoExam2IExam(infoExam);
            List availableRooms = persistentRoom.readAvailableRooms(exam);
            availableInfoRooms = (List) CollectionUtils.collect(availableRooms, TRANSFORM_TO_INFOROOM);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return availableInfoRooms;
    }

}