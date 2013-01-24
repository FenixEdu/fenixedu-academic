package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

public class AcademicCurriculumsViewAuthorization extends Filtro {

    public final static AcademicCurriculumsViewAuthorization instance = new AcademicCurriculumsViewAuthorization();

    @Override
    final public void execute(ServiceRequest request, ServiceResponse response) throws FilterException, Exception {
	if (!AcademicPredicates.MANAGE_MARKSHEETS.evaluate(getRemoteUser(request).getPerson())) {
	    throw new NotAuthorizedFilterException();
	}
    }

}
