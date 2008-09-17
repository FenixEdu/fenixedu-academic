package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.SchoolRegistrationDeclarationRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

public class RegistrationDeclaration extends AdministrativeOfficeDocument {

    private DomainReference<Registration> registrationDomainReference;

    private DomainReference<Person> employeeDomainReference;

    private DomainReference<ExecutionYear> executionYearDomainReference;

    protected RegistrationDeclaration(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    public RegistrationDeclaration(final Registration registration, final ExecutionYear executionYear, final Person loggedPerson) {
	super();
	init(registration, executionYear, loggedPerson);
    }

    private void init(Registration registration, ExecutionYear executionYear, Person loggedPerson) {
	this.registrationDomainReference = new DomainReference<Registration>(registration);
	this.employeeDomainReference = new DomainReference<Person>(loggedPerson);
	this.executionYearDomainReference = new DomainReference<ExecutionYear>(executionYear);

	addParameter("RegistrationDeclaration", this);
	setResourceBundle(ResourceBundle.getBundle("resources.AcademicAdminOffice", getLocale()));
	addDataSourceElement(this);
    }

    @Override
    protected void fillReport() {
	super.fillReport();

	init(getDocumentRequest().getRegistration(), ((SchoolRegistrationDeclarationRequest) getDocumentRequest())
		.getExecutionYear(), AccessControl.getPerson());
    }

    public Person getEmployee() {
	return employeeDomainReference == null ? null : employeeDomainReference.getObject();
    }

    @Override
    public Registration getRegistration() {
	return registrationDomainReference == null ? null : registrationDomainReference.getObject();
    }

    public ExecutionYear getExecutionYear() {
	return executionYearDomainReference == null ? null : executionYearDomainReference.getObject();
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYearDomainReference = executionYear == null ? null : new DomainReference<ExecutionYear>(executionYear);
    }

    private static final DateTimeFormatter fmt = new DateTimeFormatterBuilder().appendMonthOfYearText().toFormatter();

    public String getDeclarationBody() {
	final Registration registration = getRegistration();
	final Person student = registration.getStudent().getPerson();
	final ExecutionYear executionYear = getExecutionYear();
	final int curricularYear = registration.getCurricularYear(executionYear);
	final StudentCurricularPlan scp = registration.getStudentCurricularPlan(executionYear);
	final int numberEnrolments = scp.countEnrolments(executionYear);

	final StringBuilder builder = new StringBuilder();
	try {
	    builder.append(getResourceBundle().getString("message.declaration.registration.person.title.female"));
	    builder.append(SINGLE_SPACE);
	    builder.append(getResourceBundle().getString("message.declaration.registration.person.tail"));
	    builder.append(LINE_BREAK);
	    if (student.getGender() == Gender.MALE) {
		builder.append(getResourceBundle().getString("message.declaration.registration.student.number.header.male"));
	    } else if (student.getGender() == Gender.FEMALE) {
		builder.append(getResourceBundle().getString("message.declaration.registration.student.number.header.female"));
	    } else {
		throw new Error("unknown.gender.type.of.student");
	    }
	    builder.append(SINGLE_SPACE);
	    builder.append(registration.getNumber());
	    builder.append(getResourceBundle().getString("message.declaration.registration.comma"));
	    builder.append(SINGLE_SPACE);
	    builder.append(StringUtils.upperCase(student.getName()));
	    builder.append(getResourceBundle().getString("message.declaration.registration.comma"));
	    builder.append(SINGLE_SPACE);
	    builder.append(getResourceBundle().getString("message.declaration.registration.document.id.prefix"));
	    builder.append(SINGLE_SPACE);
	    builder.append(getEnumerationBundle().getString(student.getIdDocumentType().getName()));
	    builder.append(SINGLE_SPACE);
	    builder.append(student.getDocumentIdNumber());
	    builder.append(getResourceBundle().getString("message.declaration.registration.comma"));
	    builder.append(SINGLE_SPACE);
	    builder.append(getResourceBundle().getString("message.declaration.registration.nationality.prefix"));
	    builder.append(SINGLE_SPACE);
	    builder.append(student.getCountry().getName());
	    builder.append(getResourceBundle().getString("message.declaration.registration.comma"));
	    builder.append(SINGLE_SPACE);
	    builder.append(getResourceBundle().getString("message.declaration.registration.father.prefix"));
	    builder.append(SINGLE_SPACE);
	    builder.append(student.getNameOfFather());
	    builder.append(SINGLE_SPACE);
	    builder.append(getResourceBundle().getString("message.declaration.registration.mother.prefix"));
	    builder.append(SINGLE_SPACE);
	    builder.append(student.getNameOfMother());
	    builder.append(getResourceBundle().getString("message.declaration.registration.comma"));
	    builder.append(SINGLE_SPACE);
	    builder.append(getResourceBundle().getString("message.declaration.registration.execution.year.prefix"));
	    builder.append(SINGLE_SPACE);
	    builder.append(executionYear.getYear());
	    builder.append(SINGLE_SPACE);
	    builder.append(getResourceBundle().getString("message.declaration.registration.is.enroled"));
	    builder.append(SINGLE_SPACE);
	    builder.append(getResourceBundle().getString("message.declaration.registration.in"));
	    builder.append(SINGLE_SPACE);
	    builder.append(curricularYear);
	    builder.append(getResourceBundle().getString("message.declaration.registration.degree.prefix"));
	    builder.append(SINGLE_SPACE);
	    builder.append(getEnumerationBundle().getString(scp.getDegreeCurricularPlan().getDegree().getTipoCurso().getName()));
	    builder.append(SINGLE_SPACE);
	    builder.append(getResourceBundle().getString("label.in"));
	    builder.append(SINGLE_SPACE);
	    builder.append(scp.getRegistration().getDegreeName());
	    builder.append(SINGLE_SPACE);
	    builder.append(getResourceBundle().getString("message.declaration.registration.enroled.courses.prefix"));
	    builder.append(SINGLE_SPACE);
	    builder.append(numberEnrolments);
	    builder.append(SINGLE_SPACE);
	    builder.append(getResourceBundle().getString("message.declaration.registration.enroled.courses.posfix"));
	    builder.append(LINE_BREAK).append(SINGLE_SPACE).append(LINE_BREAK);
	    builder.append(getResourceBundle().getString("message.declaration.registration.location.prefix"));
	    builder.append(SINGLE_SPACE);
	    if (scp.getCurrentCampus().getName().equals("Alameda")) {
		builder.append("Lisboa");
	    } else {
		builder.append("Oeiras");
	    }
	    builder.append(getResourceBundle().getString("message.declaration.registration.comma"));
	    builder.append(SINGLE_SPACE);
	    final YearMonthDay today = new YearMonthDay();
	    builder.append(today.getDayOfMonth());
	    builder.append(SINGLE_SPACE);
	    builder.append(getResourceBundle().getString("message.declaration.registration.of"));
	    builder.append(SINGLE_SPACE);
	    builder.append(today.toString(fmt));
	    builder.append(SINGLE_SPACE);
	    builder.append(getResourceBundle().getString("message.declaration.registration.of"));
	    builder.append(SINGLE_SPACE);
	    builder.append(today.getYear());
	    builder.append(SINGLE_SPACE);
	    builder.append("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
	    builder.append(getResourceBundle().getString("message.declaration.registration.person.title.female"));
	} catch (Throwable t) {
	    t.printStackTrace();
	}
	return builder.toString();
    }

}
