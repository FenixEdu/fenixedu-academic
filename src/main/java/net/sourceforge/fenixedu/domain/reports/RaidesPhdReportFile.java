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
package net.sourceforge.fenixedu.domain.reports;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacy.PersonalInformationBean;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.phd.ExternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;
import net.sourceforge.fenixedu.domain.studentCurriculum.Credits;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class RaidesPhdReportFile extends RaidesPhdReportFile_Base {

    private static final Logger logger = LoggerFactory.getLogger(RaidesPhdReportFile.class);

    public RaidesPhdReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem RAIDES - PHD";
    }

    @Override
    protected String getPrefix() {
        return "phdRAIDES";
    }

    @Override
    public DegreeType getDegreeType() {
        return DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA;
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {

        ExecutionYear executionYear = getExecutionYear();
        int civilYear = executionYear.getBeginCivilYear();
        fillSpreadsheet(spreadsheet);

        logger.info("BEGIN report for " + getDegreeType().name());

        List<PhdIndividualProgramProcess> retrieveProcesses = retrieveProcesses(executionYear);

        for (PhdIndividualProgramProcess phdIndividualProgramProcess : retrieveProcesses) {
            if (phdIndividualProgramProcess.isConcluded()) {
                LocalDate conclusionDate = phdIndividualProgramProcess.getThesisProcess().getConclusionDate();

                if (conclusionDate.getYear() != civilYear && conclusionDate.getYear() != civilYear - 1
                        && conclusionDate.getYear() != civilYear + 1) {
                    continue;
                }
            }
            if (phdIndividualProgramProcess.isConcluded() || phdIndividualProgramProcess.getHasStartedStudies()) {

                reportRaidesGraduate(spreadsheet, phdIndividualProgramProcess, executionYear);
            }
        }
    }

    private List<PhdIndividualProgramProcess> retrieveProcesses(ExecutionYear executionYear) {
        List<PhdIndividualProgramProcess> phdIndividualProgramProcessList = new ArrayList<PhdIndividualProgramProcess>();

        for (PhdProgram program : Bennu.getInstance().getPhdProgramsSet()) {
            phdIndividualProgramProcessList.addAll(program.getIndividualProgramProcesses());
        }

        return phdIndividualProgramProcessList;
    }

    private Set<StudentCurricularPlan> getStudentCurricularPlansToProcess(ExecutionYear executionYear) {
        final Set<StudentCurricularPlan> result = new HashSet<StudentCurricularPlan>();

        collectStudentCurricularPlansFor(executionYear, result);

        if (executionYear.getPreviousExecutionYear() != null) {
            collectStudentCurricularPlansFor(executionYear.getPreviousExecutionYear(), result);
        }

        return result;
    }

    private void collectStudentCurricularPlansFor(final ExecutionYear executionYear, final Set<StudentCurricularPlan> result) {
        for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesByType(this.getDegreeType())) {
            result.addAll(executionDegree.getDegreeCurricularPlan().getStudentCurricularPlans());
        }
    }

    private String getReportName(final String prefix, final ExecutionYear executionYear) {

        final StringBuilder result = new StringBuilder();
        result.append(new LocalDateTime().toString("yyyyMMddHHmm"));
        result.append("_").append(prefix).append("_").append(executionYear.getName().replace('/', '_'));
        return result.toString();
    }

    private void fillSpreadsheet(Spreadsheet spreadsheet) {
        spreadsheet.setHeader("Matriculado de acordo com o plano de estudos?");
        spreadsheet.setHeader("ciclo");
        spreadsheet.setHeader("concluído (ano anterior)?");
        spreadsheet.setHeader("média do ciclo");
        spreadsheet.setHeader("Data de conclusão");
        spreadsheet.setHeader("Data de Início");
        spreadsheet.setHeader("número aluno");
        spreadsheet.setHeader("tipo identificação");
        spreadsheet.setHeader("número identificação");
        spreadsheet.setHeader("digitos controlo");
        spreadsheet.setHeader("versão doc identificação");
        spreadsheet.setHeader("nome");
        spreadsheet.setHeader("género");
        spreadsheet.setHeader("data nascimento");
        spreadsheet.setHeader("país nascimento");
        spreadsheet.setHeader("país nacionalidade");
        spreadsheet.setHeader("sigla programa doutoral");
        spreadsheet.setHeader("programa doutoral");
        spreadsheet.setHeader("tipo curso");
        spreadsheet.setHeader("nome curso");
        spreadsheet.setHeader("sigla curso");
        spreadsheet.setHeader("ramo");
        spreadsheet.setHeader("nº. anos lectivos inscrição curso actual");
        spreadsheet.setHeader("Último ano inscrito neste curso");
        spreadsheet.setHeader("estabelecimento habl anterior compl");
        spreadsheet.setHeader("curso habl anterior compl");
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
        spreadsheet.setHeader("bolseiro (info. RAIDES)");
        spreadsheet.setHeader("bolseiro (info. oficial)");
        spreadsheet.setHeader("Grau Precedente");
        spreadsheet.setHeader("grau habl anterior compl");
        spreadsheet.setHeader("outro grau habl anterior compl");
        spreadsheet.setHeader("país habilitação anterior");
        spreadsheet.setHeader("país habilitação 12º ano ou equivalente");
        spreadsheet.setHeader("ano de conclusão da habilitação anterior");
        spreadsheet.setHeader("nota da habilitação anterior");
        spreadsheet.setHeader("Nº inscrições no curso preced.");
        spreadsheet.setHeader("Duração programa mobilidade");
        spreadsheet.setHeader("tipo estabelecimento ensino secundário");
        spreadsheet.setHeader("total ECTS inscritos no ano");
        spreadsheet.setHeader("total ECTS concluídos fim ano lectivo anterior (1º Semestre do ano lectivo actual)");
        spreadsheet.setHeader("total ECTS equivalência/substituição/dispensa");
        spreadsheet.setHeader("total ECTS necessários para a conclusão");
        spreadsheet.setHeader("doutoramento: inscrito parte curricular");
        spreadsheet.setHeader("nº doutoramento");
        spreadsheet.setHeader("istId orientadores");
        /* Início - Alterações Ticket 366904*/

        spreadsheet.setHeader("istId co-orientadores");
        spreadsheet.setHeader("Nome do Orientador Externo");
        spreadsheet.setHeader("Nome do Co-Orientador Externo");

        /* Fim - Alterações Ticket 366904*/
        spreadsheet.setHeader("estado processo doutoramento");
        spreadsheet.setHeader("Ambito");
        spreadsheet.setHeader("data de candidatura");
        spreadsheet.setHeader("data de homologação");
        spreadsheet.setHeader("data de inicio dos estudos");
        spreadsheet.setHeader("data e hora da prova");
        spreadsheet.setHeader("tipo de acordo");

        // Alterações Ticket 366904

        spreadsheet.setHeader("Data de Apresentação Pública da CAT");
    }

    private void reportRaidesGraduate(Spreadsheet spreadsheet, PhdIndividualProgramProcess process, ExecutionYear executionYear) {
        final Row row = spreadsheet.addRow();
        final Person graduate = process.getPerson();
        final PersonalInformationBean personalInformationBean = process.getPersonalInformationBean(executionYear);
        final Registration registration = process.getRegistration();
        final boolean concluded = process.isConcluded();
        final LocalDate conclusionDate = process.getConclusionDate();

        if (registration != null && !registration.isBolonha()) {
            return;
        }

        YearMonthDay registrationConclusionDate =
                registration != null ? registration.getLastStudentCurricularPlan().getCycle(CycleType.THIRD_CYCLE)
                        .getConclusionDate() : null;

        if (registration != null && registrationConclusionDate == null) {
            registrationConclusionDate =
                    registration.getLastStudentCurricularPlan().calculateConclusionDate(CycleType.THIRD_CYCLE);
        }

        row.setCell(String.valueOf(registration != null && !registration.isCanceled()));

        // Ciclo
        row.setCell(CycleType.THIRD_CYCLE.getDescription());

        // Concluído
        row.setCell(String.valueOf(process.isConcluded()));

        // Média do Ciclo
        String grade = concluded ? process.getFinalGrade().getLocalizedName() : "n/a";
        if (concluded && registration != null && registration.isConcluded()) {
            grade +=
                    " "
                            + registration.getLastStudentCurricularPlan().getCycle(CycleType.THIRD_CYCLE)
                                    .getCurriculum(registrationConclusionDate.toDateTimeAtMidnight()).getAverage()
                                    .toPlainString();
        }
        row.setCell(grade);

        // Data de conclusão
        row.setCell(conclusionDate != null ? conclusionDate.toString("dd-MM-yyyy") : "");

        // Data de Início
        row.setCell(process.getCandidacyDate().toString("dd-MM-yyyy"));

        // Nº de aluno
        row.setCell(process.getStudent().getNumber());

        // Tipo Identificação
        row.setCell(graduate.getIdDocumentType().getLocalizedName());

        // Nº de Identificação
        row.setCell(graduate.getDocumentIdNumber());

        // Dígitos de Controlo
        row.setCell(graduate.getIdentificationDocumentExtraDigitValue());

        // Versão Doc. Identificação
        row.setCell(graduate.getIdentificationDocumentSeriesNumberValue());

        // Nome
        row.setCell(registration != null ? registration.getName() : process.hasPerson() ? process.getPerson().getName() : "n/a");

        // Sexo
        row.setCell(graduate.getGender().toString());

        // Data de Nascimento
        row.setCell(graduate.getDateOfBirthYearMonthDay() != null ? graduate.getDateOfBirthYearMonthDay().toString("dd-MM-yyyy") : "n/a");

        // País de Nascimento
        row.setCell(graduate.getCountryOfBirth() != null ? graduate.getCountryOfBirth().getName() : "n/a");

        // País de Nacionalidade
        row.setCell(graduate.getCountry() != null ? graduate.getCountry().getName() : "n/a");

        // Sigla programa doutoral
        row.setCell(process.getPhdProgram().getAcronym());

        // Programa doutoral
        row.setCell(process.getPhdProgram().getName().getContent());

        // Tipo Curso
        row.setCell(registration != null ? registration.getDegreeType().getLocalizedName() : "n/a");

        // Nome Curso
        row.setCell(registration != null ? registration.getDegree().getNameI18N().getContent() : "n/a");

        // Sigla Curso
        row.setCell(registration != null ? registration.getDegree().getSigla() : "n/a");

        // Ramo (caso se aplique)
        row.setCell("não determinável");

        if (registration != null) {
            // Nº de anos lectivos de inscrição no Curso actual
            row.setCell(calculateNumberOfEnrolmentYears(registration, executionYear));

            // Último ano em que esteve inscrito
            row.setCell(registration.getLastEnrolmentExecutionYear() != null ? registration.getLastEnrolmentExecutionYear()
                    .getName() : "");
        } else {
            row.setCell("n/a");
            row.setCell("n/a");
        }

        // estabelecimento do habl anterior compl (se o aluno ingressou por uma via
        // diferente CNA, e deve ser IST caso o aluno tenha estado matriculado noutro curso do IST)
        row.setCell(personalInformationBean.getInstitution() != null ? personalInformationBean.getInstitution().getName() : "");

        // curso habl anterior compl (se o aluno ingressou por uma via diferente CNA, e
        // deve ser IST caso o aluno tenha estado matriculado noutro curso do IST)
        row.setCell(personalInformationBean.getDegreeDesignation());

        // Estado Civil
        row.setCell(personalInformationBean.getMaritalStatus() != null ? personalInformationBean.getMaritalStatus().toString() : process
                .getPerson().getMaritalStatus().toString());

        // País de Residência Permanente
        if (personalInformationBean.getCountryOfResidence() != null) {
            row.setCell(personalInformationBean.getCountryOfResidence().getName());
        } else {
            row.setCell(process.getStudent().getPerson().getCountryOfResidence() != null ? process.getStudent().getPerson()
                    .getCountryOfResidence().getName() : "");
        }

        // Distrito de Residência Permanente
        if (personalInformationBean.getDistrictSubdivisionOfResidence() != null) {
            row.setCell(personalInformationBean.getDistrictSubdivisionOfResidence().getDistrict().getName());
        } else {
            row.setCell(process.getStudent().getPerson().getDistrictOfResidence());
        }

        // Concelho de Residência Permanente
        if (personalInformationBean.getDistrictSubdivisionOfResidence() != null) {
            row.setCell(personalInformationBean.getDistrictSubdivisionOfResidence().getName());
        } else {
            row.setCell(process.getStudent().getPerson().getDistrictSubdivisionOfResidence());
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

        // Condição perante a situação na profissão/Ocupação do Pai
        if (personalInformationBean.getFatherProfessionalCondition() != null) {
            row.setCell(personalInformationBean.getFatherProfessionalCondition().getName());
        } else {
            row.setCell("");
        }

        // Condição perante a situação na profissão/Ocupação da Mãe
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

        // Bolseiro (info. RAIDES)
        if (personalInformationBean.getGrantOwnerType() != null) {
            row.setCell(personalInformationBean.getGrantOwnerType().getName());
        } else {
            row.setCell("");
        }

        // Bolseiro (info. oficial)
        boolean sasFound = false;
        for (StudentStatute statute : process.getStudent().getStudentStatutes()) {
            if (statute.getStatuteType() == StudentStatuteType.SAS_GRANT_OWNER
                    && statute.isValidInExecutionPeriod(executionYear.getFirstExecutionPeriod())) {
                sasFound = true;
                break;
            }
        }
        row.setCell(String.valueOf(sasFound));

        // Grau Precedente
        row.setCell(personalInformationBean.getPrecedentSchoolLevel() != null ? personalInformationBean.getPrecedentSchoolLevel()
                .getName() : "");

        // grau habl anterior compl
        row.setCell(personalInformationBean.getSchoolLevel() != null ? personalInformationBean.getSchoolLevel().getName() : "");

        // outro grau habl anterior compl
        row.setCell(personalInformationBean.getOtherSchoolLevel() != null ? personalInformationBean.getOtherSchoolLevel() : "");

        // País de Habilitação Anterior Completa
        row.setCell(personalInformationBean.getCountryWhereFinishedPreviousCompleteDegree() != null ? personalInformationBean
                .getCountryWhereFinishedPreviousCompleteDegree().getName() : "");

        // País de Habilitação do 12º ano ou equivalente
        row.setCell(personalInformationBean.getCountryWhereFinishedHighSchoolLevel() != null ? personalInformationBean
                .getCountryWhereFinishedHighSchoolLevel().getName() : "");

        // Ano de conclusão da habilitação anterior completa
        row.setCell(personalInformationBean.getConclusionYear());

        // Nota de conclusão da habilitação anterior completa
        row.setCell(personalInformationBean.getConclusionGrade() != null ? personalInformationBean.getConclusionGrade() : "");

        // Nº inscrições no curso preced. (conta uma por cada ano)
        row.setCell(personalInformationBean.getNumberOfPreviousYearEnrolmentsInPrecedentDegree() != null ? personalInformationBean
                .getNumberOfPreviousYearEnrolmentsInPrecedentDegree().toString() : "");

        // Duração do programa de mobilidade
        row.setCell(personalInformationBean.getMobilityProgramDuration() != null ? BundleUtil.getString(Bundle.ENUMERATION,
                personalInformationBean.getMobilityProgramDuration().name()) : "");

        // Tipo de Estabelecimento Frequentado no Ensino Secundário
        if (personalInformationBean.getHighSchoolType() != null) {
            row.setCell(personalInformationBean.getHighSchoolType().getName());
        } else {
            row.setCell("");
        }

        double totalEctsConcludedUntilPreviousYear = 0d;
        if (registration != null) {

            // Total de ECTS inscritos no total do ano
            double totalCreditsEnrolled = 0d;
            for (Enrolment enrollment : registration.getLastStudentCurricularPlan().getEnrolmentsByExecutionYear(executionYear)) {
                totalCreditsEnrolled += enrollment.getEctsCredits();
            }
            row.setCell(totalCreditsEnrolled);

            // Total de ECTS concluídos até ao fim do ano lectivo anterior (1º
            // Semestre do ano lectivo actual) ao que se
            // referem os dados (neste caso até ao fim de 2008) no curso actual
            for (final CycleCurriculumGroup cycleCurriculumGroup : registration.getLastStudentCurricularPlan()
                    .getInternalCycleCurriculumGrops()) {

                // We can use current year because only the first semester has
                // occured
                totalEctsConcludedUntilPreviousYear += cycleCurriculumGroup.getCreditsConcluded(executionYear);
            }
            row.setCell(totalEctsConcludedUntilPreviousYear);

            // Nº ECTS equivalência/substituição/dispensa
            double totalCreditsDismissed = 0d;
            for (Credits credits : registration.getLastStudentCurricularPlan().getCredits()) {
                if (credits.isEquivalence()) {
                    totalCreditsDismissed += credits.getEnrolmentsEcts();
                }
            }
            row.setCell(totalCreditsDismissed);

        } else {
            row.setCell("n/a");
            row.setCell("n/a");
            row.setCell("n/a");
        }

        if (registration != null) {
            // Total de ECTS necessários para a conclusão
            if (concluded) {
                row.setCell(0);
            } else {
                row.setCell(registration.getLastStudentCurricularPlan().getRoot().getDefaultEcts(executionYear)
                        - totalEctsConcludedUntilPreviousYear);
            }
        } else {
            row.setCell("n/a");
        }

        // Se alunos de Doutoramento, inscrito na Parte Curricular?
        if (registration != null && registration.isDEA()) {
            row.setCell(String.valueOf(registration.getLastStudentCurricularPlan().hasEnrolments(executionYear)));
        } else {
            row.setCell("not PhD");
        }

        row.setCell(process.getPhdStudentNumber());

        // ist id dos orientadores
        int count = 0;
        StringBuilder guidings = new StringBuilder();
        for (PhdParticipant phdParticipant : process.getGuidings()) {
            if (phdParticipant.isInternal()) {
                if (count > 0) {
                    guidings.append(";");
                }
                guidings.append(((InternalPhdParticipant) phdParticipant).getPerson().getIstUsername());
                count++;
            }
        }
        row.setCell(guidings.toString());

        // ist id dos co-orientadores
        int countAssistantGuidings = 0;
        StringBuilder assistantGuidings = new StringBuilder();
        for (PhdParticipant phdParticipant : process.getAssistantGuidings()) {
            if (phdParticipant.isInternal()) {
                if (countAssistantGuidings > 0) {
                    assistantGuidings.append(";");
                }
                assistantGuidings.append(((InternalPhdParticipant) phdParticipant).getPerson().getIstUsername());
                countAssistantGuidings++;
            }
        }
        row.setCell(assistantGuidings.toString());

        // Nome do Orientador Externo
        int countExternalGuidings = 0;
        StringBuilder externalGuidingNames = new StringBuilder();
        for (PhdParticipant phdParticipant : process.getGuidings()) {
            if (!phdParticipant.isInternal()) {
                if (countExternalGuidings > 0) {
                    externalGuidingNames.append(";");
                }
                externalGuidingNames.append(((ExternalPhdParticipant) phdParticipant).getName());
                externalGuidingNames.append(" (");
                externalGuidingNames.append(((ExternalPhdParticipant) phdParticipant).getInstitution());
                externalGuidingNames.append(")");
                countExternalGuidings++;
            }
        }
        row.setCell(externalGuidingNames.toString());

        // Nome do Co-Orientador Externo
        int countExternalAssistantGuidings = 0;
        StringBuilder externalAssistantGuidingNames = new StringBuilder();
        for (PhdParticipant phdParticipant : process.getAssistantGuidings()) {
            if (!phdParticipant.isInternal()) {
                if (countExternalAssistantGuidings > 0) {
                    externalAssistantGuidingNames.append(";");
                }
                externalAssistantGuidingNames.append(((ExternalPhdParticipant) phdParticipant).getName());
                externalAssistantGuidingNames.append(" (");
                externalAssistantGuidingNames.append(((ExternalPhdParticipant) phdParticipant).getInstitution());
                externalAssistantGuidingNames.append(")");
                countExternalAssistantGuidings++;
            }
        }
        row.setCell(externalAssistantGuidingNames.toString());

        PhdProgramProcessState lastActiveState = process.getMostRecentState();
        row.setCell(lastActiveState != null ? lastActiveState.getType().getLocalizedName() : "n/a");

        if (process.getCollaborationType() != null) {
            row.setCell(process.getCollaborationType().getLocalizedName());
        } else {
            row.setCell("n/a");
        }

        if (process.getCandidacyDate() != null) {
            row.setCell(process.getCandidacyDate().toString("dd/MM/yyyy"));
        } else {
            row.setCell("n/a");
        }

        if (process.getCandidacyProcess().getWhenRatified() != null) {
            row.setCell(process.getCandidacyDate().toString("dd/MM/yyyy"));
        } else {
            row.setCell("n/a");
        }

        if (process.getWhenStartedStudies() != null) {
            row.setCell(process.getWhenStartedStudies().toString("dd/MM/yyyy"));
        } else {
            row.setCell("n/a");
        }

        if (process.getThesisProcess() != null && process.getThesisProcess().getDiscussionDate() != null) {
            row.setCell(process.getThesisProcess().getDiscussionDate().toString("dd/MM/yyyy"));
        } else {
            row.setCell("n/a");
        }

        // Tipo de Acordo (AFA, AM, ERASMUS, etc)
        row.setCell(registration != null ? registration.getRegistrationAgreement() != null ? registration
                .getRegistrationAgreement().getName() : "" : "");

        // Data de Apresentação Pública da CAT
        if (process.hasSeminarProcess() && process.getSeminarProcess().getPresentationDate() != null) {
            row.setCell(process.getSeminarProcess().getPresentationDate().toString("dd/MM/yyyy"));
        } else {
            row.setCell("n/a");
        }
    }

    private int calculateNumberOfEnrolmentYears(Registration registration, ExecutionYear executionYear) {
        return registration.getNumberOfYearsEnrolledUntil(executionYear);
    }

}
