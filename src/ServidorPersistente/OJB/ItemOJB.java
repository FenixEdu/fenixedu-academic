/*
 * ItemOJB.java
 *
 * Created on 21 de Agosto de 2002, 16:36
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  ars
 */

import java.util.List;

import org.odmg.QueryException;

import Dominio.IItem;
import Dominio.ISeccao;
import Dominio.Item;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IItemPersistente;

public class ItemOJB extends ObjectFenixOJB implements IItemPersistente {
   
    public IItem readBySeccaoAndNome(ISeccao seccao, String nome) throws ExcepcaoPersistencia {
        try {
            IItem item = null;
            String oqlQuery = "select itemseccao from " + Item.class.getName();
            oqlQuery += " where seccao.nome = $1 and nome = $2";
            query.create(oqlQuery);
            query.bind(seccao.getNome());
            query.bind(nome);
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                item = (IItem) result.get(0);
            return item;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
        
    public void lockWrite(IItem item) throws ExcepcaoPersistencia {
        super.lockWrite(item);
    }
    
    public void delete(IItem item) throws ExcepcaoPersistencia {
        super.delete(item);
    }
    
    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + Item.class.getName();
        super.deleteAll(oqlQuery);
    }
    
}
