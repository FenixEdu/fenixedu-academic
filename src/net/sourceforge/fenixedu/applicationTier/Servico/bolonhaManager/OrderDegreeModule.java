package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;

public class OrderDegreeModule extends FenixService {

	public void run(final Integer contextID, final Integer position) throws FenixServiceException {
		if (contextID == null) {
			throw new FenixServiceException();
		}

		final Context context = rootDomainObject.readContextByOID(contextID);
		if (context == null) {
			throw new FenixServiceException("error.noContext");
		}

		context.getParentCourseGroup().orderChild(context, position);
	}

}
