
/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota (jccm@rnl.ist.utl.pt)
 *  
 */

package ServidorAplicacao.Servico.commons.student;

import DataBeans.InfoEnrolmentEvaluation;
import Dominio.Enrolment;
import Dominio.IEnrolment;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadInfoEnrolmentEvaluationByEvaluationOID implements IServico
{

    private static ReadInfoEnrolmentEvaluationByEvaluationOID servico =
        new ReadInfoEnrolmentEvaluationByEvaluationOID();

    /**
	 * The singleton access method of this class.
	 */
    public static ReadInfoEnrolmentEvaluationByEvaluationOID getService()
    {
        return servico;
    }

    /**
	 * The actor of this class.
	 */
    private ReadInfoEnrolmentEvaluationByEvaluationOID()
    {
    }

    /**
	 * Returns The Service Name
	 */

    public final String getNome()
    {
        return "ReadInfoEnrolmentEvaluationByEvaluationOID";
    }

    public InfoEnrolmentEvaluation run(IUserView userView, Integer enrolmentOID)
        throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia
    {
        ISuportePersistente sp = null;

        GestorServicos serviceManager = GestorServicos.manager();

        IEnrolment enrolment = null;
        try
        {
            sp = SuportePersistenteOJB.getInstance();

            IEnrolment enrolmentTemp = new Enrolment();
            enrolmentTemp.setIdInternal(enrolmentOID);

            enrolment = (IEnrolment) sp.getIPersistentEnrolment().readByOId(enrolmentTemp, false);

        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }

        InfoEnrolmentEvaluation enrolmentEvaluation = null;

        try
        {
            Object args[] = { enrolment };
            enrolmentEvaluation =
                (InfoEnrolmentEvaluation) serviceManager.executar(userView, "GetEnrolmentGrade", args);
        }
        catch (FenixServiceException e)
        {
            throw new FenixServiceException(e);
        }

        return enrolmentEvaluation;
    }

}