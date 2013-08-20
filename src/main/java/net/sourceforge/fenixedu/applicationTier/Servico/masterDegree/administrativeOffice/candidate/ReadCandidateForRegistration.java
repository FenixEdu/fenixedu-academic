/*
 * Created on 14/Mar/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.util.SituationName;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidateForRegistration {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static List run(String executionDegreeCode) throws FenixServiceException {

        List<SituationName> situationNames =
                Arrays.asList(new SituationName[] { SituationName.ADMITED_CONDICIONAL_CURRICULAR_OBJ,
                        SituationName.ADMITED_CONDICIONAL_FINALIST_OBJ, SituationName.ADMITED_CONDICIONAL_FINALIST_OBJ,
                        SituationName.ADMITED_CONDICIONAL_OTHER_OBJ, SituationName.ADMITIDO_OBJ,
                        SituationName.ADMITED_SPECIALIZATION_OBJ });

        ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(executionDegreeCode);
        List<CandidateSituation> result = executionDegree.getCandidateSituationsInSituation(situationNames);

        if (result.isEmpty()) {
            throw new NonExistingServiceException();
        }

        List candidateList = new ArrayList();
        Iterator resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            CandidateSituation candidateSituation = (CandidateSituation) resultIterator.next();
            InfoMasterDegreeCandidate infoMasterDegreeCandidate =
                    InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(candidateSituation.getMasterDegreeCandidate());
            infoMasterDegreeCandidate.setInfoCandidateSituation(InfoCandidateSituation.newInfoFromDomain(candidateSituation));
            candidateList.add(infoMasterDegreeCandidate);
        }

        return candidateList;

    }
}