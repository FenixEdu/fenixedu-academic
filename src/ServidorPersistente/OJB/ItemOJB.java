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
import ServidorPersistente.exceptions.ExistingPersistentException;

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
      
      
	public List readAllItemsBySection(ISection section) throws ExcepcaoPersistencia {
			try {
				IItem item = null;
				String oqlQuery = "select sectionItem from " + Item.class.getName();
				oqlQuery += " where section.name = $1 " ; 
				query.create(oqlQuery);
				query.bind(section.getName());
				List result = (List) query.execute();
				lockRead(result);
				return result;
				
			} catch (QueryException ex) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
		}
		
		    
    public void lockWrite(IItem item) throws ExcepcaoPersistencia {
       
    
		IItem itemFromDB = null;
			if (item == null)
				// Should we throw an exception saying nothing to write or
				// something of the sort?
				// By default, if OJB received a null object it would complain.
				return;

			// read item		
			itemFromDB =
				this.readBySectionAndName(item.getSection(),item.getName());
		

			// if (item not in database) then write it
			if (itemFromDB == null)
				super.lockWrite(item);
			// else if (item is mapped to the database then write any existing changes)
			else if ((item instanceof Item) &&
					 ((Item) itemFromDB).getInternalCode().equals(
					  ((Item) item).getInternalCode())) {

						super.lockWrite(item);
				// No need to werite it because it is already mapped.
				//super.lockWrite(lessonToWrite);
				// else throw an AlreadyExists exception.
			} else
				throw new ExistingPersistentException();
		}
    
    
    
    public void delete(IItem item) throws ExcepcaoPersistencia {
        super.delete(item);
    }
    
    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + Item.class.getName();
        super.deleteAll(oqlQuery);
    }
    
}
