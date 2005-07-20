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
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICandidateSituation;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadMasterDegreeCandidate implements IService {

    public InfoMasterDegreeCandidate run(InfoExecutionDegree infoExecutionDegree,
            Integer candidateNumber, Specialization degreeType) throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        // Read the candidates
        IMasterDegreeCandidate masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate()
                .readByNumberAndExecutionDegreeAndSpecialization(candidateNumber,
                        infoExecutionDegree.getIdInternal(), degreeType);

        if (masterDegreeCandidate == null)
            return null;
        Iterator iterator = masterDegreeCandidate.getSituations().iterator();
        InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                .newInfoFromDomain(masterDegreeCandidate);
        List situations = new ArrayList();
        while (iterator.hasNext()) {
            InfoCandidateSituation infoCandidateSituation = Cloner
                    .copyICandidateSituation2InfoCandidateSituation((ICandidateSituation) iterator
                            .next());
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

        IPerson person = (IPerson) sp.getIPessoaPersistente().readByOID(Person.class,
                infoPerson.getIdInternal());
        infoPerson.copyToDomain(infoPerson, person);

        // Read the candidates

        IMasterDegreeCandidate masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate()
                .readByExecutionDegreeAndPerson(infoExecutionDegree.getIdInternal(),
                        person.getIdInternal());

        if (masterDegreeCandidate == null)
            return null;
        Iterator iterator = masterDegreeCandidate.getSituations().iterator();
        InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                .newInfoFromDomain(masterDegreeCandidate);
        List situations = new ArrayList();
        while (iterator.hasNext()) {
            InfoCandidateSituation infoCandidateSituation = Cloner
                    .copyICandidateSituation2InfoCandidateSituation((ICandidateSituation) iterator
                            .next());
            situations.add(infoCandidateSituation);

            // Check if this is the Active Situation
            if (infoCandidateSituation.getValidation().equals(new State(State.ACTIVE)))
                infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
        }
        infoMasterDegreeCandidate.setSituationList(situations);

        return infoMasterDegreeCandidate;
    }

}
