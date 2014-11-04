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
package org.fenixedu.academic.ui.struts.action.candidacy.degreeChange;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import org.fenixedu.academic.domain.candidacyProcess.PrecedentDegreeInformationBeanFactory;
import org.fenixedu.academic.domain.candidacyProcess.degreeChange.DegreeChangeCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.degreeChange.DegreeChangeIndividualCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.degreeChange.DegreeChangeIndividualCandidacyProcessBean;
import org.fenixedu.academic.domain.candidacyProcess.degreeChange.DegreeChangeIndividualCandidacyResultBean;
import org.fenixedu.academic.domain.caseHandling.Process;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.candidacy.PrecedentDegreeInformationBean;
import org.fenixedu.academic.dto.person.ChoosePersonBean;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.FenixActionForm;
import org.fenixedu.academic.ui.struts.action.candidacy.IndividualCandidacyProcessDA;
import org.fenixedu.bennu.core.domain.exceptions.BennuCoreDomainException;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/caseHandlingDegreeChangeIndividualCandidacyProcess", module = "academicAdministration",
        formBeanClass = FenixActionForm.class, functionality = DegreeChangeCandidacyProcessDA.class)
@Forwards({
        @Forward(name = "intro",
                path = "/academicAdministration/caseHandlingDegreeChangeCandidacyProcess.do?method=listProcessAllowedActivities"),
        @Forward(name = "list-allowed-activities", path = "/candidacy/degreeChange/listIndividualCandidacyActivities.jsp"),
        @Forward(name = "prepare-create-new-process", path = "/candidacy/selectPersonForCandidacy.jsp"),
        @Forward(name = "fill-personal-information", path = "/candidacy/fillPersonalInformation.jsp"),
        @Forward(name = "fill-candidacy-information", path = "/candidacy/degreeChange/fillCandidacyInformation.jsp"),
        @Forward(name = "prepare-candidacy-payment", path = "/candidacy/candidacyPayment.jsp"),
        @Forward(name = "change-state", path = "/candidacy/degreeChange/changeState.jsp"),
        @Forward(name = "edit-candidacy-personal-information", path = "/candidacy/editPersonalInformation.jsp"),
        @Forward(name = "edit-candidacy-information", path = "/candidacy/degreeChange/editCandidacyInformation.jsp"),
        @Forward(name = "edit-candidacy-curricularCourses-information",
                path = "/candidacy/degreeChange/editCandidacyCurricularCoursesInformation.jsp"),
        @Forward(name = "introduce-candidacy-result", path = "/candidacy/degreeChange/introduceCandidacyResult.jsp"),
        @Forward(name = "cancel-candidacy", path = "/candidacy/cancelCandidacy.jsp"),
        @Forward(name = "create-registration", path = "/candidacy/createRegistration.jsp"),
        @Forward(name = "prepare-edit-candidacy-documents", path = "/candidacy/editCandidacyDocuments.jsp"),
        @Forward(name = "change-process-checked-state", path = "/candidacy/changeProcessCheckedState.jsp"),
        @Forward(name = "change-payment-checked-state", path = "/candidacy/changePaymentCheckedState.jsp"),
        @Forward(name = "reject-candidacy", path = "/candidacy/rejectCandidacy.jsp") })
public class DegreeChangeIndividualCandidacyProcessDA extends IndividualCandidacyProcessDA {

    @Override
    protected Class<? extends Process> getParentProcessType() {
        return DegreeChangeCandidacyProcess.class;
    }

    @Override
    protected Class getProcessType() {
        return DegreeChangeIndividualCandidacyProcess.class;
    }

    @Override
    protected DegreeChangeCandidacyProcess getParentProcess(HttpServletRequest request) {
        return (DegreeChangeCandidacyProcess) super.getParentProcess(request);
    }

    @Override
    protected DegreeChangeIndividualCandidacyProcess getProcess(HttpServletRequest request) {
        return (DegreeChangeIndividualCandidacyProcess) super.getProcess(request);
    }

    @Override
    protected DegreeChangeIndividualCandidacyProcessBean getIndividualCandidacyProcessBean() {
        return (DegreeChangeIndividualCandidacyProcessBean) super.getIndividualCandidacyProcessBean();
    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final DegreeChangeIndividualCandidacyProcessBean bean = new DegreeChangeIndividualCandidacyProcessBean();
        bean.setCandidacyProcess(getParentProcess(request));
        bean.setChoosePersonBean(new ChoosePersonBean());

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
    }

    @Override
    protected void createCandidacyPrecedentDegreeInformation(IndividualCandidacyProcessWithPrecedentDegreeInformationBean bean,
            StudentCurricularPlan studentCurricularPlan) {
        final PrecedentDegreeInformationBean info =
                PrecedentDegreeInformationBeanFactory.createBean(studentCurricularPlan, CycleType.FIRST_CYCLE);

        bean.setPrecedentDegreeInformation(info);
    }

    public ActionForward prepareExecuteEditCandidacyPersonalInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final DegreeChangeIndividualCandidacyProcessBean bean = new DegreeChangeIndividualCandidacyProcessBean();
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
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        try {
            executeActivity(getProcess(request), "EditCandidacyPersonalInformation", getIndividualCandidacyProcessBean());
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("edit-candidacy-personal-information");
        } catch (final BennuCoreDomainException be) {
            addActionMessage(request, be.getLocalizedMessage(), false);
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("edit-candidacy-personal-information");
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecuteEditCandidacyInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        DegreeChangeIndividualCandidacyProcessBean bean = new DegreeChangeIndividualCandidacyProcessBean(getProcess(request));
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward executeEditCandidacyInformationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward executeEditCandidacyInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        try {
            DegreeChangeIndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();

            executeActivity(getProcess(request), "EditCandidacyInformation", getIndividualCandidacyProcessBean());
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("edit-candidacy-information");
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecuteEditCandidacyCurricularCoursesInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), new DegreeChangeIndividualCandidacyProcessBean(
                getProcess(request)));
        return mapping.findForward("edit-candidacy-curricularCourses-information");
    }

    public ActionForward executeEditCandidacyCurricularCoursesInformationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("edit-candidacy-curricularCourses-information");
    }

    public ActionForward executeEditCandidacyCurricularCoursesInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        try {
            executeActivity(getProcess(request), "EditCandidacyCurricularCoursesInformation", getIndividualCandidacyProcessBean());
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("edit-candidacy-curricularCourses-information");
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecuteIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        DegreeChangeIndividualCandidacyResultBean bean = getRenderedObject();
        RenderUtils.invalidateViewState();
        if (bean == null || bean.getDegree() == null) {
            request.setAttribute("individualCandidacyResultBean", new DegreeChangeIndividualCandidacyResultBean(
                    getProcess(request)));
        } else {
            request.setAttribute("individualCandidacyResultBean", new DegreeChangeIndividualCandidacyResultBean(
                    getProcess(request), bean.getDegree()));
        }

        return mapping.findForward("introduce-candidacy-result");
    }

    public ActionForward prepareExecuteChangeIndividualCandidacyState(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("individualCandidacyResultBean", new DegreeChangeIndividualCandidacyResultBean(getProcess(request)));
        return mapping.findForward("change-state");
    }

    public ActionForward executeChangeIndividualCandidacyState(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {

        try {
            executeActivity(getProcess(request), "ChangeIndividualCandidacyState", getCandidacyResultBean());
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("individualCandidacyResultBean", getCandidacyResultBean());
            return mapping.findForward("change-state");
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward executeIntroduceCandidacyResultInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("individualCandidacyResultBean", getCandidacyResultBean());
        return mapping.findForward("introduce-candidacy-result");
    }

    public ActionForward executeIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {

        try {
            executeActivity(getProcess(request), "IntroduceCandidacyResult", getCandidacyResultBean());
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("individualCandidacyResultBean", getCandidacyResultBean());
            return mapping.findForward("introduce-candidacy-result");
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    private DegreeChangeIndividualCandidacyResultBean getCandidacyResultBean() {
        return getRenderedObject("individualCandidacyResultBean");
    }

    public ActionForward prepareExecuteCreateRegistration(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        request.setAttribute("degree", getProcess(request).getCandidacySelectedDegree());
        return mapping.findForward("create-registration");
    }

    public ActionForward executeCreateRegistration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            executeActivity(getProcess(request), "CreateRegistration");
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("degree", getProcess(request).getCandidacySelectedDegree());
            return mapping.findForward("create-registration");
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    @Override
    protected void prepareInformationForBindPersonToCandidacyOperation(HttpServletRequest request,
            IndividualCandidacyProcess process) {
        // TODO Auto-generated method stub

    }

    @Override
    public ActionForward createNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        boolean isValid = hasInvalidViewState();
        if (!isValid) {
            invalidateDocumentFileRelatedViewStates();
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("fill-candidacy-information");
        }

        return super.createNewProcess(mapping, form, request, response);
    }

    public ActionForward prepareExecuteChangeProcessCheckedState(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), new DegreeChangeIndividualCandidacyProcessBean(
                getProcess(request)));

        return mapping.findForward("change-process-checked-state");
    }

    public ActionForward prepareExecuteChangePaymentCheckedState(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), new DegreeChangeIndividualCandidacyProcessBean(
                getProcess(request)));

        return mapping.findForward("change-payment-checked-state");
    }

}
