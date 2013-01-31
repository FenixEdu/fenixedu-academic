package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceLog;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageDegreeTeachingServicesDispatchAction.ShiftIDTeachingPercentage;
import net.sourceforge.fenixedu.util.BundleUtil;

public class UpdateDegreeTeachingServices extends FenixService {

	public void run(Integer professorshipID, List<ShiftIDTeachingPercentage> shiftsIDsTeachingPercentages, RoleType roleType) {

		Professorship professorship = rootDomainObject.readProfessorshipByOID(professorshipID);
		Teacher teacher = professorship.getTeacher();
		ExecutionSemester executionSemester = professorship.getExecutionCourse().getExecutionPeriod();
		TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
		if (teacherService == null) {
			teacherService = new TeacherService(teacher, executionSemester);
		}

		final StringBuilder log = new StringBuilder();
		log.append(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
				"label.teacher.schedule.change"));

		for (ShiftIDTeachingPercentage shiftIDTeachingPercentage : shiftsIDsTeachingPercentages) {
			Shift shift = rootDomainObject.readShiftByOID(shiftIDTeachingPercentage.getShiftID());
			DegreeTeachingService degreeTeachingService =
					teacherService.getDegreeTeachingServiceByShiftAndProfessorship(shift, professorship);
			if (degreeTeachingService != null) {
				degreeTeachingService.updatePercentage(shiftIDTeachingPercentage.getPercentage(), roleType);
			} else {
				if (shiftIDTeachingPercentage.getPercentage() == null
						|| (shiftIDTeachingPercentage.getPercentage() != null && shiftIDTeachingPercentage.getPercentage() != 0)) {
					new DegreeTeachingService(teacherService, professorship, shift, shiftIDTeachingPercentage.getPercentage(),
							roleType);
				}
			}

			log.append(shift.getPresentationName());
			if (shiftIDTeachingPercentage.getPercentage() != null) {
				log.append("= ");
				log.append(shiftIDTeachingPercentage.getPercentage());
			}
			log.append("% ; ");
		}

		new TeacherServiceLog(teacherService, log.toString());
	}
}
