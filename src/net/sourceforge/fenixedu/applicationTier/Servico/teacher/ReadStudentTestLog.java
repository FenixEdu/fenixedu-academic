/*
 * Created on 10/Set/2003
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudentTestLog;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentTestLogWithStudentAndDistributedTest;
import net.sourceforge.fenixedu.domain.DistributedTest;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentTestLog;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTestLog implements IService {

    public ReadStudentTestLog() {

    }

    public List run(Integer executionCourseId, Integer distributedTestId, Integer studentId)
            throws FenixServiceException {
        ISuportePersistente persistentSuport;
        List infoStudentTestLogList = new ArrayList();
        try {
            persistentSuport = SuportePersistenteOJB.getInstance();
            IStudent student = (IStudent) persistentSuport.getIPersistentStudent().readByOID(
                    Student.class, studentId);
            if (student == null) {
                throw new FenixServiceException();
            }
            IDistributedTest distributedTest = (IDistributedTest) persistentSuport
                    .getIPersistentDistributedTest().readByOID(DistributedTest.class, distributedTestId);
            if (distributedTest == null) {
                throw new FenixServiceException();
            }
            List studentTestLogList = persistentSuport.getIPersistentStudentTestLog()
                    .readByStudentAndDistributedTest(student, distributedTest);
            Iterator it = studentTestLogList.iterator();
            while (it.hasNext()) {
                IStudentTestLog studentTestLog = (IStudentTestLog) it.next();
                InfoStudentTestLog infoStudentTestLog = InfoStudentTestLogWithStudentAndDistributedTest
                        .newInfoFromDomain(studentTestLog);
                String[] eventTokens = infoStudentTestLog.getEvent().split(";");
                List eventList = new ArrayList();
                for (int i = 0; i < eventTokens.length; i++)
                    eventList.add(eventTokens[i]);
                infoStudentTestLog.setEventList(eventList);
                infoStudentTestLogList.add(infoStudentTestLog);
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return infoStudentTestLogList;
    }
}