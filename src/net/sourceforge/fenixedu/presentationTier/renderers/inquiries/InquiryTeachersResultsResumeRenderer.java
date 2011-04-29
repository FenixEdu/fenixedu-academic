/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.GroupResultsSummaryBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.QuestionResultsSummaryBean;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell.CellType;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
public class InquiryTeachersResultsResumeRenderer extends OutputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new InquiryGeneralGroupLayout(object);
    }

    private class InquiryGeneralGroupLayout extends Layout {

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());

	public InquiryGeneralGroupLayout(Object object) {
	}

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    GroupResultsSummaryBean groupResultsSummaryBean = (GroupResultsSummaryBean) object;

	    final HtmlTable mainTable = new HtmlTable();
	    for (QuestionResultsSummaryBean summaryBean : groupResultsSummaryBean.getQuestionsResults()) {
		HtmlTableRow row = mainTable.createRow();
		HtmlTableCell resultCell = row.createCell();
		String divText = "<div class=\"bar-" + summaryBean.getResultClassification().toString().toLowerCase()
			+ "\">&nbsp;</div>";
		HtmlText divComponent = new HtmlText(divText);
		divComponent.setEscaped(false);
		resultCell.setBody(divComponent);
		HtmlTableCell labelCell = row.createCell(CellType.HEADER);
		Person person = summaryBean.getQuestionResult().getProfessorship().getPerson();
		String teacherId = "";
		if (person.getTeacher() != null) {
		    teacherId = " (" + person.getIstUsername() + ") - ";
		}
		labelCell.setBody(new HtmlText(bundle.getString(summaryBean.getQuestionResult().getShiftType().name())
			+ person.getName() + teacherId));
	    }
	    return mainTable;
	}

	@Override
	public String getClasses() {
	    return "graph teacher-results";
	}
    }
}
