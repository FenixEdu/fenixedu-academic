package ServidorAplicacao.Servico.equivalence;

import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.equivalence.InfoEquivalenceContext;
import Dominio.Enrolment;
import Dominio.EnrolmentEquivalence;
import Dominio.EnrolmentEvaluation;
import Dominio.EquivalentEnrolmentForEnrolmentEquivalence;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEquivalence;
import Dominio.IEnrolmentEvaluation;
import Dominio.IEquivalentEnrolmentForEnrolmentEquivalence;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IPersistentEnrolmentEquivalence;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentEquivalentEnrolmentForEnrolmentEquivalence;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

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
            ISuportePersistente persistenceDAO = SuportePersistenteOJB.getInstance();
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