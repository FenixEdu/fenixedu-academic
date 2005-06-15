/*
 * Created on 17/Ago/2004
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author joaosa & rmalo
 */

public class GroupPropertiesExecutionCourse extends GroupPropertiesExecutionCourse_Base {

    /**
     * Construtor
     */
    public GroupPropertiesExecutionCourse() {
    }

    /**
     * Construtor
     */
    public GroupPropertiesExecutionCourse(IGroupProperties groupProperties,
            IExecutionCourse executionCourse) {
        super.setGroupProperties(groupProperties);
        super.setExecutionCourse(executionCourse);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[GROUPPROPERTIESEXECUTIONCOURSE";
        result += ", groupProperties=" + getGroupProperties();
        result += ", executionCourse=" + getExecutionCourse();
        result += ", proposalState=" + getProposalState();
        result += "]";
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof IGroupPropertiesExecutionCourse) {
            result = (getGroupProperties().equals(((IGroupPropertiesExecutionCourse) arg0)
                    .getGroupProperties()))
                    && (getExecutionCourse().equals(((IGroupPropertiesExecutionCourse) arg0)
                            .getExecutionCourse()));
        }
        return result;
    }

}
