package ServidorApresentacao.Action.equivalence;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author David Santos
 * 9/Jul/2003
 */

public class PrepareManualEquivalenceForCoordinatorDispatchAction extends DispatchAction {
	
	private final String[] forwards = { "begin", "show" };

	public ActionForward begin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) session.getAttribute(SessionConstants.MASTER_DEGREE);

		List enrolmentsList = null;
		try {
			Object args[] = { userView, infoExecutionDegree };
			enrolmentsList = (List) ServiceUtils.executeService(userView, "GetAllAvailableCurricularCoursesForCoordinatorEquivalence", args);
		} catch(FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute(SessionConstants.ENROLMENT_LIST, enrolmentsList);
		return mapping.findForward(forwards[0]);
	}

	public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer infoStudentOID = new Integer(request.getParameter("studentOID"));

		InfoStudent infoStudent = null;
		try {
			Object args[] = { infoStudentOID };
			infoStudent = (InfoStudent) ServiceUtils.executeService(userView, "GetStudentByOID", args);
		} catch(FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute(SessionConstants.STUDENT, infoStudent);
		return mapping.findForward(forwards[1]);
	}
}