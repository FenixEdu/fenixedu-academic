/**
 * Nov 28, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.domain.teacher.TeacherAdviseService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class EditTeacherAdviseService extends Service {

    public void run(Integer teacherID, Integer executionPeriodID, final Integer studentNumber,
            Double percentage, AdviseType adviseType, RoleType roleType) throws ExcepcaoPersistencia, FenixServiceException {

        Teacher teacher = rootDomainObject.readTeacherByOID(teacherID);
        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);

        List<Registration> students = rootDomainObject.getStudents();
        Registration student = (Registration) CollectionUtils.find(students, new Predicate() {
            public boolean evaluate(Object arg0) {
                Registration tempStudent = (Registration) arg0;
                return tempStudent.getNumber().equals(studentNumber);
            }
        });

        if (student == null) {
            throw new FenixServiceException("errors.invalid.student-number");
        }

        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        if (teacherService == null) {
            teacherService = new TeacherService(teacher, executionPeriod);
        }
        List<Advise> advises = student.getAdvisesByTeacher(teacher);
        Advise advise = null;
        if (advises == null || advises.isEmpty()) {
            advise = new Advise(teacher, student, adviseType, executionPeriod, executionPeriod);
        } else {
            advise = advises.iterator().next();
        }

        TeacherAdviseService teacherAdviseService = advise.getTeacherAdviseServiceByExecutionPeriod(executionPeriod);
        if (teacherAdviseService == null) {
            teacherAdviseService = new TeacherAdviseService(teacherService, advise, percentage, roleType);
        } else {
            teacherAdviseService.updatePercentage(percentage, roleType);
        }
    }
}