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
package net.sourceforge.fenixedu.presentationTier.Action.BolonhaManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager.DeleteCompetenceCourseInformationChangeRequest;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.presentationTier.Action.BolonhaManager.BolonhaManagerApplication.CompetenceCourseManagementApp;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@StrutsFunctionality(app = CompetenceCourseManagementApp.class, path = "versions", titleKey = "label.manage.versions")
@Mapping(module = "bolonhaManager", path = "/competenceCourses/manageVersions")
@Forwards({
        @Forward(name = "showCourses", path = "/bolonhaManager/competenceCourseVersions/listCompetenceCourses.jsp",
                tileProperties = @Tile(title = "private.bologna.competencecourses.manageversions")),
        @Forward(name = "createVersions", path = "/bolonhaManager/competenceCourseVersions/createVersion.jsp"),
        @Forward(name = "viewVersions", path = "/bolonhaManager/competenceCourseVersions/viewVersions.jsp"),
        @Forward(name = "viewVersionDetails", path = "/bolonhaManager/competenceCourseVersions/viewVersionDetails.jsp"),
        @Forward(name = "editBiblio", path = "/bolonhaManager/competenceCourseVersions/editBibliography.jsp"),
        @Forward(name = "viewInformationDetails",
                path = "/bolonhaManager/competenceCourseVersions/viewCompetenceCourseInformation.jsp") })
public class ManageCompetenceCourseInformationVersions extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Person loggedPerson = getLoggedPerson(request);
        CompetenceCourseInformationRequestBean requestBean = getOrCreateRequestBean(request);

        request.setAttribute("department", loggedPerson.getEmployee().getCurrentDepartmentWorkingPlace());
        request.setAttribute("requestBean", requestBean);
        return mapping.findForward("showCourses");
    }

    private CompetenceCourseInformationRequestBean getOrCreateRequestBean(HttpServletRequest request) {
        CompetenceCourseInformationRequestBean requestBean =
                (CompetenceCourseInformationRequestBean) getObjectFromViewState("requestBean");
        if (requestBean == null) {
            requestBean = new CompetenceCourseInformationRequestBean();
            requestBean.setShowOldCompetenceCourses(true);
        }
        return requestBean;
    }

    public ActionForward editVersion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        CompetenceCourseInformationChangeRequest changeRequest = getCompetenceCourseInformationRequest(request);
        if (changeRequest == null || !changeRequest.isLoggedPersonAllowedToEdit()) {
            addActionMessage(request, "error.cannot.edit.request");
            return forwardToViewVersions(mapping, request, changeRequest.getCompetenceCourse());
        }
        CompetenceCourseInformationRequestBean bean = new CompetenceCourseInformationRequestBean(changeRequest);
        CompetenceCourseLoadBean loadBean = new CompetenceCourseLoadBean(changeRequest);
        request.setAttribute("beanLoad", loadBean);
        request.setAttribute("bean", bean);
        return mapping.findForward("createVersions");
    }

    public ActionForward prepareCreateVersion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        CompetenceCourse course = getCompetenceCourse(request);
        ExecutionSemester period = getExecutionPeriod(request);

        CompetenceCourseInformationRequestBean bean = null;
        IViewState viewState = RenderUtils.getViewState("editVersion");
        CompetenceCourseInformation information = null;
        if (viewState != null) {
            bean = (CompetenceCourseInformationRequestBean) viewState.getMetaObject().getObject();
            ExecutionSemester beanPeriod = bean.getExecutionPeriod();
            if (beanPeriod == null) {
                beanPeriod = ExecutionSemester.readActualExecutionSemester();
                bean.setExecutionPeriod(beanPeriod);
            }
            information = bean.getCompetenceCourse().findCompetenceCourseInformationForExecutionPeriod(beanPeriod);
        }

        if (bean == null) {
            bean =
                    new CompetenceCourseInformationRequestBean(
                            course.findCompetenceCourseInformationForExecutionPeriod((period != null) ? period : ExecutionSemester
                                    .readActualExecutionSemester()));
        } else {
            if (information == null) {
                bean.reset();
            } else {
                bean.update(information);
            }
        }

        IViewState viewStateLoad = RenderUtils.getViewState("editVersionLoad");
        CompetenceCourseLoadBean load;
        if (viewStateLoad != null) {
            load = (CompetenceCourseLoadBean) viewStateLoad.getMetaObject().getObject();
        } else {
            if (information != null && information.getCompetenceCourseLoadsSet().size() > 0) {
                load = new CompetenceCourseLoadBean(information.getCompetenceCourseLoads().iterator().next());
            } else if (period != null
                    && course.findCompetenceCourseInformationForExecutionPeriod(period).getCompetenceCourseLoadsSet().size() > 0) {
                load =
                        new CompetenceCourseLoadBean(course.findCompetenceCourseInformationForExecutionPeriod(period)
                                .getCompetenceCourseLoads().iterator().next());
            } else {
                load = new CompetenceCourseLoadBean();
            }
        }

        request.setAttribute("beanLoad", load);
        RenderUtils.invalidateViewState("common-part");
        RenderUtils.invalidateViewState("pt-part");
        RenderUtils.invalidateViewState("en-part");
        RenderUtils.invalidateViewState("versionLoad");

        request.setAttribute("bean", bean);
        return mapping.findForward("createVersions");
    }

    private ActionForward forwardToViewVersions(ActionMapping mapping, HttpServletRequest request, CompetenceCourse course) {
        request.setAttribute("competenceCourse", course);
        return mapping.findForward("viewVersions");
    }

    private ExecutionSemester getExecutionPeriod(HttpServletRequest request) {
        return getDomainObject(request, "executionPeriodID");
    }

    public ActionForward revokeVersion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        CompetenceCourseInformationChangeRequest changeRequest = getChangeRequest(request);
        if (changeRequest == null || !changeRequest.isLoggedPersonAllowedToEdit()) {
            addActionMessage(request, "error.cannot.edit.request");
            return forwardToViewVersions(mapping, request, changeRequest.getCompetenceCourse());
        }
        try {
            DeleteCompetenceCourseInformationChangeRequest.run(changeRequest);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
        }

        return showVersions(mapping, form, request, response);
    }

    public ActionForward editBibliography(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        String index = request.getParameter("index");
        request.setAttribute("edit", index);

        return viewBibliography(mapping, form, request, response);
    }

    public ActionForward createBibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        CompetenceCourseInformationRequestBean bean =
                (CompetenceCourseInformationRequestBean) RenderUtils.getViewState("editVersion").getMetaObject().getObject();
        CreateReferenceBean referenceBean =
                (CreateReferenceBean) RenderUtils.getViewState("createReference").getMetaObject().getObject();
        bean.getReferences().createBibliographicReference(referenceBean.getYear(), referenceBean.getTitle(),
                referenceBean.getAuthors(), referenceBean.getReference(), referenceBean.getUrl(), referenceBean.getType());
        RenderUtils.invalidateViewState("createReference");
        return viewBibliography(mapping, form, request, response);
    }

    public ActionForward viewBibliography(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        CompetenceCourseInformationRequestBean bean =
                (CompetenceCourseInformationRequestBean) RenderUtils.getViewState("editVersion").getMetaObject().getObject();
        CompetenceCourseLoadBean load =
                (CompetenceCourseLoadBean) RenderUtils.getViewState("editVersionLoad").getMetaObject().getObject();

        request.setAttribute("bean", bean);
        request.setAttribute("beanLoad", load);

        if (areBeansValid(bean, load)) {
            request.setAttribute("referenceBean", new CreateReferenceBean());
            return mapping.findForward("editBiblio");
        } else {
            addActionMessage(request, "error.all.fields.are.required");
            request.setAttribute("proposal", getFromRequest(request, "proposal"));
            return mapping.findForward("createVersions");
        }
    }

    public ActionForward removeBibliography(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        CompetenceCourseInformationRequestBean bean =
                (CompetenceCourseInformationRequestBean) RenderUtils.getViewState("editVersion").getMetaObject().getObject();
        bean.getReferences().deleteBibliographicReference(Integer.valueOf(request.getParameter("index")));
        return viewBibliography(mapping, form, request, response);
    }

    public ActionForward createVersion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        IViewState viewState = RenderUtils.getViewState("editVersion");
        if (viewState == null) {
            return prepareCreateVersion(mapping, form, request, response);
        }
        CompetenceCourseInformationRequestBean bean =
                (CompetenceCourseInformationRequestBean) viewState.getMetaObject().getObject();
        CompetenceCourseLoadBean load =
                (CompetenceCourseLoadBean) RenderUtils.getViewState("editVersionLoad").getMetaObject().getObject();
        CompetenceCourse course = bean.getCompetenceCourse();
        if (!course.isLoggedPersonAllowedToCreateChangeRequests(bean.getExecutionPeriod())) {
            addActionMessage(request, "error.cannot.create.request.in.chosen.semester");
            return forwardToViewVersions(mapping, request, course);
        }
        try {
            createCompetenceCourseInformationChangeRequest(bean, load, getLoggedPerson(request));
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            return prepareCreateVersion(mapping, form, request, response);
        }
        return showVersions(mapping, form, request, response);
    }

    @Atomic
    private static void createCompetenceCourseInformationChangeRequest(CompetenceCourseInformationRequestBean bean,
            CompetenceCourseLoadBean loadBean, Person requestor) {
        check(RolePredicates.BOLONHA_MANAGER_PREDICATE);
        CompetenceCourse course = bean.getCompetenceCourse();
        ExecutionSemester period = bean.getExecutionPeriod();
        CompetenceCourseInformationChangeRequest request = course.getChangeRequestDraft(period);
        if (request != null) {
            request.edit(bean.getName(), bean.getNameEn(), bean.getJustification(), bean.getRegime(), bean.getObjectives(),
                    bean.getObjectivesEn(), bean.getProgram(), bean.getProgramEn(), bean.getEvaluationMethod(),
                    bean.getEvaluationMethodEn(), bean.getCompetenceCourseLevel(), requestor, loadBean.getTheoreticalHours(),
                    loadBean.getProblemsHours(), loadBean.getLaboratorialHours(), loadBean.getSeminaryHours(),
                    loadBean.getFieldWorkHours(), loadBean.getTrainingPeriodHours(), loadBean.getTutorialOrientationHours(),
                    loadBean.getAutonomousWorkHours(), loadBean.getEctsCredits(), loadBean.getSecondTheoreticalHours(),
                    loadBean.getSecondProblemsHours(), loadBean.getSecondLaboratorialHours(), loadBean.getSecondSeminaryHours(),
                    loadBean.getSecondFieldWorkHours(), loadBean.getSecondTrainingPeriodHours(),
                    loadBean.getSecondTutorialOrientationHours(), loadBean.getSecondAutonomousWorkHours(),
                    loadBean.getSecondEctsCredits(), bean.getReferences(), null);
        } else {
            new CompetenceCourseInformationChangeRequest(bean.getName(), bean.getNameEn(), bean.getJustification(),
                    bean.getRegime(), bean.getObjectives(), bean.getObjectivesEn(), bean.getProgram(), bean.getProgramEn(),
                    bean.getEvaluationMethod(), bean.getEvaluationMethodEn(), bean.getCompetenceCourse(),
                    bean.getCompetenceCourseLevel(), bean.getExecutionPeriod(), requestor, loadBean.getTheoreticalHours(),
                    loadBean.getProblemsHours(), loadBean.getLaboratorialHours(), loadBean.getSeminaryHours(),
                    loadBean.getFieldWorkHours(), loadBean.getTrainingPeriodHours(), loadBean.getTutorialOrientationHours(),
                    loadBean.getAutonomousWorkHours(), loadBean.getEctsCredits(), loadBean.getSecondTheoreticalHours(),
                    loadBean.getSecondProblemsHours(), loadBean.getSecondLaboratorialHours(), loadBean.getSecondSeminaryHours(),
                    loadBean.getSecondFieldWorkHours(), loadBean.getSecondTrainingPeriodHours(),
                    loadBean.getSecondTutorialOrientationHours(), loadBean.getSecondAutonomousWorkHours(),
                    loadBean.getSecondEctsCredits(), bean.getReferences(), null);
        }
    }

    public ActionForward showVersions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        CompetenceCourse course = getCompetenceCourse(request);
        return forwardToViewVersions(mapping, request, course);
    }

    private CompetenceCourse getCompetenceCourse(HttpServletRequest request) {
        return getDomainObject(request, "competenceCourseID");
    }

    public ActionForward viewVersion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        CompetenceCourseInformationChangeRequest changeRequest = getCompetenceCourseInformationRequest(request);
        if (changeRequest == null || !changeRequest.getCompetenceCourse().isLoggedPersonAllowedToViewChangeRequests()) {
            addActionMessage(request, "error.cannot.view.request");
            return forwardToViewVersions(mapping, request, getCompetenceCourse(request));
        }
        request.setAttribute("changeRequest", changeRequest);
        return mapping.findForward("viewVersionDetails");
    }

    private CompetenceCourseInformationChangeRequest getCompetenceCourseInformationRequest(HttpServletRequest request) {
        return getDomainObject(request, "changeRequestID");
    }

    public ActionForward showCompetenceCourseInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        CompetenceCourse course = getCompetenceCourse(request);
        String externalId = request.getParameter("oid");

        for (CompetenceCourseInformation information : course.getCompetenceCourseInformations()) {
            if (information.getExternalId().equals(externalId)) {
                request.setAttribute("information", information);
            }
        }

        return mapping.findForward("viewInformationDetails");
    }

    private CompetenceCourseInformationChangeRequest getChangeRequest(HttpServletRequest request) {
        return getDomainObject(request, "changeRequestID");
    }

    private boolean areBeansValid(CompetenceCourseInformationRequestBean bean, CompetenceCourseLoadBean loadBean) {
        if (StringUtils.isEmpty(bean.getName()) || StringUtils.isEmpty(bean.getNameEn())
                || StringUtils.isEmpty(bean.getJustification()) || bean.getRegime() == null
                || StringUtils.isEmpty(bean.getObjectives()) || StringUtils.isEmpty(bean.getObjectivesEn())
                || StringUtils.isEmpty(bean.getProgram()) || StringUtils.isEmpty(bean.getProgramEn())
                || StringUtils.isEmpty(bean.getEvaluationMethod()) || StringUtils.isEmpty(bean.getEvaluationMethodEn())
                || bean.getCompetenceCourse() == null || bean.getExecutionPeriod() == null
                || bean.getCompetenceCourseLevel() == null || loadBean.getTheoreticalHours() == null
                || loadBean.getProblemsHours() == null || loadBean.getLaboratorialHours() == null
                || loadBean.getSeminaryHours() == null || loadBean.getFieldWorkHours() == null
                || loadBean.getTrainingPeriodHours() == null || loadBean.getTutorialOrientationHours() == null
                || loadBean.getAutonomousWorkHours() == null || loadBean.getEctsCredits() == null) {
            return false;
        }

        return true;
    }

    public ActionForward exportCompetenceCourseExecutionToExcel(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        final List<CompetenceCourse> competenceCourses = getDepartmentCompetenceCourses();

        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=list.xls");

            final ServletOutputStream outputStream = response.getOutputStream();

            final Spreadsheet spreadsheet = new Spreadsheet("list");
            spreadsheet
                    .setHeader(BundleUtil.getString(Bundle.BOLONHA, "competenceCourse"));
            spreadsheet.setHeader(BundleUtil.getString(Bundle.BOLONHA, "curricularPlan"));
            spreadsheet.setHeader(BundleUtil.getString(Bundle.BOLONHA, "curricularYear"));
            spreadsheet.setHeader(BundleUtil.getString(Bundle.BOLONHA, "label.semester"));

            for (final CompetenceCourse competenceCourse : competenceCourses) {
                if (competenceCourse.getCurricularStage() == CurricularStage.APPROVED) {
                    for (final CurricularCourse curricularCourse : competenceCourse.getAssociatedCurricularCoursesSet()) {
                        for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
                            final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
                            for (final DegreeModuleScope degreeModuleScope : curricularCourse.getDegreeModuleScopes()) {
                                if (degreeModuleScope.isActiveForExecutionPeriod(executionSemester)) {
                                    final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
                                    final Row row = spreadsheet.addRow();

                                    row.setCell(competenceCourse.getName(executionSemester));
                                    row.setCell(degreeCurricularPlan.getName());
                                    row.setCell(degreeModuleScope.getCurricularYear());
                                    row.setCell(degreeModuleScope.getCurricularSemester());
                                }
                            }
                        }
                    }
                }
            }

            spreadsheet.exportToXLSSheet(outputStream);
            outputStream.flush();
            response.flushBuffer();
        } catch (final IOException e) {
            throw new FenixServiceException(e);
        }
        return null;
    }

    public List<CompetenceCourse> getDepartmentCompetenceCourses() {
        DepartmentUnit selectedDepartmentUnit = getPersonDepartment().getDepartmentUnit();
        if (selectedDepartmentUnit != null) {
            return selectedDepartmentUnit.getCompetenceCourses(CurricularStage.APPROVED);
        }
        return new ArrayList<CompetenceCourse>();
    }

    public Department getPersonDepartment() {
        final Person person = AccessControl.getPerson();
        final Employee employee = person == null ? null : person.getEmployee();
        return employee == null ? null : employee.getCurrentDepartmentWorkingPlace();
    }

}