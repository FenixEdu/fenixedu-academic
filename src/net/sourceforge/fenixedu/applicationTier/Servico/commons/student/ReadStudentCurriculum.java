/**
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadStudentCurriculum implements IService {

    /**
     * The actor of this class.
     */
    public ReadStudentCurriculum() {
    }

    public List run(Integer executionDegreeCode, Integer studentCurricularPlanID)
            throws ExcepcaoInexistente, FenixServiceException {
        ISuportePersistente sp = null;

        IStudentCurricularPlan studentCurricularPlan = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            studentCurricularPlan = (IStudentCurricularPlan) sp.getIStudentCurricularPlanPersistente()
                    .readByOID(StudentCurricularPlan.class, studentCurricularPlanID);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);
            throw newEx;
        }

        if (studentCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        Iterator iterator = studentCurricularPlan.getEnrolments().iterator();
        List result = new ArrayList();

        GetEnrolmentGrade getEnrollmentGrade = new GetEnrolmentGrade();
        while (iterator.hasNext()) {
            IEnrollment enrolmentTemp = (IEnrollment) iterator.next();

            InfoEnrolmentEvaluation infoEnrolmentEvaluation = getEnrollmentGrade.run(enrolmentTemp);

            InfoEnrolment infoEnrolment = InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear
                    .newInfoFromDomain(enrolmentTemp);

            infoEnrolment.setInfoEnrolmentEvaluation(infoEnrolmentEvaluation);

            result.add(infoEnrolment);
        }

        return result;
    }    
}