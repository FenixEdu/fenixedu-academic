/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package ServidorAplicacao.Servico.commons.student;

import DataBeans.InfoEnrolmentEvaluation;
import Dominio.Enrolment;
import Dominio.IEnrollment;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import framework.factory.ServiceManagerServiceFactory;

public class ReadInfoEnrolmentEvaluationByEvaluationOID implements IServico {

    private static ReadInfoEnrolmentEvaluationByEvaluationOID servico = new ReadInfoEnrolmentEvaluationByEvaluationOID();

    /**
     * The singleton access method of this class.
     */
    public static ReadInfoEnrolmentEvaluationByEvaluationOID getService() {
        return servico;
    }

    /**
     * The actor of this class.
     */
    private ReadInfoEnrolmentEvaluationByEvaluationOID() {
    }

    /**
     * Returns The Service Name
     */

    public final String getNome() {
        return "ReadInfoEnrolmentEvaluationByEvaluationOID";
    }

    public InfoEnrolmentEvaluation run(IUserView userView, Integer enrolmentOID)
            throws ExcepcaoInexistente, FenixServiceException {
        ISuportePersistente sp = null;

        IEnrollment enrolment = null;
        try {
            sp = SuportePersistenteOJB.getInstance();

            enrolment = (IEnrollment) sp.getIPersistentEnrolment().readByOID(
                    Enrolment.class, enrolmentOID);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        InfoEnrolmentEvaluation enrolmentEvaluation = null;

        try {
            Object args[] = { enrolment };
            enrolmentEvaluation = (InfoEnrolmentEvaluation) ServiceManagerServiceFactory
                    .executeService(userView, "GetEnrolmentGrade", args);
        } catch (FenixServiceException e) {
            throw new FenixServiceException(e);
        }

        return enrolmentEvaluation;
    }

}