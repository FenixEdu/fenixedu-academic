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

import org.apache.ojb.broker.query.Criteria;

import Dominio.IItem;
import Dominio.ISection;
import Dominio.Item;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class ItemOJB extends ObjectFenixOJB implements IPersistentItem {
   
    public IItem readBySectionAndName(ISection section, String name) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("section.name",section.getName());
        crit.addEqualTo("name",name);
        crit.addEqualTo("section.site.executionCourse.code",section.getSite().getExecutionCourse().getSigla());
        crit.addEqualTo("section.site.executionCourse.executionPeriod.name",section.getSite().getExecutionCourse().getExecutionPeriod().getName());
        crit.addEqualTo("section.site.executionCourse.executionPeriod.executionYear.year",section.getSite().getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());
        return (IItem) queryObject(Item.class,crit);
       
    }
      
      
	public List readAllItemsBySection(ISection section) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("section.name",section.getName());
        crit.addEqualTo("section.site.executionCourse.code",section.getSite().getExecutionCourse().getSigla());
        crit.addEqualTo("section.site.executionCourse.executionPeriod.name",section.getSite().getExecutionCourse().getExecutionPeriod().getName());
        crit.addEqualTo("section.site.executionCourse.executionPeriod.executionYear.year",section.getSite().getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());
		return queryList(Item.class,crit);	
       
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
					 itemFromDB.getIdInternal().equals(
					  item.getIdInternal())) {

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
    
   
    
}
