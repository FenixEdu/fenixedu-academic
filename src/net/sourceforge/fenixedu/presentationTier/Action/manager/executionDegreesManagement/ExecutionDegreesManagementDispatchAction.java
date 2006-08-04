package net.sourceforge.fenixedu.presentationTier.Action.manager.executionDegreesManagement;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.DateFormatUtil;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

public class ExecutionDegreesManagementDispatchAction extends FenixDispatchAction {

    public ActionForward readDegreeCurricularPlans(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        readAndSetDegrees(request);
        final DynaActionForm form = (DynaActionForm) actionForm;
        final String degreeType = form.getString("degreeType");
        if (degreeType != null && degreeType.length() != 0) {
            readAndSetDegreeCurricularPlans(request, degreeType);
        }
        return mapping.findForward("executionDegreeManagement");
    }

    public ActionForward readExecutionDegrees(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final Integer degreeCurricularPlanID = (Integer) form.get("degreeCurricularPlanID");
        if (degreeCurricularPlanID != null) {
            final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject
                    .readDegreeCurricularPlanByOID(degreeCurricularPlanID);
            if (degreeCurricularPlan != null) {
                request.setAttribute("executionDegrees", degreeCurricularPlan.getExecutionDegreesSet());
            }
        }
        return readDegreeCurricularPlans(mapping, actionForm, request, response);
    }

    public ActionForward readCoordinators(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final Integer executionDegreeID = (Integer) form.get("executionDegreeID");
        if (executionDegreeID != null) {
            final ExecutionDegree executionDegree = rootDomainObject
                    .readExecutionDegreeByOID(executionDegreeID);

            request.setAttribute("executionDegree", executionDegree);
            setResponsibleCoordinatorsIDs(executionDegree, form);
            return mapping.findForward("manageCoordinators");

        } else {
            return mapping.getInputForward();
        }
    }

    private void setResponsibleCoordinatorsIDs(ExecutionDegree executionDegree, DynaActionForm form) {
        final List<Integer> responsibleCoordinatorsList = new ArrayList<Integer>();
        for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
            if (coordinator.isResponsible()) {
                responsibleCoordinatorsList.add(coordinator.getIdInternal());
            }
        }
        form.set("responsibleCoordinatorsIDs", responsibleCoordinatorsList
                .toArray(new Integer[responsibleCoordinatorsList.size()]));
    }

    public ActionForward prepareInsertCoordinator(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        return readExecutionDegree(mapping, actionForm, request, "insertCoordinator");
    }

    public ActionForward insertCoordinator(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final Integer coordinatorNumber = (Integer) form.get("coordinatorNumber");
        final Integer executionDegreeID = (Integer) form.get("executionDegreeID");

        try {
            final Object[] args = { executionDegreeID, coordinatorNumber };
            ServiceManagerServiceFactory.executeService(getUserView(request), "AddCoordinatorByManager",
                    args);

        } catch (final FenixFilterException e) {
            addMessage(request, "error.notAuthorized");
        } catch (final FenixServiceException e) {
            addMessage(request, e.getMessage());
        } catch (final DomainException e) {
            addMessage(request, e.getMessage());
        }
        return readCoordinators(mapping, actionForm, request, response);
    }

    public ActionForward saveCoordinatorsInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final Integer executionDegreeID = (Integer) form.get("executionDegreeID");

        try {
            final Integer[] coordinatorsToBeResponsibleIDs = (Integer[]) form
                    .get("responsibleCoordinatorsIDs");
            ServiceManagerServiceFactory.executeService(getUserView(request),
                    "ResponsibleCoordinatorsByManager", new Object[] { executionDegreeID,
                            Arrays.asList(coordinatorsToBeResponsibleIDs) });

            final Integer[] coordinatorsToRemoveIDs = (Integer[]) form.get("removeCoordinatorsIDs");
            ServiceManagerServiceFactory.executeService(getUserView(request),
                    "RemoveCoordinatorsByManager", new Object[] { executionDegreeID,
                            Arrays.asList(coordinatorsToRemoveIDs) });

        } catch (final FenixFilterException e) {
            addMessage(request, "error.notAuthorized");
        } catch (final FenixServiceException e) {
            addMessage(request, e.getMessage());
        } catch (final DomainException e) {
            addMessage(request, e.getMessage());
        }
        return readCoordinators(mapping, actionForm, request, response);
    }

    public ActionForward prepareEditExecutionDegree(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final Integer executionDegreeID = (Integer) form.get("executionDegreeID");
        if (executionDegreeID != null) {
            final ExecutionDegree executionDegree = rootDomainObject
                    .readExecutionDegreeByOID(executionDegreeID);

            request.setAttribute("executionDegree", executionDegree);
            request.setAttribute("executionYears", ExecutionYear.readNotClosedExecutionYears());
            request.setAttribute("campus", rootDomainObject.getCampussSet());

            form.set("executionYearID", executionDegree.getExecutionYear().getIdInternal());
            form.set("campusID", executionDegree.getCampus().getIdInternal());

            final String dateFormat = "dd/MM/yyyy";
            form.set("periodLessonsFirstSemesterBegin", executionDegree.getPeriodLessonsFirstSemester()
                    .getStartYearMonthDay().toString(dateFormat));
            form.set("periodLessonsFirstSemesterEnd", executionDegree.getPeriodLessonsFirstSemester()
                    .getEndYearMonthDay().toString(dateFormat));
            form.set("periodExamsFirstSemesterBegin", executionDegree.getPeriodExamsFirstSemester()
                    .getStartYearMonthDay().toString(dateFormat));
            form.set("periodExamsFirstSemesterEnd", executionDegree.getPeriodExamsFirstSemester()
                    .getEndYearMonthDay().toString(dateFormat));
            form.set("periodLessonsSecondSemesterBegin", executionDegree
                    .getPeriodLessonsSecondSemester().getStartYearMonthDay().toString(dateFormat));
            form.set("periodLessonsSecondSemesterEnd", executionDegree.getPeriodLessonsSecondSemester()
                    .getEndYearMonthDay().toString(dateFormat));
            form.set("periodExamsSecondSemesterBegin", executionDegree.getPeriodExamsSecondSemester()
                    .getStartYearMonthDay().toString(dateFormat));
            form.set("periodExamsSecondSemesterEnd", executionDegree.getPeriodExamsSecondSemester()
                    .getEndYearMonthDay().toString(dateFormat));

            // not all executionDegrees have these periods defined, but should!
            if (executionDegree.getPeriodExamsSpecialSeason() != null) {
                form.set("periodExamsSpecialSeasonBegin", executionDegree.getPeriodExamsSpecialSeason()
                        .getStartYearMonthDay().toString(dateFormat));
                form.set("periodExamsSpecialSeasonEnd", executionDegree.getPeriodExamsSpecialSeason()
                        .getEndYearMonthDay().toString(dateFormat));
            }
            if (executionDegree.getPeriodGradeSubmissionNormalSeasonFirstSemester() != null) {
                form.set("periodGradeSubmissionNormalSeasonFirstSemesterEnd", executionDegree
                        .getPeriodGradeSubmissionNormalSeasonFirstSemester().getEndYearMonthDay()
                        .toString(dateFormat));
            }
            if (executionDegree.getPeriodGradeSubmissionNormalSeasonSecondSemester() != null) {
                form.set("periodGradeSubmissionNormalSeasonSecondSemesterEnd", executionDegree
                        .getPeriodGradeSubmissionNormalSeasonSecondSemester().getEndYearMonthDay()
                        .toString(dateFormat));
            }
            if (executionDegree.getPeriodGradeSubmissionSpecialSeason() != null) {
                form.set("periodGradeSubmissionSpecialSeasonEnd", executionDegree
                        .getPeriodGradeSubmissionSpecialSeason().getEndYearMonthDay().toString(
                                dateFormat));
            }
        }
        return mapping.findForward("editExecutionDegree");
    }

    public ActionForward editExecutionDegree(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final Integer executionDegreeID = (Integer) form.get("executionDegreeID");
        final Integer executionYearID = (Integer) form.get("executionYearID");
        final Integer campusID = (Integer) form.get("campusID");
        final String dateFormat = "dd/MM/yyyy";
        final Boolean temporaryExamMap = Boolean.valueOf((String) form.get("temporaryExamMap"));

        try {
            final Date periodLessonsFirstSemesterBegin = DateFormatUtil.parse(dateFormat, (String) form
                    .get("periodLessonsFirstSemesterBegin"));
            final Date periodLessonsFirstSemesterEnd = DateFormatUtil.parse(dateFormat, (String) form
                    .get("periodLessonsFirstSemesterEnd"));
            final Date periodExamsFirstSemesterBegin = DateFormatUtil.parse(dateFormat, (String) form
                    .get("periodExamsFirstSemesterBegin"));
            final Date periodExamsFirstSemesterEnd = DateFormatUtil.parse(dateFormat, (String) form
                    .get("periodExamsFirstSemesterEnd"));
            final Date periodLessonsSecondSemesterBegin = DateFormatUtil.parse(dateFormat, (String) form
                    .get("periodLessonsSecondSemesterBegin"));
            final Date periodLessonsSecondSemesterEnd = DateFormatUtil.parse(dateFormat, (String) form
                    .get("periodLessonsSecondSemesterEnd"));
            final Date periodExamsSecondSemesterBegin = DateFormatUtil.parse(dateFormat, (String) form
                    .get("periodExamsSecondSemesterBegin"));
            final Date periodExamsSecondSemesterEnd = DateFormatUtil.parse(dateFormat, (String) form
                    .get("periodExamsSecondSemesterEnd"));
            final Date periodExamsSpecialSeasonBegin = DateFormatUtil.parse(dateFormat, (String) form
                    .get("periodExamsSpecialSeasonBegin"));
            final Date periodExamsSpecialSeasonEnd = DateFormatUtil.parse(dateFormat, (String) form
                    .get("periodExamsSpecialSeasonEnd"));
            final Date periodGradeSubmissionNormalSeasonFirstSemesterEnd = DateFormatUtil.parse(
                    dateFormat, (String) form.get("periodGradeSubmissionNormalSeasonFirstSemesterEnd"));
            final Date periodGradeSubmissionNormalSeasonSecondSemesterEnd = DateFormatUtil.parse(
                    dateFormat, (String) form.get("periodGradeSubmissionNormalSeasonSecondSemesterEnd"));
            final Date periodGradeSubmissionSpecialSeasonEnd = DateFormatUtil.parse(dateFormat,
                    (String) form.get("periodGradeSubmissionSpecialSeasonEnd"));

            ServiceUtils.executeService(getUserView(request), "EditBolonhaExecutionDegree",
                    new Object[] { executionDegreeID, executionYearID, campusID, temporaryExamMap,
                            periodLessonsFirstSemesterBegin, periodLessonsFirstSemesterEnd,
                            periodExamsFirstSemesterBegin, periodExamsFirstSemesterEnd,
                            periodLessonsSecondSemesterBegin, periodLessonsSecondSemesterEnd,
                            periodExamsSecondSemesterBegin, periodExamsSecondSemesterEnd,
                            periodExamsSpecialSeasonBegin, periodExamsSpecialSeasonEnd,
                            periodGradeSubmissionNormalSeasonFirstSemesterEnd,
                            periodGradeSubmissionNormalSeasonSecondSemesterEnd,
                            periodGradeSubmissionSpecialSeasonEnd });

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
        final ResourceBundle enumerationResources = ResourceBundle.getBundle(
                "resources/EnumerationResources", request.getLocale());

        final List<LabelValueBean> degreeTypes = new ArrayList<LabelValueBean>();
        for (final DegreeType bolonhaDegreeType : DegreeType.values()) {
            if (bolonhaDegreeType.isBolonhaType()) {
                degreeTypes.add(new LabelValueBean(enumerationResources.getString(bolonhaDegreeType
                        .name()), bolonhaDegreeType.name()));
            }
        }
        degreeTypes.add(0, new LabelValueBean(enumerationResources.getString("dropDown.Default"), ""));
        request.setAttribute("degreeTypes", degreeTypes);
    }

    private void readAndSetDegreeCurricularPlans(HttpServletRequest request, final String degreeTypeName) {

        final List<DegreeCurricularPlan> toShow = new ArrayList<DegreeCurricularPlan>();
        for (final DegreeCurricularPlan degreeCurricularPlan : rootDomainObject
                .getDegreeCurricularPlansSet()) {
            if (degreeCurricularPlan.getDegree().getDegreeType().name().equals(degreeTypeName)
                    && degreeCurricularPlan.hasAnyExecutionDegrees()) {
                toShow.add(degreeCurricularPlan);
            }
        }
        Collections
                .sort(
                        toShow,
                        DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);

        final ResourceBundle enumerationResources = ResourceBundle.getBundle(
                "resources/EnumerationResources", request.getLocale());
        final List<LabelValueBean> degreeCurricularPlans = new ArrayList<LabelValueBean>();
        for (final DegreeCurricularPlan degreeCurricularPlan : toShow) {
            degreeCurricularPlans.add(new LabelValueBean(degreeCurricularPlan.getDegree().getName()
                    + " > " + degreeCurricularPlan.getName(), degreeCurricularPlan.getIdInternal()
                    .toString()));
        }

        degreeCurricularPlans.add(0, new LabelValueBean(enumerationResources
                .getString("dropDown.Default"), ""));
        request.setAttribute("degreeCurricularPlans", degreeCurricularPlans);
    }

    private ActionForward readExecutionDegree(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, String findForward) {
        final DynaActionForm form = (DynaActionForm) actionForm;
        final Integer executionDegreeID = (Integer) form.get("executionDegreeID");
        if (executionDegreeID != null) {
            final ExecutionDegree executionDegree = rootDomainObject
                    .readExecutionDegreeByOID(executionDegreeID);
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

    public ActionForward deleteExecutionDegrees(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        DynaActionForm deleteForm = (DynaActionForm) form;
        List<Integer> executionDegreesIds = Arrays.asList((Integer[]) deleteForm.get("internalIds"));

        try {
            Object args[] = { executionDegreesIds };
            List<String> undeletedExecutionDegreesYears = (List<String>) ServiceUtils.executeService(
                    getUserView(request), "DeleteExecutionDegreesOfDegreeCurricularPlan", args);

            if (!undeletedExecutionDegreesYears.isEmpty()) {
                ActionErrors actionErrors = new ActionErrors();
                for (String undeletedExecutionDegreesYear : undeletedExecutionDegreesYears) {
                    // Create an ACTION_ERROR for each EXECUTION_DEGREE
                    ActionError error = new ActionError(
                            "errors.invalid.delete.not.empty.execution.degree",
                            undeletedExecutionDegreesYear);
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
