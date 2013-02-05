package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.reports;

import java.io.ByteArrayOutputStream;

import net.sourceforge.fenixedu.domain.QueueJobResult;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyPersonalDetails;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class ErasmusCandidacyProcessReport extends ErasmusCandidacyProcessReport_Base {

    protected ErasmusCandidacyProcessReport(final MobilityApplicationProcess applicationProcess) {
        super();
        check(applicationProcess, "error.ErasmusCandidacyProcessReport.erasmusCandidacyProcess.null", new String[0]);
        setMobilityApplicationProcess(applicationProcess);
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
        spreadsheet.setHeaders(new String[] { "N� Processo", "Nome", "Data Nascimento", "Nacionalidade", "Email", "Curso",
                "Data de chegada", "Data de partida", "Estado" });

        for (IndividualCandidacyProcess individualCandidacyProcess : getMobilityApplicationProcess().getChildProcesses()) {
            MobilityIndividualApplicationProcess erasmusIndividualCandidacyProcess =
                    (MobilityIndividualApplicationProcess) individualCandidacyProcess;
            if (individualCandidacyProcess.isCandidacyCancelled()) {
                continue;
            }

            if (individualCandidacyProcess.isCandidacyRejected()) {
                continue;
            }

            IndividualCandidacyPersonalDetails personalDetails = erasmusIndividualCandidacyProcess.getPersonalDetails();

            Row row = spreadsheet.addRow();

            row.setCell(0, erasmusIndividualCandidacyProcess.getProcessCode());
            row.setCell(1, erasmusIndividualCandidacyProcess.getPersonalDetails().getName());
            row.setCell(2, personalDetails.getDateOfBirthYearMonthDay() != null ? personalDetails.getDateOfBirthYearMonthDay()
                    .toString("dd/MM/yyyy") : "N/A");
            row.setCell(3, erasmusIndividualCandidacyProcess.getPersonalDetails().getCountry().getCountryNationality()
                    .getContent());
            row.setCell(4, erasmusIndividualCandidacyProcess.getCandidacyHashCode().getEmail());
            row.setCell(5, erasmusIndividualCandidacyProcess.getCandidacy().getSelectedDegree().getNameI18N().getContent());
            row.setCell(
                    6,
                    erasmusIndividualCandidacyProcess.getCandidacy().getMobilityStudentData().getDateOfArrival()
                            .toString("dd/MM/yyyy"));
            row.setCell(7, erasmusIndividualCandidacyProcess.getCandidacy().getMobilityStudentData().getDateOfDeparture()
                    .toString("dd/MM/yyyy"));
            row.setCell(8, erasmusIndividualCandidacyProcess.getErasmusCandidacyStateDescription());
        }

        return spreadsheet;

    }

    @Service
    public static ErasmusCandidacyProcessReport create(final MobilityApplicationProcess applicationProcess) {
        return new ErasmusCandidacyProcessReport(applicationProcess);
    }

    @Override
    public String getFilename() {
        return String.format("%s_%s.xls", getMobilityApplicationProcess().getDisplayName(),
                getRequestDate().toString("dd_MM_yyyy_hh_mm_ss"));
    }
}
