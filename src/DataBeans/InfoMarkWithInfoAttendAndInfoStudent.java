/*
 * Created on 23/Jun/2004
 *
 */
package DataBeans;

import Dominio.IMark;

/**
 * @author Tânia Pousão 23/Jun/2004
 */
public class InfoMarkWithInfoAttendAndInfoStudent extends InfoMark {

    public void copyFromDomain(IMark mark) {
        super.copyFromDomain(mark);
        if (mark != null) {
            setInfoFrequenta(InfoFrequentaWithInfoStudent.newInfoFromDomain(mark.getAttend()));
        }
    }

    public static InfoMark newInfoFromDomain(IMark mark) {
        InfoMarkWithInfoAttendAndInfoStudent infoMark = null;
        if (mark != null) {
            infoMark = new InfoMarkWithInfoAttendAndInfoStudent();
            infoMark.copyFromDomain(mark);
        }

        return infoMark;
    }
}