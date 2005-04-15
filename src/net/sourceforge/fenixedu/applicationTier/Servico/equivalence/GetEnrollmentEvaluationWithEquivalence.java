package net.sourceforge.fenixedu.applicationTier.Servico.equivalence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluationWithResponsibleForGrade;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithInfoStudent;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEquivalence;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IEquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author David Santos in May 17, 2004
 */

public class GetEnrollmentEvaluationWithEquivalence extends EnrollmentEquivalenceServiceUtils implements
        IService {
    public GetEnrollmentEvaluationWithEquivalence() {
    }

    public List run(Integer studentNumber, TipoCurso degreeType, Integer enrollmentID)
            throws FenixServiceException {
        return (List) convertDataOutput(execute(convertDataInput(enrollmentID)));
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.Service#convertDataInput(java.lang.Object)
     */
    protected Object convertDataInput(Object object) {
        return object;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.Service#convertDataOutput(java.lang.Object)
     */
    protected Object convertDataOutput(Object object) {
        List input = (List) object;
        List output = new ArrayList();

        for (int i = 0; i < input.size(); i++) {
            IEnrolmentEvaluation enrollmentEvaluation = (IEnrolmentEvaluation) input.get(i);

            InfoEnrolmentEvaluation infoEnrollmentEvaluation = InfoEnrolmentEvaluationWithResponsibleForGrade
                    .newInfoFromDomain(enrollmentEvaluation);
            infoEnrollmentEvaluation
                    .setInfoEnrolment(InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear
                            .newInfoFromDomain(enrollmentEvaluation.getEnrolment()));
            infoEnrollmentEvaluation.getInfoEnrolment().setInfoStudentCurricularPlan(
                    InfoStudentCurricularPlanWithInfoStudent.newInfoFromDomain(enrollmentEvaluation
                            .getEnrolment().getStudentCurricularPlan()));

            output.add(i, infoEnrollmentEvaluation);
        }

        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.Service#execute(java.lang.Object)
     */
    protected Object execute(Object object) throws FenixServiceException {
        Integer enrollmentID = (Integer) object;

        List enrollmentEvaluations = new ArrayList();

        try {
            ISuportePersistente persistenceDAO = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentEnrollment enrollmentDAO = persistenceDAO.getIPersistentEnrolment();
            IPersistentEnrolmentEquivalence enrollmentEquivalenceDAO = persistenceDAO
                    .getIPersistentEnrolmentEquivalence();
            IPersistentEquivalentEnrolmentForEnrolmentEquivalence equivalentEnrollmentForEnrollmentEquivalenceDAO = persistenceDAO
                    .getIPersistentEquivalentEnrolmentForEnrolmentEquivalence();

            IEnrolment enrollment = (IEnrolment) enrollmentDAO
                    .readByOID(Enrolment.class, enrollmentID);

            enrollmentEvaluations.add(0, getEnrollmentEvaluation(enrollment));

            if (enrollment != null) {
                IEnrolmentEquivalence enrollmentEquivalence = enrollmentEquivalenceDAO
                        .readByEnrolment(enrollment);
                if (enrollmentEquivalence != null) {
                    List equivalentEnrollmentsForEnrollmentEquivalence = equivalentEnrollmentForEnrollmentEquivalenceDAO
                            .readByEnrolmentEquivalence(enrollmentEquivalence);

                    if ((equivalentEnrollmentsForEnrollmentEquivalence != null)
                            && (!equivalentEnrollmentsForEnrollmentEquivalence.isEmpty())) {
                        for (int j = 0; j < equivalentEnrollmentsForEnrollmentEquivalence.size(); j++) {
                            IEquivalentEnrolmentForEnrolmentEquivalence equivalentEnrolmentForEnrolmentEquivalence = (IEquivalentEnrolmentForEnrolmentEquivalence) equivalentEnrollmentsForEnrollmentEquivalence
                                    .get(j);

                            enrollmentEvaluations.add((j + 1),
                                    getEnrollmentEvaluation(equivalentEnrolmentForEnrolmentEquivalence
                                            .getEquivalentEnrolment()));
                        }
                    }
                }
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return enrollmentEvaluations;
    }

    private IEnrolmentEvaluation getEnrollmentEvaluation(IEnrolment enrollment) {
        List enrolmentEvaluations = enrollment.getEvaluations();

        // This sorts the list ascendingly so we need to reverse it to get the
        // first object.
        Collections.sort(enrolmentEvaluations);
        Collections.reverse(enrolmentEvaluations);

        return (IEnrolmentEvaluation) enrolmentEvaluations.get(0);
    }
}