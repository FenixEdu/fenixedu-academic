/**
 * Project Sop 
 * 
 * Package presentationTier.Action.sop
 * 
 * Created on 2003/03/21
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewExamByDayAndShift;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixDateAndTimeAndCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.Util;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;
import net.sourceforge.fenixedu.util.Season;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

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

        Map roomsHashTable = new HashMap();
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
        sortedRooms.addAll(roomsHashTable.values());

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