package ServidorApresentacao.Action.sop;

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
import DataBeans.InfoRoomExamsMap;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ViewRoomForExamsFormAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession();
		DynaActionForm indexForm = (DynaActionForm) form;
		request.removeAttribute(SessionConstants.INFO_SECTION);
		if (session != null) {
			IUserView userView = (IUserView) session.getAttribute("UserView");
			GestorServicos gestor = GestorServicos.manager();
			
			List infoRooms = (List) request.getAttribute(SessionConstants.SELECTED_ROOMS);
			InfoRoom infoRoom =
				(InfoRoom) infoRooms.get(
					((Integer) indexForm.get("index")).intValue());
			request.removeAttribute("publico.infoRoom");
			request.setAttribute("publico.infoRoom", infoRoom);

			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) this
					.servlet
					.getServletContext()
					.getAttribute(
					SessionConstants.INFO_EXECUTION_PERIOD_KEY);

			Object argsReadLessons[] = { infoExecutionPeriod, infoRoom };

			try {
				List lessons;
				lessons =
					(List) ServiceUtils.executeService(
						null,
						"LerAulasDeSalaEmSemestre",
						argsReadLessons);

				if (lessons != null) {
					request.setAttribute(
						SessionConstants.LESSON_LIST_ATT,
						lessons);
				}

			} catch (FenixServiceException e) {
				throw new FenixActionException();
			}

			Object[] args =
				{ infoRoom, infoExecutionPeriod };
			InfoRoomExamsMap infoExamsMap;
			try {
				infoExamsMap =
					(InfoRoomExamsMap) gestor.executar(
						userView,
						"ReadRoomExamsMap",
						args);
			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException(e);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
			request.setAttribute(SessionConstants.INFO_EXAMS_MAP, infoExamsMap);

			return mapping.findForward("Sucess");
		} else {
			throw new FenixActionException();
		}

	}
}