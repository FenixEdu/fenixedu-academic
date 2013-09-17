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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@Mapping(path = "/qucCoordinatorsStatus", module = "pedagogicalCouncil")
@Forwards({ @Forward(name = "viewQucCoordinatorsState", path = "/pedagogicalCouncil/inquiries/viewQucCoordinatorsStatus.jsp",
        tileProperties = @Tile(title = "private.pedagogiccouncil.control.coordinatorsstatusresponse")) })
public class ViewQucCoordinatorsStatus extends FenixDispatchAction {

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
