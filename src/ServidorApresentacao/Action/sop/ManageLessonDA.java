package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import DataBeans.InfoShift;
import DataBeans.KeyLesson;
import DataBeans.RoomKey;
import DataBeans.comparators.RoomAlphabeticComparator;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.InterceptingServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidTimeIntervalServiceException;
import ServidorAplicacao.Servico.sop.CreateLesson;
import ServidorAplicacao.Servico.sop.EditLesson;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.InterceptingActionException;
import ServidorApresentacao.Action.exceptions.InvalidTimeIntervalActionException;
import ServidorApresentacao.Action.sop.base.FenixLessonAndShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.DiaSemana;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ManageLessonDA
	extends FenixLessonAndShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

	public static String INVALID_TIME_INTERVAL =
		"errors.lesson.invalid.time.interval";
	public static String INVALID_WEEKDAY = "errors.lesson.invalid.weekDay";
	public static String UNKNOWN_ERROR = "errors.unknown";

	public ActionForward findInput(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		String action = request.getParameter("action");
		if (action != null && action.equals("edit")) {
			return prepareEdit(mapping, form, request, response);
		} else {
			return prepareCreate(mapping, form, request, response);
		}

	}

	public ActionForward prepareCreate(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		return mapping.findForward("ShowLessonForm");
	}

	public ActionForward prepareEdit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		DynaActionForm manageLessonForm = (DynaActionForm) form;

		InfoLesson infoLesson =
			(InfoLesson) request.getAttribute(SessionConstants.LESSON);

		manageLessonForm.set(
			"diaSemana",
			infoLesson.getDiaSemana().getDiaSemana().toString());
		manageLessonForm.set(
			"horaInicio",
			"" + infoLesson.getInicio().get(Calendar.HOUR_OF_DAY));
		manageLessonForm.set(
			"minutosInicio",
			"" + infoLesson.getInicio().get(Calendar.MINUTE));
		manageLessonForm.set(
			"horaFim",
			"" + infoLesson.getFim().get(Calendar.HOUR_OF_DAY));
		manageLessonForm.set(
			"minutosFim",
			"" + infoLesson.getFim().get(Calendar.MINUTE));
		manageLessonForm.set(
			"nomeSala",
			"" + infoLesson.getInfoSala().getNome());

		request.setAttribute("action", "edit");
		return mapping.findForward("ShowLessonForm");
	}

	public ActionForward chooseRoom(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		DynaActionForm manageLessonForm = (DynaActionForm) form;

		DiaSemana weekDay =
			new DiaSemana(
				new Integer(
					formDay2EnumerateDay(
						(String) manageLessonForm.get("diaSemana"))));

		Calendar inicio = Calendar.getInstance();
		inicio.set(
			Calendar.HOUR_OF_DAY,
			Integer.parseInt((String) manageLessonForm.get("horaInicio")));
		inicio.set(
			Calendar.MINUTE,
			Integer.parseInt((String) manageLessonForm.get("minutosInicio")));
		inicio.set(Calendar.SECOND, 0);
		Calendar fim = Calendar.getInstance();
		fim.set(
			Calendar.HOUR_OF_DAY,
			Integer.parseInt((String) manageLessonForm.get("horaFim")));
		fim.set(
			Calendar.MINUTE,
			Integer.parseInt((String) manageLessonForm.get("minutosFim")));
		fim.set(Calendar.SECOND, 0);

		InfoRoom infoSala = new InfoRoom();
		infoSala.setNome((String) manageLessonForm.get("nomeSala"));

		ActionErrors actionErrors =
			checkTimeIntervalAndWeekDay(inicio, fim, weekDay);

		if (actionErrors.isEmpty()) {
			InfoRoom infoRoom = new InfoRoom();
			infoRoom.setCapacidadeNormal(new Integer(0));
			infoRoom.setCapacidadeExame(new Integer(0));

			InfoLesson lessonBeingEdited = (InfoLesson) request.getAttribute(SessionConstants.LESSON);

			InfoLesson infoLesson = new InfoLesson();
			if (lessonBeingEdited != null) {
				infoLesson.setIdInternal(lessonBeingEdited.getIdInternal());
			}
			infoLesson.setDiaSemana(weekDay);
			infoLesson.setInicio(inicio);
			infoLesson.setFim(fim);

			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) (request
					.getAttribute(SessionConstants.EXECUTION_PERIOD));

			Object args[] = { infoRoom, infoLesson, infoExecutionPeriod };

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

			String action = request.getParameter("action");
			if (action != null && action.equals("edit")) {
				// Permit selection of current room only if the day didn't
				// change and the hour is contained within the original hour
//				InfoLesson infoLessonOld =
//					(InfoLesson) request.getAttribute(SessionConstants.LESSON);
				manageLessonForm.set(
					"nomeSala",
					""
						+ ((InfoLesson) request
							.getAttribute(SessionConstants.LESSON))
							.getInfoSala()
							.getNome());
			}

			Collections.sort(emptyRoomsList, new RoomAlphabeticComparator());
			List listaSalas = new ArrayList();
			for (int i = 0; i < emptyRoomsList.size(); i++) {
				InfoRoom elem = (InfoRoom) emptyRoomsList.get(i);
				listaSalas.add(
					new LabelValueBean(elem.getNome(), elem.getNome()));
			}

			request.setAttribute("action", action);
			//emptyRoomsList.add(0, infoSala);

			//sessao.removeAttribute("listaSalas");
			//sessao.removeAttribute("listaInfoSalas");
			request.setAttribute("listaSalas", listaSalas);
			//request.setAttribute("listaInfoSalas", emptyRoomsList);
			//			request.setAttribute(SessionConstants.LESSON, infoLesson);

			request.setAttribute("manageLessonForm", manageLessonForm);

			return mapping.findForward("ShowChooseRoomForm");
		} else {
			saveErrors(request, actionErrors);
			return mapping.getInputForward();
		}

	}

	public ActionForward createLesson(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		DynaActionForm manageLessonForm = (DynaActionForm) form;
		request.setAttribute("manageLessonForm", manageLessonForm);

		DiaSemana weekDay =
			new DiaSemana(
				new Integer(
					formDay2EnumerateDay(
						(String) manageLessonForm.get("diaSemana"))));

		Calendar inicio = Calendar.getInstance();
		inicio.set(
			Calendar.HOUR_OF_DAY,
			Integer.parseInt((String) manageLessonForm.get("horaInicio")));
		inicio.set(
			Calendar.MINUTE,
			Integer.parseInt((String) manageLessonForm.get("minutosInicio")));
		inicio.set(Calendar.SECOND, 0);
		Calendar fim = Calendar.getInstance();
		fim.set(
			Calendar.HOUR_OF_DAY,
			Integer.parseInt((String) manageLessonForm.get("horaFim")));
		fim.set(
			Calendar.MINUTE,
			Integer.parseInt((String) manageLessonForm.get("minutosFim")));
		fim.set(Calendar.SECOND, 0);

		InfoRoom infoSala = new InfoRoom();
		infoSala.setNome((String) manageLessonForm.get("nomeSala"));

		ActionErrors actionErrors =
			checkTimeIntervalAndWeekDay(inicio, fim, weekDay);

		if (actionErrors.isEmpty()) {
			InfoRoom infoRoom = new InfoRoom();
			infoRoom.setCapacidadeNormal(new Integer(0));
			infoRoom.setCapacidadeExame(new Integer(0));

			InfoLesson infoLesson = new InfoLesson();
			infoLesson.setDiaSemana(weekDay);
			infoLesson.setInicio(inicio);
			infoLesson.setFim(fim);

			InfoShift infoShift =
				(InfoShift) (request.getAttribute(SessionConstants.SHIFT));

			infoLesson.setInfoDisciplinaExecucao(
				infoShift.getInfoDisciplinaExecucao());
			infoLesson.setTipo(infoShift.getTipo());
			infoLesson.setInfoSala(infoSala);

			Object args[] = { infoLesson, infoShift };

			try {
				ServiceUtils.executeService(
					SessionUtils.getUserView(request),
					"CreateLesson",
					args);
			} catch (CreateLesson.InvalidLoadException ex) {
				//ActionErrors actionErrors = new ActionErrors();
				if (ex.getMessage().endsWith("REACHED")) {			
					actionErrors.add(
						"errors.shift.hours.limit.reached",
						new ActionError("errors.shift.hours.limit.reached"));
				} else {
					actionErrors.add(
						"errors.shift.hours.limit.exceeded",
						new ActionError("errors.shift.hours.limit.exceeded"));					
				}
				saveErrors(request, actionErrors);
				return mapping.getInputForward();
			}

			return mapping.findForward("EditShift");
		} else {
			saveErrors(request, actionErrors);
			return mapping.getInputForward();
		}
	}

	private String formDay2EnumerateDay(String string) {
		String result = string;
		if (string.equalsIgnoreCase("2")) {
			result = "2";
		}
		if (string.equalsIgnoreCase("3")) {
			result = "3";
		}
		if (string.equalsIgnoreCase("4")) {
			result = "4";
		}
		if (string.equalsIgnoreCase("5")) {
			result = "5";
		}
		if (string.equalsIgnoreCase("6")) {
			result = "6";
		}
		if (string.equalsIgnoreCase("S")) {
			result = "7";
		}
		if (string.equalsIgnoreCase("D")) {
			result = "1";
		}
		return result;
	}

	private ActionErrors checkTimeIntervalAndWeekDay(
		Calendar begining,
		Calendar end,
		DiaSemana weekday) {
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

		if (weekday.getDiaSemana().intValue() < 1
			|| weekday.getDiaSemana().intValue() > 7) {
			actionErrors.add(
				INVALID_WEEKDAY,
				new ActionError(INVALID_WEEKDAY, ""));
		}

		return actionErrors;
	}

	public ActionForward deleteLesson(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession sessao = request.getSession(false);

		IUserView userView = (IUserView) sessao.getAttribute("UserView");

		Object argsReadLessonByOID[] =
			{ new Integer(request.getParameter(SessionConstants.LESSON_OID))};

		InfoLesson lessonToDelete =
			(InfoLesson) ServiceUtils.executeService(
				SessionUtils.getUserView(request),
				"ReadLessonByOID",
				argsReadLessonByOID);

		if (lessonToDelete != null) {
			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) request.getAttribute(
					SessionConstants.EXECUTION_PERIOD);

			Object argsApagarAula[] =
				{
					new KeyLesson(
						lessonToDelete.getDiaSemana(),
						lessonToDelete.getInicio(),
						lessonToDelete.getFim(),
						new RoomKey(lessonToDelete.getInfoSala().getNome())),
					infoExecutionPeriod };
			Boolean result =
				(Boolean) ServiceManagerServiceFactory.executeService(
					userView,
					"ApagarAula",
					argsApagarAula);

			if (result != null && result.booleanValue()) {
				request.removeAttribute(SessionConstants.LESSON_OID);
			}

		}

		return mapping.findForward("LessonDeleted");
	}

	public ActionForward createEditLesson(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		DynaActionForm manageLessonForm = (DynaActionForm) form;
		request.setAttribute("manageLessonForm", manageLessonForm);

		DiaSemana weekDay =
			new DiaSemana(
				new Integer(
					formDay2EnumerateDay(
						(String) manageLessonForm.get("diaSemana"))));

		Calendar inicio = Calendar.getInstance();
		inicio.set(
			Calendar.HOUR_OF_DAY,
			Integer.parseInt((String) manageLessonForm.get("horaInicio")));
		inicio.set(
			Calendar.MINUTE,
			Integer.parseInt((String) manageLessonForm.get("minutosInicio")));
		inicio.set(Calendar.SECOND, 0);
		Calendar fim = Calendar.getInstance();
		fim.set(
			Calendar.HOUR_OF_DAY,
			Integer.parseInt((String) manageLessonForm.get("horaFim")));
		fim.set(
			Calendar.MINUTE,
			Integer.parseInt((String) manageLessonForm.get("minutosFim")));
		fim.set(Calendar.SECOND, 0);

		InfoRoom infoSala = new InfoRoom();
		infoSala.setNome((String) manageLessonForm.get("nomeSala"));

		ActionErrors actionErrors =
			checkTimeIntervalAndWeekDay(inicio, fim, weekDay);

		if (actionErrors.isEmpty()) {
			InfoRoom infoRoom = new InfoRoom();
			infoRoom.setCapacidadeNormal(new Integer(0));
			infoRoom.setCapacidadeExame(new Integer(0));

			InfoLesson infoLessonToCreateOrEdited = new InfoLesson();
			infoLessonToCreateOrEdited.setDiaSemana(weekDay);
			infoLessonToCreateOrEdited.setInicio(inicio);
			infoLessonToCreateOrEdited.setFim(fim);

			InfoShift infoShift =
				(InfoShift) (request.getAttribute(SessionConstants.SHIFT));

			infoLessonToCreateOrEdited.setInfoDisciplinaExecucao(
				infoShift.getInfoDisciplinaExecucao());
			infoLessonToCreateOrEdited.setTipo(infoShift.getTipo());
			infoLessonToCreateOrEdited.setInfoSala(infoSala);

			String action = request.getParameter("action");
			if (action != null && action.equals("edit")) {

				InfoLesson infoLessonOld =
					(InfoLesson) request.getAttribute(SessionConstants.LESSON);
				KeyLesson keyLessonOld =
					new KeyLesson(
						infoLessonOld.getDiaSemana(),
						infoLessonOld.getInicio(),
						infoLessonOld.getFim(),
						new RoomKey(infoLessonOld.getInfoSala().getNome()));

				Object argsEditLesson[] =
					{
						keyLessonOld,
						infoLessonToCreateOrEdited, /*, iExecutionPeriod*/
						infoShift		
				};

				try {
					ServiceUtils.executeService(
						SessionUtils.getUserView(request),
						"EditLesson",
						argsEditLesson);
				} catch (EditLesson.InvalidLoadException ex) {
					//ActionErrors actionErrors = new ActionErrors();
					if (ex.getMessage().endsWith("REACHED")) {			
						actionErrors.add(
							"errors.shift.hours.limit.reached",
							new ActionError("errors.shift.hours.limit.reached"));
					} else {
						actionErrors.add(
							"errors.shift.hours.limit.exceeded",
							new ActionError("errors.shift.hours.limit.exceeded"));					
					}
					saveErrors(request, actionErrors);
					return mapping.getInputForward();
				} catch (ExistingServiceException ex) {
					throw new ExistingActionException("A aula", ex);
				} catch (InterceptingServiceException ex) {
					throw new InterceptingActionException(
						infoSala.getNome(),
						ex);
				} catch (InvalidTimeIntervalServiceException ex) {
					throw new InvalidTimeIntervalActionException(ex);
				}

			} else {
				Object argsCreateLesson[] =
					{ infoLessonToCreateOrEdited, infoShift };
				try {
					ServiceUtils.executeService(
						SessionUtils.getUserView(request),
						"CreateLesson",
						argsCreateLesson);
				} catch (CreateLesson.InvalidLoadException ex) {
					//ActionErrors actionErrors = new ActionErrors();
					if (ex.getMessage().endsWith("REACHED")) {			
						actionErrors.add(
							"errors.shift.hours.limit.reached",
							new ActionError("errors.shift.hours.limit.reached"));
					} else {
						actionErrors.add(
							"errors.shift.hours.limit.exceeded",
							new ActionError("errors.shift.hours.limit.exceeded"));					
					}
					saveErrors(request, actionErrors);
					return mapping.getInputForward();
				}					
			}

			return mapping.findForward("EditShift");
		} else {
			saveErrors(request, actionErrors);
			return mapping.getInputForward();
		}
	}

}