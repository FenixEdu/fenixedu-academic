package ServidorApresentacao.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoom;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author tfc130
 */
public class ViewRoomFormAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		SessionUtils.validSessionVerification(request, mapping);
		
		DynaActionForm indexForm = (DynaActionForm) form;

		HttpSession session = request.getSession(false);
		session.removeAttribute(SessionConstants.INFO_SECTION);
		if (session != null) {
			List infoRooms = (List) session.getAttribute("publico.infoRooms");
			InfoRoom infoRoom =	(InfoRoom) infoRooms.get(((Integer) indexForm.get("index")).intValue());
			session.removeAttribute("publico.infoRoom");
			session.setAttribute("publico.infoRoom", infoRoom);


			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) session.getAttribute(
					SessionConstants.INFO_EXECUTION_PERIOD_KEY);

			Object argsReadLessons[] = { infoExecutionPeriod, infoRoom };

			List lessons =
				(List) ServiceUtils.executeService(
					null,
					"LerAulasDeSalaEmSemestre",
					argsReadLessons);
			
			if (lessons != null) {
				session.setAttribute(SessionConstants.LESSON_LIST_ATT, lessons);
			}
			  
			return mapping.findForward("Sucess");
		} else
			throw new Exception(); 
		}
}