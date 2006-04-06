/*
 * Created on 11/Ago/2005 - 16:12:00
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoExternalActivity;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class EditExternalActivity extends Service {

    public void run(Integer externalActivityId, InfoExternalActivity infoExternalActivity) throws FenixServiceException, ExcepcaoPersistencia {
		ExternalActivity externalActivity = (ExternalActivity) persistentObject.readByOID(ExternalActivity.class, externalActivityId);
		//If it doesn't exist in the database, a new one has to be created
		if(externalActivity == null) {
			Teacher teacher = rootDomainObject.readTeacherByOID(infoExternalActivity.getInfoTeacher().getIdInternal());
			externalActivity = DomainFactory.makeExternalActivity(teacher, infoExternalActivity);

		} else {
			externalActivity.edit(infoExternalActivity);
		}
    }
}
