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
package org.fenixedu.academic.ui.struts.action.publico.candidacies.degreeTransfer;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Installation;
import org.fenixedu.academic.domain.PublicCandidacyHashCode;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import org.fenixedu.academic.domain.candidacyProcess.PrecedentDegreeInformationBeanFactory;
import org.fenixedu.academic.domain.candidacyProcess.degreeTransfer.DegreeTransferCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacyProcessBean;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.candidacy.PrecedentDegreeInformationBean;
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

@StrutsFunctionality(app = PublicCandidaciesApp.class, path = "degree-transfer",
        titleKey = "title.application.name.degreeTransfer")
@Mapping(path = "/candidacies/caseHandlingDegreeTransferIndividualCandidacyProcess", module = "publico")
@Forwards({
        @Forward(name = "begin-candidacy-process-intro", path = "/publico/candidacy/degreeTransfer/main.jsp"),
        @Forward(name = "begin-candidacy-process-intro-en", path = "/publico/candidacy/degreeTransfer/main.jsp"),
        @Forward(name = "open-candidacy-process-closed", path = "/publico/candidacy/candidacyProcessClosed.jsp"),
        @Forward(name = "show-pre-creation-candidacy-form", path = "/publico/candidacy/preCreationCandidacyForm.jsp"),
        @Forward(name = "show-email-message-sent", path = "/publico/candidacy/showEmailSent.jsp"),
        @Forward(name = "show-application-submission-conditions", path = "/publico/candidacy/applicationSubmissionConditions.jsp"),
        @Forward(name = "open-candidacy-processes-not-found", path = "/publico/candidacy/individualCandidacyNotFound.jsp"),
        @Forward(name = "show-candidacy-creation-page", path = "/publico/candidacy/degreeTransfer/createCandidacyPartOne.jsp"),
        @Forward(name = "candidacy-continue-creation", path = "/publico/candidacy/degreeTransfer/createCandidacyPartTwo.jsp"),
        @Forward(name = "inform-submited-candidacy", path = "/publico/candidacy/candidacySubmited.jsp"),
        @Forward(name = "show-candidacy-details", path = "/publico/candidacy/degreeTransfer/viewCandidacy.jsp"),
        @Forward(name = "edit-candidacy", path = "/publico/candidacy/degreeTransfer/editCandidacy.jsp"),
        @Forward(name = "edit-candidacy-habilitations", path = "/publico/candidacy/degreeTransfer/editCandidacyHabilitations.jsp"),
        @Forward(name = "edit-candidacy-documents", path = "/publico/candidacy/degreeTransfer/editCandidacyDocuments.jsp"),
        @Forward(name = "upload-photo", path = "/publico/candidacy/degreeTransfer/uploadPhoto.jsp") })
public class DegreeTransferIndividualCandidacyProcessRefactoredDA extends RefactoredIndividualCandidacyProcessPublicDA {

    private static final Logger logger = LoggerFactory.getLogger(DegreeTransferIndividualCandidacyProcessRefactoredDA.class);

    @Override
    protected String getCandidacyNameKey() {
        return "title.application.name.degreeTransfer";
    }

    @Override
    protected Class getParentProcessType() {
        return DegreeTransferCandidacyProcess.class;
    }

    @Override
    protected Class getProcessType() {
        return DegreeTransferIndividualCandidacyProcess.class;
    }

    @Override
    public ActionForward viewCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        DegreeTransferIndividualCandidacyProcess individualCandidacyProcess =
                (DegreeTransferIndividualCandidacyProcess) request.getAttribute("individualCandidacyProcess");
        DegreeTransferIndividualCandidacyProcessBean bean =
                new DegreeTransferIndividualCandidacyProcessBean(individualCandidacyProcess);

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

        DegreeTransferIndividualCandidacyProcessBean bean = new DegreeTransferIndividualCandidacyProcessBean();
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
        DegreeTransferIndividualCandidacyProcessBean bean =
                (DegreeTransferIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
        bean.addConcludedFormationBean();

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        invalidateDocumentFileRelatedViewStates();

        return forwardTo(mapping, request);
    }

    @Override
    public ActionForward removeConcludedHabilitationsEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        DegreeTransferIndividualCandidacyProcessBean bean =
                (DegreeTransferIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
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

            DegreeTransferIndividualCandidacyProcessBean bean =
                    (DegreeTransferIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
            bean.setInternalPersonCandidacy(Boolean.TRUE);

            boolean isValid = hasInvalidViewState();
            if (!isValid) {
                invalidateDocumentFileRelatedViewStates();
                request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
                return mapping.findForward("candidacy-continue-creation");
            }

            List<Degree> degreeList = new ArrayList<Degree>();
            degreeList.add(bean.getSelectedDegree());
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

            if (isOrWasEnrolledInInstitution(bean)) {
                addActionMessage("error", request, "error.degreeTransfer.is.or.was.enrolled.in.institution", Unit
                        .getInstitutionName().getContent());
                invalidateDocumentFileRelatedViewStates();
                request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
                return mapping.findForward("candidacy-continue-creation");
            }

            DegreeTransferIndividualCandidacyProcess process =
                    (DegreeTransferIndividualCandidacyProcess) createNewPublicProcess(bean);

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

    private boolean isOrWasEnrolledInInstitution(final DegreeTransferIndividualCandidacyProcessBean bean) {
        if (StringUtils.isEmpty(bean.getPersonNumber())) {
            return false;
        }

        Student student = Student.readStudentByNumber(Integer.valueOf(bean.getPersonNumber()));

        if (student == null) {
            throw new DomainException("error.degreeTransfer.person.number.is.not.empty.but.check.for.enrollment.on.institution");
        }

        ExecutionYear candidacyExecutionInterval = bean.getCandidacyExecutionInterval();

        for (Registration registration : student.getRegistrationsSet()) {
            StudentCurricularPlan lastStudentCurricularPlan = registration.getLastStudentCurricularPlan();

            if (lastStudentCurricularPlan.isActive(candidacyExecutionInterval.getPreviousExecutionYear())) {
                if ("LEIC-A 2006".equals(lastStudentCurricularPlan.getDegreeCurricularPlan().getName())
                        && "LEIC-T".equals(bean.getSelectedDegree().getSigla())) {
                    return false;
                }

                if ("LEIC-T 2006".equals(lastStudentCurricularPlan.getDegreeCurricularPlan().getName())
                        && "LEIC-A".equals(bean.getSelectedDegree().getSigla())) {
                    return false;
                }

                return true;
            }
        }

        return false;
    }

    public ActionForward editCandidacyProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DegreeTransferIndividualCandidacyProcessBean bean =
                (DegreeTransferIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
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

        DegreeTransferIndividualCandidacyProcessBean bean =
                (DegreeTransferIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
        try {
            boolean isValid = hasInvalidViewState();
            if (!isValid) {
                request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
                return mapping.findForward("edit-candidacy-habilitations");
            }

            IndividualCandidacyProcess individualCandidacyProcess = bean.getIndividualCandidacyProcess();

            if (individualCandidacyProcess.getPersonalDetails().getPerson().getStudent() != null) {
                bean.setPersonNumber(individualCandidacyProcess.getPersonalDetails().getPerson().getStudent().getNumber()
                        .toString());
            }

            if (isOrWasEnrolledInInstitution(bean)) {
                addActionMessage("error", request, "error.degreeTransfer.is.or.was.enrolled.in.institution", Unit
                        .getInstitutionName().getContent());
                request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
                return mapping.findForward("edit-candidacy-habilitations");
            }

            executeActivity(individualCandidacyProcess, "EditPublicCandidacyHabilitations", getIndividualCandidacyProcessBean());
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("edit-candidacy-habilitations");
        }

        request.setAttribute("individualCandidacyProcess", bean.getIndividualCandidacyProcess());
        return backToViewCandidacyInternal(mapping, form, request, response);
    }

    @Override
    protected void createCandidacyPrecedentDegreeInformation(IndividualCandidacyProcessWithPrecedentDegreeInformationBean bean,
            StudentCurricularPlan studentCurricularPlan) {
        final PrecedentDegreeInformationBean info =
                PrecedentDegreeInformationBeanFactory.createBean(studentCurricularPlan, CycleType.FIRST_CYCLE);

        bean.setPrecedentDegreeInformation(info);
    }

    @Override
    protected String getCandidacyInformationLinkDefaultLanguage() {
        String message = getStringFromDefaultBundle("link.candidacy.information.default.degreeTransfer");
        return MessageFormat.format(message, Installation.getInstance().getInstituitionURL());
    }

    @Override
    protected String getCandidacyInformationLinkEnglish() {
        String message = getStringFromDefaultBundle("link.candidacy.information.english.degreeTransfer");
        return MessageFormat.format(message, Installation.getInstance().getInstituitionURL());
    }

}
