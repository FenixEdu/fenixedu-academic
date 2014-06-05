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
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonValidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadAllStudentsAndGroups;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadStudentsAndGroupsByShiftID;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadStudentsAndGroupsWithoutShift;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadShiftsAndGroups;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.AcceptNewProjectProposal;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.CreateGrouping;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.CreateStudentGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteAllGroupingMembers;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteGrouping;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteGroupingMembers;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteGroupingMembersByExecutionCourseID;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteProjectProposal;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteStudentGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteStudentGroupMembers;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.EditGrouping;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.EditStudentGroupShift;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.EditStudentGroupsShift;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.EnrollStudentGroupShift;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.InsertGroupingMembers;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.InsertStudentGroupMembers;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.InsertStudentsInGrouping;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.NewProjectProposal;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.PrepareCreateStudentGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.PrepareEditGroupingMembers;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.PrepareEditStudentGroupMembers;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.RejectNewProjectProposal;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.UnEnrollStudentGroupShift;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.VerifyIfCanEnrollStudentGroupsInShift;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.VerifyIfGroupPropertiesHasProjectProposal;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.VerifyStudentGroupWithoutShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoGroupingWithAttends;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteGroupsByShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShifts;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShiftsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroupAndStudents;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsAndGroups;
import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.executionCourse.ExecutionCourseBaseAction;
import net.sourceforge.fenixedu.util.EnrolmentGroupPolicyType;
import net.sourceforge.fenixedu.util.ProposalState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.fenixedu.bennu.core.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

// TODO Clean this up. Remove Info's
@Mapping(path = "/studentGroupManagement", module = "teacher", formBean = "studentGroupsForm",
        functionality = ManageExecutionCourseDA.class, validate = false)
public class StudentGroupManagementDA extends ExecutionCourseBaseAction {

    private static final Logger logger = LoggerFactory.getLogger(StudentGroupManagementDA.class);

    public ActionForward prepareViewExecutionCourseProjects(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        ExecutionCourse executionCourse = getExecutionCourse(request);

        if (executionCourse.hasProposals()) {
            request.setAttribute("hasProposals", true);
        }
        if (hasWaitingAnswer(executionCourse)) {
            request.setAttribute("waitingAnswer", true);
        }
        return forward(request, "/teacher/viewProjectsAndLink_bd.jsp");
    }

    protected boolean hasWaitingAnswer(ExecutionCourse executionCourse) {
        List<Grouping> groupings = executionCourse.getGroupings();
        for (final Grouping grouping : groupings) {
            final Collection<ExportGrouping> groupingExecutionCourses = grouping.getExportGroupingsSet();
            for (final ExportGrouping groupingExecutionCourse : groupingExecutionCourses) {
                if (groupingExecutionCourse.getProposalState().getState().intValue() == ProposalState.EM_ESPERA) {
                    return true;
                }
            }
        }
        return false;
    }

    protected List<Grouping> getWaitingAnswerGroupings(ExecutionCourse executionCourse) {
        List<Grouping> groupings = new ArrayList<>();
        for (final Grouping grouping : executionCourse.getGroupings()) {
            final Collection<ExportGrouping> groupingExecutionCourses = grouping.getExportGroupingsSet();
            for (final ExportGrouping groupingExecutionCourse : groupingExecutionCourses) {
                if (groupingExecutionCourse.getProposalState().getState().intValue() == ProposalState.EM_ESPERA) {
                    groupings.add(groupingExecutionCourse.getGrouping());
                }
            }
        }
        return groupings;
    }

    private List<Grouping> getNewProposals(ExecutionCourse executionCourse) {
        List<Grouping> groupings = new ArrayList<Grouping>();
        for (ExportGrouping export : executionCourse.getExportGroupingsSet()) {
            if (export.getProposalState().getState() == 3) {
                groupings.add(export.getGrouping());
            }
        }
        return groupings;
    }

    public ActionForward viewNewProjectProposals(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        List<Grouping> newProposals = getNewProposals(getExecutionCourse(request));

        if (newProposals.isEmpty()) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = new ActionMessage("error.viewNewProjectProposals");
            actionErrors.add("error.viewNewProjectProposals", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }
        request.setAttribute("newProposals", newProposals);
        return forward(request, "/teacher/viewNewProjectProposals_bd.jsp");
    }

    public ActionForward viewSentedProjectProposalsWaiting(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        List<Grouping> groupings = getWaitingAnswerGroupings(getExecutionCourse(request));
        if (groupings.isEmpty()) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = new ActionMessage("error.SentedProjectsProposalsWaiting");
            actionErrors.add("error.SentedProjectsProposalsWaiting", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }
        request.setAttribute("groupings", groupings);
        return forward(request, "/teacher/viewSentedProjectProposalsWaiting_bd.jsp");
    }

    public ActionForward rejectNewProjectProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        User userView = getUserView(request);
        String objectCode = getExecutionCourse(request).getExternalId();
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        try {
            RejectNewProjectProposal.runRejectNewProjectProposal(objectCode, groupPropertiesCodeString, userView.getUsername());
        } catch (ExistingServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noProjectProposal");
            actionErrors.add("error.noProjectProposal", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (NotAuthorizedException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroupProperties.ProjectProposal");
            actionErrors.add("error.noGroupProperties.ProjectProposal", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (FenixServiceException e) {
            logger.error(e.getMessage(), e);
            throw new FenixActionException(e.getMessage());
        }

        return prepareViewExecutionCourseProjects(mapping, form, request, response);
    }

    public ActionForward acceptNewProjectProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        User userView = getUserView(request);
        String objectCode = getExecutionCourse(request).getExternalId();
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        try {
            AcceptNewProjectProposal.runAcceptNewProjectProposal(objectCode, groupPropertiesCodeString, userView.getUsername());
        } catch (ExistingServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noProjectProposal");
            actionErrors.add("error.noProjectProposal", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (NotAuthorizedException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroupProperties.ProjectProposal");
            actionErrors.add("error.noGroupProperties.ProjectProposal", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            actionErrors.add("error.ProjectProposalName", new ActionMessage("error.ProjectProposalName", e.getMessage()));
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);

        } catch (FenixServiceException e) {
            logger.error(e.getMessage(), e);
            throw new FenixActionException(e.getMessage());
        }

        return prepareViewExecutionCourseProjects(mapping, form, request, response);
    }

    public ActionForward viewShiftsAndGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

        InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(getExecutionCourse(request));
        InfoExecutionPeriod infoExecutionPeriod = infoExecutionCourse.getInfoExecutionPeriod();
        request.setAttribute(PresentationConstants.EXECUTION_PERIOD, infoExecutionPeriod);
        request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getExternalId());

        InfoSiteShiftsAndGroups shiftsAndGroupsView = getInfoSiteShiftsAndGroups(groupPropertiesCodeString);

        if (shiftsAndGroupsView.getInfoGrouping() == null) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = new ActionMessage("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }

        if (shiftsAndGroupsView.getInfoSiteGroupsByShiftList().isEmpty()) {
            request.setAttribute("noShifts", new Boolean(true));
        } else {
            boolean found = false;
            Iterator iterShiftsAndGroups = shiftsAndGroupsView.getInfoSiteGroupsByShiftList().iterator();
            while (iterShiftsAndGroups.hasNext() && !found) {
                InfoSiteGroupsByShift shiftsAndGroups = (InfoSiteGroupsByShift) iterShiftsAndGroups.next();
                if (!shiftsAndGroups.getInfoSiteStudentGroupsList().isEmpty()) {
                    request.setAttribute("hasGroups", new Boolean(true));
                    found = true;
                }
            }
        }
        request.setAttribute("component", shiftsAndGroupsView);
        return forward(request, "/teacher/viewShiftsAndGroups_bd.jsp");

    }

    static InfoSiteShiftsAndGroups getInfoSiteShiftsAndGroups(String groupPropertiesCode) {
        InfoSiteShiftsAndGroups component = new InfoSiteShiftsAndGroups();
        Grouping groupProperties = FenixFramework.getDomainObject(groupPropertiesCode);
        component.setInfoSiteGroupsByShiftList(ReadShiftsAndGroups.run(groupProperties).getInfoSiteGroupsByShiftList());
        component.setNumberOfStudentsOutsideAttendsSet(groupProperties.getNumberOfStudentsNotInGrouping());
        component.setNumberOfStudentsInsideAttendsSet(groupProperties.getNumberOfStudentsInGrouping());
        component.setInfoGrouping(InfoGrouping.newInfoFromDomain(groupProperties));
        return component;
    }

    public ActionForward viewStudentGroupInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String objectCode = getExecutionCourse(request).getExternalId();
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

        String shiftCodeString = request.getParameter("shiftCode");

        InfoSiteStudentGroup studentGroup =
                new TeacherAdministrationSiteComponentBuilder().getInfoSiteStudentGroup(new InfoSiteStudentGroup(),
                        studentGroupCodeString);
        StudentGroup group = FenixFramework.getDomainObject(studentGroupCodeString);
        request.setAttribute("component", studentGroup);
        request.setAttribute("groupMembers", group.getAttendsSet());

        if (request.getParameter("showPhotos") == null) {
            request.setAttribute("showPhotos", "false");
        }

        try {

            Integer type =
                    VerifyStudentGroupWithoutShift.runVerifyStudentGroupWithoutShift(objectCode, studentGroupCodeString,
                            groupPropertiesCodeString, shiftCodeString);

            request.setAttribute("ShiftType", type);

        } catch (ExistingServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            if (!StringUtils.isEmpty(shiftCodeString)) {
                request.setAttribute("shiftCode", shiftCodeString);
            }
            return viewShiftsAndGroups(mapping, form, request, response);
        } catch (InvalidArgumentsServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.StudentGroupShiftIsChanged");
            actionErrors.add("error.StudentGroupShiftIsChanged", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return forward(request, "/teacher/viewStudentGroupInformation_bd.jsp");
    }

    public ActionForward viewDeletedStudentGroupInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String groupId = request.getParameter("studentGroupId");
        StudentGroup studentGroup = FenixFramework.getDomainObject(groupId);
        request.setAttribute("studentGroup", studentGroup);
        request.setAttribute("executionCourseID", request.getParameter("executionCourseID"));
        request.setAttribute("projectID", request.getParameter("projectID"));
        return forward(request, "/student/viewDeletedStudentGroupInformation_bd.jsp");
    }

    public List<LabelValueBean> getShiftTypeLabelValues(HttpServletRequest request, Boolean differentiatedCapacity) {
        String executionCourseCode = getExecutionCourse(request).getExternalId();
        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseCode);

        List<LabelValueBean> shiftTypeValues = new ArrayList<LabelValueBean>();

        if (executionCourse != null) {
            for (CourseLoad cl : executionCourse.getCourseLoadsSet()) {
                shiftTypeValues.add(new LabelValueBean(RenderUtils.getEnumString(cl.getType()), cl.getType().name()));
            }
        }
        if (!differentiatedCapacity) {
            shiftTypeValues.add(new LabelValueBean("SEM TURNO", "SEM TURNO"));
        }

        return shiftTypeValues;
    }

    public ActionForward prepareCreateGroupProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        DynaActionForm groupPropertiesForm = (DynaActionForm) form;
        List<LabelValueBean> shiftTypeValues = getShiftTypeLabelValues(request, getDifferentiatedCapacity(groupPropertiesForm));
        request.setAttribute("shiftTypeValues", shiftTypeValues);

        if (getDifferentiatedCapacity(groupPropertiesForm)) {
            request.setAttribute("automaticEnrolmentDisable", "true");
        }
        if (getAutomaticEnrolment(groupPropertiesForm)) {
            request.setAttribute("differentiatedCapacityDisable", "true");
        }
        return forward(request, "/teacher/insertGroupProperties_bd.jsp");
    }

    public ActionForward createGroupPropertiesPostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        DynaActionForm groupPropertiesForm = setAutomaticEnrolmentFields(form, request, null);
        return prepareCreateGroupProperties(mapping, groupPropertiesForm, request, response);
    }

    public ActionForward createGroupCapacityPropertiesPostBack(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        DynaActionForm groupPropertiesForm = setDifferentiatedCapacity(form, request, null);
        ExecutionCourse executionCourse = getExecutionCourse(request);
        String shiftType = (String) groupPropertiesForm.get("shiftType");

        if (shiftType.equalsIgnoreCase("Sem Turno")) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = new ActionMessage("error.groupProperties.DiffCapacityNoShift");
            actionErrors.add("error.groupProperties.DiffCapacityNoShift", error);
            saveErrors(request, actionErrors);
            groupPropertiesForm.set("differentiatedCapacity", Boolean.FALSE);
            return prepareCreateGroupProperties(mapping, groupPropertiesForm, request, response);
        }
        List<InfoShift> shiftsList = InfoShift.getInfoShiftsByType(executionCourse, ShiftType.valueOf(shiftType));

        if (shiftsList == null || shiftsList.size() == 0) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = new ActionMessage("error.groupProperties.DiffCapacityNoShiftsList");
            actionErrors.add("error.groupProperties.DiffCapacityNoShiftsList", error);
            saveErrors(request, actionErrors);
            groupPropertiesForm.set("differentiatedCapacity", Boolean.FALSE);
            return prepareCreateGroupProperties(mapping, groupPropertiesForm, request, response);
        }
        request.setAttribute("shiftsList", shiftsList);

        RenderUtils.invalidateViewState();

        return prepareCreateGroupProperties(mapping, groupPropertiesForm, request, response);
    }

    private DynaActionForm setDifferentiatedCapacity(ActionForm form, HttpServletRequest request, InfoGrouping infoGroupProperties) {
        DynaActionForm groupPropertiesForm = (DynaActionForm) form;
        Boolean differentiatedCapacity = getDifferentiatedCapacity(groupPropertiesForm);

        if (differentiatedCapacity) {
            if (infoGroupProperties != null) {
                infoGroupProperties.setGroupMaximumNumber(null);
                infoGroupProperties.setAutomaticEnrolment(Boolean.FALSE);
                infoGroupProperties.setDifferentiatedCapacity(Boolean.TRUE);
            }
            groupPropertiesForm.set("groupMaximumNumber", StringUtils.EMPTY);
            groupPropertiesForm.set("automaticEnrolment", Boolean.FALSE);
            groupPropertiesForm.set("differentiatedCapacity", Boolean.TRUE);
        } else {
            if (infoGroupProperties != null) {
                infoGroupProperties.setGroupMaximumNumber(null);
                infoGroupProperties.setAutomaticEnrolment(Boolean.FALSE);
                infoGroupProperties.setDifferentiatedCapacity(Boolean.FALSE);
            }
            groupPropertiesForm.set("groupMaximumNumber", StringUtils.EMPTY);
            groupPropertiesForm.set("differentiatedCapacity", Boolean.FALSE);
        }
        return groupPropertiesForm;
    }

    private DynaActionForm setAutomaticEnrolmentFields(ActionForm form, HttpServletRequest request,
            InfoGrouping infoGroupProperties) {
        DynaActionForm groupPropertiesForm = (DynaActionForm) form;
        Boolean automaticEnrolment = getAutomaticEnrolment(groupPropertiesForm);
        if (automaticEnrolment) {
            String objectCode = getExecutionCourse(request).getExternalId();
            ExecutionCourse executionCourse = FenixFramework.getDomainObject(objectCode);
            if (infoGroupProperties != null) {
                infoGroupProperties.setMaximumCapacity(1);
                infoGroupProperties.setMinimumCapacity(1);
                infoGroupProperties.setIdealCapacity(1);
                infoGroupProperties.setGroupMaximumNumber(executionCourse.getAttendsSet().size());
                infoGroupProperties.setShiftType(null);
                infoGroupProperties.setEnrolmentPolicy(new EnrolmentGroupPolicyType(2));
                infoGroupProperties.setAutomaticEnrolment(Boolean.TRUE);
                infoGroupProperties.setDifferentiatedCapacity(Boolean.FALSE);
            }
            groupPropertiesForm.set("maximumCapacity", "1");
            groupPropertiesForm.set("minimumCapacity", "1");
            groupPropertiesForm.set("idealCapacity", "1");
            groupPropertiesForm.set("groupMaximumNumber", String.valueOf(executionCourse.getAttendsSet().size()));
            groupPropertiesForm.set("shiftType", "SEM TURNO");
            groupPropertiesForm.set("enrolmentPolicy", "false");
            request.setAttribute("automaticEnrolment", "true");
            request.setAttribute("differentiatedCapacity", "false");
        } else {
            if (infoGroupProperties != null) {
                infoGroupProperties.setMaximumCapacity(null);
                infoGroupProperties.setMinimumCapacity(null);
                infoGroupProperties.setIdealCapacity(null);
                infoGroupProperties.setGroupMaximumNumber(null);
                infoGroupProperties.setShiftType(null);
                infoGroupProperties.setEnrolmentPolicy(new EnrolmentGroupPolicyType(1));
                infoGroupProperties.setAutomaticEnrolment(Boolean.FALSE);
                infoGroupProperties.setDifferentiatedCapacity(Boolean.FALSE);
            }
            groupPropertiesForm.set("maximumCapacity", StringUtils.EMPTY);
            groupPropertiesForm.set("minimumCapacity", StringUtils.EMPTY);
            groupPropertiesForm.set("idealCapacity", StringUtils.EMPTY);
            groupPropertiesForm.set("groupMaximumNumber", StringUtils.EMPTY);
            groupPropertiesForm.set("shiftType", StringUtils.EMPTY);
            groupPropertiesForm.set("enrolmentPolicy", StringUtils.EMPTY);
        }
        return groupPropertiesForm;
    }

    public ActionForward createGroupProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        DynaActionForm insertGroupPropertiesForm = (DynaActionForm) form;
        String name = (String) insertGroupPropertiesForm.get("name");
        String projectDescription = (String) insertGroupPropertiesForm.get("projectDescription");
        String maximumCapacityString = (String) insertGroupPropertiesForm.get("maximumCapacity");
        String minimumCapacityString = (String) insertGroupPropertiesForm.get("minimumCapacity");
        String idealCapacityString = (String) insertGroupPropertiesForm.get("idealCapacity");
        String groupMaximumNumber = (String) insertGroupPropertiesForm.get("groupMaximumNumber");
        String enrolmentBeginDayString = (String) insertGroupPropertiesForm.get("enrolmentBeginDay");
        String enrolmentBeginHourString = (String) insertGroupPropertiesForm.get("enrolmentBeginHour");
        String enrolmentEndDayString = (String) insertGroupPropertiesForm.get("enrolmentEndDay");
        String enrolmentEndHourString = (String) insertGroupPropertiesForm.get("enrolmentEndHour");
        String shiftType = (String) insertGroupPropertiesForm.get("shiftType");
        Boolean optional = new Boolean((String) insertGroupPropertiesForm.get("enrolmentPolicy"));
        Boolean automaticEnrolment = getAutomaticEnrolment(insertGroupPropertiesForm);
        Boolean differentiatedCapacity = getDifferentiatedCapacity(insertGroupPropertiesForm);

        final ArrayList<InfoShift> infoShifts = getRenderedObject("shiftsTable");

        if ("".equals(name)) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = new ActionMessage("error.groupProperties.missingName");
            actionErrors.add("error.groupProperties.missingName", error);
            saveErrors(request, actionErrors);
            return prepareCreateGroupProperties(mapping, form, request, response);
        }

        InfoGrouping infoGroupProperties = new InfoGrouping();
        infoGroupProperties.setName(name);
        infoGroupProperties.setProjectDescription(projectDescription);

        if (!shiftType.equalsIgnoreCase("Sem Turno")) {
            infoGroupProperties.setShiftType(ShiftType.valueOf(shiftType));
        }

        if (differentiatedCapacity) {
            for (InfoShift info : infoShifts) {
                if (!maximumCapacityString.equals("")
                        && info.getGroupCapacity() * Integer.parseInt(maximumCapacityString) > info.getLotacao()) {
                    ActionMessages actionErrors = new ActionMessages();
                    ActionMessage error = new ActionMessage("error.groupProperties.capacityOverflow");
                    actionErrors.add("error.groupProperties.capacityOverflow", error);
                    saveErrors(request, actionErrors);
                    return prepareCreateGroupProperties(mapping, form, request, response);
                }
            }
            infoGroupProperties.setInfoShifts(infoShifts);
        }

        Integer maximumCapacity = null;
        Integer minimumCapacity = null;
        Integer idealCapacity = null;

        if (!maximumCapacityString.equals("")) {
            maximumCapacity = new Integer(maximumCapacityString);
            infoGroupProperties.setMaximumCapacity(maximumCapacity);
        }

        if (!minimumCapacityString.equals("")) {
            minimumCapacity = new Integer(minimumCapacityString);
            if (maximumCapacity != null) {
                if (minimumCapacity.compareTo(maximumCapacity) > 0) {
                    ActionMessages actionErrors = new ActionMessages();
                    ActionMessage error = null;
                    error = new ActionMessage("error.groupProperties.minimum");
                    actionErrors.add("error.groupProperties.minimum", error);
                    saveErrors(request, actionErrors);
                    return prepareCreateGroupProperties(mapping, form, request, response);
                }
            }
            infoGroupProperties.setMinimumCapacity(minimumCapacity);
        }

        if (!idealCapacityString.equals("")) {

            idealCapacity = new Integer(idealCapacityString);

            if (!minimumCapacityString.equals("")) {
                if (idealCapacity.compareTo(minimumCapacity) < 0) {
                    ActionMessages actionErrors = new ActionMessages();
                    ActionMessage error = null;
                    error = new ActionMessage("error.groupProperties.ideal.minimum");
                    actionErrors.add("error.groupProperties.ideal.minimum", error);
                    saveErrors(request, actionErrors);
                    return prepareCreateGroupProperties(mapping, form, request, response);
                }
            }

            if (!maximumCapacityString.equals("")) {
                if (idealCapacity.compareTo(maximumCapacity) > 0) {
                    ActionMessages actionErrors = new ActionMessages();
                    ActionMessage error = null;
                    error = new ActionMessage("error.groupProperties.ideal.maximum");
                    actionErrors.add("error.groupProperties.ideal.maximum", error);
                    saveErrors(request, actionErrors);
                    return prepareCreateGroupProperties(mapping, form, request, response);
                }
            }
            infoGroupProperties.setIdealCapacity(idealCapacity);
        }

        if (!groupMaximumNumber.equals("")) {
            infoGroupProperties.setGroupMaximumNumber(new Integer(groupMaximumNumber));
        }

        infoGroupProperties.setDifferentiatedCapacity(differentiatedCapacity);

        EnrolmentGroupPolicyType enrolmentPolicy;
        if (optional.booleanValue()) {
            enrolmentPolicy = new EnrolmentGroupPolicyType(1);
        } else {
            enrolmentPolicy = new EnrolmentGroupPolicyType(2);
        }
        infoGroupProperties.setEnrolmentPolicy(enrolmentPolicy);
        infoGroupProperties.setAutomaticEnrolment(automaticEnrolment);
        Calendar enrolmentBeginDay = null;
        if (!enrolmentBeginDayString.equals("")) {
            String[] beginDate = enrolmentBeginDayString.split("/");
            enrolmentBeginDay = Calendar.getInstance();
            enrolmentBeginDay.set(Calendar.DAY_OF_MONTH, (new Integer(beginDate[0])).intValue());
            enrolmentBeginDay.set(Calendar.MONTH, (new Integer(beginDate[1])).intValue() - 1);
            enrolmentBeginDay.set(Calendar.YEAR, (new Integer(beginDate[2])).intValue());

            if (!enrolmentBeginHourString.equals("")) {
                String[] beginHour = enrolmentBeginHourString.split(":");
                enrolmentBeginDay.set(Calendar.HOUR_OF_DAY, (new Integer(beginHour[0])).intValue());
                enrolmentBeginDay.set(Calendar.MINUTE, (new Integer(beginHour[1])).intValue());
                enrolmentBeginDay.set(Calendar.SECOND, 0);
            }
        } else {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = new ActionMessage("error.groupProperties.missingEnrolmentBeginDay");
            actionErrors.add("error.groupProperties.missingEnrolmentBeginDay", error);
            saveErrors(request, actionErrors);
            return prepareCreateGroupProperties(mapping, form, request, response);
        }

        infoGroupProperties.setEnrolmentBeginDay(enrolmentBeginDay);

        Calendar enrolmentEndDay = null;
        if (!enrolmentEndDayString.equals("")) {
            String[] endDate = enrolmentEndDayString.split("/");
            enrolmentEndDay = Calendar.getInstance();
            enrolmentEndDay.set(Calendar.DAY_OF_MONTH, (new Integer(endDate[0])).intValue());
            enrolmentEndDay.set(Calendar.MONTH, (new Integer(endDate[1])).intValue() - 1);
            enrolmentEndDay.set(Calendar.YEAR, (new Integer(endDate[2])).intValue());

            if (!enrolmentEndHourString.equals("")) {
                String[] endHour = enrolmentEndHourString.split(":");
                enrolmentEndDay.set(Calendar.HOUR_OF_DAY, (new Integer(endHour[0])).intValue());
                enrolmentEndDay.set(Calendar.MINUTE, (new Integer(endHour[1])).intValue());
                enrolmentEndDay.set(Calendar.SECOND, 0);
            }
        } else {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = new ActionMessage("error.groupProperties.missingEnrolmentEndDay");
            actionErrors.add("error.groupProperties.missingEnrolmentEndDay", error);
            saveErrors(request, actionErrors);
            return prepareCreateGroupProperties(mapping, form, request, response);
        }

        float compareDate = enrolmentBeginDay.compareTo(enrolmentEndDay);

        if (compareDate >= 0.0) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.manager.wrongDates");
            actionErrors.add("error.manager.wrongDates", error);
            saveErrors(request, actionErrors);
            return prepareCreateGroupProperties(mapping, form, request, response);
        }

        infoGroupProperties.setEnrolmentEndDay(enrolmentEndDay);

        String objectCode = getExecutionCourse(request).getExternalId();
        try {
            CreateGrouping.runCreateGrouping(objectCode, infoGroupProperties);
        } catch (DomainException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = new ActionMessage("error.exception.existing.groupProperties");
            actionErrors.add("error.exception.existing.groupProperties", error);
            saveErrors(request, actionErrors);
            return prepareCreateGroupProperties(mapping, form, request, response);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        return prepareViewExecutionCourseProjects(mapping, form, request, response);
    }

    public ActionForward prepareEditGroupProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        InfoGrouping infoGroupProperties = getInfoGrouping(request);
        Integer enrolmentPolicy = infoGroupProperties.getEnrolmentPolicy().getType();
        DynaActionForm groupPropertiesForm = (DynaActionForm) form;

        request.setAttribute("infoGroupProperties", infoGroupProperties);
        request.setAttribute("enrolmentPolicyValue", enrolmentPolicy);

        List<LabelValueBean> shiftTypeValues = getShiftTypeLabelValues(request, getDifferentiatedCapacity(groupPropertiesForm));
        request.setAttribute("shiftTypeValues", shiftTypeValues);

        List enrolmentPolicyValues = new ArrayList();
        enrolmentPolicyValues.add(new Integer(1));
        enrolmentPolicyValues.add(new Integer(2));

        List enrolmentPolicyNames = new ArrayList();
        enrolmentPolicyNames.add("Atómica");
        enrolmentPolicyNames.add("Individual");

        enrolmentPolicyValues.remove(enrolmentPolicy.intValue() - 1);
        String enrolmentPolicyName = enrolmentPolicyNames.remove(enrolmentPolicy.intValue() - 1).toString();

        request.setAttribute("enrolmentPolicyName", enrolmentPolicyName);
        request.setAttribute("enrolmentPolicyValues", enrolmentPolicyValues);
        request.setAttribute("enrolmentPolicyNames", enrolmentPolicyNames);

        final Grouping grouping = FenixFramework.getDomainObject(infoGroupProperties.getExternalId());

        String shiftType = (String) groupPropertiesForm.get("shiftType");
        if (getDifferentiatedCapacity(groupPropertiesForm) && shiftType.equalsIgnoreCase("Sem Turno")) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.groupProperties.DiffCapacityNoShift");
            actionErrors.add("error.groupProperties.DiffCapacityNoShift", error);
            saveErrors(request, actionErrors);
            infoGroupProperties.setDifferentiatedCapacity(Boolean.FALSE);
        } else if (infoGroupProperties.getDifferentiatedCapacity()) {
            String executionCourseCode = getExecutionCourse(request).getExternalId();
            ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseCode);
            List<InfoShift> shiftsList;
            if (!shiftType.isEmpty()) {
                shiftsList = InfoShift.getInfoShiftsByType(executionCourse, ShiftType.valueOf(shiftType));
            } else {
                shiftsList = InfoShift.getInfoShiftsByType(executionCourse, grouping.getShiftType());
            }
            request.setAttribute("shiftsList", shiftsList);
        }

        if (StringUtils.isEmpty(shiftType)) {
            if (grouping.getShiftType() != null) {
                groupPropertiesForm.set("shiftType", grouping.getShiftType().getName());
            } else {
                groupPropertiesForm.set("shiftType", "SEM TURNO");
            }
        }

        if (getDifferentiatedCapacity(groupPropertiesForm)) {
            request.setAttribute("automaticEnrolmentDisable", "true");
        }
        if (getAutomaticEnrolment(groupPropertiesForm)) {
            request.setAttribute("differentiatedCapacityDisable", "true");
        }

        if (!grouping.getStudentGroupsSet().isEmpty() && !grouping.getAutomaticEnrolment()) {
            request.setAttribute("automaticEnrolmentDisable", "true");
        }
        if (!grouping.getStudentGroupsSet().isEmpty() && !grouping.getDifferentiatedCapacity()) {
            request.setAttribute("differentiatedCapacityDisable", "true");
        }
        if (grouping.hasAnyStudentGroups() && grouping.getAutomaticEnrolment() && !infoGroupProperties.getAutomaticEnrolment()) {
            request.setAttribute("notPosibleToRevertChoice", "true");
        }
        if (grouping.hasAnyStudentGroups() && grouping.getDifferentiatedCapacity()
                && !infoGroupProperties.getDifferentiatedCapacity()) {
            request.setAttribute("notPosibleToRevertChoice", "true");
        }

        return forward(request, "/teacher/editGroupProperties_bd.jsp");
    }

    private InfoGrouping getInfoGrouping(HttpServletRequest request) throws FenixActionException {

        InfoGrouping infoGroupProperties =
                request.getAttribute("infoGroupProperties") != null ? (InfoGrouping) request.getAttribute("infoGroupProperties") : null;

        if (infoGroupProperties == null) {
            String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
            Grouping grouping = FenixFramework.getDomainObject(groupPropertiesCodeString);
            InfoGrouping infoGrouping = InfoGrouping.newInfoFromDomain(grouping);
            request.setAttribute("infoGrouping", infoGrouping);
            return infoGrouping;
        }
        return infoGroupProperties;
    }

    public ActionForward editGroupPropertiesPostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        InfoGrouping infoGroupProperties = getInfoGrouping(request);
        request.setAttribute("infoGroupProperties", infoGroupProperties);

        DynaActionForm groupPropertiesForm = setAutomaticEnrolmentFields(form, request, infoGroupProperties);

        return prepareEditGroupProperties(mapping, groupPropertiesForm, request, response);
    }

    public ActionForward editGroupCapacityPropertiesPostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        InfoGrouping infoGroupProperties = getInfoGrouping(request);
        request.setAttribute("infoGroupProperties", infoGroupProperties);

        DynaActionForm groupPropertiesForm = setDifferentiatedCapacity(form, request, infoGroupProperties);
        return prepareEditGroupProperties(mapping, groupPropertiesForm, request, response);
    }

    public ActionForward editGroupProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        DynaActionForm editGroupPropertiesForm = (DynaActionForm) form;
        String groupPropertiesString = request.getParameter("groupPropertiesCode");
        String groupPropertiesCode = groupPropertiesString;
        String name = (String) editGroupPropertiesForm.get("name");
        String projectDescription = (String) editGroupPropertiesForm.get("projectDescription");
        String maximumCapacityString = (String) editGroupPropertiesForm.get("maximumCapacity");
        String minimumCapacityString = (String) editGroupPropertiesForm.get("minimumCapacity");
        String idealCapacityString = (String) editGroupPropertiesForm.get("idealCapacity");

        String groupMaximumNumber = (String) editGroupPropertiesForm.get("groupMaximumNumber");
        String enrolmentBeginDayString = (String) editGroupPropertiesForm.get("enrolmentBeginDayFormatted");
        String enrolmentBeginHourString = (String) editGroupPropertiesForm.get("enrolmentBeginHourFormatted");
        String enrolmentEndDayString = (String) editGroupPropertiesForm.get("enrolmentEndDayFormatted");
        String enrolmentEndHourString = (String) editGroupPropertiesForm.get("enrolmentEndHourFormatted");
        String shiftType = (String) editGroupPropertiesForm.get("shiftType");
        String enrolmentPolicy = (String) editGroupPropertiesForm.get("enrolmentPolicy");
        Boolean automaticEnrolment = getAutomaticEnrolment(editGroupPropertiesForm);
        Boolean differentiatedCapacity = getDifferentiatedCapacity(editGroupPropertiesForm);

        final ArrayList<InfoShift> infoShifts = getRenderedObject("shiftsTable");

        if ("".equals(name)) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = new ActionMessage("error.groupProperties.missingName");
            actionErrors.add("error.groupProperties.missingName", error);
            saveErrors(request, actionErrors);
            return prepareEditGroupProperties(mapping, form, request, response);
        }

        Calendar enrolmentBeginDay = null;
        if (!enrolmentBeginDayString.equals("")) {
            String[] beginDate = enrolmentBeginDayString.split("/");
            enrolmentBeginDay = Calendar.getInstance();
            enrolmentBeginDay.set(Calendar.DAY_OF_MONTH, (new Integer(beginDate[0])).intValue());
            enrolmentBeginDay.set(Calendar.MONTH, (new Integer(beginDate[1])).intValue() - 1);
            enrolmentBeginDay.set(Calendar.YEAR, (new Integer(beginDate[2])).intValue());

            if (!enrolmentBeginHourString.equals("")) {
                String[] beginHour = enrolmentBeginHourString.split(":");
                enrolmentBeginDay.set(Calendar.HOUR_OF_DAY, (new Integer(beginHour[0])).intValue());
                enrolmentBeginDay.set(Calendar.MINUTE, (new Integer(beginHour[1])).intValue());
                enrolmentBeginDay.set(Calendar.SECOND, 0);
            }
        }
        Calendar enrolmentEndDay = null;
        if (!enrolmentEndDayString.equals("")) {
            String[] endDate = enrolmentEndDayString.split("/");
            enrolmentEndDay = Calendar.getInstance();
            enrolmentEndDay.set(Calendar.DAY_OF_MONTH, (new Integer(endDate[0])).intValue());
            enrolmentEndDay.set(Calendar.MONTH, (new Integer(endDate[1])).intValue() - 1);
            enrolmentEndDay.set(Calendar.YEAR, (new Integer(endDate[2])).intValue());

            if (!enrolmentEndHourString.equals("")) {
                String[] endHour = enrolmentEndHourString.split(":");
                enrolmentEndDay.set(Calendar.HOUR_OF_DAY, (new Integer(endHour[0])).intValue());
                enrolmentEndDay.set(Calendar.MINUTE, (new Integer(endHour[1])).intValue());
                enrolmentEndDay.set(Calendar.SECOND, 0);
            }
        }

        float compareDate = enrolmentBeginDay.compareTo(enrolmentEndDay);

        if (compareDate >= 0.0) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.manager.wrongDates");
            actionErrors.add("error.manager.wrongDates", error);
            saveErrors(request, actionErrors);
            return prepareEditGroupProperties(mapping, form, request, response);
        }

        InfoGrouping infoGroupProperties = new InfoGrouping();
        infoGroupProperties.setExternalId(groupPropertiesCode);
        infoGroupProperties.setEnrolmentBeginDay(enrolmentBeginDay);
        infoGroupProperties.setEnrolmentEndDay(enrolmentEndDay);
        infoGroupProperties.setEnrolmentPolicy(new EnrolmentGroupPolicyType(new Integer(enrolmentPolicy)));
        infoGroupProperties.setAutomaticEnrolment(automaticEnrolment);
        infoGroupProperties.setDifferentiatedCapacity(differentiatedCapacity);

        if (differentiatedCapacity) {
            for (InfoShift info : infoShifts) {
                if (!maximumCapacityString.equals("")
                        && info.getGroupCapacity() * Integer.parseInt(maximumCapacityString) > info.getLotacao()) {
                    ActionMessages actionErrors = new ActionMessages();
                    ActionMessage error = null;
                    error = new ActionMessage("error.groupProperties.capacityOverflow");
                    actionErrors.add("error.groupProperties.capacityOverflow", error);
                    saveErrors(request, actionErrors);
                    return prepareEditGroupProperties(mapping, form, request, response);
                }
            }
            infoGroupProperties.setInfoShifts(infoShifts);
        }

        infoGroupProperties.setDifferentiatedCapacity(differentiatedCapacity);

        if (!groupMaximumNumber.equals("")) {
            infoGroupProperties.setGroupMaximumNumber(new Integer(groupMaximumNumber));
        }
        Integer maximumCapacity = null;
        Integer minimumCapacity = null;
        Integer idealCapacity = null;

        if (!maximumCapacityString.equals("")) {
            maximumCapacity = new Integer(maximumCapacityString);
            infoGroupProperties.setMaximumCapacity(maximumCapacity);
        }

        if (!minimumCapacityString.equals("")) {
            minimumCapacity = new Integer(minimumCapacityString);
            if (maximumCapacity != null) {
                if (minimumCapacity.compareTo(maximumCapacity) > 0) {
                    ActionMessages actionErrors = new ActionMessages();
                    ActionMessage error = null;
                    error = new ActionMessage("error.groupProperties.minimum");
                    actionErrors.add("error.groupProperties.minimum", error);
                    saveErrors(request, actionErrors);
                    return prepareEditGroupProperties(mapping, form, request, response);

                }
            }
            infoGroupProperties.setMinimumCapacity(minimumCapacity);
        }

        if (!idealCapacityString.equals("")) {

            idealCapacity = new Integer(idealCapacityString);

            if (!minimumCapacityString.equals("")) {
                if (idealCapacity.compareTo(minimumCapacity) < 0) {
                    ActionMessages actionErrors = new ActionMessages();
                    ActionMessage error = null;
                    error = new ActionMessage("error.groupProperties.ideal.minimum");
                    actionErrors.add("error.groupProperties.ideal.minimum", error);
                    saveErrors(request, actionErrors);
                    return prepareEditGroupProperties(mapping, form, request, response);

                }
            }

            if (!maximumCapacityString.equals("")) {
                if (idealCapacity.compareTo(maximumCapacity) > 0) {
                    ActionMessages actionErrors = new ActionMessages();
                    ActionMessage error = null;
                    error = new ActionMessage("error.groupProperties.ideal.maximum");
                    actionErrors.add("error.groupProperties.ideal.maximum", error);
                    saveErrors(request, actionErrors);
                    return prepareEditGroupProperties(mapping, form, request, response);

                }
            }

            infoGroupProperties.setIdealCapacity(idealCapacity);
        }

        infoGroupProperties.setName(name);
        infoGroupProperties.setProjectDescription(projectDescription);

        if (!shiftType.equalsIgnoreCase("Sem Turno")) {
            infoGroupProperties.setShiftType(ShiftType.valueOf(shiftType));
        }
        String objectCode = getExecutionCourse(request).getExternalId();
        List errors = new ArrayList();
        try {
            errors = EditGrouping.runEditGrouping(objectCode, infoGroupProperties);
        } catch (InvalidSituationServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (DomainException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage(e.getArgs()[0]);
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (errors.size() != 0) {
            ActionMessages actionErrors = new ActionMessages();

            Iterator iterErrors = errors.iterator();
            ActionMessage errorInt = null;
            errorInt = new ActionMessage("error.exception.editGroupProperties");
            actionErrors.add("error.exception.editGroupProperties", errorInt);
            while (iterErrors.hasNext()) {
                Integer intError = (Integer) iterErrors.next();

                if (intError.equals(Integer.valueOf(-1))) {
                    ActionMessage error = null;
                    error = new ActionMessage("error.exception.nrOfGroups.editGroupProperties");
                    actionErrors.add("error.exception.nrOfGroups.editGroupProperties", error);
                }
                if (intError.equals(Integer.valueOf(-2))) {
                    ActionMessage error = null;
                    error = new ActionMessage("error.exception.maximumCapacity.editGroupProperties");
                    actionErrors.add("error.exception.maximumCapacity.editGroupProperties", error);
                }
                if (intError.equals(Integer.valueOf(-3))) {
                    ActionMessage error = null;
                    error = new ActionMessage("error.exception.minimumCapacity.editGroupProperties");
                    actionErrors.add("error.exception.minimumCapacity.editGroupProperties", error);
                }
            }
            saveErrors(request, actionErrors);

        }
        return prepareViewExecutionCourseProjects(mapping, form, request, response);
    }

    private Boolean getAutomaticEnrolment(DynaActionForm form) {
        Boolean automaticEnrolment = (Boolean) form.get("automaticEnrolment");
        if (automaticEnrolment == null) {
            return Boolean.FALSE;
        }
        return automaticEnrolment;
    }

    private Boolean getDifferentiatedCapacity(DynaActionForm form) {
        Boolean differenciatedCapacity = (Boolean) form.get("differentiatedCapacity");
        if (differenciatedCapacity == null) {
            return Boolean.FALSE;
        }
        return differenciatedCapacity;
    }

    public ActionForward deleteGroupProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        ActionMessages actionErrors = new ActionMessages();

        String objectCode = getExecutionCourse(request).getExternalId();

        String groupPropertiesCode = null;
        try {
            String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
            groupPropertiesCode = groupPropertiesCodeString;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            actionErrors.add("errors.delete.groupPropertie", new ActionMessage(e.getMessage()));
            saveErrors(request, actionErrors);

            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }

        Boolean result = Boolean.FALSE;
        try {
            result = DeleteGrouping.runDeleteGrouping(objectCode, groupPropertiesCode);
        } catch (ExistingServiceException e) {
            ActionMessages actionErrors1 = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroupProperties");
            actionErrors1.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors1);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionMessages actionErrors2 = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.groupProperties.delete.attendsSet.withGroups");
            actionErrors2.add("error.groupProperties.delete.attendsSet.withGroups", error);
            saveErrors(request, actionErrors2);
            return viewShiftsAndGroups(mapping, form, request, response);
        } catch (FenixServiceException e) {
            logger.error(e.getMessage(), e);
            throw new FenixActionException(e.getMessage());
        }
        if (result.equals(Boolean.FALSE)) {
            actionErrors.add("errors.delete.groupPropertie", new ActionMessage("error.groupProperties.delete"));
            saveErrors(request, actionErrors);

            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }

        return prepareViewExecutionCourseProjects(mapping, form, request, response);
    }

    public ActionForward prepareImportGroupProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        String objectCode = getExecutionCourse(request).getExternalId();
        Grouping grouping = FenixFramework.getDomainObject(groupPropertiesCodeString);
        InfoGrouping infoGrouping = InfoGrouping.newInfoFromDomain(grouping);
        Boolean result;
        try {
            result =
                    VerifyIfGroupPropertiesHasProjectProposal.runVerifyIfGroupPropertiesHasProjectProposal(objectCode,
                            groupPropertiesCodeString);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (!result.booleanValue()) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noProjectProposal");
            actionErrors.add("error.noProjectProposal", error);
            saveErrors(request, actionErrors);
            return viewNewProjectProposals(mapping, form, request, response);
        }

        String enrolmentPolicyName = infoGrouping.getEnrolmentPolicy().getTypeFullName();
        request.setAttribute("enrolmentPolicyName", enrolmentPolicyName);

        ShiftType shiftType = infoGrouping.getShiftType();
        String shiftTypeName = "Sem Turno";
        if (shiftType != null) {
            shiftTypeName = shiftType.getFullNameTipoAula();
        }

        request.setAttribute("infoGrouping", infoGrouping);
        request.setAttribute("shiftTypeName", shiftTypeName);

        return forward(request, "/teacher/importGroupProperties_bd.jsp");
    }

    public ActionForward deleteStudentGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String objectCode = getExecutionCourse(request).getExternalId();
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        try {
            DeleteStudentGroup.runDeleteStudentGroup(objectCode, studentGroupCodeString);
        } catch (ExistingServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("errors.invalid.delete.not.empty.studentGroup");
            actionErrors.add("errors.invalid.delete.not.empty.studentGroup", error);
            saveErrors(request, actionErrors);
            return viewStudentGroupInformation(mapping, form, request, response);
        } catch (DomainException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("errors.invalid.delete.not.empty.studentGroup");
            actionErrors.add("errors.invalid.delete.not.empty.studentGroup", error);
            saveErrors(request, actionErrors);
            return viewStudentGroupInformation(mapping, form, request, response);
        } catch (FenixServiceException e) {
            logger.error(e.getMessage(), e);
            throw new FenixActionException(e.getMessage());
        }

        return viewShiftsAndGroups(mapping, form, request, response);
    }

    public ActionForward prepareCreateStudentGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String objectCode = getExecutionCourse(request).getExternalId();
        String groupPropertiesString = request.getParameter("groupPropertiesCode");

        String shiftCodeString = request.getParameter("shiftCode");
        request.setAttribute("shiftCode", shiftCodeString);

        if (request.getParameter("showPhotos") == null) {
            request.setAttribute("showPhotos", "false");
        }

        InfoSiteStudentGroup infoSiteStudentGroup;
        try {
            infoSiteStudentGroup =
                    (InfoSiteStudentGroup) PrepareCreateStudentGroup.runPrepareCreateStudentGroup(objectCode,
                            groupPropertiesString);
        } catch (ExistingServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("infoSiteStudentGroup", infoSiteStudentGroup);
        return forward(request, "/teacher/insertStudentGroup_bd.jsp");
    }

    public ActionForward createStudentGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String objectCode = getExecutionCourse(request).getExternalId();

        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

        String shiftCodeString = request.getParameter("shiftCode");

        DynaActionForm insertStudentGroupForm = (DynaActionForm) form;

        List studentCodes = Arrays.asList((String[]) insertStudentGroupForm.get("studentCodes"));

        String groupNumberString = (String) insertStudentGroupForm.get("nrOfElements");
        Integer groupNumber = new Integer(groupNumberString);

        try {
            CreateStudentGroup.runCreateStudentGroup(objectCode, groupNumber, groupPropertiesCodeString, shiftCodeString,
                    studentCodes);

        } catch (DomainException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = new ActionMessage(e.getArgs()[0]);
            actionErrors.add("error.invalidNumberOfStudentsGroups", error);
            saveErrors(request, actionErrors);
            return prepareCreateStudentGroup(mapping, form, request, response);

        } catch (NotAuthorizedException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);

        } catch (InvalidSituationServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = new ActionMessage("errors.existing.studentEnrolment");
            actionErrors.add("errors.existing.studentEnrolment", error);
            saveErrors(request, actionErrors);
            return prepareCreateStudentGroup(mapping, form, request, response);

        } catch (InvalidArgumentsServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = new ActionMessage("errors.notExisting.studentInAttendsSetToCreateStudentGroup");
            actionErrors.add("errors.notExisting.studentInAttendsSetToCreateStudentGroup", error);
            saveErrors(request, actionErrors);
            return prepareCreateStudentGroup(mapping, form, request, response);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return viewShiftsAndGroups(mapping, form, request, response);

    }

    public ActionForward viewStudentsAndGroupsByShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String objectCode = getExecutionCourse(request).getExternalId();

        String groupPropertiesString = request.getParameter("groupPropertiesCode");
        String shiftCodeString = request.getParameter("shiftCode");

        InfoSiteStudentsAndGroups infoSiteStudentsAndGroups = new InfoSiteStudentsAndGroups();
        Boolean type = null;

        if (request.getParameter("showPhotos") == null) {
            request.setAttribute("showPhotos", "false");
        }

        try {
            infoSiteStudentsAndGroups = ReadStudentsAndGroupsByShiftID.run(groupPropertiesString, shiftCodeString);

            type =
                    VerifyIfCanEnrollStudentGroupsInShift.runVerifyIfCanEnrollStudentGroupsInShift(objectCode,
                            groupPropertiesString, shiftCodeString);

        } catch (ExistingServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("infoSiteStudentsAndGroups", infoSiteStudentsAndGroups);

        if (type.booleanValue() == true) {
            request.setAttribute("type", new Boolean(true));
        } else {
            request.setAttribute("type", new Boolean(false));
        }
        return forward(request, "/teacher/viewStudentsAndGroupsByShift_bd.jsp");
    }

    public ActionForward viewStudentsAndGroupsWithoutShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String groupPropertiesString = request.getParameter("groupPropertiesCode");

        InfoSiteStudentsAndGroups infoSiteStudentsAndGroups = new InfoSiteStudentsAndGroups();

        if (request.getParameter("showPhotos") == null) {
            request.setAttribute("showPhotos", "false");
        }

        try {
            infoSiteStudentsAndGroups = ReadStudentsAndGroupsWithoutShift.run(groupPropertiesString);
        } catch (ExistingServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("infoSiteStudentsAndGroups", infoSiteStudentsAndGroups);
        return forward(request, "/teacher/viewStudentsAndGroupsWithoutShift_bd.jsp");
    }

    public ActionForward viewAllStudentsAndGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String groupPropertiesString = request.getParameter("groupPropertiesCode");

        if (request.getParameter("showPhotos") == null) {
            request.setAttribute("showPhotos", "false");
        }

        InfoSiteStudentsAndGroups infoSiteStudentsAndGroups = new InfoSiteStudentsAndGroups();

        try {
            infoSiteStudentsAndGroups = ReadAllStudentsAndGroups.run(groupPropertiesString);
        } catch (ExistingServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = new ActionMessage("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("infoSiteStudentsAndGroups", infoSiteStudentsAndGroups);
        return forward(request, "/teacher/viewAllStudentsAndGroups_bd.jsp");
    }

    public ActionForward prepareEditStudentGroupShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String studentGroupCodeString = request.getParameter("studentGroupCode");
        String shiftCodeString = request.getParameter("shiftCode");
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

        InfoSiteShifts component =
                new TeacherAdministrationSiteComponentBuilder().getInfoSiteShifts(new InfoSiteShifts(),
                        groupPropertiesCodeString, studentGroupCodeString);
        request.setAttribute("component", component);
        if (component == null) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }

        List shifts = component.getShifts();
        if (shifts == null) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        }
        if (shifts.size() == 0) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("errors.shifts.not.available");
            actionErrors.add("errors.shifts.not.available", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        }

        ArrayList shiftsList = new ArrayList();
        InfoShift oldInfoShift = component.getOldShift();

        if (shifts.size() != 0) {
            shiftsList.add(new LabelValueBean("(escolher)", ""));
            InfoShift infoShift;
            Iterator iter = shifts.iterator();
            String label, value;
            while (iter.hasNext()) {
                infoShift = (InfoShift) iter.next();
                value = infoShift.getExternalId().toString();
                label = infoShift.getNome();
                shiftsList.add(new LabelValueBean(label, value));
            }
            request.setAttribute("shiftsList", shiftsList);
        }
        if (!StringUtils.isEmpty(shiftCodeString)) {
            request.setAttribute("shift", oldInfoShift);
        }
        return forward(request, "/teacher/editStudentGroupShift_bd.jsp");
    }

    public ActionForward editStudentGroupShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String objectCode = getExecutionCourse(request).getExternalId();
        DynaActionForm editStudentGroupForm = (DynaActionForm) form;
        String oldShiftString = request.getParameter("shiftCode");
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        String newShiftString = (String) editStudentGroupForm.get("shift");
        if (newShiftString.equals(oldShiftString)) {
            return viewShiftsAndGroups(mapping, form, request, response);
        } else if (newShiftString.equals("")) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("errors.invalid.insert.studentGroupShift");
            actionErrors.add("errors.invalid.insert.studentGroupShift", error);
            saveErrors(request, actionErrors);
            return prepareEditStudentGroupShift(mapping, form, request, response);
        } else {

            try {
                EditStudentGroupShift.runEditStudentGroupShift(objectCode, studentGroupCodeString, groupPropertiesCodeString,
                        newShiftString);
            } catch (ExistingServiceException e) {
                ActionMessages actionErrors = new ActionMessages();
                ActionMessage error = null;
                error = new ActionMessage("error.noGroupProperties");
                actionErrors.add("error.noGroupProperties", error);
                saveErrors(request, actionErrors);
                return prepareViewExecutionCourseProjects(mapping, form, request, response);

            } catch (InvalidArgumentsServiceException e) {
                ActionMessages actionErrors = new ActionMessages();
                ActionMessage error = null;
                error = new ActionMessage("error.noGroup");
                actionErrors.add("error.noGroup", error);
                saveErrors(request, actionErrors);
                return viewShiftsAndGroups(mapping, form, request, response);

            } catch (DomainException e) {
                ActionMessages actionErrors = new ActionMessages();
                ActionMessage error = null;
                error = new ActionMessage(e.getArgs()[0]);
                actionErrors.add("error.noGroup", error);
                saveErrors(request, actionErrors);
                return viewShiftsAndGroups(mapping, form, request, response);

            } catch (InvalidChangeServiceException e) {
                ActionMessages actionErrors = new ActionMessages();
                ActionMessage error = null;
                error = new ActionMessage("error.GroupPropertiesShiftTypeChanged");
                actionErrors.add("error.GroupPropertiesShiftTypeChanged", error);
                saveErrors(request, actionErrors);
                return viewShiftsAndGroups(mapping, form, request, response);

            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            return viewShiftsAndGroups(mapping, form, request, response);
        }
    }

    public ActionForward prepareEnrollStudentGroupShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String studentGroupCodeString = request.getParameter("studentGroupCode");
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

        InfoSiteShifts component =
                new TeacherAdministrationSiteComponentBuilder().getInfoSiteShifts(new InfoSiteShifts(),
                        groupPropertiesCodeString, studentGroupCodeString);
        request.setAttribute("component", component);
        if (component == null) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }

        List shifts = component.getShifts();
        if (shifts == null) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        }
        if (shifts.size() == 0) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("errors.shifts.not.available");
            actionErrors.add("errors.shifts.not.available", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        }

        ArrayList shiftsList = new ArrayList();

        if (shifts.size() != 0) {
            shiftsList.add(new LabelValueBean("(escolher)", ""));
            InfoShift infoShift;
            Iterator iter = shifts.iterator();
            String label, value;
            while (iter.hasNext()) {
                infoShift = (InfoShift) iter.next();
                value = infoShift.getExternalId().toString();
                label = infoShift.getNome();
                shiftsList.add(new LabelValueBean(label, value));
            }
            request.setAttribute("shiftsList", shiftsList);
        }

        return forward(request, "/teacher/enrollStudentGroupShift_bd.jsp");
    }

    public ActionForward enrollStudentGroupShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String objectCode = getExecutionCourse(request).getExternalId();
        DynaActionForm enrollStudentGroupForm = (DynaActionForm) form;
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        String newShiftString = (String) enrollStudentGroupForm.get("shift");
        if (newShiftString.equals("")) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("errors.invalid.insert.studentGroupShift");
            actionErrors.add("errors.invalid.insert.studentGroupShift", error);
            saveErrors(request, actionErrors);
            return prepareEnrollStudentGroupShift(mapping, form, request, response);
        }

        try {
            EnrollStudentGroupShift.runEnrollStudentGroupShift(objectCode, studentGroupCodeString, groupPropertiesCodeString,
                    newShiftString);
        } catch (ExistingServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidArgumentsServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);

        } catch (InvalidSituationServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noShift");
            actionErrors.add("error.noShift", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);

        } catch (InvalidChangeServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.enrollStudentGroupShift");
            actionErrors.add("error.enrollStudentGroupShift", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return viewShiftsAndGroups(mapping, form, request, response);
    }

    public ActionForward unEnrollStudentGroupShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String objectCode = getExecutionCourse(request).getExternalId();
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

        try {
            UnEnrollStudentGroupShift.runUnEnrollStudentGroupShift(objectCode, studentGroupCodeString, groupPropertiesCodeString);
        } catch (ExistingServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidArgumentsServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        } catch (InvalidChangeServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.UnEnrollStudentGroupShift");
            actionErrors.add("error.UnEnrollStudentGroupShift", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return viewShiftsAndGroups(mapping, form, request, response);
    }

    public ActionForward prepareEditStudentGroupMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String studentGroupCodeString = request.getParameter("studentGroupCode");

        String shiftCodeString = request.getParameter("shiftCode");
        request.setAttribute("shiftCode", shiftCodeString);
        String objectCode = getExecutionCourse(request).getExternalId();

        InfoSiteStudentGroup studentGroup =
                new TeacherAdministrationSiteComponentBuilder().getInfoSiteStudentGroup(new InfoSiteStudentGroup(),
                        studentGroupCodeString);
        request.setAttribute("studentGroup", studentGroup);

        if (studentGroup.getInfoSiteStudentInformationList() == null) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        }

        List infoStudentList = null;
        try {
            infoStudentList =
                    PrepareEditStudentGroupMembers.runPrepareEditStudentGroupMembers(objectCode, studentGroupCodeString);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(infoStudentList, new BeanComparator("number"));
        request.setAttribute("infoStudentList", infoStudentList);
        return forward(request, "/teacher/editStudentGroupMembers_bd.jsp");
    }

    public ActionForward prepareEditStudentGroupsShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        String shiftCodeString = request.getParameter("shiftCode");

        InfoSiteStudentGroupAndStudents component =
                new TeacherAdministrationSiteComponentBuilder().getInfoSiteStudentGroupAndStudents(
                        new InfoSiteStudentGroupAndStudents(), groupPropertiesCodeString, shiftCodeString);

        if (component.getInfoSiteStudentsAndShiftByStudentGroupList() == null) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        }
        request.setAttribute("component", component);
        return forward(request, "/teacher/editStudentGroupsShift_bd.jsp");
    }

    public ActionForward editStudentGroupsShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String objectCode = getExecutionCourse(request).getExternalId();
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        String shiftCodeString = request.getParameter("shiftCode");

        DynaActionForm editStudentGroupsShiftForm = (DynaActionForm) form;
        List<String> studentGroupsCodes = Arrays.asList((String[]) editStudentGroupsShiftForm.get("studentGroupsCodes"));

        try {
            EditStudentGroupsShift.runEditStudentGroupsShift(objectCode, groupPropertiesCodeString, shiftCodeString,
                    studentGroupsCodes);
        } catch (ExistingServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (DomainException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage(e.getArgs()[0]);
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidChangeServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noShift");
            actionErrors.add("error.noShift", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage(e.getMessage());
            actionErrors.add("error.studentGroupNotInList", error);
            saveErrors(request, actionErrors);
            return prepareEditStudentGroupsShift(mapping, form, request, response);
        } catch (InvalidArgumentsServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.studentGroupNotFromGroupProperties");
            actionErrors.add("error.studentGroupNotFromGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareEditStudentGroupsShift(mapping, form, request, response);
        } catch (NonValidChangeServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.studentGroupAndGroupPropertiesDifferentShiftTypes");
            actionErrors.add("error.studentGroupAndGroupPropertiesDifferentShiftTypes", error);
            saveErrors(request, actionErrors);
            return viewStudentsAndGroupsByShift(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return prepareEditStudentGroupsShift(mapping, form, request, response);
    }

    public ActionForward insertStudentGroupMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String objectCode = getExecutionCourse(request).getExternalId();
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        DynaActionForm insertStudentGroupForm = (DynaActionForm) form;
        List<String> studentUsernames = Arrays.asList((String[]) insertStudentGroupForm.get("studentsToInsert"));

        try {
            InsertStudentGroupMembers.runInsertStudentGroupMembers(objectCode, studentGroupCodeString, groupPropertiesCodeString,
                    studentUsernames);
        } catch (ExistingServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = new ActionMessage("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        } catch (InvalidChangeServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = new ActionMessage("error.insertStudentGroupMembers.AttendsSet");
            actionErrors.add("error.insertStudentGroupMembers.AttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = new ActionMessage("errors.existing.studentInGroup");
            actionErrors.add("errors.existing.studentInGroup", error);
            saveErrors(request, actionErrors);
            return prepareEditStudentGroupMembers(mapping, form, request, response);
        } catch (InvalidArgumentsServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = new ActionMessage(e.getMessage());
            actionErrors.add("error.editStudentGroupMembers", error);
            saveErrors(request, actionErrors);
            return prepareEditStudentGroupMembers(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return prepareEditStudentGroupMembers(mapping, form, request, response);
    }

    public ActionForward deleteStudentGroupMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String objectCode = getExecutionCourse(request).getExternalId();
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        DynaActionForm deleteStudentGroupForm = (DynaActionForm) form;
        List studentUsernames = Arrays.asList((String[]) deleteStudentGroupForm.get("studentsToRemove"));

        try {
            DeleteStudentGroupMembers.runDeleteStudentGroupMembers(objectCode, studentGroupCodeString, groupPropertiesCodeString,
                    studentUsernames);
        } catch (ExistingServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        } catch (InvalidChangeServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.deleteStudentGroupMembers.AttendsSet");
            actionErrors.add("error.deleteStudentGroupMembers.AttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("errors.notExisting.studentInGroup");
            actionErrors.add("errors.notExisting.studentInGroup", error);
            saveErrors(request, actionErrors);
            return prepareEditStudentGroupMembers(mapping, form, request, response);
        } catch (InvalidArgumentsServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("errors.notExisting.studentInAttendsSet");
            actionErrors.add("errors.notExisting.studentInAttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareEditStudentGroupMembers(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return prepareEditStudentGroupMembers(mapping, form, request, response);
    }

    public ActionForward viewAttendsSet(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Grouping grouping = getDomainObject(request, "groupPropertiesCode");

        InfoGrouping infoGrouping = InfoGroupingWithAttends.newInfoFromDomain(grouping);
        request.setAttribute("infoGrouping", infoGrouping);

        if (request.getParameter("showPhotos") == null) {
            request.setAttribute("showPhotos", "false");
        }

        Collections.sort(infoGrouping.getInfoAttends(), new BeanComparator("aluno.number"));
        return forward(request, "/teacher/viewAttendsSet_bd.jsp");
    }

    public ActionForward prepareEditAttendsSetMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String groupingIDString = request.getParameter("groupPropertiesCode");

        String objectCode = getExecutionCourse(request).getExternalId();

        if (request.getParameter("showPhotos") == null) {
            request.setAttribute("showPhotos", "false");
        }

        Grouping grouping = FenixFramework.getDomainObject(groupingIDString);
        InfoGrouping infoGrouping = InfoGroupingWithAttends.newInfoFromDomain(grouping);

        List infoStudentList = null;
        try {
            infoStudentList = PrepareEditGroupingMembers.run(objectCode, groupingIDString);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(infoGrouping.getInfoAttends(), new BeanComparator("aluno.number"));
        Collections.sort(infoStudentList, new BeanComparator("number"));
        request.setAttribute("infoGrouping", infoGrouping);
        request.setAttribute("infoStudentList", infoStudentList);
        return forward(request, "/teacher/editAttendsSetMembers_bd.jsp");
    }

    public ActionForward insertAttendsSetMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String objectCode = getExecutionCourse(request).getExternalId();
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        DynaActionForm insertAttendsSetForm = (DynaActionForm) form;
        List<String> studentCodes = Arrays.asList((String[]) insertAttendsSetForm.get("studentCodesToInsert"));

        try {
            InsertGroupingMembers.runInsertGroupingMembers(objectCode, groupPropertiesCodeString, studentCodes);
        } catch (ExistingServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noAttendsSet");
            actionErrors.add("error.noAttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("errors.existing.studentInAttendsSet");
            actionErrors.add("errors.existing.studentInAttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareEditAttendsSetMembers(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return prepareEditAttendsSetMembers(mapping, form, request, response);
    }

    public ActionForward prepareInsertStudentsInAttendsSet(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

        String objectCode = getExecutionCourse(request).getExternalId();

        List infoStudentList = null;
        try {
            infoStudentList = PrepareEditGroupingMembers.run(objectCode, groupPropertiesCodeString);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(infoStudentList, new BeanComparator("number"));
        request.setAttribute("infoStudentList", infoStudentList);
        return forward(request, "/teacher/insertStudentsInAttendsSet_bd.jsp");
    }

    public ActionForward insertStudentsInAttendsSet(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String objectCode = getExecutionCourse(request).getExternalId();
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        String[] selected = request.getParameterValues("selected");

        try {
            InsertStudentsInGrouping.runInsertStudentsInGrouping(objectCode, groupPropertiesCodeString, selected);
        } catch (ExistingServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noAttendsSet");
            actionErrors.add("error.noAttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("errors.existing.studentInAttendsSet");
            actionErrors.add("errors.existing.studentInAttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareInsertStudentsInAttendsSet(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return viewShiftsAndGroups(mapping, form, request, response);
    }

    public ActionForward deleteAttendsSetMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String objectCode = getExecutionCourse(request).getExternalId();
        DynaActionForm deleteAttendsSetForm = (DynaActionForm) form;
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        List<String> studentUsernames = Arrays.asList((String[]) deleteAttendsSetForm.get("studentsToRemove"));

        try {
            DeleteGroupingMembers.runDeleteGroupingMembers(objectCode, groupPropertiesCodeString, studentUsernames);
        } catch (ExistingServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noAttendsSet");
            actionErrors.add("error.noAttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("errors.notExisting.studentInAttendsSet");
            actionErrors.add("errors.notExisting.studentInAttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareEditAttendsSetMembers(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return prepareEditAttendsSetMembers(mapping, form, request, response);
    }

    public ActionForward deleteAttendsSetMembersByExecutionCourse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        String objectCode = getExecutionCourse(request).getExternalId();
        String groupingOIDString = request.getParameter("groupingOID");

        try {
            DeleteGroupingMembersByExecutionCourseID.runDeleteGroupingMembersByExecutionCourseID(objectCode, groupingOIDString);
        } catch (ExistingServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noAttendsSet");
            actionErrors.add("error.noAttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noExecutionCourse");
            actionErrors.add("error.noExecutionCourse", error);
            saveErrors(request, actionErrors);
            return viewAttendsSet(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return viewAttendsSet(mapping, form, request, response);
    }

    public ActionForward deleteAllAttendsSetMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String objectCode = getExecutionCourse(request).getExternalId();
        String groupingOIDString = request.getParameter("groupingOID");

        try {
            DeleteAllGroupingMembers.runDeleteAllGroupingMembers(objectCode, groupingOIDString);
        } catch (ExistingServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            ActionMessage error = null;
            error = new ActionMessage("error.noAttendsSet");
            actionErrors.add("error.noAttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return viewAttendsSet(mapping, form, request, response);
    }

    public ActionForward deleteProjectProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        User userView = getUserView(request);
        String objectCode = getExecutionCourse(request).getExternalId();
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        String executionCourseCodeString = request.getParameter("executionCourseCode");

        try {
            DeleteProjectProposal.runDeleteProjectProposal(objectCode, groupPropertiesCodeString, executionCourseCodeString,
                    userView.getUsername());
            ActionMessages actionErrors = new ActionMessages();
            actionErrors.add("sucessfull", new ActionMessage("error.DeleteProjectProposal"));
            saveErrors(request, actionErrors);

        } catch (InvalidArgumentsServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            actionErrors.add(e.getMessage(), new ActionMessage(e.getMessage()));
            saveErrors(request, actionErrors);

            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (FenixServiceException e) {
            logger.error(e.getMessage(), e);
            throw new FenixActionException(e.getMessage());
        }

        return prepareViewExecutionCourseProjects(mapping, form, request, response);
    }

    public ActionForward firstPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String objectCode = getExecutionCourse(request).getExternalId();
        User userView = getUserView(request);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        String goalExecutionCourseIdString = request.getParameter("goalExecutionCourseId");

        Boolean type = null;
        try {
            type =
                    NewProjectProposal.runNewProjectProposal(objectCode, goalExecutionCourseIdString, groupPropertiesCodeString,
                            userView.getUsername());
        } catch (InvalidArgumentsServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            actionErrors.add(e.getMessage(), new ActionMessage(e.getMessage()));
            saveErrors(request, actionErrors);

            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionMessages actionErrors = new ActionMessages();
            actionErrors.add(e.getMessage(), new ActionMessage(e.getMessage()));
            saveErrors(request, actionErrors);

            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (type.booleanValue() == false) {
            ActionMessages actionErrors = new ActionMessages();
            actionErrors.add("sucessfull", new ActionMessage("error.NewProjectProposalSucessfull"));
            saveErrors(request, actionErrors);
        } else {
            ActionMessages actionErrors = new ActionMessages();
            actionErrors.add("sucessfull", new ActionMessage("error.NewProjectCreated"));
            saveErrors(request, actionErrors);
        }
        return prepareViewExecutionCourseProjects(mapping, form, request, response);
    }

}