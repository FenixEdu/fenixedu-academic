
package ServidorApresentacao.Action.masterDegree.coordinator;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class StudentListDispatchAction extends DispatchAction {
	
	
	public ActionForward getList(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);

		List studentList = null;

		if (session != null) {
			
			InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) session.getAttribute(SessionConstants.MASTER_DEGREE);

			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

System.out.println(infoExecutionDegree);

			Object args[] = {infoExecutionDegree};


			try {
				studentList = (ArrayList) serviceManager.executar(userView, "ReadStudentListByDegree", args);
			} catch (ExistingServiceException e) {
				throw new ExistingActionException(e);
			}
			
	
			if ((studentList == null) || (studentList.size() == 0)){
				throw new NonExistingActionException("");
			}


			request.setAttribute(SessionConstants.STUDENT_LIST, studentList);

			return mapping.findForward("ChooseSuccess");
		  } else
			throw new Exception();   
	}

}
