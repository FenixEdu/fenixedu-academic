package ServidorApresentacao.Action.commons.student;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class CurriculumDispatchAction extends DispatchAction {

	public ActionForward getCurriculum(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();

		GestorServicos serviceManager = GestorServicos.manager();
		
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		

		Integer studentID = Integer.valueOf(request.getParameter("studentID"));

		List result = null;
		Object args[] = { studentID };
		
		try {
			result = (ArrayList) serviceManager.executar(userView, "ReadStudentCurriculum", args);
		} catch (ExistingServiceException e) {
			throw new ExistingActionException(e);
		}

		request.setAttribute(SessionConstants.CURRICULUM, result);

		return mapping.findForward("PrepareSuccess");
	}

}