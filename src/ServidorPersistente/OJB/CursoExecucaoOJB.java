/*
 * CursoExecucaoOJB.java
 *
 * Created on 2 de Novembro de 2002, 21:17
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  rpfi
 */

import java.util.List;

import org.odmg.QueryException;

import Dominio.CursoExecucao;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;

public class CursoExecucaoOJB extends ObjectFenixOJB implements ICursoExecucaoPersistente {
   
    public ICursoExecucao readByCursoAndAnoLectivo(ICurso curso, String anoLectivo) throws ExcepcaoPersistencia {
        try {
            ICursoExecucao cursoExecucao = null;
            String oqlQuery = "select all from " + CursoExecucao.class.getName();
            oqlQuery += " where curso.sigla = $1 and anoLectivo = $2";
            query.create(oqlQuery);
            query.bind(curso.getSigla());
            query.bind(anoLectivo);
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                cursoExecucao = (ICursoExecucao) result.get(0);
            return cursoExecucao;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
        
    public void lockWrite(ICursoExecucao cursoExecucao) throws ExcepcaoPersistencia {
        super.lockWrite(cursoExecucao);
    }
    
    public void delete(ICursoExecucao cursoExecucao) throws ExcepcaoPersistencia {
        super.delete(cursoExecucao);
    }
    
    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + CursoExecucao.class.getName();
        super.deleteAll(oqlQuery);
    }
    
    public ICursoExecucao readBySigla(String sigla) throws ExcepcaoPersistencia {
        try {
            ICursoExecucao cursoExecucao = null;
            String oqlQuery = "select all from " + CursoExecucao.class.getName();
            oqlQuery += " where curso.sigla = $1";
            query.create(oqlQuery);
            query.bind(sigla);
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                cursoExecucao = (ICursoExecucao) result.get(0);
            return cursoExecucao;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

}
