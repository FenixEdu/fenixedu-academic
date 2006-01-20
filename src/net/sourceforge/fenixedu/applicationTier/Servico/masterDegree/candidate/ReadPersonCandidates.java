/*
 * ReadMasterDegreeCandidateByUsername.java
 *
 * The Service ReadMasterDegreeCandidateByUsername reads the information of a
 * Candidate and returns it
 * 
 * Created on 02 de Dezembro de 2002, 16:25
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.candidate;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadPersonCandidates extends Service {

    public Object run(IUserView userView) throws ExcepcaoInexistente, FenixServiceException,
            ExcepcaoPersistencia {

        String username = new String(userView.getUtilizador());

        List candidates = persistentSupport.getIPersistentMasterDegreeCandidate().readMasterDegreeCandidatesByUsername(
                username);

        if (candidates == null) {
            throw new ExcepcaoInexistente("No Candidates Found !!");
        }

        return CollectionUtils.collect(candidates, new Transformer() {

            public Object transform(Object arg0) {
                MasterDegreeCandidate masterDegreeCandidate = (MasterDegreeCandidate) arg0;
                return InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidate);
            }

        });

    }
}