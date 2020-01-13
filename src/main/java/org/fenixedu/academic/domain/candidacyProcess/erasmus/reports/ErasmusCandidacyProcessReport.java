/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.candidacyProcess.erasmus.reports;

import java.io.ByteArrayOutputStream;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.QueueJobResult;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyPersonalDetails;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;

import pt.ist.fenixframework.Atomic;

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
        return documentsList.stream().filter(document -> document.getCandidacyFileType() == type).findFirst().orElse(null);
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
        spreadsheet.setHeaders("N.º Processo", "IST ID", "Nome", "Genero", "Data Nascimento", "Nacionalidade", "Universidade",
                "País da Universidade", "Programa", "Email", "Curso", "Data de chegada", "Data de partida", "Estado",
                "Documentação Entregue Completa", "Foto", "Fotocópia do Passaporte ou do Cartão de Identificação", "Acordo", "CV",
                "Registo Académico", "Nível Inglês","N Identificaçaõ");

        for (IndividualCandidacyProcess individualCandidacyProcess : getMobilityApplicationProcess().getChildProcessesSet()) {
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
            if (personalDetails.getPerson() != null && personalDetails.getPerson().getUsername() != null) {
                row.setCell(1, personalDetails.getPerson().getUsername());
            } else {
                row.setCell(1, "");
            }
            row.setCell(2, erasmusIndividualCandidacyProcess.getPersonalDetails().getName());
            row.setCell(3, personalDetails.getGender().toLocalizedString());
            row.setCell(4, personalDetails.getDateOfBirthYearMonthDay() != null ? personalDetails.getDateOfBirthYearMonthDay()
                    .toString("dd/MM/yyyy") : "N/A");
            row.setCell(5,
                    erasmusIndividualCandidacyProcess.getPersonalDetails().getCountry().getCountryNationality().getContent());

            row.setCell(6, erasmusIndividualCandidacyProcess.getCandidacy().getMobilityStudentData().getSelectedOpening()
                    .getMobilityAgreement().getUniversityUnit().getName());
            row.setCell(7, erasmusIndividualCandidacyProcess.getCandidacy().getMobilityStudentData().getSelectedOpening()
                    .getMobilityAgreement().getUniversityUnit().getCountry().getName());
            row.setCell(8, erasmusIndividualCandidacyProcess.getMobilityProgram().getRegistrationProtocol().getDescription()
                    .getContent());
            row.setCell(9, getEmail(erasmusIndividualCandidacyProcess));
            row.setCell(10, erasmusIndividualCandidacyProcess.getCandidacy().getSelectedDegree().getNameI18N().getContent());
            row.setCell(11, erasmusIndividualCandidacyProcess.getCandidacy().getMobilityStudentData().getDateOfArrival()
                    .toString("dd/MM/yyyy"));
            row.setCell(12, erasmusIndividualCandidacyProcess.getCandidacy().getMobilityStudentData().getDateOfDeparture()
                    .toString("dd/MM/yyyy"));
            row.setCell(13, erasmusIndividualCandidacyProcess.getErasmusCandidacyStateDescription());
            row.setCell(21,erasmusIndividualCandidacyProcess.getPersonalDetails().getDocumentIdNumber());
            if (erasmusIndividualCandidacyProcess.getPhoto() != null) {
                row.setCell(15, "Sim");
                photo = true;
            } else {
                row.setCell(15, "Não");
            }
            if (getUploadedDocumentByType(erasmusIndividualCandidacyProcess.getCandidacy().getDocumentsSet(),
                    IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION) != null) {
                row.setCell(16, "Sim");
                photocopy = true;
            } else {
                row.setCell(16, "Não");
            }
            if (getUploadedDocumentByType(erasmusIndividualCandidacyProcess.getCandidacy().getDocumentsSet(),
                    IndividualCandidacyDocumentFileType.LEARNING_AGREEMENT) != null) {
                row.setCell(17, "Sim");
                agree = true;
            } else {
                row.setCell(17, "Não");
            }
            if (getUploadedDocumentByType(erasmusIndividualCandidacyProcess.getCandidacy().getDocumentsSet(),
                    IndividualCandidacyDocumentFileType.CV_DOCUMENT) != null) {
                row.setCell(18, "Sim");
                cv = true;
            } else {
                row.setCell(18, "Não");
            }
            if (getUploadedDocumentByType(erasmusIndividualCandidacyProcess.getCandidacy().getDocumentsSet(),
                    IndividualCandidacyDocumentFileType.TRANSCRIPT_OF_RECORDS) != null) {
                row.setCell(19, "Sim");
                transcript = true;
            } else {
                row.setCell(19, "Não");
            }
            if (getUploadedDocumentByType(erasmusIndividualCandidacyProcess.getCandidacy().getDocumentsSet(),
                    IndividualCandidacyDocumentFileType.ENGLISH_LEVEL_DECLARATION) != null) {
                row.setCell(20, "Sim");
                englishLevel = true;
            } else {
                row.setCell(20, "Não");
            }
            if (photo && photocopy && agree && cv && transcript && englishLevel) {
                row.setCell(14, "Sim");
            } else {
                row.setCell(14, "Não");
            }
        }

        return spreadsheet;

    }

    private String getEmail(MobilityIndividualApplicationProcess process) {
        if (process.getCandidacyHashCode() != null) {
            return process.getCandidacyHashCode().getEmail();
        }
        if (process.getCandidacy() != null) {
            if (process.getCandidacy().getPersonalDetails() != null) {
                return process.getCandidacy().getPersonalDetails().getEmail();
            }
        }
        return StringUtils.EMPTY;
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

}