package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.improvment;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author nmgo
 */
public class ImprovmentEnrollService extends Service {

    public Object run(Integer studentNumber, String employeeUserName, List enrolmentsIds)
            throws FenixServiceException, ExcepcaoPersistencia {

        Student student = Student.readStudentByNumberAndDegreeType(studentNumber, DegreeType.DEGREE);
        if (student == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Person person = Person.readPersonByUsername(employeeUserName);
        final ExecutionPeriod currentExecutionPeriod = ExecutionPeriod.readActualExecutionPeriod();

        if (person == null) {
            throw new InvalidArgumentsServiceException();
        }

        Employee employee = person.getEmployee();
        if (employee == null) {
            throw new InvalidArgumentsServiceException();
        }

        Iterator iterator = enrolmentsIds.iterator();
        while (iterator.hasNext()) {
            Integer enrolmentId = (Integer) iterator.next();
            Enrolment enrollment = (Enrolment) rootDomainObject.readCurriculumModuleByOID(enrolmentId);
            if (enrollment == null) {
                throw new InvalidArgumentsServiceException();
            }

            enrollment
                    .createEnrolmentEvaluationForImprovement(employee, currentExecutionPeriod, student);
        }
        return Boolean.TRUE;
    }
}
