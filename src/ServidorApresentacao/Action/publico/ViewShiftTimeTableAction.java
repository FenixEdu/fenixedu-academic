/**
 * 
 * Project sop 
 * Package ServidorApresentacao.Action.publico 
 * Created on 1/Fev/2003
 */
package ServidorApresentacao.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import DataBeans.ShiftKey;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author João Mota
 *
 */
public class ViewShiftTimeTableAction extends Action {

	/**
	 * Constructor for ViewClassTimeTableAction.
	 */

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		SessionUtils.validSessionVerification(request, mapping);
						
		String shiftName = request.getParameter("shiftName");

		if (shiftName == null)
			return mapping.getInputForward();
		HttpSession session = request.getSession(false);		
		session.removeAttribute(SessionConstants.INFO_SECTION);
		InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) session.getAttribute(SessionConstants.EXECUTION_COURSE_KEY);

		Object[] args = { new ShiftKey(shiftName, infoExecutionCourse)};
		List lessons =
			(List) ServiceUtils.executeService(null, "LerAulasDeTurno", args);

		InfoShift shiftView = new InfoShift();
		shiftView.setNome(shiftName);
		session.setAttribute(SessionConstants.SHIFT_VIEW,shiftView);
		session.setAttribute(SessionConstants.LESSON_LIST_ATT, lessons);

		return mapping.findForward("Sucess");
	}
}
