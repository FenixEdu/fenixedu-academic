package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ISchoolClass;

/**
 * @author Fernanda Quitério Created on 9/Ago/2004
 *  
 */
public class InfoClassWithAll extends InfoClassWithInfoExecutionDegree {

    public void copyFromDomain(ISchoolClass turma) {
        super.copyFromDomain(turma);
        if (turma != null) {
            setInfoExecutionPeriod(InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(turma
                    .getExecutionPeriod()));
        }
    }

    public static InfoClass newInfoFromDomain(ISchoolClass turma) {
        InfoClassWithAll infoClass = null;
        if (turma != null) {
            infoClass = new InfoClassWithAll();
            infoClass.copyFromDomain(turma);
        }
        return infoClass;
    }

}