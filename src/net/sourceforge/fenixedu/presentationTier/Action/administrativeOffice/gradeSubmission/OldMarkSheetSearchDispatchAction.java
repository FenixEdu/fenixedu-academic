package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.gradeSubmission;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementSearchBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class OldMarkSheetSearchDispatchAction extends MarkSheetSearchDispatchAction {

    public ActionForward prepareSearchMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	MarkSheetManagementSearchBean markSheetManagementSearchBean = new MarkSheetManagementSearchBean();
	request.setAttribute("edit", markSheetManagementSearchBean);

	return mapping.findForward("searchMarkSheet");
    }

}
