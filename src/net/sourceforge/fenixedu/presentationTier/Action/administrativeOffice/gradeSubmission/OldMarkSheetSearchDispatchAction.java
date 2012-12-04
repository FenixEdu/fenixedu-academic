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
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/oldMarkSheetManagement", module = "academicAdminOffice", formBean = "markSheetManagementForm", input = "/academicAdminOffice/gradeSubmission/oldMarkSheets/markSheetManagement.jsp")
@Forwards({
	@Forward(name = "searchMarkSheet", path = "/academicAdminOffice/gradeSubmission/oldMarkSheets/markSheetManagement.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.marksheets.oldmanagementguidelines")),
	@Forward(name = "viewMarkSheet", path = "/academicAdminOffice/gradeSubmission/oldMarkSheets/viewMarkSheet.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.marksheets.oldmanagementguidelines")),
	@Forward(name = "removeMarkSheet", path = "/academicAdminOffice/gradeSubmission/oldMarkSheets/removeMarkSheet.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.marksheets.oldmanagementguidelines")),
	@Forward(name = "searchMarkSheetFilled", path = "/oldMarkSheetManagement.do?method=prepareSearchMarkSheetFilled", tileProperties = @Tile(title = "private.academicadministrativeoffice.marksheets.oldmanagementguidelines")),
	@Forward(name = "confirmMarkSheet", path = "/academicAdminOffice/gradeSubmission/oldMarkSheets/confirmMarkSheet.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.marksheets.oldmanagementguidelines")),
	@Forward(name = "choosePrinter", path = "/printMarkSheet.do?method=choosePrinterMarkSheet", tileProperties = @Tile(title = "private.academicadministrativeoffice.marksheets.oldmanagementguidelines")) })
public class OldMarkSheetSearchDispatchAction extends MarkSheetSearchDispatchAction {

    @Override
    public ActionForward prepareSearchMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	MarkSheetManagementSearchBean markSheetManagementSearchBean = new MarkSheetManagementSearchBean();
	request.setAttribute("edit", markSheetManagementSearchBean);

	return mapping.findForward("searchMarkSheet");
    }

}
