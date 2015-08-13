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
package org.fenixedu.academic.ui.struts.action.scientificCouncil.thesis;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.ScientificCommission;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.interfaces.HasExecutionYear;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant;
import org.fenixedu.academic.domain.thesis.ThesisFile;
import org.fenixedu.academic.domain.thesis.ThesisParticipationType;
import org.fenixedu.academic.domain.thesis.ThesisVisibilityType;
import org.fenixedu.academic.dto.VariantBean;
import org.fenixedu.academic.service.services.commons.FactoryExecutor;
import org.fenixedu.academic.service.services.scientificCouncil.thesis.ApproveThesisDiscussion;
import org.fenixedu.academic.service.services.scientificCouncil.thesis.ApproveThesisProposal;
import org.fenixedu.academic.service.services.thesis.ChangeThesisPerson;
import org.fenixedu.academic.service.services.thesis.ChangeThesisPerson.PersonChange;
import org.fenixedu.academic.service.services.thesis.ChangeThesisPerson.PersonTarget;
import org.fenixedu.academic.service.services.thesis.CreateThesisAbstractFile;
import org.fenixedu.academic.service.services.thesis.CreateThesisDissertationFile;
import org.fenixedu.academic.ui.renderers.providers.ExecutionDegreesWithDissertationByExecutionYearProvider;
import org.fenixedu.academic.ui.struts.action.commons.AbstractManageThesisDA;
import org.fenixedu.academic.ui.struts.action.coordinator.thesis.ThesisBean;
import org.fenixedu.academic.ui.struts.action.coordinator.thesis.ThesisPresentationState;
import org.fenixedu.academic.ui.struts.action.scientificCouncil.ScientificCouncilApplication.ScientificDisserationsApp;
import org.fenixedu.academic.ui.struts.action.student.thesis.ThesisFileBean;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.io.ByteStreams;

@StrutsFunctionality(app = ScientificDisserationsApp.class, path = "list", titleKey = "navigation.list.jury.proposals")
@Mapping(path = "/scientificCouncilManageThesis", module = "scientificCouncil")
@Forwards({ @Forward(name = "list-thesis", path = "/scientificCouncil/thesis/listThesis.jsp"),
        @Forward(name = "review-proposal", path = "/scientificCouncil/thesis/reviewProposal.jsp"),
        @Forward(name = "review-thesis", path = "/scientificCouncil/thesis/reviewThesis.jsp"),
        @Forward(name = "view-thesis", path = "/scientificCouncil/thesis/viewThesis.jsp"),
        @Forward(name = "list-scientific-comission", path = "/scientificCouncil/thesis/listScientificComission.jsp"),
        @Forward(name = "list-thesis-creation-periods", path = "/scientificCouncil/thesis/listThesisCreationPeriods.jsp"),
        @Forward(name = "viewOperationsThesis", path = "/student/thesis/viewOperationsThesis.jsp"),
        @Forward(name = "showDissertationsInfo", path = "/scientificCouncil/thesis/showDissertationsInfo.jsp"),
        @Forward(name = "editParticipant", path = "/scientificCouncil/thesis/editParticipant.jsp"),
        @Forward(name = "select-person", path = "/scientificCouncil/thesis/selectPerson.jsp"),
        @Forward(name = "select-external", path = "/scientificCouncil/thesis/selectExternal.jsp"),
        @Forward(name = "change-information-with-docs", path = "/scientificCouncil/thesis/changeInformationWithDocs.jsp"),
        @Forward(name = "search-student", path = "/scientificCouncil/thesis/searchStudent.jsp") })
public class ScientificCouncilManageThesisDA extends AbstractManageThesisDA {

    @StrutsFunctionality(app = ScientificDisserationsApp.class, path = "define-rules", titleKey = "navigation.thesis.info")
    @Mapping(path = "/defineDissertationRules", module = "scientificCouncil")
    public static class DefineDissertationRules extends ScientificCouncilManageThesisDA {
        @EntryPoint
        @Override
        public ActionForward dissertations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
                HttpServletResponse response) throws Exception {
            return super.dissertations(mapping, actionForm, request, response);
        }
    }

    @StrutsFunctionality(app = ScientificDisserationsApp.class, path = "define-periods",
            titleKey = "navigation.list.thesis.creation.periods")
    @Mapping(path = "/defineDissertationPeriods", module = "scientificCouncil")
    public static class DefineDissertationPeriods extends ScientificCouncilManageThesisDA {
        @EntryPoint
        @Override
        public ActionForward listThesisCreationPeriods(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
                HttpServletResponse response) throws Exception {
            return super.listThesisCreationPeriods(mapping, actionForm, request, response);
        }
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Thesis thesis = getThesis(request);
        request.setAttribute("thesis", thesis);

        if (thesis != null) {
            final ThesisPresentationState thesisPresentationState = ThesisPresentationState.getThesisPresentationState(thesis);
            request.setAttribute("thesisPresentationState", thesisPresentationState);
            request.setAttribute("degreeID", thesis.getDegree().getExternalId());
            request.setAttribute("executionYearID", thesis.getExecutionYear().getExternalId());
        }

        Degree degree = getDegree(request);
        ExecutionYear executionYear = getExecutionYear(request);

        setFilterContext(request, degree, executionYear);

        return super.execute(mapping, actionForm, request, response);
    }

    public static class DissertationsContextBean implements Serializable, HasExecutionYear {

        ExecutionDegree executionDegree;

        ExecutionYear executionYear;

        public DissertationsContextBean(ExecutionDegree executionDegree, ExecutionYear executionYear) {
            this.executionDegree = executionDegree;
            this.executionYear = executionYear;
        }

        public ExecutionDegree getExecutionDegree() {
            return executionDegree;
        }

        public void setExecutionDegree(ExecutionDegree executionDegree) {
            this.executionDegree = executionDegree;
        }

        @Override
        public ExecutionYear getExecutionYear() {
            return executionYear;
        }

        public void setExecutionYear(ExecutionYear executionYear) {
            this.executionYear = executionYear;
        }
    }

    @SuppressWarnings({ "serial", "deprecation" })
    public static class ThesisCreationPeriodFactoryExecutor implements FactoryExecutor, HasExecutionYear, Serializable {

        private ExecutionYear executionYear;

        private ExecutionDegree executionDegree;

        private YearMonthDay beginThesisCreationPeriod;

        private YearMonthDay endThesisCreationPeriod;

        @Override
        public Object execute() {
            final ExecutionDegree executionDegree = getExecutionDegree();
            if (executionDegree == null) {
                final ExecutionYear executionYear = getExecutionYear();
                if (executionYear != null) {
                    for (final ExecutionDegree otherExecutionDegree : executionYear.getExecutionDegreesSet()) {
                        execute(otherExecutionDegree);
                    }
                }
            } else {
                execute(executionDegree);
            }
            return null;
        }

        private void execute(final ExecutionDegree executionDegree) {
            executionDegree.setBeginThesisCreationPeriod(beginThesisCreationPeriod);
            executionDegree.setEndThesisCreationPeriod(endThesisCreationPeriod);
        }

        @Override
        public ExecutionYear getExecutionYear() {
            return executionYear;
        }

        public void setExecutionYear(final ExecutionYear executionYear) {
            this.executionYear = executionYear;
        }

        public ExecutionDegree getExecutionDegree() {
            return executionDegree;
        }

        public void setExecutionDegree(final ExecutionDegree executionDegree) {
            this.executionDegree = executionDegree;
        }

        public YearMonthDay getBeginThesisCreationPeriod() {
            return beginThesisCreationPeriod;
        }

        public void setBeginThesisCreationPeriod(YearMonthDay beginThesisCreationPeriod) {
            this.beginThesisCreationPeriod = beginThesisCreationPeriod;
        }

        public YearMonthDay getEndThesisCreationPeriod() {
            return endThesisCreationPeriod;
        }

        public void setEndThesisCreationPeriod(YearMonthDay endThesisCreationPeriod) {
            this.endThesisCreationPeriod = endThesisCreationPeriod;
        }

        public boolean hasExecutionYear() {
            return getExecutionYear() != null;
        }

    }

    public ActionForward dissertations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DissertationsContextBean dissertationsContextBean = getDissertationsContextBean(request);

        if (dissertationsContextBean.getExecutionDegree() != null) {
            request.setAttribute("executionDegree", dissertationsContextBean.getExecutionDegree());
        }

        request.setAttribute("dissertationsContextBean", dissertationsContextBean);

        return mapping.findForward("showDissertationsInfo");
    }

    private void setFilterContext(HttpServletRequest request, Degree degree, ExecutionYear executionYear) {
        request.setAttribute("degree", degree);
        request.setAttribute("degreeId", degree == null ? "" : degree.getExternalId());
        request.setAttribute("executionYear", executionYear);
        request.setAttribute("executionYearId", executionYear == null ? "" : executionYear.getExternalId());
    }

    private Degree getDegree(HttpServletRequest request) {
        return getDomainObject(request, "degreeID");
    }

    private ExecutionDegree getExecutionDegree(HttpServletRequest request) {
        return getDomainObject(request, "executionDegreeOID");
    }

    private ExecutionYear getExecutionYear(HttpServletRequest request) {
        return getDomainObject(request, "executionYearID");
    }

    @EntryPoint
    public ActionForward listThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ThesisContextBean bean = getContextBean(request);

        Degree degree = bean.getDegree();
        ExecutionYear executionYear = bean.getExecutionYear();

        setFilterContext(request, degree, executionYear);

        List<Thesis> theses = new ArrayList<Thesis>();

        theses.addAll(Thesis.getSubmittedThesis(degree, executionYear));
        theses.addAll(Thesis.getApprovedThesis(degree, executionYear));
        theses.addAll(Thesis.getConfirmedThesis(degree, executionYear));
        theses.addAll(Thesis.getEvaluatedThesis(degree, executionYear));

        request.setAttribute("contextBean", bean);
        request.setAttribute("theses", theses);

        return mapping.findForward("list-thesis");
    }

    public ActionForward listScientificComission(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Degree degree = getDomainObject(request, "degreeId");
        request.setAttribute("degree", degree);

        final ExecutionYear executionYear = getDomainObject(request, "executionYearId");
        request.setAttribute("executionYear", executionYear);

        if (degree == null || executionYear == null) {
            return listThesis(mapping, actionForm, request, response);
        }
        final Set<ExecutionDegree> executionDegrees = new HashSet<ExecutionDegree>();
        for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
            for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
                if (executionDegree.getExecutionYear() == executionYear) {
                    executionDegrees.add(executionDegree);
                }
            }
        }
        request.setAttribute("executionDegrees", executionDegrees);
        request.setAttribute("usernameBean", new VariantBean());
        return mapping.findForward("list-scientific-comission");
    }

    public ActionForward removeScientificCommission(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ScientificCommission scientificCommission = getDomainObject(request, "commissionID");
        removeScientificCommissionFromExecutionDegree(scientificCommission, scientificCommission.getExecutionDegree());
        return listScientificComission(mapping, actionForm, request, response);
    }

    @Atomic
    public void removeScientificCommissionFromExecutionDegree(ScientificCommission scientificCommission,
            ExecutionDegree executionDegree) {
        scientificCommission.delete();
    }

    public ActionForward addScientificCommission(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        VariantBean bean = getRenderedObject("usernameChoice");
        if (bean != null) {
            ExecutionDegree executionDegree = FenixFramework.getDomainObject(request.getParameter("executionDegreeID"));
            Person person = Person.readPersonByUsername(bean.getString());
            if (person == null || executionDegree.isPersonInScientificCommission(person)) {
                addActionMessage("addError", request, "error.scientificComission.person");
            } else {
                addScientificCommissionFromExecutionDegree(executionDegree, person);
                RenderUtils.invalidateViewState("usernameChoice");
            }
        }
        return listScientificComission(mapping, actionForm, request, response);
    }

    @Atomic
    public void addScientificCommissionFromExecutionDegree(ExecutionDegree executionDegree, Person person) {
        new ScientificCommission(executionDegree, person);
    }

    private ThesisContextBean getContextBean(HttpServletRequest request) {
        ThesisContextBean bean = getRenderedObject("contextBean");
        RenderUtils.invalidateViewState("contextBean");

        if (bean != null) {
            return bean;
        } else {
            Degree degree = getDegree(request);
            ExecutionYear executionYear = getExecutionYear(request);

            if (executionYear == null) {
                executionYear = ExecutionYear.readCurrentExecutionYear();
            }

            return new ThesisContextBean(degree, executionYear);
        }
    }

    private DissertationsContextBean getDissertationsContextBean(HttpServletRequest request) {
        DissertationsContextBean bean = getRenderedObject("dissertationsContextBean");
        RenderUtils.invalidateViewState("dissertationsContextBean");

        if (bean != null) {
            return bean;
        } else {
            ExecutionDegree degree = getExecutionDegree(request);
            ExecutionYear executionYear = getExecutionYear(request);

            if (executionYear == null) {
                executionYear = ExecutionYear.readCurrentExecutionYear();
            }

            return new DissertationsContextBean(degree, executionYear);
        }
    }

    public ActionForward reviewProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("review-proposal");
    }

    public ActionForward approveProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        if (thesis != null) {
            try {
                ApproveThesisProposal.runApproveThesisProposal(thesis);
                final ThesisPresentationState thesisPresentationState =
                        ThesisPresentationState.getThesisPresentationState(thesis);
                request.setAttribute("thesisPresentationState", thesisPresentationState);
            } catch (final DomainException e) {
                addActionMessage("error", request, e.getKey(), e.getArgs());
                return reviewProposal(mapping, actionForm, request, response);
            }
            addActionMessage("mail", request, "thesis.approved.mail.sent");
        }

        // return listThesis(mapping, actionForm, request, response);
        return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward confirmRejectProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("confirmReject", true);
        return reviewProposal(mapping, actionForm, request, response);
    }

    public ActionForward reviewThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("review-thesis");
    }

    public ActionForward confirmApprove(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("confirmApprove", true);
        return reviewThesis(mapping, actionForm, request, response);
    }

    public ActionForward confirmDisapprove(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("confirmDisapprove", true);
        return reviewThesis(mapping, actionForm, request, response);
    }

    public ActionForward approveThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        if (thesis != null) {
            try {
                ApproveThesisDiscussion.runApproveThesisDiscussion(thesis);
                addActionMessage("mail", request, "thesis.evaluated.mail.sent");
                final ThesisPresentationState thesisPresentationState =
                        ThesisPresentationState.getThesisPresentationState(thesis);
                request.setAttribute("thesisPresentationState", thesisPresentationState);
            } catch (DomainException e) {
                addActionMessage("error", request, e.getKey(), e.getArgs());
                return reviewThesis(mapping, actionForm, request, response);
            }
        }

        // return listThesis(mapping, actionForm, request, response);
        return viewThesis(mapping, actionForm, request, response);
    }

    @Atomic
    public ActionForward changeThesisFilesVisibility(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        getThesis(request).swapFilesVisibility();
        return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward viewThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        setDocumentAvailability(request);
        return mapping.findForward("view-thesis");
    }

    private void setDocumentAvailability(final HttpServletRequest request) {
        final Thesis thesis = getThesis(request);
        if (thesis.areThesisFilesReadable()) {
            request.setAttribute("containsThesisFileReadersGroup", Boolean.TRUE);
        }
    }

    public ActionForward showMakeDocumentUnavailablePage(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("showMakeDocumentUnavailablePage", Boolean.TRUE);
        return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward showMakeDocumentsAvailablePage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("showMakeDocumentsAvailablePage", Boolean.TRUE);
        return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward editThesisAbstract(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("editThesisAbstract", Boolean.TRUE);
        return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward showSubstituteDocumentsPage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Thesis thesis = getThesis(request);
        final ThesisFile thesisFile = thesis.getDissertation();
        final ThesisFileBean thesisFileBean = new ThesisFileBean();
        thesisFileBean.setTitle(thesisFile.getTitle());
        thesisFileBean.setSubTitle(thesisFile.getSubTitle());
        thesisFileBean.setLanguage(thesisFile.getLanguage());
        request.setAttribute("fileBean", thesisFileBean);
        request.setAttribute("showSubstituteDocumentsPage", Boolean.TRUE);
        return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward showSubstituteExtendedAbstractPage(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final ThesisFileBean thesisFileBean = new ThesisFileBean();
        request.setAttribute("fileBean", thesisFileBean);
        request.setAttribute("showSubstituteExtendedAbstractPage", Boolean.TRUE);
        return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward makeDocumentUnavailablePage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Thesis thesis = getThesis(request);
        FenixFramework.atomic(() -> thesis.setVisibility(null));
        return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward makeDocumentAvailablePage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Thesis thesis = getThesis(request);
        FenixFramework.atomic(() -> thesis.setVisibility(ThesisVisibilityType.INTRANET));
        return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward substituteDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ThesisFileBean bean = getRenderedObject();
        RenderUtils.invalidateViewState();

        if (bean != null && bean.getFile() != null) {
            byte[] bytes = ByteStreams.toByteArray(bean.getFile());
            CreateThesisDissertationFile.runCreateThesisDissertationFile(getThesis(request), bytes, bean.getSimpleFileName(),
                    bean.getTitle(), bean.getSubTitle(), bean.getLanguage());
        }

        return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward substituteExtendedAbstract(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ThesisFileBean bean = getRenderedObject();
        RenderUtils.invalidateViewState();

        if (bean != null && bean.getFile() != null) {
            byte[] bytes = ByteStreams.toByteArray(bean.getFile());
            CreateThesisAbstractFile.runCreateThesisAbstractFile(getThesis(request), bytes, bean.getSimpleFileName(),
                    bean.getTitle(), bean.getSubTitle(), bean.getLanguage());
        }

        return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward listThesisCreationPeriods(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ThesisCreationPeriodFactoryExecutor thesisCreationPeriodFactoryExecutor =
                getThesisCreationPeriodFactoryExecutor(request);

        return forwardToListThesisCreationPeriodsPage(mapping, request, thesisCreationPeriodFactoryExecutor);
    }

    private ActionForward forwardToListThesisCreationPeriodsPage(ActionMapping mapping, HttpServletRequest request,
            final ThesisCreationPeriodFactoryExecutor thesisCreationPeriodFactoryExecutor) {
        if (thesisCreationPeriodFactoryExecutor.getExecutionDegree() == null) {
            final ExecutionDegreesWithDissertationByExecutionYearProvider provider =
                    new ExecutionDegreesWithDissertationByExecutionYearProvider();
            request.setAttribute("executionDegrees", provider.provide(thesisCreationPeriodFactoryExecutor, null));
        }

        return mapping.findForward("list-thesis-creation-periods");
    }

    private ThesisCreationPeriodFactoryExecutor getThesisCreationPeriodFactoryExecutor(final HttpServletRequest request) {
        return getThesisCreationPeriodFactoryExecutor(request, true);
    }

    private ThesisCreationPeriodFactoryExecutor getThesisCreationPeriodFactoryExecutor(final HttpServletRequest request,
            boolean invalidateViewState) {
        ThesisCreationPeriodFactoryExecutor thesisCreationPeriodFactoryExecutor =
                getRenderedObject("thesisCreationPeriodFactoryExecutor");
        if (thesisCreationPeriodFactoryExecutor == null) {
            thesisCreationPeriodFactoryExecutor = new ThesisCreationPeriodFactoryExecutor();

            final String executionYearIdString = request.getParameter("executionYearId");
            final ExecutionYear executionYear;
            if (executionYearIdString == null) {
                if (thesisCreationPeriodFactoryExecutor.hasExecutionYear()) {
                    executionYear = thesisCreationPeriodFactoryExecutor.getExecutionYear();
                } else {
                    executionYear = ExecutionYear.readCurrentExecutionYear();
                }
            } else {
                executionYear = FenixFramework.getDomainObject(executionYearIdString);
            }
            thesisCreationPeriodFactoryExecutor.setExecutionYear(executionYear);

            final String executionDegreeIdString = request.getParameter("executionDegreeId");
            if (executionDegreeIdString != null) {
                final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeIdString);
                thesisCreationPeriodFactoryExecutor.setExecutionDegree(executionDegree);
            }
        }
        if (invalidateViewState) {
            RenderUtils.invalidateViewState();
        }
        request.setAttribute("thesisCreationPeriodFactoryExecutor", thesisCreationPeriodFactoryExecutor);
        return thesisCreationPeriodFactoryExecutor;
    }

    public ActionForward prepareDefineCreationPeriods(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ThesisCreationPeriodFactoryExecutor thesisCreationPeriodFactoryExecutor =
                getThesisCreationPeriodFactoryExecutor(request);

        final ExecutionDegree executionDegree = thesisCreationPeriodFactoryExecutor.getExecutionDegree();
        if (executionDegree == null) {
            final ExecutionYear executionYear = thesisCreationPeriodFactoryExecutor.getExecutionYear();
            if (executionYear != null) {
                for (final ExecutionDegree otherExecutionDegree : executionYear.getExecutionDegreesSet()) {
                    final YearMonthDay beginThesisCreationPeriod = otherExecutionDegree.getBeginThesisCreationPeriod();
                    final YearMonthDay endThesisCreationPeriod = otherExecutionDegree.getEndThesisCreationPeriod();
                    if (beginThesisCreationPeriod != null) {
                        thesisCreationPeriodFactoryExecutor.setBeginThesisCreationPeriod(beginThesisCreationPeriod);
                    }
                    if (endThesisCreationPeriod != null) {
                        thesisCreationPeriodFactoryExecutor.setEndThesisCreationPeriod(endThesisCreationPeriod);
                    }
                    if (thesisCreationPeriodFactoryExecutor.getBeginThesisCreationPeriod() != null
                            && thesisCreationPeriodFactoryExecutor.getEndThesisCreationPeriod() != null) {
                        break;
                    }
                }
            }
        } else {
            thesisCreationPeriodFactoryExecutor.setBeginThesisCreationPeriod(executionDegree.getBeginThesisCreationPeriod());
            thesisCreationPeriodFactoryExecutor.setEndThesisCreationPeriod(executionDegree.getEndThesisCreationPeriod());
        }

        request.setAttribute("prepareDefineCreationPeriods", Boolean.TRUE);

        return mapping.findForward("list-thesis-creation-periods");
    }

    public ActionForward defineCreationPeriods(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ThesisCreationPeriodFactoryExecutor thesisCreationPeriodFactoryExecutor =
                getThesisCreationPeriodFactoryExecutor(request);
        executeFactoryMethod(thesisCreationPeriodFactoryExecutor);
        thesisCreationPeriodFactoryExecutor.setExecutionDegree(null);
        return forwardToListThesisCreationPeriodsPage(mapping, request, thesisCreationPeriodFactoryExecutor);
    }

    public ActionForward defineCreationPeriodsInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ThesisCreationPeriodFactoryExecutor thesisCreationPeriodFactoryExecutor =
                getThesisCreationPeriodFactoryExecutor(request, false);
        request.setAttribute("prepareDefineCreationPeriods", Boolean.TRUE);
        return forwardToListThesisCreationPeriodsPage(mapping, request, thesisCreationPeriodFactoryExecutor);
    }

    public ActionForward downloadDissertationsList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String executionYearIdString = request.getParameter("executionYearId");
        final ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearIdString);

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=dissertacoes" + executionYear.getYear().replace("/", "")
                + ".xls");
        ServletOutputStream writer = response.getOutputStream();

        exportDissertations(writer, executionYear);

        writer.flush();
        response.flushBuffer();
        return null;
    }

    private void exportDissertations(final ServletOutputStream writer, final ExecutionYear executionYear) throws IOException {
        final Spreadsheet spreadsheet = new Spreadsheet("Dissertacoes " + executionYear.getYear().replace("/", ""));
        spreadsheet.setHeader("Numero aluno");
        spreadsheet.setHeader("Nome aluno");
        spreadsheet.setHeader("Tipo Curso");
        spreadsheet.setHeader("Curso");
        spreadsheet.setHeader("Sigla Curso");
        spreadsheet.setHeader("Tese");
        spreadsheet.setHeader("Estado da tese");

        spreadsheet.setHeader("Data da Discussão");
        spreadsheet.setHeader("Resumo");
        spreadsheet.setHeader("Abstract");

        spreadsheet.setHeader("Numero Orientador");
        spreadsheet.setHeader("Nome Orientador");
        spreadsheet.setHeader("Affiliacao Orientador");
        spreadsheet.setHeader("Distribuicao Creditos Orientador");
        spreadsheet.setHeader("Numero Corientador");
        spreadsheet.setHeader("Nome Corientador");
        spreadsheet.setHeader("Affiliacao Corientador");
        spreadsheet.setHeader("Distribuicao Creditos Corientador");
        spreadsheet.setHeader("Numero Presidente");
        spreadsheet.setHeader("Nome Presidente");
        spreadsheet.setHeader("Afiliação Presidente");
        spreadsheet.setHeader("Nota Dissertação");

        for (final Thesis thesis : rootDomainObject.getThesesSet()) {
            final Enrolment enrolment = thesis.getEnrolment();
            final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
            if (executionSemester.getExecutionYear() == executionYear) {
                final ThesisPresentationState thesisPresentationState =
                        ThesisPresentationState.getThesisPresentationState(thesis);

                final Degree degree = enrolment.getStudentCurricularPlan().getDegree();
                final DegreeType degreeType = degree.getDegreeType();

                final Row row = spreadsheet.addRow();
                row.setCell(thesis.getStudent().getNumber().toString());
                row.setCell(thesis.getStudent().getPerson().getName());
                row.setCell(degreeType.getName().getContent());
                row.setCell(degree.getPresentationName(executionYear));
                row.setCell(degree.getSigla());
                row.setCell(thesis.getTitle().getContent());
                row.setCell(thesisPresentationState.getName());

                if (thesis.getDiscussed() != null) {
                    row.setCell(thesis.getDiscussed().toDate().toString());
                } else {
                    row.setCell("");
                }

                row.setCell(thesis.getThesisAbstractPt());
                row.setCell(thesis.getThesisAbstractEn());

                addTeacherRows(thesis, row, ThesisParticipationType.ORIENTATOR);
                addTeacherRows(thesis, row, ThesisParticipationType.COORIENTATOR);
                addTeacherRows(thesis, row, ThesisParticipationType.PRESIDENT);

                row.setCell(thesis.getMark());
            }
        }
        spreadsheet.exportToXLSSheet(writer);
    }

    protected void addTeacherRows(final Thesis thesis, final Row row, final ThesisParticipationType thesisParticipationType) {
        final StringBuilder numbers = new StringBuilder();
        final StringBuilder names = new StringBuilder();
        final StringBuilder oasb = new StringBuilder();
        final StringBuilder odsb = new StringBuilder();
        for (final ThesisEvaluationParticipant thesisEvaluationParticipant : thesis.getAllParticipants(thesisParticipationType)) {
            if (numbers.length() > 0) {
                numbers.append(" ");
            }
            numbers.append(thesisEvaluationParticipant.getPerson() != null ? thesisEvaluationParticipant.getPerson()
                    .getUsername() : "");

            if (names.length() > 0) {
                names.append(" ");
            }
            names.append(thesisEvaluationParticipant.getName());

            if (oasb.length() > 0) {
                oasb.append(" ");
            }
            final String affiliation = thesisEvaluationParticipant.getAffiliation();
            if (affiliation != null) {
                oasb.append(affiliation);
            } else {
                oasb.append("--");
            }

            if (thesisParticipationType == ThesisParticipationType.ORIENTATOR
                    || thesisParticipationType == ThesisParticipationType.COORIENTATOR) {
                if (odsb.length() > 0) {
                    odsb.append(" ");
                }
                final double credistDistribution = getCreditsDistribution(thesisEvaluationParticipant);
                odsb.append(Double.toString(credistDistribution));
            }
        }
        row.setCell(numbers.toString());
        row.setCell(names.toString());
        row.setCell(oasb.toString());
        if (thesisParticipationType == ThesisParticipationType.ORIENTATOR
                || thesisParticipationType == ThesisParticipationType.COORIENTATOR) {
            row.setCell(odsb.toString());
        }
    }

    public ActionForward changeParticipationInfo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String target = request.getParameter("target");

        if (target == null) {
            return editProposal(mapping, actionForm, request, response);
        }

        Thesis thesis = getThesis(request);
        ThesisEvaluationParticipant participant = FenixFramework.getDomainObject(target);

        PersonTarget targetType = getPersonTarget(participant.getType());

        request.setAttribute("targetType", targetType);
        request.setAttribute("participant", participant);
        return mapping.findForward("editParticipant");
    }

    public ActionForward deleteParticipant(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String target = request.getParameter("target");

        ThesisEvaluationParticipant participant = FenixFramework.getDomainObject(target);

        ChangeThesisPerson.remove(participant);

        return editProposal(mapping, actionForm, request, response);
    }

    private PersonTarget getPersonTarget(ThesisParticipationType type) {
        if (type.equals(ThesisParticipationType.ORIENTATOR)) {
            return PersonTarget.orientator;
        }

        if (type.equals(ThesisParticipationType.COORIENTATOR)) {
            return PersonTarget.coorientator;
        }

        if (type.equals(ThesisParticipationType.PRESIDENT)) {
            return PersonTarget.president;
        }

        if (type.equals(ThesisParticipationType.VOWEL)) {
            return PersonTarget.vowel;
        }

        return null;
    }

    @Override
    public ActionForward editProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        // if (thesis == null) {
        // return listThesis(mapping, actionForm, request, response);
        // }
        //
        request.setAttribute("conditions", thesis.getConditions());

        return viewThesis(mapping, actionForm, request, response);
    }

    private ThesisEvaluationParticipant getVowel(HttpServletRequest request) {
        String parameter = request.getParameter("vowelID");
        if (parameter == null) {
            return null;
        }

        Thesis thesis = getThesis(request);
        for (ThesisEvaluationParticipant participant : thesis.getVowels()) {
            if (participant.getExternalId().equals(parameter)) {
                return participant;
            }
        }

        return null;
    }

    public ActionForward changePerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String target = request.getParameter("target");
        boolean remove = request.getParameter("remove") != null;

        if (target == null) {
            return editProposal(mapping, actionForm, request, response);
        }

        Thesis thesis = getThesis(request);
        ThesisBean bean = new ThesisBean(thesis);

        Degree degree = getDegree(request);
        bean.setDegree(degree);

        PersonTarget targetType = PersonTarget.valueOf(target);
        bean.setTargetType(targetType);

        if (targetType.equals(PersonTarget.vowel)) {
            ThesisEvaluationParticipant targetVowel = getVowel(request);

            if (targetVowel != null) {
                bean.setTarget(targetVowel);
            } else {
                bean.setTarget(null);
            }
        }

        if (remove) {
            DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
            ChangeThesisPerson.run(degreeCurricularPlan, thesis, new PersonChange(bean.getTargetType(), null, bean.getTarget()));

            return editProposal(mapping, actionForm, request, response);
        } else {
            request.setAttribute("bean", bean);
            return mapping.findForward("select-person");
        }
    }

    public ActionForward changeExternal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String target = request.getParameter("target");
        boolean remove = request.getParameter("remove") != null;

        if (target == null) {
            return editProposal(mapping, actionForm, request, response);
        }

        Thesis thesis = getThesis(request);
        ThesisBean bean = new ThesisBean(thesis);

        Degree degree = getDegree(request);
        bean.setDegree(degree);

        PersonTarget targetType = PersonTarget.valueOf(target);
        bean.setTargetType(targetType);

        if (targetType.equals(PersonTarget.vowel)) {
            ThesisEvaluationParticipant targetVowel = getVowel(request);

            if (targetVowel != null) {
                bean.setTarget(targetVowel);
            } else {
                bean.setTarget(null);
            }
        }

        if (remove) {
            DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
            ChangeThesisPerson.run(degreeCurricularPlan, thesis, new PersonChange(bean.getTargetType(), null, bean.getTarget()));

            return editProposal(mapping, actionForm, request, response);
        } else {
            request.setAttribute("bean", bean);
            return mapping.findForward("select-external");
        }
    }

    @Override
    protected DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        Degree degree = getDomainObject(request, "degreeID");

        return degree.getMostRecentDegreeCurricularPlan();
    }

    public ActionForward changeCredits(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String target = request.getParameter("target");

        if (target == null) {
            return editProposal(mapping, actionForm, request, response);
        }

        request.setAttribute("editOrientatorCreditsDistribution", target);

        return editProposal(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward selectPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ThesisBean bean = getRenderedObject("bean");

        if (bean == null) {
            return editProposal(mapping, actionForm, request, response);
        }

        request.setAttribute("bean", bean);

        Person selectedPerson = bean.getPerson();
        if (selectedPerson == null) {
            addActionMessage("info", request, "thesis.selectPerson.internal.required");
            return mapping.findForward("select-person");
        } else {
            Degree degree = getDegree(request);
            ExecutionYear executionYear = getExecutionYear(request);
            List<DegreeCurricularPlan> degreeCurricularPlansForYear = degree.getDegreeCurricularPlansForYear(executionYear);
            DegreeCurricularPlan degreeCurricularPlan = degreeCurricularPlansForYear.iterator().next();
            Thesis thesis = getThesis(request);
            final PersonTarget personTarget = bean.getTargetType();
            if (personTarget == PersonTarget.president) {
                if (selectedPerson == null || !degreeCurricularPlan.isScientificCommissionMember(executionYear, selectedPerson)) {
                    addActionMessage("info", request, "thesis.selectPerson.president.required.scientific.commission");
                    return mapping.findForward("select-person");
                }
            }
            ChangeThesisPerson.run(degreeCurricularPlan, thesis,
                    new PersonChange(bean.getTargetType(), selectedPerson, bean.getTarget()));

            return editProposal(mapping, actionForm, request, response);
        }
    }

    public ActionForward changeInformationWithDocs(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("change-information-with-docs");
    }

    public ActionForward editProposalWithDocs(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        if (thesis == null) {
            return listThesis(mapping, actionForm, request, response);
        }

        return viewThesis(mapping, actionForm, request, response);
    }

    private double getCreditsDistribution(ThesisEvaluationParticipant thesisEvaluationParticipant) {
        Thesis thesis = thesisEvaluationParticipant.getThesis();

        if (!thesis.hasCredits()) {
            return 0;
        }
        return thesisEvaluationParticipant.getPercentageDistribution();
    }

}