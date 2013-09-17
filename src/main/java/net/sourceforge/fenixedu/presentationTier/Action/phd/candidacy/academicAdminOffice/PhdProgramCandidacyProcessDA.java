package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.academicAdminOffice;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.CreateNewProcess;
import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessStateBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.RatifyCandidacyBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.RegistrationFormalizationBean;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import net.sourceforge.fenixedu.domain.phd.notification.PhdNotification;
import net.sourceforge.fenixedu.domain.phd.notification.PhdNotificationBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessStateBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.CommonPhdCandidacyDA;
import net.sourceforge.fenixedu.presentationTier.docs.phd.notification.PhdCandidacyDeclarationDocument;
import net.sourceforge.fenixedu.presentationTier.docs.phd.notification.PhdNotificationDocument;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/phdProgramCandidacyProcess", module = "academicAdministration")
@Forwards({

@Forward(name = "searchPerson", path = "/phd/candidacy/academicAdminOffice/searchPerson.jsp"),

@Forward(name = "createCandidacy", path = "/phd/candidacy/academicAdminOffice/createCandidacy.jsp"),

@Forward(name = "manageProcesses", path = "/phdIndividualProgramProcess.do?method=manageProcesses"),

@Forward(name = "editCandidacyInformation", path = "/phd/candidacy/academicAdminOffice/editCandidacyInformation.jsp"),

@Forward(name = "manageCandidacyDocuments", path = "/phd/candidacy/academicAdminOffice/manageCandidacyDocuments.jsp"),

@Forward(name = "requestCandidacyReview", path = "/phd/candidacy/academicAdminOffice/requestCandidacyReview.jsp"),

@Forward(name = "manageCandidacyReview", path = "/phd/candidacy/academicAdminOffice/manageCandidacyReview.jsp"),

@Forward(name = "rejectCandidacyProcess", path = "/phd/candidacy/academicAdminOffice/rejectCandidacyProcess.jsp"),

@Forward(name = "ratifyCandidacy", path = "/phd/candidacy/academicAdminOffice/ratifyCandidacy.jsp"),

@Forward(name = "viewProcess", path = "/phdIndividualProgramProcess.do?method=viewProcess"),

@Forward(name = "manageNotifications", path = "/phd/candidacy/academicAdminOffice/manageNotifications.jsp"),

@Forward(name = "createNotification", path = "/phd/candidacy/academicAdminOffice/createNotification.jsp"),

@Forward(name = "registrationFormalization", path = "/phd/candidacy/academicAdminOffice/registrationFormalization.jsp"),

@Forward(name = "associateRegistration", path = "/phd/candidacy/academicAdminOffice/associateRegistration.jsp"),

@Forward(name = "manageStates", path = "/phd/candidacy/academicAdminOffice/manageStates.jsp"),

@Forward(name = "editProcessAttributes", path = "/phd/candidacy/academicAdminOffice/editProcessAttributes.jsp"),

@Forward(name = "editPhdProcessState", path = "/phd/candidacy/academicAdminOffice/editState.jsp"),

@Forward(name = "viewLogs", path = "/phd/candidacy/academicAdminOffice/logs/viewLogs.jsp")

})
public class PhdProgramCandidacyProcessDA extends CommonPhdCandidacyDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Process process = getProcess(request);
        if (process != null) {
            request.setAttribute("processId", process.getExternalId());
            request.setAttribute("process", process);
        }

        return super.execute(mapping, actionForm, request, response);
    }

    // Create Candidacy Steps

    public ActionForward prepareSearchPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean();
        bean.setState(PhdProgramCandidacyProcessState.STAND_BY_WITH_MISSING_INFORMATION);
        bean.setPersonBean(new PersonBean());
        bean.setChoosePersonBean(new ChoosePersonBean());

        request.setAttribute("createCandidacyBean", bean);
        request.setAttribute("persons", Collections.emptyList());

        return mapping.findForward("searchPerson");
    }

    public ActionForward searchPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final PhdProgramCandidacyProcessBean bean = getCreateCandidacyProcessBean();
        request.setAttribute("createCandidacyBean", bean);

        final ChoosePersonBean choosePersonBean = getCreateCandidacyProcessBean().getChoosePersonBean();
        if (!choosePersonBean.hasPerson()) {
            if (choosePersonBean.isFirstTimeSearch()) {
                final Collection<Person> persons = Person.findPersonByDocumentID(choosePersonBean.getIdentificationNumber());
                choosePersonBean.setFirstTimeSearch(false);
                if (showSimilarPersons(choosePersonBean, persons)) {
                    RenderUtils.invalidateViewState();
                    return mapping.findForward("searchPerson");
                }
            }
            bean.setPersonBean(new PersonBean(choosePersonBean.getName(), choosePersonBean.getIdentificationNumber(),
                    choosePersonBean.getDocumentType(), choosePersonBean.getDateOfBirth()));

            return mapping.findForward("createCandidacy");

        } else {
            bean.setPersonBean(new PersonBean(bean.getChoosePersonBean().getPerson()));
            setIsEmployeeAttributeAndMessage(request, bean.getChoosePersonBean().getPerson());
            return mapping.findForward("createCandidacy");
        }

    }

    protected boolean showSimilarPersons(final ChoosePersonBean choosePersonBean, final Collection<Person> persons) {
        if (!persons.isEmpty()) {
            return true;
        }
        return !Person.findByDateOfBirth(choosePersonBean.getDateOfBirth(),
                Person.findInternalPersonMatchingFirstAndLastName(choosePersonBean.getName())).isEmpty();
    }

    public ActionForward createCandidacyInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("createCandidacyBean", getCreateCandidacyProcessBean());
        setIsEmployeeAttributeAndMessage(request, getCreateCandidacyProcessBean().getChoosePersonBean().getPerson());

        return mapping.findForward("createCandidacy");
    }

    public ActionForward createCandidacyPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("createCandidacyBean", getCreateCandidacyProcessBean());
        setIsEmployeeAttributeAndMessage(request, getCreateCandidacyProcessBean().getChoosePersonBean().getPerson());

        RenderUtils.invalidateViewState();

        return mapping.findForward("createCandidacy");
    }

    public ActionForward createCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        try {

            if (!validateAreaCodeAndAreaOfAreaCode(request, getCreateCandidacyProcessBean().getChoosePersonBean().getPerson(),
                    getCreateCandidacyProcessBean().getPersonBean().getCountryOfResidence(), getCreateCandidacyProcessBean()
                            .getPersonBean().getAreaCode(), getCreateCandidacyProcessBean().getPersonBean().getAreaOfAreaCode())) {

                setIsEmployeeAttributeAndMessage(request, getCreateCandidacyProcessBean().getChoosePersonBean().getPerson());
                request.setAttribute("createCandidacyBean", getCreateCandidacyProcessBean());
                return mapping.findForward("createCandidacy");

            }

            CreateNewProcess.run(PhdIndividualProgramProcess.class, getCreateCandidacyProcessBean());

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            setIsEmployeeAttributeAndMessage(request, getCreateCandidacyProcessBean().getChoosePersonBean().getPerson());
            request.setAttribute("createCandidacyBean", getCreateCandidacyProcessBean());
            return mapping.findForward("createCandidacy");
        }

        return mapping.findForward("manageProcesses");

    }

    private PhdProgramCandidacyProcessBean getCreateCandidacyProcessBean() {
        return getRenderedObject("createCandidacyBean");
    }

    public ActionForward cancelCreateCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("manageProcesses");
    }

    // End of Create Candidacy Steps

    @Override
    public ActionForward manageCandidacyDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        prepareDocumentsToUpload(request);
        return mapping.findForward("manageCandidacyDocuments");
    }

    private void prepareDocumentsToUpload(HttpServletRequest request) {
        request.setAttribute("documentsToUpload", Arrays.asList(new PhdProgramDocumentUploadBean(),
                new PhdProgramDocumentUploadBean(), new PhdProgramDocumentUploadBean(), new PhdProgramDocumentUploadBean(),
                new PhdProgramDocumentUploadBean()));
    }

    public ActionForward uploadDocumentsInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("documentsToUpload", getDocumentsToUpload());
        return mapping.findForward("manageCandidacyDocuments");
    }

    public ActionForward uploadDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!hasAnyDocumentToUpload()) {
            request.setAttribute("documentsToUpload", getDocumentsToUpload());

            addErrorMessage(request, "message.no.documents.to.upload");

            return mapping.findForward("manageCandidacyDocuments");
        }

        final ActionForward result =
                executeActivity(net.sourceforge.fenixedu.domain.phd.candidacy.activities.UploadDocuments.class,
                        getDocumentsToUpload(), request, mapping, "manageCandidacyDocuments", "manageCandidacyDocuments",
                        "message.documents.uploaded.with.success");

        RenderUtils.invalidateViewState("documentsToUpload");

        prepareDocumentsToUpload(request);

        return result;

    }

    protected boolean hasAnyDocumentToUpload() {
        for (final PhdProgramDocumentUploadBean each : getDocumentsToUpload()) {
            if (each.hasAnyInformation()) {
                return true;
            }
        }
        return false;
    }

    protected List<PhdProgramDocumentUploadBean> getDocumentsToUpload() {
        return (List<PhdProgramDocumentUploadBean>) getObjectFromViewState("documentsToUpload");
    }

    public ActionForward prepareRequestCandidacyReview(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcess process = getProcess(request);
        final PhdProgramCandidacyProcessStateBean bean =
                new PhdProgramCandidacyProcessStateBean(process.getIndividualProgramProcess());
        bean.setState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION);
        request.setAttribute("stateBean", bean);
        return mapping.findForward("requestCandidacyReview");
    }

    public ActionForward prepareRequestCandidacyReviewPostback(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("stateBean", getRenderedObject("stateBean"));

        RenderUtils.invalidateViewState();
        return mapping.findForward("requestCandidacyReview");
    }

    public ActionForward requestCandidacyReview(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            final PhdProgramCandidacyProcess process = getProcess(request);
            ExecuteProcessActivity.run(process,
                    net.sourceforge.fenixedu.domain.phd.candidacy.activities.RequestCandidacyReview.class.getSimpleName(),
                    getRenderedObject("stateBean"));
            return viewIndividualProgramProcess(request, process);

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            request.setAttribute("stateBean", getRenderedObject("stateBean"));
            return mapping.findForward("requestCandidacyReview");
        }
    }

    public ActionForward deleteDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        prepareDocumentsToUpload(request);

        return executeActivity(net.sourceforge.fenixedu.domain.phd.candidacy.activities.DeleteDocument.class,
                getDocument(request), request, mapping, "manageCandidacyDocuments", "manageCandidacyDocuments",
                "message.document.deleted.successfuly");
    }

    public ActionForward prepareRatifyCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("ratifyCandidacyBean", new RatifyCandidacyBean(getProcess(request)));

        return mapping.findForward("ratifyCandidacy");
    }

    public ActionForward prepareRatifyCandidacyInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("ratifyCandidacyBean", getRenderedObject("ratifyCandidacyBean"));

        return mapping.findForward("ratifyCandidacy");
    }

    public ActionForward ratifyCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final RatifyCandidacyBean bean = getRenderedObject("ratifyCandidacyBean");
        try {
            ExecuteProcessActivity.run(getProcess(request),
                    net.sourceforge.fenixedu.domain.phd.candidacy.activities.RatifyCandidacy.class, bean);
            addSuccessMessage(request, "message.candidacy.ratified.successfuly");

            request.setAttribute("processId", getProcess(request).getIndividualProgramProcess().getExternalId());

            return mapping.findForward("viewProcess");

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());

            request.setAttribute("ratifyCandidacyBean", bean);

            return mapping.findForward("ratifyCandidacy");
        }
    }

    // Notification Management

    public ActionForward manageNotifications(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        return mapping.findForward("manageNotifications");
    }

    public ActionForward prepareCreateNotification(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("notificationBean", new PhdNotificationBean(getProcess(request)));

        return mapping.findForward("createNotification");
    }

    public ActionForward prepareCreateNotificationInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("notificationBean", getRenderedObject("notificationBean"));

        return mapping.findForward("createNotification");
    }

    public ActionForward createNotification(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdNotificationBean bean = getRenderedObject("notificationBean");

        final ActionForward result =
                executeActivity(net.sourceforge.fenixedu.domain.phd.candidacy.activities.AddNotification.class, bean, request,
                        mapping, "createNotification", "manageNotifications", "message.notification.created.with.success");

        request.setAttribute("notificationBean", bean);

        return result;
    }

    private PhdNotification getNotification(HttpServletRequest request) {
        return getDomainObject(request, "notificationId");
    }

    public ActionForward printNotification(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws JRException, IOException {

        final PhdNotificationDocument report = new PhdNotificationDocument(getNotification(request), getLanguage(request));
        writeFile(response, report.getReportFileName() + ".pdf", "application/pdf",
                ReportsUtils.exportToProcessedPdfAsByteArray(report));

        return null;

    }

    private Language getLanguage(HttpServletRequest request) {
        return Language.valueOf(request.getParameter("language"));
    }

    public ActionForward markNotificationAsSent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        getNotification(request).markAsSent();

        return manageNotifications(mapping, form, request, response);

    }

    public ActionForward printCandidacyDeclaration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, JRException {

        final PhdCandidacyDeclarationDocument report =
                new PhdCandidacyDeclarationDocument(getProcess(request), getLanguage(request));
        writeFile(response, report.getReportFileName() + ".pdf", "application/pdf",
                ReportsUtils.exportToProcessedPdfAsByteArray(report));

        return null;

    }

    // End of Notification Management

    // Begin RegistrationFormalization

    public ActionForward prepareRegistrationFormalization(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        checkCandidacyPreConditions(request);
        final RegistrationFormalizationBean bean = new RegistrationFormalizationBean(getProcess(request));
        request.setAttribute("registrationFormalizationBean", bean);
        return mapping.findForward("registrationFormalization");
    }

    private void checkCandidacyPreConditions(final HttpServletRequest request) {
        final PhdProgramCandidacyProcess process = getProcess(request);
        final Person person = process.getPerson();

        request.setAttribute("idDocument", process.hasAnyDocuments(PhdIndividualProgramDocumentType.ID_DOCUMENT));
        request.setAttribute("personalPhoto", process.getPerson().hasPersonalPhoto());
        request.setAttribute("healthBulletin", process.hasAnyDocuments(PhdIndividualProgramDocumentType.HEALTH_BULLETIN));
        request.setAttribute("habilitationsCertificates", person.getAssociatedQualificationsSet().size() == process
                .getDocumentsCount(PhdIndividualProgramDocumentType.HABILITATION_CERTIFICATE_DOCUMENT));

        if (!process.hasStudyPlan()) {
            addWarningMessage(request,
                    "error.phd.candidacy.PhdProgramCandidacyProcess.registrationFormalization.must.create.study.plan");
        } else if (process.isStudyPlanExempted()) {
            addWarningMessage(request, "message.phd.candidacy.registration.formalization.study.plan.is.exempted");
        }
    }

    public ActionForward registrationFormalizationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        checkCandidacyPreConditions(request);
        request.setAttribute("registrationFormalizationBean", getRenderedObject("registrationFormalizationBean"));
        return mapping.findForward("registrationFormalization");
    }

    public ActionForward registrationFormalization(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {

            final RegistrationFormalizationBean bean = getRenderedObject("registrationFormalizationBean");

            if (!bean.hasRegistration() && mustSelectFirstAnyRegistratiom(request)) {
                bean.setSelectRegistration(true);
                return registrationFormalizationInvalid(mapping, actionForm, request, response);
            }

            ExecuteProcessActivity.run(getProcess(request),
                    net.sourceforge.fenixedu.domain.phd.candidacy.activities.RegistrationFormalization.class, bean);

            // TODO: message and warning due to insurance, enrolment debts, etc
            // etc
            // addSuccessMessage(request,
            // "message.candidacy.ratified.successfuly");

        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return registrationFormalizationInvalid(mapping, actionForm, request, response);
        }

        return viewIndividualProgramProcess(request, getProcess(request));
    }

    // End of RegistrationFormalization

    private boolean mustSelectFirstAnyRegistratiom(HttpServletRequest request) {
        final PhdProgramCandidacyProcess process = getProcess(request);

        return process.hasStudyPlan() && process.hasPhdProgram()
                && process.hasActiveRegistrationFor(process.getPhdProgramLastActiveDegreeCurricularPlan());
    }

    static public class PhdRegistrationFormalizationRegistrations implements DataProvider {

        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }

        @Override
        public Object provide(Object source, Object currentValue) {
            final RegistrationFormalizationBean bean = (RegistrationFormalizationBean) source;
            return bean.getAvailableRegistrationsToAssociate();
        }
    }

    public ActionForward prepareAssociateRegistration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcess process = getProcess(request);
        final RegistrationFormalizationBean bean = new RegistrationFormalizationBean(process);
        bean.setWhenStartedStudies(process.getWhenStartedStudies());

        request.setAttribute("registrationFormalizationBean", bean);
        return mapping.findForward("associateRegistration");
    }

    public ActionForward associateRegistrationInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("registrationFormalizationBean", getRenderedObject("registrationFormalizationBean"));
        return mapping.findForward("associateRegistration");
    }

    public ActionForward associateRegistration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            ExecuteProcessActivity.run(getProcess(request),
                    net.sourceforge.fenixedu.domain.phd.candidacy.activities.AssociateRegistration.class,
                    getRenderedObject("registrationFormalizationBean"));
            addSuccessMessage(request, "message.registration.associated.successfuly");

        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return associateRegistrationInvalid(mapping, actionForm, request, response);
        }

        return viewIndividualProgramProcess(request, getProcess(request));
    }

    public ActionForward manageStates(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean(getProcess(request));
        request.setAttribute("processBean", bean);

        return mapping.findForward("manageStates");
    }

    public ActionForward addState(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            PhdProgramCandidacyProcessBean bean = getRenderedObject("processBean");
            ExecuteProcessActivity.run(getProcess(request),
                    net.sourceforge.fenixedu.domain.phd.candidacy.activities.AddState.class, bean);
        } catch (PhdDomainOperationException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
        }

        RenderUtils.invalidateViewState();

        return manageStates(mapping, form, request, response);
    }

    public ActionForward addStateInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdThesisProcessBean bean = getRenderedObject("thesisProcessBean");

        request.setAttribute("thesisProcessBean", bean);

        return mapping.findForward("manageStates");
    }

    public ActionForward removeLastState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgramCandidacyProcessBean bean = getRenderedObject("processBean");
        try {
            ExecuteProcessActivity.run(getProcess(request),
                    net.sourceforge.fenixedu.domain.phd.candidacy.activities.RemoveLastState.class, bean);
        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
        }

        RenderUtils.invalidateViewState();

        return manageStates(mapping, form, request, response);
    }

    public ActionForward prepareEditProcessAttributes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean(getProcess(request));
        request.setAttribute("processBean", bean);

        return mapping.findForward("editProcessAttributes");
    }

    public ActionForward editProcessAttributesInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("processBean", getRenderedObject("processBean"));

        return mapping.findForward("editProcessAttributes");
    }

    public ActionForward editProcessAttributes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgramCandidacyProcessBean bean = getRenderedObject("processBean");
        ExecuteProcessActivity.run(getProcess(request),
                net.sourceforge.fenixedu.domain.phd.candidacy.activities.EditProcessAttributes.class, bean);

        return viewIndividualProgramProcess(mapping, form, request, response);
    }

    /* EDIT PHD STATES */

    public ActionForward prepareEditState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProcessState state = getDomainObject(request, "stateId");
        PhdProcessStateBean bean = new PhdProcessStateBean(state);

        request.setAttribute("bean", bean);

        return mapping.findForward("editPhdProcessState");
    }

    public ActionForward editState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProcessStateBean bean = getRenderedObject("bean");
        bean.getState().editStateDate(bean);

        return manageStates(mapping, form, request, response);
    }

    public ActionForward editStateInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProcessStateBean bean = getRenderedObject("bean");
        request.setAttribute("bean", bean);

        return mapping.findForward("editPhdProcessState");
    }

    public ActionForward viewLogs(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        return mapping.findForward("viewLogs");
    }

    /* EDIT PHD STATES */

}
