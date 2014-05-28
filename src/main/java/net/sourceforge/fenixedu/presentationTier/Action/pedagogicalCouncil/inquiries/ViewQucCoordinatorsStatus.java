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
package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.inquiries;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.CoordinatorInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryCoordinatorAnswer;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.PedagogicalCouncilApp.PedagogicalControlApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@StrutsFunctionality(app = PedagogicalControlApp.class, path = "view-quc-coordinator-status",
        titleKey = "title.inquiries.coordinators.status", bundle = "InquiriesResources")
@Mapping(path = "/qucCoordinatorsStatus", module = "pedagogicalCouncil")
@Forwards(@Forward(name = "viewQucCoordinatorsState", path = "/pedagogicalCouncil/inquiries/viewQucCoordinatorsStatus.jsp"))
public class ViewQucCoordinatorsStatus extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final CoordinatorInquiryTemplate coordinatorInquiryTemplate =
                CoordinatorInquiryTemplate.getTemplateByExecutionPeriod(ExecutionSemester.readActualExecutionSemester()
                        .getPreviousExecutionPeriod());
        if (coordinatorInquiryTemplate != null) {
            request.setAttribute("coordinatorInquiryOID", coordinatorInquiryTemplate.getExternalId());
        }
        return mapping.findForward("viewQucCoordinatorsState");
    }

    public ActionForward dowloadReport(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final CoordinatorInquiryTemplate coordinatorInquiryTemplate =
                FenixFramework.getDomainObject(getFromRequest(request, "coordinatorInquiryOID").toString());
        final ExecutionSemester executionPeriod = coordinatorInquiryTemplate.getExecutionPeriod();

        final Set<Coordinator> coordinatorsSet = new HashSet<Coordinator>();
        for (ExecutionDegree executionDegree : executionPeriod.getExecutionYear().getExecutionDegrees()) {
            if (executionDegree.hasAnyInquiryResults()) {
                for (Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
                    if (coordinator.getResponsible()
                    // há casos em que a secretária está não só como
                    // coordenadora de curso mas também como responsável...
                            && coordinator.getPerson().hasTeacher()) {
                        InquiryCoordinatorAnswer inquiryCoordinatorAnswer = null;
                        if (coordinatorInquiryTemplate.getShared()) {
                            inquiryCoordinatorAnswer = executionDegree.getInquiryCoordinationAnswers(executionPeriod);
                        } else {
                            inquiryCoordinatorAnswer = coordinator.getInquiryCoordinatorAnswer(executionPeriod);
                        }
                        if (inquiryCoordinatorAnswer == null
                                || inquiryCoordinatorAnswer.hasRequiredQuestionsToAnswer(coordinatorInquiryTemplate)) {
                            coordinatorsSet.add(coordinator);
                        }
                    }
                }
            }
        }

        Spreadsheet spreadsheet = createReport(coordinatorsSet);
        StringBuilder filename = new StringBuilder("Coordenadores_em_falta_");
        filename.append(new DateTime().toString("yyyy_MM_dd_HH_mm"));

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");

        OutputStream outputStream = response.getOutputStream();
        spreadsheet.exportToXLSSheet(outputStream);
        outputStream.flush();
        outputStream.close();
        return null;
    }

    private Spreadsheet createReport(Set<Coordinator> coordinatorsSet) throws IOException {
        Spreadsheet spreadsheet = new Spreadsheet("Coordenadores em falta");
        spreadsheet.setHeader("Tipo Curso");
        spreadsheet.setHeader("Nome Curso");
        spreadsheet.setHeader("Coordenador");
        spreadsheet.setHeader("ISTid");
        spreadsheet.setHeader("Telefone");
        spreadsheet.setHeader("Email");

        for (Coordinator coordinator : coordinatorsSet) {
            Row row = spreadsheet.addRow();
            row.setCell(coordinator.getExecutionDegree().getDegreeType().getFilteredName());
            row.setCell(coordinator.getExecutionDegree().getDegree().getNameI18N().toString());
            row.setCell(coordinator.getPerson().getName());
            row.setCell(coordinator.getPerson().getUsername());
            row.setCell(coordinator.getPerson().getDefaultMobilePhoneNumber());
            row.setCell(coordinator.getPerson().getDefaultEmailAddressValue());
        }

        return spreadsheet;
    }
}
