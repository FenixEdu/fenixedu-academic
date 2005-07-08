package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.fileSuport.INode;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota ciapl Dominio
 * 
 */
public class ExecutionYear extends ExecutionYear_Base {

    public String toString() {
        String result = "[EXECUTION_YEAR";
        result += ", internalCode=" + getIdInternal();
        result += ", year=" + getYear();
        result += ", begin=" + getBeginDate();
        result += ", end=" + getEndDate();
        result += "]";
        return result;
    }

    public String getSlideName() {
        String result = "/EY" + getIdInternal();
        return result;
    }

    public INode getParentNode() {
        return null;
    }

}
