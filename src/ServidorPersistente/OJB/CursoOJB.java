/*
 * CursoOJB.java
 *
 * Created on 1 de Novembro de 2002, 12:37
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  rpfi
 */

import java.util.List;

import org.odmg.QueryException;

import Dominio.Curso;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurso;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;

public class CursoOJB extends ObjectFenixOJB implements ICursoPersistente {

	/** Creates a new instance of CursoOJB */
	public CursoOJB() {
	}

	public ICurso readBySigla(String sigla) throws ExcepcaoPersistencia {
		try {
			ICurso curso = null;
			String oqlQuery = "select curso from " + Curso.class.getName();
			oqlQuery += " where sigla = $1 ";
			query.create(oqlQuery);
			query.bind(sigla);
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0) {
				curso = (ICurso) result.get(0);
			}
			return curso;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	
	public ICurso readByIdInternal(Integer idInternal) throws ExcepcaoPersistencia {
			try {
				ICurso degree = null;
				String oqlQuery = "select curso from " + Curso.class.getName();
				oqlQuery += " where idInternal = $1 ";
				query.create(oqlQuery);
				query.bind(idInternal);
				List result = (List) query.execute();	
				lockRead(result);
				
				if (result.size() != 0) {
					degree = (ICurso) result.get(0);
				}
				
				return degree;
			} catch (QueryException ex) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
		}
		

	public void lockWrite(ICurso degreeToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

		ICurso degreeFromDB = null;

		// If there is nothing to write, simply return.
		if (degreeToWrite == null)
			return;

		// Read degree from database.
		degreeFromDB = this.readBySigla(degreeToWrite.getSigla());

		// If degree is not in database, then write it.
		if (degreeFromDB == null)
			super.lockWrite(degreeToWrite);
		// else If the degree is mapped to the database, then write any existing changes.
		else if (
			degreeFromDB.getIdInternal().equals(
				degreeToWrite.getIdInternal())) {
			super.lockWrite(degreeToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}


	public Boolean delete(ICurso degree) throws ExcepcaoPersistencia{
		try{
				String oqlQuery =
					"select degreeCurricularPlan from "
						+ DegreeCurricularPlan.class.getName();
				oqlQuery += " where degree.sigla = $1 ";
				query.create(oqlQuery);
				query.bind(degree.getSigla());
				List result = (List) query.execute();
				Boolean bool = new Boolean(true); 	
					
				if (result.isEmpty())
					super.delete(degree);
				else
					bool = new Boolean(false);
				
				return bool ;			
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
   
	}
		
		

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Curso.class.getName();
		super.deleteAll(oqlQuery);
	}

	public List readAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select degree from " + Curso.class.getName();
			query.create(oqlQuery);
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

	public List readAllByDegreeType(TipoCurso degreeType) throws ExcepcaoPersistencia {
		try {
//			ICurso degree = null;
			String oqlQuery = "select curso from " + Curso.class.getName();
			oqlQuery += " where tipoCurso = $1 "
							+ " order by nome asc";
			query.create(oqlQuery);
			query.bind(degreeType);
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

}
