package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidates {

    @Service
    public static List run(String[] candidateList) throws FenixServiceException {

        List result = new ArrayList();

        // Read the admited candidates
        int size = candidateList.length;
        int i = 0;
        for (i = 0; i < size; i++) {

            result.add(InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(RootDomainObject.getInstance()
                    .readMasterDegreeCandidateByOID(new Integer(candidateList[i]))));
        }

        return result;
    }
}