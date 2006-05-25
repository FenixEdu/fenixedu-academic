package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.gradeSubmission;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.MarkSheet;

public class PrintMarkSheet extends Service{
	
	public void run(MarkSheet markSheet) throws FenixServiceException {
		
		JRDataSource collectionDataSource = new JRMapCollectionDataSource(markSheet.getEnrolmentEvaluations());
		Map parameters = new HashMap();
		parameters.put("markSheet", markSheet);
		try {
			InputStream resourceAsStream = PrintMarkSheet.class.getResourceAsStream("reports/new_report.jrxml");
			JasperReport report = JasperCompileManager.compileReport(resourceAsStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, collectionDataSource);
			JasperExportManager.exportReportToPdfFile(jasperPrint, "/home/nmgo/test.pdf");
		} catch (JRException e) {
			throw new FenixServiceException(e);
		}
		
	}

}
