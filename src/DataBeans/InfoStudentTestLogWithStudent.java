/*
 * Created on 10/Set/2003
 *
 */
package DataBeans;

import Dominio.IStudentTestLog;

/**
 * @author Susana Fernandes
 */
public class InfoStudentTestLogWithStudent extends InfoStudentTestLog {

    public void copyFromDomain(IStudentTestLog studentTestLog) {
        super.copyFromDomain(studentTestLog);
        if (studentTestLog != null) {
            setInfoStudent(InfoStudent.newInfoFromDomain(studentTestLog.getStudent()));
        }
    }

    public static InfoStudentTestLog newInfoFromDomain(IStudentTestLog studentTestLog) {
        InfoStudentTestLogWithStudent infoStudentTestLog = null;
        if (studentTestLog != null) {
            infoStudentTestLog = new InfoStudentTestLogWithStudent();
            infoStudentTestLog.copyFromDomain(studentTestLog);
        }
        return infoStudentTestLog;
    }

}