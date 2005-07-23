package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
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
            throws FenixServiceException {

        List<InfoEnrolmentEvaluation> infoEvaluationsWithError = new ArrayList<InfoEnrolmentEvaluation>();
		
        try {
            Calendar calendario = Calendar.getInstance();
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp.getIPersistentEnrolmentEvaluation();
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			IPersistentEmployee persistentEmployee = sp.getIPersistentEmployee();
			
            IPerson person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
            if (person == null)
                throw new NonExistingServiceException();
			
            IEmployee employee = persistentEmployee.readByPerson(person.getIdInternal().intValue());
			
            ITeacher teacher = persistentTeacher.readByNumber(teacherNumber);
            if (teacher == null)
                throw new NonExistingServiceException();

            IEnrolmentEvaluation enrolmentEvaluationCopy = (IEnrolmentEvaluation) persistentEnrolmentEvaluation
            	.readByOID(EnrolmentEvaluation.class, enrolmentEvaluationCode);
            if (enrolmentEvaluationCopy == null)
                throw new NonExistingServiceException();
			
			try {
				enrolmentEvaluationCopy.alterStudentEnrolmentEvaluationForMasterDegree(infoEnrolmentEvaluation.getGrade(),
						employee, teacher.getPerson(), infoEnrolmentEvaluation.getEnrolmentEvaluationType(), 
						infoEnrolmentEvaluation.getGradeAvailableDate(), infoEnrolmentEvaluation.getExamDate(),
						infoEnrolmentEvaluation.getObservation());
			}
			
			catch (DomainException e) {
				infoEvaluationsWithError.add(infoEnrolmentEvaluation);
			}

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            FenixServiceException newEx = new FenixServiceException("");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoEvaluationsWithError;
    }

}