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
		
		    
   
    
    
    
    public void delete(IItem item) throws ExcepcaoPersistencia {
        super.delete(item);
    }
    
   
    
}
