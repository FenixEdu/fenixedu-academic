package ServidorApresentacao.Action.sop;

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
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.sop.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.sop.exceptions.InterceptingServiceException;
import ServidorAplicacao.Servico.sop.exceptions.InvalidTimeIntervalServiceException;
import ServidorApresentacao.Action.FenixAction;
import ServidorApresentacao.Action.exceptions.InvalidTimeIntervalActionException;
import ServidorApresentacao.Action.sop.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.exceptions.InterceptingActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.DiaSemana;
import Util.TipoAula;

/**
 * @author tfc130
 */
public class CriarAulaFormAction extends FenixAction {

	public static String INVALID_TIME_INTERVAL =
		"errors.lesson.invalid.time.interval";

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		DynaActionForm criarAulaForm = (DynaActionForm) form;

		HttpSession sessao = request.getSession(false);
		if (sessao != null) {
			IUserView userView =
				(IUserView) sessao.getAttribute(SessionConstants.U_VIEW);

			Calendar inicio = Calendar.getInstance();
			inicio.set(
				Calendar.HOUR_OF_DAY,
				Integer.parseInt((String) criarAulaForm.get("horaInicio")));
			inicio.set(
				Calendar.MINUTE,
				Integer.parseInt((String) criarAulaForm.get("minutosInicio")));
			inicio.set(Calendar.SECOND, 0);
			Calendar fim = Calendar.getInstance();
			fim.set(
				Calendar.HOUR_OF_DAY,
				Integer.parseInt((String) criarAulaForm.get("horaFim")));
			fim.set(
				Calendar.MINUTE,
				Integer.parseInt((String) criarAulaForm.get("minutosFim")));
			fim.set(Calendar.SECOND, 0);

			String initials = (String) criarAulaForm.get("courseInitials");

			InfoExecutionCourse courseView =
				RequestUtils.getExecutionCourseBySigla(request, initials);

			if (courseView == null) {
				return mapping.getInputForward();
			}

			InfoRoom infoSala = new InfoRoom();
			infoSala.setNome((String) criarAulaForm.get("nomeSala"));
			Object argsCriarAula[] =
				{
					 new InfoLesson(
						new DiaSemana(
							new Integer(
								(String) criarAulaForm.get("diaSemana"))),
						inicio,
						fim,
						new TipoAula(
							new Integer(
								(String) criarAulaForm.get("tipoAula"))),
						infoSala,
						courseView)};

			InfoLessonServiceResult result = null;
			try {
				result =
					(InfoLessonServiceResult) ServiceUtils.executeService(
						userView,
						"CriarAula",
						argsCriarAula);
			} catch (InterceptingServiceException ex) {
				
				throw new InterceptingActionException(infoSala.getNome(), ex);
			} catch (ExistingServiceException ex) {
				
				throw new ExistingActionException("A aula", ex);
			} catch (InvalidTimeIntervalServiceException ex) {
				throw new InvalidTimeIntervalActionException(ex);
			}
			ActionErrors actionErrors = getActionErrors(result, inicio, fim);

			if (actionErrors.isEmpty()) {
				return mapping.findForward("Sucesso");
			} else {
				saveErrors(request, actionErrors);
				return mapping.getInputForward();
			}

		} else
			throw new Exception();
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
				actionErrors.add(
					INVALID_TIME_INTERVAL,
					new ActionError(INVALID_TIME_INTERVAL));
				break;
		}
		return actionErrors;
	}

}
