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
package org.fenixedu.academic.ui.struts.action.resourceAllocationManager;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.EnrolmentPeriod;
import org.fenixedu.academic.domain.EnrolmentPeriodInClasses;
import org.fenixedu.academic.domain.EnrolmentPeriodInClassesMobility;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.OccupationPeriod;
import org.fenixedu.academic.domain.OccupationPeriodType;
import org.fenixedu.academic.domain.interfaces.HasExecutionSemester;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.RAMApplication.RAMPeriodsApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = RAMPeriodsApp.class, path = "show", titleKey = "link.periods")
@Mapping(path = "/showPeriods", module = "resourceAllocationManager")
@Forwards(@Forward(name = "firstPage", path = "/resourceAllocationManager/periods/firstPage.jsp"))
public class ViewPeriodsAction extends FenixDispatchAction {

    public static class ContextBean implements Serializable, HasExecutionSemester {

        private static final long serialVersionUID = 1L;

        private ExecutionSemester executionSemester;
        private ExecutionDegree executionDegree;

        public ContextBean() {
            setExecutionSemester(ExecutionSemester.readActualExecutionSemester());
        }

        public ContextBean(final ExecutionSemester executionSemester) {
            setExecutionSemester(executionSemester);
        }

        public ExecutionSemester getExecutionSemester() {
            return executionSemester;
        }

        @Override
        public ExecutionSemester getExecutionPeriod() {
            return getExecutionSemester();
        }

        public void setExecutionSemester(ExecutionSemester executionSemester) {
            this.executionSemester = executionSemester;
            this.executionDegree = null;
        }

        public ExecutionDegree getExecutionDegree() {
            return executionDegree;
        }

        public void setExecutionDegree(ExecutionDegree executionDegree) {
            this.executionDegree = executionDegree;
        }

        public EnrolmentPeriodInClasses getEnrolmentPeriodInClasses() {
            if (executionSemester != null && executionDegree != null) {
                for (final EnrolmentPeriod enrolmentPeriod : executionSemester.getEnrolmentPeriodSet()) {
                    if (enrolmentPeriod instanceof EnrolmentPeriodInClasses
                            && executionDegree.getDegreeCurricularPlan() == enrolmentPeriod.getDegreeCurricularPlan()) {
                        return (EnrolmentPeriodInClasses) enrolmentPeriod;
                    }
                }
            }
            return null;
        }

        public EnrolmentPeriodInClassesMobility getEnrolmentPeriodInClassesMobility() {
            if (executionSemester != null && executionDegree != null) {
                for (final EnrolmentPeriod enrolmentPeriod : executionSemester.getEnrolmentPeriodSet()) {
                    if (enrolmentPeriod instanceof EnrolmentPeriodInClassesMobility
                            && executionDegree.getDegreeCurricularPlan() == enrolmentPeriod.getDegreeCurricularPlan()) {
                        return (EnrolmentPeriodInClassesMobility) enrolmentPeriod;
                    }
                }
            }
            return null;
        }

        public OccupationPeriod getLessonPeriod() {
            return getPeriod(OccupationPeriodType.LESSONS, executionSemester);
        }

        public OccupationPeriod getExamPeriod() {
            return getPeriod(OccupationPeriodType.EXAMS, executionSemester);
        }

        public OccupationPeriod getSpecialSeasonExamPeriod() {
            return getPeriod(OccupationPeriodType.EXAMS_SPECIAL_SEASON, null);
        }

        private OccupationPeriod getPeriod(OccupationPeriodType type, ExecutionSemester semester) {
            if (executionSemester != null && executionDegree != null) {
                return executionDegree.getPeriods(type, semester == null ? null : semester.getSemester()).findAny().orElse(null);
            }
            return null;
        }

    }

    public ActionForward firstPage(final ActionMapping mapping, final HttpServletRequest request, final ContextBean contextBean) {
        RenderUtils.invalidateViewState();
        request.setAttribute("contextBean", contextBean);
        return mapping.findForward("firstPage");
    }

    @EntryPoint
    public ActionForward firstPage(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        ContextBean contextBean = getRenderedObject();
        if (contextBean == null) {
            contextBean = new ContextBean();
        }
        return firstPage(mapping, request, contextBean);
    }

}
