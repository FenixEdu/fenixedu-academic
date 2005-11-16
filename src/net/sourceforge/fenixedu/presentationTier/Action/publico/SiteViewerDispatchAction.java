package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluationMethod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteAssociatedCurricularCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteBibliography;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluationMarks;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluations;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteFirstPage;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteObjectives;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteProgram;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteProjects;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteRoomTimeTable;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShifts;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShiftsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSummaries;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTimetable;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

public class SiteViewerDispatchAction extends FenixContextDispatchAction {

    public ActionForward firstPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException,
            FenixFilterException {

        ISiteComponent firstPageComponent = new InfoSiteFirstPage();
        String objectCodeString = request.getParameter("objectCode");
        if (objectCodeString == null) {
            objectCodeString = (String) request.getAttribute("objectCode");
        }
        Integer infoExecutionCourseCode = new Integer(objectCodeString);
        setFromRequest(request);

        readSiteView(request, firstPageComponent, infoExecutionCourseCode, null, null);
        return mapping.findForward("sucess");
    }

    public ActionForward announcements(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        setFromRequest(request);
        ISiteComponent announcementsComponent = new InfoSiteAnnouncement();
        readSiteView(request, announcementsComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward objectives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        setFromRequest(request);
        ISiteComponent objectivesComponent = new InfoSiteObjectives();
        readSiteView(request, objectivesComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward program(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        setFromRequest(request);
        ISiteComponent programComponent = new InfoSiteProgram();
        readSiteView(request, programComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward evaluationMethod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        setFromRequest(request);
        ISiteComponent evaluationComponent = new InfoEvaluationMethod();
        readSiteView(request, evaluationComponent, null, null, null);

        return mapping.findForward("sucess");

    }

    public ActionForward bibliography(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        setFromRequest(request);
        ISiteComponent bibliographyComponent = new InfoSiteBibliography();
        readSiteView(request, bibliographyComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward curricularCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        setFromRequest(request);
        ISiteComponent curricularCoursesComponent = new InfoSiteAssociatedCurricularCourses();
        readSiteView(request, curricularCoursesComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward timeTable(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        setFromRequest(request);
        ISiteComponent timeTableComponent = new InfoSiteTimetable();
        readSiteView(request, timeTableComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward shifts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        setFromRequest(request);
        ISiteComponent shiftsComponent = new InfoSiteShifts();
        readSiteView(request, shiftsComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward evaluations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        setFromRequest(request);
        ISiteComponent evaluationComponent = new InfoSiteEvaluations();
        readSiteView(request, evaluationComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward viewMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        final String evaluationOID = request.getParameter("evaluationOID");

        setFromRequest(request);
        final InfoSiteEvaluationMarks evaluationMarksComponent = new InfoSiteEvaluationMarks();
        evaluationMarksComponent.setEvaluationID(Integer.valueOf(evaluationOID));
        readSiteView(request, evaluationMarksComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward section(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        setFromRequest(request);
        String indexString = request.getParameter("index");
        Integer sectionIndex = new Integer(indexString);

        ISiteComponent sectionComponent = new InfoSiteSection();
        readSiteView(request, sectionComponent, null, sectionIndex, null);

        return mapping.findForward("sucess");
    }

    public ActionForward summaries(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        setFromRequest(request);
        ISiteComponent summariesComponent = new InfoSiteSummaries();

        ShiftType lessonType = null;
        if (request.getParameter("bySummaryType") != null
                && request.getParameter("bySummaryType").length() > 0
                && !request.getParameter("bySummaryType").equals("0")) {

            lessonType = ShiftType.valueOf(request.getParameter("bySummaryType"));
            ((InfoSiteSummaries) summariesComponent).setShiftType(lessonType);
        }

        Integer shiftId = null;
        if (request.getParameter("byShift") != null && request.getParameter("byShift").length() > 0) {
            shiftId = new Integer(request.getParameter("byShift"));
            ((InfoSiteSummaries) summariesComponent).setShiftId(shiftId);
        }

        Integer professorShiftId = null;
        if (request.getParameter("byTeacher") != null && request.getParameter("byTeacher").length() > 0) {
            professorShiftId = new Integer(request.getParameter("byTeacher"));
            ((InfoSiteSummaries) summariesComponent).setTeacherId(professorShiftId);
        }

        SiteView siteView = readSiteView(request, summariesComponent, null, null, null);


        final InfoSiteSummaries infoSiteSummaries = (InfoSiteSummaries) ((ExecutionCourseSiteView) siteView)
                .getComponent();                
        
        for (final InfoShift infoShift : (List<InfoShift>) infoSiteSummaries.getInfoShifts()) {
            Collections.sort(infoShift.getInfoLessons());
        }
        Collections.sort(infoSiteSummaries.getInfoSummaries(), Collections.reverseOrder());
        Collections.sort(infoSiteSummaries.getInfoShifts(), new BeanComparator("lessons"));
        
        request.setAttribute("siteView", siteView);

        return mapping.findForward("sucess");

    }

    public ActionForward curricularCourse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        setFromRequest(request);
        String curricularCourseIdString = request.getParameter("ccCode");
        if (curricularCourseIdString == null) {
            curricularCourseIdString = (String) request.getAttribute("ccCode");
        }
        Integer curricularCourseId = new Integer(curricularCourseIdString);
        ISiteComponent curricularCourseComponent = new InfoSiteCurricularCourse();
        readSiteView(request, curricularCourseComponent, null, null, curricularCourseId);

        return mapping.findForward("sucess");

    }

    public ActionForward viewExecutionCourseProjects(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        setFromRequest(request);
        ISiteComponent viewProjectsComponent = new InfoSiteProjects();
        readGroupView(request, viewProjectsComponent, null, null, null, null, null);
        return mapping.findForward("sucess");

    }

    public ActionForward viewShiftsAndGroupsAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        setFromRequest(request);
        String objectCodeString = null;
        Integer groupPropertiesCode = null;
        objectCodeString = request.getParameter("groupProperties");
        if (objectCodeString == null)
            groupPropertiesCode = (Integer) request.getAttribute("groupProperties");

        else
            groupPropertiesCode = new Integer(objectCodeString);

        ISiteComponent viewShiftsAndGroups = new InfoSiteShiftsAndGroups();
        readGroupView(request, viewShiftsAndGroups, null, groupPropertiesCode, null, null, null);
        request.setAttribute("groupProperties", groupPropertiesCode);

        return mapping.findForward("sucess");
    }

    public ActionForward viewStudentsAndGroupsByShiftAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        setFromRequest(request);
        String objectCodeString = null;
        String shiftCodeString = null;

        Integer groupPropertiesCode = null;
        Integer shiftCode = null;

        objectCodeString = request.getParameter("groupProperties");
        shiftCodeString = request.getParameter("shift");

        if (objectCodeString == null)
            groupPropertiesCode = (Integer) request.getAttribute("groupProperties");

        else
            groupPropertiesCode = new Integer(objectCodeString);

        if (shiftCodeString == null)
            shiftCode = (Integer) request.getAttribute("shift");

        else
            shiftCode = new Integer(shiftCodeString);

        ISiteComponent infoSiteStudentsAndGroups = new InfoSiteStudentsAndGroups();
        readGroupView(request, infoSiteStudentsAndGroups, null, groupPropertiesCode, null, shiftCode,
                new Integer(1));
        request.setAttribute("groupProperties", groupPropertiesCode);
        request.setAttribute("shift", shiftCode);

        request.setAttribute("ShiftChosenType", new Integer(1));

        return mapping.findForward("sucess");
    }

    public ActionForward viewStudentsAndGroupsWithoutShiftAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        setFromRequest(request);
        String objectCodeString = null;
        Integer groupPropertiesCode = null;
        objectCodeString = request.getParameter("groupProperties");

        if (objectCodeString == null)
            groupPropertiesCode = (Integer) request.getAttribute("groupProperties");

        else
            groupPropertiesCode = new Integer(objectCodeString);

        ISiteComponent infoSiteStudentsAndGroups = new InfoSiteStudentsAndGroups();
        readGroupView(request, infoSiteStudentsAndGroups, null, groupPropertiesCode, null, null,
                new Integer(2));
        request.setAttribute("groupProperties", groupPropertiesCode);

        request.setAttribute("ShiftChosenType", new Integer(2));

        return mapping.findForward("sucess");
    }

    public ActionForward viewAllStudentsAndGroupsAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        setFromRequest(request);
        String objectCodeString = null;
        Integer groupPropertiesCode = null;
        objectCodeString = request.getParameter("groupProperties");

        if (objectCodeString == null)
            groupPropertiesCode = (Integer) request.getAttribute("groupProperties");

        else
            groupPropertiesCode = new Integer(objectCodeString);

        ISiteComponent infoSiteStudentsAndGroups = new InfoSiteStudentsAndGroups();
        readGroupView(request, infoSiteStudentsAndGroups, null, groupPropertiesCode, null, null,
                new Integer(3));
        request.setAttribute("groupProperties", groupPropertiesCode);

        request.setAttribute("ShiftChosenType", new Integer(3));
        return mapping.findForward("sucess");
    }

    public ActionForward viewStudentGroupInformationAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        setFromRequest(request);
        String studentGroupCodeString = request.getParameter("studentGroupCode");

        Integer studentGroupCode = new Integer(studentGroupCodeString);

        ISiteComponent viewStudentGroup = new InfoSiteStudentGroup();

        readGroupView(request, viewStudentGroup, null, null, studentGroupCode, null, null);

        return mapping.findForward("sucess");

    }

    private SiteView readSiteView(HttpServletRequest request, ISiteComponent firstPageComponent,
            Integer infoExecutionCourseCode, Integer sectionIndex, Integer curricularCourseId)
            throws FenixActionException, FenixFilterException, FenixFilterException {
        Integer objectCode = null;
        if (infoExecutionCourseCode == null) {
            String objectCodeString = request.getParameter("objectCode");
            if (objectCodeString == null) {
                objectCodeString = (String) request.getAttribute("objectCode");

            }
            objectCode = new Integer(objectCodeString);
        }

        ISiteComponent commonComponent = new InfoSiteCommon();

        Object[] args = { commonComponent, firstPageComponent, objectCode, infoExecutionCourseCode,
                sectionIndex, curricularCourseId };

        try {
            ExecutionCourseSiteView siteView = (ExecutionCourseSiteView) ServiceUtils.executeService(
                    null, "ExecutionCourseSiteComponentService", args);

            List curricularCoursesByDegree = ((InfoSiteCommon) siteView.getCommonComponent())
                    .getAssociatedDegreesByDegree();
            Collections.sort(curricularCoursesByDegree, new BeanComparator(
                    "infoDegreeCurricularPlan.infoDegree.sigla"));

            if (siteView != null) {
                if (infoExecutionCourseCode != null) {
                    request.setAttribute("objectCode", ((InfoSiteFirstPage) siteView.getComponent())
                            .getSiteIdInternal());
                } else {
                    request.setAttribute("objectCode", objectCode);
                }

                request.setAttribute("siteView", siteView);
                request.setAttribute("executionCourseCode", ((InfoSiteCommon) siteView
                        .getCommonComponent()).getExecutionCourse().getIdInternal());
                request.setAttribute("sigla", ((InfoSiteCommon) siteView.getCommonComponent())
                        .getExecutionCourse().getSigla());
                request.setAttribute("executionPeriodCode", ((InfoSiteCommon) siteView
                        .getCommonComponent()).getExecutionCourse().getInfoExecutionPeriod()
                        .getIdInternal());

                if (siteView.getComponent() instanceof InfoSiteSection) {
                    request.setAttribute("infoSection", ((InfoSiteSection) siteView.getComponent())
                            .getSection());
                }
            }
            return siteView;
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("A disciplina", e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
    }

    public ActionForward roomViewer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String roomName = request.getParameter("roomName");
        if (roomName == null) {
            roomName = (String) request.getAttribute("roomName");
        }
        request.setAttribute("roomName", roomName);
        RoomKey roomKey = null;

        if (roomName != null) {
            roomKey = new RoomKey(roomName);

            ISiteComponent bodyComponent = new InfoSiteRoomTimeTable();
            DynaActionForm indexForm = (DynaActionForm) form;
            Integer indexWeek = (Integer) indexForm.get("indexWeek");
            //Integer executionPeriodID = (Integer) indexForm.get("selectedExecutionPeriodID");
            String executionPeriodIDString = request.getParameter("selectedExecutionPeriodID");
            if (executionPeriodIDString == null) {
            	executionPeriodIDString = (String) request.getAttribute("selectedExecutionPeriodID");
            }
            Integer executionPeriodID = (executionPeriodIDString != null) ? Integer.valueOf(executionPeriodIDString) : null;
            if (executionPeriodID == null) {
            	try {
            		//executionPeriodID = (Integer) indexForm.get("selectedExecutionPeriodID");
            		executionPeriodID = Integer.valueOf((String) indexForm.get("selectedExecutionPeriodID"));
            	} catch (IllegalArgumentException ex) {
            	}
            }
            Calendar today = Calendar.getInstance();
            ArrayList weeks = new ArrayList();

            InfoExecutionPeriod executionPeriod;
            if (executionPeriodID == null) {
                executionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(userView, "ReadCurrentExecutionPeriod", new Object[] {});
                try {
                	indexForm.set("selectedExecutionPeriodID", executionPeriod.getIdInternal().toString());
                } catch (IllegalArgumentException ex) {
                }
            } else {
                executionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(userView, "ReadExecutionPeriodByOID", new Object[] { executionPeriodID });
            }

            try {
                // weeks
                Calendar begin = Calendar.getInstance();
                begin.setTime(executionPeriod.getBeginDate());
                Calendar end = Calendar.getInstance();
                end.setTime(executionPeriod.getEndDate());
                ArrayList weeksLabelValueList = new ArrayList();
                begin.add(Calendar.DATE, Calendar.MONDAY - begin.get(Calendar.DAY_OF_WEEK));
                int i = 0;
                boolean selectedWeek = false;
                while (begin.before(end) || begin.before(Calendar.getInstance())) {
                    Calendar day = Calendar.getInstance();
                    day.setTimeInMillis(begin.getTimeInMillis());
                    weeks.add(day);
                    String beginWeekString = DateFormatUtils.format(begin.getTime(), "dd/MM/yyyy");
                    begin.add(Calendar.DATE, 5);
                    String endWeekString = DateFormatUtils.format(begin.getTime(), "dd/MM/yyyy");
                    weeksLabelValueList.add(new LabelValueBean(beginWeekString + " - " + endWeekString,
                            new Integer(i).toString()));
                    begin.add(Calendar.DATE, 2);
                    if (!selectedWeek && indexWeek == null && Calendar.getInstance().before(begin)) {
                        indexForm.set("indexWeek", new Integer(i));
                        selectedWeek = true;
                    }
                    i++;
                }

                final List<IExecutionPeriod> executionPeriods = (List<IExecutionPeriod>)
                    ServiceUtils.executeService(userView, "ReadAllDomainObjects", new Object[] {
                        ExecutionPeriod.class
                });
                final List<LabelValueBean> executionPeriodLabelValueBeans = new ArrayList<LabelValueBean>();
                for (final IExecutionPeriod ep : executionPeriods) {
                    executionPeriodLabelValueBeans.add(new LabelValueBean(
                    		ep.getName() + " " + ep.getExecutionYear().getYear(), ep.getIdInternal().toString()));
                }
                request.setAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD, executionPeriodLabelValueBeans);

                request.setAttribute(SessionConstants.LABELLIST_WEEKS, weeksLabelValueList);
            } catch (FenixServiceException e) {
                throw new FenixActionException();
            }
            if (indexWeek != null) {
                today = (Calendar) weeks.get(indexWeek.intValue());
            }
            Object[] args = { bodyComponent, roomKey, today, executionPeriodID };

            try {
                SiteView siteView = (SiteView) ServiceUtils.executeService(null,
                        "RoomSiteComponentServiceByExecutionPeriodID", args);

                request.setAttribute("siteView", siteView);

            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException(e);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
            return mapping.findForward("roomViewer");
        }
        throw new FenixActionException();

    }

    private ExecutionCourseSiteView readGroupView(HttpServletRequest request,
            ISiteComponent firstPageComponent, Integer infoExecutionCourseCode,
            Integer groupPropertiesCode, Integer studentGroupCode, Integer shiftCode, Integer value)
            throws FenixActionException, FenixFilterException, FenixFilterException {

        Integer objectCode = null;
        if (infoExecutionCourseCode == null) {
            String objectCodeString = request.getParameter("objectCode");
            if (objectCodeString == null) {
                objectCodeString = (String) request.getAttribute("objectCode");
            }
            objectCode = new Integer(objectCodeString);
        }

        ISiteComponent commonComponent = new InfoSiteCommon();

        Object[] args = { commonComponent, firstPageComponent, objectCode, groupPropertiesCode,
                studentGroupCode, shiftCode, value };
        ExecutionCourseSiteView siteView = null;
        try {
            siteView = (ExecutionCourseSiteView) ServiceUtils.executeService(null,
                    "GroupSiteComponentService", args);

            if (infoExecutionCourseCode != null) {
                request.setAttribute("objectCode", ((InfoSiteFirstPage) siteView.getComponent())
                        .getSiteIdInternal());
            } else {
                request.setAttribute("objectCode", objectCode);
            }
            request.setAttribute("siteView", siteView);
            request.setAttribute("executionCourseCode", ((InfoSiteCommon) siteView.getCommonComponent())
                    .getExecutionCourse().getIdInternal());

            request.setAttribute("executionPeriodCode", ((InfoSiteCommon) siteView.getCommonComponent())
                    .getExecutionCourse().getInfoExecutionPeriod().getIdInternal());
            if (siteView.getComponent() instanceof InfoSiteSection) {
                request.setAttribute("infoSection", ((InfoSiteSection) siteView.getComponent())
                        .getSection());
            }

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("A disciplina", e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return siteView;
    }

    private void setFromRequest(HttpServletRequest request) {

        String shift = request.getParameter("shift");
        if (shift == null)
            shift = (String) request.getAttribute("shift");
        if (shift == null)
            shift = new String("false");

        request.setAttribute("shift", shift);

    }

}