/*
 * Created on Jun 7, 2004
 *  
 */
package DataBeans;

import Dominio.IExecutionDegree;

/**
 * @author João Mota
 *  
 */
public class InfoExecutionDegreeWithInfoExecutionYear extends InfoExecutionDegree {

    public void copyFromDomain(IExecutionDegree executionDegree) {
        super.copyFromDomain(executionDegree);
        if (executionDegree != null) {
            setInfoExecutionYear(InfoExecutionYear.newInfoFromDomain(executionDegree.getExecutionYear()));
        }
    }

    /**
     * @param executionDegree
     * @return
     */
    public static InfoExecutionDegree newInfoFromDomain(IExecutionDegree executionDegree) {
        InfoExecutionDegreeWithInfoExecutionYear infoExecutionDegree = null;
        if (executionDegree != null) {
            infoExecutionDegree = new InfoExecutionDegreeWithInfoExecutionYear();
            infoExecutionDegree.copyFromDomain(executionDegree);
        }
        return infoExecutionDegree;
    }
}