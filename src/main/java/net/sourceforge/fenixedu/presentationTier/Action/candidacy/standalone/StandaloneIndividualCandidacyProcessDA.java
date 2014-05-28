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
package net.sourceforge.fenixedu.presentationTier.Action.candidacy.standalone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.commons.CurricularCourseByExecutionSemesterBean;
import net.sourceforge.fenixedu.dataTransferObject.commons.SearchCurricularCourseByDegree;
import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.standalone.StandaloneCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.standalone.StandaloneIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.standalone.StandaloneIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.standalone.StandaloneIndividualCandidacyResultBean;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.EnrollmentDomainException;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.IndividualCandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/caseHandlingStandaloneIndividualCandidacyProcess", module = "academicAdministration",
        formBeanClass = StandaloneIndividualCandidacyProcessDA.StandaloneIndividualCandidacyForm.class,
        functionality = StandaloneCandidacyProcessDA.class)
@Forwards({
        @Forward(name = "intro",
                path = "/academicAdministration/caseHandlingStandaloneCandidacyProcess.do?method=listProcessAllowedActivities"),
        @Forward(name = "list-allowed-activities", path = "/candidacy/listIndividualCandidacyActivities.jsp"),
        @Forward(name = "prepare-create-new-process", path = "/candidacy/selectPersonForCandidacy.jsp"),
        @Forward(name = "fill-personal-information", path = "/candidacy/fillPersonalInformation.jsp"),
        @Forward(name = "fill-candidacy-information", path = "/candidacy/standalone/fillCandidacyInformation.jsp"),
        @Forward(name = "prepare-candidacy-payment", path = "/candidacy/candidacyPayment.jsp"),
        @Forward(name = "edit-candidacy-personal-information", path = "/candidacy/editPersonalInformation.jsp"),
        @Forward(name = "edit-common-candidacy-information", path = "/candidacy/editCommonCandidacyInformation.jsp"),
        @Forward(name = "edit-candidacy-information", path = "/candidacy/standalone/editCandidacyInformation.jsp"),
        @Forward(name = "introduce-candidacy-result", path = "/candidacy/standalone/introduceCandidacyResult.jsp"),
        @Forward(name = "cancel-candidacy", path = "/candidacy/cancelCandidacy.jsp"),
        @Forward(name = "create-registration", path = "/candidacy/createRegistration.jsp"),
        @Forward(name = "select-person-for-bind-with-candidacy", path = "/candidacy/selectPersonForBind.jsp"),
        @Forward(name = "edit-personal-information-for-bind", path = "/candidacy/editPersonalInformationForCandidacyBind.jsp") })
public class StandaloneIndividualCandidacyProcessDA extends IndividualCandidacyProcessDA {

    static public class StandaloneIndividualCandidacyForm extends FenixActionForm {
        private String curricularCourseBeanKey;

        public String getCurricularCourseBeanKey() {
            return curricularCourseBeanKey;
        }

        public void setCurricularCourseBeanKey(String curricularCourseBeanKey) {
            this.curricularCourseBeanKey = curricularCourseBeanKey;
        }
    }

    @Override
    protected Class<? extends Process> getProcessType() {
        return StandaloneIndividualCandidacyProcess.class;
    }

    @Override
    protected Class getParentProcessType() {
        return StandaloneCandidacyProcess.class;
    }

    @Override
    protected StandaloneCandidacyProcess getParentProcess(HttpServletRequest request) {
        return (StandaloneCandidacyProcess) super.getParentProcess(request);
    }

    @Override
    protected StandaloneIndividualCandidacyProcess getProcess(HttpServletRequest request) {
        return (StandaloneIndividualCandidacyProcess) super.getProcess(request);
    }

    @Override
    protected StandaloneIndividualCandidacyProcessBean getIndividualCandidacyProcessBean() {
        return (StandaloneIndividualCandidacyProcessBean) super.getIndividualCandidacyProcessBean();
    }

    @Override
    public ActionForward listProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("processId", getParentProcess(request).getExternalId());
        return mapping.findForward("intro");
    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final StandaloneIndividualCandidacyProcessBean bean = new StandaloneIndividualCandidacyProcessBean();
        bean.setCandidacyProcess(getParentProcess(request));

        /*
         * 06/05/2009 - Due to Public Candidacies, a candidacy created in admin
         * office is external So we dont require ChoosePersonBean because a
         * Person will not be associated or created at individual candidacy
         * creation stage. Instead we bind with an empty PersonBean.
         * 
         * bean.setChoosePersonBean(new ChoosePersonBean());
         */
        bean.setPersonBean(new PersonBean());
        bean.setChoosePersonBean(new ChoosePersonBean());

        /*
         * 06/05/2009 - Also we mark the bean as an external candidacy.
         */
        bean.setInternalPersonCandidacy(Boolean.FALSE);
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
    }

    @Override
    public ActionForward fillCandidacyInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final StandaloneIndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        bean.setSearchCurricularCourseByDegree(new SearchCurricularCourseByDegree(bean.getCandidacyExecutionInterval()));
        return super.fillCandidacyInformation(mapping, actionForm, request, response);
    }

    public ActionForward searchCurricularCourseByDegreePostBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        RenderUtils.invalidateViewState();
        return mapping.findForward("fill-candidacy-information");
    }

    public ActionForward addCurricularCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final StandaloneIndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        bean.addSelectedCurricularCourseToResult();
        return mapping.findForward("fill-candidacy-information");
    }

    public ActionForward removeCurricularCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final StandaloneIndividualCandidacyForm form = (StandaloneIndividualCandidacyForm) actionForm;
        final StandaloneIndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        bean.removeCurricularCourseFromResult(CurricularCourseByExecutionSemesterBean.buildFrom(form.getCurricularCourseBeanKey()));
        return mapping.findForward("fill-candidacy-information");
    }

    public ActionForward prepareExecuteEditCandidacyPersonalInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final StandaloneIndividualCandidacyProcessBean bean = new StandaloneIndividualCandidacyProcessBean();
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
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecuteEditCandidacyInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final StandaloneIndividualCandidacyProcessBean bean = new StandaloneIndividualCandidacyProcessBean(getProcess(request));
        bean.setSearchCurricularCourseByDegree(new SearchCurricularCourseByDegree(getProcess(request)
                .getCandidacyExecutionInterval()));
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward executeEditCandidacyInformationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward searchCurricularCourseByDegreePostBackWhenEditing(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
        RenderUtils.invalidateViewState();
        return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward addCurricularCourseWhenEditing(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final StandaloneIndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        bean.addSelectedCurricularCourseToResult();
        return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward removeCurricularCourseWhenEditing(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final StandaloneIndividualCandidacyForm form = (StandaloneIndividualCandidacyForm) actionForm;
        final StandaloneIndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
        bean.removeCurricularCourseFromResult(CurricularCourseByExecutionSemesterBean.buildFrom(form.getCurricularCourseBeanKey()));
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
        request.setAttribute("standaloneIndividualCandidacyResultBean", new StandaloneIndividualCandidacyResultBean(
                getProcess(request)));
        return mapping.findForward("introduce-candidacy-result");
    }

    public ActionForward executeIntroduceCandidacyResultInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("standaloneIndividualCandidacyResultBean", getCandidacyResultBean());
        return mapping.findForward("introduce-candidacy-result");
    }

    public ActionForward executeIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {

        try {
            executeActivity(getProcess(request), "IntroduceCandidacyResult", getCandidacyResultBean());
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("standaloneIndividualCandidacyResultBean", getCandidacyResultBean());
            return mapping.findForward("introduce-candidacy-result");
        }

        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    private StandaloneIndividualCandidacyResultBean getCandidacyResultBean() {
        return getRenderedObject("standaloneIndividualCandidacyResultBean");
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
        } catch (EnrollmentDomainException ex) {
            addRuleResultMessagesToActionMessages("error", request, ex.getFalseResult());
            request.setAttribute("degree", getProcess(request).getCandidacySelectedDegree());
            return mapping.findForward("create-registration");

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
        final StandaloneIndividualCandidacyProcessBean bean =
                new StandaloneIndividualCandidacyProcessBean((StandaloneIndividualCandidacyProcess) process);
        bean.setCandidacyProcess(getParentProcess(request));

        bean.setChoosePersonBean(new ChoosePersonBean(process.getCandidacy().getPersonalDetails()));
        bean.setPersonBean(new PersonBean(process.getCandidacy().getPersonalDetails()));

        request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
    }

}
