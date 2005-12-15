/**
 * Nov 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import net.sourceforge.fenixedu.domain.teacher.ITeacherAdviseService;
import net.sourceforge.fenixedu.domain.teacher.TeacherAdviseService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DeleteTeacherAdviseServiceByOID implements IService {

    public void run(Integer teacherAdviseServiceID) throws ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ITeacherAdviseService teacherAdviseService = (ITeacherAdviseService) persistentSupport
                .getIPersistentObject().readByOID(TeacherAdviseService.class, teacherAdviseServiceID);
        teacherAdviseService.delete();
    }
}
