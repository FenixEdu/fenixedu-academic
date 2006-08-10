/*
 * Created on 10/Set/2003
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestLog;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestLogWithStudentAndDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTestLog extends Service {

	public List run(Integer executionCourseId, Integer distributedTestId, Integer studentId)
			throws FenixServiceException, ExcepcaoPersistencia {
		List<InfoStudentTestLog> infoStudentTestLogList = new ArrayList<InfoStudentTestLog>();
		Registration student = rootDomainObject.readRegistrationByOID(
				studentId);
		if (student == null) {
			throw new FenixServiceException();
		}
		DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);
		if (distributedTest == null) {
			throw new FenixServiceException();
		}
		List<StudentTestLog> studentTestLogList = distributedTest.getStudentTestLogs(student);

		for (StudentTestLog studentTestLog : studentTestLogList) {
			InfoStudentTestLog infoStudentTestLog = InfoStudentTestLogWithStudentAndDistributedTest
					.newInfoFromDomain(studentTestLog);
			String[] eventTokens = infoStudentTestLog.getEvent().split(";");
			List<String> eventList = new ArrayList<String>();
			for (int i = 0; i < eventTokens.length; i++)
				eventList.add(eventTokens[i]);
			infoStudentTestLog.setEventList(eventList);
			infoStudentTestLogList.add(infoStudentTestLog);
		}
		return infoStudentTestLogList;
	}
}