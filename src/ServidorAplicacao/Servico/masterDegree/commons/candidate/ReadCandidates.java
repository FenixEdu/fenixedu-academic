package ServidorAplicacao.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.IMasterDegreeCandidate;
import Dominio.MasterDegreeCandidate;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidates implements IServico {

    private static ReadCandidates servico = new ReadCandidates();

    /**
     * The singleton access method of this class.
     */
    public static ReadCandidates getService() {
        return servico;
    }

    /**
     * The actor of this class.
     */
    private ReadCandidates() {
    }

    /**
     * Returns The Service Name
     */

    public final String getNome() {
        return "ReadCandidates";
    }

    public List run(String[] candidateList) throws FenixServiceException {

        ISuportePersistente sp = null;
        List result = new ArrayList();

        try {
            sp = SuportePersistenteOJB.getInstance();

            // Read the admited candidates
            int size = candidateList.length;
            int i = 0;
            for (i = 0; i < size; i++) {
               
                result
                        .add(Cloner
                                .copyIMasterDegreeCandidate2InfoMasterDegreCandidate((IMasterDegreeCandidate) sp
                                        .getIPersistentMasterDegreeCandidate()
                                        .readByOID(MasterDegreeCandidate.class, new Integer(candidateList[i]))));
            }
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException(
                    "Persistence layer error",ex);
           
            throw newEx;
        }
        return result;
    }
}