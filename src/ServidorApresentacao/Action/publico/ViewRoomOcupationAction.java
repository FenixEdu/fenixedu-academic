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
import ServidorApresentacao.Action.FenixAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author tfc130
 */
public class ViewRoomOcupationAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		DynaActionForm indexForm = (DynaActionForm) form;

		HttpSession session = request.getSession(false);
		if (session != null) {

			InfoRoom infoRoom =
				(InfoRoom) session.getAttribute("publico.infoRoom");

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