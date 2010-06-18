package net.sourceforge.fenixedu.presentationTier.Action.teacher.candidacy.erasmus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ApprovedLearningAgreementDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/caseHandlingErasmusIndividualCandidacyProcess", module = "teacher", formBeanClass = FenixActionForm.class)
@Forwards( {
	@Forward(name = "intro", path = "/caseHandlingErasmusCandidacyProcess.do?method=listProcessAllowedActivities"),
	@Forward(name = "list-allowed-activities", path = "/candidacy/erasmus/listIndividualCandidacyActivities.jsp"),
	@Forward(name = "set-coordinator-validation", path = "/teacher/candidacy/erasmus/setCoordinatorValidation.jsp"),
	@Forward(name = "visualize-alerts", path = "/candidacy/erasmus/visualizeAlerts.jsp"),
	@Forward(name = "upload-learning-agreement", path = "/candidacy/erasmus/uploadLearningAgreement.jsp") })
public class ErasmusIndividualCandidacyProcessDA extends
	net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus.ErasmusIndividualCandidacyProcessDA {


    @Override
    protected List<Activity> getAllowedActivities(final IndividualCandidacyProcess process) {
	List<Activity> activities = process.getAllowedActivities(AccessControl.getUserView());
	ArrayList<Activity> resultActivities = new ArrayList<Activity>();

	for (Activity activity : activities) {
	    if (activity.isVisibleForCoordinator()) {
		resultActivities.add(activity);
	    }
	}

	return resultActivities;
    }

    public ActionForward prepareExecuteSetCoordinatorValidation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final ErasmusIndividualCandidacyProcessBean bean = new ErasmusIndividualCandidacyProcessBean(getProcess(request));

	bean.setCreateAlert(true);
	bean.setSendEmail(true);

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	return mapping.findForward("set-coordinator-validation");

    }

    public ActionForward executeSetCoordinatorValidation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();

	    if (bean.getCreateAlert()
		    && (StringUtils.isEmpty(bean.getAlertSubject()) || StringUtils.isEmpty(bean.getAlertBody()))) {
		addActionMessage(request, "error.erasmus.alert.subject.and.body.must.not.be.empty");
	    } else {
		executeActivity(getProcess(request), "SetCoordinatorValidation", getIndividualCandidacyProcessBean());
		return listProcessAllowedActivities(mapping, actionForm, request, response);
	    }
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	}

	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("set-coordinator-validation");
    }

    public ActionForward executeSetCoordinatorValidationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("set-coordinator-validation");
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
		    getParentProcessType(),
		    learningAgreementUploadBean.getIndividualCandidacyProcess().getPersonalDetails().getDocumentIdNumber());

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

}
