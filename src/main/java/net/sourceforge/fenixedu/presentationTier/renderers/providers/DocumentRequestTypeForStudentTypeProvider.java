/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

/**
 * @author - Angela
 * 
 */
public class DocumentRequestTypeForStudentTypeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final Collection<DocumentRequestType> result = new ArrayList<DocumentRequestType>();
        for (DocumentRequestType request : DocumentRequestType.values()) {
            if (request.isStudentRequestable()) {
                result.add(request);
            }
        }

        return result;
    }

    @Override
    public Converter getConverter() {
        return new EnumConverter();
    }

}
