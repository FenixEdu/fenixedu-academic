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
package org.fenixedu.academic.ui.struts.action.publico.candidacies.over23;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Installation;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.PublicCandidacyHashCode;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcessBean;
import org.fenixedu.academic.domain.candidacyProcess.over23.Over23CandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.over23.Over23IndividualCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.over23.Over23IndividualCandidacyProcessBean;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.publico.PublicApplication.PublicCandidaciesApp;
import org.fenixedu.academic.ui.struts.action.publico.candidacies.RefactoredIndividualCandidacyProcessPublicDA;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@StrutsFunctionality(app = PublicCandidaciesApp.class, path = "over-23", titleKey = "title.application.name.over23")
@Mapping(path = "/candidacies/caseHandlingOver23IndividualCandidacyProcess", module = "publico")
@Forwards({
        @Forward(name = "begin-candidacy-process-intro", path = "/publico/candidacy/over23/main.jsp"),
        @Forward(name = "open-candidacy-process-closed", path = "/publico/candidacy/candidacyProcessClosed.jsp"),
        @Forward(name = "show-pre-creation-candidacy-form", path = "/publico/candidacy/preCreationCandidacyForm.jsp"),
        @Forward(name = "show-email-message-sent", path = "/publico/candidacy/showEmailSent.jsp"),
        @Forward(name = "show-application-submission-conditions", path = "/publico/candidacy/applicationSubmissionConditions.jsp"),
        @Forward(name = "open-candidacy-processes-not-found", path = "/publico/candidacy/individualCandidacyNotFound.jsp"),
        @Forward(name = "show-candidacy-creation-page", path = "/publico/candidacy/over23/createCandidacyPartOne.jsp"),
        @Forward(name = "candidacy-continue-creation", path = "/publico/candidacy/over23/createCandidacyPartTwo.jsp"),
        @Forward(name = "inform-submited-candidacy", path = "/publico/candidacy/candidacySubmited.jsp"),
        @Forward(name = "show-candidacy-details", path = "/publico/candidacy/over23/viewCandidacy.jsp"),
        @Forward(name = "edit-candidacy", path = "/publico/candidacy/over23/editCandidacy.jsp"),
        @Forward(name = "edit-candidacy-habilitations", path = "/publico/candidacy/over23/editCandidacyHabilitations.jsp"),
        @Forward(name = "edit-candidacy-documents", path = "/publico/candidacy/over23/editCandidacyDocuments.jsp") })
public class Over23IndividualCandidacyProcessRefactoredDA extends RefactoredIndividualCandidacyProcessPublicDA {

    private static final Logger logger = LoggerFactory.getLogger(Over23IndividualCandidacyProcessRefactoredDA.class);

    @Override
    protected String getCandidacyInformationLinkDefaultLanguage() {
        String message = getStringFromDefaultBundle("link.candidacy.information.default.over23");
        return MessageFormat.format(message, Installation.getInstance().getInstituitionURL());
    }

    @Override
    protected String getCandidacyInformationLinkEnglish() {
        String message = getStringFromDefaultBundle("link.candidacy.information.english.over23");
        return MessageFormat.format(message, Installation.getInstance().getInstituitionURL());
    }

    @Override
    protected String getCandidacyNameKey() {
        return "title.application.name.over23";
    }

    @Override
    public ActionForward viewCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        Over23IndividualCandidacyProcess individualCandidacyProcess =
                (Over23IndividualCandidacyProcess) request.getAttribute("individualCandidacyProcess");
        Over23IndividualCandidacyProcessBean bean = new Over23IndividualCandidacyProcessBean(individualCandidacyProcess);

        bean.setPersonBean(new PersonBean(individualCandidacyProcess.getPersonalDetails()));
        request.setAttribute("individualCandidacyProcessBean", bean);

        return mapping.findForward("show-candidacy-details");
    }

    public ActionForward prepareCandidacyCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        CandidacyProcess candidacyProcess = getCurrentOpenParentProcess();

        String hash = request.getParameter("hash");
        DegreeOfficePublicCandidacyHashCode candidacyHashCode =
                (DegreeOfficePublicCandidacyHashCode) PublicCandidacyHashCode.getPublicCandidacyCodeByHash(hash);

        if (candidacyHashCode == null) {
            return mapping.findForward("open-candidacy-processes-not-found");
        }

        if (candidacyHashCode.getIndividualCandidacyProcess() != null
                && candidacyHashCode.getIndividualCandidacyProcess().getCandidacyProcess() == candidacyProcess) {
            request.setAttribute("individualCandidacyProcess", candidacyHashCode.getIndividualCandidacyProcess());
            return viewCandidacy(mapping, form, request, response);
        } else if (candidacyHashCode.getIndividualCandidacyProcess() != null
                && candidacyHashCode.getIndividualCandidacyProcess().getCandidacyProcess() != candidacyProcess) {
            return mapping.findForward("open-candidacy-processes-not-found");
        }

        Over23IndividualCandidacyProcessBean bean = new Over23IndividualCandidacyProcessBean();
        bean.setPersonBean(new PersonBean());
        bean.setCandidacyProcess(candidacyProcess);
        bean.setPublicCandidacyHashCode(candidacyHashCode);

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        bean.getPersonBean().setEmail(candidacyHashCode.getEmail());
        return mapping.findForward("show-candidacy-creation-page");

    }

    @Override
    protected void setParentProcess(HttpServletRequest request) {
        super.setParentProcess(request);
        if (request.getAttribute("parentProcess") == null) {
            request.setAttribute("parentProcess", getCurrentOpenParentProcess());
        }
    }

    @Override
    public ActionForward addConcludedHabilitationsEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        bean.addConcludedFormationBean();

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        invalidateDocumentFileRelatedViewStates();

        return forwardTo(mapping, request);
    }

    public ActionForward addNonConcludedHabilitationsEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        bean.addNonConcludedFormationBean();

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        invalidateDocumentFileRelatedViewStates();

        return forwardTo(mapping, request);
    }

    @Override
    public ActionForward removeConcludedHabilitationsEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        Integer index = getIntegerFromRequest(request, "removeIndex");
        bean.removeFormationConcludedBean(index);

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        invalidateDocumentFileRelatedViewStates();

        return forwardTo(mapping, request);
    }

    public ActionForward removeNonConcludedHabilitationsEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse reponse) {
        IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        Integer index = getIntegerFromRequest(request, "removeIndex");
        bean.removeFormationNonConcludedBean(index);

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        invalidateDocumentFileRelatedViewStates();

        return forwardTo(mapping, request);
    }

    public ActionForward addSelectedDegreesEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        Over23IndividualCandidacyProcessBean bean = (Over23IndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();

        if (bean.hasDegreeToAdd() && !bean.containsDegree(bean.getDegreeToAdd())) {
            bean.addDegree(bean.getDegreeToAdd());
            bean.setDegreeToAdd(null);
        }

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        invalidateDocumentFileRelatedViewStates();

        return forwardTo(mapping, request);
    }

    public ActionForward removeSelectedDegreesEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        Over23IndividualCandidacyProcessBean bean = (Over23IndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
        Integer index = getIntegerFromRequest(request, "removeIndex");
        bean.removeDegree(bean.getSelectedDegrees().get(index));

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        invalidateDocumentFileRelatedViewStates();

        return forwardTo(mapping, request);
    }

    private ActionForward forwardTo(ActionMapping mapping, HttpServletRequest request) {
        if (getFromRequest(request, "userAction").equals("createCandidacy")) {
            return mapping.findForward("candidacy-continue-creation");
        } else if (getFromRequest(request, "userAction").equals("editCandidacyQualifications")) {
            return mapping.findForward("edit-candidacy-habilitations");
        }

        return null;
    }

    @Override
    public ActionForward continueCandidacyCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
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
                    + ".error.public.candidacies.fill.personal.information.and.institution.person.already.exist");
            return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);
        }

        final Person person = persons.size() == 1 ? persons.iterator().next() : null;

        // check if person already exists
        if (person != null) {
            if (isPersonStudentAndNumberIsCorrect(person, bean.getPersonNumber())) {
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

            // person does not exist (neither by document Id nor by contributor number)

            if (!StringUtils.isEmpty(bean.getPersonNumber())) {
                // userid field was filled
                addActionMessage("individualCandidacyMessages", request, getProcessType().getSimpleName()
                        + ".error.public.candidacies.fill.personal.information.and.institution.id.userId.wrongIdorNewPerson");
                return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);
            } else {
                request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
                return mapping.findForward("candidacy-continue-creation");
            }
        }

        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

        return mapping.findForward("candidacy-continue-creation");
    }

    public ActionForward submitCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, FenixServiceException {
        try {
            ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
            if (actionForwardError != null) {
                return actionForwardError;
            }

            Over23IndividualCandidacyProcessBean bean =
                    (Over23IndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
            bean.setInternalPersonCandidacy(Boolean.TRUE);

            boolean isValid = validateOver23IndividualCandidacy(request, bean) && hasInvalidViewState();
            if (!isValid) {
                invalidateDocumentFileRelatedViewStates();
                request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
                return mapping.findForward("candidacy-continue-creation");
            }

            if (candidacyIndividualProcessExistsForThisEmail(bean.getPersonBean().getEmail(), bean.getSelectedDegrees())) {
                addActionMessage("error", request, "error.candidacy.hash.code.already.bounded");
                invalidateDocumentFileRelatedViewStates();
                request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
                return mapping.findForward("candidacy-continue-creation");
            }

            if (!bean.getHonorAgreement()) {
                addActionMessage("error", request, "error.must.agree.on.declaration.of.honor");
                invalidateDocumentFileRelatedViewStates();
                request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
                return mapping.findForward("candidacy-continue-creation");
            }

            /*
             * 10/05/2009 - Since we step candidacy information form we must
             * copy some fields
             */

            Over23IndividualCandidacyProcess process = (Over23IndividualCandidacyProcess) createNewPublicProcess(bean);

            request.setAttribute("process", process);
            request.setAttribute("mappingPath", mapping.getPath());
            request.setAttribute("individualCandidacyProcess", process);
            request.setAttribute("endSubmissionDate", getFormattedApplicationSubmissionEndDate());

            return mapping.findForward("inform-submited-candidacy");
        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage(), e.getArgs());
            logger.error(e.getMessage(), e);
            getIndividualCandidacyProcessBean().getPersonBean().setPerson(null);
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("candidacy-continue-creation");
        }
    }

    private boolean validateOver23IndividualCandidacy(HttpServletRequest request, Over23IndividualCandidacyProcessBean bean) {
        boolean isValid = true;

        if (bean.getSelectedDegrees().isEmpty()) {
            addActionMessage("error", request, "error.formation.selectedDegrees.required");
            isValid = false;
        }

        if (bean.getFormationConcludedBeanList().isEmpty() && bean.getFormationNonConcludedBeanList().isEmpty()) {
            addActionMessage("error", request, "error.formation.required");
            return false;
        }

        return isValid;
    }

    public ActionForward editCandidacyProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        Over23IndividualCandidacyProcessBean bean = (Over23IndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
        PersonBean personBean = bean.getPersonBean();

        try {
            ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
            if (actionForwardError != null) {
                return actionForwardError;
            }

            if (!isApplicationSubmissionPeriodValid()) {
                return beginCandidacyProcessIntro(mapping, form, request, response);
            }

            final Set<Person> persons = new HashSet<Person>(Person.readByDocumentIdNumber(personBean.getDocumentIdNumber()));

            if (persons.size() > 1) {
                addActionMessage("individualCandidacyMessages", request, getProcessType().getSimpleName()
                        + ".error.public.candidacies.fill.personal.information.and.institution.id.person.already.exist");
                return prepareEditCandidacyProcess(mapping, form, request, response);
            }

            final Person person = persons.size() == 1 ? persons.iterator().next() : null;

            // check if person already exists
            if (person != null) {
                if (isPersonStudentAndNumberIsCorrect(person, bean.getPersonNumber())) {
                    if (!person.getDateOfBirthYearMonthDay().equals(personBean.getDateOfBirth())) {
                        // found person with diff date
                        addActionMessage("individualCandidacyMessages", request, getProcessType().getSimpleName()
                                + ".error.public.candidacies.fill.personal.information.and.institution.id.birth");
                        return prepareEditCandidacyProcess(mapping, form, request, response);
                    } else if (!StringUtils.isEmpty(personBean.getSocialSecurityNumber())
                            && !StringUtils.isEmpty(person.getSocialSecurityNumber())
                            && !person.getSocialSecurityNumber().equals(personBean.getSocialSecurityNumber())) {
                        // found person with diff social security number
                        addActionMessage("individualCandidacyMessages", request, getProcessType().getSimpleName()
                                + ".error.public.candidacies.fill.personal.information.and.institution.id.contributorNumber");
                        return prepareEditCandidacyProcess(mapping, form, request, response);
                    } else {
                        personBean.setPerson(person);
                    }
                } else {
                    // found person with diff ist userid
                    addActionMessage("individualCandidacyMessages", request, getProcessType().getSimpleName()
                            + ".error.public.candidacies.fill.personal.information.and.institution.id.userId");
                    return prepareEditCandidacyProcess(mapping, form, request, response);
                }
            } else {
                if (Person.readByContributorNumber(personBean.getSocialSecurityNumber()) != null) {
                    // found person with same contributor number
                    addActionMessage("individualCandidacyMessages", request, getProcessType().getSimpleName()
                            + ".error.public.candidacies.fill.personal.information.and.institution.id.contributorNumber");
                    return prepareEditCandidacyProcess(mapping, form, request, response);
                }
            }

            executeActivity(bean.getIndividualCandidacyProcess(), "EditPublicCandidacyPersonalInformation",
                    getIndividualCandidacyProcessBean());
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("edit-candidacy");
        }

        request.setAttribute("individualCandidacyProcess", bean.getIndividualCandidacyProcess());
        return backToViewCandidacyInternal(mapping, form, request, response);
    }

    public ActionForward editCandidacyHabilitations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        Over23IndividualCandidacyProcessBean bean = (Over23IndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
        try {
            boolean isValid = validateOver23IndividualCandidacy(request, bean) && hasInvalidViewState();
            if (!isValid) {
                request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
                return mapping.findForward("edit-candidacy-habilitations");
            }

            if (!isApplicationSubmissionPeriodValid()) {
                return beginCandidacyProcessIntro(mapping, form, request, response);
            }

            executeActivity(bean.getIndividualCandidacyProcess(), "EditPublicCandidacyHabilitations",
                    getIndividualCandidacyProcessBean());
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("edit-candidacy-habilitations");
        }

        request.setAttribute("individualCandidacyProcess", bean.getIndividualCandidacyProcess());
        return backToViewCandidacyInternal(mapping, form, request, response);
    }

    @Override
    protected Class getParentProcessType() {
        return Over23CandidacyProcess.class;
    }

    @Override
    protected Class getProcessType() {
        return Over23IndividualCandidacyProcess.class;
    }

}
