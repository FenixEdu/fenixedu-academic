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
import Dominio.ISection;
import Dominio.Item;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentItem;

public class ItemOJB extends ObjectFenixOJB implements IPersistentItem {
   
    public IItem readBySectionAndName(ISection section, String name) throws ExcepcaoPersistencia {
        try {
            IItem item = null;
            String oqlQuery = "select sectionItem from " + Item.class.getName();
            oqlQuery += " where section.name = $1 and name = $2";
            query.create(oqlQuery);
            query.bind(section.getName());
            query.bind(name);
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
