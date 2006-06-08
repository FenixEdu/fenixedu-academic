package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.gradeSubmission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.UnableToPrintServiceException;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.util.FenixDigestUtils;
import net.sourceforge.fenixedu.util.ReportsUtils;


public class PrintMarkSheets extends Service{
	
	public void run(Collection<MarkSheet> markSheets, String printerName) throws FenixServiceException {
		for (MarkSheet markSheet : markSheets) {
			printMarkSheet(markSheet, printerName);
		}
	}
	
	
	private void printMarkSheet(MarkSheet markSheet, String printerName) throws FenixServiceException {
		Map parameters = new HashMap();
		parameters.put("markSheet", markSheet);
		parameters.put("checkSum", FenixDigestUtils.getPrettyCheckSum(markSheet.getCheckSum()));
		ResourceBundle bundle = ResourceBundle.getBundle("resources.ReportsResources");
		List<EnrolmentEvaluation> evaluations = new ArrayList<EnrolmentEvaluation>(markSheet.getEnrolmentEvaluations());
		Collections.sort(evaluations, EnrolmentEvaluation.SORT_BY_STUDENT_NUMBER);

		boolean result = ReportsUtils.printReport("markSheet", parameters, bundle, evaluations, printerName);
		if(!result) {
			throw new UnableToPrintServiceException("error.print.failed");
		}
		markSheet.setPrinted(Boolean.TRUE);
	}

}
