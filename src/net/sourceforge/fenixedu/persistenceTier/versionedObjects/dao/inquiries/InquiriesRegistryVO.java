/*
 * Created on 27/Jun/2005 - 11:52:26
 * 
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.inquiries.IInquiriesRegistry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesRegistry;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class InquiriesRegistryVO extends VersionedObjectsBase implements
		IPersistentInquiriesRegistry {

	public List<IInquiriesRegistry> readByStudentId(Integer studentId)
	throws ExcepcaoPersistencia {

		IStudent student = (IStudent) readByOID(Student.class, studentId);
		
		List<IInquiriesRegistry> res = student.getInquiriesRegistries();
		
		return res;
		
		
	}

	public List<IInquiriesRegistry> readByExecutionPeriodId(Integer executionPeriodId)
	throws ExcepcaoPersistencia {
		IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class, executionPeriodId);
		
		List<IInquiriesRegistry> res = executionPeriod.getInquiriesRegistries();
		
		return res;
		
	}

}
