/*
 * SeccaoOJB.java
 *
 * Created on 23 de Agosto de 2002, 16:58
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  ars
 */

import java.util.List;

import org.odmg.QueryException;

import Dominio.ISeccao;
import Dominio.ISitio;
import Dominio.Seccao;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISeccaoPersistente;

public class SeccaoOJB extends ObjectFenixOJB implements ISeccaoPersistente {
    
    /** Creates a new instance of SeccaoOJB */
    public SeccaoOJB() {
    }
    
    public ISeccao readBySitioAndSeccaoAndNome(ISitio sitio, ISeccao seccao, String nome) throws ExcepcaoPersistencia {
        try {
            ISeccao seccaoResultado = null;
            String oqlQuery = "select seccao from " + Seccao.class.getName();
            if (seccao == null)
                oqlQuery += " where chaveSeccaoSuperior = null and nome = $1 and sitio.nome = $2";
            else
                oqlQuery += " where seccaoSuperior.nome = $1 and nome = $2 and sitio.nome = $3";
            query.create(oqlQuery);
            if (seccao != null)
                query.bind(seccao.getNome());
            query.bind(nome);
            query.bind(sitio.getNome());
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                seccaoResultado = (ISeccao) result.get(0);
            return seccaoResultado;
        } catch (QueryException queryEx) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, queryEx);
        }
    }
    
    public void lockWrite(ISeccao seccao) throws ExcepcaoPersistencia {
        super.lockWrite(seccao);
    }
    
    public void delete(ISeccao seccao) throws ExcepcaoPersistencia {
        super.delete(seccao);
    }
    
    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + Seccao.class.getName();
        super.deleteAll(oqlQuery);
    }
    
}
