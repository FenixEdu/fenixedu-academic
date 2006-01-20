/*
 * Created on Nov 22, 2004
 */
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
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;

/**
 * @author nmgo
 */
public class ImprovmentEnrollService extends Service {

    public Object run(Integer studentNumber, String employeeUserName, List enrolmentsIds)
            throws FenixServiceException, ExcepcaoPersistencia {
        IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();

        Student student = persistentStudent.readStudentByNumberAndDegreeType(studentNumber,
                DegreeType.DEGREE);
        if (student == null) {
            throw new InvalidArgumentsServiceException();
        }

        IPessoaPersistente pessoaPersistente = persistentSupport.getIPessoaPersistente();
        Person pessoa = pessoaPersistente.lerPessoaPorUsername(employeeUserName);

        IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
        final ExecutionPeriod currentExecutionPeriod = persistentExecutionPeriod
                .readActualExecutionPeriod();

        if (pessoa == null) {
            throw new InvalidArgumentsServiceException();
        }

        IPersistentEmployee persistentEmployee = persistentSupport.getIPersistentEmployee();
        Employee employee = persistentEmployee.readByPerson(pessoa);
        ;

        if (employee == null) {
            throw new InvalidArgumentsServiceException();
        }

        IPersistentEnrollment persistentEnrollment = persistentSupport.getIPersistentEnrolment();

        Iterator iterator = enrolmentsIds.iterator();
        while (iterator.hasNext()) {
            Integer enrolmentId = (Integer) iterator.next();
            Enrolment enrollment = (Enrolment) persistentEnrollment.readByOID(Enrolment.class,
                    enrolmentId);
            if (enrollment == null) {
                throw new InvalidArgumentsServiceException();
            }

            enrollment
                    .createEnrolmentEvaluationForImprovement(employee, currentExecutionPeriod, student);
        }

        return new Boolean(true);
    }
}
