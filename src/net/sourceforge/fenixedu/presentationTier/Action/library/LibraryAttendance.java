package net.sourceforge.fenixedu.presentationTier.Action.library;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.SpaceAttendances;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.services.Service;

public class LibraryAttendance implements Serializable {
    public static class PlaceProvider implements DataProvider {
	@Override
	public Object provide(Object source, Object currentValue) {
	    LibraryAttendance attendance = (LibraryAttendance) source;
	    Set<Space> availableSpaces = new HashSet<Space>();
	    for (Space space : attendance.getLibrary().getContainedSpacesSet()) {
		if (!space.hasCurrentAttendance()) {
		    availableSpaces.add(space);
		}
	    }
	    return availableSpaces;
	}

	@Override
	public Converter getConverter() {
	    return new DomainObjectKeyConverter();
	}
    }

    private Space library;

    private String personId;

    private Person person;

    private String personLibraryCardNumber;

    private Space selectedSpace;

    private SpaceAttendances personAttendance;

    private Unit teacherUnit;

    private Unit researcherUnit;

    private Unit grantOwnerUnit;

    private Unit employeeUnit;

    private Registration studentRegistration;

    private Registration alumniRegistration;

    public LibraryAttendance() {
    }

    public LibraryAttendance(String personId, Space library) {
	setLibrary(library);
	setPersonId(personId);
    }

    public LibraryAttendance(SpaceAttendances attendance, Space library) {
	setLibrary(library);
	setPersonId(attendance.getPersonIstUsername());
	if (attendance.getPerson() != null) {
	    setPerson(attendance.getPerson());
	}
    }

    public Space getLibrary() {
	return library;
    }

    public void setLibrary(Space library) {
	this.library = library;
    }

    public String getPersonId() {
	return personId;
    }

    public void setPersonId(String personId) {
	this.personId = personId;
    }

    public Person getPerson() {
	return person;
    }

    public void setPerson(Person person) {
	this.person = person;
	teacherUnit = null;
	researcherUnit = null;
	grantOwnerUnit = null;
	employeeUnit = null;
	studentRegistration = null;
	alumniRegistration = null;
	setPersonLibraryCardNumber(null);
	setSelectedSpace(null);
	setPersonAttendance(null);
	if (person != null) {
	    setPersonLibraryCardNumber(person.getLibraryCardNumber());
	    for (Space space : library.getContainedSpacesSet()) {
		if (space.hasCurrentAttendance() && person.equals(space.getCurrentAttendance().getPerson())) {
		    setPersonAttendance(space.getCurrentAttendance());
		    setSelectedSpace(space);
		}
	    }
	    if (person.hasPersonProfessionalData()) {
		if (person.getPersonProfessionalData().getGiafProfessionalDataByCategoryType(CategoryType.TEACHER) != null) {
		    teacherUnit = person.getWorkingPlaceUnitForAnyRoleType();
		} else if (person.getPersonProfessionalData().getGiafProfessionalDataByCategoryType(CategoryType.RESEARCHER) != null) {
		    researcherUnit = person.getWorkingPlaceUnitForAnyRoleType();
		    // TODO: when grant owners are imported from GIAF uncomment
		    // this line and remove the grant owner special case in the
		    // bottom.
		    // } else if
		    // (person.getPersonProfessionalData().getGiafProfessionalDataByCategoryType(CategoryType.GRANT_OWNER)
		    // != null) {
		    // grantOwnerUnit = person.getWorkingPlaceForAnyRoleType();
		} else if (person.getPersonProfessionalData().getGiafProfessionalDataByCategoryType(CategoryType.EMPLOYEE) != null) {
		    employeeUnit = person.getWorkingPlaceUnitForAnyRoleType();
		}
	    }
	    if (person.hasGrantOwner()) {
		grantOwnerUnit = person.getWorkingPlaceUnitForAnyRoleType();
	    }
	    if (person.hasStudent()) {
		studentRegistration = person.getStudent().getLastActiveRegistration();
		if (studentRegistration == null) {
		    alumniRegistration = person.getStudent().getLastConcludedRegistration();
		}
	    }
	}
    }

    public Unit getTeacherUnit() {
	return teacherUnit;
    }

    public Unit getResearcherUnit() {
	return researcherUnit;
    }

    public Unit getGrantOwnerUnit() {
	return grantOwnerUnit;
    }

    public Unit getEmployeeUnit() {
	return employeeUnit;
    }

    public Registration getStudentRegistration() {
	return studentRegistration;
    }

    public Registration getAlumniRegistration() {
	return alumniRegistration;
    }

    public String getPersonLibraryCardNumber() {
	return personLibraryCardNumber;
    }

    public void setPersonLibraryCardNumber(String personLibraryCardNumber) {
	this.personLibraryCardNumber = personLibraryCardNumber;
    }

    public Space getSelectedSpace() {
	return selectedSpace;
    }

    public void setSelectedSpace(Space selectedSpace) {
	this.selectedSpace = selectedSpace;
    }

    public SpaceAttendances getPersonAttendance() {
	return personAttendance;
    }

    public void setPersonAttendance(SpaceAttendances personAttendance) {
	this.personAttendance = personAttendance;
    }

    public void generateCardNumber() {
	setPersonLibraryCardNumber("random stuff");
    }

    @Service
    public void saveCardNumber() {
	getPerson().setLibraryCardNumber(getPersonLibraryCardNumber());
    }

    @Service
    public void enterSpace() {
	setPersonAttendance(getSelectedSpace().addAttendance(getPerson(), AccessControl.getPerson().getIstUsername()));
    }

    @Service
    public void exitSpace() {
	getPersonAttendance().exit(AccessControl.getPerson().getIstUsername());
	if (getPerson() != null && getPerson().equals(getPersonAttendance().getPerson())) {
	    setPersonAttendance(null);
	    setSelectedSpace(null);
	}
    }
}