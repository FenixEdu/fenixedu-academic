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
/*
 * Created 2004/04/13
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.finalDegreeWork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionDegreesByExecutionYearAndType;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.AddFinalDegreeWorkProposalCandidacyForGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.student.AddStudentToFinalDegreeWorkStudentGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ChangePreferenceOrderOfFinalDegreeWorkStudentGroupCandidacy;
import net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork;
import net.sourceforge.fenixedu.applicationTier.Servico.student.EstablishFinalDegreeWorkStudentGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadActiveStudentCurricularPlanByDegreeType;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadAvailableFinalDegreeWorkProposalHeadersForGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadFinalDegreeWorkStudentGroupByUsername;
import net.sourceforge.fenixedu.applicationTier.Servico.student.RemoveProposalFromFinalDegreeWorkStudentGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.student.RemoveStudentFromFinalDegreeWorkStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroup;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupStudent;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentSeniorsApp;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 */
@StrutsFunctionality(app = StudentSeniorsApp.class, path = "disseration-candidacy", titleKey = "link.finalDegreeWork.candidacy")
@Mapping(module = "student", path = "/finalDegreeWorkCandidacy", input = "/finalDegreeWork/candidacy.jsp",
        formBean = "finalDegreeWorkCandidacyForm")
@Forwards({ @Forward(name = "showDissertationsInfo", path = "/student/finalDegreeWork/dissertations.jsp"),
        @Forward(name = "showSelectProposalsForm", path = "/student/finalDegreeWork/selectProposals.jsp"),
        @Forward(name = "showCandidacyForm", path = "/student/finalDegreeWork/candidacy.jsp") })
@Exceptions(
        value = {
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.NotCompletedCurricularYearException.class,
                        key = "error.message.NotCompletedCurricularYearException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.NumberOfNecessaryCompletedCreditsInSecondCycleNotSpecifiedException.class,
                        key = "error.message.NumberOfNecessaryCompletedCreditsInSecondCycleNotSpecifiedException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.EnrolmentInDissertationIsRequired.class,
                        key = "error.message.EnrolmentInDissertationIsRequired",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddStudentToFinalDegreeWorkStudentGroup.MinimumCompletedCreditsSecondCycleUndefinedException.class,
                        key = "error.message.InsufficientCompletedCreditsInSecondCycleException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.InsufficientCompletedCreditsInSecondCycleException.class,
                        key = "error.message.InsufficientCompletedCreditsInSecondCycleException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.RemoveStudentFromFinalDegreeWorkStudentGroup.GroupProposalCandidaciesExistException.class,
                        key = "error.message.GroupProposalCandidaciesExistException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddFinalDegreeWorkProposalCandidacyForGroup.MinimumNumberOfStudentsUndefinedException.class,
                        key = "error.message.MinimumNumberOfStudentsUndefinedException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.CandidacyPeriodNotDefinedException.class,
                        key = "error.message.CandidacyPeriodNotDefinedException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddStudentToFinalDegreeWorkStudentGroup.MaximumNumberOfStudentsUndefinedException.class,
                        key = "error.message.MaximumNumberOfStudentsUndefinedException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddStudentToFinalDegreeWorkStudentGroup.MinimumCompletedCreditsSecondCycleNotReachedException.class,
                        key = "error.message.InsufficientCompletedCreditsInSecondCycleException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddFinalDegreeWorkProposalCandidacyForGroup.MaximumNumberOfProposalCandidaciesPerGroupUndefinedException.class,
                        key = "error.message.MaximumNumberOfProposalCandidaciesPerGroupUndefinedException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddStudentToFinalDegreeWorkStudentGroup.MinimumCompletedCreditsFirstCycleUndefinedException.class,
                        key = "error.message.InsufficientCompletedCreditsInFirstCycleException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.presentationTier.Action.student.finalDegreeWork.FinalDegreeWorkCandidacyDA.NoDegreeStudentCurricularPlanFoundException.class,
                        key = "error.message.NoDegreeStudentCurricularPlanFoundException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.CandidacyInOtherExecutionDegreesNotAllowed.class,
                        key = "error.message.CandidacyInOtherExecutionDegreesNotAllowed",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.OutOfCandidacyPeriodException.class,
                        key = "error.message.OutOfCandidacyPeriodException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddFinalDegreeWorkProposalCandidacyForGroup.MinimumNumberOfStudentsNotReachedException.class,
                        key = "error.message.MinimumNumberOfStudentsNotReachedException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.NoDegreeStudentCurricularPlanFoundException.class,
                        key = "error.message.NoDegreeStudentCurricularPlanFoundException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.InsufficientCompletedCoursesException.class,
                        key = "error.message.InsufficientCompletedCoursesException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddStudentToFinalDegreeWorkStudentGroup.NotCompletedCurricularYearException.class,
                        key = "error.message.NotCompletedCurricularYearException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.RemoveProposalFromFinalDegreeWorkStudentGroup.GroupProposalAttributedByTeacherException.class,
                        key = "error.message.GroupProposalAttributedByTeacherException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.EstablishFinalDegreeWorkStudentGroup.StudentCannotBeACandidateForSelectedDegree.class,
                        key = "error.message.StudentCannotBeACandidateForSelectedDegree",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.RemoveProposalFromFinalDegreeWorkStudentGroup.GroupProposalAttributedException.class,
                        key = "error.message.GroupProposalAttributedException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddStudentToFinalDegreeWorkStudentGroup.MinimumCompletedCreditsFirstCycleNotReachedException.class,
                        key = "error.message.InsufficientCompletedCreditsInFirstCycleException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddStudentToFinalDegreeWorkStudentGroup.MaximumNumberOfStudentsReachedException.class,
                        key = "error.message.MaximumNumberOfStudentsReachedException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.InsufficientCompletedCreditsInFirstCycleException.class,
                        key = "error.message.InsufficientCompletedCreditsInFirstCycleException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.EstablishFinalDegreeWorkStudentGroup.GroupProposalCandidaciesExistException.class,
                        key = "error.message.GroupProposalCandidaciesExistException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.EstablishFinalDegreeWorkStudentGroup.GroupStudentCandidaciesExistException.class,
                        key = "error.message.GroupStudentCandidaciesExistException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddStudentToFinalDegreeWorkStudentGroup.MinimumNumberOfCompletedCoursesNotReachedException.class,
                        key = "error.message.InsufficientCompletedCoursesException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddStudentToFinalDegreeWorkStudentGroup.MinimumNumberOfCompletedCoursesUndefinedException.class,
                        key = "error.message.InsufficientCompletedCoursesException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddFinalDegreeWorkProposalCandidacyForGroup.MaximumNumberOfProposalCandidaciesPerGroupReachedException.class,
                        key = "error.message.MaximumNumberOfProposalCandidaciesPerGroupReachedException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.NumberOfNecessaryCompletedCoursesNotSpecifiedException.class,
                        key = "error.message.NumberOfNecessaryCompletedCoursesNotSpecifiedException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.NumberOfNecessaryCompletedCreditsInFirstCycleNotSpecifiedException.class,
                        key = "error.message.NumberOfNecessaryCompletedCreditsInFirstCycleNotSpecifiedException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request") })
public class FinalDegreeWorkCandidacyDA extends FenixDispatchAction {

    @StrutsFunctionality(app = StudentSeniorsApp.class, path = "dissertations", titleKey = "link.student.finalWorkTitle")
    @Mapping(path = "/finalDegreeWorkDisserations", module = "student", formBean = "finalDegreeWorkCandidacyForm")
    public static class FinalDegreeWorkDisserations extends FinalDegreeWorkCandidacyDA {
        @Override
        @EntryPoint
        public ActionForward dissertations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                HttpServletResponse response) throws Exception {
            return super.dissertations(mapping, form, request, response);
        }
    }

    public class NoDegreeStudentCurricularPlanFoundException extends Exception {
    }

    public ActionForward dissertations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm dynaActionForm = (DynaActionForm) form;

        final ExecutionYear executionYear;
        final String executionYearOID = (String) dynaActionForm.get("executionYearOID");
        if (executionYearOID == null || executionYearOID.equals("")) {
            executionYear = ExecutionYear.readCurrentExecutionYear();
            dynaActionForm.set("executionYearOID", executionYear.getExternalId().toString());
        } else {
            executionYear = FenixFramework.getDomainObject(executionYearOID);
        }

        final Set<ExecutionYear> executionYears = new TreeSet<ExecutionYear>(ExecutionYear.REVERSE_COMPARATOR_BY_YEAR);
        executionYears.addAll(rootDomainObject.getExecutionYearsSet());
        request.setAttribute("executionYears", executionYears);

        List infoExecutionDegrees = placeListOfExecutionDegreesInRequest(request, executionYear);

        return mapping.findForward("showDissertationsInfo");
    }

    public ActionForward selectDissertationsExecutionDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String executionDegreeOID = (String) dynaActionForm.get("executionDegreeOID");
        if (executionDegreeOID != null && executionDegreeOID.length() > 0) {
            ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeOID);
            Scheduleing scheduling = executionDegree.getScheduling();
            request.setAttribute("executionDegree", executionDegree);
            request.setAttribute("scheduling", scheduling);
        }
        return dissertations(mapping, form, request, response);
    }

    public ActionForward selectDissertationsExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String executionYearOID = (String) dynaActionForm.get("executionYearOID");
        if (executionYearOID != null && executionYearOID.length() > 0) {
            ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearOID);
            placeListOfExecutionDegreesInRequest(request, executionYear);
        }
        return dissertations(mapping, form, request, response);
    }

    @EntryPoint
    public ActionForward prepareCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm dynaActionForm = (DynaActionForm) form;

        final ExecutionYear executionYear;
        final String executionYearOID = (String) dynaActionForm.get("executionYearOID");
        if (executionYearOID == null || executionYearOID.equals("")) {
            executionYear = ExecutionYear.readCurrentExecutionYear();
            dynaActionForm.set("executionYearOID", executionYear.getExternalId().toString());
        } else {
            executionYear = FenixFramework.getDomainObject(executionYearOID);
        }

        final Set<ExecutionYear> executionYears = new TreeSet<ExecutionYear>(ExecutionYear.REVERSE_COMPARATOR_BY_YEAR);
        executionYears.addAll(rootDomainObject.getExecutionYearsSet());
        request.setAttribute("executionYears", executionYears);

        // InfoGroup infoGroup = fillOutFinalDegreeWorkCandidacyForm(form,
        // request, executionYear);

        /* List infoExecutionDegrees = */placeListOfExecutionDegreesInRequest(request, executionYear);

        // setDefaultExecutionDegree(form, request, infoExecutionDegrees);

        // String executionDegreeOID = (String)
        // dynaActionForm.get("executionDegreeOID");
        // if (executionDegreeOID != null && !executionDegreeOID.equals("")) {
        // // the student's curricular plan may not have an executionDegree
        // User userView = Authenticate.getUser();
        // checkCandidacyConditions(userView, executionDegreeOID);

        // request.setAttribute("infoGroup", infoGroup);

        // String externalId = (String) dynaActionForm.get("externalId");
        // if ((externalId == null || externalId.equals("")) &&
        // request.getAttribute("CalledFromSelect") == null) {
        // selectExecutionDegree(mapping, form, request, response);
        // }
        // }

        return mapping.findForward("showCandidacyForm");
    }

    public ActionForward selectExecutionDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionDegree executionDegree = getDomainObject(request, "executionDegreeOID");
        if (executionDegree != null) {
            final User userView = Authenticate.getUser();
            try {
                checkCandidacyConditions(userView, executionDegree);
                EstablishFinalDegreeWorkStudentGroup.run(userView.getPerson(), executionDegree);
                final InfoGroup infoGroup = fillOutFinalDegreeWorkCandidacyForm(form, request, executionDegree);
                request.setAttribute("infoGroup", infoGroup);
            } catch (final FenixServiceException ex) {
                request.setAttribute("CalledFromSelect", Boolean.TRUE);
                prepareCandidacy(mapping, form, request, response);
                throw ex;
            }
        }
        return mapping.findForward("showCandidacyForm");
    }

    public ActionForward selectExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // DynaActionForm dynaActionForm = (DynaActionForm) form;
        // String executionDegreeOID = (String)
        // dynaActionForm.get("executionDegreeOID");
        // if (executionDegreeOID != null && !executionDegreeOID.equals("")) {
        // User userView = Authenticate.getUser();
        // try {
        // EstablishFinalDegreeWorkStudentGroup.run(userView.getPerson(), new
        // Integer(executionDegreeOID));
        // } catch (FenixServiceException ex) {
        // request.setAttribute("CalledFromSelect", Boolean.TRUE);
        // dynaActionForm.set("executionDegreeOID", null);
        // prepareCandidacy(mapping, form, request, response);
        // throw ex;
        // }
        // }
        request.setAttribute("CalledFromSelect", Boolean.TRUE);
        return prepareCandidacy(mapping, form, request, response);
    }

    public ActionForward addStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String externalId = (String) dynaActionForm.get("externalId");
        String studentUsernameToAdd = (String) dynaActionForm.get("studentUsernameToAdd");

        User userView = Authenticate.getUser();
        if (studentUsernameToAdd != null && !studentUsernameToAdd.equals("")
                && !studentUsernameToAdd.equalsIgnoreCase(userView.getUsername()) && externalId != null && !externalId.equals("")) {

            try {
                AddStudentToFinalDegreeWorkStudentGroup.run(externalId, studentUsernameToAdd);
            } catch (FenixServiceException ex) {
                prepareCandidacy(mapping, form, request, response);
                throw ex;
            }
        }

        dynaActionForm.set("studentUsernameToAdd", null);
        request.setAttribute("finalDegreeWorkCandidacyForm", dynaActionForm);
        return prepareCandidacy(mapping, form, request, response);
    }

    public ActionForward removeStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String externalId = (String) dynaActionForm.get("externalId");
        String studentToRemove = (String) dynaActionForm.get("studentToRemove");

        User userView = Authenticate.getUser();
        if (studentToRemove != null && !studentToRemove.equals("") && externalId != null && !externalId.equals("")) {

            try {
                RemoveStudentFromFinalDegreeWorkStudentGroup.run(userView.getUsername(), externalId, studentToRemove);
            } catch (FenixServiceException ex) {
                prepareCandidacy(mapping, form, request, response);
                throw ex;
            }
        }

        dynaActionForm.set("studentToRemove", null);
        request.setAttribute("finalDegreeWorkCandidacyForm", dynaActionForm);
        return prepareCandidacy(mapping, form, request, response);
    }

    public ActionForward selectProposals(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String groupOID = (String) dynaActionForm.get("externalId");

        if (groupOID != null && !groupOID.equals("") && StringUtils.isNumeric(groupOID)) {
            User userView = Authenticate.getUser();

            List finalDegreeWorkProposalHeaders = ReadAvailableFinalDegreeWorkProposalHeadersForGroup.run(groupOID);
            request.setAttribute("finalDegreeWorkProposalHeaders", finalDegreeWorkProposalHeaders);
        }

        return mapping.findForward("showSelectProposalsForm");
    }

    public ActionForward addProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String groupOID = (String) dynaActionForm.get("externalId");
        String selectedProposal = (String) dynaActionForm.get("selectedProposal");

        if (groupOID != null && !groupOID.equals("") && StringUtils.isNumeric(groupOID) && selectedProposal != null
                && !selectedProposal.equals("") && StringUtils.isNumeric(selectedProposal)) {
            try {
                final FinalDegreeWorkGroup group = FenixFramework.getDomainObject(groupOID);
                request.setAttribute("infoGroup", InfoGroup.newInfoFromDomain(group));
                AddFinalDegreeWorkProposalCandidacyForGroup.run(group, selectedProposal);
                return mapping.findForward("showCandidacyForm");
            } catch (FenixServiceException ex) {
                prepareCandidacy(mapping, form, request, response);
                throw ex;
            }
        }

        return prepareCandidacy(mapping, form, request, response);
    }

    public ActionForward removeProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String externalId = (String) dynaActionForm.get("externalId");
        String selectedGroupProposal = (String) dynaActionForm.get("selectedGroupProposal");

        User userView = Authenticate.getUser();
        if (selectedGroupProposal != null && !selectedGroupProposal.equals("") && StringUtils.isNumeric(selectedGroupProposal)
                && externalId != null && !externalId.equals("") && StringUtils.isNumeric(externalId)) {
            try {
                final FinalDegreeWorkGroup group = FenixFramework.getDomainObject(externalId);
                RemoveProposalFromFinalDegreeWorkStudentGroup.run(group, selectedGroupProposal);
                request.setAttribute("infoGroup", InfoGroup.newInfoFromDomain(group));
                return mapping.findForward("showCandidacyForm");
            } catch (FenixServiceException ex) {
                prepareCandidacy(mapping, form, request, response);
                throw ex;
            }
        }

        dynaActionForm.set("selectedGroupProposal", null);
        request.setAttribute("finalDegreeWorkCandidacyForm", dynaActionForm);
        return prepareCandidacy(mapping, form, request, response);
    }

    public ActionForward changePreferenceOrder(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String externalId = (String) dynaActionForm.get("externalId");
        String selectedGroupProposal = (String) dynaActionForm.get("selectedGroupProposal");
        String orderOfProposalPreference = request.getParameter("orderOfProposalPreference" + selectedGroupProposal);

        User userView = Authenticate.getUser();
        if (selectedGroupProposal != null && !selectedGroupProposal.equals("") && StringUtils.isNumeric(selectedGroupProposal)
                && externalId != null && !externalId.equals("") && StringUtils.isNumeric(externalId)
                && orderOfProposalPreference != null && !orderOfProposalPreference.equals("")
                && StringUtils.isNumeric(orderOfProposalPreference)) {

            final FinalDegreeWorkGroup group = FenixFramework.getDomainObject(externalId);
            request.setAttribute("infoGroup", InfoGroup.newInfoFromDomain(group));
            ChangePreferenceOrderOfFinalDegreeWorkStudentGroupCandidacy.run(group, selectedGroupProposal, new Integer(
                    orderOfProposalPreference));
        }

        dynaActionForm.set("selectedGroupProposal", null);
        request.setAttribute("finalDegreeWorkCandidacyForm", dynaActionForm);
        return mapping.findForward("showCandidacyForm");
    }

    private boolean checkCandidacyConditions(User userView, final ExecutionDegree executionDegree) throws FenixServiceException {
        return executionDegree == null || CheckCandidacyConditionsForFinalDegreeWork.run(userView, executionDegree);
    }

    private InfoGroup fillOutFinalDegreeWorkCandidacyForm(ActionForm form, HttpServletRequest request,
            ExecutionDegree executionDegree) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;

        User userView = Authenticate.getUser();

        InfoGroup infoGroup = ReadFinalDegreeWorkStudentGroupByUsername.run(userView.getPerson(), executionDegree);

        if (infoGroup != null) {
            if (infoGroup.getExecutionDegree() != null && infoGroup.getExecutionDegree().getExternalId() != null) {
                String executionDegreeOID = infoGroup.getExecutionDegree().getExternalId().toString();
                dynaActionForm.set("executionDegreeOID", executionDegreeOID);
            }
            if (infoGroup.getGroupStudents() != null && !infoGroup.getGroupStudents().isEmpty()) {
                String[] students = new String[infoGroup.getGroupStudents().size()];
                for (int i = 0; i < infoGroup.getGroupStudents().size(); i++) {
                    InfoGroupStudent infoGroupStudent = infoGroup.getGroupStudents().get(i);
                    students[i] = infoGroupStudent.getStudent().getExternalId().toString();
                }
                dynaActionForm.set("students", students);
            }
            if (infoGroup.getExternalId() != null) {
                dynaActionForm.set("externalId", infoGroup.getExternalId().toString());
            }
            Collections.sort(infoGroup.getGroupProposals(), new BeanComparator("orderOfPreference"));
            request.setAttribute("finalDegreeWorkCandidacyForm", dynaActionForm);
        }
        return infoGroup;
    }

    private void setDefaultExecutionDegree(ActionForm form, HttpServletRequest request, List infoExecutionDegrees)
            throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String executionDegreeOID = (String) dynaActionForm.get("executionDegreeOID");

        if ((executionDegreeOID == null || executionDegreeOID.length() == 0 || executionDegreeOID.equals(""))
                && infoExecutionDegrees != null && !infoExecutionDegrees.isEmpty()) {
            User userView = Authenticate.getUser();

            InfoStudentCurricularPlan infoStudentCurricularPlan = getDefaultStudentCurricularPlan(userView);
            if (infoStudentCurricularPlan == null) {
                throw new NoDegreeStudentCurricularPlanFoundException();
            }

            InfoExecutionDegree infoExecutionDegree =
                    (InfoExecutionDegree) CollectionUtils.find(infoExecutionDegrees,
                            new PREDICATE_FIND_EXECUTION_DEGREE_BY_DEGREE_CURRICULAR_PLAB(infoStudentCurricularPlan
                                    .getInfoDegreeCurricularPlan().getExternalId()));

            if (infoExecutionDegree != null && infoExecutionDegree.getExternalId() != null) {
                executionDegreeOID = infoExecutionDegree.getExternalId().toString();
                dynaActionForm.set("executionDegreeOID", executionDegreeOID);
                request.setAttribute("finalDegreeWorkCandidacyForm", dynaActionForm);
            }
        }
    }

    private InfoStudentCurricularPlan getDefaultStudentCurricularPlan(User userView) throws FenixServiceException {
        InfoStudentCurricularPlan infoStudentCurricularPlan =
                getDefaultStudentCurricularPlan(userView, DegreeType.BOLONHA_MASTER_DEGREE);
        if (infoStudentCurricularPlan == null) {
            infoStudentCurricularPlan = getDefaultStudentCurricularPlan(userView, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
        }
        if (infoStudentCurricularPlan == null) {
            infoStudentCurricularPlan = getDefaultStudentCurricularPlan(userView, DegreeType.BOLONHA_DEGREE);
        }
        if (infoStudentCurricularPlan == null) {
            infoStudentCurricularPlan = getDefaultStudentCurricularPlan(userView, DegreeType.DEGREE);
        }
        return infoStudentCurricularPlan;
    }

    private InfoStudentCurricularPlan getDefaultStudentCurricularPlan(User userView, final DegreeType degreeType)
            throws FenixServiceException {

        return ReadActiveStudentCurricularPlanByDegreeType.run(userView, degreeType);
    }

    /**
     * @param request
     * @param executionYear
     */
    private List placeListOfExecutionDegreesInRequest(HttpServletRequest request, ExecutionYear executionYear)
            throws FenixServiceException {
        if (executionYear == null) {
            return new ArrayList(0);
        }
        final InfoExecutionYear infoExecutionYear = InfoExecutionYear.newInfoFromDomain(executionYear);

        final Set<DegreeType> degreeTypes = new HashSet<DegreeType>();
        degreeTypes.add(DegreeType.DEGREE);
        degreeTypes.add(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
        degreeTypes.add(DegreeType.BOLONHA_MASTER_DEGREE);

        List infoExecutionDegrees =
                ReadExecutionDegreesByExecutionYearAndType.run(infoExecutionYear.getExternalId(), degreeTypes);
        filterExecutionDegreesForUser(infoExecutionDegrees);
        Collections.sort(infoExecutionDegrees, new BeanComparator("infoDegreeCurricularPlan.infoDegree.nome"));
        request.setAttribute("infoExecutionDegrees", infoExecutionDegrees);

        return infoExecutionDegrees;
    }

    private void filterExecutionDegreesForUser(final List infoExecutionDegrees) {
        final User userView = Authenticate.getUser();
        final Person person = userView == null ? null : userView.getPerson();
        final Student student = person == null ? null : person.getStudent();
        for (final Iterator<InfoExecutionDegree> iterator = infoExecutionDegrees.iterator(); iterator.hasNext();) {
            final InfoExecutionDegree infoExecutionDegree = iterator.next();
            if (!studentHasRegistrationFor(student, infoExecutionDegree.getExecutionDegree())) {
                iterator.remove();
            }
        }
    }

    private boolean studentHasRegistrationFor(final Student student, final ExecutionDegree executionDegree) {
        final Degree degree = executionDegree.getDegree();
        if (student != null) {
            for (final Registration registration : student.getRegistrationsSet()) {
                final Degree degreeFromRegistration = registration.getDegree();
                if (degree == degreeFromRegistration) {
                    return true;
                }
                for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
                    final CycleCurriculumGroup cycleCurriculumGroup = studentCurricularPlan.getSecondCycle();
                    if (cycleCurriculumGroup != null
                            && cycleCurriculumGroup.getDegreeCurricularPlanOfDegreeModule().getDegree() == degree) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private class PREDICATE_FIND_EXECUTION_DEGREE_BY_DEGREE_CURRICULAR_PLAB implements Predicate {

        String degreeCurricularPlanID = null;

        @Override
        public boolean evaluate(Object arg0) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) arg0;
            if (degreeCurricularPlanID != null && infoExecutionDegree != null
                    && infoExecutionDegree.getInfoDegreeCurricularPlan() != null
                    && degreeCurricularPlanID.equals(infoExecutionDegree.getInfoDegreeCurricularPlan().getExternalId())) {
                return true;
            }

            return false;

        }

        public PREDICATE_FIND_EXECUTION_DEGREE_BY_DEGREE_CURRICULAR_PLAB(String degreeCurricularPlanID) {
            super();
            this.degreeCurricularPlanID = degreeCurricularPlanID;
        }
    }

}