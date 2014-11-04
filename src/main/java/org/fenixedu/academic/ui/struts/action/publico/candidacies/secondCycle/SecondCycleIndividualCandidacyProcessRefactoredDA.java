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
package org.fenixedu.academic.ui.struts.action.publico.candidacies.secondCycle;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Installation;
import org.fenixedu.academic.domain.PublicCandidacyHashCode;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode;
import org.fenixedu.academic.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcessBean;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.candidacy.PrecedentDegreeInformationBean;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.publico.PublicApplication.PublicCandidaciesApp;
import org.fenixedu.academic.ui.struts.action.publico.candidacies.RefactoredIndividualCandidacyProcessPublicDA;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@StrutsFunctionality(app = PublicCandidaciesApp.class, path = "second-cycle",
        titleKey = "title.application.name.secondCycle.short")
@Mapping(path = "/candidacies/caseHandlingSecondCycleCandidacyIndividualProcess", module = "publico")
@Forwards({
        @Forward(name = "begin-candidacy-process-intro", path = "/publico/candidacy/secondCycle/main.jsp"),
        @Forward(name = "begin-candidacy-process-intro-en", path = "/publico/candidacy/secondCycle/main_en.jsp"),
        @Forward(name = "open-candidacy-process-closed", path = "/publico/candidacy/candidacyProcessClosed.jsp"),
        @Forward(name = "show-pre-creation-candidacy-form", path = "/publico/candidacy/preCreationCandidacyForm.jsp"),
        @Forward(name = "show-email-message-sent", path = "/publico/candidacy/showEmailSent.jsp"),
        @Forward(name = "show-application-submission-conditions", path = "/publico/candidacy/applicationSubmissionConditions.jsp"),
        @Forward(name = "open-candidacy-processes-not-found", path = "/publico/candidacy/individualCandidacyNotFound.jsp"),
        @Forward(name = "show-candidacy-creation-page", path = "/publico/candidacy/secondCycle/createCandidacyPartOne.jsp"),
        @Forward(name = "candidacy-continue-creation", path = "/publico/candidacy/secondCycle/createCandidacyPartTwo.jsp"),
        @Forward(name = "inform-submited-candidacy", path = "/publico/candidacy/candidacySubmited.jsp"),
        @Forward(name = "show-candidacy-details", path = "/publico/candidacy/secondCycle/viewCandidacy.jsp"),
        @Forward(name = "edit-candidacy", path = "/publico/candidacy/secondCycle/editCandidacy.jsp"),
        @Forward(name = "edit-candidacy-habilitations", path = "/publico/candidacy/secondCycle/editCandidacyHabilitations.jsp"),
        @Forward(name = "edit-candidacy-documents", path = "/publico/candidacy/secondCycle/editCandidacyDocuments.jsp"),
        @Forward(name = "show-recover-access-link-form", path = "/publico/candidacy/secondCycle/recoverAccess.jsp"),
        @Forward(name = "show-recovery-email-sent", path = "/publico/candidacy/secondCycle/recoveryEmailSent.jsp"),
        @Forward(name = "upload-photo", path = "/publico/candidacy/secondCycle/uploadPhoto.jsp") })
public class SecondCycleIndividualCandidacyProcessRefactoredDA extends RefactoredIndividualCandidacyProcessPublicDA {

    private static final Logger logger = LoggerFactory.getLogger(SecondCycleIndividualCandidacyProcessRefactoredDA.class);

    @Override
    protected Class<? extends CandidacyProcess> getParentProcessType() {
        return SecondCycleCandidacyProcess.class;
    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    protected Class getProcessType() {
        return SecondCycleIndividualCandidacyProcess.class;
    }

    @Override
    protected String getCandidacyNameKey() {
        return "title.application.name.secondCycle";
    }

    @Override
    public ActionForward viewCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        SecondCycleIndividualCandidacyProcess individualCandidacyProcess =
                (SecondCycleIndividualCandidacyProcess) request.getAttribute("individualCandidacyProcess");
        SecondCycleIndividualCandidacyProcessBean bean =
                new SecondCycleIndividualCandidacyProcessBean(individualCandidacyProcess);

        bean.setPersonBean(new PersonBean(individualCandidacyProcess.getPersonalDetails()));

        request.setAttribute("individualCandidacyProcessBean", bean);
        request.setAttribute("hasSelectedDegrees", !individualCandidacyProcess.getSelectedDegrees().isEmpty());
        request.setAttribute("isApplicationSubmissionPeriodValid",
                redefineApplicationSubmissionPeriodValid(individualCandidacyProcess));

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

        SecondCycleIndividualCandidacyProcessBean bean = new SecondCycleIndividualCandidacyProcessBean();
        bean.setPrecedentDegreeInformation(new PrecedentDegreeInformationBean());
        bean.setPersonBean(new PersonBean());
        bean.setCandidacyProcess(candidacyProcess);
        bean.setPublicCandidacyHashCode(candidacyHashCode);

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        bean.getPersonBean().setEmail(candidacyHashCode.getEmail());
        return mapping.findForward("show-candidacy-creation-page");

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
    public ActionForward addConcludedHabilitationsEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        SecondCycleIndividualCandidacyProcessBean bean =
                (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
        bean.addConcludedFormationBean();

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        invalidateDocumentFileRelatedViewStates();

        return forwardTo(mapping, request);
    }

    @Override
    public ActionForward removeConcludedHabilitationsEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        SecondCycleIndividualCandidacyProcessBean bean =
                (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
        Integer index = getIntegerFromRequest(request, "removeIndex");
        bean.removeFormationConcludedBean(index);

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        invalidateDocumentFileRelatedViewStates();

        return forwardTo(mapping, request);
    }

    public ActionForward submitCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, FenixServiceException {
        try {
            ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
            if (actionForwardError != null) {
                return actionForwardError;
            }

            SecondCycleIndividualCandidacyProcessBean bean =
                    (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
            bean.setInternalPersonCandidacy(Boolean.TRUE);

            boolean isValid = hasInvalidViewState();
            if (!isValid) {
                invalidateDocumentFileRelatedViewStates();
                request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
                return mapping.findForward("candidacy-continue-creation");
            }

            List<Degree> degreeList = new ArrayList<Degree>(bean.getSelectedDegreeList());
            if (candidacyIndividualProcessExistsForThisEmail(bean.getPersonBean().getEmail(), degreeList)) {
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

            if (bean.getSelectedDegreeList().isEmpty()) {
                addActionMessage("error", request, "error.must.select.at.least.one.degree");
                invalidateDocumentFileRelatedViewStates();
                request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
                return mapping.findForward("candidacy-continue-creation");
            }

            SecondCycleIndividualCandidacyProcess process = (SecondCycleIndividualCandidacyProcess) createNewPublicProcess(bean);

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

    public ActionForward editCandidacyProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        SecondCycleIndividualCandidacyProcessBean bean =
                (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
        try {
            ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
            if (actionForwardError != null) {
                return actionForwardError;
            }

            if (!isApplicationSubmissionPeriodValid()) {
                return beginCandidacyProcessIntro(mapping, form, request, response);
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

    public ActionForward editCandidacyQualifications(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
        if (actionForwardError != null) {
            return actionForwardError;
        }

        SecondCycleIndividualCandidacyProcessBean bean =
                (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
        try {
            boolean isValid = hasInvalidViewState();
            if (!isValid) {
                request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
                return mapping.findForward("edit-candidacy-habilitations");
            }

            if (bean.getSelectedDegreeList().isEmpty()) {
                addActionMessage(request, "error.must.select.at.least.one.degree");
                request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
                return mapping.findForward("edit-candidacy-habilitations");
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

    public ActionForward addSelectedDegreesEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        SecondCycleIndividualCandidacyProcessBean bean =
                (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();

        if (bean.getSelectedDegree() != null && !bean.getSelectedDegreeList().contains(bean.getSelectedDegree())) {
            bean.addSelectedDegree(bean.getSelectedDegree());
        }

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        invalidateDocumentFileRelatedViewStates();

        return forwardTo(mapping, request);
    }

    public ActionForward removeSelectedDegreesEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        SecondCycleIndividualCandidacyProcessBean bean =
                (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();

        Degree selectedDegree = getDomainObject(request, "removeIndex");
        bean.removeSelectedDegree(selectedDegree);

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        invalidateDocumentFileRelatedViewStates();

        return forwardTo(mapping, request);
    }

    protected boolean redefineApplicationSubmissionPeriodValid(
            final SecondCycleIndividualCandidacyProcess individualCandidacyProcess) {
        CandidacyProcess process = getCurrentOpenParentProcess();

        if (process == null) {
            return false;
        }

        DateTime now = new DateTime();

        return now.isAfter(process.getCandidacyStart()) && now.isBefore(process.getCandidacyEnd());
    }

    @Override
    protected String getCandidacyInformationLinkDefaultLanguage() {
        String message = getStringFromDefaultBundle("link.candidacy.information.default.secondCycle");
        return MessageFormat.format(message, Installation.getInstance().getInstituitionURL());
    }

    @Override
    protected String getCandidacyInformationLinkEnglish() {
        String message = getStringFromDefaultBundle("link.candidacy.information.english.secondCycle");
        return MessageFormat.format(message, Installation.getInstance().getInstituitionURL());
    }

    @Override
    protected void setParentProcess(HttpServletRequest request) {
        super.setParentProcess(request);
        if (request.getAttribute("parentProcess") == null) {
            request.setAttribute("parentProcess", getCurrentOpenParentProcess());
        }
    }

}
