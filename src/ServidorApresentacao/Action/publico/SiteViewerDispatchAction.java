package ServidorApresentacao.Action.publico;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.ISiteComponent;
import DataBeans.InfoEvaluationMethod;
import DataBeans.InfoSiteAnnouncement;
import DataBeans.InfoSiteAssociatedCurricularCourses;
import DataBeans.InfoSiteBibliography;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteCurricularCourse;
import DataBeans.InfoSiteEvaluation;
import DataBeans.InfoSiteFirstPage;
import DataBeans.InfoSiteObjectives;
import DataBeans.InfoSiteProgram;
import DataBeans.InfoSiteProjects;
import DataBeans.InfoSiteRoomTimeTable;
import DataBeans.InfoSiteSection;
import DataBeans.InfoSiteShifts;
import DataBeans.InfoSiteShiftsAndGroups;
import DataBeans.InfoSiteStudentGroup;
import DataBeans.InfoSiteSummaries;
import DataBeans.InfoSiteTimetable;
import DataBeans.RoomKey;
import DataBeans.SiteView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import Util.TipoAula;

public class SiteViewerDispatchAction extends FenixContextDispatchAction
{

    public ActionForward firstPage(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        ISiteComponent firstPageComponent = new InfoSiteFirstPage();
        String objectCodeString = request.getParameter("objectCode");
        if (objectCodeString == null)
        {
            objectCodeString = (String) request.getAttribute("objectCode");
        }
        Integer infoExecutionCourseCode = new Integer(objectCodeString);

        readSiteView(request, firstPageComponent, infoExecutionCourseCode, null, null);
        return mapping.findForward("sucess");
    }

    public ActionForward announcements(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        ISiteComponent announcementsComponent = new InfoSiteAnnouncement();
        readSiteView(request, announcementsComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward objectives(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        ISiteComponent objectivesComponent = new InfoSiteObjectives();
        readSiteView(request, objectivesComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward program(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        ISiteComponent programComponent = new InfoSiteProgram();
        readSiteView(request, programComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward evaluationMethod(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        ISiteComponent evaluationComponent = new InfoEvaluationMethod();
        readSiteView(request, evaluationComponent, null, null, null);

        return mapping.findForward("sucess");

        //		ISiteComponent evaluationComponent = new InfoSiteEvaluationMethods();
        //		readSiteView(request, evaluationComponent, null, null, null);
        //
        //		return mapping.findForward("sucess");
    }

    public ActionForward bibliography(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        ISiteComponent bibliographyComponent = new InfoSiteBibliography();
        readSiteView(request, bibliographyComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward curricularCourses(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        ISiteComponent curricularCoursesComponent = new InfoSiteAssociatedCurricularCourses();
        readSiteView(request, curricularCoursesComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward timeTable(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        ISiteComponent timeTableComponent = new InfoSiteTimetable();
        readSiteView(request, timeTableComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward shifts(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        ISiteComponent shiftsComponent = new InfoSiteShifts();
        readSiteView(request, shiftsComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward evaluation(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        ISiteComponent evaluationComponent = new InfoSiteEvaluation();
        readSiteView(request, evaluationComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward section(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        String indexString = request.getParameter("index");
        Integer sectionIndex = new Integer(indexString);

        ISiteComponent sectionComponent = new InfoSiteSection();
        readSiteView(request, sectionComponent, null, sectionIndex, null);

        return mapping.findForward("sucess");
    }

    public ActionForward summaries(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException
    {

        ISiteComponent summariesComponent = new InfoSiteSummaries();
        
        Integer lessonType = null;
        if (request.getParameter("bySummaryType") != null
                && request.getParameter("bySummaryType").length() > 0)
        {
            lessonType = new Integer(request.getParameter("bySummaryType"));
            ((InfoSiteSummaries) summariesComponent).setLessonType(lessonType);
        }

        Integer shiftId = null;
        if (request.getParameter("byShift") != null && request.getParameter("byShift").length() > 0)
        {
            shiftId = new Integer(request.getParameter("byShift"));
            ((InfoSiteSummaries) summariesComponent).setShiftId(shiftId);
        }

        Integer professorShiftId = null;
        if (request.getParameter("byTeacher") != null && request.getParameter("byTeacher").length() > 0)
        {
            professorShiftId = new Integer(request.getParameter("byTeacher"));
            ((InfoSiteSummaries) summariesComponent).setTeacherId(professorShiftId);
        }        
        
        SiteView siteView = readSiteView(request, summariesComponent, null, null, null);

        Collections.sort(((InfoSiteSummaries) ((ExecutionCourseSiteView) siteView).getComponent())
                .getInfoSummaries(), Collections.reverseOrder());
        request.setAttribute("siteView", siteView);

        return mapping.findForward("sucess");

    }

    /**
     * @param typeFilter
     * @return
     */
    private TipoAula mapFromStringToLessonType(String typeFilter)
    {
        TipoAula result = null;
        if (typeFilter.equals("T"))
        {
            result = new TipoAula(TipoAula.TEORICA);
        } else if (typeFilter.equals("P"))
        {
            result = new TipoAula(TipoAula.PRATICA);
        } else if (typeFilter.equals("TP"))
        {
            result = new TipoAula(TipoAula.TEORICO_PRATICA);
        } else if (typeFilter.equals("L"))
        {
            result = new TipoAula(TipoAula.LABORATORIAL);
        }

        return result;
    }

    public ActionForward curricularCourse(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        String curricularCourseIdString = request.getParameter("ccCode");
        if (curricularCourseIdString == null)
        {
            curricularCourseIdString = (String) request.getAttribute("ccCode");
        }
        Integer curricularCourseId = new Integer(curricularCourseIdString);
        ISiteComponent curricularCourseComponent = new InfoSiteCurricularCourse();
        readSiteView(request, curricularCourseComponent, null, null, curricularCourseId);

        return mapping.findForward("sucess");

    }

    public ActionForward viewExecutionCourseProjects(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        ISiteComponent viewProjectsComponent = new InfoSiteProjects();
        readGroupView(request, viewProjectsComponent, null, null, null);
        return mapping.findForward("sucess");

    }

    public ActionForward viewShiftsAndGroupsAction(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        String objectCodeString = null;
        Integer groupPropertiesCode = null;
        objectCodeString = request.getParameter("groupProperties");
        if (objectCodeString == null)
            groupPropertiesCode = (Integer) request.getAttribute("groupProperties");

        else
            groupPropertiesCode = new Integer(objectCodeString);

        ISiteComponent viewShiftsAndGroups = new InfoSiteShiftsAndGroups();
        readGroupView(request, viewShiftsAndGroups, null, groupPropertiesCode, null);
        request.setAttribute("groupProperties", groupPropertiesCode);

        return mapping.findForward("sucess");
    }

    public ActionForward viewStudentGroupInformationAction(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        String studentGroupCodeString = request.getParameter("studentGroupCode");

        Integer studentGroupCode = new Integer(studentGroupCodeString);

        ISiteComponent viewStudentGroup = new InfoSiteStudentGroup();

        readGroupView(request, viewStudentGroup, null, null, studentGroupCode);

        return mapping.findForward("sucess");

    }

    private SiteView readSiteView(
        HttpServletRequest request,
        ISiteComponent firstPageComponent,
        Integer infoExecutionCourseCode,
        Integer sectionIndex,
        Integer curricularCourseId)
        throws FenixActionException
    {

        Integer objectCode = null;
        if (infoExecutionCourseCode == null)
        {
            String objectCodeString = request.getParameter("objectCode");
            if (objectCodeString == null)
            {
                objectCodeString = (String) request.getAttribute("objectCode");

            }
            objectCode = new Integer(objectCodeString);
        }

        ISiteComponent commonComponent = new InfoSiteCommon();

        Object[] args =
            {
                commonComponent,
                firstPageComponent,
                objectCode,
                infoExecutionCourseCode,
                sectionIndex,
                curricularCourseId };

        try
        {
            ExecutionCourseSiteView siteView =
                (ExecutionCourseSiteView) ServiceUtils.executeService(
                    null,
                    "ExecutionCourseSiteComponentService",
                    args);
                    
            if(siteView != null) {      
            if (infoExecutionCourseCode != null)
            {
                request.setAttribute(
                    "objectCode",
                    ((InfoSiteFirstPage) siteView.getComponent()).getSiteIdInternal());
            } else
            {
                request.setAttribute("objectCode", objectCode);
            }
            request.setAttribute("siteView", siteView);
            request.setAttribute(
                "executionCourseCode",
                ((InfoSiteCommon) siteView.getCommonComponent()).getExecutionCourse().getIdInternal());
            request.setAttribute(
                "executionPeriodCode",
                ((InfoSiteCommon) siteView.getCommonComponent())
                    .getExecutionCourse()
                    .getInfoExecutionPeriod()
                    .getIdInternal());
            if (siteView.getComponent() instanceof InfoSiteSection)
            {
                request.setAttribute(
                    "infoSection",
                    ((InfoSiteSection) siteView.getComponent()).getSection());
            }
}
            return siteView;
        } catch (NonExistingServiceException e)
        {
            throw new NonExistingActionException("A disciplina", e);
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
    }

    public ActionForward roomViewer(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        String roomName = request.getParameter("roomName");
        if (roomName == null)
        {
            roomName = (String) request.getAttribute("roomName");
        }

        RoomKey roomKey = null;

        if (roomName != null)
        {

            roomKey = new RoomKey(roomName);

            Integer objectCode = null;
            String objectCodeString = request.getParameter("objectCode");
            if (objectCodeString == null)
            {
                objectCodeString = (String) request.getAttribute("objectCode");
            }
            objectCode = new Integer(objectCodeString);
            ISiteComponent bodyComponent = new InfoSiteRoomTimeTable();

            Object[] args = { bodyComponent, roomKey, objectCode };

            try
            {
                SiteView siteView =
                    (SiteView) ServiceUtils.executeService(null, "RoomSiteComponentService", args);

                request.setAttribute("siteView", siteView);
                request.setAttribute("objectCode", objectCode);

            } catch (NonExistingServiceException e)
            {
                throw new NonExistingActionException(e);
            } catch (FenixServiceException e)
            {
                throw new FenixActionException(e);
            }
            return mapping.findForward("roomViewer");
        } else
        {
            throw new FenixActionException();
        }
    }

    private ExecutionCourseSiteView readGroupView(
        HttpServletRequest request,
        ISiteComponent firstPageComponent,
        Integer infoExecutionCourseCode,
        Integer groupPropertiesCode,
        Integer studentGroupCode)
        throws FenixActionException
    {

        Integer objectCode = null;
        if (infoExecutionCourseCode == null)
        {
            String objectCodeString = request.getParameter("objectCode");
            if (objectCodeString == null)
            {
                objectCodeString = (String) request.getAttribute("objectCode");
            }
            objectCode = new Integer(objectCodeString);
        }

        ISiteComponent commonComponent = new InfoSiteCommon();

        Object[] args =
            { commonComponent, firstPageComponent, objectCode, groupPropertiesCode, studentGroupCode };
        ExecutionCourseSiteView siteView = null;
        try
        {
            siteView =
                (ExecutionCourseSiteView) ServiceUtils.executeService(
                    null,
                    "GroupSiteComponentService",
                    args);

            if (infoExecutionCourseCode != null)
            {
                request.setAttribute(
                    "objectCode",
                    ((InfoSiteFirstPage) siteView.getComponent()).getSiteIdInternal());
            } else
            {
                request.setAttribute("objectCode", objectCode);
            }
            request.setAttribute("siteView", siteView);
            request.setAttribute(
                "executionCourseCode",
                ((InfoSiteCommon) siteView.getCommonComponent()).getExecutionCourse().getIdInternal());

            request.setAttribute(
                "executionPeriodCode",
                ((InfoSiteCommon) siteView.getCommonComponent())
                    .getExecutionCourse()
                    .getInfoExecutionPeriod()
                    .getIdInternal());
            if (siteView.getComponent() instanceof InfoSiteSection)
            {
                request.setAttribute(
                    "infoSection",
                    ((InfoSiteSection) siteView.getComponent()).getSection());
            }

        } catch (NonExistingServiceException e)
        {
            throw new NonExistingActionException("A disciplina", e);
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        return siteView;
    }

}