package DataBeans;

import Dominio.ITurma;

/**
 * @author Fernanda Quitério Created on 9/Ago/2004
 *  
 */
public class InfoClassWithAll extends InfoClassWithInfoExecutionDegree {

    public void copyFromDomain(ITurma turma) {
        super.copyFromDomain(turma);
        if (turma != null) {
            setInfoExecutionPeriod(InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(turma
                    .getExecutionPeriod()));
        }
    }

    public static InfoClass newInfoFromDomain(ITurma turma) {
        InfoClassWithAll infoClass = null;
        if (turma != null) {
            infoClass = new InfoClassWithAll();
            infoClass.copyFromDomain(turma);
        }
        return infoClass;
    }

}