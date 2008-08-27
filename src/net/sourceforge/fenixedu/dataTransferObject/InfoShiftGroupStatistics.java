package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.util.NumberUtils;

public class InfoShiftGroupStatistics extends InfoObject {

    private List<InfoShift> shiftsInGroup;

    public InfoShiftGroupStatistics() {
	this.shiftsInGroup = new ArrayList();
    }

    public List getShiftsInGroup() {
	return shiftsInGroup;
    }

    public Integer getTotalCapacity() {
	Integer totalCapacity = Integer.valueOf(0);

	Iterator iterator = this.shiftsInGroup.iterator();
	while (iterator.hasNext()) {
	    InfoShift infoShift = (InfoShift) iterator.next();
	    totalCapacity = Integer.valueOf(totalCapacity.intValue() + infoShift.getLotacao().intValue());
	}

	return totalCapacity;
    }

    public Double getTotalPercentage() {
	Integer totalCapacity = Integer.valueOf(0);
	Integer students = Integer.valueOf(0);

	for (InfoShift infoShift : this.shiftsInGroup) {
	    students += infoShift.getOcupation();
	    totalCapacity += infoShift.getLotacao();
	}

	if (students == 0) {
	    // No calculations necessary
	    return 0.0;
	}
	return NumberUtils.formatNumber(new Double(students.floatValue() * 100 / totalCapacity.floatValue()), 1);
    }

    public void setShiftsInGroup(List shiftsInGroup) {
	this.shiftsInGroup = shiftsInGroup;
    }

    public Integer getTotalNumberOfStudents() {
	Integer totalNumberOfStudents = Integer.valueOf(0);

	Iterator iterator = this.shiftsInGroup.iterator();
	while (iterator.hasNext()) {
	    InfoShift infoShift = (InfoShift) iterator.next();
	    totalNumberOfStudents = Integer.valueOf(totalNumberOfStudents.intValue() + infoShift.getOcupation().intValue());
	}

	return totalNumberOfStudents;
    }

}
