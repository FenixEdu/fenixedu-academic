/*
 * Created on Apr 15, 2004
 *
 */
package Dominio.finalDegreeWork;

import java.util.List;

import Dominio.DomainObject;
import Dominio.ICursoExecucao;

/**
 * @author Luis Cruz
 *  
 */
public class Group extends DomainObject implements IGroup {

    private Integer keyExecutionDegree;

    private ICursoExecucao executionDegree;

    private List groupStudents;

    private List groupProposals;

    /* Construtores */
    public Group() {
        super();
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IGroup) {
            IGroup group = (IGroup) obj;

            if (group.getIdInternal() != null && getIdInternal() != null) {
                result = group.getIdInternal().equals(getIdInternal());
            }
        }
        return result;
    }

    public String toString() {
        String result = "[Group";
        result += ", idInternal=" + getIdInternal();
        result += ", executionDegree=" + getExecutionDegree();
        result += "]";
        return result;
    }

    /**
     * @return Returns the executionDegree.
     */
    public ICursoExecucao getExecutionDegree() {
        return executionDegree;
    }

    /**
     * @param executionDegree
     *            The executionDegree to set.
     */
    public void setExecutionDegree(ICursoExecucao executionDegree) {
        this.executionDegree = executionDegree;
    }

    /**
     * @return Returns the keyExecutionDegree.
     */
    public Integer getKeyExecutionDegree() {
        return keyExecutionDegree;
    }

    /**
     * @param keyExecutionDegree
     *            The keyExecutionDegree to set.
     */
    public void setKeyExecutionDegree(Integer keyExecutionDegree) {
        this.keyExecutionDegree = keyExecutionDegree;
    }

    /**
     * @return Returns the groupProposals.
     */
    public List getGroupProposals() {
        return groupProposals;
    }

    /**
     * @param groupProposals
     *            The groupProposals to set.
     */
    public void setGroupProposals(List groupProposals) {
        this.groupProposals = groupProposals;
    }

    /**
     * @return Returns the groupStudents.
     */
    public List getGroupStudents() {
        return groupStudents;
    }

    /**
     * @param groupStudents
     *            The groupStudents to set.
     */
    public void setGroupStudents(List groupStudents) {
        this.groupStudents = groupStudents;
    }
}