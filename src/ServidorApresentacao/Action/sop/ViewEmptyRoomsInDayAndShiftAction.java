package ServidorApresentacao.Action.sop;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Luis Cruz e Sara Ribeiro
 */
public class ViewEmptyRoomsInDayAndShiftAction extends Action {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		if (session != null) {
			GestorServicos gestor = GestorServicos.manager();
			IUserView userView = SessionUtils.getUserView(request);

			Calendar examDateAndTime =
							(Calendar) session.getAttribute(
								SessionConstants.EXAM_DATEANDTIME);

						Calendar examDate = Calendar.getInstance();
						Calendar examTime = Calendar.getInstance();

						examDate.set(
							Calendar.DAY_OF_MONTH,
							examDateAndTime.get(Calendar.DAY_OF_MONTH));
						examDate.set(Calendar.MONTH, examDateAndTime.get(Calendar.MONTH));
						examDate.set(Calendar.YEAR, examDateAndTime.get(Calendar.YEAR));
						examDate.set(Calendar.HOUR_OF_DAY, 0);
						examDate.set(Calendar.MINUTE, 0);
						examDate.set(Calendar.SECOND, 0);

						examTime.set(Calendar.DAY_OF_MONTH, 1);
						examTime.set(Calendar.MONTH, 1);
						examTime.set(Calendar.YEAR, 1970);
						examTime.set(
							Calendar.HOUR_OF_DAY,
							examDateAndTime.get(Calendar.HOUR_OF_DAY));
						examTime.set(Calendar.MINUTE, 0);
						examTime.set(Calendar.SECOND, 0);

			// Chamar servico que vai ler salas vazias no dia escolhido

System.out.println("TA Na action....");

			return mapping.findForward("View");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
}
