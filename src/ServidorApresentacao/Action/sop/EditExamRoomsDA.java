/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 2003/03/21
 *
 */
package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExam;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoRoom;
import DataBeans.InfoViewExamByDayAndShift;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.base.FenixDateAndTimeAndCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.sop.utils.Util;
import ServidorApresentacao.Action.utils.ContextUtils;
import Util.Season;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class EditExamRoomsDA
        extends
        FenixDateAndTimeAndCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm editExamRoomsForm = (DynaActionForm) form;

        //InfoExam infoExam =
        //	((InfoViewExamByDayAndShift) session
        //		.getAttribute(SessionConstants.INFO_EXAMS_KEY))
        //		.getInfoExam();
        ContextUtils.setExecutionCourseContext(request);
        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) request
                .getAttribute(SessionConstants.EXECUTION_COURSE);

        Season oldExamsSeason = new Season(new Integer(request.getParameter("oldExamSeason")));

        Object args1[] = { infoExecutionCourse.getSigla(), oldExamsSeason,
                infoExecutionCourse.getInfoExecutionPeriod() };
        InfoExam infoExam = null;
        InfoViewExamByDayAndShift infoViewExamByDayAndShift = null;
        try {
            infoViewExamByDayAndShift = ((InfoViewExamByDayAndShift) ServiceUtils.executeService(
                    userView, "ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriod", args1));
            infoExam = infoViewExamByDayAndShift.getInfoExam();
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        List examRooms = infoExam.getAssociatedRooms();

        if (examRooms != null) {
            String[] rooms = new String[examRooms.size()];
            for (int i = 0; i < examRooms.size(); i++) {
                rooms[i] = ((InfoRoom) examRooms.get(i)).getIdInternal().toString();
            }
            editExamRoomsForm.set("selectedRooms", rooms);
        } else {
            editExamRoomsForm.set("selectedRooms", null);
        }

        Object[] args = { infoExam };
        List availableRooms;
        try {
            availableRooms = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadEmptyRoomsForExam", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Hashtable roomsHashTable = new Hashtable();
        for (int i = 0; i < availableRooms.size(); i++) {
            InfoRoom infoRoom = (InfoRoom) availableRooms.get(i);
            List roomsOfBuilding = (List) roomsHashTable.get(infoRoom.getEdificio());
            if (roomsOfBuilding == null) {
                roomsOfBuilding = new ArrayList();
                roomsHashTable.put(infoRoom.getEdificio(), roomsOfBuilding);
            }
            roomsOfBuilding.add(infoRoom);
        }

        List sortedRooms = new ArrayList();
        Enumeration keyBuildings = roomsHashTable.keys();
        while (keyBuildings.hasMoreElements()) {
            sortedRooms.add(roomsHashTable.get(keyBuildings.nextElement()));
        }

        request.setAttribute(SessionConstants.AVAILABLE_ROOMS, sortedRooms);

        request.setAttribute(SessionConstants.INFO_EXAMS_KEY, infoViewExamByDayAndShift);

        String input = request.getParameter("input");
        request.setAttribute(SessionConstants.NEXT_PAGE, input);

        return mapping.findForward("ViewSelectRoomsForm");
    }

    public ActionForward select(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm editExamRoomsForm = (DynaActionForm) form;

        //InfoViewExamByDayAndShift infoViewExamByDayAndShift =
        //	(InfoViewExamByDayAndShift) session.getAttribute(
        //		SessionConstants.INFO_EXAMS_KEY);
        //InfoExam infoExam = infoViewExamByDayAndShift.getInfoExam();
        ContextUtils.setExecutionCourseContext(request);
        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) request
                .getAttribute(SessionConstants.EXECUTION_COURSE);

        Season oldExamsSeason = new Season(new Integer(request.getParameter("oldExamSeason")));

        Object args1[] = { infoExecutionCourse.getSigla(), oldExamsSeason,
                infoExecutionCourse.getInfoExecutionPeriod() };
        InfoExam infoExam = null;
        InfoViewExamByDayAndShift infoViewExamByDayAndShift = null;
        try {
            infoViewExamByDayAndShift = ((InfoViewExamByDayAndShift) ServiceUtils.executeService(
                    userView, "ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriod", args1));
            infoExam = infoViewExamByDayAndShift.getInfoExam();
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        String[] rooms = (String[]) editExamRoomsForm.get("selectedRooms");
        List roomsToSet = new ArrayList();
        for (int i = 0; i < rooms.length; i++) {
            roomsToSet.add(new Integer(rooms[i]));
        }

        Object[] args = { infoExam, roomsToSet };
        try {
            infoExam = (InfoExam) ServiceManagerServiceFactory.executeService(userView, "EditExamRooms",
                    args);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        infoViewExamByDayAndShift.setInfoExam(infoExam);
        //session.removeAttribute(SessionConstants.INFO_EXAMS_KEY);
        request.setAttribute(SessionConstants.INFO_EXAMS_KEY, infoViewExamByDayAndShift);

        List horas = Util.getExamShifts();
        request.setAttribute(SessionConstants.LABLELIST_HOURS, horas);

        List daysOfMonth = Util.getDaysOfMonth();
        request.setAttribute(SessionConstants.LABLELIST_DAYSOFMONTH, daysOfMonth);

        List monthsOfYear = Util.getMonthsOfYear();
        request.setAttribute(SessionConstants.LABLELIST_MONTHSOFYEAR, monthsOfYear);

        List examSeasons = Util.getExamSeasons();
        request.setAttribute(SessionConstants.LABLELIST_SEASONS, examSeasons);

        String input = request.getParameter("input");
        request.setAttribute(SessionConstants.NEXT_PAGE, input);

        return mapping.findForward("ViewEditExamForm");
    }

}