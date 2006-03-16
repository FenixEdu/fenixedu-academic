package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;

/**
 * @author Angela 04/07/2003
 * 
 */
public class AlterStudentEnrolmentEvaluation extends Service {

    public List run(Integer curricularCourseCode, Integer enrolmentEvaluationCode,
            InfoEnrolmentEvaluation infoEnrolmentEvaluation, Integer teacherNumber, IUserView userView)
            throws FenixServiceException, ExcepcaoPersistencia {

        List<InfoEnrolmentEvaluation> infoEvaluationsWithError = new ArrayList<InfoEnrolmentEvaluation>();
        IPersistentEmployee persistentEmployee = persistentSupport.getIPersistentEmployee();

        Person person = Person.readPersonByUsername(userView.getUtilizador());
        if (person == null)
            throw new NonExistingServiceException();

        Employee employee = persistentEmployee.readByPerson(person.getIdInternal().intValue());

        Teacher teacher = Teacher.readByNumber(teacherNumber);
        if (teacher == null)
            throw new NonExistingServiceException();

        EnrolmentEvaluation enrolmentEvaluationCopy = (EnrolmentEvaluation) persistentObject
                .readByOID(EnrolmentEvaluation.class, enrolmentEvaluationCode);
        if (enrolmentEvaluationCopy == null)
            throw new NonExistingServiceException();

        try {
            enrolmentEvaluationCopy.alterStudentEnrolmentEvaluationForMasterDegree(
                    infoEnrolmentEvaluation.getGrade(), employee, teacher.getPerson(),
                    infoEnrolmentEvaluation.getEnrolmentEvaluationType(), infoEnrolmentEvaluation
                            .getGradeAvailableDate(), infoEnrolmentEvaluation.getExamDate(),
                    infoEnrolmentEvaluation.getObservation());
        }

        catch (DomainException e) {
            infoEvaluationsWithError.add(infoEnrolmentEvaluation);
        }

        return infoEvaluationsWithError;
    }

}