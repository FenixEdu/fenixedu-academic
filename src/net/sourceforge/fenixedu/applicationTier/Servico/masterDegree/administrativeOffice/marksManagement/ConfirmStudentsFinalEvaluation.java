package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.MarkType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério 10/07/2003
 *  
 */
public class ConfirmStudentsFinalEvaluation implements IService {

    public Boolean run(Integer curricularCourseCode, String yearString, IUserView userView)
            throws FenixServiceException {

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            IPersistentEnrollment persistentEnrolment = sp.getIPersistentEnrolment();
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
			IPersistentEmployee persistentEmployee = sp.getIPersistentEmployee();

            IPerson person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
			IEmployee employee = persistentEmployee.readByPerson(person.getIdInternal().intValue());

            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, curricularCourseCode, false);

            List enrolments = null;
            if (yearString != null) {
                enrolments = persistentEnrolment.readByCurricularCourseAndYear(curricularCourseCode,
                        yearString);
            } else {
                enrolments = curricularCourse.getEnrolments();
            }
			
			
            List enrolmentEvaluations = new ArrayList();
            Iterator iterEnrolment = enrolments.listIterator();
            while (iterEnrolment.hasNext()) {
                IEnrolment enrolment = (IEnrolment) iterEnrolment.next();
             
				List allEnrolmentEvaluations = enrolment.getEvaluations();
                IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) allEnrolmentEvaluations
                        .get(allEnrolmentEvaluations.size() - 1);
                enrolmentEvaluations.add(enrolmentEvaluation);
            }

			
            if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
				
                ListIterator iterEnrolmentEvaluations = enrolmentEvaluations.listIterator();
                while (iterEnrolmentEvaluations.hasNext()) {
					
                    IEnrolmentEvaluation enrolmentEvaluationElem = (IEnrolmentEvaluation) iterEnrolmentEvaluations.next();
					
                    if (enrolmentEvaluationElem.getGrade() != null && 
						enrolmentEvaluationElem.getGrade().length() > 0 && 
						enrolmentEvaluationElem.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.TEMPORARY_OBJ)) {

						enrolmentEvaluationElem.confirmSubmission(employee, "Lançamento de Notas na Secretaria");
                    }
                }
            }
        } 
		
		catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            FenixServiceException newEx = new FenixServiceException("");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return Boolean.TRUE;
    }

}