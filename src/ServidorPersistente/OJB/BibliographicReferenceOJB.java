package ServidorPersistente.OJB;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.BibliographicReference;
import Dominio.IBibliographicReference;
import Dominio.IExecutionCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBibliographicReference;
/**
 *
 * @author  EP 15
 */
public class BibliographicReferenceOJB
	extends ObjectFenixOJB
	implements IPersistentBibliographicReference {
	public void lockWrite(IBibliographicReference bibliographicReference)
		throws ExcepcaoPersistencia {
		super.lockWrite(bibliographicReference);
	}
	public void delete(IBibliographicReference bibliographicReference)
		throws ExcepcaoPersistencia {
		super.delete(bibliographicReference);
	}
	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery =
			"select all from " + BibliographicReference.class.getName();
		super.deleteAll(oqlQuery);
	}
	public IBibliographicReference readBibliographicReference(
		IExecutionCourse executionCourse,
		String title,
		String authors,
		String reference,
		String year)
		throws ExcepcaoPersistencia {
		try {
			String oqlQuery =
				"select bibiographicReference from "
					+ BibliographicReference.class.getName();
			oqlQuery += " where executionCourse.sigla = $1";
			oqlQuery += " and executionCourse.executionPeriod.name = $2";
			oqlQuery
				+= " and executionCourse.executionPeriod.executionYear.year = $3";
			oqlQuery += " and title = $4";
			oqlQuery += " and authors = $5";
			oqlQuery += " and reference = $6";
			oqlQuery += " and year = $7";
			query.create(oqlQuery);
			query.bind(executionCourse.getSigla());
			query.bind(executionCourse.getExecutionPeriod().getName());
			query.bind(
				executionCourse
					.getExecutionPeriod()
					.getExecutionYear()
					.getYear());
			query.bind(title);
			query.bind(authors);
			query.bind(reference);
			query.bind(year);
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() == 0)
				return null;
			else
				return (IBibliographicReference) result.get(0);
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readBibliographicReference(IExecutionCourse executionCourse)
		throws ExcepcaoPersistencia {
			Criteria crit = new Criteria();
			crit.addEqualTo("keyExecutionCourse",executionCourse.getIdInternal());
			List result = queryList(BibliographicReference.class,crit);			
			return result;
		
	}

}
