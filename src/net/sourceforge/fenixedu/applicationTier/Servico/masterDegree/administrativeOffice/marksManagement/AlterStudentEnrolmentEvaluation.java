package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Angela 04/07/2003
 * 
 */
public class AlterStudentEnrolmentEvaluation extends FenixService {

    @Service
    public static void run(Integer curricularCourseCode, Integer enrolmentEvaluationCode,
	    InfoEnrolmentEvaluation infoEnrolmentEvaluation, String teacherId, IUserView userView) throws FenixServiceException {

	Person person = userView.getPerson();
	if (person == null)
	    throw new NonExistingServiceException();

	Teacher teacher = Teacher.readByIstId(teacherId);
	if (teacher == null)
	    throw new NonExistingServiceException();

	EnrolmentEvaluation enrolmentEvaluationCopy = rootDomainObject.readEnrolmentEvaluationByOID(enrolmentEvaluationCode);
	if (enrolmentEvaluationCopy == null)
	    throw new NonExistingServiceException();

	enrolmentEvaluationCopy.alterStudentEnrolmentEvaluationForMasterDegree(infoEnrolmentEvaluation.getGradeValue(), person,
		teacher.getPerson(), infoEnrolmentEvaluation.getEnrolmentEvaluationType(),
		infoEnrolmentEvaluation.getGradeAvailableDate(), infoEnrolmentEvaluation.getExamDate(),
		infoEnrolmentEvaluation.getObservation());

    }

}