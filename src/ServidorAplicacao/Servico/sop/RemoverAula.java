package ServidorAplicacao.Servico.sop;

/**
 * Serviï¿½o RemoverAula
 * 
 * @author tfc130
 * @version
 */

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.ILesson;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class RemoverAula implements IService {

    // FIXME : O serviço nao devolve False quando a aula nao existe!...

    public Object run(InfoLesson infoLesson, InfoShift infoShift) {
        boolean result = false;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            //IRoom room =
            // sp.getISalaPersistente().readByName(infoLesson.getInfoSala().getNome());

            //IShift shift = Cloner.copyInfoShift2Shift(infoShift);
            ILesson lesson = Cloner.copyInfoLesson2Lesson(infoLesson);

            sp.getIAulaPersistente().delete(lesson);
            //      sp.getITurnoAulaPersistente().delete(shift,
            // infoLesson.getDiaSemana(),
            //                                           infoLesson.getInicio(), infoLesson.getFim(), room);
            result = true;
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }

        return new Boolean(result);
    }

}