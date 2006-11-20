package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.EnrolmentCertificateRequest;
import net.sourceforge.fenixedu.util.StringUtils;

public class EnrolmentCertificate extends AdministrativeOfficeDocument {

    public EnrolmentCertificate(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	super.fillReport();
	
	final EnrolmentCertificateRequest enrolmentCertificateRequest = (EnrolmentCertificateRequest) getDocumentRequest();
	final ExecutionYear executionYear = enrolmentCertificateRequest.getExecutionYear();

	final Integer curricularYear = Integer.valueOf(enrolmentCertificateRequest.getRegistration()
		.getCurricularYear(executionYear));
	parameters.put("curricularYear", curricularYear);

	dataSource = new ArrayList();
	if (enrolmentCertificateRequest.getDetailed()) {
	    GenericPair<String, String> dummy = new GenericPair<String, String>("\t\t" + curricularYear
		    + ".ANO", null);
	    dataSource.add(dummy);

	    final List<Enrolment> enrolments = enrolmentCertificateRequest.getRegistration()
		    .getStudentCurricularPlan(executionYear).getEnrolmentsByExecutionYear(executionYear);
	    for (final Enrolment enrolment : enrolments) {
		dummy = new GenericPair<String, String>(StringUtils.multipleLineRightPad(64, enrolment
			.getName().toUpperCase(), '-'), null);
		dataSource.add(dummy);
	    }
	    
	    parameters.put("numberEnrolments", Integer.valueOf(enrolments.size()));
	} else {
	    GenericPair<String, String> dummy = new GenericPair<String, String>(
		    org.apache.commons.lang.StringUtils.EMPTY, null);
	    dataSource.add(dummy);
	}
    }

}
