/*
 * Created on 14/Mar/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.SituationName;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidateForRegistration extends Service {

    public List run(Integer executionDegreeCode) throws FenixServiceException, ExcepcaoPersistencia {

        List<SituationName> situationNames = Arrays.asList(
                new SituationName[] {
                        SituationName.ADMITED_CONDICIONAL_CURRICULAR_OBJ,
                        SituationName.ADMITED_CONDICIONAL_FINALIST_OBJ,
                        SituationName.ADMITED_CONDICIONAL_FINALIST_OBJ,
                        SituationName.ADMITED_CONDICIONAL_OTHER_OBJ, SituationName.ADMITIDO_OBJ,
                        SituationName.ADMITED_SPECIALIZATION_OBJ 
                });

        ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeCode);
        List<CandidateSituation> result = executionDegree.getCandidateSituationsInSituation(situationNames);
        
        if (result.isEmpty()) {
            throw new NonExistingServiceException();
        }

        List candidateList = new ArrayList();
        Iterator resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            CandidateSituation candidateSituation = (CandidateSituation) resultIterator.next();
            InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                    .newInfoFromDomain(candidateSituation.getMasterDegreeCandidate());
            infoMasterDegreeCandidate.setInfoCandidateSituation(InfoCandidateSituation
                    .newInfoFromDomain(candidateSituation));
            candidateList.add(infoMasterDegreeCandidate);
        }

        return candidateList;

    }
}