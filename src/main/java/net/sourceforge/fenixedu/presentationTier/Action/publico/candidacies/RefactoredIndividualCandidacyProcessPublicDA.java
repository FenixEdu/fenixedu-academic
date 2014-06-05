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
package net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.CreateNewProcess;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Installation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCodeOperations;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean.PrecedentDegreeType;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeIndividualCandidacyProcess.SendEmailForApplicationSubmission;
import net.sourceforge.fenixedu.domain.candidacyProcess.exceptions.HashCodeForEmailAndProcessAlreadyBounded;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.IndividualCandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.publico.KaptchaAction;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.utl.ist.fenix.tools.util.Pair;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public abstract class RefactoredIndividualCandidacyProcessPublicDA extends IndividualCandidacyProcessDA {

    private static final Logger logger = LoggerFactory.getLogger(RefactoredIndividualCandidacyProcessPublicDA.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("application.name", getStringFromDefaultBundle(getCandidacyNameKey()));
        request.setAttribute("mappingPath", mapping.getPath());
        request.setAttribute("isApplicationSubmissionPeriodValid", isApplicationSubmissionPeriodValid());
        request.setAttribute("application.information.link.default", getCandidacyInformationLinkDefaultLanguage());
        request.setAttribute("application.information.link.english", getCandidacyInformationLinkEnglish());
        setProcess(request);
        return super.execute(mapping, actionForm, request, response);
    }

    protected String getStringFromDefaultBundle(String key) {
        return BundleUtil.getString(Bundle.CANDIDATE, key);
    }

    @Override
    protected void prepareInformationForBindPersonToCandidacyOperation(HttpServletRequest request,
            IndividualCandidacyProcess process) {
        // TODO Auto-generated method stub

    }

    protected ActionForward verifySubmissionPreconditions(ActionMapping mapping) {
        if (getCurrentOpenParentProcess() == null) {
            return mapping.findForward("open-candidacy-process-closed");
        }

        return null;
    }

    protected ActionForward verifyEditPreconditions(ActionMapping mapping) {
        if (getCurrentOpenParentProcess() == null) {
            return mapping.findForward("open-candidacy-process-closed");
        }

        return null;
    }

    public ActionForward beginCandidacyProcessIntro(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        if (isInEnglishLocale()) {
            return mapping.findForward("begin-candidacy-process-intro-en");
        }

        return mapping.findForward("begin-candidacy-process-intro");
    }

    public ActionForward preparePreCreationOfCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        request.setAttribute("candidacyPreCreationBean", new CandidacyPreCreationBean());
        return mapping.findForward("show-pre-creation-candidacy-form");
    }

    public ActionForward preparePreCreationOfCandidacyInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        return preparePreCreationOfCandidacy(mapping, form, request, response);
    }

    protected CandidacyProcess getCurrentOpenParentProcess() {
        Set<Process> degreeChangeCandidacyProcesses =
                Sets.<Process> newHashSet(Iterables.filter(Bennu.getInstance().getProcessesSet(), getParentProcessType()));

        for (Process candidacyProcess : degreeChangeCandidacyProcesses) {
            if (candidacyProcess instanceof CandidacyProcess && ((CandidacyProcess) candidacyProcess).hasOpenCandidacyPeriod()) {
                return (CandidacyProcess) candidacyProcess;
            }
        }

        return null;
    }

    public ActionForward bindEmailWithHashCodeAndSendMailWithLink(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        if (!validateCaptcha(mapping, request)) {
            invalidateDocumentFileRelatedViewStates();
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("show-pre-creation-candidacy-form");
        }

        try {
            String email = (String) getObjectFromViewState("PublicAccessCandidacy.preCreationForm");
            DegreeOfficePublicCandidacyHashCode hash =
                    DegreeOfficePublicCandidacyHashCodeOperations
                            .getUnusedOrCreateNewHashCodeAndSendEmailForApplicationSubmissionToCandidate(getProcessType(),
                                    getCurrentOpenParentProcess(), email);

            String link =
                    String.format(BundleUtil.getString(Bundle.CANDIDATE, getProcessType().getSimpleName()
                            + ".const.public.application.submission.link"), hash.getValue(), I18N.getLocale().getLanguage());

            request.setAttribute("link", link);

            return mapping.findForward("show-email-message-sent");
        } catch (HashCodeForEmailAndProcessAlreadyBounded e) {
            addActionMessage(request, "error.candidacy.hash.code.already.bounded");
            return mapping.findForward("show-pre-creation-candidacy-form");
        }
    }

    public ActionForward showApplicationSubmissionConditions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        String hash = request.getParameter("hash");
        DegreeOfficePublicCandidacyHashCode candidacyHashCode =
                (DegreeOfficePublicCandidacyHashCode) DegreeOfficePublicCandidacyHashCode.getPublicCandidacyCodeByHash(hash);

        if (candidacyHashCode == null) {
            return mapping.findForward("open-candidacy-processes-not-found");
        }

        if (candidacyHashCode.getIndividualCandidacyProcess() != null) {
            request.setAttribute("individualCandidacyProcess", candidacyHashCode.getIndividualCandidacyProcess());
            return viewCandidacy(mapping, form, request, response);
        }

        request.setAttribute("hash", hash);
        return mapping.findForward("show-application-submission-conditions");
    }

    protected abstract String getCandidacyNameKey();

    protected abstract String getCandidacyInformationLinkDefaultLanguage();

    protected abstract String getCandidacyInformationLinkEnglish();

    public abstract ActionForward viewCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response);

    protected boolean validateCaptcha(ActionMapping mapping, HttpServletRequest request) {
        final String captchaResponse = request.getParameter("j_captcha_response");

        if (!KaptchaAction.validateResponse(request.getSession(), captchaResponse)) {
            addActionMessage("captcha.error", request, "captcha.wrong.word");
            return false;
        }
        return true;
    }

    private boolean isInEnglishLocale() {
        Locale locale = I18N.getLocale();
        return locale.getLanguage().equals(Locale.ENGLISH.getLanguage());
    }

    public ActionForward continueCandidacyCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();

        final PersonBean personBean = bean.getPersonBean();

        if (existsIndividualCandidacyProcessForDocumentId(request, personBean.getIdDocumentType(),
                personBean.getDocumentIdNumber())) {
            addActionMessage("individualCandidacyMessages", request, "error.candidacy.for.person.already.exists");
            return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);
        }

        final Set<Person> persons = new HashSet<Person>(Person.readByDocumentIdNumber(personBean.getDocumentIdNumber()));

        if (persons.size() > 1) {
            addActionMessage("individualCandidacyMessages", request, getProcessType().getSimpleName()
                    + ".error.public.candidacies.fill.personal.information.and.institution.id.person.already.exist");
            return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);
        }

        final Person person = persons.size() == 1 ? persons.iterator().next() : null;

        // check if person already exists
        if (person != null) {
            if (isPersonStudentOrEmployeeAndNumberIsCorrect(person, bean.getPersonNumber())) {
                if (!person.getDateOfBirthYearMonthDay().equals(personBean.getDateOfBirth())) {
                    // found person with diff date
                    addActionMessage("individualCandidacyMessages", request, getProcessType().getSimpleName()
                            + ".error.public.candidacies.fill.personal.information.and.institution.id.birth");
                    return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);
                } else if (!StringUtils.isEmpty(personBean.getSocialSecurityNumber())
                        && !StringUtils.isEmpty(person.getSocialSecurityNumber())
                        && !person.getSocialSecurityNumber().equals(personBean.getSocialSecurityNumber())) {
                    // found person with diff social security number
                    addActionMessage("individualCandidacyMessages", request, getProcessType().getSimpleName()
                            + ".error.public.candidacies.fill.personal.information.and.institution.id.contributorNumber");
                    return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);
                } else {
                    personBean.setPerson(person);
                }
            } else {
                // found person with diff ist userid
                addActionMessage("individualCandidacyMessages", request, getProcessType().getSimpleName()
                        + ".error.public.candidacies.fill.personal.information.and.institution.id.userId");
                return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);
            }
        } else {
            if (Person.readByContributorNumber(personBean.getSocialSecurityNumber()) != null) {
                // found person with same contributor number
                addActionMessage("individualCandidacyMessages", request, getProcessType().getSimpleName()
                        + ".error.public.candidacies.fill.personal.information.and.institution.id.contributorNumber");
                return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);
            }

            if (!StringUtils.isEmpty(bean.getPersonNumber())) {
                // person must fill ist userid
                addActionMessage("individualCandidacyMessages", request, getProcessType().getSimpleName()
                        + ".error.public.candidacies.fill.personal.information.and.institution.id.userId.missing");
                return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);
            } else {
                fillExternalPrecedentInformation(mapping, form, request, response);
            }
        }

        IndividualCandidacyDocumentFile photoDocumentFile =
                createIndividualCandidacyDocumentFile(bean.getPhotoDocument(), bean.getPersonBean().getDocumentIdNumber());
        bean.getPhotoDocument().setDocumentFile(photoDocumentFile);
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

        return mapping.findForward("candidacy-continue-creation");
    }

    public ActionForward continueCandidacyCreationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        invalidateDocumentFileRelatedViewStates();

        return mapping.findForward("candidacy-continue-creation");
    }

    protected boolean isPersonStudentOrEmployeeAndNumberIsCorrect(Person person, String personNumber) {
        return (person.hasStudent() && person.getStudent().getNumber().toString().equals(personNumber))
                || (person.hasEmployee() && person.getEmployee().getEmployeeNumber().toString().equals(personNumber))
                || (!person.hasStudent() && !person.hasEmployee() && StringUtils.isEmpty(personNumber));
    }

    public ActionForward executeCreateCandidacyPersonalInformationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        invalidateDocumentFileRelatedViewStates();

        return mapping.findForward("show-candidacy-creation-page");
    }

    protected boolean candidacyIndividualProcessExistsForThisEmail(String email) {
        return candidacyIndividualProcessExistsForThisEmail(email, new ArrayList<Degree>());
    }

    protected boolean candidacyIndividualProcessExistsForThisEmail(String email, List<Degree> degreeList) {
        return DegreeOfficePublicCandidacyHashCode.getPublicCandidacyHashCodeByEmailAndCandidacyProcessType(email,
                getProcessType(), getCurrentOpenParentProcess(), degreeList) != null;
    }

    protected IndividualCandidacyProcess createNewPublicProcess(IndividualCandidacyProcessBean bean) throws DomainException,
            FenixServiceException {
        return (IndividualCandidacyProcess) CreateNewProcess.run(getProcessType(), bean,
                buildActivitiesForApplicationSubmission(bean.getPublicCandidacyHashCode()));
    }

    private List<Pair<Class<?>, Object>> buildActivitiesForApplicationSubmission(DegreeOfficePublicCandidacyHashCode hashCode) {
        final List<Pair<Class<?>, Object>> result = new ArrayList<Pair<Class<?>, Object>>();

        result.add(pair(SendEmailForApplicationSubmission.class, hashCode));

        return result;
    }

    private Pair<Class<?>, Object> pair(final Class<?> class1, final Object object) {
        return new Pair<Class<?>, Object>(class1, object);
    }

    public ActionForward fillInternalPrecedentInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        final IndividualCandidacyProcessWithPrecedentDegreeInformationBean bean =
                (IndividualCandidacyProcessWithPrecedentDegreeInformationBean) getIndividualCandidacyProcessBean();

        StudentCurricularPlan studentCurricularPlan = bean.getLastPrecedentStudentCurricularPlan();

        if (studentCurricularPlan == null) {
            addActionMessage("candidacyMessages", request, "error.public.candidacies.message.no.student.curricular.plan", null);
        } else {
            if (studentCurricularPlan.getRegistration().isTransited()) {
                addActionMessage("candidacyMessages", request, "error.public.candidacies.message.no.student.curricular.plan",
                        null);
            } else {
                bean.setPrecedentDegreeType(PrecedentDegreeType.INSTITUTION_DEGREE);
                bean.setPrecedentStudentCurricularPlan(studentCurricularPlan);
                createCandidacyPrecedentDegreeInformation(bean, bean.getPrecedentStudentCurricularPlan());
            }
        }

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        return mapping.findForward("candidacy-continue-creation");
    }

    public ActionForward fillExternalPrecedentInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        final IndividualCandidacyProcessWithPrecedentDegreeInformationBean bean =
                (IndividualCandidacyProcessWithPrecedentDegreeInformationBean) getIndividualCandidacyProcessBean();
        bean.setPrecedentDegreeType(PrecedentDegreeType.EXTERNAL_DEGREE);
        bean.setPrecedentDegreeInformation(new PrecedentDegreeInformationBean());

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        return mapping.findForward("candidacy-continue-creation");
    }

    public ActionForward prepareEditCandidacyProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("edit-candidacy");
    }

    public ActionForward editCandidacyProcessInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("edit-candidacy");
    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        // TODO Auto-generated method stub

    }

    protected String getFormattedApplicationSubmissionEndDate() {
        DateTime end = getCurrentOpenParentProcess().getCandidacyEnd();
        if (isInEnglishLocale()) {
            return end.toString("dd', 'MMMM' of 'yyyy", I18N.getLocale());
        }
        return end.toString("dd' de 'MMMM' de 'yyyy", I18N.getLocale());
    }

    @Override
    protected boolean existsIndividualCandidacyProcessForDocumentId(HttpServletRequest request, IDDocumentType documentType,
            String identification) {
        return getCurrentOpenParentProcess().getOpenChildProcessByDocumentId(documentType, identification) != null;
    }

    protected boolean isApplicationSubmissionPeriodValid() {
        CandidacyProcess process = getCurrentOpenParentProcess();

        if (process == null) {
            return false;
        }

        DateTime now = new DateTime(System.currentTimeMillis());
        return now.isAfter(process.getCandidacyStart()) && now.isBefore(process.getCandidacyEnd());
    }

    public ActionForward backToViewCandidacyInternal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        IndividualCandidacyProcess individualCandidacyProcess =
                (IndividualCandidacyProcess) request.getAttribute("individualCandidacyProcess");
        return forward(request, mapping.getPath() + ".do?method=prepareCandidacyCreation&hash="
                + individualCandidacyProcess.getCandidacyHashCode().getValue());
    }

    public ActionForward prepareEditCandidacyQualifications(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("edit-candidacy-habilitations");
    }

    private ActionForward forward(HttpServletRequest request, String windowLocation) {
        final ActionForward actionForward = new ActionForward();
        actionForward.setName(windowLocation);
        actionForward.setPath(windowLocation);
        actionForward.setRedirect(true);
        return actionForward;
    }

    public static class CandidacyPreCreationBean implements java.io.Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        private String email;

        public String getEmail() {
            return this.email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public ActionForward backToViewCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        IndividualCandidacyProcess individualCandidacyProcess = getDomainObject(request, "individualCandidacyProcess");
        return forward(
                request,
                getLinkFromPublicCandidacyHashCodeForInternalUse(mapping, request,
                        individualCandidacyProcess.getCandidacyHashCode()));
    }

    protected String getLinkFromPublicCandidacyHashCodeForInternalUse(ActionMapping mapping, HttpServletRequest request,
            PublicCandidacyHashCode hashCode) {
        return mapping.getPath() + ".do?method=prepareCandidacyCreation&hash=" + hashCode.getValue();
    }

    public ActionForward prepareEditCandidacyDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        CandidacyProcessDocumentUploadBean bean = new CandidacyProcessDocumentUploadBean();
        bean.setIndividualCandidacyProcess(getIndividualCandidacyProcessBean().getIndividualCandidacyProcess());
        request.setAttribute("candidacyDocumentUploadBean", bean);
        return mapping.findForward("edit-candidacy-documents");
    }

    public ActionForward prepareEditCandidacyDocumentsInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        CandidacyProcessDocumentUploadBean bean = new CandidacyProcessDocumentUploadBean();
        bean.setIndividualCandidacyProcess(getIndividualCandidacyProcessBean().getIndividualCandidacyProcess());
        request.setAttribute("candidacyDocumentUploadBean", bean);
        return mapping.findForward("edit-candidacy-documents");
    }

    public ActionForward editCandidacyDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, IOException {
        CandidacyProcessDocumentUploadBean uploadBean =
                (CandidacyProcessDocumentUploadBean) getObjectFromViewState("individualCandidacyProcessBean.document.file");
        try {
            IndividualCandidacyDocumentFile documentFile =
                    createIndividualCandidacyDocumentFile(uploadBean, uploadBean.getIndividualCandidacyProcess()
                            .getPersonalDetails().getDocumentIdNumber());
            uploadBean.setDocumentFile(documentFile);

            executeActivity(uploadBean.getIndividualCandidacyProcess(), "EditPublicCandidacyDocumentFile", uploadBean);
            request.setAttribute("individualCandidacyProcess", uploadBean.getIndividualCandidacyProcess());
            return backToViewCandidacyInternal(mapping, form, request, response);
        } catch (final DomainException e) {
            invalidateDocumentFileRelatedViewStates();
            CandidacyProcessDocumentUploadBean bean = new CandidacyProcessDocumentUploadBean();
            bean.setIndividualCandidacyProcess(uploadBean.getIndividualCandidacyProcess());
            request.setAttribute("candidacyDocumentUploadBean", bean);

            addActionMessage("error", request, e.getMessage(), e.getArgs());
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("edit-candidacy-documents");
        }
    }

    public ActionForward backCandidacyCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("show-candidacy-creation-page");
    }

    public ActionForward prepareRecoverAccessLink(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        request.setAttribute("candidacyPreCreationBean", new CandidacyPreCreationBean());
        return mapping.findForward("show-recover-access-link-form");
    }

    public ActionForward prepareRecoverAccessLinkInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("candidacyPreCreationBean", new CandidacyPreCreationBean());
        return mapping.findForward("show-recover-access-link-form");
    }

    public ActionForward sendAccessLinkEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        String email = (String) getObjectFromViewState("PublicAccessCandidacy.preCreationForm");
        DegreeOfficePublicCandidacyHashCode hash =
                DegreeOfficePublicCandidacyHashCode.getPublicCandidacyHashCodeByEmailAndCandidacyProcessTypeOrNotAssociated(
                        email, getProcessType(), getCurrentOpenParentProcess());

        if (hash != null) {
            hash.sendEmailFoAccessLinkRecovery();
        }

        return mapping.findForward("show-recovery-email-sent");
    }

    public ActionForward prepareUploadPhoto(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        CandidacyProcessDocumentUploadBean bean = new CandidacyProcessDocumentUploadBean();
        bean.setIndividualCandidacyProcess(getIndividualCandidacyProcessBean().getIndividualCandidacyProcess());
        bean.setType(IndividualCandidacyDocumentFileType.PHOTO);

        request.setAttribute("candidacyDocumentUploadBean", bean);
        return mapping.findForward("upload-photo");
    }

    public ActionForward uploadPhotoInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        CandidacyProcessDocumentUploadBean bean = new CandidacyProcessDocumentUploadBean();
        bean.setIndividualCandidacyProcess(getIndividualCandidacyProcessBean().getIndividualCandidacyProcess());
        request.setAttribute("candidacyDocumentUploadBean", bean);
        return mapping.findForward("upload-photo");
    }

    public ActionForward uploadPhoto(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, IOException {
        CandidacyProcessDocumentUploadBean uploadBean =
                (CandidacyProcessDocumentUploadBean) getObjectFromViewState("individualCandidacyProcessBean.document.file");
        try {
            IndividualCandidacyDocumentFile documentFile =
                    createIndividualCandidacyDocumentFile(uploadBean, uploadBean.getIndividualCandidacyProcess()
                            .getPersonalDetails().getDocumentIdNumber());
            uploadBean.setDocumentFile(documentFile);

            executeActivity(uploadBean.getIndividualCandidacyProcess(), "EditPublicCandidacyDocumentFile", uploadBean);
            request.setAttribute("individualCandidacyProcess", uploadBean.getIndividualCandidacyProcess());
            return backToViewCandidacyInternal(mapping, form, request, response);
        } catch (final DomainException e) {
            invalidateDocumentFileRelatedViewStates();
            CandidacyProcessDocumentUploadBean bean = new CandidacyProcessDocumentUploadBean();
            bean.setIndividualCandidacyProcess(uploadBean.getIndividualCandidacyProcess());
            request.setAttribute("candidacyDocumentUploadBean", bean);

            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("upload-photo");
        }
    }

    public ActionForward candidaciesTypesInformationIntro(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        Locale locale = I18N.getLocale();
        String countryCode = readCountryCode(locale);

        String institutionalURL = Installation.getInstance().getInstituitionURL();
        if ("PT".equals(countryCode)) {
            return redirect(institutionalURL + "pt/candidatos/candidaturas/", request);
        }

        return redirect(institutionalURL + "en/prospective-students/admissions/", request);
    }

    static private String readCountryCode(final Locale locale) {
        String country = locale.getCountry();
        String language = locale.getLanguage();

        String result = null;
        if (!StringUtils.isEmpty(country)) {
            result = country.toUpperCase();
        } else if (!StringUtils.isEmpty(language)) {
            result = language.toUpperCase();
        }

        if (!StringUtils.isEmpty(result)) {
            return result;
        }

        return "PT";
    }

}
