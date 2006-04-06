/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsWithDistributedTest extends Service {

	public List run(Integer executionCourseId, Integer distributedTestId) throws FenixServiceException, ExcepcaoPersistencia {
		List<InfoStudent> result = new ArrayList<InfoStudent>();

		DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);
		if (distributedTest == null)
			throw new FenixServiceException();

		List<Student> studentList = persistentSupport.getIPersistentStudentTestQuestion()
				.readStudentsByDistributedTest(distributedTest.getIdInternal());

		for (Student student : studentList) {
            result.add(InfoStudentWithInfoPerson.newInfoFromDomain(student));    
        }
		
		return result;
	}
    
}
