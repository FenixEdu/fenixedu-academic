package net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProfessorship;

public class TSDProfessorshipDTOEntry extends DataTranferObject {
    private List<TSDProfessorship> tsdProfessorshipList;
    private List<ExecutionSemester> executionPeriodList = null;

    public TSDProfessorshipDTOEntry(List<TSDProfessorship> tsdProfessorshipList, List<ExecutionSemester> executionPeriodList) {
	this.tsdProfessorshipList = tsdProfessorshipList;
	this.executionPeriodList = executionPeriodList;
    }

    public TSDCourseDTOEntry getTSDCourseDTOEntry() {
	return new TSDCourseDTOEntry(tsdProfessorshipList.get(0).getTSDCourse(), executionPeriodList);
    }

    public TSDTeacherDTOEntry getTSDTeacherDTOEntry() {
	return new TSDTeacherDTOEntry(tsdProfessorshipList.get(0).getTSDTeacher(), executionPeriodList);
    }

    /*
     * public TSDProfessorship getTSDProfessorship() { return tsdProfessorship;
     * }
     */

    public Double getHoursByShiftType(ShiftType type) {
	Double hours = 0d;

	for (TSDProfessorship professorship : tsdProfessorshipList) {
	    if (professorship.getType().equals(type)) {
		hours += professorship.getHours();
	    }
	}

	return hours;
    }

    public Double getTotalHours() {
	Double hours = 0d;

	for (TSDProfessorship professorship : tsdProfessorshipList) {
	    hours += professorship.getHours();
	}

	return hours;
    }

    public List<TSDProfessorship> getTSDProfessorships() {
	return tsdProfessorshipList;
    }
}
