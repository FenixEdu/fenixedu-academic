/*
 * 
 * Created on 2003/08/15
 */

package ServidorAplicacao.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Lesson;
import Dominio.ILesson;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class DeleteLessons implements IService {

    public Object run(List lessonOIDs) throws FenixServiceException, ExcepcaoPersistencia {

        boolean result = false;
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        for (int j = 0; j < lessonOIDs.size(); j++) {
            ILesson lesson = (ILesson) sp.getIAulaPersistente().readByOID(Lesson.class,
                    (Integer) lessonOIDs.get(j));

            sp.getIPersistentRoomOccupation().delete(lesson.getRoomOccupation());
            sp.getIAulaPersistente().delete(lesson);
        }

        result = true;

        return new Boolean(result);

    }

}