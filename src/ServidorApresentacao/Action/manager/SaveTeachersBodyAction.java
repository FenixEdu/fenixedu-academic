/*
 * Created on 23/Set/2003
 */
package ServidorApresentacao.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author lmac1
 */

public class SaveTeachersBodyAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		IUserView userView = SessionUtils.getUserView(request);
		Integer executionCourseId = new Integer(request.getParameter("executionCourseId"));
		DynaActionForm actionForm = (DynaActionForm) form;
		
		Integer[] responsibleTeachersIds = (Integer[]) actionForm.get("responsibleTeachersIds");
		Integer[] professorShipTeachersIds = (Integer[]) actionForm.get("professorShipTeachersIds");
		Object args[] = { responsibleTeachersIds, professorShipTeachersIds, executionCourseId };

		try {
				ServiceUtils.executeService(userView, "SaveTeachersBody", args);
		} catch (ExistingServiceException e) {
			throw new ExistingActionException(e.getMessage());
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		
		return mapping.findForward("readCurricularCourse");
	}
}