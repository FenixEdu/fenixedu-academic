package ServidorPersistente.OJB;

import ServidorPersistente.IPersistentDegreeCurricularPlanEnrolmentInfo;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class DegreeCurricularPlanEnrolmentInfoOJB extends ObjectFenixOJB implements IPersistentDegreeCurricularPlanEnrolmentInfo {

/*	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + DegreeCurricularPlanEnrolmentInfo.class.getName();
		super.deleteAll(oqlQuery);
	}

	public void delete(IDegreeCurricularPlanEnrolmentInfo degreeEnrolmentInfo) throws ExcepcaoPersistencia {
		super.delete(degreeEnrolmentInfo);
	}

	public void lockWrite(IDegreeCurricularPlanEnrolmentInfo degreeEnrolmentInfoToWrite) throws ExcepcaoPersistencia, ExistingPersistentException {

		IDegreeCurricularPlanEnrolmentInfo degreeEnrolmentInfoFromDB = null;

		// If there is nothing to write, simply return.
		if (degreeEnrolmentInfoToWrite == null) {
			return;
		}

		// Read DegreeCurricularPlanEnrolmentInfo from database.
		degreeEnrolmentInfoFromDB =(IDegreeCurricularPlanEnrolmentInfo) this.readDegreeEnrolmentInfoByDegreeCurricularPlan(degreeEnrolmentInfoToWrite.getDegreeCurricularPlan());
		// If DegreeCurricularPlanEnrolmentInfo is not in database, then write it.
		if (degreeEnrolmentInfoFromDB == null) {
			super.lockWrite(degreeEnrolmentInfoToWrite);
		// else If the DegreeCurricularPlanEnrolmentInfo is mapped to the database, then write any existing changes.
		} else if (
			(degreeEnrolmentInfoToWrite instanceof DegreeCurricularPlanEnrolmentInfo) &&
			((DegreeCurricularPlanEnrolmentInfo) degreeEnrolmentInfoFromDB).getInternalID().equals(((DegreeCurricularPlanEnrolmentInfo) degreeEnrolmentInfoToWrite).getInternalID())) {
			super.lockWrite(degreeEnrolmentInfoToWrite);
		// else Throw an already existing exception
		} else {
			throw new ExistingPersistentException();
		}
	}

	public List readAll() throws ExcepcaoPersistencia {

		try {
			String oqlQuery = "select all from " + DegreeCurricularPlanEnrolmentInfo.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public IDegreeCurricularPlanEnrolmentInfo readDegreeEnrolmentInfoByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia {

		try {
			IDegreeCurricularPlanEnrolmentInfo degreeEnrolmentInfo = null;
			String oqlQuery = "select all from " + DegreeCurricularPlanEnrolmentInfo.class.getName() +
			" where degreeCurricularPlan.name = $1 " +
			" and degreeCurricularPlan.degree.sigla = $2";

			query.create(oqlQuery);
			query.bind(degreeCurricularPlan.getName());
			query.bind(degreeCurricularPlan.getDegree().getSigla());
			List result = (List) query.execute();
			lockRead(result);
			if ((result != null) && (result.size() != 0)) {
				degreeEnrolmentInfo = (IDegreeCurricularPlanEnrolmentInfo) result.get(0);
			}
			return degreeEnrolmentInfo;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	
	*/
}