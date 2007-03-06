/*
 * Created on 23/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Attends;

/**
 * @author Tânia Pousão 23/Jun/2004
 */
public class InfoFrequentaWithInfoStudent extends InfoFrequenta {
    public void copyFromDomain(Attends frequenta) {
        super.copyFromDomain(frequenta);
        if (frequenta != null) {
            setAluno(InfoStudent.newInfoFromDomain(frequenta.getRegistration()));
        }
    }

    public static InfoFrequenta newInfoFromDomain(Attends attend) {
        InfoFrequentaWithInfoStudent infoAttend = null;
        if (attend != null) {
            infoAttend = new InfoFrequentaWithInfoStudent();
            infoAttend.copyFromDomain(attend);
        }

        return infoAttend;
    }
}