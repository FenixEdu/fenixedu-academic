/*
 * Created on 11/Ago/2005 - 19:12:31
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class DeleteExternalActivity extends FenixService {

    public void run(Integer externalActivityId) {
	ExternalActivity externalActivity = rootDomainObject.readExternalActivityByOID(externalActivityId);
	externalActivity.delete();
    }

}
