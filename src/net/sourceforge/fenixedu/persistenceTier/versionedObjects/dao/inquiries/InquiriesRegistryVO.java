/*
 * Created on 27/Jun/2005 - 11:52:26
 * 
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesRegistry;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class InquiriesRegistryVO extends VersionedObjectsBase implements
		IPersistentInquiriesRegistry {

	public List<InquiriesRegistry> readByStudentId(Integer studentId)
	throws ExcepcaoPersistencia {

		Student student = (Student) readByOID(Student.class, studentId);
		
		List<InquiriesRegistry> res = student.getAssociatedInquiriesRegistries();
		
		return res;
		
		
	}

	public List<InquiriesRegistry> readByExecutionPeriodId(Integer executionPeriodId)
	throws ExcepcaoPersistencia {
		ExecutionPeriod executionPeriod = (ExecutionPeriod) readByOID(ExecutionPeriod.class, executionPeriodId);
		
		List<InquiriesRegistry> res = executionPeriod.getAssociatedInquiriesRegistries();
		
		return res;
		
	}

}
