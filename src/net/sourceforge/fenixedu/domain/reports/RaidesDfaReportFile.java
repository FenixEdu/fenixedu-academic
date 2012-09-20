package net.sourceforge.fenixedu.domain.reports;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
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
	createHeaders(spreadsheet);

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
			    reportRaides(spreadsheet, registration, getFullRegistrationPath(registration), executionYear,
				    cycleType, true, registrationConclusionBean.getConclusionDate());
			} else if (registration.isActive()) {
			    reportRaides(spreadsheet, registration, getFullRegistrationPath(registration), executionYear,
				    cycleType, false, null);
			}
		    }
		}
		count++;
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

    private void createHeaders(Spreadsheet spreadsheet) {
	RaidesCommonReportFieldsWrapper.createHeaders(spreadsheet);
	spreadsheet.setHeader("Total ECTS necessários para a conclusão");
    }

    private void reportRaides(final Spreadsheet sheet, final Registration registration, List<Registration> registrationPath,
	    ExecutionYear executionYear, final CycleType cycleType, final boolean concluded, final YearMonthDay conclusionDate) {

	final Row row = RaidesCommonReportFieldsWrapper.reportRaidesFields(sheet, registration, registrationPath, executionYear,
		cycleType, concluded, conclusionDate, null, false);

	// Total de ECTS concluídos até ao fim do ano lectivo anterior ao que se referem os dados  no curso actual
	double totalEctsConcludedUntilPreviousYear = 0d;
	for (final CycleCurriculumGroup cycleCurriculumGroup : registration.getLastStudentCurricularPlan()
		.getInternalCycleCurriculumGrops()) {
	    totalEctsConcludedUntilPreviousYear += cycleCurriculumGroup.getCreditsConcluded(executionYear
		    .getPreviousExecutionYear());
	}

	// Total de ECTS necessários para a conclusão
	if (concluded) {
	    row.setCell(0);
	} else {
	    row.setCell(registration.getLastStudentCurricularPlan().getRoot().getDefaultEcts(executionYear)
		    - totalEctsConcludedUntilPreviousYear);
	}
    }
}
