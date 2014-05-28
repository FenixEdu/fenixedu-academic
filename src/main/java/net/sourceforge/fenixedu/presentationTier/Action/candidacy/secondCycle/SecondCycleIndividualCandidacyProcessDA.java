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
package net.sourceforge.fenixedu.presentationTier.Action.candidacy.secondCycle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.PrecedentDegreeInformationBeanFactory;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyResultBean;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.IndividualCandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.commons.FenixActionForward;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/caseHandlingSecondCycleIndividualCandidacyProcess", module = "academicAdministration",
        formBeanClass = FenixActionForm.class, functionality = SecondCycleCandidacyProcessDA.class)
@Forwards({
        @Forward(name = "intro",
                path = "/academicAdministration/caseHandlingSecondCycleCandidacyProcess.do?method=listProcessAllowedActivities"),
        @Forward(name = "list-allowed-activities", path = "/candidacy/secondCycle/listIndividualCandidacyActivities.jsp"),
        @Forward(name = "prepare-create-new-process", path = "/candidacy/selectPersonForCandidacy.jsp"),
        @Forward(name = "fill-personal-information", path = "/candidacy/fillPersonalInformation.jsp"),
        @Forward(name = "fill-candidacy-information", path = "/candidacy/secondCycle/fillCandidacyInformation.jsp"),
        @Forward(name = "change-state", path = "/candidacy/secondCycle/changeState.jsp"),
        @Forward(name = "prepare-candidacy-payment", path = "/candidacy/candidacyPayment.jsp"),
        @Forward(name = "edit-candidacy-personal-information", path = "/candidacy/editPersonalInformation.jsp"),
        @Forward(name = "edit-candidacy-information", path = "/candidacy/secondCycle/editCandidacyInformation.jsp"),
        @Forward(name = "introduce-candidacy-result", path = "/candidacy/secondCycle/introduceCandidacyResult.jsp"),
        @Forward(name = "cancel-candidacy", path = "/candidacy/cancelCandidacy.jsp"),
        @Forward(name = "create-registration", path = "/candidacy/secondCycle/createRegistration.jsp"),
        @Forward(name = "prepare-edit-candidacy-documents", path = "/candidacy/editCandidacyDocuments.jsp"),
        @Forward(name = "select-person-for-bind-with-candidacy", path = "/candidacy/selectPersonForBind.jsp"),
        @Forward(name = "edit-personal-information-for-bind", path = "/candidacy/editPersonalInformationForCandidacyBind.jsp"),
        @Forward(name = "change-process-checked-state", path = "/candidacy/changeProcessCheckedState.jsp"),
        @Forward(name = "change-payment-checked-state", path = "/candidacy/changePaymentCheckedState.jsp"),
        @Forward(name = "reject-candidacy", path = "/candidacy/rejectCandidacy.jsp"),
        @Forward(name = "choose-degree-for-registration-creation", path = "/candidacy/chooseDegreeForRegistrationCreation.jsp"),
        @Forward(name = "upload-photo", path = "/candidacy/secondCycle/uploadPhoto.jsp"),
        @Forward(name = "select-destination-period-to-copy", path = "/candidacy/secondCycle/selectDestinationPeriodToCopy.jsp"),
        @Forward(name = "set-not-accepted-state", path = "/candidacy/secondCycle/setNotAcceptedState.jsp") })
public class SecondCycleIndividualCandidacyProcessDA extends IndividualCandidacyProcessDA {

    private static final Logger logger = LoggerFactory.getLogger(SecondCycleIndividualCandidacyProcessDA.class);

    @Override
    protected Class<? extends Process> getParentProcessType() {
        return SecondCycleCandidacyProcess.class;
    }

    @Override
    protected Class getProcessType() {
        return SecondCycleIndividualCandidacyProcess.class;
    }

    @Override
    protected SecondCycleCandidacyProcess getParentProcess(HttpServletRequest request) {
        return (SecondCycleCandidacyProcess) super.getParentProcess(request);
    }

    @Override
    protected SecondCycleIndividualCandidacyProcess getProcess(HttpServletRequest request) {
        return (SecondCycleIndividualCandidacyProcess) super.getProcess(request);
    }

    @Override
    public SecondCycleIndividualCandidacyProcessBean getIndividualCandidacyProcessBean() {
        return (SecondCycleIndividualCandidacyProcessBean) super.getIndividualCandidacyProcessBean();
    }

    @Override
    public ActionForward listProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("processId", getParentProcess(request).getExternalId());
        return mapping.findForward("intro");
    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final SecondCycleIndividualCandidacyProcessBean bean = new SecondCycleIndividualCandidacyProcessBean();
        bean.setCandidacyProcess(getParentProcess(request));

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
        bean.setPrecedentDegreeInformation(new PrecedentDegreeInformationBean());

        /*
         * 06/05/2009 - Also we mark the bean as an external candidacy.
         */
        bean.setInternalPersonCandidacy(Boolean.TRUE);
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
    }

    @Override
    protected void createCandidacyPrecedentDegreeInformation(
            final IndividualCandidacyProcessWithPrecedentDegreeInformationBean bean,
            final StudentCurricularPlan studentCurricularPlan) {
        if (!studentCurricularPlan.isBolonhaDegree()) {
            bean.setPrecedentDegreeInformation(PrecedentDegreeInformationBeanFactory.createBean(studentCurricularPlan));
        } else {
            bean.setPrecedentDegreeInformation(PrecedentDegreeInformationBeanFactory.createBean(studentCurricularPlan,
                    CycleType.FIRST_CYCLE));
        }
    }

    public ActionForward prepareExecuteEditCandidacyPersonalInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final SecondCycleIndividualCandidacyProcessBean bean = new SecondCycleIndividualCandidacyProcessBean();
        bean.setPersonBean(new PersonBean(getProcess(request).getPersonalDetails()));
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        return mapping.findForward("edit-candidacy-personal-information");
    }

    public ActionForward prepareExecuteChangeIndividualCandidacyState(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("secondCycleIndividualCandidacyResultBean", new SecondCycleIndividualCandidacyResultBean(
                getProcess(request)));
        return mapping.findForward("change-state");
    }

    public ActionForward executeChangeIndividualCandidacyState(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {

        try {
            executeActivity(getProcess(request), "ChangeIndividualCandidacyState", getCandidacyResultBean());
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("secondCycleIndividualCandidacyResultBean", getCandidacyResultBean());
            return mapping.findForward("change-state");
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
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
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecuteEditCandidacyInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        SecondCycleIndividualCandidacyProcess process = getProcess(request);
        SecondCycleIndividualCandidacyProcessBean bean = new SecondCycleIndividualCandidacyProcessBean(process);
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
            executeActivity(getProcess(request), "EditCandidacyInformation", getIndividualCandidacyProcessBean());
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("edit-candidacy-information");
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecuteIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        SecondCycleIndividualCandidacyResultBean bean = getRenderedObject();
        RenderUtils.invalidateViewState();
        if (bean == null || bean.getDegree() == null) {
            request.setAttribute("secondCycleIndividualCandidacyResultBean", new SecondCycleIndividualCandidacyResultBean(
                    getProcess(request)));
        } else {
            request.setAttribute("secondCycleIndividualCandidacyResultBean", new SecondCycleIndividualCandidacyResultBean(
                    getProcess(request), bean.getDegree()));
        }
        return mapping.findForward("introduce-candidacy-result");
    }

    public ActionForward executeIntroduceCandidacyResultInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("secondCycleIndividualCandidacyResultBean", getCandidacyResultBean());
        return mapping.findForward("introduce-candidacy-result");
    }

    public ActionForward executeIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {

        try {
            executeActivity(getProcess(request), "IntroduceCandidacyResult", getCandidacyResultBean());
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("secondCycleIndividualCandidacyResultBean", getCandidacyResultBean());
            return mapping.findForward("introduce-candidacy-result");
        }

        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    private SecondCycleIndividualCandidacyResultBean getCandidacyResultBean() {
        return getRenderedObject("secondCycleIndividualCandidacyResultBean");
    }

    public ActionForward prepareExecuteCreateRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), new SecondCycleIndividualCandidacyProcessBean(
                getProcess(request)));

        return mapping.findForward("choose-degree-for-registration-creation");
    }

    public ActionForward prepareExecuteCreateRegistrationInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

        return mapping.findForward("choose-degree-for-registration-creation");
    }

    public ActionForward continueExecuteCreateRegistration(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

        return mapping.findForward("create-registration");
    }

    public ActionForward executeCreateRegistration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            executeActivity(getProcess(request), "CreateRegistration", getIndividualCandidacyProcessBean());
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("create-registration");
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    @Override
    /*
     * * Prepare the beans to choose a person or create a new one
     */
    protected void prepareInformationForBindPersonToCandidacyOperation(HttpServletRequest request,
            IndividualCandidacyProcess process) {
        final SecondCycleIndividualCandidacyProcessBean bean =
                new SecondCycleIndividualCandidacyProcessBean((SecondCycleIndividualCandidacyProcess) process);
        bean.setCandidacyProcess(getParentProcess(request));

        bean.setChoosePersonBean(new ChoosePersonBean(process.getCandidacy().getPersonalDetails()));
        bean.setPersonBean(new PersonBean(process.getCandidacy().getPersonalDetails()));

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
    }

    @Override
    public ActionForward createNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        SecondCycleIndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();

        boolean isValid = hasInvalidViewState();
        if (!isValid) {
            invalidateDocumentFileRelatedViewStates();
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("fill-candidacy-information");
        }

        boolean hasSelectedDegrees = !bean.getSelectedDegreeList().isEmpty();
        if (!hasSelectedDegrees) {
            invalidateDocumentFileRelatedViewStates();
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            addActionMessage(request, "error.SecondCycleIndividualCandidacyProcessBean.must.select.at.least.one.degree");
            return mapping.findForward("fill-candidacy-information");
        }

        return super.createNewProcess(mapping, form, request, response);
    }

    public ActionForward prepareExecuteChangeProcessCheckedState(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), new SecondCycleIndividualCandidacyProcessBean(
                getProcess(request)));

        return mapping.findForward("change-process-checked-state");
    }

    public ActionForward prepareExecuteChangePaymentCheckedState(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), new SecondCycleIndividualCandidacyProcessBean(
                getProcess(request)));

        return mapping.findForward("change-payment-checked-state");
    }

    public ActionForward addSelectedDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        SecondCycleIndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();

        bean.addSelectedDegree(bean.getSelectedDegree());
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        if (getFromRequest(request, "userAction").equals("createCandidacy")) {
            return mapping.findForward("fill-candidacy-information");
        } else if (getFromRequest(request, "userAction").equals("editCandidacyQualifications")) {
            return mapping.findForward("edit-candidacy-information");
        }

        return null;
    }

    public ActionForward removeSelectedDegree(ActionMapping mapping, ActionForm ActionForm, HttpServletRequest request,
            HttpServletResponse response) {
        SecondCycleIndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        Degree selectedDegree = getDomainObject(request, "removeDegreeExternalId");

        bean.removeSelectedDegree(selectedDegree);
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        if (getFromRequest(request, "userAction").equals("createCandidacy")) {
            return mapping.findForward("fill-candidacy-information");
        } else if (getFromRequest(request, "userAction").equals("editCandidacyQualifications")) {
            return mapping.findForward("edit-candidacy-information");
        }

        return null;
    }

    public ActionForward prepareExecuteCopyIndividualCandidacyToNextCandidacyProcess(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), new SecondCycleIndividualCandidacyProcessBean(
                getProcess(request)));

        return mapping.findForward("select-destination-period-to-copy");
    }

    public ActionForward executeCopyIndividualCandidacyToNextCandidacyProcess(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws FenixServiceException {
        SecondCycleIndividualCandidacyProcessBean individualCandidacyProcessBean = getIndividualCandidacyProcessBean();

        try {
            SecondCycleIndividualCandidacyProcess newProcess =
                    (SecondCycleIndividualCandidacyProcess) executeActivity(getProcess(request),
                            "CopyIndividualCandidacyToNextCandidacyProcess", individualCandidacyProcessBean);
            return new FenixActionForward(request, new ActionForward(
                    "/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=listProcessAllowedActivities&processId="
                            + newProcess.getExternalId()));
        } catch (final DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            logger.error(e.getMessage(), e);
            return mapping.findForward("select-destination-period-to-copy");
        }

    }

    public ActionForward executeCopyIndividualCandidacyToNextCandidacyProcessInvalid(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) {
        SecondCycleIndividualCandidacyProcessBean individualCandidacyProcessBean = getIndividualCandidacyProcessBean();

        request.setAttribute(getIndividualCandidacyProcessBeanName(), individualCandidacyProcessBean);

        return mapping.findForward("select-destination-period-to-copy");
    }

    public static class SelectedDegreesForRegistrationCreationProvider extends AbstractDomainObjectProvider {

        @Override
        public Object provide(Object source, Object currentValue) {
            return ((SecondCycleIndividualCandidacyProcessBean) source).getSelectedDegreeList();
        }

    }

    public ActionForward prepareExecuteSetNotAcceptedState(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), new SecondCycleIndividualCandidacyProcessBean(
                getProcess(request)));

        return mapping.findForward("set-not-accepted-state");
    }

    public ActionForward executeSetNotAcceptedState(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws FenixServiceException {
        SecondCycleIndividualCandidacyProcessBean individualCandidacyProcessBean = getIndividualCandidacyProcessBean();

        try {
            SecondCycleIndividualCandidacyProcess newProcess =
                    (SecondCycleIndividualCandidacyProcess) executeActivity(getProcess(request), "SetNotAcceptedState",
                            individualCandidacyProcessBean);
            return new FenixActionForward(request, new ActionForward(
                    "/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=listProcessAllowedActivities&processId="
                            + newProcess.getExternalId()));
        } catch (final DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            logger.error(e.getMessage(), e);
            return mapping.findForward("set-not-accepted-state");
        }
    }

}
