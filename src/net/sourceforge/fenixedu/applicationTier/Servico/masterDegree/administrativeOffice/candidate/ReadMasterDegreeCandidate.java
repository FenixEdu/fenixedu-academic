/*
 * Created on 14/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.State;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadMasterDegreeCandidate extends Service {

    public InfoMasterDegreeCandidate run(InfoExecutionDegree infoExecutionDegree,
            Integer candidateNumber, Specialization degreeType) throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        // Read the candidates
        MasterDegreeCandidate masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate()
                .readByNumberAndExecutionDegreeAndSpecialization(candidateNumber,
                        infoExecutionDegree.getIdInternal(), degreeType);

        if (masterDegreeCandidate == null)
            return null;
        Iterator iterator = masterDegreeCandidate.getSituations().iterator();
        InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                .newInfoFromDomain(masterDegreeCandidate);
        List situations = new ArrayList();
        while (iterator.hasNext()) {
            InfoCandidateSituation infoCandidateSituation = InfoCandidateSituation.newInfoFromDomain((CandidateSituation) iterator.next());
            situations.add(infoCandidateSituation);

            // Check if this is the Active Situation
            if (infoCandidateSituation.getValidation().equals(new State(State.ACTIVE)))
                infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
        }
        infoMasterDegreeCandidate.setSituationList(situations);

        return infoMasterDegreeCandidate;
    }

    public InfoMasterDegreeCandidate run(InfoExecutionDegree infoExecutionDegree, InfoPerson infoPerson)
            throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        Person person = (Person) sp.getIPessoaPersistente().readByOID(Person.class,
                infoPerson.getIdInternal());

        // Read the candidates

        MasterDegreeCandidate masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate()
                .readByExecutionDegreeAndPerson(infoExecutionDegree.getIdInternal(),
                        person.getIdInternal());

        if (masterDegreeCandidate == null)
            return null;
        Iterator iterator = masterDegreeCandidate.getSituations().iterator();
        InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                .newInfoFromDomain(masterDegreeCandidate);
        List situations = new ArrayList();
        while (iterator.hasNext()) {
            InfoCandidateSituation infoCandidateSituation = InfoCandidateSituation.newInfoFromDomain((CandidateSituation) iterator.next());
            situations.add(infoCandidateSituation);

            // Check if this is the Active Situation
            if (infoCandidateSituation.getValidation().equals(new State(State.ACTIVE)))
                infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
        }
        infoMasterDegreeCandidate.setSituationList(situations);

        return infoMasterDegreeCandidate;
    }

}
