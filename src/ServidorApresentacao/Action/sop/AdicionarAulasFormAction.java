package ServidorApresentacao.Action.sop;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.InfoShiftServiceResult;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.FenixAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author tfc130
 */
public class AdicionarAulasFormAction extends FenixAction {
	
	public static String THEORETICAL_HOURS_LIMIT_EXCEEDED =
		"errors.shift.theoretical.hours.limit.exceeded";
	public static String PRATICAL_HOURS_LIMIT_EXCEEDED =
		"errors.shift.pratical.hours.limit.exceeded";
	public static String THEO_PRAT_HOURS_LIMIT_EXCEEDED =
		"errors.shift.theo.pratical.hours.limit.exceeded";
	public static String LAB_HOURS_LIMIT_EXCEEDED =
		"errors.shift.lab.hours.limit.exceeded";
	public static String THEORETICAL_HOURS_LIMIT_REACHED =
		"errors.shift.theoretical.hours.limit.reached";
	public static String PRATICAL_HOURS_LIMIT_REACHED =
		"errors.shift.pratical.hours.limit.reached";
	public static String THEO_PRAT_HOURS_LIMIT_REACHED =
		"errors.shift.theo.pratical.hours.limit.reached";
	public static String LAB_HOURS_LIMIT_REACHED =
		"errors.shift.lab.hours.limit.reached";
	public static String UNKNOWN_ERROR = "errors.unknown";
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		SessionUtils.validSessionVerification(request, mapping);
		DynaActionForm adicionarAulasForm = (DynaActionForm) form;

		HttpSession sessao = request.getSession(false);
		if (sessao != null) {
			DynaActionForm manipularTurnosForm =
				(DynaActionForm) sessao.getAttribute("manipularTurnosForm");
			IUserView userView = (IUserView) sessao.getAttribute("UserView");
			GestorServicos gestor = GestorServicos.manager();
			Integer indexTurno =
				(Integer) manipularTurnosForm.get("indexTurno");
			ArrayList infoTurnos =
				(ArrayList) sessao.getAttribute(
					"infoTurnosDeDisciplinaExecucao");
			InfoShift infoTurno =
				(InfoShift) infoTurnos.get(indexTurno.intValue());
			Integer indexAula = (Integer) adicionarAulasForm.get("indexAula");
			ArrayList infoAulas =
				(ArrayList) sessao.getAttribute(
					"infoAulasDeDisciplinaExecucao");
			InfoLesson infoAula =
				(InfoLesson) infoAulas.get(indexAula.intValue());
			Object argsAdicionarAula[] = { infoTurno, infoAula };
			InfoShiftServiceResult result = null;
			try {
				result =
					(InfoShiftServiceResult) ServiceUtils.executeService(
						userView,
						"AdicionarAula",
						argsAdicionarAula);
			} catch (ExistingServiceException ex) {
				throw new ExistingActionException("Essa relação Turno-Aula", ex);
			}
			ActionErrors actionErrors = getActionErrors(result, infoTurno);
			if (actionErrors.isEmpty()) {
				//		if (result.isSUCESS()) {
				return mapping.findForward("Sucesso");
			} else {
				saveErrors(request, actionErrors);
				return mapping.getInputForward();
			}

			//      gestor.executar(userView, "AdicionarAula", argsAdicionarAula);
			//      return mapping.findForward("Sucesso");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}

	private ActionErrors getActionErrors(
		InfoShiftServiceResult result,
		InfoShift infoShift) {
		ActionErrors actionErrors = new ActionErrors();

		switch (result.getMessageType()) {
			case InfoShiftServiceResult.SUCESS :
				break;
			case InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_EXCEEDED :
				actionErrors.add(
					THEORETICAL_HOURS_LIMIT_EXCEEDED,
					new ActionError(
						THEORETICAL_HOURS_LIMIT_EXCEEDED,
						""
							+ infoShift
								.getInfoDisciplinaExecucao()
								.getTheoreticalHours()));
				break;
			case InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_REACHED :
				actionErrors.add(
					THEORETICAL_HOURS_LIMIT_REACHED,
					new ActionError(
						THEORETICAL_HOURS_LIMIT_REACHED,
						""
							+ infoShift
								.getInfoDisciplinaExecucao()
								.getTheoreticalHours()));
				break;
			case InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_EXCEEDED :
				actionErrors.add(
					PRATICAL_HOURS_LIMIT_EXCEEDED,
					new ActionError(
						PRATICAL_HOURS_LIMIT_EXCEEDED,
						""
							+ infoShift
								.getInfoDisciplinaExecucao()
								.getPraticalHours()));
				break;
			case InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_REACHED :
				actionErrors.add(
					PRATICAL_HOURS_LIMIT_REACHED,
					new ActionError(
						PRATICAL_HOURS_LIMIT_REACHED,
						""
							+ infoShift
								.getInfoDisciplinaExecucao()
								.getPraticalHours()));
				break;
			case InfoShiftServiceResult.THEO_PRAT_HOURS_LIMIT_EXCEEDED :
				actionErrors.add(
					THEO_PRAT_HOURS_LIMIT_EXCEEDED,
					new ActionError(
						THEO_PRAT_HOURS_LIMIT_EXCEEDED,
						""
							+ infoShift
								.getInfoDisciplinaExecucao()
								.getTheoPratHours()));
				break;
			case InfoShiftServiceResult.THEO_PRAT_HOURS_LIMIT_REACHED :
				actionErrors.add(
					THEO_PRAT_HOURS_LIMIT_REACHED,
					new ActionError(
						THEO_PRAT_HOURS_LIMIT_REACHED,
						""
							+ infoShift
								.getInfoDisciplinaExecucao()
								.getTheoPratHours()));
				break;
			case InfoShiftServiceResult.LAB_HOURS_LIMIT_EXCEEDED :
				actionErrors.add(
					LAB_HOURS_LIMIT_EXCEEDED,
					new ActionError(
						LAB_HOURS_LIMIT_EXCEEDED,
						""
							+ infoShift
								.getInfoDisciplinaExecucao()
								.getLabHours()));
				break;
			case InfoShiftServiceResult.LAB_HOURS_LIMIT_REACHED :
				actionErrors.add(
					LAB_HOURS_LIMIT_REACHED,
					new ActionError(
						LAB_HOURS_LIMIT_REACHED,
						""
							+ infoShift
								.getInfoDisciplinaExecucao()
								.getLabHours()));
				break;
			default :
				actionErrors.add(UNKNOWN_ERROR, new ActionError(UNKNOWN_ERROR));
				break;
		}
		return actionErrors;
	}

}
