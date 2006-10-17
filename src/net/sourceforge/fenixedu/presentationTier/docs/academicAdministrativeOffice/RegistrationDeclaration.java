package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.student.Registration;

public class RegistrationDeclaration implements Serializable {

    public static final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", new Locale("pt", "PT"));

    private DomainReference<Registration> registrationDomainReference;

    private DomainReference<Person> employeeDomainReference;

    public RegistrationDeclaration(final Registration registration, final Person person) {
        registrationDomainReference = new DomainReference<Registration>(registration);
        employeeDomainReference = new DomainReference<Person>(person);
    }

    public Person getEmployee() {
        return employeeDomainReference == null ? null : employeeDomainReference.getObject();
    }

    public Registration getRegistration() {
        return registrationDomainReference == null ? null : registrationDomainReference.getObject();
    }

    public String getDeclarationBody() {
        final Person employee = getEmployee();
        final Registration registration = getRegistration();
        final Person student = registration.getStudent().getPerson();
        final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();

        final StringBuilder stringBuilder = new StringBuilder();
        try {
        stringBuilder.append("\t");
        if (employee.getGender() == Gender.MALE) {
            stringBuilder.append(resourceBundle.getString("message.declaration.registration.person.title.male"));
        } else if (employee.getGender() == Gender.FEMALE) {
            stringBuilder.append(resourceBundle.getString("message.declaration.registration.person.title.female"));
        } else {
            throw new Error("unknown.gender.type.of.employee");
        }
        stringBuilder.append(" ");
        stringBuilder.append(resourceBundle.getString("message.declaration.registration.person.tail"));
        stringBuilder.append("\n\t");
        if (student.getGender() == Gender.MALE) {
            stringBuilder.append(resourceBundle.getString("message.declaration.registration.student.number.header.male"));
        } else if (student.getGender() == Gender.FEMALE) {
            stringBuilder.append(resourceBundle.getString("message.declaration.registration.student.number.header.female"));
        } else {
            throw new Error("unknown.gender.type.of.student");
        }
        stringBuilder.append(" ");
        stringBuilder.append(registration.getNumber());
        stringBuilder.append(" ");
        stringBuilder.append(student.getName());
        stringBuilder.append(" ");
        stringBuilder.append(resourceBundle.getString("message.declaration.registration.document.id.prefix"));
        stringBuilder.append(" ");
        stringBuilder.append(student.getIdDocumentType());
        stringBuilder.append(" ");
        stringBuilder.append(student.getDocumentIdNumber());
        stringBuilder.append(" ");
        stringBuilder.append(resourceBundle.getString("message.declaration.registration.nationality.prefix"));
        stringBuilder.append(" ");
        stringBuilder.append(student.getNacionalidade());
        stringBuilder.append(" ");
        stringBuilder.append(resourceBundle.getString("message.declaration.registration.father.prefix"));
        stringBuilder.append(" ");
        stringBuilder.append(student.getNameOfFather());
        stringBuilder.append(" ");
        stringBuilder.append(resourceBundle.getString("message.declaration.registration.mother.prefix"));
        stringBuilder.append(" ");
        stringBuilder.append(student.getNameOfMother());
        stringBuilder.append(" ");
        stringBuilder.append(resourceBundle.getString("message.declaration.registration.execution.year.prefix"));
        stringBuilder.append(" ");
        stringBuilder.append(ExecutionYear.readCurrentExecutionYear().getYear());
        stringBuilder.append(" ");
        stringBuilder.append(resourceBundle.getString("message.declaration.registration.is.enroled"));
        stringBuilder.append(" ");
        //stringBuilder.append(resourceBundle.getString("message.declaration.registration.was.enroled"));
        stringBuilder.append(resourceBundle.getString("message.declaration.registration.in"));
        stringBuilder.append(" ");
        stringBuilder.append(registration.calculateCurricularYear());
        stringBuilder.append(" ");
        stringBuilder.append(resourceBundle.getString("message.declaration.registration.degree.prefix"));
        stringBuilder.append(" ");
        stringBuilder.append(studentCurricularPlan.getDegreeCurricularPlan().getDegree().getName());
        stringBuilder.append(" ");
        stringBuilder.append(resourceBundle.getString("message.declaration.registration.enroled.courses.prefix"));
        stringBuilder.append(" ");
        stringBuilder.append(studentCurricularPlan.countCurrentEnrolments());
        stringBuilder.append(" ");
        stringBuilder.append(resourceBundle.getString("message.declaration.registration.enroled.courses.posfix"));
        stringBuilder.append("\n\t");
        stringBuilder.append(resourceBundle.getString("message.declaration.registration.location.prefix"));
        stringBuilder.append(" ");
        // TODO : fix this when the new spaces structure is introduced and the location of each campus is known.
        if (studentCurricularPlan.getCurrentCampus().getName().equals("Alameda")) {
            stringBuilder.append("Lisboa");
        } else {
            stringBuilder.append("Oeiras");
        }
        stringBuilder.append(" ");
        stringBuilder.append(resourceBundle.getString("message.declaration.registration.date.prefix"));
        final YearMonthDay today = new YearMonthDay();
        stringBuilder.append(today.toString("dd/MM/yyyy", new Locale("pt", "PT")));
        stringBuilder.append("\n\t\t\t\t\t\t");
        if (employee.getGender() == Gender.MALE) {
            stringBuilder.append(resourceBundle.getString("message.declaration.registration.person.title.male"));
        } else if (employee.getGender() == Gender.FEMALE) {
            stringBuilder.append(resourceBundle.getString("message.declaration.registration.person.title.female"));
        } else {
            throw new Error("unknown.gender.type.of.employee");
        }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

}
