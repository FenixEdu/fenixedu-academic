/*
 * SitioOJB.java
 *
 * Created on 25 de Agosto de 2002, 1:02
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  ars
 */

import java.util.List;

import org.odmg.QueryException;

import Dominio.ISitio;
import Dominio.Sitio;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISitioPersistente;

public class SitioOJB extends ObjectFenixOJB implements ISitioPersistente {
    
    /** Creates a new instance of SitioOJB */
    public SitioOJB() {
    }
    
    public ISitio readByNome(String nome) throws ExcepcaoPersistencia {
        try {
            ISitio sitio = null;
            String oqlQuery = "select sitio from " + Sitio.class.getName();
            oqlQuery += " where NOME = $1 ";
            query.create(oqlQuery);
            query.bind(nome);
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0) 
                sitio = (ISitio) result.get(0);
            return sitio;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public List readAll() throws ExcepcaoPersistencia {
        try {
            String oqlQuery = "select sitio from " + Sitio.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            lockRead(result);
            return result;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public void lockWrite(ISitio sitio) throws ExcepcaoPersistencia {
        super.lockWrite(sitio);
    }
    
    public void delete(ISitio sitio) throws ExcepcaoPersistencia {
        super.delete(sitio);
    }
    
     public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + Sitio.class.getName();
        super.deleteAll(oqlQuery);
   }
   
}
