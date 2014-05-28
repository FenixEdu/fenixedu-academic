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
package net.sourceforge.fenixedu.presentationTier.Action.candidacy.over23;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacyResultBean;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.IndividualCandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/caseHandlingOver23IndividualCandidacyProcess", module = "academicAdministration",
        formBeanClass = Over23IndividualCandidacyProcessDA.CandidacyForm.class, functionality = Over23CandidacyProcessDA.class)
@Forwards({
        @Forward(name = "intro",
                path = "/academicAdministration/caseHandlingOver23CandidacyProcess.do?method=listProcessAllowedActivities"),
        @Forward(name = "list-allowed-activities", path = "/candidacy/over23/listIndividualCandidacyActivities.jsp"),
        @Forward(name = "prepare-create-new-process", path = "/candidacy/selectPersonForCandidacy.jsp"),
        @Forward(name = "fill-personal-information", path = "/candidacy/fillPersonalInformation.jsp"),
        @Forward(name = "fill-candidacy-information", path = "/candidacy/over23/fillCandidacyInformation.jsp"),
        @Forward(name = "prepare-candidacy-payment", path = "/candidacy/candidacyPayment.jsp"),
        @Forward(name = "edit-candidacy-personal-information", path = "/candidacy/editPersonalInformation.jsp"),
        @Forward(name = "edit-candidacy-information", path = "/candidacy/over23/editCandidacyInformation.jsp"),
        @Forward(name = "introduce-candidacy-result", path = "/candidacy/over23/introduceCandidacyResult.jsp"),
        @Forward(name = "cancel-candidacy", path = "/candidacy/cancelCandidacy.jsp"),
        @Forward(name = "create-registration", path = "/candidacy/createRegistration.jsp"),
        @Forward(name = "prepare-edit-candidacy-documents", path = "/candidacy/editCandidacyDocuments.jsp"),
        @Forward(name = "select-person-for-bind-with-candidacy", path = "/candidacy/selectPersonForBind.jsp"),
        @Forward(name = "edit-personal-information-for-bind", path = "/candidacy/editPersonalInformationForCandidacyBind.jsp"),
        @Forward(name = "change-process-checked-state", path = "/candidacy/changeProcessCheckedState.jsp"),
        @Forward(name = "change-payment-checked-state", path = "/candidacy/changePaymentCheckedState.jsp")

})
public class Over23IndividualCandidacyProcessDA extends IndividualCandidacyProcessDA {

    @Override
    protected Class<? extends Process> getParentProcessType() {
        return Over23CandidacyProcess.class;
    }

    @Override
    protected Class getProcessType() {
        return Over23IndividualCandidacyProcess.class;
    }

    @Override
    protected Over23CandidacyProcess getParentProcess(HttpServletRequest request) {
        return (Over23CandidacyProcess) super.getParentProcess(request);
    }

    @Override
    protected Over23IndividualCandidacyProcess getProcess(HttpServletRequest request) {
        return (Over23IndividualCandidacyProcess) super.getProcess(request);
    }

    @Override
    protected Over23IndividualCandidacyProcessBean getIndividualCandidacyProcessBean() {
        return (Over23IndividualCandidacyProcessBean) super.getIndividualCandidacyProcessBean();
    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final IndividualCandidacyProcessBean bean = new Over23IndividualCandidacyProcessBean();
        bean.setCandidacyProcess(getParentProcess(request));
        bean.setChoosePersonBean(new ChoosePersonBean());

        /*
         * 06/05/2009 - Due to Public Candidacies, a candidacy created in admin
         * office is external So we dont require ChoosePersonBean because a
         * Person will not be associated or created at individual candidacy
         * creation stage. Instead we bind with an empty PersonBean.
         * 
         * bean.setChoosePersonBean(new ChoosePersonBean());
         */
        bean.setPersonBean(new PersonBean());

        /*
         * 06/05/2009 - Also we mark the bean as an external candidacy.
         */
        bean.setInternalPersonCandidacy(Boolean.FALSE);
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
    }

    public ActionForward addDegreeToCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return addDegreeToCandidacy(mapping, actionForm, request, response, "fill-candidacy-information");
    }

    private ActionForward addDegreeToCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, String forward) {

        final Over23IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        if (bean.hasDegreeToAdd() && !bean.containsDegree(bean.getDegreeToAdd())) {
            bean.addDegree(bean.getDegreeToAdd());
            bean.setDegreeToAdd(null);
            RenderUtils.invalidateViewState();
        }
        return mapping.findForward(forward);
    }

    public ActionForward removeDegreeFromCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return removeDegreeFromCandidacy(mapping, actionForm, request, response, "fill-candidacy-information");
    }

    private ActionForward removeDegreeFromCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, String forward) {

        final Over23IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        final String degreeId = ((CandidacyForm) actionForm).getDegreeToDelete();
        if (degreeId != null) {
            final Degree degree = getDegree(degreeId);
            if (bean.containsDegree(degree)) {
                bean.removeDegree(degree);
            }
        }

        return mapping.findForward(forward);
    }

    private Degree getDegree(final String degreeId) {
        return FenixFramework.getDomainObject(degreeId);
    }

    public ActionForward prepareExecuteEditCandidacyPersonalInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final IndividualCandidacyProcessBean bean = new Over23IndividualCandidacyProcessBean();
        bean.setPersonBean(new PersonBean(getProcess(request).getPersonalDetails()));
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        return mapping.findForward("edit-candidacy-personal-information");
    }

    public ActionForward executeEditCandidacyPersonalInformationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("edit-candidacy-personal-information");
    }

    public ActionForward executeEditCandidacyPersonalInformation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        try {
            executeActivity(getProcess(request), "EditCandidacyPersonalInformation", getIndividualCandidacyProcessBean());
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("edit-candidacy-personal-information");
        }
        return listProcessAllowedActivities(mapping, form, request, response);
    }

    public ActionForward prepareExecuteEditCandidacyInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), new Over23IndividualCandidacyProcessBean(
                getProcess(request)));
        return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward executeEditCandidacyInformationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward addDegreeToCandidacyWhenEditing(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        return addDegreeToCandidacy(mapping, actionForm, request, response, "edit-candidacy-information");
    }

    public ActionForward removeDegreeFromCandidacyWhenEditing(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        return removeDegreeFromCandidacy(mapping, actionForm, request, response, "edit-candidacy-information");
    }

    public ActionForward prepareExecuteChangePaymentCheckedState(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), new Over23IndividualCandidacyProcessBean(
                getProcess(request)));

        return mapping.findForward("change-payment-checked-state");
    }

    public ActionForward executeEditCandidacyInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            boolean isValid =
                    validateOver23IndividualCandidacy(request, getIndividualCandidacyProcessBean()) && hasInvalidViewState();
            if (!isValid) {
                request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
                return mapping.findForward("fill-candidacy-information");
            }

            executeActivity(getProcess(request), "EditCandidacyInformation", getIndividualCandidacyProcessBean());
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("edit-candidacy-information");
        }
        return listProcessAllowedActivities(mapping, form, request, response);
    }

    public ActionForward prepareExecuteIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("over23IndividualCandidacyResultBean", new Over23IndividualCandidacyResultBean(getProcess(request)));
        return mapping.findForward("introduce-candidacy-result");
    }

    public ActionForward executeIntroduceCandidacyResultInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("over23IndividualCandidacyResultBean", getCandidacyResultBean());
        return mapping.findForward("introduce-candidacy-result");
    }

    public ActionForward executeIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        try {
            executeActivity(getProcess(request), "IntroduceCandidacyResult", getCandidacyResultBean());
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            return mapping.findForward("introduce-candidacy-result");
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    private Over23IndividualCandidacyResultBean getCandidacyResultBean() {
        return getRenderedObject("over23IndividualCandidacyResultBean");
    }

    public ActionForward prepareExecuteCreateRegistration(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final Over23IndividualCandidacyProcess process = getProcess(request);
        request.setAttribute("degree", process.getAcceptedDegree());
        return mapping.findForward("create-registration");
    }

    public ActionForward executeCreateRegistration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            executeActivity(getProcess(request), "CreateRegistration");
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("degree", getProcess(request).getAcceptedDegree());
            return mapping.findForward("create-registration");
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward addConcludedHabilitationsEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        bean.addConcludedFormationBean();

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        return forwardTo(mapping, request);
    }

    public ActionForward addNonConcludedHabilitationsEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        bean.addNonConcludedFormationBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        return forwardTo(mapping, request);
    }

    @Override
    public ActionForward removeConcludedHabilitationsEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        Integer index = getIntegerFromRequest(request, "removeIndex");
        bean.removeFormationConcludedBean(index);
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        return forwardTo(mapping, request);
    }

    public ActionForward removeNonConcludedHabilitationsEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse reponse) {
        IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        Integer index = getIntegerFromRequest(request, "removeIndex");
        bean.removeFormationNonConcludedBean(index);
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        return forwardTo(mapping, request);
    }

    private ActionForward forwardTo(ActionMapping mapping, HttpServletRequest request) {
        if (getFromRequest(request, "userAction").equals("createCandidacy")) {
            return mapping.findForward("fill-candidacy-information");
        } else if (getFromRequest(request, "userAction").equals("editCandidacy")) {
            return mapping.findForward("edit-candidacy-information");
        }

        return null;
    }

    @Override
    public ActionForward createNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        Over23IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();

        boolean isValid = validateOver23IndividualCandidacy(request, bean) && hasInvalidViewState();
        if (!isValid) {
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("fill-candidacy-information");
        }

        return super.createNewProcess(mapping, form, request, response);
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

    static public class CandidacyForm extends FenixActionForm {
        private String degreeToDelete;

        public String getDegreeToDelete() {
            return degreeToDelete;
        }

        public void setDegreeToDelete(String degreeToDelete) {
            this.degreeToDelete = degreeToDelete;
        }
    }

    @Override
    /*
     * * Prepare the beans to choose a person or create a new one
     */
    protected void prepareInformationForBindPersonToCandidacyOperation(HttpServletRequest request,
            IndividualCandidacyProcess process) {
        final IndividualCandidacyProcessBean bean =
                new Over23IndividualCandidacyProcessBean((Over23IndividualCandidacyProcess) process);
        bean.setCandidacyProcess(getParentProcess(request));

        bean.setChoosePersonBean(new ChoosePersonBean(process.getCandidacy().getPersonalDetails()));
        bean.setPersonBean(new PersonBean(process.getCandidacy().getPersonalDetails()));

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
    }

    public ActionForward prepareExecuteChangeProcessCheckedState(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), new Over23IndividualCandidacyProcessBean(
                getProcess(request)));

        return mapping.findForward("change-process-checked-state");
    }

}