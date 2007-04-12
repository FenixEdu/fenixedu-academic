/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

/**
 * @author - Angela
 * 
 */
public class DocumentRequestTypeForStudentTypeProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        DocumentRequestCreateBean documentRequestCreateBean = (DocumentRequestCreateBean) source;

        DegreeType degreeType = documentRequestCreateBean.getRegistration().getDegreeType();

        final Collection<DocumentRequestType> result = new ArrayList<DocumentRequestType>();
        if (DocumentRequestType.SCHOOL_REGISTRATION_CERTIFICATE.getAdministrativeOfficeTypes().contains(degreeType.getAdministrativeOfficeType())){
            result.add(DocumentRequestType.SCHOOL_REGISTRATION_CERTIFICATE);
        }
        if (DocumentRequestType.ENROLMENT_CERTIFICATE.getAdministrativeOfficeTypes().contains(degreeType.getAdministrativeOfficeType())) {
            result.add(DocumentRequestType.ENROLMENT_CERTIFICATE);
        }

        return result;
    }

    public Converter getConverter() {
        return new EnumConverter();
    }

}
