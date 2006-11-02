package net.sourceforge.fenixedu.presentationTier.backBeans.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.PeriodState;
import pt.utl.ist.fenix.tools.smtp.EmailSender;

public class SendMailBackingBean extends FenixBackingBean {

    private String from = null;
    private String to = null;
    private String ccs = null;
    private String bccs = null;
    private String subject = null;
    private String message = null;

    private Boolean teachers = null;
    private Boolean employees = null;
    private Boolean degreeStudents = null;
    private Boolean masterDegreeStudents = null;
    private Boolean executionCourseResponsibles = null;
    private Boolean masterDegreeCoordinators = null;
    private Boolean degreeCoordinators = null;

    public void send() throws FenixFilterException, FenixServiceException {
        EmailSender.send(getFrom(), getFrom(), getToList(), getCCList(), getBCCList(), getSubject(), getMessage());
    }

    private List<String> getEmailList(final String emailStrings) {
        final List<String> emails = new ArrayList<String>();
        if (emailStrings != null && emailStrings.length() > 0) {
            for (final String email : emailStrings.split(",")) {
                emails.add(email);
            }
        }
        return emails;
    }

    private List<String> getToList() {
    	return getEmailList(getTo());
    }

    private List<String> getCCList() {
    	return getEmailList(getCcs());
    }

    private List<String> getBCCList() throws FenixFilterException, FenixServiceException {
    	final List<String> emails = getEmailList(getBccs());

        final Boolean teachers = getTeachers();
        if (teachers.booleanValue()) {
        	addEmails(emails, RoleType.TEACHER);
        }

        final Boolean employees = getEmployees();
        if (employees.booleanValue()) {
        	final Role role = Role.getRoleByRoleType(RoleType.EMPLOYEE);
            for (final Person person : role.getAssociatedPersons()) {
                if (person.getTeacher() == null) {
                    if (person.getEmail() != null && person.getEmail().length() > 0) {
                        emails.add(person.getEmail());
                    }
                }
            }
        }

        final Boolean degreeStudents = getDegreeStudents();
        final Boolean masterDegreeStudents = getMasterDegreeStudents();
        if (degreeStudents.booleanValue() || masterDegreeStudents.booleanValue()) {
        	final Role role = Role.getRoleByRoleType(RoleType.STUDENT);
            for (final Person person : role.getAssociatedPersons()) {
                Registration registration = null;
                if (degreeStudents.booleanValue()) {
                    registration = person.getStudentByType(DegreeType.DEGREE);
                }
                if (registration == null && masterDegreeStudents.booleanValue()) {
                    registration = person.getStudentByType(DegreeType.MASTER_DEGREE);
                }
                if (registration != null && person.getEmail() != null && person.getEmail().length() > 0) {
                    emails.add(person.getEmail());
                }
            }
        }

        final Boolean executionCourseResponsibles = getExecutionCourseResponsibles();
        if (executionCourseResponsibles.booleanValue()) {
            final Collection<ExecutionYear> executionYears = rootDomainObject.getExecutionYearsSet();
            for (final ExecutionYear executionYear : executionYears) {
                if (executionYear.getState().equals(PeriodState.CURRENT)) {
                    for (final ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
                        for (final ExecutionCourse executionCourse : executionPeriod.getAssociatedExecutionCourses()) {
                            for (final Professorship professorship : executionCourse.getProfessorships()) {
                                if (professorship.getResponsibleFor().booleanValue()) {
                                    final Teacher teacher = professorship.getTeacher();
                                    final Person person = teacher.getPerson();
                                    emails.add(person.getEmail());
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }

        final Boolean degreeCoordinators = getDegreeCoordinators();
        if (degreeCoordinators.booleanValue()) {
            addEmailsForDegreeType(emails, DegreeType.DEGREE);
        }

        final Boolean masterDegreeCoordinators = getMasterDegreeCoordinators();
        if (masterDegreeCoordinators.booleanValue()) {
            addEmailsForDegreeType(emails, DegreeType.MASTER_DEGREE);
        }

        return emails;
    }

    private void addEmails(final List<String> emails, final RoleType roleType) {
        final Role role = Role.getRoleByRoleType(roleType);
        for (final Person person : role.getAssociatedPersons()) {
            if (person.getEmail() != null && person.getEmail().length() > 0) {
                emails.add(person.getEmail());
            }
        }
    }

	private void addEmailsForDegreeType(final List<String> emails, final DegreeType degreeType) throws FenixServiceException, FenixFilterException {
        final Collection<ExecutionYear> executionYears = rootDomainObject.getExecutionYearsSet();
        for (final ExecutionYear executionYear : executionYears) {
            if (executionYear.getState().equals(PeriodState.CURRENT)) {
                for (final ExecutionDegree executionDegree : executionYear.getExecutionDegrees()) {
                    final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
                    final Degree degree = degreeCurricularPlan.getDegree();
                    if (degree.getTipoCurso() == degreeType) {
                        for (final Coordinator coordinator : executionDegree.getCoordinatorsList()) {
                            final Person person = coordinator.getPerson();
                            emails.add(person.getEmail());
                        }
                    }
                }
                break;
            }
        }
    }

    public String getBccs() {
        return bccs;
    }
    public void setBccs(String bccs) {
        this.bccs = bccs;
    }
    public String getCcs() {
        return ccs;
    }
    public void setCcs(String ccs) {
        this.ccs = ccs;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public Boolean getTeachers() {
        return teachers;
    }
    public void setTeachers(Boolean teachers) {
        this.teachers = teachers;
    }

    public Boolean getDegreeStudents() {
        return degreeStudents;
    }

    public void setDegreeStudents(Boolean degreeStudents) {
        this.degreeStudents = degreeStudents;
    }

    public Boolean getEmployees() {
        return employees;
    }

    public void setEmployees(Boolean employees) {
        this.employees = employees;
    }

    public Boolean getMasterDegreeStudents() {
        return masterDegreeStudents;
    }

    public void setMasterDegreeStudents(Boolean masterDegreeStudents) {
        this.masterDegreeStudents = masterDegreeStudents;
    }

    public Boolean getExecutionCourseResponsibles() {
        return executionCourseResponsibles;
    }

    public void setExecutionCourseResponsibles(Boolean executionCourseResponsibles) {
        this.executionCourseResponsibles = executionCourseResponsibles;
    }

    public Boolean getDegreeCoordinators() {
        return degreeCoordinators;
    }

    public void setDegreeCoordinators(Boolean degreeCoordinators) {
        this.degreeCoordinators = degreeCoordinators;
    }

    public Boolean getMasterDegreeCoordinators() {
        return masterDegreeCoordinators;
    }

    public void setMasterDegreeCoordinators(Boolean masterDegreeCoordinators) {
        this.masterDegreeCoordinators = masterDegreeCoordinators;
    }

}
