/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DocumentRequestTypeProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	AdministrativeOfficeType administrativeOfficeType = AccessControl.getPerson().getEmployee()
		.getAdministrativeOffice().getAdministrativeOfficeType();

	final Collection<DocumentRequestType> result = new ArrayList<DocumentRequestType>();

	for (final DocumentRequestType documentRequestType : DocumentRequestType.values()) {
	    if (documentRequestType.getAdministrativeOfficeTypes().contains(administrativeOfficeType)
		    && !documentRequestType.isAllowedToQuickDeliver()) {
		result.add(documentRequestType);
	    }
	}
	
	return result;
    }

    public Converter getConverter() {
	return new EnumConverter();
    }

}
