package ServidorApresentacao.Action.masterDegree.administrativeOffice.thesis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import ServidorApresentacao.Action.exceptions.NonExistingActionException;

/**
 * 
 * @author :
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */

public class CreateMasterDegreeThesisDispatchAction extends DispatchAction {

	public ActionForward getStudentForCreateMasterDegreeThesis(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		Integer degreeType = (Integer) request.getAttribute("degreeType");
		Integer studentNumber = (Integer) request.getAttribute("studentNumber");

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();
		boolean isSuccess = operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

		if (isSuccess) {
			return mapping.findForward("start");
		} else {
			throw new NonExistingActionException("error.exception.masterDegree.nonExistentStudent", mapping.findForward("error"));

		}

	}

}