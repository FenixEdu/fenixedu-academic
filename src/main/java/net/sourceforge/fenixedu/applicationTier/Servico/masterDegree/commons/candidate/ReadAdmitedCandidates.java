package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.SituationName;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadAdmitedCandidates {

    /**
     * 
     * @param candidateList
     * @return The candidates Admited for Master Degree (Specialization not
     *         Included) This is only for Numerus Clauses count
     * @throws FenixServiceException
     * @throws ExcepcaoPersistencia
     */
    @Atomic
    public static List run(String[] candidateList, String[] ids) throws FenixServiceException {
        List<String> result = new ArrayList<String>();

        // Read the admited candidates
        int size = candidateList.length;
        int i = 0;
        for (i = 0; i < size; i++) {
            if (candidateList[i].equals(SituationName.ADMITIDO_STRING)
                    || candidateList[i].equals(SituationName.ADMITED_SPECIALIZATION_STRING)
                    || candidateList[i].equals(SituationName.ADMITED_CONDICIONAL_CURRICULAR_STRING)
                    || candidateList[i].equals(SituationName.ADMITED_CONDICIONAL_FINALIST_STRING)
                    || candidateList[i].equals(SituationName.ADMITED_CONDICIONAL_OTHER_STRING)) {

                MasterDegreeCandidate masterDegreeCandidate = FenixFramework.getDomainObject(ids[i]);
                if (!masterDegreeCandidate.getSpecialization().equals(Specialization.STUDENT_CURRICULAR_PLAN_SPECIALIZATION)) {
                    result.add(candidateList[i]);
                }
            }
        }

        return result;
    }

}