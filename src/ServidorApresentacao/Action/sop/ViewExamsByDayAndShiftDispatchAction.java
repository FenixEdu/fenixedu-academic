/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 2003/03/21
 *
 */
package ServidorApresentacao.Action.sop;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ViewExamsByDayAndShiftDispatchAction extends DispatchAction {

	public ActionForward view(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);

		HttpSession session = request.getSession(false);
		IUserView userView = SessionUtils.getUserView(request);

		Calendar examDateAndTime =
			(Calendar) session.getAttribute("examDateAndTime");

		Object args[] = { examDateAndTime.getTime(), examDateAndTime };
		List infoExams =
			(List) ServiceUtils.executeService(
				userView,
				"ReadExamsByDayAndBeginning",
				args);

		if (infoExams != null && infoExams.size() == 0)
			infoExams = null;

		session.removeAttribute("infoExams");
		session.setAttribute("infoExams", infoExams);

		return mapping.findForward("View Exams");
	}

}