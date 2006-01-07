/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.SchoolClass;

/**
 * @author João Mota
 *  
 */
public class InfoClassWithInfoExecutionDegree extends InfoClass {

    public void copyFromDomain(SchoolClass turma) {
        super.copyFromDomain(turma);
        if (turma != null) {
            setInfoExecutionDegree(InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan
                    .newInfoFromDomain(turma.getExecutionDegree()));
        }
    }

    public static InfoClass newInfoFromDomain(SchoolClass turma) {
        InfoClassWithInfoExecutionDegree infoClass = null;
        if (turma != null) {
            infoClass = new InfoClassWithInfoExecutionDegree();
            infoClass.copyFromDomain(turma);
        }
        return infoClass;
    }
}