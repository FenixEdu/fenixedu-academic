package ServidorPersistente.OJB;

import java.util.List;

import org.odmg.QueryException;

import Dominio.Guide;
import Dominio.IGuide;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGuide;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GuideOJB extends ObjectFenixOJB implements IPersistentGuide {

	public GuideOJB() {}

	public void write(IGuide guideToWrite) throws ExcepcaoPersistencia, ExistingPersistentException {
		IGuide guideBD = null;
		if (guideToWrite == null)
			// Should we throw an exception saying nothing to write or
			// something of the sort?
			// By default, if OJB received a null object it would complain.
			return;

		// read Guide
		
		guideBD = this.readByNumberAndYear(guideToWrite.getNumber(), guideToWrite.getYear()); 
						
		// if (guide not in database) then write it
		if (guideBD == null)
			super.lockWrite(guideToWrite);
		// else if (guide is mapped to the database then write any existing changes)
		else if ((guideToWrite instanceof IGuide) &&
				 ((Guide) guideBD).getInternalCode().equals(
		          ((Guide) guideToWrite).getInternalCode())) {

			guideBD.setTotal(guideToWrite.getTotal());
			guideBD.setRemarks(guideToWrite.getRemarks());
			guideBD.setContributor(guideToWrite.getContributor());

			// No need to re-write it because it is already mapped.
			//super.lockWrite(lessonToWrite);
			// else throw an AlreadyExists exception.
		} else
			throw new ExistingPersistentException();
	}

	public IGuide readByNumberAndYear(Integer number, Integer year) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Guide.class.getName();
			oqlQuery += " where number = $1";
			oqlQuery += " and year = $2";
			query.create(oqlQuery);

			query.bind(number);
			query.bind(year);

			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				return (IGuide) result.get(0);
			return null;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	
	
}
