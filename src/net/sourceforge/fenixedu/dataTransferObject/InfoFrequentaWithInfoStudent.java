/*
 * Created on 23/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IAttends;

/**
 * @author Tânia Pousão 23/Jun/2004
 */
public class InfoFrequentaWithInfoStudent extends InfoFrequenta {
    public void copyFromDomain(IAttends frequenta) {
        super.copyFromDomain(frequenta);
        if (frequenta != null) {
            setAluno(InfoStudent.newInfoFromDomain(frequenta.getAluno()));
        }
    }

    public static InfoFrequenta newInfoFromDomain(IAttends attend) {
        InfoFrequentaWithInfoStudent infoAttend = null;
        if (attend != null) {
            infoAttend = new InfoFrequentaWithInfoStudent();
            infoAttend.copyFromDomain(attend);
        }

        return infoAttend;
    }
}