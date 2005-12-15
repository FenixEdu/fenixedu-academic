/**
 * Nov 17, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import java.util.List;

import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.teacher.IDegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.ITeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.ManageDegreeTeachingServicesDispatchAction.ShiftIDTeachingPercentage;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class UpdateDegreeTeachingServices implements IService {

    public void run(Integer professorshipID, List<ShiftIDTeachingPercentage> shiftsIDsTeachingPercentages)
            throws ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IProfessorship professorship = (IProfessorship) persistentSupport.getIPersistentProfessorship().readByOID(
                Professorship.class, professorshipID);        

        ITeacher teacher = professorship.getTeacher();
        IExecutionPeriod executionPeriod = professorship.getExecutionCourse().getExecutionPeriod();
        ITeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        if (teacherService == null) {
            teacherService = DomainFactory.makeTeacherService(teacher, executionPeriod);
        }
        for (ShiftIDTeachingPercentage shiftIDTeachingPercentage : shiftsIDsTeachingPercentages) {
            IShift shift = (IShift) persistentSupport.getITurnoPersistente().readByOID(Shift.class,
                    shiftIDTeachingPercentage.getShiftID());
            IDegreeTeachingService degreeTeachingService = teacherService
                    .getDegreeTeachingServiceByShiftAndExecutionCourse(shift, professorship.getExecutionCourse());
            if (degreeTeachingService != null) {
                degreeTeachingService.updatePercentage(shiftIDTeachingPercentage.getPercentage());
            } else {
                DomainFactory.makeDegreeTeachingService(teacherService, professorship, shift,
                        shiftIDTeachingPercentage.getPercentage());
            }
        }
    }
}
