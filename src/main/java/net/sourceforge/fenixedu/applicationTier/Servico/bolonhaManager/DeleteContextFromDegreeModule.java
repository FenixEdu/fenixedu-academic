/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DeleteContextFromDegreeModule {

    @Service
    public static void run(final String degreeModuleID, final String contextID) throws FenixServiceException {
        final DegreeModule degreeModule = AbstractDomainObject.fromExternalId(degreeModuleID);
        if (degreeModule == null) {
            throw new FenixServiceException("error.noDegreeModule");
        }
        final Context context = AbstractDomainObject.fromExternalId(contextID);
        if (context == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }
        degreeModule.deleteContext(context);
    }
}
