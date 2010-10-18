package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

public class StudentContextSelectionBean implements Serializable {

    private AcademicInterval academicInterval;
    private String number;

    public StudentContextSelectionBean(AcademicInterval academicInterval) {
	this.academicInterval = academicInterval;
    }

    public StudentContextSelectionBean() {
	this(AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER));
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

}
