/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package ServidorAplicacao.Servico.commons.student;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoEnrolmentEvaluation;
import Dominio.Enrolment;
import Dominio.IEnrollment;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            sp = SuportePersistenteOJB.getInstance();

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