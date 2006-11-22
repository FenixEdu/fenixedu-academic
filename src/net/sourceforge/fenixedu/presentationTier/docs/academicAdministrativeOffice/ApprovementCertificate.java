package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ApprovementCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.util.StringUtils;

public class ApprovementCertificate extends AdministrativeOfficeDocument {

    protected ApprovementCertificate(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	super.fillReport();
	
	final ApprovementCertificateRequest approvementCertificateRequest = (ApprovementCertificateRequest) getDocumentRequest();

	final List<Enrolment> approvedEnrolments = approvementCertificateRequest.getRegistration().getLastStudentCurricularPlanExceptPast().getAprovedEnrolments();
	parameters.put("numberApprovements", Integer.valueOf(approvedEnrolments.size()));
	
	final ComparatorChain comparatorChain = new ComparatorChain();
	comparatorChain.addComparator(new BeanComparator("executionPeriod.executionYear"));
	comparatorChain.addComparator(new BeanComparator("name"));
	Collections.sort(approvedEnrolments, comparatorChain);
	
	GenericPair<String,String> dummy;
	for (final Enrolment approvedEnrolment : approvedEnrolments) {
	    dummy = new GenericPair<String, String>(StringUtils.multipleLineRightPad(LINE_LENGTH, approvedEnrolment
		    .getName().toUpperCase(), '-'), null);
	    dataSource.add(dummy);
	}
    }

}
