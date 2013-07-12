package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.reports;

import java.io.ByteArrayOutputStream;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.QueueJobResult;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyPersonalDetails;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class ErasmusCandidacyProcessReport extends ErasmusCandidacyProcessReport_Base {

    protected ErasmusCandidacyProcessReport(final MobilityApplicationProcess applicationProcess) {
        super();
        String[] args = new String[0];
        if (applicationProcess == null) {
            throw new DomainException("error.ErasmusCandidacyProcessReport.erasmusCandidacyProcess.null", args);
        }
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

    public IndividualCandidacyDocumentFile getUploadedDocumentByType(Collection<IndividualCandidacyDocumentFile> documentsList,
            IndividualCandidacyDocumentFileType type) {
        for (IndividualCandidacyDocumentFile document : documentsList) {
            if (document.getCandidacyFileType().equals(type)) {
                return document;
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    private Spreadsheet retrieveIndividualProcessesData() {
        final Spreadsheet spreadsheet = new Spreadsheet("Sheet");
        Boolean photo;
        Boolean photocopy;
        Boolean agree;
        Boolean cv;
        Boolean transcript;
        spreadsheet.setHeaders(new String[] { "N.º Processo", "Nome", "Data Nascimento", "Nacionalidade", "Email", "Curso",
                "Data de chegada", "Data de partida", "Estado", "Documentação Entregue Completa", "Foto",
                "Fotocópia do Passaporte ou do Cartão de Identificação", "Acordo", "CV", "Registo Académico" });

        for (IndividualCandidacyProcess individualCandidacyProcess : getMobilityApplicationProcess().getChildProcesses()) {
            photo = false;
            photocopy = false;
            agree = false;
            cv = false;
            transcript = false;
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
            if (erasmusIndividualCandidacyProcess.getPhoto() != null) {
                row.setCell(10, "Sim");
                photo = true;
            } else {
                row.setCell(10, "Não");
            }
            if (getUploadedDocumentByType(erasmusIndividualCandidacyProcess.getCandidacy().getDocuments(),
                    IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION) != null) {
                row.setCell(11, "Sim");
                photocopy = true;
            } else {
                row.setCell(11, "Não");
            }
            if (getUploadedDocumentByType(erasmusIndividualCandidacyProcess.getCandidacy().getDocuments(),
                    IndividualCandidacyDocumentFileType.LEARNING_AGREEMENT) != null) {
                row.setCell(12, "Sim");
                agree = true;
            } else {
                row.setCell(12, "Não");
            }
            if (getUploadedDocumentByType(erasmusIndividualCandidacyProcess.getCandidacy().getDocuments(),
                    IndividualCandidacyDocumentFileType.CV_DOCUMENT) != null) {
                row.setCell(13, "Sim");
                cv = true;
            } else {
                row.setCell(13, "Não");
            }
            if (getUploadedDocumentByType(erasmusIndividualCandidacyProcess.getCandidacy().getDocuments(),
                    IndividualCandidacyDocumentFileType.TRANSCRIPT_OF_RECORDS) != null) {
                row.setCell(14, "Sim");
                transcript = true;
            } else {
                row.setCell(14, "Não");
            }
            if (photo && photocopy && agree && cv && transcript) {
                row.setCell(9, "Sim");
            } else {
                row.setCell(9, "Não");
            }
        }

        return spreadsheet;

    }

    @Atomic
    public static ErasmusCandidacyProcessReport create(final MobilityApplicationProcess applicationProcess) {
        return new ErasmusCandidacyProcessReport(applicationProcess);
    }

    @Override
    public String getFilename() {
        return String.format("%s_%s.xls", getMobilityApplicationProcess().getDisplayName(),
                getRequestDate().toString("dd_MM_yyyy_hh_mm_ss"));
    }

    @Deprecated
    public boolean hasMobilityApplicationProcess() {
        return getMobilityApplicationProcess() != null;
    }

}
