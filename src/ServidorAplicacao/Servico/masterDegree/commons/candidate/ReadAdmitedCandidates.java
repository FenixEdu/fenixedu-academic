package ServidorAplicacao.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.List;

import Dominio.IMasterDegreeCandidate;
import Dominio.MasterDegreeCandidate;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.SituationName;
import Util.Specialization;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadAdmitedCandidates implements IServico {

    private static ReadAdmitedCandidates servico = new ReadAdmitedCandidates();

    /**
     * The singleton access method of this class.
     */
    public static ReadAdmitedCandidates getService() {
        return servico;
    }

    /**
     * The actor of this class.
     */
    private ReadAdmitedCandidates() {
    }

    /**
     * Returns The Service Name
     */

    public final String getNome() {
        return "ReadAdmitedCandidates";
    }

    /**
     * 
     * @param candidateList
     * @return The candidates Admited for Master Degree (Specialization not
     *         Included) This is only for Numerus Clauses count
     * @throws FenixServiceException
     */
    public List run(String[] candidateList, String[] ids)
            throws FenixServiceException {

        ISuportePersistente sp = null;
        List result = new ArrayList();

        try {
            sp = SuportePersistenteOJB.getInstance();

            // Read the admited candidates
            int size = candidateList.length;
            int i = 0;
            for (i = 0; i < size; i++) {
                if (candidateList[i].equals(SituationName.ADMITIDO_STRING)
                        || candidateList[i]
                                .equals(SituationName.ADMITED_SPECIALIZATION_STRING)
                        || candidateList[i]
                                .equals(SituationName.ADMITED_CONDICIONAL_CURRICULAR_STRING)
                        || candidateList[i]
                                .equals(SituationName.ADMITED_CONDICIONAL_FINALIST_STRING)
                        || candidateList[i]
                                .equals(SituationName.ADMITED_CONDICIONAL_OTHER_STRING)) {

                  

                    IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) sp
                            .getIPersistentMasterDegreeCandidate().readByOID(
                                    MasterDegreeCandidate.class, new Integer(ids[i]));
                    if (!masterDegreeCandidate.getSpecialization().equals(
                            new Specialization(
                                    Specialization.ESPECIALIZACAO_STRING))) {
                        result.add(candidateList[i]);
                    }
                }
            }
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException(
                    "Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return result;

    }

}