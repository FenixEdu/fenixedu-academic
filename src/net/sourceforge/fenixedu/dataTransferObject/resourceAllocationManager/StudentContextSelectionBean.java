package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

public class StudentContextSelectionBean implements Serializable {

    private AcademicInterval academicInterval;
    private String number;
    private boolean toEdit;

    public StudentContextSelectionBean(AcademicInterval academicInterval) {
	this.academicInterval = academicInterval;
	this.toEdit = false;
    }

    public StudentContextSelectionBean() {
	this(AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER));
	this.toEdit = false;
    }

    public AcademicInterval getAcademicInterval() {
	return academicInterval;
    }

    public void setAcademicInterval(AcademicInterval academicInterval) {
	this.academicInterval = academicInterval;
    }

    public String getNumber() {
	return number;
    }

    public void setNumber(String number) {
	this.number = number;
    }

    public boolean getToEdit() {
	return toEdit;
    }

    public void setToEdit(boolean toEdit) {
	this.toEdit = toEdit;
    }

}
