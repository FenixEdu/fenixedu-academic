/*
 * Created on 7/Jul/2004
 *
 */
package DataBeans;

import Dominio.IStudentGroupAttend;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoStudentGroupAttendWithAllUntilPersons extends InfoStudentGroupAttend {

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.InfoStudentGroupAttend#copyFromDomain(Dominio.IStudentGroupAttend)
     */
    public void copyFromDomain(IStudentGroupAttend studentGroupAttend) {
        super.copyFromDomain(studentGroupAttend);
        if (studentGroupAttend != null) {
            setInfoAttend(InfoFrequentaWithInfoStudentAndPerson.newInfoFromDomain(studentGroupAttend
                    .getAttend()));
        }
    }

    public static InfoStudentGroupAttend newInfoFromDomain(IStudentGroupAttend studentGroupAttend) {
        InfoStudentGroupAttendWithAllUntilPersons infoStudentGroupAttend = null;
        if (studentGroupAttend != null) {
            infoStudentGroupAttend = new InfoStudentGroupAttendWithAllUntilPersons();
            infoStudentGroupAttend.copyFromDomain(studentGroupAttend);
        }
        return infoStudentGroupAttend;
    }
}