package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import DataBeans.InfoLessonServiceResult;
import DataBeans.InfoRoom;
import DataBeans.KeyLesson;
import DataBeans.RoomKey;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.DiaSemana;
import Util.TipoAula;

/**
 * @author tfc130
 */
public class EditarAulaFormAction extends FenixAction {

	public static String INVALID_TIME_INTERVAL =
		"errors.lesson.invalid.time.interval";
	public static String UNKNOWN_ERROR = "errors.unknown";

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		DynaActionForm editarAulaForm = (DynaActionForm) form;

		HttpSession sessao = request.getSession(false);
		if (sessao != null) {

			

			try {
				IUserView userView = (IUserView) sessao.getAttribute(SessionConstants.U_VIEW);
				GestorServicos gestor = GestorServicos.manager();
				InfoLesson iAulaAntiga =
					(InfoLesson) sessao.getAttribute("infoAula");
				
				Calendar inicio = Calendar.getInstance();
				inicio.set(
					Calendar.HOUR_OF_DAY,
					Integer.parseInt((String)editarAulaForm.get("horaInicio")));
				inicio.set(
					Calendar.MINUTE,
					Integer.parseInt((String)editarAulaForm.get("minutosInicio")));
				inicio.set(Calendar.SECOND, 0);
				Calendar fim = Calendar.getInstance();
				fim.set(
					Calendar.HOUR_OF_DAY,
					Integer.parseInt((String)editarAulaForm.get("horaFim")));
				fim.set(
					Calendar.MINUTE,
					Integer.parseInt((String)editarAulaForm.get("minutosFim")));
				fim.set(Calendar.SECOND, 0);
				
				InfoRoom infoSala = new InfoRoom();
				infoSala.setNome((String) editarAulaForm.get("nomeSala"));
				
				RoomKey kSalaAntiga =
					new RoomKey(iAulaAntiga.getInfoSala().getNome());
				
				KeyLesson kAulaAntiga =
					new KeyLesson(
						iAulaAntiga.getDiaSemana(),
						iAulaAntiga.getInicio(),
						iAulaAntiga.getFim(),
						kSalaAntiga);
				InfoLesson iAula =
					new InfoLesson(
						new DiaSemana(new Integer((String) editarAulaForm.get("diaSemana"))),
						inicio,
						fim,
						new TipoAula(new Integer((String) editarAulaForm.get("tipoAula"))),
						infoSala,
						iAulaAntiga.getInfoDisciplinaExecucao());
				
				Object argsEditarAula[] = { kAulaAntiga, iAula };
				
				InfoLessonServiceResult result =
					(InfoLessonServiceResult) gestor.executar(
						userView,
						"EditarAula",
						argsEditarAula);
				
				InfoExecutionCourse iDE =
					(InfoExecutionCourse) sessao.getAttribute(
						"infoDisciplinaExecucao");
				Object argsLerAulas[] = new Object[1];
				argsLerAulas[0] = iDE;
				ArrayList infoAulas =
					(ArrayList) gestor.executar(
						userView,
						"LerAulasDeDisciplinaExecucao",
						argsLerAulas);
				
				sessao.removeAttribute("listaAulas");
				if (infoAulas != null && !infoAulas.isEmpty())
					sessao.setAttribute("listaAulas", infoAulas);
				
				
				sessao.removeAttribute("indexAula");
				
				ActionErrors actionErrors = getActionErrors(result, inicio, fim);
				
				if (actionErrors.isEmpty()) {
					sessao.removeAttribute("infoAula");
					return mapping.findForward("Sucesso");
				} else {
					
					saveErrors(request, actionErrors);
					return mapping.getInputForward();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}

		} else
		
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}

	private ActionErrors getActionErrors(
		InfoLessonServiceResult result,
		Calendar inicio,
		Calendar fim) {
		ActionErrors actionErrors = new ActionErrors();
		String beginMinAppend = "";
		String endMinAppend = "";

		if (inicio.get(Calendar.MINUTE) == 0)
			beginMinAppend = "0";
		if (fim.get(Calendar.MINUTE) == 0)
				endMinAppend = "0";


		switch (result.getMessageType()) {
			case InfoLessonServiceResult.SUCESS :
				break;
			case InfoLessonServiceResult.INVALID_TIME_INTERVAL :
				actionErrors.add(
					INVALID_TIME_INTERVAL,
					new ActionError(
						INVALID_TIME_INTERVAL,
						""
							+ inicio.get(Calendar.HOUR_OF_DAY)
							+ ":"
							+ inicio.get(Calendar.MINUTE)
							+ beginMinAppend
							+ " - "
							+ fim.get(Calendar.HOUR_OF_DAY)
							+ ":"
							+ fim.get(Calendar.MINUTE)
							+ endMinAppend));
				break;
			default :
				actionErrors.add(UNKNOWN_ERROR, new ActionError(UNKNOWN_ERROR));
				break;
		}
		return actionErrors;
	}

}
