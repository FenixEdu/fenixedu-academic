package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class OrderDegreeModule {

    @Service
    public static void run(final Integer contextID, final Integer position) throws FenixServiceException {
        if (contextID == null) {
            throw new FenixServiceException();
        }

        final Context context = AbstractDomainObject.fromExternalId(contextID);
        if (context == null) {
            throw new FenixServiceException("error.noContext");
        }

        context.getParentCourseGroup().orderChild(context, position);
    }

}
