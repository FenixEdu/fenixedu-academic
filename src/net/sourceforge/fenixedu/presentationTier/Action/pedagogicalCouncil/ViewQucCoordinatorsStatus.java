package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

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
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@Mapping(path = "/qucCoordinatorsStatus", module = "pedagogicalCouncil")
@Forwards( { @Forward(name = "viewQucCoordinatorsState", path = "/pedagogicalCouncil/inquiries/viewQucCoordinatorsStatus.jsp") })
public class ViewQucCoordinatorsStatus extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final CoordinatorInquiryTemplate coordinatorInquiryTemplate = CoordinatorInquiryTemplate
		.getTemplateByExecutionPeriod(ExecutionSemester.readActualExecutionSemester().getPreviousExecutionPeriod());
	if (coordinatorInquiryTemplate != null) {
	    request.setAttribute("coordinatorInquiryOID", coordinatorInquiryTemplate.getExternalId());
	}
	return mapping.findForward("viewQucCoordinatorsState");
    }

    public ActionForward dowloadReport(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final CoordinatorInquiryTemplate coordinatorInquiryTemplate = AbstractDomainObject.fromExternalId(getFromRequest(request,
		"coordinatorInquiryOID").toString());
	final ExecutionSemester executionPeriod = coordinatorInquiryTemplate.getExecutionPeriod();

	final Map<Coordinator, String> coordinatorsMap = new HashMap<Coordinator, String>();
	for (ExecutionDegree executionDegree : executionPeriod.getExecutionYear().getExecutionDegrees()) {
	    if (executionDegree.getDegree().isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree()) {
		for (Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
		    if (coordinator.getResponsible()) {
			InquiryCoordinatorAnswer inquiryCoordinatorAnswer = coordinator
				.getInquiryCoordinatorAnswer(executionPeriod);
			if (inquiryCoordinatorAnswer == null || !inquiryCoordinatorAnswer.hasAnyQuestionAnswers()) {
			    coordinatorsMap.put(coordinator, executionDegree.getDegreeType().getFilteredName() + " "
				    + executionDegree.getDegree().getNameI18N().toString());
			}
		    }
		}
	    }
	}

	Spreadsheet spreadsheet = createReport(coordinatorsMap);
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

    private Spreadsheet createReport(Map<Coordinator, String> coordinatorsMap) throws IOException {
	Spreadsheet spreadsheet = new Spreadsheet("Coordenadores em falta");
	spreadsheet.setHeader("Curso");
	spreadsheet.setHeader("Coordenador");
	spreadsheet.setHeader("ISTid");
	spreadsheet.setHeader("Telefone");
	spreadsheet.setHeader("Email");

	for (Coordinator coordinator : coordinatorsMap.keySet()) {
	    Row row = spreadsheet.addRow();
	    row.setCell(coordinator.getExecutionDegree().getDegree().getNameI18N().toString());
	    row.setCell(coordinator.getPerson().getName());
	    row.setCell(coordinator.getPerson().getUsername());
	    row.setCell(coordinator.getPerson().getDefaultMobilePhoneNumber());
	    row.setCell(coordinator.getPerson().getDefaultEmailAddressValue());
	}

	return spreadsheet;
    }
}
