/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadInfoEnrolmentEvaluationByEvaluationOID implements IService {

    /**
     * The actor of this class.
     */
    public ReadInfoEnrolmentEvaluationByEvaluationOID() {
    }

    public InfoEnrolmentEvaluation run(IUserView userView, Integer enrolmentOID)
            throws ExcepcaoInexistente, FenixServiceException {
        ISuportePersistente sp = null;

        IEnrollment enrolment = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            enrolment = (IEnrollment) sp.getIPersistentEnrolment().readByOID(Enrolment.class,
                    enrolmentOID);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        InfoEnrolmentEvaluation enrolmentEvaluation = null;

        try {
            
            enrolmentEvaluation = (new GetEnrolmentGrade()).run(enrolment);
        } catch (FenixServiceException e) {
            throw new FenixServiceException(e);
        }

        return enrolmentEvaluation;
    }

}