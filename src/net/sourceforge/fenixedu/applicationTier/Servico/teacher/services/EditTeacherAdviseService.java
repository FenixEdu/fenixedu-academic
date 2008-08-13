/**
 * Nov 28, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
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

    public void run(Integer teacherID, Integer executionPeriodID, final Integer studentNumber, Double percentage,
	    AdviseType adviseType, RoleType roleType) throws FenixServiceException {

	Teacher teacher = rootDomainObject.readTeacherByOID(teacherID);
	ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodID);

	List<Registration> students = rootDomainObject.getRegistrations();
	Registration registration = (Registration) CollectionUtils.find(students, new Predicate() {
	    public boolean evaluate(Object arg0) {
		Registration tempStudent = (Registration) arg0;
		return tempStudent.getNumber().equals(studentNumber);
	    }
	});

	if (registration == null) {
	    throw new FenixServiceException("errors.invalid.student-number");
	}

	TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
	if (teacherService == null) {
	    teacherService = new TeacherService(teacher, executionSemester);
	}
	List<Advise> advises = registration.getAdvisesByTeacher(teacher);
	Advise advise = null;
	if (advises == null || advises.isEmpty()) {
	    advise = new Advise(teacher, registration, adviseType, executionSemester, executionSemester);
	} else {
	    advise = advises.iterator().next();
	}

	TeacherAdviseService teacherAdviseService = advise.getTeacherAdviseServiceByExecutionPeriod(executionSemester);
	if (teacherAdviseService == null) {
	    teacherAdviseService = new TeacherAdviseService(teacherService, advise, percentage, roleType);
	} else {
	    teacherAdviseService.updatePercentage(percentage, roleType);
	}
    }
}