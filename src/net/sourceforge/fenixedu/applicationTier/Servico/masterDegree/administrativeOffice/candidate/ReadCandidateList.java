/*
 * Created on 14/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICandidateSituation;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.SituationName;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidateList implements IService {

    public List run(String degreeName, Specialization degreeType, SituationName candidateSituation,
            Integer candidateNumber, String executionYearString) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        // Get the Actual Execution Year
        final IExecutionYear executionYear = sp.getIPersistentExecutionYear().readExecutionYearByName(executionYearString);

        // Read the candidates
        final List result = sp.getIPersistentMasterDegreeCandidate().readCandidateList(Integer.parseInt(degreeName), degreeType,
                candidateSituation, candidateNumber, executionYear.getIdInternal());

        final List candidateList = new ArrayList();
        final Iterator iterator = result.iterator();
        while (iterator.hasNext()) {
            final IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) iterator.next();
            final InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                    .newInfoFromDomain(masterDegreeCandidate);

            final Iterator situationIterator = masterDegreeCandidate.getSituations().iterator();
            final List situations = new ArrayList();
            while (situationIterator.hasNext()) {
                final InfoCandidateSituation infoCandidateSituation = Cloner
                        .copyICandidateSituation2InfoCandidateSituation((ICandidateSituation) situationIterator
                                .next());
                situations.add(infoCandidateSituation);
            }
            infoMasterDegreeCandidate.setSituationList(situations);

            final IExecutionDegree executionDegree = masterDegreeCandidate.getExecutionDegree();
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
            infoMasterDegreeCandidate.setInfoExecutionDegree(infoExecutionDegree);

            final IDegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            final InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
            infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

            final IDegree degree = degreeCurricularPlan.getDegree();
            final InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
            infoDegreeCurricularPlan.setInfoDegree(infoDegree);

            candidateList.add(infoMasterDegreeCandidate);
        }

        return candidateList;
    }
}