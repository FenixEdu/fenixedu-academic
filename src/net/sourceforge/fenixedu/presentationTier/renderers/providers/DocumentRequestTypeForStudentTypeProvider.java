/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.spi.RegisterableService;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentOptionalEnrolmentBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
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

        Collection<DocumentRequestType> result = new ArrayList<DocumentRequestType>();
        if (degreeType.getAdministrativeOfficeType().equals(
                DocumentRequestType.SCHOOL_REGISTRATION_CERTIFICATE.getAdministrativeOfficeType())) {
            result.add(DocumentRequestType.SCHOOL_REGISTRATION_CERTIFICATE);
        }
        if (degreeType.getAdministrativeOfficeType().equals(
                DocumentRequestType.ENROLMENT_CERTIFICATE.getAdministrativeOfficeType())) {
            result.add(DocumentRequestType.ENROLMENT_CERTIFICATE);
        }
        if (degreeType.getAdministrativeOfficeType().equals(
                DocumentRequestType.SCHOOL_REGISTRATION_DECLARATION.getAdministrativeOfficeType())) {
            result.add(DocumentRequestType.SCHOOL_REGISTRATION_DECLARATION);
        }
        if (degreeType.getAdministrativeOfficeType().equals(
                DocumentRequestType.ENROLMENT_DECLARATION.getAdministrativeOfficeType())) {
            result.add(DocumentRequestType.ENROLMENT_DECLARATION);
        }

        return result;
    }

    public Converter getConverter() {
        return new EnumConverter();
    }

}
