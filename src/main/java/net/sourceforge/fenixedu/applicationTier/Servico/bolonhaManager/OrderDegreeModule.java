package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class OrderDegreeModule {

    @Atomic
    public static void run(final String contextID, final Integer position) throws FenixServiceException {
        if (contextID == null) {
            throw new FenixServiceException();
        }

        final Context context = FenixFramework.getDomainObject(contextID);
        if (context == null) {
            throw new FenixServiceException("error.noContext");
        }

        context.getParentCourseGroup().orderChild(context, position);
    }

}
