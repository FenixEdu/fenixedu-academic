package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
import org.apache.struts.util.LabelValueBean;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonServiceResult;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.comparators.RoomAlphabeticComparator;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InterceptingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidTimeIntervalServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InterceptingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidTimeIntervalActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextLookupDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.TipoAula;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class LessonManagerDispatchAction extends
        FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextLookupDispatchAction {

    public static String INVALID_TIME_INTERVAL = "errors.lesson.invalid.time.interval";

    public static String UNKNOWN_ERROR = "errors.unknown";

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("lable.changeRoom", "changeRoom");
        map.put("label.save", "storeChanges");
        map.put("lable.chooseRoom", "chooseRoom");
        map.put("label.create", "createRoom");
        map.put("input", "input");
        return map;
    }

    public ActionForward createRoom(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm criarAulaForm = (DynaActionForm) form;
        HttpSession sessao = request.getSession(false);
        ContextUtils.setExecutionPeriodContext(request);
        ContextUtils.setExecutionDegreeContext(request);
        ContextUtils.setCurricularYearContext(request);
        if (sessao != null) {
            IUserView userView = (IUserView) sessao.getAttribute(SessionConstants.U_VIEW);

            Calendar inicio = Calendar.getInstance();
            inicio.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String) criarAulaForm.get("horaInicio")));
            inicio.set(Calendar.MINUTE, Integer.parseInt((String) criarAulaForm.get("minutosInicio")));
            inicio.set(Calendar.SECOND, 0);
            Calendar fim = Calendar.getInstance();
            fim.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String) criarAulaForm.get("horaFim")));
            fim.set(Calendar.MINUTE, Integer.parseInt((String) criarAulaForm.get("minutosFim")));
            fim.set(Calendar.SECOND, 0);

            String initials = (String) criarAulaForm.get("courseInitials");

            InfoExecutionCourse courseView = RequestUtils.getExecutionCourseBySigla(request, initials);

            if (courseView == null) {
                return mapping.getInputForward();
            }

            InfoRoom infoSala = new InfoRoom();
            infoSala.setNome((String) criarAulaForm.get("nomeSala"));

            DiaSemana diaSemana = new DiaSemana(new Integer(formDay2EnumerateDay((String) criarAulaForm
                    .get("diaSemana"))));

            /** *** added on 27 may 2004 by amsg * */
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                    .getAttribute(SessionConstants.EXECUTION_DEGREE);

            InfoRoomOccupation infoRoomOccupation = new InfoRoomOccupation();
            infoRoomOccupation.setDayOfWeek(diaSemana);
            infoRoomOccupation.setEndTime(fim);
            infoRoomOccupation.setStartTime(inicio);
            infoRoomOccupation.setInfoRoom(infoSala);
            InfoPeriod infoPeriod = null;
            if (courseView.getInfoExecutionPeriod().getSemester().equals(new Integer(1))) {
                infoPeriod = infoExecutionDegree.getInfoPeriodLessonsFirstSemester();
            } else {
                infoPeriod = infoExecutionDegree.getInfoPeriodLessonsSecondSemester();
            }
            if (infoPeriod == null) {
                throw new FenixActionException(
                        "Info Execution Degree doesn't have a Lessons Info Period");
            }
            infoRoomOccupation.setInfoPeriod(infoPeriod);
            /** *** */

            Object argsCriarAula[] = { new InfoLesson(diaSemana, inicio, fim, new TipoAula(new Integer(
                    (String) criarAulaForm.get("tipoAula"))), infoSala, infoRoomOccupation,
                    new InfoShift(null,
                            new TipoAula(new Integer((String) criarAulaForm.get("tipoAula"))), null,
                            courseView)) };

            InfoLessonServiceResult result = null;
            try {
                result = (InfoLessonServiceResult) ServiceUtils.executeService(userView, "CriarAula",
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
                String parameter = request.getParameter(new String("operation"));
                return mapping.findForward(parameter);
            }
            saveErrors(request, actionErrors);
            return mapping.getInputForward();

        }
        throw new Exception();
    }

    /**
     * @param string
     */
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

    public ActionForward storeChanges(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm editarAulaForm = (DynaActionForm) form;

        HttpSession sessao = request.getSession(false);
        ContextUtils.setExecutionPeriodContext(request);
        ContextUtils.setExecutionDegreeContext(request);
        ContextUtils.setCurricularYearContext(request);
        ContextUtils.setExecutionCourseContext(request);

        if (sessao != null) {
            IUserView userView = (IUserView) sessao.getAttribute(SessionConstants.U_VIEW);

            RequestUtils.setLessonTypes(request);

            Integer oldLessonOID = new Integer(request.getParameter("infoAula_oid"));

            Object args[] = { oldLessonOID };

            InfoLesson iAulaAntiga = null;
            iAulaAntiga = (InfoLesson) ServiceManagerServiceFactory.executeService(userView,
                    "ReadLessonByOID", args);

            //InfoLesson iAulaAntiga =
            //	(InfoLesson) request.getAttribute("infoAula");

            Calendar inicio = Calendar.getInstance();
            inicio
                    .set(Calendar.HOUR_OF_DAY, Integer.parseInt((String) editarAulaForm
                            .get("horaInicio")));
            inicio.set(Calendar.MINUTE, Integer.parseInt((String) editarAulaForm.get("minutosInicio")));
            inicio.set(Calendar.SECOND, 0);
            Calendar fim = Calendar.getInstance();
            fim.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String) editarAulaForm.get("horaFim")));
            fim.set(Calendar.MINUTE, Integer.parseInt((String) editarAulaForm.get("minutosFim")));
            fim.set(Calendar.SECOND, 0);

            InfoRoom infoSala = new InfoRoom();
            infoSala.setNome((String) editarAulaForm.get("nomeSala"));

            DiaSemana diaSemana = new DiaSemana(new Integer(formDay2EnumerateDay((String) editarAulaForm
                    .get("diaSemana"))));

            /** *** added on 27 may 2004 by amsg * */
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                    .getAttribute(SessionConstants.EXECUTION_DEGREE);

            InfoRoomOccupation infoRoomOccupation = new InfoRoomOccupation();
            infoRoomOccupation.setDayOfWeek(diaSemana);
            infoRoomOccupation.setEndTime(fim);
            infoRoomOccupation.setStartTime(inicio);
            infoRoomOccupation.setInfoRoom(infoSala);

            String initials = (String) editarAulaForm.get("courseInitials");
            InfoExecutionCourse courseView = RequestUtils.getExecutionCourseBySigla(request, initials);

            InfoPeriod infoPeriod = null;
            if (courseView.getInfoExecutionPeriod().getSemester().equals(new Integer(1))) {
                infoPeriod = infoExecutionDegree.getInfoPeriodLessonsFirstSemester();
            } else {
                infoPeriod = infoExecutionDegree.getInfoPeriodLessonsSecondSemester();
            }
            if (infoPeriod == null) {
                throw new FenixActionException(
                        "Info Execution Degree doesn't have a Lessons Info Period");
            }
            infoRoomOccupation.setInfoPeriod(infoPeriod);
            /** ****** */

            //            RoomKey kSalaAntiga = new
            // RoomKey(iAulaAntiga.getInfoSala().getNome());
            InfoLesson iAula = new InfoLesson(diaSemana, inicio, fim, new TipoAula(new Integer(
                    (String) editarAulaForm.get("tipoAula"))), infoSala, infoRoomOccupation, iAulaAntiga
                    .getInfoShift());

            //			InfoExecutionPeriod iExecutionPeriod =
            //				(InfoExecutionPeriod) sessao.getAttribute(
            //					SessionConstants.INFO_EXECUTION_PERIOD_KEY);

            Object argsEditarAula[] = { iAulaAntiga, iAula /* , iExecutionPeriod */
            };

            InfoLessonServiceResult result = null;
            try {
                result = (InfoLessonServiceResult) ServiceManagerServiceFactory.executeService(userView,
                        "EditarAula", argsEditarAula);
            } catch (ExistingServiceException ex) {
                throw new ExistingActionException("A aula", ex);
            } catch (InterceptingServiceException ex) {
                throw new InterceptingActionException(infoSala.getNome(), ex);
            } catch (InvalidTimeIntervalServiceException ex) {
                throw new InvalidTimeIntervalActionException(ex);
            }

            InfoExecutionCourse iDE = (InfoExecutionCourse) request
                    .getAttribute(SessionConstants.EXECUTION_COURSE);
            Object argsLerAulas[] = new Object[1];
            argsLerAulas[0] = iDE;
            ArrayList infoAulas = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "LerAulasDeDisciplinaExecucao", argsLerAulas);

            //sessao.removeAttribute("listaAulas");
            if (infoAulas != null && !infoAulas.isEmpty())
                request.setAttribute("listaAulas", infoAulas);

            //sessao.removeAttribute("indexAula");

            ActionErrors actionErrors = getActionErrors(result, inicio, fim);

            if (actionErrors.isEmpty()) {
                //sessao.removeAttribute("infoAula");

                String parameter = request.getParameter(new String("operation"));
                return mapping.findForward(parameter);
            }
            saveErrors(request, actionErrors);
            return mapping.getInputForward();

        }
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
            actionErrors.add(INVALID_TIME_INTERVAL, new ActionError(INVALID_TIME_INTERVAL, ""
                    + begining.get(Calendar.HOUR_OF_DAY) + ":" + begining.get(Calendar.MINUTE)
                    + beginMinAppend + " - " + end.get(Calendar.HOUR_OF_DAY) + ":"
                    + end.get(Calendar.MINUTE) + endMinAppend));
        }
        return actionErrors;
    }

    private ActionErrors getActionErrors(InfoLessonServiceResult result, Calendar inicio, Calendar fim) {
        ActionErrors actionErrors = new ActionErrors();
        String beginMinAppend = "";
        String endMinAppend = "";

        if (inicio.get(Calendar.MINUTE) == 0)
            beginMinAppend = "0";
        if (fim.get(Calendar.MINUTE) == 0)
            endMinAppend = "0";

        switch (result.getMessageType()) {
        case InfoLessonServiceResult.SUCESS:
            break;
        case InfoLessonServiceResult.INVALID_TIME_INTERVAL:
            actionErrors.add(INVALID_TIME_INTERVAL, new ActionError(INVALID_TIME_INTERVAL, ""
                    + inicio.get(Calendar.HOUR_OF_DAY) + ":" + inicio.get(Calendar.MINUTE)
                    + beginMinAppend + " - " + fim.get(Calendar.HOUR_OF_DAY) + ":"
                    + fim.get(Calendar.MINUTE) + endMinAppend));
            break;
        default:
            actionErrors.add(UNKNOWN_ERROR, new ActionError(UNKNOWN_ERROR));
            break;
        }
        return actionErrors;
    }

    public ActionForward input(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        //super.execute(mapping, form, request, response);
        ContextUtils.setExecutionPeriodContext(request);
        ContextUtils.setExecutionDegreeContext(request);
        ContextUtils.setCurricularYearContext(request);
        ContextUtils.setExecutionCourseContext(request);

        DynaActionForm editarAulaForm = (DynaActionForm) form;

        HttpSession sessao = request.getSession(false);
        if (sessao != null) {

            IUserView userView = (IUserView) sessao.getAttribute("UserView");

            Integer oldLessonOID = new Integer(request.getParameter("infoAula_oid"));
            Object argsLesson[] = { oldLessonOID };
            InfoLesson infoAula = null;
            infoAula = (InfoLesson) ServiceManagerServiceFactory.executeService(userView,
                    "ReadLessonByOID", argsLesson);
            request.setAttribute("infoAula", infoAula);

            editarAulaForm.set("diaSemana", String.valueOf(infoAula.getWeekDay()));
            editarAulaForm.set("horaInicio", String.valueOf(infoAula.getInicio().get(
                    Calendar.HOUR_OF_DAY)));
            editarAulaForm.set("minutosInicio", String
                    .valueOf(infoAula.getInicio().get(Calendar.MINUTE)));
            editarAulaForm.set("horaFim", String.valueOf(infoAula.getFim().get(Calendar.HOUR_OF_DAY)));
            editarAulaForm.set("minutosFim", String.valueOf(infoAula.getFim().get(Calendar.MINUTE)));
            editarAulaForm.set("tipoAula", String.valueOf(infoAula.getTipo().getTipo().intValue()));
            editarAulaForm.set("nomeSala", infoAula.getInfoRoomOccupation().getInfoRoom().getNome());

            RequestUtils.setLessonTypes(request);

            return mapping.findForward("Input");
        }
        throw new Exception();

    }
}