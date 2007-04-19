package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.EnrolmentCertificateRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.StringUtils;

public class EnrolmentCertificate extends AdministrativeOfficeDocument {

    protected EnrolmentCertificate(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	super.fillReport();
	
	final EnrolmentCertificateRequest enrolmentCertificateRequest = (EnrolmentCertificateRequest) getDocumentRequest();
	final Registration registration = enrolmentCertificateRequest.getRegistration();
	final ExecutionYear executionYear = enrolmentCertificateRequest.getExecutionYear();
	
	final Integer curricularYear = Integer.valueOf(registration.getCurricularYear(executionYear));
	parameters.put("curricularYear", curricularYear);

	final List<Enrolment> enrolments = new ArrayList<Enrolment>(enrolmentCertificateRequest.getEnrolmentsToDisplay());
	parameters.put("numberEnrolments", Integer.valueOf(enrolments.size()));
	
	StringBuilder enrolmentsInfo = new StringBuilder();
	if (enrolmentCertificateRequest.getDetailed()) {
	    Collections.sort(enrolments, Enrolment.COMPARATOR_BY_EXECUTION_YEAR_AND_NAME_AND_ID);
	    
	    for (final Enrolment enrolment : enrolments) {
		enrolmentsInfo.append(StringUtils.multipleLineRightPad(
			enrolment.getName().getContent().toUpperCase(),
			LINE_LENGTH, 
			'-')).append("\n");
	    }
	    
	    enrolmentsInfo.append(StringUtils.multipleLineRightPad(
		    org.apache.commons.lang.StringUtils.EMPTY,
		    LINE_LENGTH, 
		    '-')).append("\n");
	} 
	parameters.put("enrolmentsInfo", enrolmentsInfo.toString());
    }

}
