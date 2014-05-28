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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.report.GratuityReportingService;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = DirectiveCouncilApplication.class, path = "gratuity-reports",
        titleKey = "label.directiveCouncil.gratuityReports")
@Mapping(path = "/gratuityReports", module = "directiveCouncil")
@Forwards(@Forward(name = "showReport", path = "/directiveCouncil/gratuityReports/showReport.jsp"))
public class GratuityReportsDA extends FenixDispatchAction {

    public static class GratuityReportParametersBean implements Serializable {

        private static final long serialVersionUID = 1L;

        private ExecutionYear executionYear;

        private LocalDate startDate;

        private LocalDate endDate;

        private Boolean detailed;

        private List<DegreeType> degreeTypes;

        public GratuityReportParametersBean(ExecutionYear executionYear) {
            setExecutionYear(executionYear);
            setDetailed(false);
            this.degreeTypes = new ArrayList<DegreeType>();
        }

        public ExecutionYear getExecutionYear() {
            return this.executionYear;
        }

        public void setExecutionYear(ExecutionYear executionYear) {
            this.executionYear = executionYear;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public Boolean getDetailed() {
            return detailed;
        }

        public void setDetailed(Boolean detailed) {
            this.detailed = detailed;
        }

        public List<DegreeType> getDegreeTypes() {
            return degreeTypes;
        }

        public void setDegreeTypes(List<DegreeType> degreeTypes) {
            this.degreeTypes = degreeTypes;
        }

    }

    @EntryPoint
    public ActionForward showReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final GratuityReportParametersBean parametersBean =
                getGratuityReportParameters() != null ? getGratuityReportParameters() : new GratuityReportParametersBean(
                        ExecutionYear.readCurrentExecutionYear());

        request.setAttribute("reportParameters", parametersBean);
        request.setAttribute(
                "report",
                new GratuityReportingService().createGratuityReport(parametersBean.getExecutionYear(),
                        parametersBean.getStartDate(), parametersBean.getEndDate(), parametersBean.getDegreeTypes()));

        return mapping.findForward("showReport");
    }

    public ActionForward showReportInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("reportParameters", getRenderedObject());

        final GratuityReportParametersBean parametersBean =
                getGratuityReportParameters() != null ? getGratuityReportParameters() : new GratuityReportParametersBean(
                        ExecutionYear.readCurrentExecutionYear());

        request.setAttribute(
                "report",
                new GratuityReportingService().createGratuityReport(parametersBean.getExecutionYear(),
                        parametersBean.getStartDate(), parametersBean.getEndDate(), parametersBean.getDegreeTypes()));

        return mapping.findForward("showReport");
    }

    private GratuityReportParametersBean getGratuityReportParameters() {
        return getRenderedObject("reportParameters");
    }

}
