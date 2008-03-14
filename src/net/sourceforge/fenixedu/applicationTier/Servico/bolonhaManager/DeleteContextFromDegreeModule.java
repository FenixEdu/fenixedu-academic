/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteContextFromDegreeModule extends Service {

    public void run(final Integer degreeModuleID, final Integer contextID) throws ExcepcaoPersistencia, FenixServiceException {
	final DegreeModule degreeModule = rootDomainObject.readDegreeModuleByOID(degreeModuleID);
	if (degreeModule == null) {
	    throw new FenixServiceException("error.noDegreeModule");
	}
	final Context context = rootDomainObject.readContextByOID(contextID);
	if (context == null) {
	    throw new FenixServiceException("error.noCourseGroup");
	}
	degreeModule.deleteContext(context);
    }
}
