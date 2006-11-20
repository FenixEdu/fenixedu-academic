/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DocumentRequestTypeProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

        Collection<DocumentRequestType> result = new ArrayList<DocumentRequestType>();
        result.add(DocumentRequestType.SCHOOL_REGISTRATION_CERTIFICATE);
        result.add(DocumentRequestType.ENROLMENT_CERTIFICATE);

        return result;
    }

    public Converter getConverter() {
        return new EnumConverter();
    }

}
