/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.library;

import java.io.Serializable;
import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.space.Space;

/**
 * @author - Pedro Amaral
 * 
 */
public class SelectLibraryBean implements Serializable {

    private Space library;
    
    private String personId;

    private String libraryCardNumber;

    private Person person;
    
    private boolean personInside;

    private String personEntranceTime;

    private boolean editableCardNumber;

    private boolean editCardNumberError;

    private ArrayList<AttendanceInformation> attendances;

    private int attendancesCount;

    public SelectLibraryBean() {
	this.attendances = new ArrayList<SelectLibraryBean.AttendanceInformation>();
	this.attendancesCount = attendances.size();
    }

    public void setLibrary(Space library) {
	this.library = library;
    }

    public Space getLibrary() {
	return library;
    }

    public void setPersonId(String personId) {
	this.personId = personId;
    }

    public String getPersonId() {
	return personId;
    }

    public void setPerson(Person person) {
	this.person = person;
	if (person != null && person.getLibraryCard() != null && person.getLibraryCard().getCardNumber() != null) {
	    setEditableCardNumber(true);
	} else {
	    setEditableCardNumber(false);
	}
    }

    public Person getPerson() {
	return person;
    }

    public void setPersonInside(boolean personInside) {
	this.personInside = personInside;
    }

    public boolean isPersonInside() {
	return personInside;
    }

    public void resetPersonInformation() {
	this.setPerson(null);
	this.setPersonId(null);
	this.setPersonInside(false);
	this.attendances = new ArrayList<AttendanceInformation>();
	this.attendancesCount = attendances.size();
    }

    public void setLibraryCardNumber(String libraryCardNumber) {
	this.libraryCardNumber = libraryCardNumber;
    }

    public String getLibraryCardNumber() {
	return libraryCardNumber;
    }

    public void setEditableCardNumber(boolean editableCardNumber) {
	this.editableCardNumber = editableCardNumber;
    }

    public boolean isEditableCardNumber() {
	return editableCardNumber;
    }

    public void setAttendances(ArrayList<AttendanceInformation> attendances) {
	this.attendancesCount = attendances.size();
	this.attendances = attendances;
    }

    public ArrayList<AttendanceInformation> getAttendances() {
	return attendances;
    }

    public void addAttendance(Person person, String entranceInfo) {
	attendances.add(new AttendanceInformation(person, entranceInfo));
	this.attendancesCount = attendances.size();
    }

    public void removeAttendance(String istUsername) {
	for (AttendanceInformation attInfo : attendances) {
	    if (attInfo.getPerson().getIstUsername().equals(istUsername)) {
		attendances.remove(attInfo);
		this.attendancesCount = attendances.size();
		return;
	    }
	}
    }

    public void setEditCardNumberError(boolean editCardNumberError) {
	this.editCardNumberError = editCardNumberError;
    }

    public boolean isEditCardNumberError() {
	return editCardNumberError;
    }

    public void setAttendancesCount(int attendancesCount) {
	this.attendancesCount = attendancesCount;
    }

    public int getAttendancesCount() {
	return attendancesCount;
    }

    public void setPersonEntranceTime(String personEntranceTime) {
	this.personEntranceTime = personEntranceTime;
    }

    public String getPersonEntranceTime() {
	return personEntranceTime;
    }


    public class AttendanceInformation implements Serializable {

	private Person person;

	private String entranceInfo;

	public AttendanceInformation(Person person, String entranceInfo) {
	    this.person = person;
	    this.entranceInfo = entranceInfo;
	}

	public void setPerson(Person person) {
	    this.person = person;
	}

	public Person getPerson() {
	    return person;
	}

	public void setEntranceInfo(String entranceInfo) {
	    this.entranceInfo = entranceInfo;
	}

	public String getEntranceInfo() {
	    return entranceInfo;
	}
    }

}
