package ServidorApresentacao.Action.publico;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.ISiteComponent;
import DataBeans.InfoEvaluationMethod;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
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
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
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
	 /*   InfoExecutionPeriod infoExecutionPeriod =
		   (InfoExecutionPeriod) request.getAttribute(
			   SessionConstants.EXECUTION_PERIOD);
	    InfoExecutionDegree infoExecutionDegree =
				   (InfoExecutionDegree) request.getAttribute(
					   SessionConstants.EXECUTION_DEGREE);
	    if (infoExecutionDegree == null) {
			HttpSession session = request.getSession();
			infoExecutionDegree = (InfoExecutionDegree)session.getAttribute(SessionConstants.EXECUTION_DEGREE);
	    }
	    
	    String shift = request.getParameter("shift");
	    if (shift == null)
			shift = (String) request.getAttribute("shift");
	    if (shift == null)
			shift= new String("false");
			
	    	request.setAttribute("shift",shift);
	
	    Integer executionPeriodOId = getFromRequest("executionPeriodOID", request);
		Integer degreeId = getFromRequest("degreeID", request);
        if (degreeId == null )
        	degreeId = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getIdInternal();
		request.setAttribute("degreeID", degreeId);
		
		Integer executionDegreeId = getFromRequest("executionDegreeID", request);
		if (executionDegreeId == null)
			executionDegreeId = infoExecutionDegree.getIdInternal();
		request.setAttribute("executionDegreeID", executionDegreeId);
		Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
		if (degreeCurricularPlanId == null)
			degreeCurricularPlanId = infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal();
		request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);*/
		setFromRequest(request);

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
		setFromRequest(request);
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
            request.setAttribute("sigla", ((InfoSiteCommon) siteView.getCommonComponent()).getExecutionCourse().getSigla());
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
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String roomName = request.getParameter("roomName");
        if (roomName == null)
        {
            roomName = (String) request.getAttribute("roomName");
        }
        request.setAttribute("roomName", roomName);
        RoomKey roomKey = null;

        if (roomName != null)
        {

            roomKey = new RoomKey(roomName);

/*            Integer objectCode = null;
            String objectCodeString = request.getParameter("objectCode");
            if (objectCodeString == null)
            {
                objectCodeString = (String) request.getAttribute("objectCode");
            }
            objectCode = new Integer(objectCodeString);
*/          
            ISiteComponent bodyComponent = new InfoSiteRoomTimeTable();
			DynaActionForm indexForm = (DynaActionForm) form;
			Integer indexWeek = (Integer) indexForm.get("indexWeek");
			Calendar today = Calendar.getInstance();
			ArrayList weeks = new ArrayList();
            try {
                //              weeks
                InfoExecutionPeriod executionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(
                        userView, "ReadCurrentExecutionPeriod", new Object[] {});
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

                request.setAttribute(SessionConstants.LABELLIST_WEEKS, weeksLabelValueList);
            } catch (FenixServiceException e) {
                throw new FenixActionException();
            }
			if (indexWeek != null)
			{
                today = (Calendar) weeks.get(indexWeek.intValue()); 			    
			}
            Object[] args = { bodyComponent, roomKey, today };

            try
            {
                SiteView siteView =
                    (SiteView) ServiceUtils.executeService(null, "RoomSiteComponentService", args);

                request.setAttribute("siteView", siteView);
//                request.setAttribute("objectCode", objectCode);

            } catch (NonExistingServiceException e)
            {
                throw new NonExistingActionException(e);
            } catch (FenixServiceException e)
            {
                throw new FenixActionException(e);
            }
            return mapping.findForward("roomViewer");
        } 
            throw new FenixActionException();
        
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
	private Integer getFromRequest(String parameter, HttpServletRequest request)
	{
		Integer parameterCode = null;
		String parameterCodeString = request.getParameter(parameter);
		if (parameterCodeString == null)
		{
			parameterCodeString = (String) request.getAttribute(parameter);
		}
		if (parameterCodeString != null)
		{
			try
			{
				parameterCode = new Integer(parameterCodeString);
			}
			catch (Exception exception)
			{
				return null;
			}
		}
		return parameterCode;
	}
	private void setFromRequest(HttpServletRequest request) {
		InfoExecutionDegree infoExecutionDegree =
						  (InfoExecutionDegree) request.getAttribute(
							  SessionConstants.EXECUTION_DEGREE);
			   if (infoExecutionDegree == null) {
				   HttpSession session = request.getSession();
				   infoExecutionDegree = (InfoExecutionDegree)session.getAttribute(SessionConstants.EXECUTION_DEGREE);
			   }
	    
			   String shift = request.getParameter("shift");
			   if (shift == null)
				   shift = (String) request.getAttribute("shift");
			   if (shift == null)
				   shift= new String("false");
			
				   request.setAttribute("shift",shift);
	
		/*	   Integer degreeId = getFromRequest("degreeID", request);
			   if (degreeId == null )
				   degreeId = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getIdInternal();
			   request.setAttribute("degreeID", degreeId);
		
			   Integer executionDegreeId = getFromRequest("executionDegreeID", request);
			   if (executionDegreeId == null)
				   executionDegreeId = infoExecutionDegree.getIdInternal();
			   request.setAttribute("executionDegreeID", executionDegreeId);
			   Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
			   if (degreeCurricularPlanId == null)
				   degreeCurricularPlanId = infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal();
			   request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);*/
	}

}