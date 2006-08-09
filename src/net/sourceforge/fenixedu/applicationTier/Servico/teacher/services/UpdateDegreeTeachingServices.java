package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageDegreeTeachingServicesDispatchAction.ShiftIDTeachingPercentage;

public class UpdateDegreeTeachingServices extends Service {

    public void run(Integer professorshipID, List<ShiftIDTeachingPercentage> shiftsIDsTeachingPercentages, RoleType roleType)
            throws ExcepcaoPersistencia {
        
        Professorship professorship = rootDomainObject.readProfessorshipByOID(professorshipID);        
        Teacher teacher = professorship.getTeacher();
        ExecutionPeriod executionPeriod = professorship.getExecutionCourse().getExecutionPeriod();
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        if (teacherService == null) {
            teacherService = new TeacherService(teacher, executionPeriod);
        }
        
        for (ShiftIDTeachingPercentage shiftIDTeachingPercentage : shiftsIDsTeachingPercentages) {
            Shift shift = rootDomainObject.readShiftByOID(shiftIDTeachingPercentage.getShiftID());
            DegreeTeachingService degreeTeachingService = teacherService
                    .getDegreeTeachingServiceByShiftAndProfessorship(shift, professorship);
            if (degreeTeachingService != null) {
                degreeTeachingService.updatePercentage(shiftIDTeachingPercentage.getPercentage(), roleType);
            } else {
                new DegreeTeachingService(teacherService, professorship, shift,
                        shiftIDTeachingPercentage.getPercentage(), roleType);
            }
        }
    }
}
