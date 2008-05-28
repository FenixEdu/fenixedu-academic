package net.sourceforge.fenixedu.applicationTier.Servico.student.reports;

import java.io.Serializable;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentDataByExecutionYear;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

public class GenerateStudentReport implements Serializable {

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources/EnumerationResources");

    public static class StudentReportPredicate implements Serializable {

	private DomainReference<ExecutionYear> executionYearDomainReference;

	private DegreeType degreeType;

	private boolean concluded = false;

	private boolean active = false;

	{
	    setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	}

	public ExecutionYear getExecutionYear() {
	    return executionYearDomainReference != null ? executionYearDomainReference.getObject() : null;
	}

	public void setExecutionYear(final ExecutionYear executionYear) {
	    executionYearDomainReference = executionYear == null ? null : new DomainReference<ExecutionYear>(executionYear);
	}

	public DegreeType getDegreeType() {
	    return degreeType;
	}

	public void setDegreeType(DegreeType degreeType) {
	    this.degreeType = degreeType;
	}

	public boolean getConcluded() {
	    return concluded;
	}

	public void setConcluded(boolean concluded) {
	    this.concluded = concluded;
	}

	public boolean getActive() {
	    return active;
	}

	public void setActive(boolean active) {
	    this.active = active;
	}

	public boolean getAreRequiredFieldsFilledOut() {
	    return (getActive() || getConcluded()) && getExecutionYear() != null; 
	}

	public boolean applyFor(final DegreeType forDegreeType) {
	    final DegreeType degreeType = getDegreeType();
	    return degreeType == null || degreeType == forDegreeType;
	}

	public boolean applyFor(final Registration registration) {
	    final ExecutionYear executionYear = getExecutionYear();
	    final RegistrationState registrationState = registration.getLastRegistrationState(executionYear);
	    if (registrationState == null) {
		return false;
	    }
	    final RegistrationStateType registrationStateType = registrationState.getStateType();
	    return (getActive() && registrationStateType.isActive()) ||
	    		(getConcluded() && registrationStateType == RegistrationStateType.CONCLUDED
	    			&& executionYear.containsDate(registrationState.getStateDate()));
	}

    }

    public static Spreadsheet generateReport(final StudentReportPredicate studentReportPredicate) {
	final Spreadsheet spreadsheet = new Spreadsheet("StudentDataAuthorizations");
	addHeaders(spreadsheet);
	final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
	for (final Degree degree : RootDomainObject.getInstance().getDegreesSet()) {
	    final DegreeType degreeType = degree.getDegreeType();
	    if (studentReportPredicate.applyFor(degreeType)) {
		processDegree(spreadsheet, studentReportPredicate, degree, executionYear);
	    }
	}
	return spreadsheet;
    }

    private static void addHeaders(final Spreadsheet spreadsheet) {
	spreadsheet.setHeader("Número");
	spreadsheet.setHeader("Nome");
	spreadsheet.setHeader("Curso");
	spreadsheet.setHeader("Ramo");
	spreadsheet.setHeader("Ano Curricular");
	spreadsheet.setHeader("Média");
	spreadsheet.setHeader("Ano Léctivo de Conclusão");
	spreadsheet.setHeader("Morada");
	spreadsheet.setHeader("Localidade");
	spreadsheet.setHeader("Código Postal");
	spreadsheet.setHeader("Localidade do Código Postal");
	spreadsheet.setHeader("Telefone");
	spreadsheet.setHeader("Telemovel");
	spreadsheet.setHeader("E-mail");
	spreadsheet.setHeader("Autorização");
    }

    private static void processDegree(final Spreadsheet spreadsheet, final StudentReportPredicate studentReportPredicate, final Degree degree, final ExecutionYear executionYear) {
	for (final Registration registration : degree.getRegistrationsSet()) {
	    if (studentReportPredicate.applyFor(registration)) {
		final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
		processStudentCurricularPlan(spreadsheet, studentCurricularPlan, executionYear);
	    }
	}
    }

    private static void processStudentCurricularPlan(final Spreadsheet spreadsheet, final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionYear executionYear) {
	final Registration registration = studentCurricularPlan.getRegistration();
	final Student student = registration.getStudent();
	final Person person = student.getPerson();
	final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
	final Degree degree = degreeCurricularPlan.getDegree();
	final StudentDataByExecutionYear studentDataByExecutionYear = student.getStudentDataByExecutionYear(executionYear);
	final Branch branch = studentCurricularPlan.getBranch();
	final StudentPersonalDataAuthorizationChoice studentPersonalDataAuthorizationChoice = studentDataByExecutionYear == null ? null
		: studentDataByExecutionYear.getPersonalDataAuthorization();
	final String spdac = studentPersonalDataAuthorizationChoice == null ? StudentPersonalDataAuthorizationChoice.NO_END
		.getQualifiedName() : studentPersonalDataAuthorizationChoice.getQualifiedName();

	final Row row = spreadsheet.addRow();
	row.setCell(student.getNumber().toString());
	row.setCell(person.getName());
	row.setCell(degree.getTipoCurso().getLocalizedName() + " " + degree.getName());
	row.setCell(branch == null ? "" : branch.getName());
	row.setCell("" + registration.getCurricularYear(executionYear));
	row.setCell("" + registration.getAverage(executionYear));
	if (registration.isConcluded()) {
	    row.setCell(executionYear.getYear());
	} else {
	    row.setCell("");
	}
	row.setCell(person.getAddress());
	row.setCell(person.getArea());
	row.setCell(person.getAreaCode());
	row.setCell(person.getAreaOfAreaCode());
	row.setCell(person.getPhone());
	row.setCell(person.getMobile());
	row.setCell(person.getEmail());
	row.setCell(resourceBundle.getString(spdac));
    }

}
