/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

public class DocumentRequestQuickTypeProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	AdministrativeOfficeType administrativeOfficeType = AccessControl.getPerson().getEmployee()
		.getAdministrativeOffice().getAdministrativeOfficeType();

	Collection<DocumentRequestType> result = new ArrayList<DocumentRequestType>();
	for (final DocumentRequestType documentRequestType : DocumentRequestType.values()) {
	    if (administrativeOfficeType.equals(documentRequestType.getAdministrativeOfficeType()) && documentRequestType.isAllowedToQuickDeliver()) {
		result.add(documentRequestType);
	    }
	}
	
	return result;
    }

    public Converter getConverter() {
	return new EnumConverter();
    }

}
