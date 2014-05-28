/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ExecutionCourseSiteComponentService;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteAssociatedCurricularCourses;
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
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SiteViewerDispatchActionNew extends FenixContextDispatchAction {

    public ActionForward firstPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        ISiteComponent firstPageComponent = new InfoSiteFirstPage();
        String objectCodeString = request.getParameter("objectCode");
        if (objectCodeString == null) {
            objectCodeString = (String) request.getAttribute("objectCode");
        }
        String infoExecutionCourseCode = objectCodeString;

        setFromRequest(request);

        readSiteView(request, firstPageComponent, infoExecutionCourseCode, null, null);
        return mapping.findForward("sucess");
    }

    public ActionForward objectives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        ISiteComponent objectivesComponent = new InfoSiteObjectives();
        readSiteView(request, objectivesComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        ISiteComponent programComponent = new InfoSiteProgram();
        readSiteView(request, programComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward curricularCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        ISiteComponent curricularCoursesComponent = new InfoSiteAssociatedCurricularCourses();
        readSiteView(request, curricularCoursesComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward timeTable(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        ISiteComponent timeTableComponent = new InfoSiteTimetable();
        readSiteView(request, timeTableComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward shifts(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        ISiteComponent shiftsComponent = new InfoSiteShifts();
        readSiteView(request, shiftsComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward evaluation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        ISiteComponent evaluationComponent = new InfoSiteEvaluation();
        readSiteView(request, evaluationComponent, null, null, null);

        return mapping.findForward("sucess");
    }

    public ActionForward section(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        String indexString = request.getParameter("index");
        Integer sectionIndex = new Integer(indexString);

        ISiteComponent sectionComponent = new InfoSiteSection();
        readSiteView(request, sectionComponent, null, sectionIndex, null);

        return mapping.findForward("sucess");
    }

    public ActionForward curricularCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String curricularCourseIdString = request.getParameter("ccCode");
        if (curricularCourseIdString == null) {
            curricularCourseIdString = (String) request.getAttribute("ccCode");
        }
        Integer curricularCourseId = new Integer(curricularCourseIdString);
        ISiteComponent curricularCourseComponent = new InfoSiteCurricularCourse();
        readSiteView(request, curricularCourseComponent, null, null, curricularCourseId);

        return mapping.findForward("sucess");

    }

    public ActionForward viewExecutionCourseProjects(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        ISiteComponent viewProjectsComponent = new InfoSiteProjects();
        readGroupView(request, viewProjectsComponent, null, null, null);
        return mapping.findForward("sucess");

    }

    public ActionForward viewShiftsAndGroupsAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String objectCodeString = null;
        Integer groupPropertiesCode = null;
        objectCodeString = request.getParameter("groupProperties");
        if (objectCodeString == null) {
            groupPropertiesCode = (Integer) request.getAttribute("groupProperties");
        } else {
            groupPropertiesCode = new Integer(objectCodeString);
        }

        ISiteComponent viewShiftsAndGroups = new InfoSiteShiftsAndGroups();
        readGroupView(request, viewShiftsAndGroups, null, groupPropertiesCode, null);
        request.setAttribute("groupProperties", groupPropertiesCode);

        return mapping.findForward("sucess");
    }

    public ActionForward viewStudentGroupInformationAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String studentGroupCodeString = request.getParameter("studentGroupCode");

        Integer studentGroupCode = new Integer(studentGroupCodeString);

        ISiteComponent viewStudentGroup = new InfoSiteStudentGroup();

        readGroupView(request, viewStudentGroup, null, null, studentGroupCode);

        return mapping.findForward("sucess");

    }

    private SiteView readSiteView(HttpServletRequest request, ISiteComponent firstPageComponent, String infoExecutionCourseCode,
            Integer sectionIndex, Integer curricularCourseId) throws FenixActionException {
        String objectCode = null;
        if (infoExecutionCourseCode == null) {
            String objectCodeString = request.getParameter("objectCode");
            if (objectCodeString == null) {
                objectCodeString = (String) request.getAttribute("objectCode");

            }
            objectCode = objectCodeString;
        }

        ISiteComponent commonComponent = new InfoSiteCommon();

        try {
            ExecutionCourseSiteView siteView =
                    ExecutionCourseSiteComponentService.runExecutionCourseSiteComponentService(commonComponent,
                            firstPageComponent, objectCode, infoExecutionCourseCode, sectionIndex, curricularCourseId);

            if (siteView != null) {
                if (infoExecutionCourseCode != null) {
                    request.setAttribute("objectCode", ((InfoSiteFirstPage) siteView.getComponent()).getSiteExternalId());
                } else {
                    request.setAttribute("objectCode", objectCode);
                }

                request.setAttribute("siteView", siteView);
                request.setAttribute("executionCourseCode", ((InfoSiteCommon) siteView.getCommonComponent()).getExecutionCourse()
                        .getExternalId());
                request.setAttribute("sigla", ((InfoSiteCommon) siteView.getCommonComponent()).getExecutionCourse().getSigla());
                request.setAttribute("executionPeriodCode", ((InfoSiteCommon) siteView.getCommonComponent()).getExecutionCourse()
                        .getInfoExecutionPeriod().getExternalId());

                if (siteView.getComponent() instanceof InfoSiteSection) {
                    request.setAttribute("infoSection", ((InfoSiteSection) siteView.getComponent()).getSection());
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

        String roomName = request.getParameter("roomName");
        if (roomName == null) {
            roomName = (String) request.getAttribute("roomName");
        }
        // input

        InfoExecutionDegree infoExecutionDegree =
                (InfoExecutionDegree) request.getAttribute(PresentationConstants.EXECUTION_DEGREE);

        String degreeId = getFromRequest("degreeID", request);
        if (degreeId == null) {
            degreeId = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getExternalId();
        }
        request.setAttribute("degreeID", degreeId);
        String executionDegreeId = getFromRequest("executionDegreeID", request);
        if (executionDegreeId == null) {
            executionDegreeId = infoExecutionDegree.getExternalId();
        }
        request.setAttribute("executionDegreeID", executionDegreeId);
        String degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
        if (degreeCurricularPlanId == null) {
            degreeCurricularPlanId = infoExecutionDegree.getInfoDegreeCurricularPlan().getExternalId();
        }
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        request.setAttribute("infoDegreeCurricularPlan", infoExecutionDegree.getInfoDegreeCurricularPlan());

        //

        RoomKey roomKey = null;

        if (roomName != null) {

            roomKey = new RoomKey(roomName);

            Integer objectCode = null;
            String objectCodeString = request.getParameter("objectCode");
            if (objectCodeString == null) {
                objectCodeString = (String) request.getAttribute("objectCode");
            }
            objectCode = new Integer(objectCodeString);
            ISiteComponent bodyComponent = new InfoSiteRoomTimeTable();

            // ****************************************************************
            // The following code was refactored from a berserk call into a not
            // compiling invocation.
            throw new FenixActionException();
            // try {
            // SiteView siteView = (SiteView)
            // RoomSiteComponentService.run(bodyComponent, roomKey, objectCode);
            //
            // request.setAttribute("sigla", ((InfoSiteRoomTimeTable)
            // siteView.getComponent()).getInfoRoom().getNome());
            // request.setAttribute("siteView", siteView);
            // request.setAttribute("objectCode", objectCode);
            //
            // } catch (NonExistingServiceException e) {
            // throw new NonExistingActionException(e);
            // } catch (FenixServiceException e) {
            // throw new FenixActionException(e);
            // }
            //
            // return mapping.findForward("roomViewer");
        }
        throw new FenixActionException();

    }

    private ExecutionCourseSiteView readGroupView(HttpServletRequest request, ISiteComponent firstPageComponent,
            Integer infoExecutionCourseCode, Integer groupPropertiesCode, Integer studentGroupCode) throws FenixActionException {

        Integer objectCode = null;
        if (infoExecutionCourseCode == null) {
            String objectCodeString = request.getParameter("objectCode");
            if (objectCodeString == null) {
                objectCodeString = (String) request.getAttribute("objectCode");
            }
            objectCode = new Integer(objectCodeString);
        }

        ISiteComponent commonComponent = new InfoSiteCommon();

        ExecutionCourseSiteView siteView = null;
        // ****************************************************************
        // The following code was refactored from a berserk call into a not
        // compiling invocation.
        throw new FenixActionException();
        // try {
        // siteView = (ExecutionCourseSiteView)
        // GroupSiteComponentService.run(commonComponent, firstPageComponent,
        // objectCode, groupPropertiesCode, studentGroupCode);
        //
        // if (infoExecutionCourseCode != null) {
        // request.setAttribute("objectCode", ((InfoSiteFirstPage)
        // siteView.getComponent()).getSiteExternalId());
        // } else {
        // request.setAttribute("objectCode", objectCode);
        // }
        // request.setAttribute("siteView", siteView);
        // request.setAttribute("executionCourseCode", ((InfoSiteCommon)
        // siteView.getCommonComponent()).getExecutionCourse()
        // .getExternalId());
        //
        // request.setAttribute("executionPeriodCode", ((InfoSiteCommon)
        // siteView.getCommonComponent()).getExecutionCourse()
        // .getInfoExecutionPeriod().getExternalId());
        // if (siteView.getComponent() instanceof InfoSiteSection) {
        // request.setAttribute("infoSection", ((InfoSiteSection)
        // siteView.getComponent()).getSection());
        // }
        //
        // } catch (NonExistingServiceException e) {
        // throw new NonExistingActionException("A disciplina", e);
        // } catch (FenixServiceException e) {
        // throw new FenixActionException(e);
        // }
        // return siteView;
    }

    private void setFromRequest(HttpServletRequest request) {
        InfoExecutionDegree infoExecutionDegree =
                (InfoExecutionDegree) request.getAttribute(PresentationConstants.EXECUTION_DEGREE);

        String shift = request.getParameter("shift");
        if (shift == null) {
            shift = (String) request.getAttribute("shift");
        }
        if (shift == null) {
            shift = "false";
        }

        request.setAttribute("shift", shift);

        String degreeId = getFromRequest("degreeID", request);
        if (degreeId == null) {
            degreeId = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getExternalId();
        }
        request.setAttribute("degreeID", degreeId);

        String executionDegreeId = getFromRequest("executionDegreeID", request);
        if (executionDegreeId == null) {
            executionDegreeId = infoExecutionDegree.getExternalId();
        }
        request.setAttribute("executionDegreeID", executionDegreeId);
        String degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
        if (degreeCurricularPlanId == null) {
            degreeCurricularPlanId = infoExecutionDegree.getInfoDegreeCurricularPlan().getExternalId();
        }
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);
    }

}