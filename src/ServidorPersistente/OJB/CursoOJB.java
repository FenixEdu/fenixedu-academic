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
import Dominio.ICurso;
import Dominio.IPlanoCurricularCurso;
import Dominio.PlanoCurricularCurso;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPlanoCurricularCursoPersistente;

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
    
    public void lockWrite(ICurso licenciatura) throws ExcepcaoPersistencia {
		try {
			ICurso curso = null;
			String oqlQuery = "select curso from " + Curso.class.getName();
			oqlQuery += " where sigla = $1 ";
			query.create(oqlQuery);
			query.bind(licenciatura.getSigla());
			List result = (List) query.execute();
			lockRead(result);

			if (result.size() != 0) 
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.EXISTING);
			else 
				super.lockWrite(licenciatura);				
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
    }
    
    public void delete(ICurso licenciatura) throws ExcepcaoPersistencia {
		try {
			IPlanoCurricularCurso degreeCurricularPlan = null;
			IPlanoCurricularCursoPersistente persistentDegreeCurricularPlan = null;
			PlanoCurricularCursoOJB degreeCurricularPlanOJB = new PlanoCurricularCursoOJB();
			String oqlQuery = "select all from " + PlanoCurricularCurso.class.getName();
			oqlQuery += " where curso.nome = $1 ";

			query.create(oqlQuery);
			query.bind(licenciatura.getNome());
			List result = (List) query.execute();
			Iterator iterador = result.iterator();
			while (iterador.hasNext()) {
				degreeCurricularPlan = (IPlanoCurricularCurso) iterador.next();
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
