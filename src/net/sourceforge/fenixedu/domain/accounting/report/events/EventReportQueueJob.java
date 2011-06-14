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
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class EventReportQueueJob extends EventReportQueueJob_Base {

    private static final List<String> EVENTS = Arrays.asList(new String[] { "4578436156582", "4578436156381", "6201933792470",
	    "6201933792469" });

    public EventReportQueueJob() {
	super();
    }

    @Override
    public String getFilename() {
	return "propinas";
    }

    @Override
    public QueueJobResult execute() throws Exception {
	ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();

	List<Spreadsheet> buildReport = buildReport();

	for (Spreadsheet spreadsheet : buildReport) {
	    spreadsheet.exportToCSV(byteArrayOS, ";");
	}

	byteArrayOS.close();

	final QueueJobResult queueJobResult = new QueueJobResult();
	queueJobResult.setContentType("text/csv");
	queueJobResult.setContent(byteArrayOS.toByteArray());

	System.out.println("Job " + getFilename() + " completed");

	return queueJobResult;
    }

    private List<Spreadsheet> buildReport() {
	final Spreadsheet eventsSheet = allEvents();
	final Spreadsheet exemptionsSheet = allExemptions();
	final Spreadsheet transactionsSheet = allTransactions();

	return Arrays.asList(new Spreadsheet[] { eventsSheet, exemptionsSheet, transactionsSheet });
    }

    /* ALL EVENTS */
    private Spreadsheet allEvents() {
	final Spreadsheet spreadsheet = new Spreadsheet(getFilename());

	defineHeadersForEvents(spreadsheet);

	System.out.println("Events");
	for (Event event : getAccountingEvents()) {
	    /*
	     * if (!EVENTS.contains(event.getExternalId())) { continue; }
	     */

	    try {
		writeEvent(event, spreadsheet);
	    } catch (Exception e) {
		System.out.println("Oppps on event -> " + event.getExternalId() + " .... Show must go on...");
	    }
	}
	return spreadsheet;
    }

    private List<Event> getAccountingEvents() {
	List<Event> result = new ArrayList<Event>();

	int count = 0;
	for (Event event : RootDomainObject.getInstance().getAccountingEvents()) {
	    Wrapper wrapper = buildWrapper(event);

	    if (wrapper == null) {
		continue;
	    }

	    result.add(event);

	    if (++count > 30) {
		break;
	    }
	}

	return result;
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

	row.setCell(event.getDescription().toString());
	row.setCell(event.getWhenOccured().toString("dd/MM/yyyy"));
	row.setCell(event.getTotalAmountToPay().toPlainString());
	row.setCell(event.getPayedAmount().toPlainString());
	row.setCell(event.getAmountToPay().toPlainString());
	row.setCell(event.getReimbursableAmount().toPlainString());
	row.setCell(event.getTotalDiscount().toPlainString());
    }

    private void defineHeadersForEvents(final Spreadsheet spreadsheet) {
	// Academical information
	spreadsheet.setHeader("Identificador");
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
	spreadsheet.setHeader("Residência - Ano");
	spreadsheet.setHeader("Residência - Mês");

	// Event information
	spreadsheet.setHeader("Tipo de divida");
	spreadsheet.setHeader("Data de criação");
	spreadsheet.setHeader("Valor Total");
	spreadsheet.setHeader("Valor Pago");
	spreadsheet.setHeader("Valor em divida");
	spreadsheet.setHeader("Valor Reembolsável");
	spreadsheet.setHeader("Desconto");

    }

    /* ALL EXEMPTIONS */
    private Spreadsheet allExemptions() {
	final Spreadsheet spreadsheet = new Spreadsheet(getFilename());

	defineHeadersForExemptions(spreadsheet);

	int count = 0;
	System.out.println("Exemptions");
	for (Event event : getAccountingEvents()) {
	    /*
	     * if (!EVENTS.contains(event.getExternalId())) { continue; }
	     */

	    try {
		writeExemptionInformation(event, spreadsheet);
	    } catch (Exception e) {
		System.out.println("Oppps on event -> " + event.getExternalId() + " .... Show must go on...");
	    }
	}
	return spreadsheet;

    }

    // write Exemption Information
    private void writeExemptionInformation(Event event, Spreadsheet spreadsheet) {
	Set<Exemption> exemptionsSet = event.getExemptionsSet();

	for (Exemption exemption : exemptionsSet) {
	    ExemptionWrapper wrapper = new ExemptionWrapper(exemption);
	    Row row = spreadsheet.addRow();

	    row.setCell(event.getExternalId());
	    row.setCell(wrapper.getExemptionTypeDescription());
	    row.setCell(wrapper.getExemptionValue());
	    row.setCell(wrapper.getPercentage());
	    row.setCell(wrapper.getJustification());
	}
    }

    private void defineHeadersForExemptions(final Spreadsheet spreadsheet) {
	spreadsheet.setHeader("Identificador");
	spreadsheet.setHeader("Tipo da Isenção");
	spreadsheet.setHeader("Valor da Isenção");
	spreadsheet.setHeader("Percentagem da Isenção");
	spreadsheet.setHeader("Motivo da Isenção");
    }

    /* ALL TRANSACTIONS */

    private Spreadsheet allTransactions() {
	final Spreadsheet spreadsheet = new Spreadsheet(getFilename());

	defineHeadersForTransactions(spreadsheet);

	int count = 0;
	System.out.println("Transactions");
	for (Event event : getAccountingEvents()) {
	    /*
	     * if (!EVENTS.contains(event.getExternalId())) { continue; }
	     */

	    try {
		writeTransactionInformation(event, spreadsheet);
	    } catch (Exception e) {
		System.out.println("Oppps on event -> " + event.getExternalId() + " .... Show must go on...");
	    }
	}
	return spreadsheet;

    }

    private void writeTransactionInformation(Event event, Spreadsheet spreadsheet) {
	for (AccountingTransaction transaction : event.getNonAdjustingTransactions()) {
	    for (AccountingTransaction adjustment : transaction.getAdjustmentTransactions()) {
		Row row = spreadsheet.addRow();

		row.setCell(event.getExternalId());
		row.setCell(transaction.getWhenRegistered().toString("dd-MM-yyyy"));
		row.setCell(transaction.getOriginalAmount().toPlainString());
		row.setCell(transaction.getAmountWithAdjustment().toPlainString());
		row.setCell(transaction.getPaymentMode().getLocalizedName());
		row.setCell(adjustment.getWhenRegistered().toString("dd-MM-yyyy"));
		row.setCell(adjustment.getAmountWithAdjustment().toPlainString());
		row.setCell(adjustment.getComments());
	    }

	    if (transaction.getAdjustmentTransactions().isEmpty()) {
		Row row = spreadsheet.addRow();

		row.setCell(event.getExternalId());
		row.setCell(transaction.getWhenRegistered().toString("dd-MM-yyyy"));
		row.setCell(transaction.getOriginalAmount().toPlainString());
		row.setCell(transaction.getAmountWithAdjustment().toPlainString());
		row.setCell(transaction.getPaymentMode().getLocalizedName());
		row.setCell("-");
		row.setCell("-");
		row.setCell("-");
	    }
	}
    }

    private void defineHeadersForTransactions(final Spreadsheet spreadsheet) {
	// Transaction Information
	spreadsheet.setHeader("Identificador");
	spreadsheet.setHeader("Data de entrada do pagamento");
	spreadsheet.setHeader("Montante inicial");
	spreadsheet.setHeader("Montante ajustado");
	spreadsheet.setHeader("Modo de pagamento");
	spreadsheet.setHeader("Data de entrada do ajuste");
	spreadsheet.setHeader("Montante do ajuste");
	spreadsheet.setHeader("Justificação");
    }

    // Residence

    private Wrapper buildWrapper(Event event) {
	if (event.isGratuity()) {
	    return new GratuityEventWrapper((GratuityEvent) event);
	} else if (event.isAcademicServiceRequestEvent()) {
	    // return new
	    // AcademicServiceRequestEventWrapper((AcademicServiceRequestEvent)
	    // event);
	} else if (event.isIndividualCandidacyEvent()) {
	    // return new
	    // IndividualCandidacyEventWrapper((IndividualCandidacyEvent)
	    // event);
	} else if (event.isPhdEvent()) {
	    // return new PhdEventWrapper((PhdEvent) event);
	} else {
	    // return new EventWrapper(event);
	    return null;
	}

	return null;
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
