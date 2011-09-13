package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.reports;

import java.io.ByteArrayOutputStream;

import net.sourceforge.fenixedu.domain.QueueJobResult;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcess;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class ErasmusCandidacyProcessReport extends ErasmusCandidacyProcessReport_Base {

    protected ErasmusCandidacyProcessReport(final ErasmusCandidacyProcess candidacyProcess) {
	super();
	check(candidacyProcess, "error.ErasmusCandidacyProcessReport.erasmusCandidacyProcess.null", new String[0]);
	setErasmusCandidacyProcess(candidacyProcess);
    }

    @Override
    public QueueJobResult execute() throws Exception {
	Spreadsheet spreasheet = retrieveIndividualProcessesData();
	ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();

	spreasheet.exportToXLSSheet(byteArrayOS);
	byteArrayOS.close();

	final QueueJobResult queueJobResult = new QueueJobResult();
	queueJobResult.setContentType("application/excel");
	queueJobResult.setContent(byteArrayOS.toByteArray());

	return queueJobResult;
    }

    private Spreadsheet retrieveIndividualProcessesData() {
	final Spreadsheet spreadsheet = new Spreadsheet("Sheet");
	spreadsheet.setHeaders(new String[] { "Nº Processo", "Nome", "Data Nascimento", "Nacionalidade", "Email", "Curso",
		"Data de chegada", "Data de partida", "Estado" });

	for (IndividualCandidacyProcess individualCandidacyProcess : getErasmusCandidacyProcess().getChildProcesses()) {
	    ErasmusIndividualCandidacyProcess erasmusIndividualCandidacyProcess = (ErasmusIndividualCandidacyProcess) individualCandidacyProcess;
	    if (individualCandidacyProcess.isCandidacyCancelled()) {
		continue;
	    }

	    if (individualCandidacyProcess.isCandidacyRejected()) {
		continue;
	    }

	    Row row = spreadsheet.addRow();

	    row.setCell(0, erasmusIndividualCandidacyProcess.getProcessCode());
	    row.setCell(1, erasmusIndividualCandidacyProcess.getPersonalDetails().getName());
	    row.setCell(2, erasmusIndividualCandidacyProcess.getPersonalDetails().getDateOfBirthYearMonthDay().toString(
		    "dd/MM/yyyy"));
	    row.setCell(3, erasmusIndividualCandidacyProcess.getPersonalDetails().getCountry().getCountryNationality()
		    .getContent());
	    row.setCell(4, erasmusIndividualCandidacyProcess.getCandidacyHashCode().getEmail());
	    row.setCell(5, erasmusIndividualCandidacyProcess.getCandidacy().getSelectedDegree().getNameI18N().getContent());
	    row.setCell(6, erasmusIndividualCandidacyProcess.getCandidacy().getErasmusStudentData().getDateOfArrival().toString(
		    "dd/MM/yyyy"));
	    row.setCell(7, erasmusIndividualCandidacyProcess.getCandidacy().getErasmusStudentData().getDateOfDeparture()
		    .toString("dd/MM/yyyy"));
	    row.setCell(8, erasmusIndividualCandidacyProcess.getErasmusCandidacyStateDescription());
	}

	return spreadsheet;

    }

    @Service
    public static ErasmusCandidacyProcessReport create(final ErasmusCandidacyProcess candidacyProcess) {
	return new ErasmusCandidacyProcessReport(candidacyProcess);
    }

    @Override
    public String getFilename() {
	return String.format("%s_%s.xls", getErasmusCandidacyProcess().getDisplayName(), getRequestDate().toString(
		"dd_MM_yyyy_hh_mm_ss"));
    }
}
