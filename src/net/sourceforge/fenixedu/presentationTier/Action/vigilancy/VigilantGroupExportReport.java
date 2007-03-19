package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.WrittenEvaluationVigilancyView;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class VigilantGroupExportReport extends VigilantGroupManagement {

	public ActionForward exportGroupReportAsXLS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		String vigilantGroupId = request.getParameter("oid");
		VigilantGroup group = (VigilantGroup) RootDomainObject.readDomainObjectByOID(VigilantGroup.class,
				Integer.valueOf(vigilantGroupId));

		response.setContentType("text/plain");
        response.setHeader("Content-disposition", "attachment; filename=\"" + group.getName() + ".xls\"");
        
		List<WrittenEvaluationVigilancyView> beans = getStatsViewForVigilantGroup(group);
		Boolean withNames = Boolean.valueOf(request.getParameter("showVigilants"));
		final Spreadsheet spreadsheet = getSpreadsheet(withNames);
		
		for (final WrittenEvaluationVigilancyView view :  getStatsViewForVigilantGroup(group)) {
			
			WrittenEvaluation evaluation = view.getWrittenEvaluation();
			
			final Row row = spreadsheet.addRow();
		
			row.setCell(evaluation.getDayDateYearMonthDay() + " " + evaluation.getBeginningDateHourMinuteSecond() + " " + evaluation.getName());
			row.setCell(String.valueOf(view.getTotalVigilancies()));
			row.setCell(String.valueOf(view.getVigilanciesFromTeachers()));
			if(withNames) {
				row.setCell(getVigilantListAsString(view.getTeachersVigilants()));
			}
			row.setCell(String.valueOf(view.getVigilanciesFromOthers()));
			if(withNames) {
				row.setCell(getVigilantListAsString(view.getOtherVigilants()));
			}
			row.setCell(String.valueOf(view.getNumberOfCancelledConvokes()));
			if(withNames) {
				row.setCell(getVigilantListAsString(view.getCancelledConvokes()));
			}
			row.setCell(String.valueOf(view.getNumberOfConfirmedConvokes()));
			if(withNames) {
				row.setCell(getVigilantListAsString(view.getConfirmedConvokes()));
			}
			row.setCell(String.valueOf(view.getNumberOfAttendedConvokes()));
			if(withNames) {
				row.setCell(getVigilantListAsString(view.getAttendedConvokes()));
			}
		}

		final OutputStream outputStream = response.getOutputStream();
		spreadsheet.exportToXLSSheet(outputStream);
		outputStream.close();
		return null;
	}

	private String getVigilantListAsString(List<Vigilant> vigilants) {
		String vigilantList = "";
		for(Vigilant vigilant : vigilants) {
			vigilantList += vigilant.getPerson().getFirstAndLastName() + " (" + vigilant.getPerson().getIstUsername() + ")\n";
		}
		return vigilantList;
	}

	private Spreadsheet getSpreadsheet(Boolean withNames) {
		final ResourceBundle enumResourceBundle = ResourceBundle.getBundle("resources.VigilancyResources",
				new Locale("pt", "PT"));
		final Spreadsheet spreadsheet = new Spreadsheet("Report");
		spreadsheet.setHeader(enumResourceBundle.getString("label.vigilancy.writtenEvaluation.header"));
		spreadsheet.setHeader(enumResourceBundle.getString("label.totalVigilancies"));
		spreadsheet.setHeader(enumResourceBundle.getString("label.vigilanciesFromTeachers"));
		if (withNames) {
			spreadsheet.setHeader(enumResourceBundle.getString("label.teachersVigilants"));
		}
		spreadsheet.setHeader(enumResourceBundle.getString("label.vigilanciesFromOthers"));
		if (withNames) {
			spreadsheet.setHeader(enumResourceBundle.getString("label.otherVigilants"));
		}
		spreadsheet.setHeader(enumResourceBundle.getString("label.numberOfCancelledConvokes"));
		if (withNames) {
			spreadsheet.setHeader(enumResourceBundle.getString("label.cancelledConvokes"));
		}
		spreadsheet.setHeader(enumResourceBundle.getString("label.numberOfConfirmedConvokes"));
		if (withNames) {
			spreadsheet.setHeader(enumResourceBundle.getString("label.confirmedConvokes"));
		}
		spreadsheet.setHeader(enumResourceBundle.getString("label.numberOfAttendedConvokes"));
		if (withNames) {
			spreadsheet.setHeader(enumResourceBundle.getString("label.attendedConvokes"));
		}

		return spreadsheet;
	}
}
