package ServidorPersistente.OJB;

import java.util.List;

import org.odmg.QueryException;

import Dominio.GuideEntry;
import Dominio.IGuide;
import Dominio.IGuideEntry;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGuideEntry;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.DocumentType;
import Util.GraduationType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GuideEntryOJB extends ObjectFenixOJB implements IPersistentGuideEntry {

	public GuideEntryOJB() {}

	public void write(IGuideEntry guideEntryToWrite) throws ExcepcaoPersistencia, ExistingPersistentException {
		IGuideEntry guideEntryBD = null;
		if (guideEntryToWrite == null)
			// Should we throw an exception saying nothing to write or
			// something of the sort?
			// By default, if OJB received a null object it would complain.
			return;

		// read Guide Entry		
		
		guideEntryBD = this.readByGuideAndGraduationTypeAndDocumentTypeAndDescription(guideEntryToWrite.getGuide(),
						guideEntryToWrite.getGraduationType(),
						guideEntryToWrite.getDocumentType(),
						guideEntryToWrite.getDescription());



		// if (guideEntry not in database) then write it
		if (guideEntryBD == null) {
			super.lockWrite(guideEntryToWrite);
			return;
			
		}
		// else if (guideEntry is mapped to the database then write any existing changes)
		else if ((guideEntryToWrite != null) &&
				 ((GuideEntry) guideEntryBD).getInternalCode().equals(
		          ((GuideEntry) guideEntryToWrite).getInternalCode())) {

			guideEntryBD.setDescription(guideEntryToWrite.getDescription());
			guideEntryBD.setDocumentType(guideEntryToWrite.getDocumentType());
			guideEntryBD.setGraduationType(guideEntryToWrite.getGraduationType());
			guideEntryBD.setPrice(guideEntryToWrite.getPrice());
			guideEntryBD.setQuantity(guideEntryToWrite.getQuantity());

			// No need to werite it because it is already mapped.
			//super.lockWrite(lessonToWrite);
			// else throw an AlreadyExists exception.
		} else {
			throw new ExistingPersistentException();
		}
			
	}

	public void delete(IGuideEntry guideEntry) throws ExcepcaoPersistencia {
		super.delete(guideEntry);
	}

	public List readByGuide(IGuide guide) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + GuideEntry.class.getName();
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
	
	public IGuideEntry readByGuideAndGraduationTypeAndDocumentTypeAndDescription(IGuide guide,GraduationType graduationType,
				DocumentType documentType, String description) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + GuideEntry.class.getName()
							+ " where guide.number = $1"
							+ " and guide.year = $2"
							+ " and guide.version = $3"
							+ " and graduationType = $4"
							+ " and documentType = $5"
							+ " and description = $6";
			
			query.create(oqlQuery);

			query.bind(guide.getNumber());
			query.bind(guide.getYear());
			query.bind(guide.getVersion());
			query.bind(graduationType.getType());
			query.bind(documentType.getType());
			query.bind(description);

			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)	return (IGuideEntry) result.get(0);
			return null;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	
	
		
	}


	
}
