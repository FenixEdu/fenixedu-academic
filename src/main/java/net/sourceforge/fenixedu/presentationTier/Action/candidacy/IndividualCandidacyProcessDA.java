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
package net.sourceforge.fenixedu.presentationTier.Action.candidacy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.CreateNewProcess;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCodeOperations;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.PrecedentDegreeInformationBeanFactory;
import net.sourceforge.fenixedu.domain.candidacyProcess.exceptions.HashCodeForEmailAndProcessAlreadyBounded;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.presentationTier.Action.casehandling.CaseHandlingDispatchAction;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.components.state.LifeCycleConstants;
import pt.ist.fenixWebFramework.renderers.plugin.RenderersRequestProcessorImpl;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

/**
 * INFO: when extending this class pay attention to the following aspects
 * 
 * Common methods to all candidacies: SearchPersonForCandidacy (used to
 * select/create person for candidacy), FillCandidacyInformation (forward to
 * candidacy page), CreateNewProcess (used to create a new process),
 * CancelCandidacy (must exist an activity with this name in
 * IndividualCandidacyProcess)
 * 
 * <p>
 * Common methods to candidacies with precedent degree information: FillPrecedentInformationPostback (used to select between
 * internal and external information), FillPrecedentInformationStudentCurricularPlanPostback (used to set choosed precedent
 * studentCurricularPlan)
 * 
 * <p>
 * Must configure the following forwards: intro (common value: /candidacy/mainCandidacyProcess.jsp), list-allowed-activities
 * (common value: /candidacy/listIndividualCandidacyActivities.jsp), prepare-create-new-process (common value:
 * /candidacy/selectPersonForCandidacy.jsp), fill-personal-information (common value: /candidacy/fillPersonalInformation.jsp),
 * fill-candidacy-information (jsp used by each candidacy), cancel-candidacy (common value: /candidacy/cancelCandidacy.jsp)
 * 
 */

public abstract class IndividualCandidacyProcessDA extends CaseHandlingDispatchAction {

    private static final String SIBS_ENTITY_CODE = FenixConfigurationManager.getConfiguration().getSibsEntityCode();

    abstract protected Class<? extends Process> getParentProcessType();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        setParentProcess(request);
        request.setAttribute("sibsEntityCode", SIBS_ENTITY_CODE);

        return super.execute(mapping, actionForm, request, response);
    }

    protected void setParentProcess(HttpServletRequest request) {
        final String parentProcessId = (String) getFromRequest(request, "parentProcessId");
        if (parentProcessId != null) {
            request.setAttribute("parentProcess", FenixFramework.getDomainObject(parentProcessId));
        } else {
            setProcess(request);
            if (hasProcess(request)) {
                request.setAttribute("parentProcess", getProcess(request).getCandidacyProcess());
            }
        }
    }

    protected CandidacyProcess getParentProcess(final HttpServletRequest request) {
        return (CandidacyProcess) request.getAttribute("parentProcess");
    }

    protected boolean hasParentProcess(final HttpServletRequest request) {
        return getParentProcess(request) != null;
    }

    @Override
    public ActionForward listProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        setMainCandidacyProcessInformation(request, getParentProcess(request));
        return mapping.findForward("intro");
    }

    @Override
    protected IndividualCandidacyProcess getProcess(HttpServletRequest request) {
        return (IndividualCandidacyProcess) super.getProcess(request);
    }

    /**
     * Set context information used by intro page
     */
    protected void setMainCandidacyProcessInformation(final HttpServletRequest request, final CandidacyProcess process) {
        request.setAttribute("process", process);
        request.setAttribute("processName", getParentProcessType().getSimpleName());
        request.setAttribute("canCreateProcess", canCreateProcess(getParentProcessType().getName()));
        request.setAttribute("processActivities", process.getAllowedActivities(Authenticate.getUser()));
        request.setAttribute("canCreateChildProcess", canCreateProcess(getProcessType().getName()));
        request.setAttribute("childProcessName", getProcessType().getSimpleName());
        request.setAttribute("childProcesses", process.getChildProcesses());
        request.setAttribute("executionIntervalId", process.getCandidacyExecutionInterval().getExternalId());
        request.setAttribute("executionIntervals",
                ExecutionInterval.readExecutionIntervalsWithCandidacyPeriod(process.getCandidacyPeriod().getClass()));
    }

    protected List<Activity> getAllowedActivities(final IndividualCandidacyProcess process) {
        List<Activity> activities = process.getAllowedActivities(Authenticate.getUser());
        ArrayList<Activity> resultActivities = new ArrayList<Activity>();

        for (Activity activity : activities) {
            if (activity.isVisibleForAdminOffice()) {
                resultActivities.add(activity);
            }
        }

        return resultActivities;
    }

    /**
     * Represents the id of the bean used in candidacy pages
     */
    protected String getIndividualCandidacyProcessBeanName() {
        return "individualCandidacyProcessBean";
    }

    protected IndividualCandidacyProcessBean getIndividualCandidacyProcessBean() {
        return getRenderedObject(getIndividualCandidacyProcessBeanName());
    }

    @Override
    public ActionForward prepareCreateNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        if (!hasParentProcess(request)) {
            addActionMessage(request, "error.IndividualCandidacy.invalid.candidacyProcess");
            return listProcesses(mapping, form, request, response);
        } else {
            setStartInformation(form, request, response);

            /*
             * 06/05/2009 - Skip search Person and step to personal data form. A
             * person will not be created
             * 
             * return mapping.findForward("prepare-create-new-process");
             */
            return mapping.findForward("prepare-create-new-process");
            // return mapping.findForward("fill-personal-information");
        }
    }

    /**
     * Used by {@link #prepareCreateNewProcess(ActionMapping,ActionForm,HttpServletRequest,HttpServletResponse)} method to init
     * request with necessary information to start creating a new
     * process
     */
    abstract protected void setStartInformation(final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response);

    public ActionForward prepareCreateNewProcessInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("prepare-create-new-process");
    }

    public ActionForward searchPersonForCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        final ChoosePersonBean choosePersonBean = bean.getChoosePersonBean();

        if (!choosePersonBean.hasPerson()) {
            if (choosePersonBean.isFirstTimeSearch()) {
                final Collection<Person> persons = Person.findPersonByDocumentID(choosePersonBean.getIdentificationNumber());
                choosePersonBean.setFirstTimeSearch(false);
                if (showSimilarPersons(choosePersonBean, persons)) {
                    RenderUtils.invalidateViewState();
                    return mapping.findForward("prepare-create-new-process");
                }
            }
            bean.setPersonBean(new PersonBean(choosePersonBean.getName(), choosePersonBean.getIdentificationNumber(),
                    choosePersonBean.getDocumentType(), choosePersonBean.getDateOfBirth()));

            // bean.getPersonBean().setAddress("Some address");
            // bean.getPersonBean().setArea("Some area");
            // bean.getPersonBean().setAreaOfAreaCode("2345-2341");
            // //bean.getPersonBean().setCountryOfBirth(Country.readByTwoLetterCode("AF"));
            // bean.getPersonBean().setCountryOfResidence(Country.readByTwoLetterCode("PT"));
            // bean.getPersonBean().setDistrictOfBirth("Some district of birth");
            // bean.getPersonBean().setDistrictOfResidence("Some district of residence");
            // bean.getPersonBean().setDistrictSubdivisionOfBirth("district subdivision of birth");
            // bean.getPersonBean().setDistrictSubdivisionOfResidence("district subdivision of residence");
            // bean.getPersonBean().setGender(Gender.MALE);
            // bean.getPersonBean().setPhone("12313234132");
            // bean.getPersonBean().setNationality(Country.readByTwoLetterCode("AF"));
            // bean.getPersonBean().setAreaCode("1223-123");

            return mapping.findForward("fill-personal-information");

        } else {
            bean.setPersonBean(new PersonBean(bean.getChoosePersonBean().getPerson()));

            // bean.getPersonBean().setAddress("Some address");
            // bean.getPersonBean().setArea("Some area");
            // bean.getPersonBean().setAreaOfAreaCode("2345-2341");
            // bean.getPersonBean().setCountryOfBirth(Country.readByTwoLetterCode("AF"));
            // bean.getPersonBean().setCountryOfResidence(Country.readByTwoLetterCode("PT"));
            // bean.getPersonBean().setDistrictOfBirth("Some district of birth");
            // bean.getPersonBean().setDistrictOfResidence("Some district of residence");
            // bean.getPersonBean().setDistrictSubdivisionOfBirth("district subdivision of birth");
            // bean.getPersonBean().setDistrictSubdivisionOfResidence("district subdivision of residence");
            // bean.getPersonBean().setGender(Gender.MALE);
            // bean.getPersonBean().setPhone("12313234132");
            // bean.getPersonBean().setNationality(Country.readByTwoLetterCode("AF"));
            // bean.getPersonBean().setAreaCode("1223-123");

            return mapping.findForward("fill-personal-information");
        }
    }

    protected boolean showSimilarPersons(final ChoosePersonBean choosePersonBean, final Collection<Person> persons) {
        if (!persons.isEmpty()) {
            return true;
        }
        return !Person.findByDateOfBirth(choosePersonBean.getDateOfBirth(),
                Person.findInternalPersonMatchingFirstAndLastName(choosePersonBean.getName())).isEmpty();
    }

    public ActionForward searchAgainPersonForCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        bean.getChoosePersonBean().setFirstTimeSearch(true);
        bean.getChoosePersonBean().setPerson(null);
        RenderUtils.invalidateViewState();
        return mapping.findForward("prepare-create-new-process");
    }

    public ActionForward selectPersonForCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        if (!bean.hasChoosenPerson()) {
            addActionMessage(request, "error.candidacy.must.select.any.person");
            return mapping.findForward("prepare-create-new-process");
        }

        if (existsIndividualCandidacyProcessForDocumentId(request, bean.getChoosePersonBean().getPerson().getIdDocumentType(),
                bean.getChoosePersonBean().getPerson().getDocumentIdNumber())) {
            addActionMessage(request, "error.candidacy.already.exists.for.this.person");
            return mapping.findForward("prepare-create-new-process");
        }

        bean.setPersonBean(new PersonBean(bean.getChoosePersonBean().getPerson()));
        bean.setChoosePersonBean(null);
        return mapping.findForward("fill-personal-information");
    }

    public ActionForward fillPersonalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        final ChoosePersonBean choosePersonBean = bean.getChoosePersonBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        bean.setPersonBean(new PersonBean(choosePersonBean.getName(), choosePersonBean.getIdentificationNumber(),
                choosePersonBean.getDocumentType(), choosePersonBean.getDateOfBirth()));

        // bean.getPersonBean().setAddress("Some address");
        // bean.getPersonBean().setArea("Some area");
        // bean.getPersonBean().setAreaOfAreaCode("2345-2341");
        // bean.getPersonBean().setCountryOfBirth(Country.readByTwoLetterCode("AF"));
        // bean.getPersonBean().setCountryOfResidence(Country.readByTwoLetterCode("PT"));
        // bean.getPersonBean().setDistrictOfBirth("Some district of birth");
        // bean.getPersonBean().setDistrictOfResidence("Some district of residence");
        // bean.getPersonBean().setDistrictSubdivisionOfBirth("district subdivision of birth");
        // bean.getPersonBean().setDistrictSubdivisionOfResidence("district subdivision of residence");
        // bean.getPersonBean().setGender(Gender.MALE);
        // bean.getPersonBean().setPhone("12313234132");
        // bean.getPersonBean().setNationality(Country.readByTwoLetterCode("AF"));
        // bean.getPersonBean().setAreaCode("1223-123");

        bean.setChoosePersonBean(null);
        return mapping.findForward("fill-personal-information");
    }

    public ActionForward fillPersonalInformationInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("fill-personal-information");
    }

    public ActionForward fillCommonCandidacyInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        return mapping.findForward("fill-common-candidacy-information");
    }

    public ActionForward fillCommonCandidacyInformationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("fill-common-candidacy-information");
    }

    public ActionForward fillCandidacyInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        if (!StringUtils.isEmpty(bean.getPersonBean().getSocialSecurityNumber())) {
            Party existingSocialSecurityNumberParty =
                    Person.readByContributorNumber(bean.getPersonBean().getSocialSecurityNumber());

            if (existingSocialSecurityNumberParty != null
                    && existingSocialSecurityNumberParty != bean.getPersonBean().getPerson()) {
                // found person with same contributor number
                addActionMessage(request, "error.party.existing.contributor.number");
                return mapping.findForward("fill-personal-information");
            }
        }

        try {
            DegreeOfficePublicCandidacyHashCode candidacyHashCode =
                    DegreeOfficePublicCandidacyHashCodeOperations.getUnusedOrCreateNewHashCode(getProcessType(),
                            getParentProcess(request), bean.getPersonBean().getEmail());
            bean.setPublicCandidacyHashCode(candidacyHashCode);
        } catch (HashCodeForEmailAndProcessAlreadyBounded e) {
            addActionMessage(request, "error.candidacy.hash.code.already.bounded");
            return mapping.findForward("fill-personal-information");
        }

        return mapping.findForward("fill-candidacy-information");
    }

    public ActionForward fillCandidacyInformationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("fill-candidacy-information");
    }

    public ActionForward fillPrecedentInformationPostback(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        // assuming that when using this method each individual candidacy bean
        // extends IndividualCandidacyProcessWithPrecedentDegreeInformationBean
        final IndividualCandidacyProcessWithPrecedentDegreeInformationBean bean =
                (IndividualCandidacyProcessWithPrecedentDegreeInformationBean) getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        RenderUtils.invalidateViewState();

        if (bean.hasPrecedentDegreeType()) {
            if (bean.isExternalPrecedentDegreeType()) {
                bean.setPrecedentDegreeInformation(new PrecedentDegreeInformationBean());
            } else if (bean.hasPrecedentStudentCurricularPlan()) {
                createCandidacyPrecedentDegreeInformation(bean, bean.getPrecedentStudentCurricularPlan());
            } else {
                final List<StudentCurricularPlan> scps = bean.getPrecedentStudentCurricularPlans();
                if (scps.size() == 1) {
                    createCandidacyPrecedentDegreeInformation(bean, scps.iterator().next());
                    bean.setPrecedentStudentCurricularPlan(scps.iterator().next());
                }
            }
        }

        return mapping.findForward("fill-candidacy-information");
    }

    protected void createCandidacyPrecedentDegreeInformation(
            final IndividualCandidacyProcessWithPrecedentDegreeInformationBean bean,
            final StudentCurricularPlan studentCurricularPlan) {

        if (studentCurricularPlan.isBolonhaDegree()) {
            final CycleType cycleType;

            if (studentCurricularPlan.hasConcludedAnyInternalCycle()) {
                cycleType = studentCurricularPlan.getLastConcludedCycleCurriculumGroup().getCycleType();
            } else {
                cycleType = studentCurricularPlan.getLastOrderedCycleCurriculumGroup().getCycleType();
            }

            bean.setPrecedentDegreeInformation(PrecedentDegreeInformationBeanFactory.createBean(studentCurricularPlan, cycleType));
        } else {
            bean.setPrecedentDegreeInformation(PrecedentDegreeInformationBeanFactory.createBean(studentCurricularPlan));
        }
    }

    public ActionForward fillPrecedentInformationStudentCurricularPlanPostback(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        // assuming that when using this method each individual candidacy bean
        // extends IndividualCandidacyProcessWithPrecedentDegreeInformationBean
        final IndividualCandidacyProcessWithPrecedentDegreeInformationBean bean =
                (IndividualCandidacyProcessWithPrecedentDegreeInformationBean) getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

        if (bean.hasPrecedentStudentCurricularPlan()) {
            createCandidacyPrecedentDegreeInformation(bean, bean.getPrecedentStudentCurricularPlan());
        }

        RenderUtils.invalidateViewState();
        return mapping.findForward("fill-candidacy-information");
    }

    @Override
    public ActionForward createNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            request.setAttribute("process", CreateNewProcess.run(getProcessType().getName(), getIndividualCandidacyProcessBean()));
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("fill-candidacy-information");
        }
        return listProcessAllowedActivities(mapping, form, request, response);
    }

    public ActionForward prepareExecuteCandidacyPayment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        if (!getProcess(request).isCandidacyInternal()) {
            return prepareExecuteBindPersonToCandidacy(mapping, actionForm, request, response);
        }

        return mapping.findForward("prepare-candidacy-payment");
    }

    public ActionForward prepareExecuteCancelCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("cancel-candidacy");
    }

    public ActionForward executeCancelCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            executeActivity(getProcess(request), "CancelCandidacy", null);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            return mapping.findForward("cancel-candidacy");
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecuteEditDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final IndividualCandidacyProcess process = getProcess(request);
        request.setAttribute(getIndividualCandidacyProcessBeanName(), process);
        CandidacyProcessDocumentUploadBean uploadBean = new CandidacyProcessDocumentUploadBean();
        uploadBean.setIndividualCandidacyProcess(process);
        request.setAttribute("candidacyDocumentUploadBean", uploadBean);

        RenderUtils.invalidateViewState("individualCandidacyProcessBean.document");

        return mapping.findForward("prepare-edit-candidacy-documents");
    }

    public ActionForward uploadDocument(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, IOException {
        CandidacyProcessDocumentUploadBean uploadBean =
                (CandidacyProcessDocumentUploadBean) getObjectFromViewState("individualCandidacyProcessBean.document");
        try {
            IndividualCandidacyDocumentFile documentFile =
                    createIndividualCandidacyDocumentFile(uploadBean, uploadBean.getIndividualCandidacyProcess()
                            .getPersonalDetails().getDocumentIdNumber());
            uploadBean.setDocumentFile(documentFile);

            executeActivity(getProcess(request), "EditDocuments", uploadBean);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
        }

        return prepareExecuteEditDocuments(mapping, actionForm, request, response);

    }

    @Override
    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final IndividualCandidacyProcess process = getProcess(request);
        request.setAttribute("activities", getAllowedActivities(process));
        return mapping.findForward("list-allowed-activities");
    }

    protected boolean hasInvalidViewState() {
        List<IViewState> viewStates =
                (List<IViewState>) RenderersRequestProcessorImpl.getCurrentRequest().getAttribute(
                        LifeCycleConstants.VIEWSTATE_PARAM_NAME);
        boolean valid = true;
        if (viewStates != null) {
            for (IViewState state : viewStates) {
                valid &= state.isValid();
            }
        }
        return valid;
    }

    protected abstract void prepareInformationForBindPersonToCandidacyOperation(HttpServletRequest request,
            IndividualCandidacyProcess process);

    public ActionForward prepareExecuteBindPersonToCandidacy(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        IndividualCandidacyProcess individualCandidacyProcess = getProcess(request);
        prepareInformationForBindPersonToCandidacyOperation(request, individualCandidacyProcess);
        setProcess(request);

        return mapping.findForward("select-person-for-bind-with-candidacy");
    }

    public ActionForward prepareEditPersonalInformationForBindWithSelectedPerson(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

        if (bean.getChoosePersonBean().getPerson() == null) {
            addActionMessage(request, "error.candidacy.select.person.for.bind");
            return mapping.findForward("select-person-for-bind-with-candidacy");
        }

        return mapping.findForward("edit-personal-information-for-bind");
    }

    public ActionForward prepareEditPersonalInformationForBindWithNewPerson(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

        return mapping.findForward("edit-personal-information-for-bind");
    }

    public ActionForward executeEditCandidacyPersonalInformationForBindInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("edit-personal-information-for-bind");
    }

    public ActionForward bindPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            executeActivity(getProcess(request), "BindPersonToCandidacy", getIndividualCandidacyProcessBean());
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("edit-personal-information-for-bind");
        }

        return prepareExecuteCandidacyPayment(mapping, actionForm, request, response);
    }

    protected IndividualCandidacyDocumentFile createIndividualCandidacyDocumentFile(
            CandidacyProcessDocumentUploadBean uploadBean, String documentIdNumber) throws IOException {
        return uploadBean.createIndividualCandidacyDocumentFile(getParentProcessType(), documentIdNumber);
    }

    public ActionForward executeChangeProcessCheckedState(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        try {
            executeActivity(getProcess(request), "ChangeProcessCheckedState", getIndividualCandidacyProcessBean());
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("change-process-checked-state");
        }

        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward executeChangePaymentCheckedState(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        try {
            executeActivity(getProcess(request), "ChangePaymentCheckedState", getIndividualCandidacyProcessBean());
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
            return mapping.findForward("change-payment-checked-state");
        }

        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecuteRevertApplicationToStandBy(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        try {
            executeActivity(getProcess(request), "RevertApplicationToStandBy", null);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            return listProcessAllowedActivities(mapping, actionForm, request, response);
        }

        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    protected void invalidateDocumentFileRelatedViewStates() {
        List<IViewState> viewStates =
                new ArrayList<IViewState>((List<IViewState>) RenderersRequestProcessorImpl.getCurrentRequest().getAttribute(
                        LifeCycleConstants.VIEWSTATE_PARAM_NAME));
        if (viewStates != null) {
            for (IViewState state : viewStates) {
                if (state.getId().indexOf("individualCandidacyProcessBean.document.file") > -1) {
                    RenderUtils.invalidateViewState(state.getId());
                }
            }
        }
    }

    protected boolean existsIndividualCandidacyProcessForDocumentId(HttpServletRequest request, IDDocumentType documentType,
            String identification) {
        return getParentProcess(request).getOpenChildProcessByDocumentId(documentType, identification) != null;
    }

    public ActionForward addConcludedHabilitationsEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        bean.addConcludedFormationBean();

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        invalidateDocumentFileRelatedViewStates();

        return forwardTo(mapping, request);
    }

    public ActionForward removeConcludedHabilitationsEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        IndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        Integer index = getIntegerFromRequest(request, "removeIndex");
        bean.removeFormationConcludedBean(index);

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        invalidateDocumentFileRelatedViewStates();

        return forwardTo(mapping, request);
    }

    private ActionForward forwardTo(ActionMapping mapping, HttpServletRequest request) {
        if (getFromRequest(request, "userAction").equals("createCandidacy")) {
            return mapping.findForward("fill-candidacy-information");
        } else if (getFromRequest(request, "userAction").equals("editCandidacyQualifications")) {
            return mapping.findForward("edit-candidacy-information");
        }

        return null;
    }

    public ActionForward revokeDocumentFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        CandidacyProcessDocumentUploadBean uploadBean =
                (CandidacyProcessDocumentUploadBean) getObjectFromViewState("individualCandidacyProcessBean.document");
        String documentExternalId = request.getParameter("documentFileOid");
        IndividualCandidacyDocumentFile documentFile = FenixFramework.getDomainObject(documentExternalId);
        uploadBean.setDocumentFile(documentFile);

        executeActivity(getProcess(request), "RevokeDocumentFile", uploadBean);

        return prepareExecuteEditDocuments(mapping, form, request, response);
    }

    public ActionForward prepareExecuteRejectCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

        return mapping.findForward("reject-candidacy");
    }

    public ActionForward executeRejectCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            executeActivity(getProcess(request), "RejectCandidacy", null);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            return mapping.findForward("reject-candidacy");
        }
        return listProcessAllowedActivities(mapping, form, request, response);
    }
}