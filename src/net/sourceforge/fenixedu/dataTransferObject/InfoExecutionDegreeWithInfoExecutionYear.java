/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ExecutionDegree;

/**
 * @author João Mota
 *  
 */
public class InfoExecutionDegreeWithInfoExecutionYear extends InfoExecutionDegree {

    public void copyFromDomain(ExecutionDegree executionDegree) {
        super.copyFromDomain(executionDegree);
        if (executionDegree != null) {
            setInfoExecutionYear(InfoExecutionYear.newInfoFromDomain(executionDegree.getExecutionYear()));
        }
    }

    /**
     * @param executionDegree
     * @return
     */
    public static InfoExecutionDegree newInfoFromDomain(ExecutionDegree executionDegree) {
        InfoExecutionDegreeWithInfoExecutionYear infoExecutionDegree = null;
        if (executionDegree != null) {
            infoExecutionDegree = new InfoExecutionDegreeWithInfoExecutionYear();
            infoExecutionDegree.copyFromDomain(executionDegree);
        }
        return infoExecutionDegree;
    }
}