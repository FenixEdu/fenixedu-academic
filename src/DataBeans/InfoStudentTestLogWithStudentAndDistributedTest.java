/*
 * Created on 10/Set/2003
 *
 */
package DataBeans;

import Dominio.IStudentTestLog;

/**
 * @author Susana Fernandes
 */
public class InfoStudentTestLogWithStudentAndDistributedTest extends InfoStudentTestLogWithStudent {

    public void copyFromDomain(IStudentTestLog studentTestLog) {
        super.copyFromDomain(studentTestLog);
        if (studentTestLog != null) {
            setInfoDistributedTest(InfoDistributedTest.newInfoFromDomain(studentTestLog
                    .getDistributedTest()));
        }
    }

    public static InfoStudentTestLog newInfoFromDomain(IStudentTestLog studentTestLog) {
        InfoStudentTestLogWithStudentAndDistributedTest infoStudentTestLog = null;
        if (studentTestLog != null) {
            infoStudentTestLog = new InfoStudentTestLogWithStudentAndDistributedTest();
            infoStudentTestLog.copyFromDomain(studentTestLog);
        }
        return infoStudentTestLog;
    }

}