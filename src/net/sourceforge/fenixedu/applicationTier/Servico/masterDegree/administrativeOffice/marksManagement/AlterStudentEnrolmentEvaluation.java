package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Angela 04/07/2003
 * 
 */
public class AlterStudentEnrolmentEvaluation extends Service {

    public void run(Integer curricularCourseCode, Integer enrolmentEvaluationCode,
	    InfoEnrolmentEvaluation infoEnrolmentEvaluation, Integer teacherNumber, IUserView userView)
	    throws FenixServiceException, ExcepcaoPersistencia {

	Person person = userView.getPerson();
	if (person == null)
	    throw new NonExistingServiceException();

	Employee employee = person.getEmployee();

	Teacher teacher = Teacher.readByNumber(teacherNumber);
	if (teacher == null)
	    throw new NonExistingServiceException();

	EnrolmentEvaluation enrolmentEvaluationCopy = rootDomainObject.readEnrolmentEvaluationByOID(enrolmentEvaluationCode);
	if (enrolmentEvaluationCopy == null)
	    throw new NonExistingServiceException();

	enrolmentEvaluationCopy
		.alterStudentEnrolmentEvaluationForMasterDegree(infoEnrolmentEvaluation.getGradeValue(), employee, teacher
			.getPerson(), infoEnrolmentEvaluation.getEnrolmentEvaluationType(), infoEnrolmentEvaluation
			.getGradeAvailableDate(), infoEnrolmentEvaluation.getExamDate(), infoEnrolmentEvaluation.getObservation());

    }

}