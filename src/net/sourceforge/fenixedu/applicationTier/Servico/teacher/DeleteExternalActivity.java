/*
 * Created on 11/Ago/2005 - 19:12:31
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class DeleteExternalActivity extends Service {

    public void run(Integer externalActivityId) {
	ExternalActivity externalActivity = rootDomainObject.readExternalActivityByOID(externalActivityId);
	externalActivity.delete();
    }

}
