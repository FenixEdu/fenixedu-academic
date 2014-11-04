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
package org.fenixedu.academic.ui.struts.action.directiveCouncil;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.interfaces.HasDegreeType;
import org.fenixedu.academic.domain.interfaces.HasExecutionYear;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = DirectiveCouncilApplication.class, path = "student-statistics", titleKey = "link.statistics.students")
@Mapping(path = "/studentStatistics", module = "directiveCouncil")
@Forwards(@Forward(name = "show.student.statistics", path = "/directiveCouncil/showStudentStatistics.jsp"))
public class StudentStatisticsDA extends FenixDispatchAction {

    public static class ContextBean implements Serializable, HasExecutionYear, HasDegreeType {
        private DegreeType degreeType;
        private ExecutionYear executionYearReference;
        private ExecutionDegree executionDegreeReference;

        @Override
        public DegreeType getDegreeType() {
            return degreeType;
        }

        public void setDegreeType(DegreeType degreeType) {
            this.degreeType = degreeType;
        }

        @Override
        public ExecutionYear getExecutionYear() {
            return executionYearReference;
        }

        public void setExecutionYear(final ExecutionYear executionYear) {
            executionYearReference = executionYear;
        }

        public ExecutionDegree getExecutionDegree() {
            return executionDegreeReference;
        }

        public void setExecutionDegree(final ExecutionDegree executionDegree) {
            executionDegreeReference = executionDegree;
        }

        public ExecutionYear getExecutionYearFourYearsBack() {
            final ExecutionYear executionYear = getExecutionYear();
            return executionYear == null ? null : getExecutionYearFourYearsBack(executionYear);
        }

        public static ExecutionYear getExecutionYearFourYearsBack(final ExecutionYear executionYear) {
            ExecutionYear executionYearFourYearsBack = executionYear;
            if (executionYear != null) {
                for (int i = 5; i > 1; i--) {
                    final ExecutionYear previousExecutionYear = executionYearFourYearsBack.getPreviousExecutionYear();
                    if (previousExecutionYear != null) {
                        executionYearFourYearsBack = previousExecutionYear;
                    }
                }
            }
            return executionYearFourYearsBack;
        }
    }

    public static class StatisticsBean implements Serializable {

        private final ContextBean contextBean;

        public StatisticsBean(final ContextBean contextBean) {
            this.contextBean = contextBean;
        }

        public int getNumberOfRegisteredStudents() {
            final ExecutionDegree executionDegree = contextBean.getExecutionDegree();
            int counter = 0;
            if (executionDegree == null) {
                final ExecutionYear executionYear = contextBean.getExecutionYear();
                final DegreeType degreeType = contextBean.getDegreeType();
                for (final Degree degree : rootDomainObject.getDegreesSet()) {
                    if (degree.getDegreeType() == degreeType) {
                        counter += countDegreeRegistrations(executionYear, degree);
                    }
                }
            } else {
                final Degree degree = executionDegree.getDegree();
                counter = countDegreeRegistrations(executionDegree.getExecutionYear(), degree);
            }
            return counter;
        }

        private int countDegreeRegistrations(final ExecutionYear executionYear, final Degree degree) {
            int counter = 0;
            for (final Registration registration : degree.getRegistrationsSet()) {
                if (registration.isRegistered(executionYear)) {
                    counter++;
                }
            }
            return counter;
        }

        public Boolean getShowResult() {
            return (contextBean.getDegreeType() != null && contextBean.getExecutionYear() != null)
                    || contextBean.getExecutionDegree() != null;
        }
    }

    @EntryPoint
    public ActionForward showStatistics(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ContextBean contextBean = getRenderedObject();
        if (contextBean == null) {
            contextBean = new ContextBean();
            contextBean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("contextBean", contextBean);
        request.setAttribute("statisticsBean", new StatisticsBean(contextBean));

        return mapping.findForward("show.student.statistics");
    }

}
