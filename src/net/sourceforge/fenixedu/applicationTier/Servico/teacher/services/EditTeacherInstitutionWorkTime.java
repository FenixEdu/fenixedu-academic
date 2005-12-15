/**
 * Nov 24, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InstitutionWorkTimeDTO;
import net.sourceforge.fenixedu.domain.teacher.IInstitutionWorkTime;
import net.sourceforge.fenixedu.domain.teacher.InstitutionWorkTime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class EditTeacherInstitutionWorkTime implements IService {

    public void run(InstitutionWorkTimeDTO institutionWorkTimeDTO) throws ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IInstitutionWorkTime institutionWorkTime = (IInstitutionWorkTime) persistentSupport
                .getIPersistentInstitutionWorkTime().readByOID(InstitutionWorkTime.class,
                        institutionWorkTimeDTO.getIdInternal());
        
        institutionWorkTime.update(institutionWorkTimeDTO);
    }
}
