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

import java.util.Iterator;
import java.util.List;

import org.odmg.QueryException;

import Dominio.Curso;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.exceptions.ExistingPersistentException;

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
            if (result.size() != 0){ 
                curso = (ICurso) result.get(0);
            }
			return curso;
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
				(degreeToWrite instanceof Curso)
					&& ((Curso) degreeFromDB)
						.getCodigoInterno()
						.equals(
						((Curso) degreeToWrite)
							.getCodigoInterno())) {
				super.lockWrite(degreeToWrite);
				// else Throw an already existing exception
			} else
				throw new ExistingPersistentException();
    }
    
    public void delete(ICurso licenciatura) throws ExcepcaoPersistencia {
		try {
			IDegreeCurricularPlan degreeCurricularPlan = null;
			IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;
			DegreeCurricularPlanOJB degreeCurricularPlanOJB = new DegreeCurricularPlanOJB();
			String oqlQuery = "select all from " + DegreeCurricularPlan.class.getName();
			oqlQuery += " where degree.nome = $1 ";

			query.create(oqlQuery);
			query.bind(licenciatura.getNome());
			List result = (List) query.execute();
			Iterator iterador = result.iterator();
			while (iterador.hasNext()) {
				degreeCurricularPlan = (IDegreeCurricularPlan) iterador.next();
				degreeCurricularPlanOJB.delete(degreeCurricularPlan);
			}
			super.delete(licenciatura);
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
            String oqlQuery = "select all from " + Curso.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            lockRead(result);
            return result;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }   

}
