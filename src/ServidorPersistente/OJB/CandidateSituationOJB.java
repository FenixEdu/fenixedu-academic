/*
 * CandidateSituationOJB.java
 *
 * Created on 1 de Novembro de 2002, 16:13
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.CandidateSituation;
import Dominio.ICandidateSituation;
import Dominio.ICursoExecucao;
import Dominio.IMasterDegreeCandidate;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCandidateSituation;
import Util.SituationName;
import Util.State;

public class CandidateSituationOJB extends ObjectFenixOJB implements IPersistentCandidateSituation{
    
    
    /** Creates a new instance of CandidateSituationOJB */
    public CandidateSituationOJB() {
    }
    
    public ICandidateSituation readActiveCandidateSituation(IMasterDegreeCandidate masterDegreeCandidate) throws ExcepcaoPersistencia {
        try {
            ICandidateSituation candidate = null;
            
            String oqlQuery = "select all from " + CandidateSituation.class.getName()
				            + " where masterDegreeCandidate.candidateNumber = $1"
				            + " and masterDegreeCandidate.executionDegree.curricularPlan.degree.sigla = $2"
				            + " and masterDegreeCandidate.executionDegree.executionYear.year = $3"
							+ " and validation = $4"
							+ " and masterDegreeCandidate.specialization = $5";
            
            query.create(oqlQuery);
			query.bind(masterDegreeCandidate.getCandidateNumber());
			query.bind(masterDegreeCandidate.getExecutionDegree().getCurricularPlan().getDegree().getSigla());
			query.bind(masterDegreeCandidate.getExecutionDegree().getExecutionYear().getYear());
			query.bind(new Integer(State.ACTIVE));
			query.bind(masterDegreeCandidate.getSpecialization().getSpecialization());
            
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                candidate = (ICandidateSituation) result.get(0);
            return candidate;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
	public List readCandidateSituations(IMasterDegreeCandidate masterDegreeCandidate) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + CandidateSituation.class.getName()
							+ " where masterDegreeCandidate.candidateNumber = $1"
							+ " and masterDegreeCandidate.executionDegree.curricularPlan.degree.sigla = $2"
							+ " and masterDegreeCandidate.executionDegree.executionYear.year = $3"
							+ " and masterDegreeCandidate.specialization = $4";
            
			query.create(oqlQuery);
			query.bind(masterDegreeCandidate.getCandidateNumber());
			query.bind(masterDegreeCandidate.getExecutionDegree().getCurricularPlan().getDegree().getSigla());
			query.bind(masterDegreeCandidate.getExecutionDegree().getExecutionYear().getYear());
			query.bind(masterDegreeCandidate.getSpecialization().getSpecialization());
            
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
    
	public List readActiveSituationsBySituationList(ICursoExecucao executionDegree, List situations) throws ExcepcaoPersistencia {
		 	
		Criteria criteria = new Criteria();
		Criteria criteriaSituations = new Criteria();
		criteria.addEqualTo("validation", new State(State.ACTIVE));
		criteria.addEqualTo("masterDegreeCandidate.executionDegree.idInternal", executionDegree.getIdInternal());
		
		if ((situations != null) && (situations.size() != 0)){
			List situationsInteger = new ArrayList();
			Iterator iterator = situations.iterator();
			while(iterator.hasNext()){
				situationsInteger.add(((SituationName) iterator.next()).getSituationName());

			}
			criteriaSituations.addIn("situation", situationsInteger);		
			criteria.addAndCriteria(criteriaSituations);
		}
				
		List result =  queryList(CandidateSituation.class,criteria);
		return result;
	}
    
    
    public void writeCandidateSituation(ICandidateSituation candidateSituationToWrite) throws ExcepcaoPersistencia {
		super.lockWrite(candidateSituationToWrite);
    }
    
    public void delete(ICandidateSituation candidateSituation) throws ExcepcaoPersistencia {
        super.delete(candidateSituation);
    }

    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + CandidateSituation.class.getName();
        super.deleteAll(oqlQuery);
    }    



	public List readCandidateListforRegistration(ICursoExecucao executionDegree) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		Criteria criteriaDocs = new Criteria();
		criteria.addEqualTo("validation", new State(State.ACTIVE));
		criteria.addEqualTo("masterDegreeCandidate.executionDegree.idInternal", executionDegree.getIdInternal());
		List situations = new ArrayList();

		situations.add(new Integer(SituationName.ADMITED_CONDICIONAL_CURRICULAR));
		situations.add(new Integer(SituationName.ADMITED_CONDICIONAL_FINALIST));
		situations.add(new Integer(SituationName.ADMITED_CONDICIONAL_OTHER));
		situations.add(new Integer(SituationName.ADMITIDO));
		situations.add(new Integer(SituationName.ADMITED_SPECIALIZATION));

		criteriaDocs.addIn("situation", situations);		
		criteria.addAndCriteria(criteriaDocs);
		
		List result = queryList(CandidateSituation.class, criteria);
		if ((result == null) || (result.size() == 0)){
			return null;
		}
		return result;

	}			


    
} // End of class definition



