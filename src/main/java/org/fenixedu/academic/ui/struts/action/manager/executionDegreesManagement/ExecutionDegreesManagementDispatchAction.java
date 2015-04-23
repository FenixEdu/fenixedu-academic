/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.manager.executionDegreesManagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.predicate.IllegalDataAccessException;
import org.fenixedu.academic.service.services.coordinator.AddCoordinator;
import org.fenixedu.academic.service.services.coordinator.RemoveCoordinators;
import org.fenixedu.academic.service.services.coordinator.ResponsibleCoordinators;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.services.manager.DeleteExecutionDegreesOfDegreeCurricularPlan;
import org.fenixedu.academic.service.services.manager.executionDegreesManagement.EditExecutionDegree;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerExecutionsApp;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.spaces.domain.Space;

import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ManagerExecutionsApp.class, path = "execution-degrees-management",
        titleKey = "label.manager.executionDegreeManagement")
@Mapping(module = "manager", path = "/executionDegreesManagement",
        input = "/executionDegreesManagement.do?method=readDegreeCurricularPlans", formBean = "executionDegreesManagementForm")
@Forwards({ @Forward(name = "manageCoordinators", path = "/manager/executionDegreesManagement/manageCoordinators.jsp"),
        @Forward(name = "insertCoordinator", path = "/manager/executionDegreesManagement/insertCoordinator.jsp"),
        @Forward(name = "editExecutionDegree", path = "/manager/executionDegreesManagement/editExecutionDegree.jsp"),
        @Forward(name = "executionDegreeManagement", path = "/manager/executionDegreesManagement/executionDegreesManagement.jsp") })
public class ExecutionDegreesManagementDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward readDegreeCurricularPlans(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        readAndSetDegrees(request);
        final DynaActionForm form = (DynaActionForm) actionForm;
        final String degreeType = form.getString("degreeType");
        if (degreeType != null && degreeType.length() != 0) {
            readAndSetDegreeCurricularPlans(request, degreeType);
        }
        return mapping.findForward("executionDegreeManagement");
    }

    public ActionForward readExecutionDegrees(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final String degreeCurricularPlanID = (String) form.get("degreeCurricularPlanID");
        if (!StringUtils.isEmpty(degreeCurricularPlanID)) {
            final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);
            if (degreeCurricularPlan != null) {
                request.setAttribute("executionDegrees", degreeCurricularPlan.getExecutionDegreesSet());
            }
        }
        return readDegreeCurricularPlans(mapping, actionForm, request, response);
    }

    public ActionForward readCoordinators(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final String executionDegreeID = (String) form.get("executionDegreeID");
        if (!StringUtils.isEmpty(executionDegreeID)) {
            final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeID);

            request.setAttribute("executionDegree", executionDegree);
            setResponsibleCoordinatorsIDs(executionDegree, form);
            return mapping.findForward("manageCoordinators");

        } else {
            return mapping.getInputForward();
        }
    }

    private void setResponsibleCoordinatorsIDs(ExecutionDegree executionDegree, DynaActionForm form) {
        final List<String> responsibleCoordinatorsList = new ArrayList<String>();
        for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
            if (coordinator.isResponsible()) {
                responsibleCoordinatorsList.add(coordinator.getExternalId());
            }
        }
        form.set("responsibleCoordinatorsIDs",
                responsibleCoordinatorsList.toArray(new String[responsibleCoordinatorsList.size()]));
    }

    public ActionForward prepareInsertCoordinator(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return readExecutionDegree(mapping, actionForm, request, "insertCoordinator");
    }

    public ActionForward insertCoordinator(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final String coordinatorID = (String) form.get("coordinatorID");
        final String executionDegreeID = (String) form.get("executionDegreeID");

        try {

            AddCoordinator.run(executionDegreeID, coordinatorID);

        } catch (final IllegalDataAccessException e) {
            addMessage(request, "error.notAuthorized");
        } catch (final FenixServiceException e) {
            addMessage(request, e.getMessage());
        } catch (final DomainException e) {
            addMessage(request, e.getMessage());
        }
        return readCoordinators(mapping, actionForm, request, response);
    }

    public ActionForward saveCoordinatorsInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final String executionDegreeID = (String) form.get("executionDegreeID");

        try {
            final String[] coordinatorsToBeResponsibleIDs = (String[]) form.get("responsibleCoordinatorsIDs");
            ResponsibleCoordinators.run(executionDegreeID, Arrays.asList(coordinatorsToBeResponsibleIDs));

            final String[] coordinatorsToRemoveIDs = (String[]) form.get("removeCoordinatorsIDs");
            RemoveCoordinators.run(executionDegreeID, Arrays.asList(coordinatorsToRemoveIDs));

        } catch (final IllegalDataAccessException e) {
            addMessage(request, "error.notAuthorized");
        } catch (final FenixServiceException e) {
            addMessage(request, e.getMessage());
        } catch (final DomainException e) {
            addMessage(request, e.getMessage());
        }
        return readCoordinators(mapping, actionForm, request, response);
    }

    public ActionForward prepareEditExecutionDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final String executionDegreeID = (String) form.get("executionDegreeID");
        if (!StringUtils.isEmpty(executionDegreeID)) {
            final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeID);

            request.setAttribute("executionDegree", executionDegree);
            request.setAttribute("executionYears", ExecutionYear.readNotClosedExecutionYears());
            request.setAttribute("campus", Space.getAllCampus());

            form.set("executionYearID", executionDegree.getExecutionYear().getExternalId());
            form.set("campusID", executionDegree.getCampus().getExternalId());
        }
        return mapping.findForward("editExecutionDegree");
    }

    public ActionForward editExecutionDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final String executionDegreeID = (String) form.get("executionDegreeID");
        final String executionYearID = (String) form.get("executionYearID");
        final String campusID = (String) form.get("campusID");
        final Boolean temporaryExamMap = Boolean.valueOf((String) form.get("temporaryExamMap"));

        try {
            EditExecutionDegree.run(executionDegreeID, executionYearID, campusID, !temporaryExamMap);
            return readExecutionDegrees(mapping, actionForm, request, response);
        } catch (final NotAuthorizedException e) {
            addMessage(request, "error.notAuthorized");
            return readExecutionDegrees(mapping, actionForm, request, response);
        } catch (final FenixServiceException e) {
            addMessage(request, e.getMessage());
        } catch (final DomainException e) {
            addMessage(request, e.getMessage());
        }
        return prepareEditExecutionDegree(mapping, actionForm, request, response);
    }

    private void readAndSetDegrees(HttpServletRequest request) {
        final List<LabelValueBean> degreeTypes = new ArrayList<LabelValueBean>();
        DegreeType.all().forEach(type -> degreeTypes.add(new LabelValueBean(type.getName().getContent(), type.getExternalId())));
        degreeTypes.add(0, new LabelValueBean(BundleUtil.getString(Bundle.ENUMERATION, "dropDown.Default"), ""));
        request.setAttribute("degreeTypes", degreeTypes);
    }

    private void readAndSetDegreeCurricularPlans(HttpServletRequest request, final String degreeTypeName) {

        final List<DegreeCurricularPlan> toShow = new ArrayList<DegreeCurricularPlan>();
        for (final DegreeCurricularPlan degreeCurricularPlan : DegreeCurricularPlan.readNotEmptyDegreeCurricularPlans()) {
            if (degreeCurricularPlan.getDegree().getDegreeType().getExternalId().equals(degreeTypeName)
                    && !degreeCurricularPlan.getExecutionDegreesSet().isEmpty()) {
                toShow.add(degreeCurricularPlan);
            }
        }
        Collections.sort(toShow,
                DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);

        final List<LabelValueBean> degreeCurricularPlans = new ArrayList<LabelValueBean>();
        for (final DegreeCurricularPlan degreeCurricularPlan : toShow) {
            degreeCurricularPlans.add(new LabelValueBean(degreeCurricularPlan.getDegree().getName() + " > "
                    + degreeCurricularPlan.getName(), degreeCurricularPlan.getExternalId().toString()));
        }

        degreeCurricularPlans.add(0, new LabelValueBean(BundleUtil.getString(Bundle.ENUMERATION, "dropDown.Default"), ""));
        request.setAttribute("degreeCurricularPlans", degreeCurricularPlans);
    }

    private ActionForward readExecutionDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            String findForward) {
        final DynaActionForm form = (DynaActionForm) actionForm;
        final String executionDegreeID = (String) form.get("executionDegreeID");
        if (!StringUtils.isEmpty(executionDegreeID)) {
            final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeID);
            request.setAttribute("executionDegree", executionDegree);
            return mapping.findForward(findForward);
        } else {
            return mapping.getInputForward();
        }
    }

    private void addMessage(HttpServletRequest request, String keyMessage) {
        final ActionMessages actionMessages = new ActionMessages();
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(keyMessage));
        saveMessages(request, actionMessages);
    }

    public ActionForward deleteExecutionDegrees(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        DynaActionForm deleteForm = (DynaActionForm) form;
        List<String> executionDegreesIds = Arrays.asList((String[]) deleteForm.get("internalIds"));

        try {

            List<String> undeletedExecutionDegreesYears = DeleteExecutionDegreesOfDegreeCurricularPlan.run(executionDegreesIds);

            if (!undeletedExecutionDegreesYears.isEmpty()) {
                ActionErrors actionErrors = new ActionErrors();
                for (String undeletedExecutionDegreesYear : undeletedExecutionDegreesYears) {
                    // Create an ACTION_ERROR for each EXECUTION_DEGREE
                    ActionError error =
                            new ActionError("errors.invalid.delete.not.empty.execution.degree", undeletedExecutionDegreesYear);
                    actionErrors.add("errors.invalid.delete.not.empty.execution.degree", error);
                }
                saveErrors(request, actionErrors);
            }

            return readExecutionDegrees(mapping, form, request, response);

        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
    }

}