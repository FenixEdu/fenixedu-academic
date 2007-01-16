package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class DegreeTypeDegrees implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	Person person = AccessControl.getPerson();
	Unit currentWorkingPlace = person.getEmployee().getCurrentWorkingPlace();
	AdministrativeOffice administrativeOffice = currentWorkingPlace.getAdministrativeOffice();

	if (administrativeOffice == null) {
	    for (PersonFunction personFunction : person.getActivePersonFunctions()) {
		if (personFunction.getFunction().getFunctionType().equals(
			FunctionType.ASSIDUOUSNESS_RESPONSIBLE)
			&& personFunction.getUnit().getAdministrativeOffice() != null) {
		    administrativeOffice = personFunction.getUnit().getAdministrativeOffice();
		}
	    }
	}

	return administrativeOffice.getAdministratedDegreesForMarkSheets();

    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
