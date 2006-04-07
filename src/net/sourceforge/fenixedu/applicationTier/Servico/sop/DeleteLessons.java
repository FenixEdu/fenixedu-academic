/*
 * 
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteLessons extends Service {

    public Boolean run(final List<Integer> lessonOIDs) throws ExcepcaoPersistencia {
        for (final Integer lessonOID : lessonOIDs) {
            rootDomainObject.readLessonByOID(lessonOID).delete();
        }

        return Boolean.TRUE;
    }

}
