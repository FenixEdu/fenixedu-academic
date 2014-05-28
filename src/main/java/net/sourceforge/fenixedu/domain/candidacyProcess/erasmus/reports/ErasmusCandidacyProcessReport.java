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
        Boolean englishLevel;
        spreadsheet.setHeaders(new String[] { "N.º Processo", "Nome", "Data Nascimento", "Nacionalidade", "Universidade",
                "Programa", "Email", "Curso", "Data de chegada", "Data de partida", "Estado", "Documentação Entregue Completa",
                "Foto", "Fotocópia do Passaporte ou do Cartão de Identificação", "Acordo", "CV", "Registo Académico",
                "Nível Inglês" });

        for (IndividualCandidacyProcess individualCandidacyProcess : getMobilityApplicationProcess().getChildProcesses()) {
            photo = false;
            photocopy = false;
            agree = false;
            cv = false;
            transcript = false;
            englishLevel = false;
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
            row.setCell(4, erasmusIndividualCandidacyProcess.getCandidacy().getMobilityStudentData().getSelectedOpening()
                    .getMobilityAgreement().getUniversityUnit().getName());
            row.setCell(5, erasmusIndividualCandidacyProcess.getMobilityProgram().getRegistrationAgreement().getDescription());
            row.setCell(6, erasmusIndividualCandidacyProcess.getCandidacyHashCode().getEmail());
            row.setCell(7, erasmusIndividualCandidacyProcess.getCandidacy().getSelectedDegree().getNameI18N().getContent());
            row.setCell(
                    8,
                    erasmusIndividualCandidacyProcess.getCandidacy().getMobilityStudentData().getDateOfArrival()
                            .toString("dd/MM/yyyy"));
            row.setCell(9, erasmusIndividualCandidacyProcess.getCandidacy().getMobilityStudentData().getDateOfDeparture()
                    .toString("dd/MM/yyyy"));
            row.setCell(10, erasmusIndividualCandidacyProcess.getErasmusCandidacyStateDescription());
            if (erasmusIndividualCandidacyProcess.getPhoto() != null) {
                row.setCell(12, "Sim");
                photo = true;
            } else {
                row.setCell(12, "Não");
            }
            if (getUploadedDocumentByType(erasmusIndividualCandidacyProcess.getCandidacy().getDocuments(),
                    IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION) != null) {
                row.setCell(13, "Sim");
                photocopy = true;
            } else {
                row.setCell(13, "Não");
            }
            if (getUploadedDocumentByType(erasmusIndividualCandidacyProcess.getCandidacy().getDocuments(),
                    IndividualCandidacyDocumentFileType.LEARNING_AGREEMENT) != null) {
                row.setCell(14, "Sim");
                agree = true;
            } else {
                row.setCell(14, "Não");
            }
            if (getUploadedDocumentByType(erasmusIndividualCandidacyProcess.getCandidacy().getDocuments(),
                    IndividualCandidacyDocumentFileType.CV_DOCUMENT) != null) {
                row.setCell(15, "Sim");
                cv = true;
            } else {
                row.setCell(15, "Não");
            }
            if (getUploadedDocumentByType(erasmusIndividualCandidacyProcess.getCandidacy().getDocuments(),
                    IndividualCandidacyDocumentFileType.TRANSCRIPT_OF_RECORDS) != null) {
                row.setCell(16, "Sim");
                transcript = true;
            } else {
                row.setCell(16, "Não");
            }
            if (getUploadedDocumentByType(erasmusIndividualCandidacyProcess.getCandidacy().getDocuments(),
                    IndividualCandidacyDocumentFileType.ENGLISH_LEVEL_DECLARATION) != null) {
                row.setCell(17, "Sim");
                englishLevel = true;
            } else {
                row.setCell(17, "Não");
            }
            if (photo && photocopy && agree && cv && transcript && englishLevel) {
                row.setCell(11, "Sim");
            } else {
                row.setCell(11, "Não");
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
