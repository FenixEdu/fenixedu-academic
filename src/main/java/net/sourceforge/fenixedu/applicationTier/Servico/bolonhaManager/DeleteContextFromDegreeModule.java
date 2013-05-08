/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteContextFromDegreeModule {

    @Service
    public static void run(final Integer degreeModuleID, final Integer contextID) throws FenixServiceException {
        final DegreeModule degreeModule = RootDomainObject.getInstance().readDegreeModuleByOID(degreeModuleID);
        if (degreeModule == null) {
            throw new FenixServiceException("error.noDegreeModule");
        }
        final Context context = RootDomainObject.getInstance().readContextByOID(contextID);
        if (context == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }
        degreeModule.deleteContext(context);
    }
}
