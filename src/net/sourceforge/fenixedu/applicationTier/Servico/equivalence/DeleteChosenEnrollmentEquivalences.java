package net.sourceforge.fenixedu.applicationTier.Servico.equivalence;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.equivalence.InfoEquivalenceContext;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEquivalence;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.EquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.IEnrolmentEquivalence;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IEquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author David Santos in May 12, 2004
 */

public class DeleteChosenEnrollmentEquivalences extends EnrollmentEquivalenceServiceUtils implements
        IService {
    public DeleteChosenEnrollmentEquivalences() {
    }

    public InfoEquivalenceContext run(Integer studentNumber, TipoCurso degreeType,
            List idsOfChosenEnrollments) throws FenixServiceException {
        return (InfoEquivalenceContext) convertDataOutput(execute(convertDataInput(idsOfChosenEnrollments)));
    }

    /**
     * @see ServidorAplicacao.Servico.Service#convertDataInput(java.lang.Object)
     *      This method converts this service DataBeans input objects to their
     *      respective Domain objects. These Domain objects are to be used by
     *      the service's logic.
     */
    protected Object convertDataInput(Object object) {
        return object;
    }

    /**
     * @see ServidorAplicacao.Servico.Service#convertDataInput(java.lang.Object)
     *      This method converts this service output Domain objects to their
     *      respective DataBeans. These DataBeans are the result of executing
     *      this service logic and are to be passed on to the uper layer of the
     *      architecture.
     */
    protected Object convertDataOutput(Object object) {
        return object;
    }

    /**
     * @param List
     * @return List
     * @throws FenixServiceException
     * @see ServidorAplicacao.Servico.Service#convertDataInput(java.lang.Object)
     *      This method implements the buisiness logic of this service.
     */
    protected Object execute(Object object) throws FenixServiceException {
        List idsOfChosenEnrollments = (List) object;

        try {
            ISuportePersistente persistenceDAO = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentEnrollment enrollmentDAO = persistenceDAO.getIPersistentEnrolment();
            IPersistentEnrolmentEvaluation enrolmentEvaluationDAO = persistenceDAO
                    .getIPersistentEnrolmentEvaluation();
            IPersistentEnrolmentEquivalence enrollmentEquivalenceDAO = persistenceDAO
                    .getIPersistentEnrolmentEquivalence();
            IPersistentEquivalentEnrolmentForEnrolmentEquivalence equivalentEnrollmentForEnrollmentEquivalenceDAO = persistenceDAO
                    .getIPersistentEquivalentEnrolmentForEnrolmentEquivalence();

            for (int i = 0; i < idsOfChosenEnrollments.size(); i++) {
                Integer enrollmentID = (Integer) idsOfChosenEnrollments.get(i);

                IEnrollment enrollment = (IEnrollment) enrollmentDAO.readByOID(Enrolment.class,
                        enrollmentID);

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

                                equivalentEnrollmentForEnrollmentEquivalenceDAO.deleteByOID(
                                        EquivalentEnrolmentForEnrolmentEquivalence.class,
                                        equivalentEnrolmentForEnrolmentEquivalence.getIdInternal());
                            }
                        }

                        enrollmentEquivalenceDAO.deleteByOID(EnrolmentEquivalence.class,
                                enrollmentEquivalence.getIdInternal());
                    }

                    for (int k = 0; k < enrollment.getEvaluations().size(); k++) {
                        IEnrolmentEvaluation enrollmentEvaluation = (IEnrolmentEvaluation) enrollment
                                .getEvaluations().get(k);
                        enrolmentEvaluationDAO.deleteByOID(EnrolmentEvaluation.class,
                                enrollmentEvaluation.getIdInternal());
                    }

                    enrollmentDAO.deleteByOID(Enrolment.class, enrollment.getIdInternal());
                }
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return null;
    }

}