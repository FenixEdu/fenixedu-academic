/*
 * Created on 21/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.odmg.QueryException;

import Dominio.Contributor;
import Dominio.IContributor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentContributor;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ContributorOJB extends ObjectFenixOJB implements IPersistentContributor {
    
	public ContributorOJB() {
	}
    	
	public IContributor readByContributorNumber(Integer contributorNumber) throws ExcepcaoPersistencia {
		try {
			IContributor contributor = null;
			String oqlQuery = "select all from " + Contributor.class.getName();
			oqlQuery += " where contributorNumber = $1";
			query.create(oqlQuery);
			query.bind(contributorNumber);
			List result = (List) query.execute();
			super.lockRead(result);
			if (result.size() != 0)
				contributor = (IContributor) result.get(0);
			return contributor;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}		
	}
	
	
	public void write(IContributor contributor) 
		throws ExcepcaoPersistencia, ExistingPersistentException {
    		
			IContributor contributorBD = null;
    		
			// If there is nothing to write, simply return.
			if (contributor == null)
				return;
    		
			// Read contributor from database.
			contributorBD = this.readByContributorNumber(contributor.getContributorNumber());
    		
			// If contributor is not in database, then write it.
			if (contributorBD == null)
				super.lockWrite(contributor);
			// else If the contributor is mapped to the database, then write any existing changes.
			else if ((contributor instanceof Contributor) &&
					 ((Contributor) contributorBD).getIdInternal().equals(
						((Contributor) contributor).getIdInternal())) {
						contributorBD.setContributorNumber(contributor.getContributorNumber());
    					contributorBD.setContributorName(contributor.getContributorName());
						contributorBD.setContributorAddress(contributor.getContributorAddress());
			// else Throw an already existing exception
			} else
				throw new ExistingPersistentException();
	
	}
	
	public List readAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Contributor.class.getName()
							+ " order by contributorName asc";
			query.create(oqlQuery);
			List result = (List) query.execute();
			super.lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	
	
	public List readContributorListByNumber(Integer contributorNumber) throws ExcepcaoPersistencia{
		try {
			String oqlQuery = "select all from " + Contributor.class.getName();
			
			if (contributorNumber != null)
				oqlQuery += " where contributorNumber = $1";

			oqlQuery += " order by contributorNumber asc ";
			query.create(oqlQuery);

			if (contributorNumber != null)
				query.bind(contributorNumber);
			
			List result = (List) query.execute();
			super.lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
		
		 
	}

}
