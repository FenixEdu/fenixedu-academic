package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.odmg.QueryException;

import Dominio.Branch;
import Dominio.IBranch;
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
		} else if ((branchToWrite instanceof Branch) && ((Branch) branchFromDB).getInternalID().equals(((Branch) branchToWrite).getInternalID())) {
			super.lockWrite(branchToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void delete(IBranch branch) throws ExcepcaoPersistencia {
		try {
			super.delete(branch);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
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
					list.add((IBranch) iterator.next());
			}
			return list;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

}