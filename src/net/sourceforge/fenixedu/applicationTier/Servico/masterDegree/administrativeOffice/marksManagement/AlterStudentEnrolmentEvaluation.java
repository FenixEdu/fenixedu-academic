package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
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
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Angela 04/07/2003
 * 
 */
public class AlterStudentEnrolmentEvaluation implements IService {

    public List run(Integer curricularCourseCode, Integer enrolmentEvaluationCode,
            InfoEnrolmentEvaluation infoEnrolmentEvaluation, Integer teacherNumber, IUserView userView)
            throws FenixServiceException, ExcepcaoPersistencia {

        List<InfoEnrolmentEvaluation> infoEvaluationsWithError = new ArrayList<InfoEnrolmentEvaluation>();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp
                .getIPersistentEnrolmentEvaluation();
        IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        IPersistentEmployee persistentEmployee = sp.getIPersistentEmployee();

        Person person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
        if (person == null)
            throw new NonExistingServiceException();

        Employee employee = persistentEmployee.readByPerson(person.getIdInternal().intValue());

        Teacher teacher = persistentTeacher.readByNumber(teacherNumber);
        if (teacher == null)
            throw new NonExistingServiceException();

        EnrolmentEvaluation enrolmentEvaluationCopy = (EnrolmentEvaluation) persistentEnrolmentEvaluation
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