/*
 * Created on 29/Nov/2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoCandidateSituation;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.InfoMasterDegreeCandidateWithInfoPerson;
import Dominio.ICandidateSituation;
import Dominio.IMasterDegreeCandidate;
import Dominio.MasterDegreeCandidate;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMasterDegreeCandidate;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author  <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author  <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 * 
 */
public class ReadMasterDegreeCandidateByID implements IService {

    public InfoMasterDegreeCandidate run(Integer candidateID) throws ExcepcaoPersistencia {

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        IPersistentMasterDegreeCandidate masterDegreeCandidateDAO = sp
                .getIPersistentMasterDegreeCandidate();

        IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) masterDegreeCandidateDAO
                .readByOID(MasterDegreeCandidate.class, candidateID);
        
        InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidate);
        
        //      Copy all Situations
        List situations = new ArrayList();
        Iterator situationIter = masterDegreeCandidate.getSituations().iterator();
        while (situationIter.hasNext()) {

            InfoCandidateSituation infoCandidateSituation = InfoCandidateSituation
                    .newInfoFromDomain((ICandidateSituation) situationIter.next());
            situations.add(infoCandidateSituation);

            // Check if this is the Active Situation
            if (infoCandidateSituation.getValidation().equals(new State(State.ACTIVE)))
                infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
        }
        infoMasterDegreeCandidate.setSituationList(situations);
                
        return infoMasterDegreeCandidate;
    }

}