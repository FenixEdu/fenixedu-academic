/*
 * Created on 10/Set/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;

/**
 * @author Susana Fernandes
 */
public class InfoStudentTestLogWithStudentAndDistributedTest extends InfoStudentTestLogWithStudent {

    public void copyFromDomain(StudentTestLog studentTestLog) {
        super.copyFromDomain(studentTestLog);
        if (studentTestLog != null) {
            setInfoDistributedTest(InfoDistributedTest.newInfoFromDomain(studentTestLog.getDistributedTest()));
        }
    }

    public static InfoStudentTestLog newInfoFromDomain(StudentTestLog studentTestLog) {
        InfoStudentTestLogWithStudentAndDistributedTest infoStudentTestLog = null;
        if (studentTestLog != null) {
            infoStudentTestLog = new InfoStudentTestLogWithStudentAndDistributedTest();
            infoStudentTestLog.copyFromDomain(studentTestLog);
        }
        return infoStudentTestLog;
    }

}