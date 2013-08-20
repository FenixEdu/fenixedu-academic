package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Filtro.CandidateApprovalAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ApproveCandidates {

    protected void run(String[] situations, String[] ids, String[] remarks, String[] substitutes) {

        for (int i = 0; i < situations.length; i++) {

            MasterDegreeCandidate masterDegreeCandidate = AbstractDomainObject.fromExternalId(ids[i]);
            CandidateSituation candidateSituationOldFromBD = masterDegreeCandidate.getActiveCandidateSituation();

            candidateSituationOldFromBD.setValidation(new State(State.INACTIVE));

            if ((substitutes[i] != null) && (substitutes[i].length() > 0)) {
                masterDegreeCandidate.setSubstituteOrder(new Integer(substitutes[i]));
            }

            // Create the new Candidate Situation

            CandidateSituation candidateSituation = new CandidateSituation();
            candidateSituation.setDate(Calendar.getInstance().getTime());
            candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
            candidateSituation.setRemarks(remarks[i]);
            candidateSituation.setSituation(new SituationName(situations[i]));
            candidateSituation.setValidation(new State(State.ACTIVE));

            // masterDegreeCandidate.getSituations().add(candidateSituation);

        }

    }

    // Service Invokers migrated from Berserk

    private static final ApproveCandidates serviceInstance = new ApproveCandidates();

    @Service
    public static void runApproveCandidates(String[] situations, String[] ids, String[] remarks, String[] substitutes)
            throws NotAuthorizedException {
        CandidateApprovalAuthorizationFilter.instance.execute(situations, ids, remarks, substitutes);
        serviceInstance.run(situations, ids, remarks, substitutes);
    }

}