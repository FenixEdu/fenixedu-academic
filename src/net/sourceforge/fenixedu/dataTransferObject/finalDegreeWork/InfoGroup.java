/*
 * Created on 2004/04/15
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;

/**
 * @author Luis Cruz
 *  
 */
public class InfoGroup extends InfoObject {

    private InfoExecutionDegree executionDegree;

    private List groupStudents;

    private List groupProposals;

    public InfoGroup() {
        super();
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoGroup) {
            InfoGroup group = (InfoGroup) obj;

            if (group.getIdInternal() != null && getIdInternal() != null) {
                result = group.getIdInternal().equals(getIdInternal());
            }
        }
        return result;
    }

    public String toString() {
        String result = "[InfoGroup";
        result += ", idInternal=" + getIdInternal();
        result += ", infoExecutionDegree=" + getExecutionDegree();
        result += "]";
        return result;
    }

    /**
     * @return Returns the executionDegree.
     */
    public InfoExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    /**
     * @param executionDegree
     *            The executionDegree to set.
     */
    public void setExecutionDegree(InfoExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
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