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
import Dominio.ICurso;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;

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
				return curso;
            }
            else
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.NON_EXISTING);

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
			ICurso curso = null;
			String oqlQuery = "select curso from " + Curso.class.getName();
			oqlQuery += " where sigla = $1 ";
			query.create(oqlQuery);
			query.bind(licenciatura.getSigla());
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0){ 
				super.delete(licenciatura);
			}
			else
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.NON_EXISTING);
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
