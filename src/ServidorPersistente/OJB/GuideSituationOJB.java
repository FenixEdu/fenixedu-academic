package ServidorPersistente.OJB;

import java.util.List;

import org.odmg.QueryException;

import Dominio.GuideSituation;
import Dominio.IGuide;
import Dominio.IGuideSituation;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGuideSituation;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.SituationOfGuide;
import Util.State;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GuideSituationOJB extends ObjectFenixOJB implements IPersistentGuideSituation {

	public GuideSituationOJB() {}

	public void write(IGuideSituation guideSituationToWrite) throws ExcepcaoPersistencia, ExistingPersistentException {
		IGuideSituation guideSituationBD = null;
		if (guideSituationToWrite == null)
			// Should we throw an exception saying nothing to write or
			// something of the sort?
			// By default, if OJB received a null object it would complain.
			return;

		// read SituationOfGuide
		
		guideSituationBD = this.readByGuideAndSituation(guideSituationToWrite.getGuide(), guideSituationToWrite.getSituation()); 
						
		// if (guide situation not in database) then write it
		if (guideSituationBD == null){
			super.lockWrite(guideSituationToWrite);
		}
		// else if (guide situation is mapped to the database then write any existing changes)
		else if ((guideSituationToWrite != null) &&
				 ((GuideSituation) guideSituationBD).getIdInternal().equals(
		          ((GuideSituation) guideSituationToWrite).getIdInternal())) {
		          	super.lockWrite(guideSituationBD);
 
	
		} else
			throw new ExistingPersistentException();
	}

	public List readByGuide(IGuide guide) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + GuideSituation.class.getName();
			oqlQuery += " where guide.number = $1";
			oqlQuery += " and guide.year = $2";
			query.create(oqlQuery);

			query.bind(guide.getNumber());
			query.bind(guide.getYear());

			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	

	public IGuideSituation readGuideActiveSituation(IGuide guide) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + GuideSituation.class.getName()
			                + " where guide.number = $1"
			                + " and guide.year = $2"
			                + " and state = $3";
			
			query.create(oqlQuery);

			query.bind(guide.getNumber());
			query.bind(guide.getYear());
			query.bind(new Integer(State.ACTIVE));
			

			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0) return (IGuideSituation) result.get(0);
				return null;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	
	public IGuideSituation readByGuideAndSituation(IGuide guide, SituationOfGuide situationOfGuide) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + GuideSituation.class.getName()
							+ " where guide.number = $1"
							+ " and guide.year = $2"
							+ " and guide.version = $3"
							+ " and situation = $4";
			
			query.create(oqlQuery);

			query.bind(guide.getNumber());
			query.bind(guide.getYear());
			query.bind(guide.getVersion());
			query.bind(situationOfGuide.getSituation());
			

			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0) return (IGuideSituation) result.get(0);
				return null;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
}
