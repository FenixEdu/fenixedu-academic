package ServidorApresentacao.Action.coordinator;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoCoordinator;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * 
 * @author João Mota
 * 
 */
public class CoordinationTeamDispatchAction extends FenixDispatchAction {

	public ActionForward viewTeam(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String infoExecutionDegreeIdString =
			(String) request.getParameter("infoExecutionDegreeId");
		Integer infoExecutionDegreeId =
			new Integer(infoExecutionDegreeIdString);
		Object[] args = { infoExecutionDegreeId };
		List coordinators = new ArrayList();
		try {
			coordinators =
				(List) ServiceUtils.executeService(
					userView,
					"ReadCoordinationTeam",
					args);

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		InfoCoordinator coordinator = null;
		try {
			coordinator =
				(InfoCoordinator) ServiceUtils.executeService(
					userView,
					"ReadResponsibleCoordinator",
					args);

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		
		request.setAttribute("responsibleCoordinator",coordinator);
		request.setAttribute("coordinators", coordinators);
		return mapping.findForward("sucess");
	}

}
