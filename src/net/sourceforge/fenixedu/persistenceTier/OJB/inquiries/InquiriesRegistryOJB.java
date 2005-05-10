/*
 * Created on 8/Abr/2005 - 16:09:20
 * 
 */

package net.sourceforge.fenixedu.persistenceTier.OJB.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.domain.inquiries.IInquiriesRegistry;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesRegistry;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class InquiriesRegistryOJB extends PersistentObjectOJB implements IPersistentInquiriesRegistry {
	
	public List<IInquiriesRegistry> readByStudentId(Integer studentId)
	throws ExcepcaoPersistencia {
    	Criteria criteria = new Criteria();
    	criteria.addEqualTo("keyStudent", studentId);
    	
    	return queryList(InquiriesRegistry.class, criteria);
		
	}

}
