package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.credits;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherMasterDegreeService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditTeacherMasterDegreeCredits {

    @Atomic
    public static void run(Map<String, String> hoursMap, Map<String, String> creditsMap) throws NumberFormatException {
        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);
        Set<String> professorshipIDs = new HashSet<String>(hoursMap.keySet());
        professorshipIDs.addAll(creditsMap.keySet());

        for (String stringID : professorshipIDs) {
            String creditsString = creditsMap.get(stringID);
            String hoursString = hoursMap.get(stringID);
            if (hoursString.equals("") && creditsString.equals("")) {
                continue;
            }
            Professorship professorship = FenixFramework.getDomainObject(stringID);
            Teacher teacher = professorship.getTeacher();
            ExecutionSemester executionSemester = professorship.getExecutionCourse().getExecutionPeriod();

            TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
            if (teacherService == null) {
                teacherService = new TeacherService(teacher, executionSemester);
            }

            TeacherMasterDegreeService teacherMasterDegreeService =
                    teacherService.getMasterDegreeServiceByProfessorship(professorship);
            if (teacherMasterDegreeService == null) {
                teacherMasterDegreeService = new TeacherMasterDegreeService(teacherService, professorship);
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