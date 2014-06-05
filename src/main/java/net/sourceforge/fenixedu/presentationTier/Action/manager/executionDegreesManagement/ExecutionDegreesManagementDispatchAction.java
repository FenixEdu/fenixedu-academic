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
package net.sourceforge.fenixedu.presentationTier.Action.manager.executionDegreesManagement;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.AddCoordinator;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.RemoveCoordinators;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.ResponsibleCoordinators;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteExecutionDegreesOfDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionDegreesManagement.EditExecutionDegree;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerExecutionsApp;
import net.sourceforge.fenixedu.util.Bundle;

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
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.fenixedu.spaces.domain.Space;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

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
        final Integer coordinatorNumber = (Integer) form.get("coordinatorNumber");
        final String executionDegreeID = (String) form.get("executionDegreeID");
        String istUsername = Employee.readByNumber(coordinatorNumber).getPerson().getIstUsername();

        try {

            AddCoordinator.run(executionDegreeID, istUsername);

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

            final String dateFormat = "dd/MM/yyyy";
            form.set("periodLessonsFirstSemesterBegin", executionDegree.getPeriodLessonsFirstSemester().getStartYearMonthDay()
                    .toString(dateFormat));
            form.set("periodLessonsFirstSemesterEnd", executionDegree.getPeriodLessonsFirstSemester().getEndYearMonthDay()
                    .toString(dateFormat));
            form.set("periodExamsFirstSemesterBegin", executionDegree.getPeriodExamsFirstSemester().getStartYearMonthDay()
                    .toString(dateFormat));
            form.set("periodExamsFirstSemesterEnd",
                    executionDegree.getPeriodExamsFirstSemester().getEndYearMonthDay().toString(dateFormat));
            form.set("periodLessonsSecondSemesterBegin", executionDegree.getPeriodLessonsSecondSemester().getStartYearMonthDay()
                    .toString(dateFormat));
            form.set("periodLessonsSecondSemesterEnd", executionDegree.getPeriodLessonsSecondSemester().getEndYearMonthDay()
                    .toString(dateFormat));
            form.set("periodExamsSecondSemesterBegin", executionDegree.getPeriodExamsSecondSemester().getStartYearMonthDay()
                    .toString(dateFormat));
            form.set("periodExamsSecondSemesterEnd", executionDegree.getPeriodExamsSecondSemester().getEndYearMonthDay()
                    .toString(dateFormat));

            // not all executionDegrees have these periods defined, but should!
            if (executionDegree.getPeriodExamsSpecialSeason() != null) {
                form.set("periodExamsSpecialSeasonBegin", executionDegree.getPeriodExamsSpecialSeason().getStartYearMonthDay()
                        .toString(dateFormat));
                form.set("periodExamsSpecialSeasonEnd", executionDegree.getPeriodExamsSpecialSeason().getEndYearMonthDay()
                        .toString(dateFormat));
            }
            if (executionDegree.getPeriodGradeSubmissionNormalSeasonFirstSemester() != null) {
                form.set("periodGradeSubmissionNormalSeasonFirstSemesterEnd", executionDegree
                        .getPeriodGradeSubmissionNormalSeasonFirstSemester().getEndYearMonthDay().toString(dateFormat));
            }
            if (executionDegree.getPeriodGradeSubmissionNormalSeasonSecondSemester() != null) {
                form.set("periodGradeSubmissionNormalSeasonSecondSemesterEnd", executionDegree
                        .getPeriodGradeSubmissionNormalSeasonSecondSemester().getEndYearMonthDay().toString(dateFormat));
            }
            if (executionDegree.getPeriodGradeSubmissionSpecialSeason() != null) {
                form.set("periodGradeSubmissionSpecialSeasonEnd", executionDegree.getPeriodGradeSubmissionSpecialSeason()
                        .getEndYearMonthDay().toString(dateFormat));
            }
        }
        return mapping.findForward("editExecutionDegree");
    }

    public ActionForward editExecutionDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final String executionDegreeID = (String) form.get("executionDegreeID");
        final String executionYearID = (String) form.get("executionYearID");
        final String campusID = (String) form.get("campusID");
        final String dateFormat = "dd/MM/yyyy";
        final Boolean temporaryExamMap = Boolean.valueOf((String) form.get("temporaryExamMap"));

        try {
            final Date periodLessonsFirstSemesterBegin =
                    DateFormatUtil.parse(dateFormat, (String) form.get("periodLessonsFirstSemesterBegin"));
            final Date periodLessonsFirstSemesterEnd =
                    DateFormatUtil.parse(dateFormat, (String) form.get("periodLessonsFirstSemesterEnd"));
            final Date periodExamsFirstSemesterBegin =
                    DateFormatUtil.parse(dateFormat, (String) form.get("periodExamsFirstSemesterBegin"));
            final Date periodExamsFirstSemesterEnd =
                    DateFormatUtil.parse(dateFormat, (String) form.get("periodExamsFirstSemesterEnd"));
            final Date periodLessonsSecondSemesterBegin =
                    DateFormatUtil.parse(dateFormat, (String) form.get("periodLessonsSecondSemesterBegin"));
            final Date periodLessonsSecondSemesterEnd =
                    DateFormatUtil.parse(dateFormat, (String) form.get("periodLessonsSecondSemesterEnd"));
            final Date periodExamsSecondSemesterBegin =
                    DateFormatUtil.parse(dateFormat, (String) form.get("periodExamsSecondSemesterBegin"));
            final Date periodExamsSecondSemesterEnd =
                    DateFormatUtil.parse(dateFormat, (String) form.get("periodExamsSecondSemesterEnd"));
            final Date periodExamsSpecialSeasonBegin =
                    DateFormatUtil.parse(dateFormat, (String) form.get("periodExamsSpecialSeasonBegin"));
            final Date periodExamsSpecialSeasonEnd =
                    DateFormatUtil.parse(dateFormat, (String) form.get("periodExamsSpecialSeasonEnd"));
            final Date periodGradeSubmissionNormalSeasonFirstSemesterEnd =
                    DateFormatUtil.parse(dateFormat, (String) form.get("periodGradeSubmissionNormalSeasonFirstSemesterEnd"));
            final Date periodGradeSubmissionNormalSeasonSecondSemesterEnd =
                    DateFormatUtil.parse(dateFormat, (String) form.get("periodGradeSubmissionNormalSeasonSecondSemesterEnd"));
            final Date periodGradeSubmissionSpecialSeasonEnd =
                    DateFormatUtil.parse(dateFormat, (String) form.get("periodGradeSubmissionSpecialSeasonEnd"));

            EditExecutionDegree.run(executionDegreeID, executionYearID, campusID, !temporaryExamMap,
                    periodLessonsFirstSemesterBegin, periodLessonsFirstSemesterEnd, periodExamsFirstSemesterBegin,
                    periodExamsFirstSemesterEnd, periodLessonsSecondSemesterBegin, periodLessonsSecondSemesterEnd,
                    periodExamsSecondSemesterBegin, periodExamsSecondSemesterEnd, periodExamsSpecialSeasonBegin,
                    periodExamsSpecialSeasonEnd, periodGradeSubmissionNormalSeasonFirstSemesterEnd,
                    periodGradeSubmissionNormalSeasonSecondSemesterEnd, periodGradeSubmissionSpecialSeasonEnd);

            return readExecutionDegrees(mapping, actionForm, request, response);

        } catch (final ParseException e) {
            addMessage(request, "error.executionDegrees.invalid.date.format");
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
        for (final DegreeType bolonhaDegreeType : DegreeType.values()) {
            degreeTypes
                    .add(new LabelValueBean(BundleUtil.getString(Bundle.ENUMERATION, bolonhaDegreeType.name()), bolonhaDegreeType.name()));
        }
        degreeTypes.add(0, new LabelValueBean(BundleUtil.getString(Bundle.ENUMERATION, "dropDown.Default"), ""));
        request.setAttribute("degreeTypes", degreeTypes);
    }

    private void readAndSetDegreeCurricularPlans(HttpServletRequest request, final String degreeTypeName) {

        final List<DegreeCurricularPlan> toShow = new ArrayList<DegreeCurricularPlan>();
        for (final DegreeCurricularPlan degreeCurricularPlan : DegreeCurricularPlan.readNotEmptyDegreeCurricularPlans()) {
            if (degreeCurricularPlan.getDegree().getDegreeType().name().equals(degreeTypeName)
                    && degreeCurricularPlan.hasAnyExecutionDegrees()) {
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