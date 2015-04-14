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
package org.fenixedu.academic.ui.struts.action.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.EquivalencePlanEntry;
import org.fenixedu.academic.domain.StudentCurricularPlanEquivalencePlan;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.service.services.coordinator.CreateStudentCurricularPlanEquivalencePlan;
import org.fenixedu.academic.service.services.coordinator.DeleteEquivalencePlanEntry;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerBolonhaTransitionApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ManagerBolonhaTransitionApp.class, path = "local-equivalence", titleKey = "title.local.equivalence")
@Mapping(path = "/degreeCurricularPlan/studentEquivalencyPlan", module = "manager")
@Forwards({ @Forward(name = "showPlan", path = "/academicAdminOffice/degreeCurricularPlan/showStudentEquivalencyPlan.jsp"),
        @Forward(name = "addEquivalency", path = "/academicAdminOffice/degreeCurricularPlan/addStudentEquivalency.jsp") })
public class StudentEquivalencyPlanDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DegreeCurricularPlan selectedDegreeCurricularPlan = getSelectedDegreeCurricularPlan(request);
        if (selectedDegreeCurricularPlan == null) {
            request.setAttribute("degreeCurricularPlans", DegreeCurricularPlan.getDegreeCurricularPlans(DegreeType.oneOf(
                    DegreeType::isBolonhaDegree, DegreeType::isBolonhaMasterDegree, DegreeType::isIntegratedMasterDegree)));
        } else {
            request.setAttribute("selectedDegreeCurricularPlan", selectedDegreeCurricularPlan);
        }

        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        request.setAttribute("degreeCurricularPlan",
                degreeCurricularPlan != null ? degreeCurricularPlan : selectedDegreeCurricularPlan);

        return super.execute(mapping, actionForm, request, response);
    }

    @EntryPoint
    public ActionForward showPlan(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Student student = getStudent(request);
        if (student != null) {
            final StudentCurricularPlanEquivalencePlan studentCurricularPlanEquivalencePlan =
                    getStudentCurricularPlanEquivalencePlan(request, student);
            if (studentCurricularPlanEquivalencePlan != null) {

                final DegreeCurricularPlan degreeCurricularPlan =
                        (DegreeCurricularPlan) request.getAttribute("selectedDegreeCurricularPlan");
                if (degreeCurricularPlan != null) {
                    request.setAttribute("studentCurricularPlanEquivalencePlan", studentCurricularPlanEquivalencePlan);
                    studentCurricularPlanEquivalencePlan.getRootEquivalencyPlanEntryCurriculumModuleWrapper(degreeCurricularPlan);
                    request.setAttribute("rootEquivalencyPlanEntryCurriculumModuleWrapper", studentCurricularPlanEquivalencePlan
                            .getRootEquivalencyPlanEntryCurriculumModuleWrapper(degreeCurricularPlan));
                }

            }
        }
        return mapping.findForward("showPlan");
    }

    public ActionForward showTable(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Student student = getStudent(request);
        if (student != null) {
            final StudentCurricularPlanEquivalencePlan studentCurricularPlanEquivalencePlan =
                    getStudentCurricularPlanEquivalencePlan(request, student);
            if (studentCurricularPlanEquivalencePlan != null) {
                request.setAttribute("studentCurricularPlanEquivalencePlan", studentCurricularPlanEquivalencePlan);
                final DegreeCurricularPlan degreeCurricularPlan =
                        (DegreeCurricularPlan) request.getAttribute("selectedDegreeCurricularPlan");
                final CurriculumModule curriculumModule = getCurriculumModule(request);
                request.setAttribute("equivalencePlanEntryWrappers", studentCurricularPlanEquivalencePlan
                        .getEquivalencePlanEntryWrappers(degreeCurricularPlan, curriculumModule));
            }
        }
        return mapping.findForward("showPlan");
    }

    public ActionForward prepareAddEquivalency(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Student student = getStudent(request);
        final StudentCurricularPlanEquivalencePlan studentCurricularPlanEquivalencePlan =
                getStudentCurricularPlanEquivalencePlan(request, student);
        final DegreeCurricularPlan degreeCurricularPlan =
                (DegreeCurricularPlan) request.getAttribute("selectedDegreeCurricularPlan");

        StudentEquivalencyPlanEntryCreator studentEquivalencyPlanEntryCreator = getRenderedObject();
        if (studentEquivalencyPlanEntryCreator == null) {
            studentEquivalencyPlanEntryCreator =
                    new StudentEquivalencyPlanEntryCreator(studentCurricularPlanEquivalencePlan,
                            degreeCurricularPlan.getEquivalencePlan());
        }

        final CurriculumModule curriculumModule = getCurriculumModule(request);
        if (curriculumModule != null) {
            studentEquivalencyPlanEntryCreator.setOriginDegreeModuleToAdd(curriculumModule.getDegreeModule());
            studentEquivalencyPlanEntryCreator.addOrigin(curriculumModule.getDegreeModule());
        }

        request.setAttribute("studentEquivalencyPlanEntryCreator", studentEquivalencyPlanEntryCreator);
        return mapping.findForward("addEquivalency");
    }

    public ActionForward deleteEquivalency(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        getStudent(request);
        final EquivalencePlanEntry equivalencePlanEntry = getEquivalencePlanEntry(request);
        DeleteEquivalencePlanEntry.runDeleteEquivalencePlanEntry(equivalencePlanEntry);
        return showTable(mapping, actionForm, request, response);
    }

    public ActionForward activate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return changeActiveState(mapping, actionForm, request, response, "ActivateEquivalencePlanEntry");
    }

    public ActionForward deactivate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return changeActiveState(mapping, actionForm, request, response, "DeActivateEquivalencePlanEntry");
    }

    public ActionForward changeActiveState(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, final String service) throws Exception {
        final Student student = getStudent(request);
        final StudentCurricularPlanEquivalencePlan studentCurricularPlanEquivalencePlan =
                getStudentCurricularPlanEquivalencePlan(request, student);
        final EquivalencePlanEntry equivalencePlanEntry = getEquivalencePlanEntry(request);
        final Object[] args = { studentCurricularPlanEquivalencePlan, equivalencePlanEntry };
//        ServiceManagerServiceFactory.executeService(service, args);
//        return showTable(mapping, actionForm, request, response);
        throw new UnsupportedOperationException("Service " + service + " no longer exists");
    }

    private EquivalencePlanEntry getEquivalencePlanEntry(HttpServletRequest request) {
        final String equivalencePlanEntryIDString = request.getParameter("equivalencePlanEntryID");
        return (EquivalencePlanEntry) FenixFramework.getDomainObject(equivalencePlanEntryIDString);
    }

    private StudentCurricularPlanEquivalencePlan getStudentCurricularPlanEquivalencePlan(final HttpServletRequest request,
            final Student student) throws FenixServiceException {
        return CreateStudentCurricularPlanEquivalencePlan.run(student);
    }

    private Student getStudent(final HttpServletRequest request) {
        StudentSearchBean studentSearchBean = getRenderedObject("StudentSearchBeanWithDegreeCurricularPlan");
        if (studentSearchBean == null) {
            studentSearchBean = getRenderedObject("StudentSearchBean");
        }
        if (studentSearchBean == null) {
            studentSearchBean = new StudentSearchBean();
            final String studentNumber = request.getParameter("studentNumber");
            if (studentNumber != null && studentNumber.length() > 0) {
                studentSearchBean.setStudentNumber(Integer.valueOf(studentNumber));
            }

            studentSearchBean
                    .setDegreeCurricularPlan((DegreeCurricularPlan) request.getAttribute("selectedDegreeCurricularPlan"));

            studentSearchBean.setOldDegreeCurricularPlan((DegreeCurricularPlan) request.getAttribute("degreeCurricularPlan"));

        } else {
            final DegreeCurricularPlan degreeCurricularPlan = studentSearchBean.getDegreeCurricularPlan();
            if (degreeCurricularPlan != null) {
                request.setAttribute("selectedDegreeCurricularPlan", degreeCurricularPlan);
            }
        }
        request.setAttribute("studentSearchBean", studentSearchBean);
        final Student student = studentSearchBean.search();
        if (student != null) {
            request.setAttribute("student", student);
        }

        return student;
    }

    private CurriculumModule getCurriculumModule(final HttpServletRequest request) {
        final String curriculumModuleIDString = request.getParameter("curriculumModuleID");
        return FenixFramework.getDomainObject(curriculumModuleIDString);
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(final HttpServletRequest request) {
        return getDegreeCurricularPlan(request, "degreeCurricularPlanID");
    }

    private DegreeCurricularPlan getSelectedDegreeCurricularPlan(final HttpServletRequest request) {
        final StudentSearchBean studentSearchBean = getRenderedObject("StudentSearchBeanWithDegreeCurricularPlan");
        return studentSearchBean != null ? studentSearchBean.getDegreeCurricularPlan() : getDegreeCurricularPlan(request,
                "selectedDegreeCurricularPlanID");
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(final HttpServletRequest request, final String attrName) {
        final String degreeCurricularPlanIDString = request.getParameter(attrName);
        return FenixFramework.getDomainObject(degreeCurricularPlanIDString);
    }

}
