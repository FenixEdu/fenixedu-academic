package ServidorPersistente.OJB;

import java.util.List;

import org.odmg.QueryException;

import Dominio.DegreeEnrolmentInfo;
import Dominio.IDegreeCurricularPlan;
import Dominio.IDegreeEnrolmentInfo;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeEnrolmentInfo;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class DegreeEnrolmentInfoOJB extends ObjectFenixOJB implements IPersistentDegreeEnrolmentInfo {

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + DegreeEnrolmentInfo.class.getName();
		super.deleteAll(oqlQuery);
	}

	public void delete(IDegreeEnrolmentInfo degreeEnrolmentInfo) throws ExcepcaoPersistencia {
		super.delete(degreeEnrolmentInfo);
	}

	public void lockWrite(IDegreeEnrolmentInfo degreeEnrolmentInfoToWrite) throws ExcepcaoPersistencia, ExistingPersistentException {

		IDegreeEnrolmentInfo degreeEnrolmentInfoFromDB = null;

		// If there is nothing to write, simply return.
		if (degreeEnrolmentInfoToWrite == null) {
			return;
		}

		// Read DegreeEnrolmentInfo from database.
		degreeEnrolmentInfoFromDB =(IDegreeEnrolmentInfo) this.readDegreeEnrolmentInfoByDegreeCurricularPlan(degreeEnrolmentInfoToWrite.getDegreeCurricularPlan());
		// If DegreeEnrolmentInfo is not in database, then write it.
		if (degreeEnrolmentInfoFromDB == null) {
			super.lockWrite(degreeEnrolmentInfoToWrite);
		// else If the DegreeEnrolmentInfo is mapped to the database, then write any existing changes.
		} else if (
			(degreeEnrolmentInfoToWrite instanceof DegreeEnrolmentInfo) &&
			((DegreeEnrolmentInfo) degreeEnrolmentInfoFromDB).getInternalID().equals(((DegreeEnrolmentInfo) degreeEnrolmentInfoToWrite).getInternalID())) {
			super.lockWrite(degreeEnrolmentInfoToWrite);
		// else Throw an already existing exception
		} else {
			throw new ExistingPersistentException();
		}
	}

	public List readAll() throws ExcepcaoPersistencia {

		try {
			String oqlQuery = "select all from " + DegreeEnrolmentInfo.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public IDegreeEnrolmentInfo readDegreeEnrolmentInfoByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia {

		try {
			IDegreeEnrolmentInfo degreeEnrolmentInfo = null;
			String oqlQuery = "select all from " + DegreeEnrolmentInfo.class.getName() +
			" where degreeCurricularPlan.name = $1 " +
			" and degreeCurricularPlan.degree.sigla = $2";

			query.create(oqlQuery);
			query.bind(degreeCurricularPlan.getName());
			query.bind(degreeCurricularPlan.getDegree().getSigla());
			List result = (List) query.execute();
			lockRead(result);
			if ((result != null) && (result.size() != 0)) {
				degreeEnrolmentInfo = (IDegreeEnrolmentInfo) result.get(0);
			}
			return degreeEnrolmentInfo;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
}