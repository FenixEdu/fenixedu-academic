package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.gradeSubmission;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementSearchBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		path = "/oldMarkSheetManagement",
		module = "academicAdministration",
		formBean = "markSheetManagementForm",
		input = "/academicAdminOffice/gradeSubmission/oldMarkSheets/markSheetManagement.jsp")
@Forwards({
		@Forward(name = "searchMarkSheet", path = "/academicAdminOffice/gradeSubmission/oldMarkSheets/markSheetManagement.jsp"),
		@Forward(name = "viewMarkSheet", path = "/academicAdminOffice/gradeSubmission/oldMarkSheets/viewMarkSheet.jsp"),
		@Forward(name = "removeMarkSheet", path = "/academicAdminOffice/gradeSubmission/oldMarkSheets/removeMarkSheet.jsp"),
		@Forward(name = "searchMarkSheetFilled", path = "/oldMarkSheetManagement.do?method=prepareSearchMarkSheetFilled"),
		@Forward(name = "confirmMarkSheet", path = "/academicAdminOffice/gradeSubmission/oldMarkSheets/confirmMarkSheet.jsp"),
		@Forward(name = "choosePrinter", path = "/printMarkSheet.do?method=choosePrinterMarkSheet") })
public class OldMarkSheetSearchDispatchAction extends MarkSheetSearchDispatchAction {

	@Override
	public ActionForward prepareSearchMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		MarkSheetManagementSearchBean markSheetManagementSearchBean = new MarkSheetManagementSearchBean();
		request.setAttribute("edit", markSheetManagementSearchBean);

		return mapping.findForward("searchMarkSheet");
	}
}
