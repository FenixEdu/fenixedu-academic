package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;

import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

public class AssiduousnessResponsibleAuthorizationFilter extends Filtro {

    final public void execute(ServiceRequest request, ServiceResponse response) throws FilterException, Exception {

	Assiduousness assiduousness = (Assiduousness) request.getServiceParameters().parametersArray()[0];
	YearMonthDay beginDate = new YearMonthDay((LocalDate) request.getServiceParameters().parametersArray()[1]);
	YearMonthDay endDate = new YearMonthDay((LocalDate) request.getServiceParameters().parametersArray()[2]);
	IUserView userView = getRemoteUser(request);

	for (PersonFunction personFunction : userView.getPerson().getPersonFuntions(
		AccountabilityTypeEnum.ASSIDUOUSNESS_STRUCTURE, beginDate, endDate)) {
	    if (personFunction.getFunction().getFunctionType() == FunctionType.ASSIDUOUSNESS_RESPONSIBLE
		    && personFunction.getFunction().getUnit().getAllWorkingEmployees(beginDate, endDate).contains(
			    assiduousness.getEmployee())) {
		return;
	    }

	}

	throw new NotAuthorizedFilterException();
    }
}