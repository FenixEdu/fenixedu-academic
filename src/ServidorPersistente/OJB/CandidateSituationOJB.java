/*
 * CandidateSituationOJB.java Created on 1 de Novembro de 2002, 16:13
 */

/**
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 */

package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CandidateSituation;
import Dominio.ICandidateSituation;
import Dominio.ICursoExecucao;
import Dominio.IMasterDegreeCandidate;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCandidateSituation;
import Util.SituationName;
import Util.State;

public class CandidateSituationOJB extends PersistentObjectOJB implements IPersistentCandidateSituation {

    /** Creates a new instance of CandidateSituationOJB */
    public CandidateSituationOJB() {
    }

    public ICandidateSituation readActiveCandidateSituation(IMasterDegreeCandidate masterDegreeCandidate)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("masterDegreeCandidate.candidateNumber", masterDegreeCandidate
                .getCandidateNumber());
        crit.addEqualTo("masterDegreeCandidate.executionDegree.curricularPlan.degree.sigla",
                masterDegreeCandidate.getExecutionDegree().getCurricularPlan().getDegree().getSigla());
        crit.addEqualTo("masterDegreeCandidate.executionDegree.executionYear.year",
                masterDegreeCandidate.getExecutionDegree().getExecutionYear().getYear());
        crit.addEqualTo("validation", new Integer(State.ACTIVE));
        crit.addEqualTo("masterDegreeCandidate.specialization", masterDegreeCandidate
                .getSpecialization().getSpecialization());
        return (ICandidateSituation) queryObject(CandidateSituation.class, crit);

    }

    public List readCandidateSituations(IMasterDegreeCandidate masterDegreeCandidate)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("masterDegreeCandidate.candidateNumber", masterDegreeCandidate
                .getCandidateNumber());
        crit.addEqualTo("masterDegreeCandidate.executionDegree.curricularPlan.degree.sigla",
                masterDegreeCandidate.getExecutionDegree().getCurricularPlan().getDegree().getSigla());
        crit.addEqualTo("masterDegreeCandidate.executionDegree.executionYear.year",
                masterDegreeCandidate.getExecutionDegree().getExecutionYear().getYear());
        crit.addEqualTo("masterDegreeCandidate.specialization", masterDegreeCandidate
                .getSpecialization().getSpecialization());
        return queryList(CandidateSituation.class, crit);

    }

    public List readActiveSituationsBySituationList(ICursoExecucao executionDegree, List situations)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        Criteria criteriaSituations = new Criteria();
        criteria.addEqualTo("validation", new State(State.ACTIVE));
        criteria.addEqualTo("masterDegreeCandidate.executionDegree.idInternal", executionDegree
                .getIdInternal());

        if ((situations != null) && (situations.size() != 0)) {
            List situationsInteger = new ArrayList();
            Iterator iterator = situations.iterator();
            while (iterator.hasNext()) {
                situationsInteger.add(((SituationName) iterator.next()).getSituationName());

            }
            criteriaSituations.addIn("situation", situationsInteger);
            criteria.addAndCriteria(criteriaSituations);
        }

        List result = queryList(CandidateSituation.class, criteria);
        return result;
    }

    public void delete(ICandidateSituation candidateSituation) throws ExcepcaoPersistencia {
        super.delete(candidateSituation);
    }

    public List readCandidateListforRegistration(ICursoExecucao executionDegree)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        Criteria criteriaDocs = new Criteria();
        criteria.addEqualTo("validation", new State(State.ACTIVE));
        criteria.addEqualTo("masterDegreeCandidate.executionDegree.idInternal", executionDegree
                .getIdInternal());
        List situations = new ArrayList();

        situations.add(new Integer(SituationName.ADMITED_CONDICIONAL_CURRICULAR));
        situations.add(new Integer(SituationName.ADMITED_CONDICIONAL_FINALIST));
        situations.add(new Integer(SituationName.ADMITED_CONDICIONAL_OTHER));
        situations.add(new Integer(SituationName.ADMITIDO));
        situations.add(new Integer(SituationName.ADMITED_SPECIALIZATION));

        criteriaDocs.addIn("situation", situations);
        criteria.addAndCriteria(criteriaDocs);

        List result = queryList(CandidateSituation.class, criteria);
        if ((result == null) || (result.size() == 0)) {
            return null;
        }
        return result;

    }

} // End of class definition

