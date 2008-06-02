package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class SendEmailBean implements Serializable {

    private String from = "fenix-noreply@ist.utl.pt";
    private String fromName = "";
    private String to = null;
    private String ccs = null;
    private String bccs = null;
    private String subject = null;
    private String message = null;

    private Boolean teachers = null;
    private Boolean employees = null;
    private Boolean students = null;
    private Boolean researchers = null;
    
    private Boolean bolonhaAdvancedFormationDiplomaStudents = null;
    private Boolean bolonhaDegreeStudents = null;
    private Boolean bolonhaIntegratedMasterDegreeStudents = null;
    private Boolean bolonhaMasterDegreeStudents = null;
    private Boolean bolonhaPhdProgramStudents = null;
    private Boolean bolonhaSpecializationDegreeStudents = null;
    private Boolean degreeStudents = null;
    private Boolean masterDegreeStudents = null;

    private Boolean bolonhaAdvancedFormationDiplomaCoordinators = null;
    private Boolean bolonhaDegreeCoordinators = null;
    private Boolean bolonhaIntegratedMasterDegreeCoordinators = null;
    private Boolean bolonhaMasterDegreeCoordinators = null;
    private Boolean bolonhaPhdProgramCoordinators = null;
    private Boolean bolonhaSpecializationDegreeCoordinators = null;
    private Boolean degreeCoordinators = null;
    private Boolean masterDegreeCoordinators = null;

    private Boolean executionCourseResponsibles = null;

    private Boolean sent = Boolean.FALSE;

    private Boolean allowChangeSender = Boolean.TRUE;

    public void send() throws FenixFilterException, FenixServiceException {
	final Object[] args = { getToList(), getCCList(), getBCCList(), getFromName(), getFrom(), getSubject(), getMessageWithFooter() };
	ServiceUtils.executeService(null, "commons.SendMail", args);
	sent = Boolean.TRUE;
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
		if (!person.hasRole(RoleType.TEACHER)) {
		    if (person.getEmail() != null && person.getEmail().length() > 0) {
			emails.add(person.getEmail());
		    }
		}
	    }
	}

	boolean useReachers = (getResearchers() == null) ? false : getResearchers();
	
	if(useReachers) {
	    final Role role = Role.getRoleByRoleType(RoleType.RESEARCHER);
	    for(final Person person : role.getAssociatedPersons()) {
		    if (person.getEmail() != null && person.getEmail().length() > 0) {
			emails.add(person.getEmail());
		    }
	    }
	}
	final boolean students = getStudents().booleanValue();
	final boolean bolonhaAdvancedFormationDiplomaStudents = getBolonhaAdvancedFormationDiplomaStudents().booleanValue();
	final boolean bolonhaDegreeStudents = getBolonhaDegreeStudents().booleanValue();
	final boolean bolonhaIntegratedMasterDegreeStudents = getBolonhaIntegratedMasterDegreeStudents().booleanValue();
	final boolean bolonhaMasterDegreeStudents = getBolonhaMasterDegreeStudents().booleanValue();
	final boolean bolonhaPhdProgramStudents = getBolonhaPhdProgramStudents().booleanValue();
	final boolean bolonhaSpecializationDegreeStudents = getBolonhaSpecializationDegreeStudents().booleanValue();
	final boolean degreeStudents = getDegreeStudents().booleanValue();
	final boolean masterDegreeStudents = getMasterDegreeStudents().booleanValue();

	if (students || bolonhaAdvancedFormationDiplomaStudents || bolonhaDegreeStudents || bolonhaIntegratedMasterDegreeStudents
		|| bolonhaMasterDegreeStudents || bolonhaPhdProgramStudents || bolonhaSpecializationDegreeStudents
		|| degreeStudents || masterDegreeStudents) {

	    final Set<Registration> registrations = RootDomainObject.getInstance().getRegistrationsSet();
	    for (final Registration registration : registrations) {
		if (registration.isActive()) {
		    final DegreeType degreeType = registration.getDegreeType();
		    if (students
			    || (bolonhaAdvancedFormationDiplomaStudents && degreeType == DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA)
			    || (bolonhaDegreeStudents && degreeType == DegreeType.BOLONHA_DEGREE)
			    || (bolonhaIntegratedMasterDegreeStudents && degreeType == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE)
			    || (bolonhaMasterDegreeStudents && degreeType == DegreeType.BOLONHA_MASTER_DEGREE)
			    || (bolonhaPhdProgramStudents && degreeType == DegreeType.BOLONHA_PHD_PROGRAM)
			    || (bolonhaSpecializationDegreeStudents && degreeType == DegreeType.BOLONHA_SPECIALIZATION_DEGREE)
			    || (degreeStudents && degreeType == DegreeType.DEGREE)
			    || (masterDegreeStudents && degreeType == DegreeType.MASTER_DEGREE)) {
			final String email = registration.getPerson().getEmail();
			if (email != null && email.length() > 0) {
			    emails.add(email);
			}			
		    }
		}
	    }
	}

	if (getBolonhaAdvancedFormationDiplomaCoordinators().booleanValue()) {
	    addEmailsForCoordinatorsByDegreeType(emails, DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA);
	}

	if (getBolonhaDegreeCoordinators().booleanValue()) {
	    addEmailsForCoordinatorsByDegreeType(emails, DegreeType.BOLONHA_DEGREE);
	}

	if (getBolonhaIntegratedMasterDegreeCoordinators().booleanValue()) {
	    addEmailsForCoordinatorsByDegreeType(emails, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
	}

	if (getBolonhaMasterDegreeCoordinators().booleanValue()) {
	    addEmailsForCoordinatorsByDegreeType(emails, DegreeType.BOLONHA_MASTER_DEGREE);
	}

	if (getBolonhaPhdProgramCoordinators().booleanValue()) {
	    addEmailsForCoordinatorsByDegreeType(emails, DegreeType.BOLONHA_PHD_PROGRAM);
	}

	if (getBolonhaSpecializationDegreeCoordinators().booleanValue()) {
	    addEmailsForCoordinatorsByDegreeType(emails, DegreeType.BOLONHA_SPECIALIZATION_DEGREE);
	}

	if (getDegreeCoordinators().booleanValue()) {
	    addEmailsForCoordinatorsByDegreeType(emails, DegreeType.DEGREE);
	}

	if (getMasterDegreeCoordinators().booleanValue()) {
	    addEmailsForCoordinatorsByDegreeType(emails, DegreeType.MASTER_DEGREE);
	}

	final Boolean executionCourseResponsibles = getExecutionCourseResponsibles();
	if (executionCourseResponsibles.booleanValue()) {
	    final Collection<ExecutionYear> executionYears = RootDomainObject.getInstance().getExecutionYearsSet();
	    for (final ExecutionYear executionYear : executionYears) {
		if (executionYear.isCurrent()) {
		    for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
			for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCourses()) {
			    for (final Professorship professorship : executionCourse.getProfessorships()) {
				if (professorship.isResponsibleFor()) {
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

    private void addEmailsForCoordinatorsByDegreeType(final List<String> emails, final DegreeType degreeType) throws FenixServiceException,
	    FenixFilterException {
	for (final ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYearsSet()) {
	    if (executionYear.isCurrent()) {
		for (final ExecutionDegree executionDegree : executionYear.getExecutionDegrees()) {
		    final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
		    final Degree degree = degreeCurricularPlan.getDegree();
		    if (degree.getDegreeType() == degreeType) {
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

    public String getMessageWithFooter() {
	final StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append(getMessage());
	stringBuilder.append("\n\n\n");
	stringBuilder.append("---\n");

	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.ManagerResources", Language.getLocale());
	stringBuilder.append(resourceBundle.getString("message.email.footer.prefix"));
	stringBuilder.append(getFromName());

	if (hasBooleanValueRecipients()) {
	    stringBuilder.append(resourceBundle.getString("message.email.footer.recipients.prefix"));
	    appendRecipients(resourceBundle, stringBuilder, teachers, "message.email.footer.recipients.teachers");
	    appendRecipients(resourceBundle, stringBuilder, employees, "message.email.footer.recipients.employees");
	    appendRecipients(resourceBundle, stringBuilder, students, "message.email.footer.recipients.students");
	    appendRecipients(resourceBundle, stringBuilder, researchers, "message.email.footer.recipients.researchers");
	    appendRecipients(resourceBundle, stringBuilder, bolonhaAdvancedFormationDiplomaStudents, "message.email.footer.recipients.bolonhaAdvancedFormationDiplomaStudents");
	    appendRecipients(resourceBundle, stringBuilder, bolonhaDegreeStudents, "message.email.footer.recipients.bolonhaDegreeStudents");
	    appendRecipients(resourceBundle, stringBuilder, bolonhaIntegratedMasterDegreeStudents, "message.email.footer.recipients.bolonhaIntegratedMasterDegreeStudents");
	    appendRecipients(resourceBundle, stringBuilder, bolonhaMasterDegreeStudents, "message.email.footer.recipients.bolonhaMasterDegreeStudents");
	    appendRecipients(resourceBundle, stringBuilder, bolonhaPhdProgramStudents, "message.email.footer.recipients.bolonhaPhdProgramStudents");
	    appendRecipients(resourceBundle, stringBuilder, bolonhaSpecializationDegreeStudents, "message.email.footer.recipients.bolonhaSpecializationDegreeStudents");
	    appendRecipients(resourceBundle, stringBuilder, degreeStudents, "message.email.footer.recipients.degreeStudents");
	    appendRecipients(resourceBundle, stringBuilder, masterDegreeStudents, "message.email.footer.recipients.masterDegreeStudents");
	    appendRecipients(resourceBundle, stringBuilder, bolonhaAdvancedFormationDiplomaCoordinators, "message.email.footer.recipients.bolonhaAdvancedFormationDiplomaCoordinators");
	    appendRecipients(resourceBundle, stringBuilder, bolonhaDegreeCoordinators, "message.email.footer.recipients.bolonhaDegreeCoordinators");
	    appendRecipients(resourceBundle, stringBuilder, bolonhaIntegratedMasterDegreeCoordinators, "message.email.footer.recipients.bolonhaIntegratedMasterDegreeCoordinators");
	    appendRecipients(resourceBundle, stringBuilder, bolonhaMasterDegreeCoordinators, "message.email.footer.recipients.bolonhaMasterDegreeCoordinators");
	    appendRecipients(resourceBundle, stringBuilder, bolonhaPhdProgramCoordinators, "message.email.footer.recipients.bolonhaPhdProgramCoordinators");
	    appendRecipients(resourceBundle, stringBuilder, bolonhaSpecializationDegreeCoordinators, "message.email.footer.recipients.bolonhaSpecializationDegreeCoordinators");
	    appendRecipients(resourceBundle, stringBuilder, degreeCoordinators, "message.email.footer.recipients.degreeCoordinators");
	    appendRecipients(resourceBundle, stringBuilder, masterDegreeCoordinators, "message.email.footer.recipients.masterDegreeCoordinators");
	    appendRecipients(resourceBundle, stringBuilder, executionCourseResponsibles, "message.email.footer.recipients.executionCourseResponsibles");
	} else {
	    stringBuilder.append(resourceBundle.getString("message.email.footer.recipients.none"));
	}

	return stringBuilder.toString();
    }


    private boolean hasBooleanValueRecipients() {
	return hasBooleanValueRecipients(teachers)
		|| hasBooleanValueRecipients(employees)
		|| hasBooleanValueRecipients(students)
		|| hasBooleanValueRecipients(researchers)	    
		|| hasBooleanValueRecipients(bolonhaAdvancedFormationDiplomaStudents)
		|| hasBooleanValueRecipients(bolonhaDegreeStudents)
		|| hasBooleanValueRecipients(bolonhaIntegratedMasterDegreeStudents)
		|| hasBooleanValueRecipients(bolonhaMasterDegreeStudents)
		|| hasBooleanValueRecipients(bolonhaPhdProgramStudents)
		|| hasBooleanValueRecipients(bolonhaSpecializationDegreeStudents)
		|| hasBooleanValueRecipients(degreeStudents)
		|| hasBooleanValueRecipients(masterDegreeStudents)
		|| hasBooleanValueRecipients(bolonhaAdvancedFormationDiplomaCoordinators)
		|| hasBooleanValueRecipients(bolonhaDegreeCoordinators)
		|| hasBooleanValueRecipients(bolonhaIntegratedMasterDegreeCoordinators)
		|| hasBooleanValueRecipients(bolonhaMasterDegreeCoordinators)
		|| hasBooleanValueRecipients(bolonhaPhdProgramCoordinators)
		|| hasBooleanValueRecipients(bolonhaSpecializationDegreeCoordinators)
		|| hasBooleanValueRecipients(degreeCoordinators)
		|| hasBooleanValueRecipients(masterDegreeCoordinators)
		|| hasBooleanValueRecipients(executionCourseResponsibles);
    }

    private boolean hasBooleanValueRecipients(final Boolean isRecipient) {
	return isRecipient != null && isRecipient.booleanValue();
    }

    private void appendRecipients(final ResourceBundle resourceBundle, final StringBuilder stringBuilder,
	    final Boolean isRecipient, final String key) {
	if (isRecipient != null && isRecipient.booleanValue()) {
	    stringBuilder.append("\n    ");
	    stringBuilder.append(resourceBundle.getString(key));
	}
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

    public Boolean getBolonhaAdvancedFormationDiplomaStudents() {
        return bolonhaAdvancedFormationDiplomaStudents;
    }

    public void setBolonhaAdvancedFormationDiplomaStudents(Boolean bolonhaAdvancedFormationDiplomaStudents) {
        this.bolonhaAdvancedFormationDiplomaStudents = bolonhaAdvancedFormationDiplomaStudents;
    }

    public Boolean getBolonhaDegreeStudents() {
        return bolonhaDegreeStudents;
    }

    public void setBolonhaDegreeStudents(Boolean bolonhaDegreeStudents) {
        this.bolonhaDegreeStudents = bolonhaDegreeStudents;
    }

    public Boolean getBolonhaIntegratedMasterDegreeStudents() {
        return bolonhaIntegratedMasterDegreeStudents;
    }

    public void setBolonhaIntegratedMasterDegreeStudents(Boolean bolonhaIntegratedMasterDegreeStudents) {
        this.bolonhaIntegratedMasterDegreeStudents = bolonhaIntegratedMasterDegreeStudents;
    }

    public Boolean getBolonhaMasterDegreeStudents() {
        return bolonhaMasterDegreeStudents;
    }

    public void setBolonhaMasterDegreeStudents(Boolean bolonhaMasterDegreeStudents) {
        this.bolonhaMasterDegreeStudents = bolonhaMasterDegreeStudents;
    }

    public Boolean getBolonhaPhdProgramStudents() {
        return bolonhaPhdProgramStudents;
    }

    public void setBolonhaPhdProgramStudents(Boolean bolonhaPhdProgramStudents) {
        this.bolonhaPhdProgramStudents = bolonhaPhdProgramStudents;
    }

    public Boolean getBolonhaSpecializationDegreeStudents() {
        return bolonhaSpecializationDegreeStudents;
    }

    public void setBolonhaSpecializationDegreeStudents(Boolean bolonhaSpecializationDegreeStudents) {
        this.bolonhaSpecializationDegreeStudents = bolonhaSpecializationDegreeStudents;
    }

    public Boolean getBolonhaAdvancedFormationDiplomaCoordinators() {
        return bolonhaAdvancedFormationDiplomaCoordinators;
    }

    public void setBolonhaAdvancedFormationDiplomaCoordinators(Boolean bolonhaAdvancedFormationDiplomaCoordinators) {
        this.bolonhaAdvancedFormationDiplomaCoordinators = bolonhaAdvancedFormationDiplomaCoordinators;
    }

    public Boolean getBolonhaDegreeCoordinators() {
        return bolonhaDegreeCoordinators;
    }

    public void setBolonhaDegreeCoordinators(Boolean bolonhaDegreeCoordinators) {
        this.bolonhaDegreeCoordinators = bolonhaDegreeCoordinators;
    }

    public Boolean getBolonhaIntegratedMasterDegreeCoordinators() {
        return bolonhaIntegratedMasterDegreeCoordinators;
    }

    public void setBolonhaIntegratedMasterDegreeCoordinators(Boolean bolonhaIntegratedMasterDegreeCoordinators) {
        this.bolonhaIntegratedMasterDegreeCoordinators = bolonhaIntegratedMasterDegreeCoordinators;
    }

    public Boolean getBolonhaMasterDegreeCoordinators() {
        return bolonhaMasterDegreeCoordinators;
    }

    public void setBolonhaMasterDegreeCoordinators(Boolean bolonhaMasterDegreeCoordinators) {
        this.bolonhaMasterDegreeCoordinators = bolonhaMasterDegreeCoordinators;
    }

    public Boolean getBolonhaPhdProgramCoordinators() {
        return bolonhaPhdProgramCoordinators;
    }

    public void setBolonhaPhdProgramCoordinators(Boolean bolonhaPhdProgramCoordinators) {
        this.bolonhaPhdProgramCoordinators = bolonhaPhdProgramCoordinators;
    }

    public Boolean getBolonhaSpecializationDegreeCoordinators() {
        return bolonhaSpecializationDegreeCoordinators;
    }

    public void setBolonhaSpecializationDegreeCoordinators(Boolean bolonhaSpecializationDegreeCoordinators) {
        this.bolonhaSpecializationDegreeCoordinators = bolonhaSpecializationDegreeCoordinators;
    }

    public Boolean getStudents() {
        return students;
    }

    public void setStudents(Boolean students) {
        this.students = students;
    }

    public Boolean getSent() {
        return sent;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    public Boolean getResearchers() {
        return researchers;
    }

    public void setResearchers(Boolean researchers) {
        this.researchers = researchers;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public Boolean isAllowChangeSender() {
        return allowChangeSender;
    }

    public Boolean getAllowChangeSender() {
        return allowChangeSender;
    }

    public void setAllowChangeSender(Boolean allowChangeSender) {
        this.allowChangeSender = allowChangeSender;
    }

}
