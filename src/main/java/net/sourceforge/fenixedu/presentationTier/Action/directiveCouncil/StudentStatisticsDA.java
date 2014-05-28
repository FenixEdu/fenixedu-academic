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
package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.interfaces.HasDegreeType;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.gep.ReportsByDegreeTypeDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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
            return executionYear == null ? null : ReportsByDegreeTypeDA.getExecutionYearFourYearsBack(executionYear);
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
