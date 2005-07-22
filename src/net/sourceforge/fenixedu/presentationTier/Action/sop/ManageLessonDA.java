package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InterceptingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidTimeIntervalServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.sop.CreateLesson;
import net.sourceforge.fenixedu.applicationTier.Servico.sop.EditLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.comparators.RoomAlphabeticComparator;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InterceptingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidTimeIntervalActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixLessonAndShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;
import net.sourceforge.fenixedu.util.DiaSemana;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class ManageLessonDA extends
        FenixLessonAndShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public static String INVALID_TIME_INTERVAL = "errors.lesson.invalid.time.interval";

    public static String INVALID_WEEKDAY = "errors.lesson.invalid.weekDay";

    public static String UNKNOWN_ERROR = "errors.unknown";

    public ActionForward findInput(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String action = request.getParameter("action");
        if (action != null && action.equals("edit")) {
            return prepareEdit(mapping, form, request, response);
        }
        return prepareCreate(mapping, form, request, response);

    }

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        return mapping.findForward("ShowLessonForm");
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm manageLessonForm = (DynaActionForm) form;

        InfoLesson infoLesson = (InfoLesson) request.getAttribute(SessionConstants.LESSON);

        manageLessonForm.set("diaSemana", infoLesson.getDiaSemana().getDiaSemana().toString());
        manageLessonForm.set("horaInicio", "" + infoLesson.getInicio().get(Calendar.HOUR_OF_DAY));
        manageLessonForm.set("minutosInicio", "" + infoLesson.getInicio().get(Calendar.MINUTE));
        manageLessonForm.set("horaFim", "" + infoLesson.getFim().get(Calendar.HOUR_OF_DAY));
        manageLessonForm.set("minutosFim", "" + infoLesson.getFim().get(Calendar.MINUTE));
        manageLessonForm
                .set("nomeSala", "" + infoLesson.getInfoRoomOccupation().getInfoRoom().getNome());

        if (infoLesson.getInfoRoomOccupation().getFrequency().intValue() == RoomOccupation.QUINZENAL) {
            manageLessonForm.set("quinzenal", new Boolean(true));
            manageLessonForm.set("week", infoLesson.getInfoRoomOccupation().getWeekOfQuinzenalStart()
                    .toString());
        }

        request.setAttribute("action", "edit");
        return mapping.findForward("ShowLessonForm");
    }

    public ActionForward chooseRoom(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm manageLessonForm = (DynaActionForm) form;

        ContextUtils.setExecutionPeriodContext(request);

        DiaSemana weekDay = new DiaSemana(new Integer(formDay2EnumerateDay((String) manageLessonForm
                .get("diaSemana"))));

        Calendar inicio = Calendar.getInstance();
        inicio.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String) manageLessonForm.get("horaInicio")));
        inicio.set(Calendar.MINUTE, Integer.parseInt((String) manageLessonForm.get("minutosInicio")));
        inicio.set(Calendar.SECOND, 0);
        Calendar fim = Calendar.getInstance();
        fim.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String) manageLessonForm.get("horaFim")));
        fim.set(Calendar.MINUTE, Integer.parseInt((String) manageLessonForm.get("minutosFim")));
        fim.set(Calendar.SECOND, 0);

        InfoRoom infoSala = new InfoRoom();
        infoSala.setNome((String) manageLessonForm.get("nomeSala"));

        Boolean quinzenal = (Boolean) manageLessonForm.get("quinzenal");
        Integer weekOfQuinzenalStart = null;
        if (quinzenal == null) {
            quinzenal = new Boolean(false);
        }

        if (quinzenal.booleanValue()) {
            if (manageLessonForm.get("week") == null || manageLessonForm.get("week").equals("")) {
                ActionError actionError = new ActionError("errors.emptyField.checkBoxTrue");
                ActionErrors newActionErrors = new ActionErrors();
                newActionErrors.add("errors.emptyField.checkBoxTrue", actionError);
                saveErrors(request, newActionErrors);
                return prepareCreate(mapping, form, request, response);
            }
            weekOfQuinzenalStart = new Integer(Integer.parseInt((String) manageLessonForm.get("week")));
        }

        ActionErrors actionErrors = checkTimeIntervalAndWeekDay(inicio, fim, weekDay);

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

            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                    .getAttribute(SessionConstants.EXECUTION_PERIOD);
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                    .getAttribute(SessionConstants.EXECUTION_DEGREE);

            InfoPeriod infoPeriod = null;
            if (infoExecutionPeriod.getSemester().equals(new Integer(1))) {
                infoPeriod = infoExecutionDegree.getInfoPeriodLessonsFirstSemester();
            } else {
                infoPeriod = infoExecutionDegree.getInfoPeriodLessonsSecondSemester();
            }

            String action = request.getParameter("action");
            Integer oldRoomOccupationId = null;
            if (action != null && action.equals("edit")) {
                oldRoomOccupationId = lessonBeingEdited.getInfoRoomOccupation().getIdInternal();
            }
            Integer frequency = new Integer(RoomOccupation.DIARIA);
            if (quinzenal.booleanValue()) {
                frequency = new Integer(RoomOccupation.QUINZENAL);
            }

            Object args[] = { infoPeriod.getStartDate(), infoPeriod.getEndDate(), inicio, fim, weekDay, oldRoomOccupationId, null, frequency,
                    weekOfQuinzenalStart, new Boolean(true) };

            List<InfoRoom> emptyRoomsList = (List<InfoRoom>) ServiceUtils.executeService(SessionUtils
                    .getUserView(request), "ReadAvailableRoomsForExam", args);

            if (emptyRoomsList == null || emptyRoomsList.isEmpty()) {
                actionErrors.add("search.empty.rooms.no.rooms", new ActionError(
                        "search.empty.rooms.no.rooms"));
                saveErrors(request, actionErrors);
                return mapping.getInputForward();
            }

            if (action != null && action.equals("edit")) {
                // Permit selection of current room only if the day didn't
                // change and the hour is contained within the original hour
                manageLessonForm.set("nomeSala", ""
                        + ((InfoLesson) request.getAttribute(SessionConstants.LESSON))
                                .getInfoRoomOccupation().getInfoRoom().getNome());
            }

            Collections.sort(emptyRoomsList, new RoomAlphabeticComparator());
            List<LabelValueBean> listaSalas = new ArrayList();
            for (int i = 0; i < emptyRoomsList.size(); i++) {
                InfoRoom elem = emptyRoomsList.get(i);
                listaSalas.add(new LabelValueBean(elem.getNome(), elem.getNome()));
            }

            request.setAttribute("action", action);
            request.setAttribute("listaSalas", listaSalas);
            request.setAttribute("manageLessonForm", manageLessonForm);

            return mapping.findForward("ShowChooseRoomForm");
        }
        saveErrors(request, actionErrors);
        return mapping.getInputForward();

    }

    public ActionForward createLesson(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm manageLessonForm = (DynaActionForm) form;
        request.setAttribute("manageLessonForm", manageLessonForm);

        ContextUtils.setExecutionPeriodContext(request);

        DiaSemana weekDay = new DiaSemana(new Integer(formDay2EnumerateDay((String) manageLessonForm
                .get("diaSemana"))));

        Boolean quinzenal = (Boolean) manageLessonForm.get("quinzenal");
        if (quinzenal == null) {
            quinzenal = new Boolean(false);
        }

        Calendar inicio = Calendar.getInstance();
        inicio.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String) manageLessonForm.get("horaInicio")));
        inicio.set(Calendar.MINUTE, Integer.parseInt((String) manageLessonForm.get("minutosInicio")));
        inicio.set(Calendar.SECOND, 0);
        Calendar fim = Calendar.getInstance();
        fim.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String) manageLessonForm.get("horaFim")));
        fim.set(Calendar.MINUTE, Integer.parseInt((String) manageLessonForm.get("minutosFim")));
        fim.set(Calendar.SECOND, 0);

        InfoRoom infoSala = new InfoRoom();
        infoSala.setNome((String) manageLessonForm.get("nomeSala"));

        ActionErrors actionErrors = checkTimeIntervalAndWeekDay(inicio, fim, weekDay);

        if (actionErrors.isEmpty()) {
            InfoRoom infoRoom = new InfoRoom();
            infoRoom.setCapacidadeNormal(new Integer(0));
            infoRoom.setCapacidadeExame(new Integer(0));

            InfoLesson infoLesson = new InfoLesson();
            infoLesson.setDiaSemana(weekDay);
            infoLesson.setInicio(inicio);
            infoLesson.setFim(fim);

            InfoShift infoShift = (InfoShift) (request.getAttribute(SessionConstants.SHIFT));

            infoLesson.setInfoShift(infoShift);

            infoLesson.setTipo(infoShift.getTipo());
            infoLesson.setInfoSala(infoSala);

            InfoRoomOccupation infoRoomOccupation = new InfoRoomOccupation();
            infoRoomOccupation.setDayOfWeek(weekDay);
            infoRoomOccupation.setEndTime(fim);
            infoRoomOccupation.setStartTime(inicio);
            infoRoomOccupation.setInfoRoom(infoSala);

            if (quinzenal.booleanValue()) {
                infoRoomOccupation.setFrequency(RoomOccupation.QUINZENAL);
                if (manageLessonForm.get("week") == null || manageLessonForm.get("week").equals("")) {
                    ActionError actionError = new ActionError("errors.emptyField.checkBoxTrue");
                    ActionErrors newActionErrors = new ActionErrors();
                    newActionErrors.add("errors.emptyField.checkBoxTrue", actionError);
                    saveErrors(request, newActionErrors);
                    return prepareCreate(mapping, form, request, response);
                }
                infoRoomOccupation.setWeekOfQuinzenalStart(new Integer(Integer
                        .parseInt((String) manageLessonForm.get("week"))));
            } else {
                infoRoomOccupation.setFrequency(RoomOccupation.SEMANAL);
            }

            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                    .getAttribute(SessionConstants.EXECUTION_PERIOD);
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                    .getAttribute(SessionConstants.EXECUTION_DEGREE);

            InfoPeriod infoPeriod = null;

            if (infoExecutionPeriod.getSemester().equals(new Integer(1))) {
                infoPeriod = infoExecutionDegree.getInfoPeriodLessonsFirstSemester();
            } else {
                infoPeriod = infoExecutionDegree.getInfoPeriodLessonsSecondSemester();
            }

            infoRoomOccupation.setInfoPeriod(infoPeriod);

            infoLesson.setInfoRoomOccupation(infoRoomOccupation);

            Object args[] = { infoLesson, infoShift };

            try {
                ServiceUtils.executeService(SessionUtils.getUserView(request), "CreateLesson", args);
            } catch (CreateLesson.InvalidLoadException ex) {

                if (ex.getMessage().endsWith("REACHED")) {
                    actionErrors.add("errors.shift.hours.limit.reached", new ActionError(
                            "errors.shift.hours.limit.reached"));
                } else {
                    actionErrors.add("errors.shift.hours.limit.exceeded", new ActionError(
                            "errors.shift.hours.limit.exceeded"));
                }
                saveErrors(request, actionErrors);
                return mapping.getInputForward();
            }

            return mapping.findForward("EditShift");
        }
        saveErrors(request, actionErrors);
        return mapping.getInputForward();

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
        return result;
    }

    private ActionErrors checkTimeIntervalAndWeekDay(Calendar begining, Calendar end, DiaSemana weekday) {
        ActionErrors actionErrors = new ActionErrors();
        String beginMinAppend = "";
        String endMinAppend = "";

        if (begining.get(Calendar.MINUTE) == 0)
            beginMinAppend = "0";
        if (end.get(Calendar.MINUTE) == 0)
            endMinAppend = "0";

        if (begining.getTime().getTime() >= end.getTime().getTime()) {
            actionErrors.add(INVALID_TIME_INTERVAL, new ActionError(INVALID_TIME_INTERVAL, ""
                    + begining.get(Calendar.HOUR_OF_DAY) + ":" + begining.get(Calendar.MINUTE)
                    + beginMinAppend + " - " + end.get(Calendar.HOUR_OF_DAY) + ":"
                    + end.get(Calendar.MINUTE) + endMinAppend));
        }

        if (weekday.getDiaSemana().intValue() < 1 || weekday.getDiaSemana().intValue() > 7) {
            actionErrors.add(INVALID_WEEKDAY, new ActionError(INVALID_WEEKDAY, ""));
        }

        return actionErrors;
    }

    public ActionForward deleteLesson(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession sessao = request.getSession(false);

        IUserView userView = (IUserView) sessao.getAttribute("UserView");

        List<Integer> lessons = new ArrayList<Integer>();
        lessons.add(new Integer(request.getParameter(SessionConstants.LESSON_OID)));

        Object argsApagarAula[] = { lessons };
        Boolean result = (Boolean) ServiceManagerServiceFactory.executeService(userView,
                "DeleteLessons", argsApagarAula);

        if (result != null && result.booleanValue()) {
            request.removeAttribute(SessionConstants.LESSON_OID);
        }

        return mapping.findForward("LessonDeleted");
    }

    public ActionForward createEditLesson(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm manageLessonForm = (DynaActionForm) form;
        request.setAttribute("manageLessonForm", manageLessonForm);

        ContextUtils.setExecutionPeriodContext(request);

        DiaSemana weekDay = new DiaSemana(new Integer(formDay2EnumerateDay((String) manageLessonForm
                .get("diaSemana"))));

        Boolean quinzenal = (Boolean) manageLessonForm.get("quinzenal");
        if (quinzenal == null) {
            quinzenal = new Boolean(false);
        }

        Calendar inicio = Calendar.getInstance();
        inicio.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String) manageLessonForm.get("horaInicio")));
        inicio.set(Calendar.MINUTE, Integer.parseInt((String) manageLessonForm.get("minutosInicio")));
        inicio.set(Calendar.SECOND, 0);
        Calendar fim = Calendar.getInstance();
        fim.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String) manageLessonForm.get("horaFim")));
        fim.set(Calendar.MINUTE, Integer.parseInt((String) manageLessonForm.get("minutosFim")));
        fim.set(Calendar.SECOND, 0);

        InfoRoom infoSala = new InfoRoom();
        infoSala.setNome((String) manageLessonForm.get("nomeSala"));

        ActionErrors actionErrors = checkTimeIntervalAndWeekDay(inicio, fim, weekDay);

        if (actionErrors.isEmpty()) {
            InfoRoom infoRoom = new InfoRoom();
            infoRoom.setCapacidadeNormal(new Integer(0));
            infoRoom.setCapacidadeExame(new Integer(0));

            InfoLesson infoLessonToCreateOrEdited = new InfoLesson();
            infoLessonToCreateOrEdited.setDiaSemana(weekDay);
            infoLessonToCreateOrEdited.setInicio(inicio);
            infoLessonToCreateOrEdited.setFim(fim);

            InfoShift infoShift = (InfoShift) (request.getAttribute(SessionConstants.SHIFT));

            infoLessonToCreateOrEdited.setInfoShift(infoShift);
            infoLessonToCreateOrEdited.setTipo(infoShift.getTipo());
            infoLessonToCreateOrEdited.setInfoSala(infoSala);

            InfoRoomOccupation infoRoomOccupation = new InfoRoomOccupation();
            infoRoomOccupation.setDayOfWeek(weekDay);
            infoRoomOccupation.setEndTime(fim);
            infoRoomOccupation.setStartTime(inicio);
            infoRoomOccupation.setInfoRoom(infoSala);

            if (quinzenal.booleanValue()) {
                infoRoomOccupation.setFrequency(RoomOccupation.QUINZENAL);
                if (manageLessonForm.get("week") == null || manageLessonForm.get("week").equals("")) {
                    ActionError actionError = new ActionError("errors.emptyField.checkBoxTrue");
                    ActionErrors newActionErrors = new ActionErrors();
                    newActionErrors.add("errors.emptyField.checkBoxTrue", actionError);
                    saveErrors(request, newActionErrors);
                    return prepareEdit(mapping, form, request, response);
                }
                infoRoomOccupation.setWeekOfQuinzenalStart(new Integer(Integer
                        .parseInt((String) manageLessonForm.get("week"))));
            } else {
                infoRoomOccupation.setFrequency(RoomOccupation.SEMANAL);
            }

            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                    .getAttribute(SessionConstants.EXECUTION_PERIOD);
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                    .getAttribute(SessionConstants.EXECUTION_DEGREE);

            InfoPeriod infoPeriod = null;

            if (infoExecutionPeriod.getSemester().equals(new Integer(1))) {
                infoPeriod = infoExecutionDegree.getInfoPeriodLessonsFirstSemester();
            } else {
                infoPeriod = infoExecutionDegree.getInfoPeriodLessonsSecondSemester();
            }

            infoRoomOccupation.setInfoPeriod(infoPeriod);

            infoLessonToCreateOrEdited.setInfoRoomOccupation(infoRoomOccupation);

            String action = request.getParameter("action");
            if (action != null && action.equals("edit")) {

                InfoLesson infoLessonOld = (InfoLesson) request.getAttribute(SessionConstants.LESSON);

                Object argsEditLesson[] = { infoLessonOld, infoLessonToCreateOrEdited, infoShift };

                try {
                    ServiceUtils.executeService(SessionUtils.getUserView(request), "EditLesson",
                            argsEditLesson);
                } catch (EditLesson.InvalidLoadException ex) {
                    if (ex.getMessage().endsWith("REACHED")) {
                        actionErrors.add("errors.shift.hours.limit.reached", new ActionError(
                                "errors.shift.hours.limit.reached"));
                    } else {
                        actionErrors.add("errors.shift.hours.limit.exceeded", new ActionError(
                                "errors.shift.hours.limit.exceeded"));
                    }
                    saveErrors(request, actionErrors);
                    return mapping.getInputForward();
                } catch (ExistingServiceException ex) {
                    throw new ExistingActionException("A aula", ex);
                } catch (InterceptingServiceException ex) {
                    throw new InterceptingActionException(infoSala.getNome(), ex);
                } catch (InvalidTimeIntervalServiceException ex) {
                    throw new InvalidTimeIntervalActionException(ex);
                }

            } else {
                Object argsCreateLesson[] = { infoLessonToCreateOrEdited, infoShift };
                try {
                    ServiceUtils.executeService(SessionUtils.getUserView(request), "CreateLesson",
                            argsCreateLesson);
                } catch (CreateLesson.InvalidLoadException ex) {
                    if (ex.getMessage().endsWith("REACHED")) {
                        actionErrors.add("errors.shift.hours.limit.reached", new ActionError(
                                "errors.shift.hours.limit.reached"));
                    } else {
                        actionErrors.add("errors.shift.hours.limit.exceeded", new ActionError(
                                "errors.shift.hours.limit.exceeded"));
                    }
                    saveErrors(request, actionErrors);
                    return mapping.getInputForward();
                }
            }

            return mapping.findForward("EditShift");
        }
        saveErrors(request, actionErrors);
        return mapping.getInputForward();

    }

}