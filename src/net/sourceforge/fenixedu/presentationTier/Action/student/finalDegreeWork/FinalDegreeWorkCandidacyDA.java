/*
 * Created 2004/04/13
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.finalDegreeWork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
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
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author Luis Cruz
 */
@Mapping(module = "student", path = "/finalDegreeWorkCandidacy", input = "df.page.finalDegreeWork.candidacy", attribute = "finalDegreeWorkCandidacyForm", formBean = "finalDegreeWorkCandidacyForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "showDissertationsInfo", path = "/student/finalDegreeWork/dissertations.jsp"),
		@Forward(name = "showSelectProposalsForm", path = "/student/finalDegreeWork/selectProposals.jsp"),
		@Forward(name = "showCandidacyForm", path = "/student/finalDegreeWork/candidacy.jsp") })
@Exceptions(value = {
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.NotCompletedCurricularYearException.class, key = "error.message.NotCompletedCurricularYearException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.NumberOfNecessaryCompletedCreditsInSecondCycleNotSpecifiedException.class, key = "error.message.NumberOfNecessaryCompletedCreditsInSecondCycleNotSpecifiedException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddStudentToFinalDegreeWorkStudentGroup.MinimumCompletedCreditsSecondCycleUndefinedException.class, key = "error.message.InsufficientCompletedCreditsInSecondCycleException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.InsufficientCompletedCreditsInSecondCycleException.class, key = "error.message.InsufficientCompletedCreditsInSecondCycleException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.RemoveStudentFromFinalDegreeWorkStudentGroup.GroupProposalCandidaciesExistException.class, key = "error.message.GroupProposalCandidaciesExistException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddFinalDegreeWorkProposalCandidacyForGroup.MinimumNumberOfStudentsUndefinedException.class, key = "error.message.MinimumNumberOfStudentsUndefinedException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.CandidacyPeriodNotDefinedException.class, key = "error.message.CandidacyPeriodNotDefinedException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddStudentToFinalDegreeWorkStudentGroup.MaximumNumberOfStudentsUndefinedException.class, key = "error.message.MaximumNumberOfStudentsUndefinedException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddStudentToFinalDegreeWorkStudentGroup.MinimumCompletedCreditsSecondCycleNotReachedException.class, key = "error.message.InsufficientCompletedCreditsInSecondCycleException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddFinalDegreeWorkProposalCandidacyForGroup.MaximumNumberOfProposalCandidaciesPerGroupUndefinedException.class, key = "error.message.MaximumNumberOfProposalCandidaciesPerGroupUndefinedException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddStudentToFinalDegreeWorkStudentGroup.MinimumCompletedCreditsFirstCycleUndefinedException.class, key = "error.message.InsufficientCompletedCreditsInFirstCycleException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.student.finalDegreeWork.FinalDegreeWorkCandidacyDA.NoDegreeStudentCurricularPlanFoundException.class, key = "error.message.NoDegreeStudentCurricularPlanFoundException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.CandidacyInOtherExecutionDegreesNotAllowed.class, key = "error.message.CandidacyInOtherExecutionDegreesNotAllowed", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.OutOfCandidacyPeriodException.class, key = "error.message.OutOfCandidacyPeriodException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddFinalDegreeWorkProposalCandidacyForGroup.MinimumNumberOfStudentsNotReachedException.class, key = "error.message.MinimumNumberOfStudentsNotReachedException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.NoDegreeStudentCurricularPlanFoundException.class, key = "error.message.NoDegreeStudentCurricularPlanFoundException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.InsufficientCompletedCoursesException.class, key = "error.message.InsufficientCompletedCoursesException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddStudentToFinalDegreeWorkStudentGroup.NotCompletedCurricularYearException.class, key = "error.message.NotCompletedCurricularYearException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.RemoveProposalFromFinalDegreeWorkStudentGroup.GroupProposalAttributedByTeacherException.class, key = "error.message.GroupProposalAttributedByTeacherException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.EstablishFinalDegreeWorkStudentGroup.StudentCannotBeACandidateForSelectedDegree.class, key = "error.message.StudentCannotBeACandidateForSelectedDegree", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.RemoveProposalFromFinalDegreeWorkStudentGroup.GroupProposalAttributedException.class, key = "error.message.GroupProposalAttributedException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddStudentToFinalDegreeWorkStudentGroup.MinimumCompletedCreditsFirstCycleNotReachedException.class, key = "error.message.InsufficientCompletedCreditsInFirstCycleException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddStudentToFinalDegreeWorkStudentGroup.MaximumNumberOfStudentsReachedException.class, key = "error.message.MaximumNumberOfStudentsReachedException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.InsufficientCompletedCreditsInFirstCycleException.class, key = "error.message.InsufficientCompletedCreditsInFirstCycleException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.EstablishFinalDegreeWorkStudentGroup.GroupProposalCandidaciesExistException.class, key = "error.message.GroupProposalCandidaciesExistException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.EstablishFinalDegreeWorkStudentGroup.GroupStudentCandidaciesExistException.class, key = "error.message.GroupStudentCandidaciesExistException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddStudentToFinalDegreeWorkStudentGroup.MinimumNumberOfCompletedCoursesNotReachedException.class, key = "error.message.InsufficientCompletedCoursesException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddStudentToFinalDegreeWorkStudentGroup.MinimumNumberOfCompletedCoursesUndefinedException.class, key = "error.message.InsufficientCompletedCoursesException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.AddFinalDegreeWorkProposalCandidacyForGroup.MaximumNumberOfProposalCandidaciesPerGroupReachedException.class, key = "error.message.MaximumNumberOfProposalCandidaciesPerGroupReachedException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.NumberOfNecessaryCompletedCoursesNotSpecifiedException.class, key = "error.message.NumberOfNecessaryCompletedCoursesNotSpecifiedException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.NumberOfNecessaryCompletedCreditsInFirstCycleNotSpecifiedException.class, key = "error.message.NumberOfNecessaryCompletedCreditsInFirstCycleNotSpecifiedException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class FinalDegreeWorkCandidacyDA extends FenixDispatchAction {

    public class NoDegreeStudentCurricularPlanFoundException extends Exception {
    }

    public ActionForward dissertations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DynaActionForm dynaActionForm = (DynaActionForm) form;

	final ExecutionYear executionYear;
	final String executionYearOID = (String) dynaActionForm.get("executionYearOID");
	if (executionYearOID == null || executionYearOID.equals("")) {
	    executionYear = ExecutionYear.readCurrentExecutionYear();
	    dynaActionForm.set("executionYearOID", executionYear.getIdInternal().toString());
	} else {
	    executionYear = rootDomainObject.readExecutionYearByOID(Integer.valueOf(executionYearOID));
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
	    ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(
		    Integer.valueOf(executionDegreeOID));
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
	    ExecutionYear executionYear = RootDomainObject.getInstance()
		    .readExecutionYearByOID(Integer.valueOf(executionYearOID));
	    placeListOfExecutionDegreesInRequest(request, executionYear);
	}
	return dissertations(mapping, form, request, response);
    }

    public ActionForward prepareCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DynaActionForm dynaActionForm = (DynaActionForm) form;

	final ExecutionYear executionYear;
	final String executionYearOID = (String) dynaActionForm.get("executionYearOID");
	if (executionYearOID == null || executionYearOID.equals("")) {
	    executionYear = ExecutionYear.readCurrentExecutionYear();
	    dynaActionForm.set("executionYearOID", executionYear.getIdInternal().toString());
	} else {
	    executionYear = rootDomainObject.readExecutionYearByOID(Integer.valueOf(executionYearOID));
	}

	final Set<ExecutionYear> executionYears = new TreeSet<ExecutionYear>(ExecutionYear.REVERSE_COMPARATOR_BY_YEAR);
	executionYears.addAll(rootDomainObject.getExecutionYearsSet());
	request.setAttribute("executionYears", executionYears);

	InfoGroup infoGroup = fillOutFinalDegreeWorkCandidacyForm(form, request, executionYear);

	List infoExecutionDegrees = placeListOfExecutionDegreesInRequest(request, executionYear);

	setDefaultExecutionDegree(form, request, infoExecutionDegrees);

	String executionDegreeOID = (String) dynaActionForm.get("executionDegreeOID");
	if (executionDegreeOID != null && !executionDegreeOID.equals("")) {
	    // the student's curricular plan may not have an executionDegree
	    IUserView userView = UserView.getUser();
	    checkCandidacyConditions(userView, executionDegreeOID);

	    request.setAttribute("infoGroup", infoGroup);

	    String idInternal = (String) dynaActionForm.get("idInternal");
	    if ((idInternal == null || idInternal.equals("")) && request.getAttribute("CalledFromSelect") == null) {
		selectExecutionDegree(mapping, form, request, response);
	    }
	}

	return mapping.findForward("showCandidacyForm");
    }

    public ActionForward selectExecutionDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	DynaActionForm dynaActionForm = (DynaActionForm) form;
	String executionDegreeOID = (String) dynaActionForm.get("executionDegreeOID");

	if (executionDegreeOID != null && !executionDegreeOID.equals("") && StringUtils.isNumeric(executionDegreeOID)) {
	    IUserView userView = UserView.getUser();

	    try {
		EstablishFinalDegreeWorkStudentGroup.run(userView.getPerson(), new Integer(executionDegreeOID));
	    } catch (FenixServiceException ex) {
		request.setAttribute("CalledFromSelect", Boolean.TRUE);
		prepareCandidacy(mapping, form, request, response);
		throw ex;
	    }
	}

	request.setAttribute("CalledFromSelect", Boolean.TRUE);
	return prepareCandidacy(mapping, form, request, response);
    }

    public ActionForward selectExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	DynaActionForm dynaActionForm = (DynaActionForm) form;
	String executionDegreeOID = (String) dynaActionForm.get("executionDegreeOID");
	if (executionDegreeOID != null && !executionDegreeOID.equals("")) {
	    IUserView userView = UserView.getUser();
	    try {
		EstablishFinalDegreeWorkStudentGroup.run(userView.getPerson(), new Integer(executionDegreeOID));
	    } catch (FenixServiceException ex) {
		request.setAttribute("CalledFromSelect", Boolean.TRUE);
		dynaActionForm.set("executionDegreeOID", null);
		prepareCandidacy(mapping, form, request, response);
		throw ex;
	    }
	}
	request.setAttribute("CalledFromSelect", Boolean.TRUE);
	return prepareCandidacy(mapping, form, request, response);
    }

    public ActionForward addStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	DynaActionForm dynaActionForm = (DynaActionForm) form;
	String idInternal = (String) dynaActionForm.get("idInternal");
	String studentUsernameToAdd = (String) dynaActionForm.get("studentUsernameToAdd");

	IUserView userView = UserView.getUser();
	if (studentUsernameToAdd != null && !studentUsernameToAdd.equals("")
		&& !studentUsernameToAdd.equalsIgnoreCase(userView.getUtilizador()) && idInternal != null
		&& !idInternal.equals("") && StringUtils.isNumeric(idInternal)) {

	    try {
		AddStudentToFinalDegreeWorkStudentGroup.run(new Integer(idInternal), studentUsernameToAdd);
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
	String idInternal = (String) dynaActionForm.get("idInternal");
	String studentToRemove = (String) dynaActionForm.get("studentToRemove");

	IUserView userView = UserView.getUser();
	if (studentToRemove != null && !studentToRemove.equals("") && StringUtils.isNumeric(studentToRemove)
		&& idInternal != null && !idInternal.equals("") && StringUtils.isNumeric(idInternal)) {

	    try {
		RemoveStudentFromFinalDegreeWorkStudentGroup.run(userView.getUtilizador(), new Integer(idInternal), new Integer(
			studentToRemove));
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
	String groupOID = (String) dynaActionForm.get("idInternal");

	if (groupOID != null && !groupOID.equals("") && StringUtils.isNumeric(groupOID)) {
	    IUserView userView = UserView.getUser();

	    List finalDegreeWorkProposalHeaders = ReadAvailableFinalDegreeWorkProposalHeadersForGroup.run(new Integer(groupOID));
	    request.setAttribute("finalDegreeWorkProposalHeaders", finalDegreeWorkProposalHeaders);
	}

	return mapping.findForward("showSelectProposalsForm");
    }

    public ActionForward addProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	DynaActionForm dynaActionForm = (DynaActionForm) form;
	String groupOID = (String) dynaActionForm.get("idInternal");
	String selectedProposal = (String) dynaActionForm.get("selectedProposal");

	if (groupOID != null && !groupOID.equals("") && StringUtils.isNumeric(groupOID) && selectedProposal != null
		&& !selectedProposal.equals("") && StringUtils.isNumeric(selectedProposal)) {
	    IUserView userView = UserView.getUser();

	    try {
		AddFinalDegreeWorkProposalCandidacyForGroup.run(new Integer(groupOID), new Integer(selectedProposal));
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
	String idInternal = (String) dynaActionForm.get("idInternal");
	String selectedGroupProposal = (String) dynaActionForm.get("selectedGroupProposal");

	IUserView userView = UserView.getUser();
	if (selectedGroupProposal != null && !selectedGroupProposal.equals("") && StringUtils.isNumeric(selectedGroupProposal)
		&& idInternal != null && !idInternal.equals("") && StringUtils.isNumeric(idInternal)) {
	    try {

		RemoveProposalFromFinalDegreeWorkStudentGroup.run(new Integer(idInternal), new Integer(selectedGroupProposal));
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
	String idInternal = (String) dynaActionForm.get("idInternal");
	String selectedGroupProposal = (String) dynaActionForm.get("selectedGroupProposal");
	String orderOfProposalPreference = request.getParameter("orderOfProposalPreference" + selectedGroupProposal);

	IUserView userView = UserView.getUser();
	if (selectedGroupProposal != null && !selectedGroupProposal.equals("") && StringUtils.isNumeric(selectedGroupProposal)
		&& idInternal != null && !idInternal.equals("") && StringUtils.isNumeric(idInternal)
		&& orderOfProposalPreference != null && !orderOfProposalPreference.equals("")
		&& StringUtils.isNumeric(orderOfProposalPreference)) {

	    ChangePreferenceOrderOfFinalDegreeWorkStudentGroupCandidacy.run(new Integer(idInternal), new Integer(
		    selectedGroupProposal), new Integer(orderOfProposalPreference));
	}

	dynaActionForm.set("selectedGroupProposal", null);
	request.setAttribute("finalDegreeWorkCandidacyForm", dynaActionForm);
	return prepareCandidacy(mapping, form, request, response);
    }

    private boolean checkCandidacyConditions(IUserView userView, String executionDegreeOID) throws FenixServiceException,
	    FenixFilterException {

	if (executionDegreeOID != null && executionDegreeOID.length() > 0) {
	    CheckCandidacyConditionsForFinalDegreeWork.run(userView, new Integer(executionDegreeOID));
	}
	return true;
    }

    private InfoGroup fillOutFinalDegreeWorkCandidacyForm(ActionForm form, HttpServletRequest request, ExecutionYear executionYear)
	    throws Exception {
	DynaActionForm dynaActionForm = (DynaActionForm) form;

	IUserView userView = UserView.getUser();

	InfoGroup infoGroup = ReadFinalDegreeWorkStudentGroupByUsername.run(userView.getPerson(), executionYear);

	if (infoGroup != null) {
	    if (infoGroup.getExecutionDegree() != null && infoGroup.getExecutionDegree().getIdInternal() != null) {
		String executionDegreeOID = infoGroup.getExecutionDegree().getIdInternal().toString();
		dynaActionForm.set("executionDegreeOID", executionDegreeOID);
	    }
	    if (infoGroup.getGroupStudents() != null && !infoGroup.getGroupStudents().isEmpty()) {
		String[] students = new String[infoGroup.getGroupStudents().size()];
		for (int i = 0; i < infoGroup.getGroupStudents().size(); i++) {
		    InfoGroupStudent infoGroupStudent = infoGroup.getGroupStudents().get(i);
		    students[i] = infoGroupStudent.getStudent().getIdInternal().toString();
		}
		dynaActionForm.set("students", students);
	    }
	    if (infoGroup.getIdInternal() != null) {
		dynaActionForm.set("idInternal", infoGroup.getIdInternal().toString());
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
	    IUserView userView = UserView.getUser();

	    InfoStudentCurricularPlan infoStudentCurricularPlan = getDefaultStudentCurricularPlan(userView);
	    if (infoStudentCurricularPlan == null) {
		throw new NoDegreeStudentCurricularPlanFoundException();
	    }

	    InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) CollectionUtils.find(infoExecutionDegrees,
		    new PREDICATE_FIND_EXECUTION_DEGREE_BY_DEGREE_CURRICULAR_PLAB(infoStudentCurricularPlan
			    .getInfoDegreeCurricularPlan().getIdInternal()));

	    if (infoExecutionDegree != null && infoExecutionDegree.getIdInternal() != null) {
		executionDegreeOID = infoExecutionDegree.getIdInternal().toString();
		dynaActionForm.set("executionDegreeOID", executionDegreeOID);
		request.setAttribute("finalDegreeWorkCandidacyForm", dynaActionForm);
	    }
	}
    }

    private InfoStudentCurricularPlan getDefaultStudentCurricularPlan(IUserView userView) throws FenixServiceException,
	    FenixFilterException {
	InfoStudentCurricularPlan infoStudentCurricularPlan = getDefaultStudentCurricularPlan(userView,
		DegreeType.BOLONHA_MASTER_DEGREE);
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

    private InfoStudentCurricularPlan getDefaultStudentCurricularPlan(IUserView userView, final DegreeType degreeType)
	    throws FenixServiceException, FenixFilterException {

	return ReadActiveStudentCurricularPlanByDegreeType.run(userView, degreeType);
    }

    /**
     * @param request
     * @param executionYear
     */
    private List placeListOfExecutionDegreesInRequest(HttpServletRequest request, ExecutionYear executionYear)
	    throws FenixServiceException, FenixFilterException {
	if (executionYear == null) {
	    return new ArrayList(0);
	}
	final InfoExecutionYear infoExecutionYear = InfoExecutionYear.newInfoFromDomain(executionYear);

	final Set<DegreeType> degreeTypes = new HashSet<DegreeType>();
	degreeTypes.add(DegreeType.DEGREE);
	degreeTypes.add(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
	degreeTypes.add(DegreeType.BOLONHA_MASTER_DEGREE);

	List infoExecutionDegrees = ReadExecutionDegreesByExecutionYearAndType
		.run(infoExecutionYear.getIdInternal(), degreeTypes);
	Collections.sort(infoExecutionDegrees, new BeanComparator("infoDegreeCurricularPlan.infoDegree.nome"));
	request.setAttribute("infoExecutionDegrees", infoExecutionDegrees);

	return infoExecutionDegrees;
    }

    private class PREDICATE_FIND_EXECUTION_DEGREE_BY_DEGREE_CURRICULAR_PLAB implements Predicate {

	Integer degreeCurricularPlanID = null;

	public boolean evaluate(Object arg0) {
	    InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) arg0;
	    if (degreeCurricularPlanID != null && infoExecutionDegree != null
		    && infoExecutionDegree.getInfoDegreeCurricularPlan() != null
		    && degreeCurricularPlanID.equals(infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal())) {
		return true;
	    }

	    return false;

	}

	public PREDICATE_FIND_EXECUTION_DEGREE_BY_DEGREE_CURRICULAR_PLAB(Integer degreeCurricularPlanID) {
	    super();
	    this.degreeCurricularPlanID = degreeCurricularPlanID;
	}
    }

}