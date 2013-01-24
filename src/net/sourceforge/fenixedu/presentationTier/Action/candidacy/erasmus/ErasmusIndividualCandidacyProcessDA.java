package net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ApprovedLearningAgreementDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcessBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.IndividualCandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/caseHandlingMobilityIndividualApplicationProcess", module = "academicAdministration", formBeanClass = FenixActionForm.class)
@Forwards({
	@Forward(name = "intro", path = "/caseHandlingMobilityApplicationProcess.do?method=listProcessAllowedActivities"),
	@Forward(name = "list-allowed-activities", path = "/candidacy/erasmus/listIndividualCandidacyActivities.jsp"),
	@Forward(name = "prepare-create-new-process", path = "/candidacy/erasmus/selectPersonForCandidacy.jsp"),
	@Forward(name = "fill-personal-information", path = "/candidacy/erasmus/fillPersonalInformation.jsp"),
	@Forward(name = "fill-candidacy-information", path = "/candidacy/erasmus/fillCandidacyInformation.jsp"),
	@Forward(name = "fill-degree-information", path = "/candidacy/erasmus/fillDegreeInformation.jsp"),
	@Forward(name = "fill-courses-information", path = "/candidacy/erasmus/fillCoursesInformation.jsp"),
	@Forward(name = "edit-candidacy-personal-information", path = "/candidacy/erasmus/editPersonalInformation.jsp"),
	@Forward(name = "edit-candidacy-information", path = "/candidacy/erasmus/editCandidacyInformation.jsp"),
	@Forward(name = "edit-degree-courses-information", path = "/candidacy/erasmus/editDegreeAndCoursesInformation.jsp"),
	@Forward(name = "visualize-alerts", path = "/candidacy/erasmus/visualizeAlerts.jsp"),
	@Forward(name = "prepare-edit-candidacy-documents", path = "/candidacy/erasmus/editCandidacyDocuments.jsp"),
	@Forward(name = "cancel-candidacy", path = "/candidacy/cancelCandidacy.jsp"),
	@Forward(name = "prepare-create-registration", path = "/candidacy/erasmus/prepareCreateRegistration.jsp"),
	@Forward(name = "view-registration", path = "/candidacy/erasmus/viewRegistrationData.jsp"),
	@Forward(name = "prepare-enrol-on-modules", path = "/candidacy/erasmus/prepareEnrolOnModules.jsp") })
public class ErasmusIndividualCandidacyProcessDA extends IndividualCandidacyProcessDA {

    @Override
    protected Class<? extends CandidacyProcess> getParentProcessType() {
	return MobilityApplicationProcess.class;
    }

    @Override
    protected void prepareInformationForBindPersonToCandidacyOperation(HttpServletRequest request,
	    IndividualCandidacyProcess process) {
	// TODO Auto-generated method stub

    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final MobilityIndividualApplicationProcessBean bean = new MobilityIndividualApplicationProcessBean(
		getParentProcess(request));

	/*
	 * 06/05/2009 - Due to Public Candidacies, a candidacy created in admin
	 * office is external So we dont require ChoosePersonBean because a
	 * Person will not be associated or created at individual candidacy
	 * creation stage. Instead we bind with an empty PersonBean.
	 * 
	 * bean.setChoosePersonBean(new ChoosePersonBean());
	 */
	/*
	 * 21/07/2009 - Now we create a person to process the payments
	 * imediately
	 */
	bean.setChoosePersonBean(new ChoosePersonBean());
	bean.setPersonBean(new PersonBean());

	bean.getChoosePersonBean().setName("");
	bean.getChoosePersonBean().setDocumentType(IDDocumentType.FOREIGNER_IDENTITY_CARD);

	/*
	 * 06/05/2009 - Also we mark the bean as an external candidacy.
	 */
	bean.setInternalPersonCandidacy(Boolean.TRUE);
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

    }

    @Override
    protected Class<? extends IndividualCandidacyProcess> getProcessType() {
	return MobilityIndividualApplicationProcess.class;
    }

    @Override
    protected MobilityApplicationProcess getParentProcess(HttpServletRequest request) {
	return (MobilityApplicationProcess) super.getParentProcess(request);
    }

    @Override
    protected MobilityIndividualApplicationProcess getProcess(HttpServletRequest request) {
	return (MobilityIndividualApplicationProcess) super.getProcess(request);
    }

    @Override
    public MobilityIndividualApplicationProcessBean getIndividualCandidacyProcessBean() {
	return (MobilityIndividualApplicationProcessBean) super.getIndividualCandidacyProcessBean();
    }

    public ActionForward fillDegreeInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	request.setAttribute("degreeCourseInformationBean", new DegreeCourseInformationBean(
		(ExecutionYear) getIndividualCandidacyProcessBean().getCandidacyProcess().getCandidacyExecutionInterval(),
		(MobilityApplicationProcess) getIndividualCandidacyProcessBean().getCandidacyProcess()));

	return mapping.findForward("fill-degree-information");
    }

    public ActionForward fillDegreeInformationInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("fill-degree-information");
    }

    public ActionForward chooseDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));

	RenderUtils.invalidateViewState();

	if ("editCandidacy".equals(request.getParameter("userAction"))) {
	    return mapping.findForward("edit-degree-courses-information");
	}

	return mapping.findForward("fill-degree-information");
    }

    private DegreeCourseInformationBean readDegreeCourseInformationBean(HttpServletRequest request) {
	return getRenderedObject("degree.course.information.bean");
    }

    public ActionForward addCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

	MobilityIndividualApplicationProcessBean bean = getIndividualCandidacyProcessBean();
	DegreeCourseInformationBean degreeCourseBean = readDegreeCourseInformationBean(request);

	if (degreeCourseBean.getChosenCourse() != null) {
	    bean.addCurricularCourse(degreeCourseBean.getChosenCourse());
	}

	request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));

	RenderUtils.invalidateViewState();

	if ("editCandidacy".equals(request.getParameter("userAction"))) {
	    bean.getMobilityStudentDataBean().setMobilityAgreement();
	    return mapping.findForward("edit-degree-courses-information");
	}

	return mapping.findForward("fill-degree-information");
    }

    public ActionForward removeCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

	MobilityIndividualApplicationProcessBean bean = getIndividualCandidacyProcessBean();
	DegreeCourseInformationBean degreeCourseBean = readDegreeCourseInformationBean(request);

	CurricularCourse courseToRemove = getDomainObject(request, "removeCourseId");
	bean.removeCurricularCourse(courseToRemove);

	request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));

	RenderUtils.invalidateViewState();

	if ("editCandidacy".equals(request.getParameter("userAction"))) {
	    bean.getMobilityStudentDataBean().setMobilityAgreement();
	    return mapping.findForward("edit-degree-courses-information");
	}

	return mapping.findForward("fill-degree-information");
    }

    public ActionForward prepareExecuteEditCandidacyPersonalInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final MobilityIndividualApplicationProcessBean bean = new MobilityIndividualApplicationProcessBean(getProcess(request));
	bean.setPersonBean(new PersonBean(getProcess(request).getPersonalDetails()));
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	return mapping.findForward("edit-candidacy-personal-information");
    }

    public ActionForward executeEditCandidacyPersonalInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("edit-candidacy-personal-information");
    }

    public ActionForward executeEditCandidacyPersonalInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    executeActivity(getProcess(request), "EditCandidacyPersonalInformation", getIndividualCandidacyProcessBean());
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("edit-candidacy-personal-information");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecuteEditCandidacyInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final MobilityIndividualApplicationProcessBean bean = new MobilityIndividualApplicationProcessBean(getProcess(request));
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward executeEditCandidacyInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeActivity(getProcess(request), "EditCandidacyInformation", getIndividualCandidacyProcessBean());
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("edit-candidacy-information");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward executeEditCandidacyInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward prepareExecuteEditDegreeAndCoursesInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final MobilityIndividualApplicationProcessBean bean = new MobilityIndividualApplicationProcessBean(getProcess(request));
	request.setAttribute("degreeCourseInformationBean", new DegreeCourseInformationBean((ExecutionYear) getProcess(request)
		.getCandidacyProcess().getCandidacyExecutionInterval(), (MobilityApplicationProcess) bean.getCandidacyProcess()));
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	return mapping.findForward("edit-degree-courses-information");
    }

    public ActionForward executeEditDegreeAndCoursesInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeActivity(getProcess(request), "EditDegreeAndCoursesInformation", getIndividualCandidacyProcessBean());
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("edit-degree-courses-information");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward executeEditDegreeAndCoursesInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("edit-degree-courses-information");
    }

    public ActionForward prepareExecuteVisualizeAlerts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final MobilityIndividualApplicationProcessBean bean = new MobilityIndividualApplicationProcessBean(getProcess(request));

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	return mapping.findForward("visualize-alerts");
    }

    public ActionForward chooseCountry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	MobilityIndividualApplicationProcessBean bean = getIndividualCandidacyProcessBean();
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));

	RenderUtils.invalidateViewState();

	if ("editCandidacy".equals(request.getParameter("userAction"))) {
	    bean.getMobilityStudentDataBean().setSelectedUniversity(null);

	    return mapping.findForward("edit-degree-courses-information");
	}

	return mapping.findForward("fill-candidacy-information");
    }

    public ActionForward chooseUniversityOnCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	MobilityIndividualApplicationProcessBean bean = getIndividualCandidacyProcessBean();
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));

	RenderUtils.invalidateViewState();

	return mapping.findForward("fill-candidacy-information");
    }

    public ActionForward chooseUniversity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	MobilityIndividualApplicationProcessBean bean = getIndividualCandidacyProcessBean();
	bean.getMobilityStudentDataBean().setMobilityAgreement();
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));
	request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));

	RenderUtils.invalidateViewState();
	return mapping.findForward("edit-degree-courses-information");
    }

    public ActionForward prepareExecuteUploadApprovedLearningAgreement(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	final IndividualCandidacyProcess process = getProcess(request);

	ApprovedLearningAgreementDocumentUploadBean bean = new ApprovedLearningAgreementDocumentUploadBean();

	bean.setType(IndividualCandidacyDocumentFileType.LEARNING_AGREEMENT);
	bean.setIndividualCandidacyProcess(process);

	request.setAttribute("learningAgreementUploadBean", bean);

	return mapping.findForward("upload-learning-agreement");
    }

    public ActionForward executeUploadApprovedLearningAgreement(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	ApprovedLearningAgreementDocumentUploadBean learningAgreementUploadBean = (ApprovedLearningAgreementDocumentUploadBean) getObjectFromViewState("individualCandidacyProcessBean.document.file");

	try {
	    IndividualCandidacyDocumentFile documentFile = learningAgreementUploadBean.createIndividualCandidacyDocumentFile(
		    getParentProcessType(), learningAgreementUploadBean.getIndividualCandidacyProcess().getPersonalDetails()
			    .getDocumentIdNumber());

	    executeActivity(learningAgreementUploadBean.getIndividualCandidacyProcess(), "UploadApprovedLearningAgreement",
		    documentFile);
	    request.setAttribute("individualCandidacyProcess", learningAgreementUploadBean.getIndividualCandidacyProcess());
	} catch (final IOException e) {
	    invalidateDocumentFileRelatedViewStates();
	    addActionMessage(request, "error.erasmus.upload.approved.learning.agreement");
	    return prepareExecuteUploadApprovedLearningAgreement(mapping, form, request, response);
	} catch (final DomainException e) {
	    invalidateDocumentFileRelatedViewStates();
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    return prepareExecuteUploadApprovedLearningAgreement(mapping, form, request, response);
	}

	return listProcessAllowedActivities(mapping, form, request, response);
    }

    public ActionForward executeUploadApprovedLearningAgreementInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return prepareExecuteUploadApprovedLearningAgreement(mapping, form, request, response);
    }

    public ActionForward prepareExecuteCreateRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return mapping.findForward("prepare-create-registration");
    }

    public ActionForward executeCreateRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	MobilityIndividualApplicationProcess erasmusIndividualCandidacyProcess = getProcess(request);
	executeActivity(erasmusIndividualCandidacyProcess, "CreateRegistration");

	return listProcessAllowedActivities(mapping, form, request, response);
    }

    public ActionForward prepareExecuteEnrolOnFirstSemester(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("prepare-enrol-on-modules");
    }

    public ActionForward executeEnrolOnFirstSemester(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	MobilityIndividualApplicationProcess erasmusIndividualCandidacyProcess = getProcess(request);
	executeActivity(erasmusIndividualCandidacyProcess, "EnrolOnModules");

	return listProcessAllowedActivities(mapping, form, request, response);
    }

    public ActionForward prepareExecuteEnrolStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return redirect(
		"/caseHandlingMobilityIndividualApplicationProcess.do?method=enrolStudent&processId="
			+ getProcess(request).getIdInternal().toString(), request);
    }

}
