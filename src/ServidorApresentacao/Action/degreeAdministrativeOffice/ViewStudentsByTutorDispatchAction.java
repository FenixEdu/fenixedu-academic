package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author joaosa and rmalo
 *  
 */
public class ViewStudentsByTutorDispatchAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		
		Object[] args = {userView.getUtilizador()};
		 List infoStudents = null;
		 
		 infoStudents = (List) ServiceUtils.executeService(userView,"ViewStudentsByTutor",args);
		
		if (infoStudents.size() > 1) {
            //order list by number
            Collections.sort((List) infoStudents, new BeanComparator(
                    "infoStudent.number"));
            request.setAttribute("studentsOfTutor", infoStudents);
        }
		return mapping.findForward("viewStudentsByTutor");
	}
}