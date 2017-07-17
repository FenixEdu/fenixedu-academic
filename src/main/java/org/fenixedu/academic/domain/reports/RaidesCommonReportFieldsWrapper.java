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
package org.fenixedu.academic.domain.reports;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.GrantOwnerType;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.candidacy.IngressionType;
import org.fenixedu.academic.domain.candidacy.PersonalInformationBean;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityAgreement;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.mobility.outbound.OutboundMobilityCandidacySubmission;
import org.fenixedu.academic.domain.raides.DegreeDesignation;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.StudentStatute;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.studentCurriculum.BranchCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.Credits;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.ExtraCurriculumGroup;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class RaidesCommonReportFieldsWrapper {

    public static void createHeaders(final Spreadsheet spreadsheet) {
        spreadsheet.setHeader("ciclo");
        spreadsheet.setHeader("concluído (ano anterior)?");
        spreadsheet.setHeader("média do ciclo");
        spreadsheet.setHeader("Data de conclusão");
        spreadsheet.setHeader("Data de início");
        spreadsheet.setHeader("número aluno");
        spreadsheet.setHeader("nome de utilizador");
        spreadsheet.setHeader("tipo identificação");
        spreadsheet.setHeader("número identificação");
        spreadsheet.setHeader("digitos controlo");
        spreadsheet.setHeader("versão doc identificação");
        spreadsheet.setHeader("nome");
        spreadsheet.setHeader("género");
        spreadsheet.setHeader("data nascimento");
        spreadsheet.setHeader("país nascimento");
        spreadsheet.setHeader("país nacionalidade");
        spreadsheet.setHeader("tipo curso");
        spreadsheet.setHeader("nome curso");
        spreadsheet.setHeader("sigla curso");
        spreadsheet.setHeader("Ramo Principal");
        spreadsheet.setHeader("Ramo Secundáro");
        spreadsheet.setHeader("ano curricular");
        spreadsheet.setHeader("ano ingresso curso actual");
        spreadsheet.setHeader("nº. anos lectivos inscrição curso actual");
        spreadsheet.setHeader("Último ano inscrito neste curso");
        spreadsheet.setHeader("regime frequência curso");
        spreadsheet.setHeader("tipo aluno");
        spreadsheet.setHeader("regime ingresso (código)");
        spreadsheet.setHeader("regime ingresso (designação)");
        spreadsheet.setHeader("estabelecimento do grau preced. (qd aplicável)");
        spreadsheet.setHeader("curso grau preced. (qd aplicável)");
        spreadsheet.setHeader("estabelec. curso habl anterior compl");
        spreadsheet.setHeader("curso habl anterior compl");
        spreadsheet.setHeader("nº inscrições no curso preced.");
        spreadsheet.setHeader("nota ingresso");
        spreadsheet.setHeader("opção ingresso");
        spreadsheet.setHeader("estado civil");
        spreadsheet.setHeader("país residência permanente");
        spreadsheet.setHeader("distrito residência permanente");
        spreadsheet.setHeader("concelho residência permanente");
        spreadsheet.setHeader("deslocado residência permanente");
        spreadsheet.setHeader("nível escolaridade pai");
        spreadsheet.setHeader("nível escolaridade mãe");
        spreadsheet.setHeader("condição perante profissão pai");
        spreadsheet.setHeader("condição perante profissão mãe");
        spreadsheet.setHeader("profissão pai");
        spreadsheet.setHeader("profissão mãe");
        spreadsheet.setHeader("profissão aluno");
        spreadsheet.setHeader("Data preenchimento dados RAIDES");
        spreadsheet.setHeader("estatuto trabalhador estudante introduzido (info. RAIDES)");
        spreadsheet.setHeader("estatuto trabalhador 1º semestre ano (info. oficial)");
        spreadsheet.setHeader("estatuto trabalhador 2º semestre ano (info. oficial)");
        spreadsheet.setHeader("bolseiro (info. RAIDES)");
        spreadsheet.setHeader("instituição que atribuiu a bolsa (qd aplicável)");
        spreadsheet.setHeader("bolseiro (info. oficial)");
        spreadsheet.setHeader("Grau Precedente");
        spreadsheet.setHeader("Outro Grau Precedente");
        spreadsheet.setHeader("grau habl anterior compl");
        spreadsheet.setHeader("Codigo do grau habl anterior");
        spreadsheet.setHeader("Outro grau habl anterior compl");
        spreadsheet.setHeader("país habilitação anterior");
        spreadsheet.setHeader("país habilitação 12º ano ou equivalente");
        spreadsheet.setHeader("ano de conclusão da habilitação anterior");
        spreadsheet.setHeader("nota da habilitação anterior");
        spreadsheet.setHeader("Programa mobilidade");
        spreadsheet.setHeader("País mobilidade");
        spreadsheet.setHeader("Duração programa mobilidade");
        spreadsheet.setHeader("tipo estabelecimento ensino secundário");
        spreadsheet.setHeader("total ECTS inscritos no ano");
        spreadsheet.setHeader("total ECTS concluídos fim ano lectivo anterior");
        spreadsheet.setHeader("nº. disciplinas inscritas ano lectivo anterior dados");
        spreadsheet.setHeader("nº. disciplinas aprovadas ano lectivo anterior dados");
        spreadsheet.setHeader("nº. inscrições externas ano dados");
        spreadsheet.setHeader("estado matrícula ano anterior dados");
        spreadsheet.setHeader("estado matrícula ano dados");
        spreadsheet.setHeader("data do estado de matrícula");
        spreadsheet.setHeader("nº. ECTS 1º ciclo concluídos fim ano lectivo anterior");
        spreadsheet.setHeader("nº. ECTS 2º ciclo concluídos fim ano lectivo anterior");
        spreadsheet.setHeader("nº. ECTS extra 1º ciclo concluídos fim ano lectivo anterior");
        spreadsheet.setHeader("nº. ECTS extracurriculares concluídos fim ano lectivo anterior");
        spreadsheet.setHeader("nº. ECTS Propedeuticas concluídos fim ano lectivo anterior");
        spreadsheet.setHeader("nº. ECTS inscritos em Propedeut e extra-curriculares");
        spreadsheet.setHeader("nº. ECTS equivalência/substituição/dispensa");
        spreadsheet.setHeader("Tem situação de propinas no lectivo dos dados?");
    }

    public static Row reportRaidesFields(final Spreadsheet sheet, final Registration registration,
            StudentCurricularPlan studentCurricularPlan, List<Registration> registrationPath, ExecutionYear executionYear,
            final CycleType cycleType, final boolean concluded,
            final YearMonthDay conclusionDate, BigDecimal average, boolean graduation) {

        final Row row = sheet.addRow();
        final Person graduate = registration.getPerson();
        //List<Registration> registrationPath = getFullRegistrationPath(registration);
        Registration sourceRegistration = registrationPath.iterator().next();
        final PersonalInformationBean personalInformationBean = registration.getPersonalInformationBean(executionYear);

        // Ciclo
        row.setCell(cycleType.getDescription());

        // Concluído
        row.setCell(String.valueOf(concluded));

        // Média do Ciclo
        if (graduation) {
            row.setCell(concluded ? printBigDecimal(average.setScale(0, BigDecimal.ROUND_HALF_EVEN)) : printBigDecimal(average));
        } else {
            row.setCell(concluded ? studentCurricularPlan.getCycle(cycleType).getCurriculum().getRawGrade().getValue() : "n/a");
        }

        // Data de Conclusão
        row.setCell(conclusionDate != null ? conclusionDate.toString("dd-MM-yyyy") : "");

        // Data de Início
        row.setCell(registration.getStartDate() != null ? registration.getStartDate().toString("dd-MM-yyyy") : "");

        // Nº de aluno
        row.setCell(registration.getNumber());

        // Nome de Utilizador
        row.setCell(registration.getPerson().getUsername());

        // Tipo Identificação
        row.setCell(graduate.getIdDocumentType().getLocalizedName());

        // Nº de Identificação
        row.setCell(graduate.getDocumentIdNumber());

        // Dígitos de Controlo
        row.setCell(graduate.getIdentificationDocumentExtraDigitValue());

        // Versão Doc. Identificação
        row.setCell(graduate.getIdentificationDocumentSeriesNumberValue());

        // Nome
        row.setCell(registration.getName());

        // Sexo
        row.setCell(graduate.getGender().toString());

        // Data de Nascimento
        row.setCell(graduate.getDateOfBirthYearMonthDay() != null ? graduate.getDateOfBirthYearMonthDay().toString("dd-MM-yyyy") : "n/a");

        // País de Nascimento
        row.setCell(graduate.getCountryOfBirth() != null ? graduate.getCountryOfBirth().getName() : "n/a");

        // País de Nacionalidade
        row.setCell(graduate.getCountry() != null ? graduate.getCountry().getName() : "n/a");

        // Tipo Curso
        row.setCell(registration.getDegreeType().getName().getContent());

        // Nome Curso
        row.setCell(registration.getDegree().getNameI18N().getContent());

        // Sigla Curso
        row.setCell(registration.getDegree().getSigla());

        // Ramos do currículo do aluno
        final StringBuilder majorBranches = new StringBuilder();
        final StringBuilder minorBranches = new StringBuilder();
        for (final BranchCurriculumGroup group : studentCurricularPlan.getBranchCurriculumGroups()) {
            if (group.isMajor()) {
                majorBranches.append(group.getName().getContent()).append(",");
            } else if (group.isMinor()) {
                minorBranches.append(group.getName().getContent()).append(",");
            }
        }

        // Ramo Principal
        if (majorBranches.length() > 0) {
            row.setCell(majorBranches.deleteCharAt(majorBranches.length() - 1).toString());
        } else {
            row.setCell("");
        }

        // Ramo Secundáro
        if (minorBranches.length() > 0) {
            row.setCell(minorBranches.deleteCharAt(minorBranches.length() - 1).toString());
        } else {
            row.setCell("");
        }

        // Ano Curricular
        row.setCell(registration.getCurricularYear(executionYear));

        // Ano de Ingresso no Curso Actual
        row.setCell(sourceRegistration.getStartExecutionYear().getName());

        // Nº de anos lectivos de inscrição no Curso actual
        int numberOfEnrolmentYears = 0;
        for (Registration current : registrationPath) {
            numberOfEnrolmentYears += current.getNumberOfYearsEnrolledUntil(executionYear);
        }
        row.setCell(numberOfEnrolmentYears);

        // Último ano em que esteve inscrito
        row.setCell(registration.getLastEnrolmentExecutionYear() != null ? registration.getLastEnrolmentExecutionYear().getName() : "");

        // Regime de frequência curso: Tempo integral/Tempo Parcial
        row.setCell(registration.getRegimeType(executionYear) != null ? registration.getRegimeType(executionYear).getName() : "");

        // Tipo de Aluno (AFA, AM, ERASMUS, etc)
        row.setCell(registration.getRegistrationProtocol() != null ? registration.getRegistrationProtocol().getCode() : "");

        // Regime de Ingresso no Curso Actual (código)
        IngressionType ingressionType = sourceRegistration.getIngressionType();
        if (ingressionType == null && sourceRegistration.getStudentCandidacy() != null) {
            ingressionType = sourceRegistration.getStudentCandidacy().getIngressionType();
        }
        row.setCell(ingressionType != null ? ingressionType.getCode() : "");

        // Regime de Ingresso no Curso Actual (designação)
        row.setCell(ingressionType != null ? ingressionType.getDescription().getContent() : "");

        // estabelecimento do grau preced.: Instituição onde esteve
        // inscrito mas não obteve grau, (e.g: transferencias, mudanças de
        // curso...)
        row.setCell(personalInformationBean.getPrecedentInstitution() != null ? personalInformationBean.getPrecedentInstitution()
                .getName() : "");
        // curso grau preced.
        row.setCell(personalInformationBean.getPrecedentDegreeDesignation() != null ? personalInformationBean
                .getPrecedentDegreeDesignation() : "");

        // estabelec. curso habl anterior compl (se o aluno ingressou por uma via
        // diferente CNA, e deve ser IST caso o aluno tenha estado matriculado noutro curso do IST)
        row.setCell(personalInformationBean.getInstitution() != null ? personalInformationBean.getInstitution().getName() : "");

        // curso habl anterior compl (se o aluno ingressou por uma via diferente CNA, e
        // deve ser IST caso o aluno tenha estado matriculado noutro curso do IST)
        row.setCell(personalInformationBean.getDegreeDesignation());

        // nº inscrições no curso preced. (conta uma por cada ano)
        row.setCell(personalInformationBean.getNumberOfPreviousYearEnrolmentsInPrecedentDegree() != null ? personalInformationBean
                .getNumberOfPreviousYearEnrolmentsInPrecedentDegree().toString() : "");

        // Nota de Ingresso
        Double entryGrade = null;
        if (registration.getStudentCandidacy() != null) {
            entryGrade = registration.getStudentCandidacy().getEntryGrade();
        }

        row.setCell(printDouble(entryGrade));

        // Opção de Ingresso
        Integer placingOption = null;
        if (registration.getStudentCandidacy() != null) {
            placingOption = registration.getStudentCandidacy().getPlacingOption();
        }

        row.setCell(placingOption);

        // Estado Civil
        row.setCell(personalInformationBean.getMaritalStatus() != null ? personalInformationBean.getMaritalStatus().toString() : registration
                .getPerson().getMaritalStatus().toString());

        // País de Residência Permanente
        if (personalInformationBean.getCountryOfResidence() != null) {
            row.setCell(personalInformationBean.getCountryOfResidence().getName());
        } else {
            row.setCell(registration.getStudent().getPerson().getCountryOfResidence() != null ? registration.getStudent()
                    .getPerson().getCountryOfResidence().getName() : "");
        }

        // Distrito de Residência Permanente
        if (personalInformationBean.getDistrictSubdivisionOfResidence() != null) {
            row.setCell(personalInformationBean.getDistrictSubdivisionOfResidence().getDistrict().getName());
        } else {
            row.setCell(registration.getStudent().getPerson().getDistrictOfResidence());
        }

        // Concelho de Residência Permanente
        if (personalInformationBean.getDistrictSubdivisionOfResidence() != null) {
            row.setCell(personalInformationBean.getDistrictSubdivisionOfResidence().getName());
        } else {
            row.setCell(registration.getStudent().getPerson().getDistrictSubdivisionOfResidence());
        }

        // Deslocado da Residência Permanente
        if (personalInformationBean.getDislocatedFromPermanentResidence() != null) {
            row.setCell(personalInformationBean.getDislocatedFromPermanentResidence().toString());
        } else {
            row.setCell("");
        }

        // Nível de Escolaridade do Pai
        if (personalInformationBean.getFatherSchoolLevel() != null) {
            row.setCell(personalInformationBean.getFatherSchoolLevel().getName());
        } else {
            row.setCell("");
        }

        // Nível de Escolaridade da Mãe
        if (personalInformationBean.getMotherSchoolLevel() != null) {
            row.setCell(personalInformationBean.getMotherSchoolLevel().getName());
        } else {
            row.setCell("");
        }

        // Condição perante a situação na profissão/Ocupação do
        // Pai
        if (personalInformationBean.getFatherProfessionalCondition() != null) {
            row.setCell(personalInformationBean.getFatherProfessionalCondition().getName());
        } else {
            row.setCell("");
        }

        // Condição perante a situação na profissão/Ocupação da
        // Mãe
        if (personalInformationBean.getMotherProfessionalCondition() != null) {
            row.setCell(personalInformationBean.getMotherProfessionalCondition().getName());
        } else {
            row.setCell("");
        }

        // Profissão do Pai
        if (personalInformationBean.getFatherProfessionType() != null) {
            row.setCell(personalInformationBean.getFatherProfessionType().getName());
        } else {
            row.setCell("");
        }

        // Profissão da Mãe
        if (personalInformationBean.getMotherProfessionType() != null) {
            row.setCell(personalInformationBean.getMotherProfessionType().getName());
        } else {
            row.setCell("");
        }

        // Profissão do Aluno
        if (personalInformationBean.getProfessionType() != null) {
            row.setCell(personalInformationBean.getProfessionType().getName());
        } else {
            row.setCell("");
        }

        // Data preenchimento dados RAIDES
        if (personalInformationBean.getLastModifiedDate() != null) {
            DateTime dateTime = personalInformationBean.getLastModifiedDate();
            row.setCell(dateTime.getYear() + "-" + dateTime.getMonthOfYear() + "-" + dateTime.getDayOfMonth());
        } else {
            row.setCell("");
        }

        // Estatuto de Trabalhador Estudante introduzido pelo aluno
        if (personalInformationBean.getProfessionalCondition() != null) {
            row.setCell(personalInformationBean.getProfessionalCondition().getName());
        } else {
            row.setCell("");
        }

        // Estatuto de Trabalhador Estudante 1º semestre do ano a que se
        // referem
        // os dados
        boolean working1Found = false;
        for (StudentStatute statute : registration.getStudent().getStudentStatutesSet()) {
            if (statute.getType().isWorkingStudentStatute()
                    && statute.isValidInExecutionPeriod(executionYear.getFirstExecutionPeriod())) {
                working1Found = true;
                break;
            }
        }
        row.setCell(String.valueOf(working1Found));

        // Estatuto de Trabalhador Estudante 1º semestre do ano a que se
        // referem
        // os dados
        boolean working2Found = false;
        for (StudentStatute statute : registration.getStudent().getStudentStatutesSet()) {
            if (statute.getType().isWorkingStudentStatute()
                    && statute.isValidInExecutionPeriod(executionYear.getLastExecutionPeriod())) {
                working2Found = true;
                break;
            }
        }
        row.setCell(String.valueOf(working2Found));

        // Bolseiro (info. RAIDES)
        if (personalInformationBean.getGrantOwnerType() != null) {
            row.setCell(personalInformationBean.getGrantOwnerType().getName());
        } else {
            row.setCell("");
        }

        // Instituição que atribuiu a bolsa
        if (personalInformationBean.getGrantOwnerType() != null
                && personalInformationBean.getGrantOwnerType().equals(GrantOwnerType.OTHER_INSTITUTION_GRANT_OWNER)) {
            row.setCell(personalInformationBean.getGrantOwnerProviderName());
        } else {
            row.setCell("");
        }

        // Bolseiro (info. oficial)
        boolean sasFound = false;
        for (StudentStatute statute : registration.getStudent().getStudentStatutesSet()) {
            if (statute.getType().isGrantOwnerStatute()
                    && statute.isValidInExecutionPeriod(executionYear.getFirstExecutionPeriod())) {
                sasFound = true;
                break;
            }
        }
        row.setCell(String.valueOf(sasFound));

        // Grau Precedente
        row.setCell(personalInformationBean.getPrecedentSchoolLevel() != null ? personalInformationBean.getPrecedentSchoolLevel()
                .getName() : "");

        // Outro Grau Precedente
        row.setCell(personalInformationBean.getOtherPrecedentSchoolLevel());

        // grau da habl anterior compl
        row.setCell(personalInformationBean.getSchoolLevel() != null ? personalInformationBean.getSchoolLevel().getName() : "");

        // Codigo do grau habl anterior
        DegreeDesignation designation =
                DegreeDesignation.readByNameAndSchoolLevel(personalInformationBean.getDegreeDesignation(),
                        personalInformationBean.getPrecedentSchoolLevel());
        row.setCell(designation != null ? designation.getDegreeClassification().getCode() : "");

        // Outro grau da habl anterior compl
        row.setCell(personalInformationBean.getOtherSchoolLevel());

        // País de Habilitação Anterior ao Curso Actual
        row.setCell(personalInformationBean.getCountryWhereFinishedPreviousCompleteDegree() != null ? personalInformationBean
                .getCountryWhereFinishedPreviousCompleteDegree().getName() : "");

        // País de Habilitação do 12º ano ou equivalente
        row.setCell(personalInformationBean.getCountryWhereFinishedHighSchoolLevel() != null ? personalInformationBean
                .getCountryWhereFinishedHighSchoolLevel().getName() : "");

        // Ano de conclusão da habilitação anterior
        row.setCell(personalInformationBean.getConclusionYear());

        // Nota de conclusão da habilitação anterior
        row.setCell(personalInformationBean.getConclusionGrade() != null ? personalInformationBean.getConclusionGrade() : "");

        MobilityAgreement mobilityAgreement = null;
        ExecutionInterval chosenCandidacyInterval = null;
        //getting the last mobility program done
        for (OutboundMobilityCandidacySubmission outboundCandidacySubmission : registration
                .getOutboundMobilityCandidacySubmissionSet()) {
            if (outboundCandidacySubmission.getSelectedCandidacy() != null
                    && outboundCandidacySubmission.getSelectedCandidacy().getSelected()) {
                ExecutionInterval candidacyInterval =
                        outboundCandidacySubmission.getOutboundMobilityCandidacyPeriod().getExecutionInterval();
                //the candidacies are made in the previous year
                if (candidacyInterval.getAcademicInterval().isBefore(executionYear.getAcademicInterval())) {
                    if (mobilityAgreement != null) {
                        if (!candidacyInterval.getAcademicInterval().isAfter(chosenCandidacyInterval.getAcademicInterval())) {
                            continue;
                        }
                    }
                    mobilityAgreement =
                            outboundCandidacySubmission.getSelectedCandidacy().getOutboundMobilityCandidacyContest()
                                    .getMobilityAgreement();
                    chosenCandidacyInterval = candidacyInterval;
                }
            }
        }
        // Programa de mobilidade
        row.setCell(mobilityAgreement != null ? mobilityAgreement.getMobilityProgram().getName().getContent() : "");

        // País de mobilidade
        row.setCell(mobilityAgreement != null ? mobilityAgreement.getUniversityUnit().getCountry().getName() : "");

        // Duração do programa de mobilidade
        row.setCell(personalInformationBean.getMobilityProgramDuration() != null ? BundleUtil.getString(Bundle.ENUMERATION,
                personalInformationBean.getMobilityProgramDuration().name()) : "");

        // Tipo de Estabelecimento Frequentado no Ensino Secundário
        if (personalInformationBean.getHighSchoolType() != null) {
            row.setCell(personalInformationBean.getHighSchoolType().getName());
        } else {
            row.setCell("");
        }

        int totalEnrolmentsInPreviousYear = 0;
        int totalEnrolmentsApprovedInPreviousYear = 0;
        //int totalEnrolmentsInFirstSemester = 0;
        double totalEctsConcludedUntilPreviousYear = 0d;
        for (final CycleCurriculumGroup cycleCurriculumGroup : studentCurricularPlan.getInternalCycleCurriculumGrops()) {

            totalEctsConcludedUntilPreviousYear +=
                    cycleCurriculumGroup.getCreditsConcluded(executionYear.getPreviousExecutionYear());

            totalEnrolmentsInPreviousYear +=
                    cycleCurriculumGroup.getEnrolmentsBy(executionYear.getPreviousExecutionYear()).size();

            for (final Enrolment enrolment : cycleCurriculumGroup.getEnrolmentsBy(executionYear.getPreviousExecutionYear())) {
                if (enrolment.isApproved()) {
                    totalEnrolmentsApprovedInPreviousYear++;
                }
            }

            //	    totalEnrolmentsInFirstSemester += cycleCurriculumGroup.getEnrolmentsBy(executionYear.getFirstExecutionPeriod())
            //		    .size();
        }

        // Total de ECTS inscritos no total do ano
        double totalCreditsEnrolled = 0d;
        for (Enrolment enrollment : studentCurricularPlan.getEnrolmentsByExecutionYear(executionYear)) {
            totalCreditsEnrolled += enrollment.getEctsCredits();
        }
        row.setCell(printDouble(totalCreditsEnrolled));

        // Total de ECTS concluídos até ao fim do ano lectivo anterior ao
        // que se
        // referem os dados (neste caso até ao fim de 2007/08) no curso actual
        double totalCreditsDismissed = 0d;
        for (Credits credits : studentCurricularPlan.getCreditsSet()) {
            if (credits.isEquivalence()) {
                totalCreditsDismissed += credits.getEnrolmentsEcts();
            }
        }
        row.setCell(printDouble(totalEctsConcludedUntilPreviousYear));

        // Nº de Disciplinas Inscritos no ano lectivo anterior ao que se
        // referem
        // os dados
        row.setCell(totalEnrolmentsInPreviousYear);

        // Nº de Disciplinas Aprovadas no ano lectivo anterior ao que se
        // referem
        // os dados
        row.setCell(totalEnrolmentsApprovedInPreviousYear);

        // Nº de Inscrições Externas no ano a que se referem os dados
        ExtraCurriculumGroup extraCurriculumGroup = studentCurricularPlan.getExtraCurriculumGroup();
        int extraCurricularEnrolmentsCount =
                extraCurriculumGroup != null ? extraCurriculumGroup.getEnrolmentsBy(executionYear).size() : 0;

        for (final CycleCurriculumGroup cycleCurriculumGroup : studentCurricularPlan.getExternalCurriculumGroups()) {
            extraCurricularEnrolmentsCount += cycleCurriculumGroup.getEnrolmentsBy(executionYear).size();
        }

        if (studentCurricularPlan.hasPropaedeuticsCurriculumGroup()) {
            extraCurricularEnrolmentsCount +=
                    studentCurricularPlan.getPropaedeuticCurriculumGroup().getEnrolmentsBy(executionYear).size();
        }

        row.setCell(extraCurricularEnrolmentsCount);

        // Estados de matrícula
        SortedSet<RegistrationState> states = new TreeSet<RegistrationState>(RegistrationState.DATE_COMPARATOR);
        for (Registration current : registrationPath) {
            states.addAll(current.getRegistrationStatesSet());
        }
        RegistrationState previousYearState = null;
        RegistrationState currentYearState = null;
        for (RegistrationState state : states) {
            if (!state.getStateDate().isAfter(
                    executionYear.getPreviousExecutionYear().getEndDateYearMonthDay().toDateTimeAtMidnight())) {
                previousYearState = state;
            }
            if (!state.getStateDate().isAfter(executionYear.getEndDateYearMonthDay().toDateTimeAtMidnight())) {
                currentYearState = state;
            }
        }

        // Estado da matrícula no ano lectivo anterior ao que se referem os
        // dados
        row.setCell(previousYearState != null ? previousYearState.getStateType().getDescription() : "n/a");

        // Estado (da matrícula) no ano a que se referem os dados
        row.setCell(currentYearState != null ? currentYearState.getStateType().getDescription() : "n/a");

        // Data do estado de matrícula
        row.setCell(currentYearState != null ? currentYearState.getStateDate().toString("dd-MM-yyyy") : "n/a");

        // Nº ECTS do 1º Ciclo concluídos até ao fim do ano lectivo
        // anterior ao que se referem os dados
        final CycleCurriculumGroup firstCycleCurriculumGroup =
                getStudentCurricularPlan(registration, CycleType.FIRST_CYCLE).getCycle(CycleType.FIRST_CYCLE);
        row.setCell(firstCycleCurriculumGroup != null ? printBigDecimal(firstCycleCurriculumGroup.getCurriculum(executionYear)
                .getSumEctsCredits()) : "");

        // Nº ECTS do 2º Ciclo concluídos até ao fim do ano lectivo
        // anterior ao que se referem os dados
        final CycleCurriculumGroup secondCycleCurriculumGroup =
                getStudentCurricularPlan(registration, CycleType.SECOND_CYCLE).getCycle(CycleType.SECOND_CYCLE);
        row.setCell(secondCycleCurriculumGroup != null && !secondCycleCurriculumGroup.isExternal() ? printBigDecimal(secondCycleCurriculumGroup
                .getCurriculum(executionYear).getSumEctsCredits()) : "");

        // Nº ECTS do 2º Ciclo Extra primeiro ciclo concluídos até ao fim do ano
        // lectivo anterior ao que se referem os dados
        Double extraFirstCycleEcts = 0d;
        for (final CycleCurriculumGroup cycleCurriculumGroup : studentCurricularPlan.getExternalCurriculumGroups()) {
            for (final CurriculumLine curriculumLine : cycleCurriculumGroup.getAllCurriculumLines()) {
                if (!curriculumLine.getExecutionYear().isAfter(executionYear.getPreviousExecutionYear())) {
                    extraFirstCycleEcts += curriculumLine.getCreditsConcluded(executionYear.getPreviousExecutionYear());
                }
            }
        }
        row.setCell(printDouble(extraFirstCycleEcts));

        // Nº ECTS Extracurriculares concluídos até ao fim do ano lectivo
        // anterior que ao se referem os dados
        Double extraCurricularEcts = 0d;
        Double allExtraCurricularEcts = 0d;
        if (extraCurriculumGroup != null) {
            for (final CurriculumLine curriculumLine : extraCurriculumGroup.getAllCurriculumLines()) {
                if (curriculumLine.isApproved() && curriculumLine.hasExecutionPeriod()
                        && !curriculumLine.getExecutionYear().isAfter(executionYear.getPreviousExecutionYear())) {
                    extraCurricularEcts += curriculumLine.getEctsCreditsForCurriculum().doubleValue();
                }
                if (curriculumLine.hasExecutionPeriod() && curriculumLine.getExecutionYear() == executionYear) {
                    allExtraCurricularEcts += curriculumLine.getEctsCreditsForCurriculum().doubleValue();
                }
            }
        }
        row.setCell(printDouble(extraCurricularEcts));

        // Nº ECTS Propedeutic concluídos até ao fim do ano lectivo
        // anterior que ao se referem os dados
        Double propaedeuticEcts = 0d;
        Double allPropaedeuticEcts = 0d;
        if (studentCurricularPlan.getPropaedeuticCurriculumGroup() != null) {
            for (final CurriculumLine curriculumLine : studentCurricularPlan.getPropaedeuticCurriculumGroup()
                    .getAllCurriculumLines()) {
                if (curriculumLine.isApproved() && curriculumLine.hasExecutionPeriod()
                        && !curriculumLine.getExecutionYear().isAfter(executionYear.getPreviousExecutionYear())) {
                    propaedeuticEcts += curriculumLine.getEctsCreditsForCurriculum().doubleValue();
                }
                if (curriculumLine.hasExecutionPeriod() && curriculumLine.getExecutionYear() == executionYear) {
                    allPropaedeuticEcts += curriculumLine.getEctsCreditsForCurriculum().doubleValue();
                }
            }
        }
        row.setCell(printDouble(propaedeuticEcts));

        // Nº ECTS inscritos em unidades curriculares propedêuticas e em
        // extra-curriculares
        row.setCell(printDouble(allPropaedeuticEcts + allExtraCurricularEcts));

        // Nº ECTS equivalência/substituição/dispensa
        row.setCell(printDouble(totalCreditsDismissed));

        // Tem situação de propinas no lectivo dos dados
        row.setCell(String.valueOf(studentCurricularPlan.hasAnyGratuityEventFor(executionYear)));

        return row;
    }

    private static String printDouble(Double value) {
        return value == null ? "" : value.toString().replace('.', ',');
    }

    private static String printBigDecimal(BigDecimal value) {
        return value == null ? "" : value.toPlainString().replace('.', ',');
    }

    @Deprecated
    public static StudentCurricularPlan getStudentCurricularPlan(Registration registration, CycleType cycleType) {
        return registration.getStudentCurricularPlan(cycleType);
    }

    public static Set<Registration> getRegistrationsToProcess(final ExecutionYear executionYear, final DegreeType degreeType) {
        final Set<Registration> result = new HashSet<Registration>();

        collectStudentCurricularPlansFor(executionYear, result, degreeType);

        if (executionYear.getPreviousExecutionYear() != null) {
            collectStudentCurricularPlansFor(executionYear.getPreviousExecutionYear(), result, degreeType);
        }

        return result;
    }

    public static void collectStudentCurricularPlansFor(final ExecutionYear executionYear, final Set<Registration> result,
            final DegreeType degreeType) {
        for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesByType(degreeType)) {
            for (StudentCurricularPlan studentCurricularPlan : executionDegree.getDegreeCurricularPlan()
                    .getStudentCurricularPlansSet()) {
                if (!studentCurricularPlan.getStartDateYearMonthDay().isAfter(executionYear.getEndDateYearMonthDay())) {
                    result.add(studentCurricularPlan.getRegistration());
                }
            }
        }
    }

}
