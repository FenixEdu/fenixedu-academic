package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.EnrolmentCertificateRequest;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class EnrolmentCertificate extends AdministrativeOfficeDocument {

    protected EnrolmentCertificate(final DocumentRequest documentRequest) {
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

	final List<Enrolment> enrolments = enrolmentCertificateRequest.getRegistration().getStudentCurricularPlan(executionYear).getEnrolmentsByExecutionYear(executionYear);
	parameters.put("numberEnrolments", Integer.valueOf(enrolments.size()));
	
	if (enrolmentCertificateRequest.getDetailed()) {
	    GenericPair<String, String> dummy = new GenericPair<String, String>("\t\t" + curricularYear + ".ANO", null);
	    dataSource.add(dummy);

	    final ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(new BeanComparator("executionPeriod.executionYear"));
	    comparatorChain.addComparator(new BeanComparator("name"));
	    Collections.sort(enrolments, comparatorChain);
	    
	    for (final Enrolment enrolment : enrolments) {
		dummy = new GenericPair<String, String>(StringUtils.multipleLineRightPad(LINE_LENGTH, enrolment.getName().getContent(LanguageUtils.getLanguage()).toUpperCase(), '-'), null);
		dataSource.add(dummy);
	    }
	} else {
	    GenericPair<String, String> dummy = new GenericPair<String, String>(org.apache.commons.lang.StringUtils.EMPTY, null);
	    dataSource.add(dummy);
	}
    }

}
