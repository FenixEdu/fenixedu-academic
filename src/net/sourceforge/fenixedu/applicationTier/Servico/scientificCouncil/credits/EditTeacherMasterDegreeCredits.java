/**
 * Feb 4, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.credits;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherMasterDegreeService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class EditTeacherMasterDegreeCredits extends Service {

    public void run(Map hoursMap, Map creditsMap) throws ExcepcaoPersistencia, NumberFormatException {

        Set<String> professorshipIDs = new HashSet(hoursMap.keySet());
        professorshipIDs.addAll(creditsMap.keySet());

        for (String stringID : professorshipIDs) {
            Integer professorshipID = Integer.parseInt(stringID);
            String creditsString = (String) creditsMap.get(stringID);
            String hoursString = (String) hoursMap.get(stringID);
            if (hoursString.equals("") && creditsString.equals("")) {
                continue;
            }
            Professorship professorship = (Professorship) persistentObject.readByOID(
                    Professorship.class, professorshipID);
            Teacher teacher = professorship.getTeacher();
            ExecutionPeriod executionPeriod = professorship.getExecutionCourse().getExecutionPeriod();
            TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
            if (teacherService == null) {
                teacherService = DomainFactory.makeTeacherService(teacher, executionPeriod);
            }
            TeacherMasterDegreeService teacherMasterDegreeService = teacherService
                    .getMasterDegreeServiceByProfessorship(professorship);
            if (teacherMasterDegreeService == null) {
                teacherMasterDegreeService = DomainFactory.makeTeacherMasterDegreeService(
                        teacherService, professorship);
            }
            Double credits = null;
            Double hours = null;
            if (!creditsString.equals("")) {
                credits = Double.parseDouble(creditsString);
            }
            if (!hoursString.equals("")) {
                hours = Double.parseDouble(hoursString);
            }
            teacherMasterDegreeService.updateValues(hours, credits);
        }
    }
}
