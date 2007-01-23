package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.gradeSubmission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.UnableToPrintServiceException;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.util.FenixDigestUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.ReportsUtils;

public abstract class AbstractPrintMarkSheet extends Service {

    protected void print(MarkSheet markSheet, String printerName) throws FenixServiceException {
	if (markSheet == null) {
	    throw new InvalidArgumentsServiceException("mark sheet cannot be null");
	}

	if (!markSheet.isRectification()) {
	    printMarkSheet(markSheet, printerName);
	} else {
	    printRectificationMarkSheet(markSheet, printerName);
	}

	if (!markSheet.getPrinted()) {
	    markSheet.setPrinted(Boolean.TRUE);

	    /*if(markSheet.getResponsibleTeacher().getPerson().getEmail() != null) {
	     EmailSender.send(null, "from", Collections.singletonList(markSheet.getResponsibleTeacher().getPerson().getEmail()), 
	     null, null, "subject", "message");
	     }*/
	}
    }

    private void printRectificationMarkSheet(MarkSheet markSheet, String printerName) throws UnableToPrintServiceException {
	final EnrolmentEvaluation rectification =  markSheet.getEnrolmentEvaluationsSet().iterator().next();
	
	Map parameters = new HashMap();
	parameters.put("markSheet", markSheet);
	parameters.put("checkSum", FenixDigestUtils.getPrettyCheckSum(markSheet.getCheckSum()));
	parameters.put("rectification", rectification);
	parameters.put("rectified", rectification.getRectified());
	ResourceBundle bundle = ResourceBundle.getBundle("resources.ReportsResources", LanguageUtils
		.getLocale());
	
	boolean result = ReportsUtils.printReport("markSheetRectification", parameters, bundle, Collections.emptyList(),
		printerName);
	if (!result) {
	    throw new UnableToPrintServiceException("error.print.failed");
	}
    }

    private void printMarkSheet(MarkSheet markSheet, String printerName) throws UnableToPrintServiceException {
	Map parameters = new HashMap();
	parameters.put("markSheet", markSheet);
	parameters.put("checkSum", FenixDigestUtils.getPrettyCheckSum(markSheet.getCheckSum()));
	ResourceBundle bundle = ResourceBundle.getBundle("resources.ReportsResources", LanguageUtils
		.getLocale());
	List<EnrolmentEvaluation> evaluations = new ArrayList<EnrolmentEvaluation>(markSheet
		.getEnrolmentEvaluations());
	Collections.sort(evaluations, EnrolmentEvaluation.SORT_BY_STUDENT_NUMBER);

	boolean result = ReportsUtils.printReport("markSheet", parameters, bundle, evaluations,
		printerName);
	if (!result) {
	    throw new UnableToPrintServiceException("error.print.failed");
	}
    }

}
