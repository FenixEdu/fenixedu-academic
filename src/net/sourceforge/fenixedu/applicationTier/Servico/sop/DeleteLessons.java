/*
 * 
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteLessons implements IService {

    public Object run(List lessonOIDs) throws ExcepcaoPersistencia {

        boolean result = false;
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

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