package net.sourceforge.fenixedu.domain.accounting.report.events;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.QueueJobResult;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.IndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestEvent;
import net.sourceforge.fenixedu.domain.phd.debts.PhdEvent;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class EventReportQueueJob extends EventReportQueueJob_Base {
    
    private static final List<String> EVENTS = Arrays.asList(new String[] { "4578436156582", "4578436156381", "6201933792470",
	    "6201933792469" });

    public  EventReportQueueJob() {
        super();
    }

    @Override
    public QueueJobResult execute() throws Exception {
	ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();

	buildReport().exportToCSV(byteArrayOS, ";");

	byteArrayOS.close();

	final QueueJobResult queueJobResult = new QueueJobResult();
	queueJobResult.setContentType("text/csv");
	queueJobResult.setContent(byteArrayOS.toByteArray());

	System.out.println("Job " + getFilename() + " completed");

	return queueJobResult;
    }
    
    
    private Spreadsheet buildReport() {
	final Spreadsheet spreadsheet = new Spreadsheet(getFilename());

	defineHeaders(spreadsheet);

	int count = 0;
	for (Event event : RootDomainObject.getInstance().getAccountingEvents()) {
	    if (++count % 100 == 0) {
		System.out.println("Read " + count + " students");
	    }
	    
	    /*
	     * if (!EVENTS.contains(event.getExternalId())) { continue; }
	     */

	    try {
		writeEvent(event, spreadsheet);
	    } catch(Exception e) {
		System.out.println("Oppps on event -> " + event.getExternalId() + " .... Show must go on...");

		// e.printStackTrace();
	    }

	    if (count > 30) {
		break;
	    }
	}

	return spreadsheet;
    }

    private void defineHeaders(final Spreadsheet spreadsheet) {
	// Academical information
	spreadsheet.setHeader("EVENT EXTERNAL ID");
	spreadsheet.setHeader("Aluno");
	spreadsheet.setHeader("Nome");
	spreadsheet.setHeader("Data inscrição");
	spreadsheet.setHeader("Ano lectivo");
	spreadsheet.setHeader("Tipo de matricula");
	spreadsheet.setHeader("Nome do Curso");
	spreadsheet.setHeader("Tipo de curso");
	spreadsheet.setHeader("Programa doutoral");
	spreadsheet.setHeader("ECTS inscritos");
	spreadsheet.setHeader("Regime");
	spreadsheet.setHeader("Modelo de inscrição");
	
	// Residence Information
	spreadsheet.setHeader("Ano");
	spreadsheet.setHeader("Mes");

	// Event information
	spreadsheet.setHeader("Tipo de divida");
	spreadsheet.setHeader("Data de criação");
	spreadsheet.setHeader("Valor Total");
	spreadsheet.setHeader("Valor Pago");
	spreadsheet.setHeader("Valor em divida");
	spreadsheet.setHeader("Valor Reembolsável");
	spreadsheet.setHeader("Tipo da Isenção");
	spreadsheet.setHeader("Valor da Isenção");
	spreadsheet.setHeader("Percentagem da Isenção");
	spreadsheet.setHeader("Motivo da Isenção");

	// Transaction Information
	spreadsheet.setHeader("Data de entrada do pagamento");
	spreadsheet.setHeader("Montante inicial");
	spreadsheet.setHeader("Montante ajustado");
	spreadsheet.setHeader("Modo de pagamento");
	spreadsheet.setHeader("Data de entrada do ajuste");
	spreadsheet.setHeader("Montante do ajuste");
	spreadsheet.setHeader("Justificação");

	// Standalone degree enrolment information
	spreadsheet.setHeader("Disciplina");
	spreadsheet.setHeader("Curso");
	spreadsheet.setHeader("ECTS");

    }

    private void writeEvent(final Event event, final Spreadsheet spreadsheet) {
	Row row = spreadsheet.addRow();

	Wrapper wrapper = buildWrapper(event);

	row.setCell(event.getExternalId());
	row.setCell(wrapper.getStudentNumber());
	row.setCell(wrapper.getStudentName());
	row.setCell(wrapper.getRegistrationStartDate());
	row.setCell(wrapper.getExecutionYear());
	row.setCell(wrapper.getStudiesType());
	row.setCell(wrapper.getDegreeName());
	row.setCell(wrapper.getDegreeType());
	row.setCell(wrapper.getPhdProgramName());
	row.setCell(wrapper.getEnrolledECTS());
	row.setCell(wrapper.getRegime());
	row.setCell(wrapper.getEnrolmentModel());
	row.setCell(wrapper.getResidenceYear());
	row.setCell(wrapper.getResidenceMonth());

	writeEventInformation(event, row, spreadsheet);

    }

    // Residence

    private Wrapper buildWrapper(Event event) {
	if (event.isGratuity()) {
	    return new GratuityEventWrapper((GratuityEvent) event);
	} else if (event.isAcademicServiceRequestEvent()) {
	    return new AcademicServiceRequestEventWrapper((AcademicServiceRequestEvent) event);
	} else if(event.isIndividualCandidacyEvent()) {
	    return new IndividualCandidacyEventWrapper((IndividualCandidacyEvent) event);
	} else if (event.isPhdEvent()) {
	    return new PhdEventWrapper((PhdEvent) event);
	} else {
	    return new EventWrapper(event);
	}
    }

    // write Event Information
    private void writeEventInformation(Event event, Row row, Spreadsheet spreadsheet) {
	Row currentRow = row;
	
	row.setCell(event.getDescription().toString());
	row.setCell(event.getWhenOccured().toString("dd/MM/yyyy"));
	row.setCell(event.getTotalAmountToPay().toPlainString());
	row.setCell(event.getPayedAmount().toPlainString());
	row.setCell(event.getAmountToPay().toPlainString());
	row.setCell(event.getReimbursableAmount().toPlainString());

	Set<Exemption> exemptionsSet = event.getExemptionsSet();
	
	for (Exemption exemption : exemptionsSet) {
	    Row exemptionRow = spreadsheet.addRow();

	    copyRowCells(row, exemptionRow);
	    fillExemptionInformation(exemptionRow, exemption);
	    
	    currentRow = exemptionRow;
	}
	
	if (exemptionsSet.isEmpty()) {
	    currentRow.setCell("");
	    currentRow.setCell("");
	    currentRow.setCell("");
	    currentRow.setCell("");
	}
	
	for (AccountingTransaction transaction : event.getNonAdjustingTransactions()) {
	    Row transactionRow = spreadsheet.addRow();
	    copyRowCells(currentRow, transactionRow);

	    fillTransactionInformation(spreadsheet, transactionRow, transaction);
	}
    }

    private void fillTransactionInformation(Spreadsheet spreasheet, Row row, AccountingTransaction transaction) {
	row.setCell(transaction.getWhenRegistered().toString("dd-MM-yyyy"));
	row.setCell(transaction.getOriginalAmount().toPlainString());
	row.setCell(transaction.getAmountWithAdjustment().toPlainString());
	row.setCell(transaction.getPaymentMode().getLocalizedName());

	for (AccountingTransaction adjustment : transaction.getAdjustmentTransactions()) {
	    Row adjustmentRow = spreasheet.addRow();

	    copyRowCells(row, adjustmentRow);

	    adjustmentRow.setCell(adjustment.getWhenRegistered().toString("dd-MM-yyyy"));
	    adjustmentRow.setCell(adjustment.getAmountWithAdjustment().toPlainString());
	    adjustmentRow.setCell(adjustment.getComments());
	}

	if (transaction.getAdjustmentTransactions().isEmpty()) {
	    row.setCell("");
	    row.setCell("");
	    row.setCell("");
	}
	
    }

    private void fillExemptionInformation(final Row row, final Exemption exemption) {
	ExemptionWrapper wrapper = new ExemptionWrapper(exemption);

	row.setCell(wrapper.getExemptionTypeDescription());
	row.setCell(wrapper.getExemptionValue());
	row.setCell(wrapper.getPercentage());
	row.setCell(wrapper.getJustification());
    }

    private void copyRowCells(Row from, Row to) {
	List<Object> cells = from.getCells();

	for (Object cell : cells) {
	    to.setCell((String) cell);
	}
    }

    public static List<EventReportQueueJob> retrieveAllGeneratedReports() {
	List<EventReportQueueJob> reports = new ArrayList<EventReportQueueJob>();

	CollectionUtils.select(RootDomainObject.getInstance().getQueueJobSet(), new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		if (!(arg0 instanceof EventReportQueueJob)) {
		    return false;
		}

		EventReportQueueJob eventReportQueueJob = (EventReportQueueJob) arg0;

		return eventReportQueueJob.getDone();
	    }

	}, reports);

	return reports;
    }

}
