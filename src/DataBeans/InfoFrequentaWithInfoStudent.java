/*
 * Created on 23/Jun/2004
 *
 */
package DataBeans;

import Dominio.IFrequenta;

/**
 * @author Tânia Pousão 23/Jun/2004
 */
public class InfoFrequentaWithInfoStudent extends InfoFrequenta {
    public void copyFromDomain(IFrequenta frequenta) {
        super.copyFromDomain(frequenta);
        if (frequenta != null) {
            setAluno(InfoStudent.newInfoFromDomain(frequenta.getAluno()));
        }
    }

    public static InfoFrequenta newInfoFromDomain(IFrequenta attend) {
        InfoFrequentaWithInfoStudent infoAttend = null;
        if (attend != null) {
            infoAttend = new InfoFrequentaWithInfoStudent();
            infoAttend.copyFromDomain(attend);
        }

        return infoAttend;
    }
}