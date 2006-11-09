package net.sourceforge.fenixedu.domain.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

import org.apache.commons.beanutils.BeanComparator;

public class Student extends Student_Base {

    public final static Comparator<Student> NUMBER_COMPARATOR = new BeanComparator("number");

    public Student(Person person, Integer number) {
	super();
	setPerson(person);
	if(number == null){
	    number = Student.generateStudentNumber();
	}
	setNumber(number);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Student(Person person) {
	this(person, Student.generateStudentNumber());
    }

    public static Student readStudentByNumber(Integer studentNumber) {
	for (Student student : RootDomainObject.getInstance().getStudents()) {
	    if (student.getNumber().equals(studentNumber)) {
		return student;
	    }
	}
	return null;
    }

    public Collection<Registration> getRegistrationsByDegreeType(DegreeType degreeType) {
	List<Registration> result = new ArrayList<Registration>();
	for (Registration registration : getRegistrations()) {
	    if (registration.getDegreeType().equals(degreeType)) {
		result.add(registration);
	    }
	}
	return result;
    }

    public Registration getActiveRegistrationByDegreeType(DegreeType degreeType) {
	for (Registration registration : getRegistrations()) {
	    if (registration.getDegreeType().equals(degreeType) && registration.isActive()) {
		return registration;
	    }
	}
	return null;
    }

    public static Integer generateStudentNumber() {
	Integer nextNumber = 0;
	for (Student student : RootDomainObject.getInstance().getStudents()) {
	    if (student.getNumber() < 100000 && student.getNumber() > nextNumber) {
		nextNumber = student.getNumber();
	    }
	}
	return nextNumber + 1;
    }

    public StudentDataByExecutionYear getActualExecutionYearStudentData() {
	for (StudentDataByExecutionYear studentData : getStudentDataByExecutionYear()) {
	    if (studentData.getExecutionYear().isCurrent()) {
		return studentData;
	    }
	}
	return null;
    }

    public StudentDataByExecutionYear getStudentDataByExecutionYear(ExecutionYear executionYear) {
	for (StudentDataByExecutionYear studentData : getStudentDataByExecutionYear()) {
	    if (studentData.getExecutionYear().equals(executionYear)) {
		return studentData;
	    }
	}
	return null;
    }

    public ResidenceCandidacies getResidenceCandidacyForCurrentExecutionYear() {
	if (getActualExecutionYearStudentData() == null) {
	    return null;
	}
	return getActualExecutionYearStudentData().getResidenceCandidacy();
    }

    public void setResidenceCandidacyForCurrentExecutionYear(String observations) {
	createCurrentYearStudentData();
	getActualExecutionYearStudentData()
		.setResidenceCandidacy(new ResidenceCandidacies(observations));
    }

    public void setResidenceCandidacy(ResidenceCandidacies residenceCandidacy) {
	ExecutionYear executionYear = ExecutionYear.getExecutionYearByDate(residenceCandidacy
		.getCreationDateDateTime().toYearMonthDay());
	StudentDataByExecutionYear studentData = getStudentDataByExecutionYear(executionYear);
	if (studentData == null) {
	    studentData = createStudentDataForExecutionYear(executionYear);
	}
	studentData.setResidenceCandidacy(residenceCandidacy);
    }

    public boolean getWorkingStudentForCurrentExecutionYear() {
	if (getActualExecutionYearStudentData() == null) {
	    return false;
	}
	return getActualExecutionYearStudentData().getWorkingStudent();
    }

    public void setWorkingStudentForCurrentExecutionYear() {
	createCurrentYearStudentData();
	getActualExecutionYearStudentData().setWorkingStudent(true);
    }

    public StudentPersonalDataAuthorizationChoice getPersonalDataAuthorizationForCurrentExecutionYear() {
	if (getActualExecutionYearStudentData() == null) {
	    return null;
	}
	return getActualExecutionYearStudentData().getPersonalDataAuthorization();
    }

    public void setPersonalDataAuthorizationForCurrentExecutionYear(
	    StudentPersonalDataAuthorizationChoice personalDataAuthorization) {
	createCurrentYearStudentData();
	getActualExecutionYearStudentData().setPersonalDataAuthorization(personalDataAuthorization);
    }

    public void setPersonalDataAuthorizationForExecutionYear(
	    StudentPersonalDataAuthorizationChoice personalDataAuthorization, ExecutionYear executionYear) {
	StudentDataByExecutionYear studentData = getStudentDataByExecutionYear(executionYear);
	if (studentData == null) {
	    studentData = createStudentDataForExecutionYear(executionYear);
	}
	studentData.setPersonalDataAuthorization(personalDataAuthorization);
    }

    private void createCurrentYearStudentData() {
	if (getActualExecutionYearStudentData() == null) {
	    new StudentDataByExecutionYear(this);
	}
    }

    private StudentDataByExecutionYear createStudentDataForExecutionYear(ExecutionYear executionYear) {
	if (getStudentDataByExecutionYear(executionYear) == null) {
	    return new StudentDataByExecutionYear(this, executionYear);
	}
	return getStudentDataByExecutionYear(executionYear);
    }

    public DegreeType getMostSignificantDegreeType() {
        if(isStudentOfDegreeType(DegreeType.MASTER_DEGREE)) return DegreeType.MASTER_DEGREE;
        if(isStudentOfDegreeType(DegreeType.DEGREE)) return DegreeType.DEGREE;
        if(isStudentOfDegreeType(DegreeType.BOLONHA_SPECIALIZATION_DEGREE)) return DegreeType.BOLONHA_SPECIALIZATION_DEGREE;
        if(isStudentOfDegreeType(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA)) return DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA;
        if(isStudentOfDegreeType(DegreeType.BOLONHA_PHD_PROGRAM)) return DegreeType.BOLONHA_PHD_PROGRAM;
        if(isStudentOfDegreeType(DegreeType.BOLONHA_MASTER_DEGREE)) return DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE;
        if(isStudentOfDegreeType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE)) return DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE;
        if(isStudentOfDegreeType(DegreeType.BOLONHA_DEGREE)) return DegreeType.BOLONHA_DEGREE;
        return null;
    }
    
    private boolean isStudentOfDegreeType(DegreeType degreeType) {
        for (Registration registration: getRegistrationsByDegreeType(degreeType)) {
            if(registration.isActive()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasActiveRegistrationForOffice(Unit office){	
	Set<Registration> registrations = getRegistrationsSet();
	for (Registration registration : registrations) {
	    if(registration.isActiveForOffice(office)){
		return true;
	    }
	}	
	return false;
    }
    
    // Convenience method for invocation as bean.
    public boolean getHasActiveRegistrationForOffice(){
	Unit workingPlace = AccessControl.getUserView().getPerson().getEmployee().getCurrentWorkingPlace();
	return hasActiveRegistrationForOffice(workingPlace);
    }
    
    public boolean hasRegistrationForOffice(final AdministrativeOffice administrativeOffice){	
	Set<Registration> registrations = getRegistrationsSet();
	for (Registration registration : registrations) {
	    if(registration.isForOffice(administrativeOffice)){
		return true;
	    }
	}	
	return false;
    }
    
    public boolean attends(ExecutionCourse executionCourse) {
	for (final Registration registration : getRegistrationsSet()) {
	    if (registration.attends(executionCourse)) {
		return true;		
	    }
	}
	return false;
    }    
    
    public boolean hasAnyActiveRegistration() {
	for (final Registration registration : getRegistrationsSet()) {
	    if (registration.isActive()) {
		return true;
	    }
	}

	return false;
    }

    public void delete(){
	
	if(getRegistrationsCount() > 0){
	    throw new DomainException("error.person.cannot.be.deleted");
	}

	if(getStudentDataByExecutionYearCount() > 0){
	    throw new DomainException("error.person.cannot.be.deleted");
	}

	removePerson();
	
	deleteDomainObject();
    }
    
}
