/*
 * ChangeMasterDegreeCandidate.java O Servico ChangeMasterDegreeCandidate altera
 * a informacao de um candidato de Mestrado Nota : E suposto os campos
 * (numeroCandidato, anoCandidatura, chaveCursoMestrado, username) nao se
 * puderem alterar Created on 02 de Dezembro de 2002, 16:25
 */

/**
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 */

package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.candidate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.ICandidateSituation;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.applicationTier.ICandidateView;
import net.sourceforge.fenixedu.applicationTier.Servico.CandidateView;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ChangePersonalInfo;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCandidateSituation;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

public class ChangeApplicationInfo implements IService {

    /**
     * The actor of this class.
     */
    public ChangeApplicationInfo() {
    }

    public InfoMasterDegreeCandidate run(InfoMasterDegreeCandidate newMasterDegreeCandidate,
            InfoPerson infoPerson, UserView userView) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = null;
        IMasterDegreeCandidate existingMasterDegreeCandidate = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            IExecutionDegree executionDegree = Cloner
                    .copyInfoExecutionDegree2ExecutionDegree(newMasterDegreeCandidate
                            .getInfoExecutionDegree());
            existingMasterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate()
                    .readByIdentificationDocNumberAndTypeAndExecutionDegreeAndSpecialization(
                            newMasterDegreeCandidate.getInfoPerson().getNumeroDocumentoIdentificacao(),
                            newMasterDegreeCandidate.getInfoPerson().getTipoDocumentoIdentificacao()
                                    .getTipo(), executionDegree,
                            newMasterDegreeCandidate.getSpecialization());
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (existingMasterDegreeCandidate == null) {
            throw new ExcepcaoInexistente("Unknown Candidate !!");
        }

        try {
            IService service = new ChangePersonalInfo();
            ((ChangePersonalInfo) service).run(userView, infoPerson);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        // Change the Information

        sp.getIPersistentMasterDegreeCandidate().simpleLockWrite(existingMasterDegreeCandidate);

        existingMasterDegreeCandidate.setAverage(newMasterDegreeCandidate.getAverage());
        existingMasterDegreeCandidate.setMajorDegree(newMasterDegreeCandidate.getMajorDegree());
        existingMasterDegreeCandidate.setMajorDegreeSchool(newMasterDegreeCandidate
                .getMajorDegreeSchool());
        existingMasterDegreeCandidate.setMajorDegreeYear(newMasterDegreeCandidate.getMajorDegreeYear());
        existingMasterDegreeCandidate.setSpecializationArea(newMasterDegreeCandidate
                .getSpecializationArea());

        // Change the Candidate Situation
        InfoMasterDegreeCandidate infoMasterDegreeCandidate = null;

        try {
            IPersistentCandidateSituation candidateSituationDAO = sp.getIPersistentCandidateSituation();

            infoMasterDegreeCandidate = Cloner
                    .copyIMasterDegreeCandidate2InfoMasterDegreCandidate(existingMasterDegreeCandidate);

            List situations = new ArrayList();
            ICandidateView candidateView = null;

            ICandidateSituation candidateSituationFromBD = new CandidateSituation();
            candidateSituationFromBD = (ICandidateSituation) sp.getIPersistentCandidateSituation()
                    .readByOID(CandidateSituation.class,
                            existingMasterDegreeCandidate.getActiveCandidateSituation().getIdInternal(),
                            true);
            candidateSituationFromBD.setValidation(new State(State.INACTIVE));

            //Create the New Candidate Situation
            ICandidateSituation activeCandidateSituation = createActiveSituation(
                    existingMasterDegreeCandidate, candidateSituationDAO);

            sp.confirmarTransaccao();
            sp.iniciarTransaccao();

            situations.add(Cloner
                    .copyICandidateSituation2InfoCandidateSituation(activeCandidateSituation));
            candidateView = new CandidateView(situations);
            userView.setCandidateView(candidateView);

            infoMasterDegreeCandidate.setInfoCandidateSituation(Cloner
                    .copyICandidateSituation2InfoCandidateSituation(activeCandidateSituation));
            infoMasterDegreeCandidate.setInfoPerson(infoPerson);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoMasterDegreeCandidate;
    }

    private ICandidateSituation createActiveSituation(
            IMasterDegreeCandidate existingMasterDegreeCandidate,
            IPersistentCandidateSituation candidateSituationDAO) throws ExcepcaoPersistencia {
        ICandidateSituation activeCandidateSituation = new CandidateSituation();
        candidateSituationDAO.simpleLockWrite(activeCandidateSituation);

        Calendar calendar = Calendar.getInstance();
        activeCandidateSituation.setDate(calendar.getTime());
        activeCandidateSituation.setSituation(new SituationName(SituationName.PENDENT_COM_DADOS));
        activeCandidateSituation.setValidation(new State(State.ACTIVE));

        activeCandidateSituation.setMasterDegreeCandidate(existingMasterDegreeCandidate);
        return activeCandidateSituation;
    }
}