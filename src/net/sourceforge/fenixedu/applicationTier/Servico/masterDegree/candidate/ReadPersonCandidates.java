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
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadPersonCandidates implements IService {

    public Object run(IUserView userView) throws ExcepcaoInexistente, FenixServiceException,
            ExcepcaoPersistencia {

        String username = new String(userView.getUtilizador());

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List candidates = sp.getIPersistentMasterDegreeCandidate().readMasterDegreeCandidatesByUsername(
                username);

        if (candidates == null) {
            throw new ExcepcaoInexistente("No Candidates Found !!");
        }

        return CollectionUtils.collect(candidates, new Transformer() {

            public Object transform(Object arg0) {
                IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) arg0;
                return InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidate);
            }

        });

    }
}