/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsWithDistributedTest extends Service {

	public List run(Integer executionCourseId, Integer distributedTestId) throws FenixServiceException, ExcepcaoPersistencia {
		List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>();
		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

		DistributedTest distributedTest = (DistributedTest) persistentSupport
				.getIPersistentDistributedTest().readByOID(DistributedTest.class, distributedTestId);
		if (distributedTest == null)
			throw new FenixServiceException();

		List<Student> studentList = persistentSupport.getIPersistentStudentTestQuestion()
				.readStudentsByDistributedTest(distributedTest.getIdInternal());

		for (Student student : studentList)
			infoStudentList.add(InfoStudentWithInfoPerson.newInfoFromDomain(student));
		return infoStudentList;
	}
}