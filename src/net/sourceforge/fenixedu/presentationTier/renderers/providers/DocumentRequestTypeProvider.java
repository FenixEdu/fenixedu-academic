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

    static public class PreBolonhaTypes extends DocumentRequestTypeProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
	    return super.provide(source, currentValue, true);
	}
    }

    public Object provide(Object source, Object currentValue) {
	return provide(source, currentValue, false);
    }

    public Object provide(Object source, Object currentValue, boolean includePreBolonhaTypes) {

	AdministrativeOfficeType administrativeOfficeType = AccessControl.getPerson().getEmployee().getAdministrativeOffice()
		.getAdministrativeOfficeType();

	final Collection<DocumentRequestType> result = new ArrayList<DocumentRequestType>();

	for (final DocumentRequestType documentRequestType : DocumentRequestType.values()) {
	    if (documentRequestType.getAdministrativeOfficeTypes().contains(administrativeOfficeType)
		    && !documentRequestType.isAllowedToQuickDeliver()
		    && (includePreBolonhaTypes || !documentRequestType.isPreBolonha())) {
		result.add(documentRequestType);
	    }
	}

	return result;
    }

    public Converter getConverter() {
	return new EnumConverter();
    }

}
