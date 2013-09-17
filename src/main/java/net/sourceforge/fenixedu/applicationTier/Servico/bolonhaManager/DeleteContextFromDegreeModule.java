/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteContextFromDegreeModule {

    @Atomic
    public static void run(final String degreeModuleID, final String contextID) throws FenixServiceException {
        final DegreeModule degreeModule = FenixFramework.getDomainObject(degreeModuleID);
        if (degreeModule == null) {
            throw new FenixServiceException("error.noDegreeModule");
        }
        final Context context = FenixFramework.getDomainObject(contextID);
        if (context == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }
        degreeModule.deleteContext(context);
    }
}
