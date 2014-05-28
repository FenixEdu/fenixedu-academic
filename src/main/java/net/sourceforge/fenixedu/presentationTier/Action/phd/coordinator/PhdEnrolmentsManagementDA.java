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
package net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.phd.ManageEnrolmentsBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.CoordinatorApplication.CoordinatorPhdApp;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;
import net.sourceforge.fenixedu.presentationTier.renderers.degreeStructure.DegreeCurricularPlanRendererConfig;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;

@StrutsFunctionality(app = CoordinatorPhdApp.class, path = "enrolments-management",
        titleKey = "label.externalUnits.externalEnrolments", bundle = "AcademicAdminOffice")
@Mapping(path = "/phdEnrolmentsManagement", module = "coordinator")
@Forwards({ @Forward(name = "showPhdProgram", path = "/phd/coordinator/enrolments/showPhdProgram.jsp"),
        @Forward(name = "showEnrolments", path = "/phd/coordinator/enrolments/showEnrolments.jsp") })
public class PhdEnrolmentsManagementDA extends PhdProcessDA {

    private Set<PhdProgram> getManagedPhdPrograms(HttpServletRequest request) {
        final Set<PhdProgram> result = new HashSet<PhdProgram>();
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        for (final Coordinator coordinator : getLoggedPerson(request).getCoordinatorsSet()) {
            if (coordinator.getExecutionDegree().getDegree().hasPhdProgram()
                    && coordinator.getExecutionDegree().getExecutionYear() == currentExecutionYear) {
                result.add(coordinator.getExecutionDegree().getDegree().getPhdProgram());
            }
        }
        return result;
    }

    @EntryPoint
    public ActionForward showPhdProgram(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgram program = getDomainObject(request, "phdProgramOid");
        if (program != null) {
            initPhdProgramConfig(request, program);
            return mapping.findForward("showPhdProgram");
        }

        final Set<PhdProgram> phdPrograms = getManagedPhdPrograms(request);

        if (phdPrograms.size() == 1) {
            initPhdProgramConfig(request, phdPrograms.iterator().next());
            return mapping.findForward("showPhdProgram");
        }

        request.setAttribute("phdPrograms", phdPrograms);
        return mapping.findForward("showPhdProgram");
    }

    private void initPhdProgramConfig(HttpServletRequest request, PhdProgram program) {

        final DegreeCurricularPlanRendererConfig config = new DegreeCurricularPlanRendererConfig();

        config.setDegreeCurricularPlan(program.getDegree().getLastActiveDegreeCurricularPlan());
        config.setExecutionInterval(ExecutionYear.readCurrentExecutionYear());
        config.setOrganizeBy(DegreeCurricularPlanRendererConfig.OrganizeType.GROUPS);
        config.setViewCurricularCourseUrl(getClass().getAnnotation(Mapping.class).path() + ".do");
        config.addViewCurricularCourseUrlParameter("method", "manageEnrolments");

        request.setAttribute("rendererConfig", config);
        request.setAttribute("phdProgram", program);
    }

    public ActionForward changeDegreeCurricularPlanConfig(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final DegreeCurricularPlanRendererConfig config = getRenderedObject("rendererConfig");

        request.setAttribute("rendererConfig", config);
        request.setAttribute("phdProgram", config.getDegreeCurricularPlan().getDegree().getPhdProgram());

        return mapping.findForward("showPhdProgram");
    }

    public ActionForward exportEnrolmentsToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        final ExecutionSemester semester = getDomainObject(request, "executionSemesterOid");
        final CurricularCourse curricularCourse = getDomainObject(request, "degreeModuleOid");

        byte[] content = buildSpreadsheet(curricularCourse, semester);
        writeFile(response, getFileName(curricularCourse, semester), "application/vnd.ms-excel", content);

        return null;
    }

    private byte[] buildSpreadsheet(CurricularCourse curricularCourse, ExecutionSemester semester) throws IOException {

        final List<Enrolment> enrolments = curricularCourse.getEnrolmentsByAcademicInterval(semester.getAcademicInterval());

        // sort by person name
        Collections.sort(enrolments, new Comparator<Enrolment>() {
            @Override
            public int compare(Enrolment o1, Enrolment o2) {
                return o1.getPerson().getName().compareTo(o2.getPerson().getName());
            }
        });

        final SpreadsheetBuilder builder = new SpreadsheetBuilder();
        builder.addSheet(semester.getQualifiedName().replace("/", "_"), new SheetData<Enrolment>(enrolments) {

            @Override
            protected void makeLine(final Enrolment enrolment) {
                // TODO: add to phd resource bundle
                addCell(getMessageFromResource("label.phd.number"), enrolment.getRegistration().getNumber());
                addCell(getMessageFromResource("label.phd.name"), enrolment.getPerson().getName());
                addCell(getMessageFromResource("label.phd.email"), enrolment.getPerson()
                        .getInstitutionalOrDefaultEmailAddressValue());
            }
        });

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        builder.build(WorkbookExportFormat.EXCEL, output);
        return output.toByteArray();
    }

    private String getFileName(final CurricularCourse curricularCourse, final ExecutionSemester semester) {
        return curricularCourse.getName(semester).replace(" ", "_") + ".xls";
    }

    public ActionForward manageEnrolments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        ManageEnrolmentsBean bean = getRenderedObject("manageEnrolmentsBean");
        if (bean == null) {
            bean = new ManageEnrolmentsBean();
            bean.setSemester(ExecutionSemester.readActualExecutionSemester());
            bean.setCurricularCourse((CurricularCourse) getDomainObject(request, "degreeModuleOid"));
        }

        filterEnrolments(bean);
        request.setAttribute("manageEnrolmentsBean", bean);

        return mapping.findForward("showEnrolments");
    }

    private void filterEnrolments(final ManageEnrolmentsBean bean) {
        bean.setRemainingEnrolments(bean.getCurricularCourse().getEnrolmentsByAcademicInterval(
                bean.getSemester().getAcademicInterval()));
    }

}