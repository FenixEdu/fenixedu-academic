/*
 * Created on Jun 7, 2004
 *  
 */
package DataBeans;

import Dominio.ITurma;

/**
 * @author João Mota
 *  
 */
public class InfoClassWithInfoExecutionDegree extends InfoClass {

    public void copyFromDomain(ITurma turma) {
        super.copyFromDomain(turma);
        if (turma != null) {
            setInfoExecutionDegree(InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan
                    .newInfoFromDomain(turma.getExecutionDegree()));
        }
    }

    public static InfoClass newInfoFromDomain(ITurma turma) {
        InfoClassWithInfoExecutionDegree infoClass = null;
        if (turma != null) {
            infoClass = new InfoClassWithInfoExecutionDegree();
            infoClass.copyFromDomain(turma);
        }
        return infoClass;
    }
}