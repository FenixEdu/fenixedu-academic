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

    public static InfoClass copyFromDomain(ITurma turma) {
        InfoClass infoClass = InfoClass.copyFromDomain(turma);
        if (infoClass != null) {
            infoClass.setInfoExecutionDegree(InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan
                    .copyFromDomain(turma.getExecutionDegree()));
        }
        return infoClass;
    }

}