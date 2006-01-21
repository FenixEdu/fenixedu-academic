/**
 * Nov 24, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InstitutionWorkTimeDTO;
import net.sourceforge.fenixedu.domain.teacher.InstitutionWorkTime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class EditTeacherInstitutionWorkTime extends Service {

    public void run(InstitutionWorkTimeDTO institutionWorkTimeDTO) throws ExcepcaoPersistencia {
        InstitutionWorkTime institutionWorkTime = (InstitutionWorkTime) persistentSupport
                .getIPersistentInstitutionWorkTime().readByOID(InstitutionWorkTime.class,
                        institutionWorkTimeDTO.getIdInternal());
        
        institutionWorkTime.update(institutionWorkTimeDTO);
    }
}
