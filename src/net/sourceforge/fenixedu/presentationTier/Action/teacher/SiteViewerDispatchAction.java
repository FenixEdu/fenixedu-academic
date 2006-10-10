package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluationMethod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteAssociatedCurricularCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteBibliography;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteFirstPage;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteObjectives;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteProgram;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteProjects;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteRoomTimeTable;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShifts;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShiftsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTimetable;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SiteViewerDispatchAction extends FenixContextDispatchAction
{

    public ActionForward firstPage(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException, FenixFilterException
    {
		
        ISiteComponent firstPageComponent = new InfoSiteFirstPage();
		String objectCodeString = request.getParameter("objectCode");
        if (objectCodeString == null)
        {
            objectCodeString = (String) request.getAttribute("objectCode");
        }
		Integer infoExecutionCourseCode = new Integer(objectCodeString);
	
		setFromRequest(request);

        readSiteView(request, firstPageComponent, infoExecutionCourseCode, null, null);
        return mapping.findForward("sucess");
    }
    
    public ActionForward objectives(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException, FenixFilterException
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
        throws FenixActionException, FenixFilterException
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
        throws FenixActionException, FenixFilterException
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
        throws FenixActionException, FenixFilterException
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
        throws FenixActionException, FenixFilterException
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
        throws FenixActionException, FenixFilterException
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
        throws FenixActionException, FenixFilterException
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
        throws FenixActionException, FenixFilterException
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
        throws FenixActionException, FenixFilterException
    {

        String indexString = request.getParameter("index");
        Integer sectionIndex = new Integer(indexString);

        ISiteComponent sectionComponent = new InfoSiteSection();
        readSiteView(request, sectionComponent, null, sectionIndex, null);

        return mapping.findForward("sucess");
    }

    public ActionForward curricularCourse(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException, FenixFilterException
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
        throws FenixActionException, FenixFilterException
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
        throws FenixActionException, FenixFilterException
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
        throws FenixActionException, FenixFilterException
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
        throws FenixActionException, FenixFilterException
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
        } 
            throw new FenixActionException();
        
    }

    private ExecutionCourseSiteView readGroupView(
        HttpServletRequest request,
        ISiteComponent firstPageComponent,
        Integer infoExecutionCourseCode,
        Integer groupPropertiesCode,
        Integer studentGroupCode)
        throws FenixActionException, FenixFilterException
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