package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.Branch;
import Dominio.IBranch;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public class BranchOJB extends ObjectFenixOJB implements IPersistentBranch {

	public BranchOJB() {
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Branch.class.getName();
			super.deleteAll(oqlQuery);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public void lockWrite(IBranch branchToWrite) throws ExcepcaoPersistencia, ExistingPersistentException {

		IBranch branchFromDB = null;

		// If there is nothing to write, simply return.
		if (branchToWrite == null) {
			return;
		}

		// Read branch from database.
		branchFromDB = this.readBranchByDegreeCurricularPlanAndCode(branchToWrite.getDegreeCurricularPlan(), branchToWrite.getCode());

		// If branch is not in database, then write it.
		if (branchFromDB == null) {
			super.lockWrite(branchToWrite);
		// else If the branch is mapped to the database, then write any existing changes.
		} else if ((branchToWrite instanceof Branch) && ((Branch) branchFromDB).getIdInternal().equals(((Branch) branchToWrite).getIdInternal())) {
			super.lockWrite(branchToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void delete(IBranch branch) throws ExcepcaoPersistencia {
		
		super.delete(branch);
		
	}

//	public IBranch readBranchByNameAndCode(String name, String code) throws ExcepcaoPersistencia {
//
//		try {
//			IBranch branch = null;
//			String oqlQuery = "select all from " + Branch.class.getName();
//			oqlQuery += " where name = $1";
//			oqlQuery += " and code = $2";
//			query.create(oqlQuery);
//			query.bind(name);
//			query.bind(code);
//
//			List result = (List) query.execute();
//			try {
//				lockRead(result);
//			} catch (ExcepcaoPersistencia ex) {
//				throw ex;
//			}
//
//			if( (result != null) && (result.size() != 0) ) {
//				branch = (IBranch) result.get(0);
//			}
//			return branch;
//
//		} catch (QueryException ex) {
//			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
//		}
//	}

	/**
	 * @deprecated
	 */
	public IBranch readBranchByDegreeCurricularPlanAndCode(IDegreeCurricularPlan degreeCurricularPlan, String code) throws ExcepcaoPersistencia {

		try {
			IBranch branch = null;
			String oqlQuery = "select all from " + Branch.class.getName();
			oqlQuery += " where code = $1";
			oqlQuery += " and degreeCurricularPlan.name = $2";
			oqlQuery += " and degreeCurricularPlan.degree.nome = $3";
			oqlQuery += " and degreeCurricularPlan.degree.sigla = $4";
			query.create(oqlQuery);
			query.bind(code);
			query.bind(degreeCurricularPlan.getName());
			query.bind(degreeCurricularPlan.getDegree().getNome());
			query.bind(degreeCurricularPlan.getDegree().getSigla());

			List result = (List) query.execute();
			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if( (result != null) && (result.size() != 0) ) {
				branch = (IBranch) result.get(0);
			}
			return branch;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public ArrayList readAll() throws ExcepcaoPersistencia {

		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + Branch.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();

			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if( (result != null) && (result.size() != 0) ) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
					list.add(iterator.next());
			}
			return list;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readByExecutionDegree(ICursoExecucao executionDegree) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Branch.class.getName()
							+ " where degreeCurricularPlan.idInternal = $1";
			query.create(oqlQuery);
			query.bind(executionDegree.getCurricularPlan().getIdInternal());
			List result = (List) query.execute();

			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if( (result != null) && (result.size() != 0) ) {
				return result;
			}
			return null;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	
	public List readByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia {
				
		Criteria crit = new Criteria();
		crit.addEqualTo("keyDegreeCurricularPlan", degreeCurricularPlan.getIdInternal());
		return queryList(Branch.class, crit);
	}

	public IBranch readByDegreeCurricularPlanAndBranchName(IDegreeCurricularPlan degreeCurricularPlan, String branchName) throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		crit.addEqualTo("keyDegreeCurricularPlan", degreeCurricularPlan.getIdInternal());
		crit.addEqualTo("name", branchName);
		return (IBranch) queryObject(Branch.class, crit);
	}

	public IBranch readByDegreeCurricularPlanAndCode(IDegreeCurricularPlan degreeCurricularPlan, String code) throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		crit.addEqualTo("keyDegreeCurricularPlan", degreeCurricularPlan.getIdInternal());
		crit.addEqualTo("code", code);
		return (IBranch) queryObject(Branch.class, crit);
	}

}