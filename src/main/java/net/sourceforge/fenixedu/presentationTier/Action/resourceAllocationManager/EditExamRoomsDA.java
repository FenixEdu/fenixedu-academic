/**
 * Project Sop 
 * 
 * Package presentationTier.Action.sop
 * 
 * Created on 2003/03/21
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.EditExamRooms;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadEmptyRoomsForExam;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewExamByDayAndShift;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixDateAndTimeAndCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.Util;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;
import net.sourceforge.fenixedu.util.Season;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class EditExamRoomsDA extends
        FenixDateAndTimeAndCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        IUserView userView = UserView.getUser();
        DynaActionForm editExamRoomsForm = (DynaActionForm) form;

        ContextUtils.setExecutionCourseContext(request);
        InfoExecutionCourse infoExecutionCourse =
                (InfoExecutionCourse) request.getAttribute(PresentationConstants.EXECUTION_COURSE);

        Season oldExamsSeason = new Season(new Integer(request.getParameter("oldExamSeason")));

        InfoExam infoExam = null;
        InfoViewExamByDayAndShift infoViewExamByDayAndShift = null;
        infoViewExamByDayAndShift =
                (ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriod.run(infoExecutionCourse.getSigla(),
                        oldExamsSeason, infoExecutionCourse.getInfoExecutionPeriod()));
        infoExam = infoViewExamByDayAndShift.getInfoExam();

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

        List availableRooms;
        try {
            availableRooms = ReadEmptyRoomsForExam.run(infoExam);
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

        request.setAttribute(PresentationConstants.AVAILABLE_ROOMS, sortedRooms);

        request.setAttribute(PresentationConstants.INFO_EXAMS_KEY, infoViewExamByDayAndShift);

        String input = request.getParameter("input");
        request.setAttribute(PresentationConstants.NEXT_PAGE, input);

        return mapping.findForward("ViewSelectRoomsForm");
    }

    public ActionForward select(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        IUserView userView = UserView.getUser();
        DynaActionForm editExamRoomsForm = (DynaActionForm) form;

        ContextUtils.setExecutionCourseContext(request);
        InfoExecutionCourse infoExecutionCourse =
                (InfoExecutionCourse) request.getAttribute(PresentationConstants.EXECUTION_COURSE);

        Season oldExamsSeason = new Season(new Integer(request.getParameter("oldExamSeason")));

        InfoExam infoExam = null;
        InfoViewExamByDayAndShift infoViewExamByDayAndShift = null;
        infoViewExamByDayAndShift =
                (ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriod.run(infoExecutionCourse.getSigla(),
                        oldExamsSeason, infoExecutionCourse.getInfoExecutionPeriod()));
        infoExam = infoViewExamByDayAndShift.getInfoExam();

        String[] rooms = (String[]) editExamRoomsForm.get("selectedRooms");
        List roomsToSet = new ArrayList();
        for (String room : rooms) {
            roomsToSet.add(new Integer(room));
        }

        try {
            infoExam = EditExamRooms.run(infoExam, roomsToSet);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        infoViewExamByDayAndShift.setInfoExam(infoExam);
        request.setAttribute(PresentationConstants.INFO_EXAMS_KEY, infoViewExamByDayAndShift);

        List horas = Util.getExamShifts();
        request.setAttribute(PresentationConstants.LABLELIST_HOURS, horas);

        List daysOfMonth = Util.getDaysOfMonth();
        request.setAttribute(PresentationConstants.LABLELIST_DAYSOFMONTH, daysOfMonth);

        List monthsOfYear = Util.getMonthsOfYear();
        request.setAttribute(PresentationConstants.LABLELIST_MONTHSOFYEAR, monthsOfYear);

        List examSeasons = Util.getExamSeasons();
        request.setAttribute(PresentationConstants.LABLELIST_SEASONS, examSeasons);

        String input = request.getParameter("input");
        request.setAttribute(PresentationConstants.NEXT_PAGE, input);

        return mapping.findForward("ViewEditExamForm");
    }

}