package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import DataBeans.InfoLessonServiceResult;
import DataBeans.InfoRoom;
import DataBeans.KeyLesson;
import DataBeans.RoomKey;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.InterceptingServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidTimeIntervalServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.InterceptingActionException;
import ServidorApresentacao
	.Action
	.exceptions
	.InvalidTimeIntervalActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.DiaSemana;
import Util.TipoAula;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class LessonManagerDispatchAction extends LookupDispatchAction {

	public static String INVALID_TIME_INTERVAL =
		"errors.lesson.invalid.time.interval";
	public static String UNKNOWN_ERROR = "errors.unknown";

	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("lable.changeRoom", "changeRoom");
		map.put("label.save", "storeChanges");
		map.put("lable.chooseRoom", "chooseRoom");
		map.put("label.create", "createRoom");
		return map;
	}


	public ActionForward createRoom(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			SessionUtils.validSessionVerification(request, mapping);
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
				String parameter =
					request.getParameter(new String("operation"));
				return mapping.findForward(parameter);
			} else {
				saveErrors(request, actionErrors);
				return mapping.getInputForward();
			}

		} else
			throw new Exception();
	}

	public ActionForward storeChanges(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		SessionUtils.validSessionVerification(request, mapping);
		DynaActionForm editarAulaForm = (DynaActionForm) form;

		HttpSession sessao = request.getSession(false);
		if (sessao != null) {
			IUserView userView =
				(IUserView) sessao.getAttribute(SessionConstants.U_VIEW);

			GestorServicos gestor = GestorServicos.manager();
			InfoLesson iAulaAntiga =
				(InfoLesson) sessao.getAttribute("infoAula");

			Calendar inicio = Calendar.getInstance();
			inicio.set(
				Calendar.HOUR_OF_DAY,
				Integer.parseInt((String) editarAulaForm.get("horaInicio")));
			inicio.set(
				Calendar.MINUTE,
				Integer.parseInt((String) editarAulaForm.get("minutosInicio")));
			inicio.set(Calendar.SECOND, 0);
			Calendar fim = Calendar.getInstance();
			fim.set(
				Calendar.HOUR_OF_DAY,
				Integer.parseInt((String) editarAulaForm.get("horaFim")));
			fim.set(
				Calendar.MINUTE,
				Integer.parseInt((String) editarAulaForm.get("minutosFim")));
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
					new DiaSemana(
						new Integer((String) editarAulaForm.get("diaSemana"))),
					inicio,
					fim,
					new TipoAula(
						new Integer((String) editarAulaForm.get("tipoAula"))),
					infoSala,
					iAulaAntiga.getInfoDisciplinaExecucao());

			Object argsEditarAula[] = { kAulaAntiga, iAula };

			InfoLessonServiceResult result = null;
			try {
				result =
					(InfoLessonServiceResult) gestor.executar(
						userView,
						"EditarAula",
						argsEditarAula);
			} catch (ExistingServiceException ex) {
				throw new ExistingActionException("A aula", ex);
			} catch (InterceptingServiceException ex) {
				throw new InterceptingActionException(infoSala.getNome(), ex);
			} catch (InvalidTimeIntervalServiceException ex) {
				throw new InvalidTimeIntervalActionException(ex);
			}

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

				String parameter =
					request.getParameter(new String("operation"));
				return mapping.findForward(parameter);
			} else {
				saveErrors(request, actionErrors);
				return mapping.getInputForward();
			}
		} else
			throw new Exception();
	}

	public ActionForward changeRoom(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		SessionUtils.validSessionVerification(request, mapping);
		DynaActionForm editarAulaForm = (DynaActionForm) form;

		HttpSession sessao = request.getSession(false);
		if (sessao != null) {
			IUserView userView =
				(IUserView) sessao.getAttribute(SessionConstants.U_VIEW);

			GestorServicos gestor = GestorServicos.manager();

			DiaSemana weekDay =
				new DiaSemana(
					new Integer((String) editarAulaForm.get("diaSemana")));

			sessao.setAttribute("weekDayString", weekDay.toString());			

			Calendar inicio = Calendar.getInstance();
			inicio.set(
				Calendar.HOUR_OF_DAY,
				Integer.parseInt((String) editarAulaForm.get("horaInicio")));
			inicio.set(
				Calendar.MINUTE,
				Integer.parseInt((String) editarAulaForm.get("minutosInicio")));
			inicio.set(Calendar.SECOND, 0);
			Calendar fim = Calendar.getInstance();
			fim.set(
				Calendar.HOUR_OF_DAY,
				Integer.parseInt((String) editarAulaForm.get("horaFim")));
			fim.set(
				Calendar.MINUTE,
				Integer.parseInt((String) editarAulaForm.get("minutosFim")));
			fim.set(Calendar.SECOND, 0);

			InfoRoom infoSala = new InfoRoom();
			infoSala.setNome((String) editarAulaForm.get("nomeSala"));

			ActionErrors actionErrors = checkTimeInterval(inicio, fim);

			if (actionErrors.isEmpty()) {
				InfoRoom infoRoom = new InfoRoom();
				infoRoom.setCapacidadeNormal(new Integer(0));
				infoRoom.setCapacidadeExame(new Integer(0));

				InfoLesson infoLesson = new InfoLesson();
				infoLesson.setDiaSemana(weekDay);
				infoLesson.setInicio(inicio);
				infoLesson.setFim(fim);

				Object args[] = { infoRoom, infoLesson };

				List emptyRoomsList =
					(List) ServiceUtils.executeService(
						SessionUtils.getUserView(request),
						"ReadEmptyRoomsService",
						args);

				if (emptyRoomsList == null || emptyRoomsList.isEmpty()) {
					actionErrors.add(
						"search.empty.rooms.no.rooms",
						new ActionError("search.empty.rooms.no.rooms"));
					saveErrors(request, actionErrors);
					return mapping.getInputForward();
				}

				ArrayList listaSalas = new ArrayList();
				listaSalas.add(new LabelValueBean(infoSala.getNome(), infoSala.getNome()));
				for (int i = 0; i < emptyRoomsList.size(); i++) {
					InfoRoom elem = (InfoRoom) emptyRoomsList.get(i);
					listaSalas.add(new LabelValueBean(elem.getNome(), elem.getNome()));
				}
				emptyRoomsList.add(0, infoSala);

				sessao.removeAttribute("listaSalas");
				sessao.removeAttribute("listaInfoSalas");
				sessao.setAttribute("listaSalas", listaSalas);
				sessao.setAttribute("listaInfoSalas", emptyRoomsList);

				String parameter = request.getParameter(new String("operation"));
				return mapping.findForward(parameter);
			} else {
				saveErrors(request, actionErrors);
				return mapping.getInputForward();
			}
		} else
			throw new Exception();
	}

	public ActionForward chooseRoom(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		SessionUtils.validSessionVerification(request, mapping);
		DynaActionForm editarAulaForm = (DynaActionForm) form;

		HttpSession sessao = request.getSession(false);
		if (sessao != null) {
			IUserView userView =
				(IUserView) sessao.getAttribute(SessionConstants.U_VIEW);

			GestorServicos gestor = GestorServicos.manager();

			DiaSemana weekDay =
				new DiaSemana(
					new Integer((String) editarAulaForm.get("diaSemana")));

			sessao.setAttribute("weekDayString", weekDay.toString());

			Calendar inicio = Calendar.getInstance();
			inicio.set(
				Calendar.HOUR_OF_DAY,
				Integer.parseInt((String) editarAulaForm.get("horaInicio")));
			inicio.set(
				Calendar.MINUTE,
				Integer.parseInt((String) editarAulaForm.get("minutosInicio")));
			inicio.set(Calendar.SECOND, 0);
			Calendar fim = Calendar.getInstance();
			fim.set(
				Calendar.HOUR_OF_DAY,
				Integer.parseInt((String) editarAulaForm.get("horaFim")));
			fim.set(
				Calendar.MINUTE,
				Integer.parseInt((String) editarAulaForm.get("minutosFim")));
			fim.set(Calendar.SECOND, 0);

			ActionErrors actionErrors = checkTimeInterval(inicio, fim);

			if (actionErrors.isEmpty()) {
				InfoRoom infoRoom = new InfoRoom();
				infoRoom.setCapacidadeNormal(new Integer(0));
				infoRoom.setCapacidadeExame(new Integer(0));

				InfoLesson infoLesson = new InfoLesson();
				infoLesson.setDiaSemana(weekDay);
				infoLesson.setInicio(inicio);
				infoLesson.setFim(fim);

				Object args[] = { infoRoom, infoLesson };

				List emptyRoomsList =
					(List) ServiceUtils.executeService(
						SessionUtils.getUserView(request),
						"ReadEmptyRoomsService",
						args);

				if (emptyRoomsList == null || emptyRoomsList.isEmpty()) {
					actionErrors.add(
						"search.empty.rooms.no.rooms",
						new ActionError("search.empty.rooms.no.rooms"));
					saveErrors(request, actionErrors);
					return mapping.getInputForward();
				}

				ArrayList listaSalas = new ArrayList();
				listaSalas.add(new LabelValueBean("Escolher", ""));
				for (int i = 0; i < emptyRoomsList.size(); i++) {
					InfoRoom elem = (InfoRoom) emptyRoomsList.get(i);
					listaSalas.add(new LabelValueBean(elem.getNome(), elem.getNome()));
				}

				sessao.removeAttribute("listaSalas");
				sessao.removeAttribute("listaInfoSalas");
				sessao.setAttribute("listaSalas", listaSalas);
				sessao.setAttribute("listaInfoSalas", emptyRoomsList);

				String parameter = request.getParameter(new String("operation"));
				return mapping.findForward(parameter);
			} else {
				saveErrors(request, actionErrors);
				return mapping.getInputForward();
			}
		} else
			throw new Exception();
	}


	private ActionErrors checkTimeInterval(Calendar begining, Calendar end) {
		ActionErrors actionErrors = new ActionErrors();
		String beginMinAppend = "";
		String endMinAppend = "";

		if (begining.get(Calendar.MINUTE) == 0)
			beginMinAppend = "0";
		if (end.get(Calendar.MINUTE) == 0)
			endMinAppend = "0";

		if (begining.getTime().getTime() >= end.getTime().getTime()) {
			actionErrors.add(
				INVALID_TIME_INTERVAL,
				new ActionError(
					INVALID_TIME_INTERVAL,
					""
						+ begining.get(Calendar.HOUR_OF_DAY)
						+ ":"
						+ begining.get(Calendar.MINUTE)
						+ beginMinAppend
						+ " - "
						+ end.get(Calendar.HOUR_OF_DAY)
						+ ":"
						+ end.get(Calendar.MINUTE)
						+ endMinAppend));
		}
		return actionErrors;
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
