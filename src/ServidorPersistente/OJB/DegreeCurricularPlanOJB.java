package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.DegreeCurricularPlan;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.DegreeCurricularPlanState;

/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class DegreeCurricularPlanOJB extends ObjectFenixOJB implements IPersistentDegreeCurricularPlan {

	public DegreeCurricularPlanOJB() {
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + DegreeCurricularPlan.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			Iterator iter = result.iterator();
			while (iter.hasNext()) {
				IDegreeCurricularPlan curricularPlan = (IDegreeCurricularPlan) iter.next();
				delete(curricularPlan);

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcepcaoPersistencia();
		}
	}

	public void write(IDegreeCurricularPlan degreeCurricularPlanToWrite) throws ExcepcaoPersistencia, ExistingPersistentException {

		IDegreeCurricularPlan degreeCurricularPlanFromDB = null;

		// If there is nothing to write, simply return.
		if (degreeCurricularPlanToWrite == null)
			return;

		// Read degreeCurricularPlan from database.
		degreeCurricularPlanFromDB = this.readByNameAndDegree(degreeCurricularPlanToWrite.getName(), degreeCurricularPlanToWrite.getDegree());

		// If degreeCurricularPlan is not in database, then write it.
		if (degreeCurricularPlanFromDB == null)
			super.lockWrite(degreeCurricularPlanToWrite);
		// else If the degreeCurricularPlan is mapped to the database, then write any existing changes.
		else if (
			(degreeCurricularPlanToWrite instanceof DegreeCurricularPlan)
				&& ((DegreeCurricularPlan) degreeCurricularPlanFromDB).getIdInternal().equals(
					((DegreeCurricularPlan) degreeCurricularPlanToWrite).getIdInternal())) {
			super.lockWrite(degreeCurricularPlanToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public ArrayList readAll() throws ExcepcaoPersistencia {
		try {
			ArrayList listade = new ArrayList();
			String oqlQuery = "select all from " + DegreeCurricularPlan.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			super.lockRead(result);
			if (result.size() != 0) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
					listade.add(iterator.next());
			}
			return listade;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void deleteDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia {
		
		super.delete(degreeCurricularPlan);
	
	}

//	public IDegreeCurricularPlan readByNameAndDegree(String name, ICurso degree) throws ExcepcaoPersistencia {
//		try {
//			IDegreeCurricularPlan de = null;
//
//			String oqlQuery = "select all from " + DegreeCurricularPlan.class.getName();
//			oqlQuery += " where name = $1 " + " and degree.sigla = $2 ";
//
//			query.create(oqlQuery);
//			query.bind(name);
//			query.bind(degree.getSigla());
//
//			List result = (List) query.execute();
//			super.lockRead(result);
//			if (result.size() != 0)
//				de = (IDegreeCurricularPlan) result.get(0);
//			return de;
//		} catch (QueryException ex) {
//			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
//		}
//
//	}

	public List readByDegree(ICurso degree) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + DegreeCurricularPlan.class.getName();
			oqlQuery += " where degree.nome = $1 " + " and degree.sigla = $2 " + " and degree.tipoCurso = $3 ";

			query.create(oqlQuery);
			query.bind(degree.getNome());
			query.bind(degree.getSigla());
			query.bind(degree.getTipoCurso());

			List result = (List) query.execute();
			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	
	public List readByDegreeAndState(ICurso degree,DegreeCurricularPlanState state) throws ExcepcaoPersistencia{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("degree.nome",degree.getNome());
		criteria.addEqualTo("degree.sigla",degree.getSigla());
		criteria.addEqualTo("degree.tipoCurso",degree.getTipoCurso());
		criteria.addEqualTo("state",state);
		
		return queryList(DegreeCurricularPlan.class, criteria);
	}

	public IDegreeCurricularPlan readByNameAndDegree(String name, ICurso degree) throws ExcepcaoPersistencia {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("name", name);
		criteria.addEqualTo("degreeKey", degree.getIdInternal());

		return (IDegreeCurricularPlan) queryObject(DegreeCurricularPlan.class, criteria);
	}
}