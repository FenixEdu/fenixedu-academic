package net.sourceforge.fenixedu.presentationTier.Action.BolonhaManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageCompetenceCourseInformationVersions extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Person loggedPerson = getLoggedPerson(request);
	request
		.setAttribute("department", loggedPerson.getEmployee()
			.getCurrentDepartmentWorkingPlace());
	return mapping.findForward("showCourses");
    }

    public ActionForward editVersion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	CompetenceCourseInformationChangeRequest changeRequest = getCompetenceCourseInformationRequest(request);
	CompetenceCourseInformationRequestBean bean = new CompetenceCourseInformationRequestBean(
		changeRequest);
	CompetenceCourseLoadBean loadBean = new CompetenceCourseLoadBean(changeRequest);
	request.setAttribute("beanLoad", loadBean);
	request.setAttribute("bean", bean);
	return mapping.findForward("createVersions");
    }

    public ActionForward prepareCreateVersion(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	CompetenceCourse course = getCompetenceCourse(request);

	ExecutionSemester period = getExecutionPeriod(request);

	CompetenceCourseInformationRequestBean bean = null;
	IViewState viewState = RenderUtils.getViewState("editVersion");
	CompetenceCourseInformation information = null;
	if (viewState != null) {
	    bean = (CompetenceCourseInformationRequestBean) viewState.getMetaObject().getObject();
	    ExecutionSemester beanPeriod = bean.getExecutionPeriod();
	    if (beanPeriod == null) {
		beanPeriod = ExecutionSemester.readActualExecutionSemester();
		bean.setExecutionPeriod(beanPeriod);
	    }
	    information = bean.getCompetenceCourse().findCompetenceCourseInformationForExecutionPeriod(
		    beanPeriod);
	}

	if (bean == null) {
	    bean = new CompetenceCourseInformationRequestBean(course
		    .findCompetenceCourseInformationForExecutionPeriod((period != null) ? period
			    : ExecutionSemester.readActualExecutionSemester()));
	} else {
	    if (information == null) {
		bean.reset();
	    } else {
		bean.update(information);
	    }
	}

	IViewState viewStateLoad = RenderUtils.getViewState("editVersionLoad");
	CompetenceCourseLoadBean load;
	if (viewStateLoad != null) {
	    load = (CompetenceCourseLoadBean) viewStateLoad.getMetaObject().getObject();
	} else {
	    load = (information != null && information.getCompetenceCourseLoadsCount() > 0) ? new CompetenceCourseLoadBean(
		    information.getCompetenceCourseLoads().get(0))
		    : (period != null) ? new CompetenceCourseLoadBean(course
			    .findCompetenceCourseInformationForExecutionPeriod(period)
			    .getCompetenceCourseLoads().get(0)) : new CompetenceCourseLoadBean();

	}

	request.setAttribute("beanLoad", load);
	RenderUtils.invalidateViewState("common-part");
	RenderUtils.invalidateViewState("pt-part");
	RenderUtils.invalidateViewState("en-part");
	RenderUtils.invalidateViewState("versionLoad");

	request.setAttribute("bean", bean);
	return mapping.findForward("createVersions");
    }

    private ExecutionSemester getExecutionPeriod(HttpServletRequest request) {
	String executionPeriodID = request.getParameter("executionPeriodID");
	ExecutionSemester period = null;
	if (executionPeriodID != null) {
	    period = (ExecutionSemester) RootDomainObject.readDomainObjectByOID(ExecutionSemester.class,
		    Integer.valueOf(executionPeriodID));
	}
	return period;

    }

    public ActionForward revokeVersion(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	CompetenceCourseInformationChangeRequest changeRequest = getChangeRequest(request);
	if (changeRequest != null
		&& isAllowedToViewChangeRequest(getLoggedPerson(request), changeRequest)) {
	    try {
		executeService("DeleteCompetenceCourseInformationChangeRequest",
			new Object[] { changeRequest });
	    } catch (DomainException e) {
		addActionMessage(request, e.getMessage());
	    }
	}

	return showVersions(mapping, form, request, response);
    }

    public ActionForward editBibliography(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	String index = request.getParameter("index");
	request.setAttribute("edit", index);

	return viewBibliography(mapping, form, request, response);
    }

    public ActionForward createBibliographicReference(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	CompetenceCourseInformationRequestBean bean = (CompetenceCourseInformationRequestBean) RenderUtils
		.getViewState("editVersion").getMetaObject().getObject();
	CreateReferenceBean referenceBean = (CreateReferenceBean) RenderUtils.getViewState(
		"createReference").getMetaObject().getObject();
	bean.getReferences().createBibliographicReference(referenceBean.getYear(),
		referenceBean.getTitle(), referenceBean.getAuthors(), referenceBean.getReference(),
		referenceBean.getUrl(), referenceBean.getType());
	RenderUtils.invalidateViewState("createReference");
	return viewBibliography(mapping, form, request, response);
    }

    public ActionForward viewBibliography(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	CompetenceCourseInformationRequestBean bean = (CompetenceCourseInformationRequestBean) RenderUtils
		.getViewState("editVersion").getMetaObject().getObject();
	CompetenceCourseLoadBean load = (CompetenceCourseLoadBean) RenderUtils.getViewState(
		"editVersionLoad").getMetaObject().getObject();

	request.setAttribute("bean", bean);
	request.setAttribute("beanLoad", load);

	if (areBeanValid(bean, load)) {
	    request.setAttribute("referenceBean", new CreateReferenceBean());
	    return mapping.findForward("editBiblio");
	} else {
	    addActionMessage(request, "error.all.fields.are.required");
	    return mapping.findForward("createVersions");
	}
    }

    public ActionForward removeBibliography(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	CompetenceCourseInformationRequestBean bean = (CompetenceCourseInformationRequestBean) RenderUtils
		.getViewState("editVersion").getMetaObject().getObject();
	bean.getReferences()
		.deleteBibliographicReference(Integer.valueOf(request.getParameter("index")));
	return viewBibliography(mapping, form, request, response);
    }

    public ActionForward createVersion(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	IViewState viewState = RenderUtils.getViewState("editVersion");
	if (viewState == null) {
	    return prepareCreateVersion(mapping, form, request, response);
	}
	CompetenceCourseInformationRequestBean bean = (CompetenceCourseInformationRequestBean) viewState
		.getMetaObject().getObject();
	CompetenceCourseLoadBean load = (CompetenceCourseLoadBean) RenderUtils.getViewState(
		"editVersionLoad").getMetaObject().getObject();
	try {
	    executeService("CreateCompetenceCourseInformationRequest", new Object[] { bean, load,
		    getLoggedPerson(request) });
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    return prepareCreateVersion(mapping, form, request, response);
	}
	return showVersions(mapping, form, request, response);
    }

    public ActionForward showVersions(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	CompetenceCourse course = getCompetenceCourse(request);
	request.setAttribute("competenceCourse", course);

	return mapping.findForward("viewVersions");
    }

    private CompetenceCourse getCompetenceCourse(HttpServletRequest request) {
	String competenceCourseID = request.getParameter("competenceCourseID");
	CompetenceCourse course = (CompetenceCourse) RootDomainObject.readDomainObjectByOID(
		CompetenceCourse.class, Integer.valueOf(competenceCourseID));
	return course;
    }

    public ActionForward viewVersion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	CompetenceCourseInformationChangeRequest changeRequest = getCompetenceCourseInformationRequest(request);
	if (changeRequest != null
		&& isAllowedToViewChangeRequest(getLoggedPerson(request), changeRequest)) {
	    request.setAttribute("changeRequest", changeRequest);
	}
	return mapping.findForward("viewVersionDetails");
    }

    private CompetenceCourseInformationChangeRequest getCompetenceCourseInformationRequest(
	    HttpServletRequest request) {
	String competenceCourseInformationChangeRequestId = request.getParameter("changeRequestID");
	CompetenceCourseInformationChangeRequest changeRequest = (CompetenceCourseInformationChangeRequest) RootDomainObject
		.readDomainObjectByOID(CompetenceCourseInformationChangeRequest.class, Integer
			.valueOf(competenceCourseInformationChangeRequestId));
	return changeRequest;
    }

    public ActionForward showCompetenceCourseInformation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	CompetenceCourse course = getCompetenceCourse(request);
	Integer idInternal = Integer.valueOf(request.getParameter("oid"));

	for (CompetenceCourseInformation information : course.getCompetenceCourseInformations()) {
	    if (information.getIdInternal().intValue() == idInternal.intValue()) {
		request.setAttribute("information", information);
	    }
	}

	return mapping.findForward("viewInformationDetails");
    }

    private boolean isAllowedToViewChangeRequest(Person loggedPerson,
	    CompetenceCourseInformationChangeRequest changeRequest) {

	return loggedPerson.hasPersonRoles(Role.getRoleByRoleType(RoleType.SCIENTIFIC_COUNCIL))
		|| (loggedPerson.hasPersonRoles(Role.getRoleByRoleType(RoleType.BOLONHA_MANAGER)) && changeRequest
			.getCompetenceCourse().getDepartmentUnit().getDepartment()
			.isUserMemberOfCompetenceCourseMembersGroup(loggedPerson));
    }

    private CompetenceCourseInformationChangeRequest getChangeRequest(HttpServletRequest request) {
	String competenceCourseInformationChangeRequestId = request.getParameter("changeRequestID");
	CompetenceCourseInformationChangeRequest changeRequest = null;
	if (competenceCourseInformationChangeRequestId != null) {
	    changeRequest = (CompetenceCourseInformationChangeRequest) RootDomainObject
		    .readDomainObjectByOID(CompetenceCourseInformationChangeRequest.class, Integer
			    .valueOf(competenceCourseInformationChangeRequestId));
	}
	return changeRequest;
    }

    private boolean areBeanValid(CompetenceCourseInformationRequestBean bean,
	    CompetenceCourseLoadBean loadBean) {
	if (StringUtils.isEmpty(bean.getName()) || StringUtils.isEmpty(bean.getNameEn())
		|| StringUtils.isEmpty(bean.getJustification()) || bean.getRegime() == null
		|| StringUtils.isEmpty(bean.getObjectives())
		|| StringUtils.isEmpty(bean.getObjectivesEn()) || StringUtils.isEmpty(bean.getProgram())
		|| StringUtils.isEmpty(bean.getProgramEn())
		|| StringUtils.isEmpty(bean.getEvaluationMethod())
		|| StringUtils.isEmpty(bean.getEvaluationMethodEn())
		|| bean.getCompetenceCourse() == null || bean.getExecutionPeriod() == null
		|| bean.getCompetenceCourseLevel() == null || loadBean.getTheoreticalHours() == null
		|| loadBean.getProblemsHours() == null || loadBean.getLaboratorialHours() == null
		|| loadBean.getSeminaryHours() == null || loadBean.getFieldWorkHours() == null
		|| loadBean.getTrainingPeriodHours() == null
		|| loadBean.getTutorialOrientationHours() == null
		|| loadBean.getAutonomousWorkHours() == null || loadBean.getEctsCredits() == null) {
	    return false;
	}

	return true;
    }
}
