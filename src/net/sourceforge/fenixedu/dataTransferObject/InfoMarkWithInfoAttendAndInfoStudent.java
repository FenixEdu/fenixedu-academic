/*
 * Created on 23/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IMark;

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