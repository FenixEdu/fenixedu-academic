package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.SituationName;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadAdmitedCandidates implements IService {

    /**
     * 
     * @param candidateList
     * @return The candidates Admited for Master Degree (Specialization not
     *         Included) This is only for Numerus Clauses count
     * @throws FenixServiceException
     */
    public List run(String[] candidateList, String[] ids) throws FenixServiceException {

        ISuportePersistente sp = null;
        List result = new ArrayList();

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            // Read the admited candidates
            int size = candidateList.length;
            int i = 0;
            for (i = 0; i < size; i++) {
                if (candidateList[i].equals(SituationName.ADMITIDO_STRING)
                        || candidateList[i].equals(SituationName.ADMITED_SPECIALIZATION_STRING)
                        || candidateList[i].equals(SituationName.ADMITED_CONDICIONAL_CURRICULAR_STRING)
                        || candidateList[i].equals(SituationName.ADMITED_CONDICIONAL_FINALIST_STRING)
                        || candidateList[i].equals(SituationName.ADMITED_CONDICIONAL_OTHER_STRING)) {

                    IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) sp
                            .getIPersistentMasterDegreeCandidate().readByOID(
                                    MasterDegreeCandidate.class, new Integer(ids[i]));
                    if (!masterDegreeCandidate.getSpecialization().equals(
                            Specialization.SPECIALIZATION)) {
                        result.add(candidateList[i]);
                    }
                }
            }
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return result;

    }

}