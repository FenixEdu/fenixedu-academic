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
package org.fenixedu.academic.ui.struts.action.BolonhaManager;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.DegreeModuleScope;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformation;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformationChangeRequest;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseLoadBean;
import org.fenixedu.academic.domain.degreeStructure.CurricularStage;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.DepartmentUnit;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.bolonhaManager.DeleteCompetenceCourseInformationChangeRequest;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.BolonhaManager.BolonhaManagerApplication.CompetenceCourseManagementApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.Atomic;

@StrutsFunctionality(app = CompetenceCourseManagementApp.class, path = "versions", titleKey = "label.manage.versions")
@Mapping(module = "bolonhaManager", path = "/competenceCourses/manageVersions")
@Forwards({
        @Forward(name = "showCourses", path = "/bolonhaManager/competenceCourseVersions/listCompetenceCourses.jsp"),
        @Forward(name = "createVersions", path = "/bolonhaManager/competenceCourseVersions/createVersion.jsp"),
        @Forward(name = "viewVersions", path = "/bolonhaManager/competenceCourseVersions/viewVersions.jsp"),
        @Forward(name = "viewVersionDetails", path = "/bolonhaManager/competenceCourseVersions/viewVersionDetails.jsp"),
        @Forward(name = "editBiblio", path = "/bolonhaManager/competenceCourseVersions/editBibliography.jsp"),
        @Forward(name = "editBibliographicReference",
                path = "/bolonhaManager/competenceCourseVersions/editBibliographicReference.jsp"),
        @Forward(name = "viewInformationDetails",
                path = "/bolonhaManager/competenceCourseVersions/viewCompetenceCourseInformation.jsp") })
public class ManageCompetenceCourseInformationVersions extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        CompetenceCourseInformationRequestBean requestBean = getOrCreateRequestBean(request);
        List<Department> departments = Bennu.getInstance().getDepartmentsSet().stream()
                .filter(dep -> dep.getCompetenceCourseMembersGroup().isMember(Authenticate.getUser()))
                .sorted(Department.COMPARATOR_BY_NAME)
                .collect(Collectors.toList());
        request.setAttribute("departments", departments);
        Department department = getDepartment(request, departments);
        request.setAttribute(
                "department", department);
        request.setAttribute("competenceCourseMembersGroupMembers", department == null ? null : department
                .getCompetenceCourseMembersGroup().getMembers().collect(Collectors.toSet()));
        request.setAttribute("requestBean", requestBean);
        return mapping.findForward("showCourses");
    }

    private Department getDepartment(HttpServletRequest request, List<Department> departments) {
        Department department = getDomainObject(request, "departmentID");
        return department != null ? department : departments.isEmpty() ? null : departments.get(0);
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
            CompetenceCourseInformation courseInformation = course.findCompetenceCourseInformationForExecutionPeriod((period != null) ? period : ExecutionSemester
                    .readActualExecutionSemester());
            if (courseInformation != null) {
                bean =
                    new CompetenceCourseInformationRequestBean(courseInformation);
            } else {
                bean = new CompetenceCourseInformationRequestBean(course, (period != null) ? period : ExecutionSemester
                        .readActualExecutionSemester());
            }
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
                load = new CompetenceCourseLoadBean(information.getCompetenceCourseLoadsSet().iterator().next());
            } else if (period != null
                    && course.findCompetenceCourseInformationForExecutionPeriod(period).getCompetenceCourseLoadsSet().size() > 0) {
                load =
                        new CompetenceCourseLoadBean(course.findCompetenceCourseInformationForExecutionPeriod(period)
                                .getCompetenceCourseLoadsSet().iterator().next());
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
        bean.setReferences(bean.getReferences().with(referenceBean.getYear(), referenceBean.getTitle(),
                referenceBean.getAuthors(), referenceBean.getReference(), referenceBean.getUrl(), referenceBean.getType()));
        RenderUtils.invalidateViewState("createReference");
        return viewBibliography(mapping, form, request, response);
    }

    public ActionForward prepareEditBibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        CompetenceCourseInformationRequestBean bean =
                (CompetenceCourseInformationRequestBean) RenderUtils.getViewState("editVersion").getMetaObject().getObject();

        Integer index = Integer.parseInt(request.getParameter("index"));

        request.setAttribute("editBean", new EditReferenceBean(bean.getReferences().getBibliographicReference(index)));
        request.setAttribute("bean", bean);

        return mapping.findForward("editBibliographicReference");
    }

    public ActionForward editBibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        CompetenceCourseInformationRequestBean bean =
                (CompetenceCourseInformationRequestBean) RenderUtils.getViewState("editVersion").getMetaObject().getObject();
        EditReferenceBean referenceBean =
                (EditReferenceBean) RenderUtils.getViewState("editReference").getMetaObject().getObject();

        bean.setReferences(bean.getReferences().replacing(referenceBean.getOrder(), referenceBean.getYear(),
                referenceBean.getTitle(), referenceBean.getAuthors(), referenceBean.getReference(), referenceBean.getUrl(),
                referenceBean.getType()));

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
        bean.setReferences(bean.getReferences().without(Integer.valueOf(request.getParameter("index"))));
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

        for (CompetenceCourseInformation information : course.getCompetenceCourseInformationsSet()) {
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
            spreadsheet.setHeader(BundleUtil.getString(Bundle.BOLONHA, "competenceCourse"));
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
        final Teacher teacher = person == null ? null : person.getTeacher();
        return teacher == null ? null : teacher.getDepartment();
    }

}
