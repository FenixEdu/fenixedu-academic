package ServidorAplicacao.Servico.sop;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoExam;
import DataBeans.util.Cloner;
import Dominio.IExam;
import Dominio.ISala;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
                return Cloner.copyRoom2InfoRoom((ISala) input);
            }
        };

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
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