package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ApprovementCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class ApprovementCertificate extends AdministrativeOfficeDocument {
    
    private static final ResourceBundle enumerationBundle = ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale());

    protected ApprovementCertificate(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	super.fillReport();

	final ApprovementCertificateRequest approvementCertificateRequest = (ApprovementCertificateRequest) getDocumentRequest();

	final List<Enrolment> approvedEnrolments = approvementCertificateRequest.getRegistration()
		.getLastStudentCurricularPlanExceptPast().getAprovedEnrolments();
	parameters.put("numberApprovements", Integer.valueOf(approvedEnrolments.size()));

	final ComparatorChain comparatorChain = new ComparatorChain();
	comparatorChain.addComparator(new BeanComparator("executionPeriod.executionYear"));
	comparatorChain.addComparator(new BeanComparator("name"));
	Collections.sort(approvedEnrolments, comparatorChain);

	StringBuilder approvementsInfo = new StringBuilder();
	for (final Enrolment approvedEnrolment : approvedEnrolments) {
	    StringBuilder gradeInfo = new StringBuilder();
	    gradeInfo.append(" ");
	    gradeInfo.append(approvedEnrolment.getLatestEnrolmentEvaluation().getGrade());
	    gradeInfo.append(
		    StringUtils.rightPad(
			    "(" + enumerationBundle.getString(approvedEnrolment.getLatestEnrolmentEvaluation().getGrade()) + ")",
			    11,
			    ' '));
	    gradeInfo.append(" em ").append(approvedEnrolment.getExecutionPeriod().getExecutionYear().getYear());
	    
	    approvementsInfo.append(
		    StringUtils.multipleLineRightPadWithSuffix(
			    approvedEnrolment.getName().getContent(LanguageUtils.getLanguage()).toUpperCase(),
			    LINE_LENGTH, 
			    '-',
			    gradeInfo.toString())).append("\n");
	}

	parameters.put("approvementsInfo", approvementsInfo.toString());
    }

}
