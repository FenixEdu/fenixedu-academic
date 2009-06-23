package net.sourceforge.fenixedu.domain.reports;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class RaidesDfaReportFile extends RaidesDfaReportFile_Base {

    public RaidesDfaReportFile() {
	super();
    }

    @Override
    public String getJobName() {
	return "Listagem RAIDES - DFA";
    }

    @Override
    protected String getPrefix() {
	return "dfaRAIDES";
    }

    @Override
    public DegreeType getDegreeType() {
	return DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA;
    }
    
    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {

	ExecutionYear executionYear = getExecutionYear();
	fillSpreadsheet(spreadsheet);

	System.out.println("BEGIN report for " + getDegreeType().name());
	int count = 0;

	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansToProcess(executionYear)) {
	    final Registration registration = studentCurricularPlan.getRegistration();

	    if (registration != null && !registration.isTransition()) {

		for (final CycleType cycleType : registration.getDegreeType().getCycleTypes()) {
		    final CycleCurriculumGroup cycleCGroup = studentCurricularPlan.getRoot().getCycleCurriculumGroup(cycleType);
		    if (cycleCGroup != null && !cycleCGroup.isExternal()) {
			final RegistrationConclusionBean registrationConclusionBean = new RegistrationConclusionBean(
				registration, cycleCGroup);

			ExecutionYear conclusionYear = null;
			if (cycleCGroup.isConcluded()) {
			    conclusionYear = registrationConclusionBean.getConclusionYear();

			    if (conclusionYear != executionYear && conclusionYear != executionYear.getPreviousExecutionYear()) {
				continue;
			    }

			}

			if ((registration.isActive() || registration.isConcluded()) && conclusionYear != null) {
			    reportRaidesGraduate(spreadsheet, registration, executionYear, cycleType, true,
				    registrationConclusionBean.getConclusionDate());
			} else if (registration.isActive()) {
			    reportRaidesGraduate(spreadsheet, registration, executionYear, cycleType, false, null);
			}
		    }
		}

		count++;
//		 if (count == 20) {
//		    break;
//		}
	    }

	}

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

    private void fillSpreadsheet(Spreadsheet spreadsheet) {
	spreadsheet.setHeader("ciclo");
	spreadsheet.setHeader("concluído (ano anterior)?");
	spreadsheet.setHeader("média do ciclo");
	spreadsheet.setHeader("Data de conclusão");
	spreadsheet.setHeader("Data de Início");
	spreadsheet.setHeader("número aluno");
	spreadsheet.setHeader("número identificação");
	spreadsheet.setHeader("tipo identificação");
	spreadsheet.setHeader("nome");
	spreadsheet.setHeader("género");
	spreadsheet.setHeader("data nascimento");
	spreadsheet.setHeader("país nascimento");
	spreadsheet.setHeader("país nacionalidade");
	spreadsheet.setHeader("tipo curso");
	spreadsheet.setHeader("nome curso");
	spreadsheet.setHeader("sigla curso");
	spreadsheet.setHeader("ramo");
	spreadsheet.setHeader("nº. anos lectivos inscrição curso actual");
	spreadsheet.setHeader("estabelecimento curso anterior");
	spreadsheet.setHeader("curso anterior");
	spreadsheet.setHeader("estado civil");
	spreadsheet.setHeader("país residência permanente");
	spreadsheet.setHeader("distrito residência permanente");
	spreadsheet.setHeader("concelho residência permanente");
	spreadsheet.setHeader("deslocado residência permanente");
	spreadsheet.setHeader("níve escolaridade pai");
	spreadsheet.setHeader("nível escolaridade mãe");
	spreadsheet.setHeader("condição perante profissão pai");
	spreadsheet.setHeader("condição perante profissão mãe");
	spreadsheet.setHeader("profissão pai");
	spreadsheet.setHeader("profissão mãe");
	spreadsheet.setHeader("profissão aluno");
	spreadsheet.setHeader("estatuto trabalhador estudante introduzido (info. RAIDES)");
	spreadsheet.setHeader("bolseiro (info. RAIDES)");
	spreadsheet.setHeader("bolseiro (info. oficial)");
	spreadsheet.setHeader("habilitação anterior");
	spreadsheet.setHeader("país habilitação anterior");
	spreadsheet.setHeader("tipo estabelecimento ensino secundário");
	spreadsheet.setHeader("total ECTS concluídos fim ano lectivo anterior (1º Semestre do ano lectivo actual)");
	spreadsheet.setHeader("total ECTS necessários para a conclusão");
	spreadsheet.setHeader("Tem situação de propinas no lectivo dos dados?");
	spreadsheet.setHeader("doutoramento: inscrito parte curricular");
    }

    private void reportRaidesGraduate(final Spreadsheet sheet, final Registration registration, ExecutionYear executionYear,
	    final CycleType cycleType, final boolean concluded, final YearMonthDay conclusionDate) {

	final Row row = sheet.addRow();
	final Person graduate = registration.getPerson();
	final CandidacyInformationBean candidacyInformationBean = registration.getCandidacyInformationBean();

	// Ciclo
	row.setCell(cycleType.getDescription());

	// Concluído
	row.setCell(String.valueOf(concluded));

	// Média do Ciclo
	row.setCell(concluded ? registration.getLastStudentCurricularPlan().getCycle(cycleType).getCurriculum(
		conclusionDate.toDateTimeAtMidnight()).getAverage().toPlainString() : "n/a");

	// Data de conclusão
	row.setCell(conclusionDate != null ? conclusionDate.toString("yyyy/MM/dd") : "");

	// Data de Início
	row.setCell(registration.getStartDate() != null ? registration.getStartDate().toString("yyyy/MM/dd") : "");

	// Nº de aluno
	row.setCell(registration.getNumber());

	// Nº de Identificação
	row.setCell(graduate.getDocumentIdNumber());

	// Tipo Identificação
	row.setCell(graduate.getIdDocumentType().getLocalizedName());

	// Nome
	row.setCell(registration.getName());

	// Sexo
	row.setCell(graduate.getGender().toString());

	// Data de Nascimento
	row.setCell(graduate.getDateOfBirthYearMonthDay() != null ? graduate.getDateOfBirthYearMonthDay().toString("yyyy/MM/dd")
		: "n/a");

	// País de Nascimento
	row.setCell(graduate.getCountryOfBirth() != null ? graduate.getCountryOfBirth().getName() : "n/a");

	// País de Nacionalidade
	row.setCell(graduate.getCountry() != null ? graduate.getCountry().getName() : "n/a");

	// Tipo Curso
	row.setCell(registration.getDegreeType().getLocalizedName());

	// Nome Curso
	row.setCell(registration.getDegree().getNameI18N().getContent());

	// Sigla Curso
	row.setCell(registration.getDegree().getSigla());

	// Ramo (caso se aplique)
	row.setCell("não determinável");

	// Nº de anos lectivos de inscrição no Curso actual
	row.setCell(calculateNumberOfEnrolmentYears(registration));

	// Estabelecimento do Curso Anterior (se o aluno ingressou por uma via
	// diferente CNA, e deve
	// ser IST caso o aluno tenha estado matriculado noutro curso do IST)
	row.setCell(candidacyInformationBean.getInstitution() != null ? candidacyInformationBean.getInstitution().getName() : "");

	// Curso Anterior (se o aluno ingressou por uma via diferente CNA, e
	// deve ser IST caso o aluno
	// tenha estado matriculado noutro curso do IST)
	row.setCell(candidacyInformationBean.getDegreeDesignation());

	// Estado Civil
	row.setCell(candidacyInformationBean.getMaritalStatus() != null ? candidacyInformationBean.getMaritalStatus().toString()
		: registration.getPerson().getMaritalStatus().toString());

	// País de Residência Permanente
	if (candidacyInformationBean.getCountryOfResidence() != null) {
	    row.setCell(candidacyInformationBean.getCountryOfResidence().getName());
	} else {
	    row.setCell(registration.getStudent().getPerson().getCountryOfResidence() != null ? registration.getStudent()
		    .getPerson().getCountryOfResidence().getName() : "");
	}

	// Distrito de Residência Permanente
	if (candidacyInformationBean.getDistrictSubdivisionOfResidence() != null) {
	    row.setCell(candidacyInformationBean.getDistrictSubdivisionOfResidence().getDistrict().getName());
	} else {
	    row.setCell(registration.getStudent().getPerson().getDistrictOfResidence());
	}

	// Concelho de Residência Permanente
	if (candidacyInformationBean.getDistrictSubdivisionOfResidence() != null) {
	    row.setCell(candidacyInformationBean.getDistrictSubdivisionOfResidence().getName());
	} else {
	    row.setCell(registration.getStudent().getPerson().getDistrictSubdivisionOfResidence());
	}

	// Deslocado da Residência Permanente
	if (candidacyInformationBean.getDislocatedFromPermanentResidence() != null) {
	    row.setCell(candidacyInformationBean.getDislocatedFromPermanentResidence().toString());
	} else {
	    row.setCell("");
	}

	// Nível de Escolaridade do Pai
	if (candidacyInformationBean.getFatherSchoolLevel() != null) {
	    row.setCell(candidacyInformationBean.getFatherSchoolLevel().getName());
	} else {
	    row.setCell("");
	}

	// Nível de Escolaridade da Mãe
	if (candidacyInformationBean.getMotherSchoolLevel() != null) {
	    row.setCell(candidacyInformationBean.getMotherSchoolLevel().getName());
	} else {
	    row.setCell("");
	}

	// Condição perante a situação na profissão/Ocupação do Pai
	if (candidacyInformationBean.getFatherProfessionalCondition() != null) {
	    row.setCell(candidacyInformationBean.getFatherProfessionalCondition().getName());
	} else {
	    row.setCell("");
	}

	// Condição perante a situação na profissão/Ocupação da Mãe
	if (candidacyInformationBean.getMotherProfessionalCondition() != null) {
	    row.setCell(candidacyInformationBean.getMotherProfessionalCondition().getName());
	} else {
	    row.setCell("");
	}

	// Profissão do Pai
	if (candidacyInformationBean.getFatherProfessionType() != null) {
	    row.setCell(candidacyInformationBean.getFatherProfessionType().getName());
	} else {
	    row.setCell("");
	}

	// Profissão da Mãe
	if (candidacyInformationBean.getMotherProfessionType() != null) {
	    row.setCell(candidacyInformationBean.getMotherProfessionType().getName());
	} else {
	    row.setCell("");
	}

	// Profissão do Aluno
	if (candidacyInformationBean.getProfessionType() != null) {
	    row.setCell(candidacyInformationBean.getProfessionType().getName());
	} else {
	    row.setCell("");
	}

	// Estatuto de Trabalhador Estudante introduzido pelo aluno
	if (candidacyInformationBean.getProfessionalCondition() != null) {
	    row.setCell(candidacyInformationBean.getProfessionalCondition().getName());
	} else {
	    row.setCell("");
	}

	// Bolseiro (info. RAIDES)
	if (candidacyInformationBean.getGrantOwnerType() != null) {
	    row.setCell(candidacyInformationBean.getGrantOwnerType().getName());
	} else {
	    row.setCell("");
	}

	// Bolseiro (info. oficial)
	boolean sasFound = false;
	for (StudentStatute statute : registration.getStudent().getStudentStatutes()) {
	    if (statute.getStatuteType() == StudentStatuteType.SAS_GRANT_OWNER
		    && statute.isValidInExecutionPeriod(executionYear.getFirstExecutionPeriod())) {
		sasFound = true;
		break;
	    }
	}
	row.setCell(String.valueOf(sasFound));

	// Habilitação Anterior ao Curso Actual
	row.setCell(candidacyInformationBean.getSchoolLevel() != null ? candidacyInformationBean.getSchoolLevel().getName() : "");

	// País de Habilitação Anterior ao Curso Actual
	row.setCell(candidacyInformationBean.getCountryWhereFinishedPrecedentDegree() != null ? candidacyInformationBean
		.getCountryWhereFinishedPrecedentDegree().getName() : "");

	// Tipo de Estabelecimento Frequentado no Ensino Secundário
	if (candidacyInformationBean.getHighSchoolType() != null) {
	    row.setCell(candidacyInformationBean.getHighSchoolType().getName());
	} else {
	    row.setCell("");
	}

	// Total de ECTS concluídos até ao fim do ano lectivo anterior (1º
	// Semestre do ano lectivo actual) ao que se
	// referem os dados (neste caso até ao fim de 2008) no curso actual
	double totalEctsConcludedUntilPreviousYear = 0d;
	for (final CycleCurriculumGroup cycleCurriculumGroup : registration.getLastStudentCurricularPlan()
		.getInternalCycleCurriculumGrops()) {

	    // We can use current year because only the first semester has
	    // occured
	    totalEctsConcludedUntilPreviousYear += cycleCurriculumGroup.getCreditsConcluded(executionYear);
	}

	row.setCell(totalEctsConcludedUntilPreviousYear);

	// Total de ECTS necessários para a conclusão
	if (concluded) {
	    row.setCell(0);
	} else {
	    row.setCell(registration.getLastStudentCurricularPlan().getRoot().getDefaultEcts(executionYear)
		    - totalEctsConcludedUntilPreviousYear);
	}

	// Tem situação de propinas no lectivo dos dados?
	row.setCell(String.valueOf(registration.getLastStudentCurricularPlan().hasAnyGratuityEventFor(executionYear)));

	// Se alunos de Doutoramento, inscrito na Parte Curricular?
	if (registration.isDEA()) {
	    row.setCell(String.valueOf(registration.getLastStudentCurricularPlan().hasEnrolments(executionYear)));
	} else {
	    row.setCell("not DEA");
	}

    }

    private int calculateNumberOfEnrolmentYears(Registration registration) {
	return registration.getEnrolmentsExecutionYears().size();
    }

}
