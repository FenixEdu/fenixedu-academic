package ServidorApresentacao.Action.sop;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoLesson;
import DataBeans.KeyLesson;
import DataBeans.RoomKey;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author tfc130
 */
public class ApagarAulaFormAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession sessao = request.getSession(false);
		if (sessao != null) {
			DynaActionForm manipularAulasForm =
				(DynaActionForm) request.getAttribute("manipularAulasForm");

			IUserView userView = (IUserView) sessao.getAttribute("UserView");
			GestorServicos gestor = GestorServicos.manager();
			Integer indexAula = (Integer) manipularAulasForm.get("indexAula");
			ArrayList infoAulas = (ArrayList) request.getAttribute("listaAulas");
			InfoLesson infoAula =
				(InfoLesson) infoAulas.get(indexAula.intValue());

			manipularAulasForm.set("indexAula", null);
			//sessao.removeAttribute("indexAula");

			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) request.getAttribute(
					SessionConstants.INFO_EXECUTION_PERIOD_KEY);

			Object argsApagarAula[] =
				{
					new KeyLesson(
						infoAula.getDiaSemana(),
						infoAula.getInicio(),
						infoAula.getFim(),
						new RoomKey(infoAula.getInfoSala().getNome())),
					infoExecutionPeriod };
			Boolean result =
				(Boolean) gestor.executar(
					userView,
					"ApagarAula",
					argsApagarAula);

			if (result != null && result.booleanValue()) {
				infoAulas.remove(indexAula.intValue());
				//sessao.removeAttribute("listaAulas");
				if (!infoAulas.isEmpty())
					request.setAttribute("listaAulas", infoAulas);
			}

			return mapping.findForward("Sucesso");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
}
