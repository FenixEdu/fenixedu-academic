/*
 * Created on Jun 7, 2004
 *  
 */
package DataBeans;

import Dominio.ISchoolClass;

/**
 * @author João Mota
 *  
 */
public class InfoClassWithInfoExecutionDegree extends InfoClass {

    public void copyFromDomain(ISchoolClass turma) {
        super.copyFromDomain(turma);
        if (turma != null) {
            setInfoExecutionDegree(InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan
                    .newInfoFromDomain(turma.getExecutionDegree()));
        }
    }

    public static InfoClass newInfoFromDomain(ISchoolClass turma) {
        InfoClassWithInfoExecutionDegree infoClass = null;
        if (turma != null) {
            infoClass = new InfoClassWithInfoExecutionDegree();
            infoClass.copyFromDomain(turma);
        }
        return infoClass;
    }
}