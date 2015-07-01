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
package org.fenixedu.academic.ui.struts.action.resourceAllocationManager.exams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeModuleScope;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.ui.struts.action.base.FenixContextDispatchAction;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.RAMApplication.RAMEvaluationsApp;
import org.fenixedu.bennu.core.util.VariantBean;
import org.fenixedu.bennu.portal.servlet.PortalLayoutInjector;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = RAMEvaluationsApp.class, path = "search-by-degree-and-year",
        titleKey = "link.exams.searchWrittenEvaluationsByDegreeAndYear")
@Mapping(module = "resourceAllocationManager", path = "/searchWrittenEvaluationsByDegreeAndYear",
        formBean = "searchWrittenEvaluationsByDegreeAndYearForm")
@Forwards({ @Forward(name = "showForm", path = "/resourceAllocationManager/writtenEvaluations/selectDegreeAndYear.jsp"),
        @Forward(name = "showMap", path = "/resourceAllocationManager/writtenEvaluations/degreeYearWrittenEvaluationsMap.jsp") })
public class WrittenEvaluationsSearchByDegreeAndYear extends FenixContextDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        VariantBean bean = getRenderedObject();
        if (bean == null) {
            bean = new VariantBean();
            bean.setObject(AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER));
        }
        RenderUtils.invalidateViewState();
        AcademicInterval academicInterval = (AcademicInterval) bean.getObject();
        request.setAttribute("bean", bean);

        final List<LabelValueBean> executionDegreeLabelValueBeans = new ArrayList<LabelValueBean>();
        for (final ExecutionDegree executionDegree : ExecutionDegree.filterByAcademicInterval(academicInterval)) {
            String part =
                    addAnotherInfoToLabel(executionDegree, academicInterval) ? " - "
                            + executionDegree.getDegreeCurricularPlan().getName() : "";
            executionDegreeLabelValueBeans.add(new LabelValueBean(executionDegree.getDegree().getPresentationName() + part,
                    executionDegree.getExternalId().toString()));
        }
        Collections.sort(executionDegreeLabelValueBeans, Comparator.comparing(LabelValueBean::getLabel));
        request.setAttribute("executionDegreeLabelValueBeans", executionDegreeLabelValueBeans);

        return mapping.findForward("showForm");
    }

    private boolean addAnotherInfoToLabel(ExecutionDegree executionDegreeToTest, AcademicInterval academicInterval) {
        Degree degreeToTest = executionDegreeToTest.getDegree();
        for (ExecutionDegree executionDegree : ExecutionDegree.filterByAcademicInterval(academicInterval)) {
            if (degreeToTest.equals(executionDegree.getDegree()) && !(executionDegreeToTest.equals(executionDegree))) {
                return true;
            }
        }
        return false;
    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        PortalLayoutInjector.skipLayoutOn(request);
        prepareInformationToList(form, request);
        return mapping.findForward("showMap");
    }

    private void prepareInformationToList(final ActionForm form, final HttpServletRequest request) {

        DynaActionForm dynaActionForm = (DynaActionForm) form;

        VariantBean bean = getRenderedObject();
        final AcademicInterval academicInterval = (AcademicInterval) bean.getObject();
        request.setAttribute("academicInterval", academicInterval);

        final Boolean selectAllCurricularYears = (Boolean) dynaActionForm.get("selectAllCurricularYears");
        final String[] selectedCurricularYears = (String[]) dynaActionForm.get("selectedCurricularYears");
        final String executionDegreeID = (String) dynaActionForm.get("executionDegreeID");

        final Set<Integer> years = new HashSet<Integer>();
        for (final String yearString : selectedCurricularYears) {
            years.add(Integer.valueOf(yearString));
        }

        final Map<ExecutionDegree, Map<Integer, Set<ExecutionCourse>>> executionCoursesByCurricularYearByExecutionDegree =
                new TreeMap<ExecutionDegree, Map<Integer, Set<ExecutionCourse>>>(new Comparator<ExecutionDegree>() {
                    @Override
                    public int compare(ExecutionDegree executionDegree1, ExecutionDegree executionDegree2) {
                        final Degree degree1 = executionDegree1.getDegreeCurricularPlan().getDegree();
                        final Degree degree2 = executionDegree2.getDegreeCurricularPlan().getDegree();
                        return (degree1.getDegreeType() == degree2.getDegreeType()) ? degree1.getNome().compareTo(
                                degree2.getNome()) : degree1.getDegreeType().compareTo(degree2.getDegreeType());
                    }
                });
        for (final ExecutionDegree executionDegree : ExecutionDegree.filterByAcademicInterval(academicInterval)) {
            if (executionDegreeID == null || executionDegreeID.length() == 0
                    || executionDegreeID.equals(executionDegree.getExternalId().toString())) {
                final Map<Integer, Set<ExecutionCourse>> executionCoursesByCurricularYear =
                        new TreeMap<Integer, Set<ExecutionCourse>>(new Comparator<Integer>() {
                            @Override
                            public int compare(final Integer curricularYear1, final Integer curricularYear2) {
                                return curricularYear1.compareTo(curricularYear2);
                            }
                        });
                executionCoursesByCurricularYearByExecutionDegree.put(executionDegree, executionCoursesByCurricularYear);
                for (final CurricularCourse curricularCourse : executionDegree.getDegreeCurricularPlan()
                        .getCurricularCoursesSet()) {
                    for (final DegreeModuleScope degreeModuleScope : curricularCourse.getDegreeModuleScopes()) {
                        if (degreeModuleScope.isActiveForAcademicInterval(academicInterval)) {
                            final Integer curricularSemester = degreeModuleScope.getCurricularSemester();
                            final Integer curricularYear = degreeModuleScope.getCurricularYear();
                            if (curricularSemester.intValue() == AcademicInterval
                                    .getCardinalityOfAcademicInterval(academicInterval)
                                    && (selectAllCurricularYears != null && selectAllCurricularYears.booleanValue())
                                    || years.contains(curricularYear)) {
                                final Set<ExecutionCourse> executionCourses;
                                if (!executionCoursesByCurricularYear.containsKey(curricularYear)) {
                                    executionCourses = new TreeSet<ExecutionCourse>(new Comparator<ExecutionCourse>() {
                                        @Override
                                        public int compare(final ExecutionCourse executionCourse1,
                                                final ExecutionCourse executionCourse2) {
                                            return executionCourse1.getNome().compareTo(executionCourse2.getNome());
                                        }
                                    });
                                    executionCoursesByCurricularYear.put(curricularYear, executionCourses);
                                } else {
                                    executionCourses = executionCoursesByCurricularYear.get(curricularYear);
                                }
                                for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
                                    if (academicInterval.equals(executionCourse.getAcademicInterval())) {
                                        executionCourses.add(executionCourse);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        request.setAttribute("executionCoursesByCurricularYearByExecutionDegree",
                executionCoursesByCurricularYearByExecutionDegree);

        request.setAttribute("semester", AcademicInterval.getCardinalityOfAcademicInterval(academicInterval));
    }
}