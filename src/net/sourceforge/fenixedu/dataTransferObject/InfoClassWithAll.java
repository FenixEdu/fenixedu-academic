package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.SchoolClass;

/**
 * @author Fernanda Quitério Created on 9/Ago/2004
 *  
 */
public class InfoClassWithAll extends InfoClassWithInfoExecutionDegree {

    public void copyFromDomain(SchoolClass turma) {
        super.copyFromDomain(turma);
        if (turma != null) {
            setInfoExecutionPeriod(InfoExecutionPeriod.newInfoFromDomain(turma
                    .getExecutionPeriod()));
        }
    }

    public static InfoClass newInfoFromDomain(SchoolClass turma) {
        InfoClassWithAll infoClass = null;
        if (turma != null) {
            infoClass = new InfoClassWithAll();
            infoClass.copyFromDomain(turma);
        }
        return infoClass;
    }

}