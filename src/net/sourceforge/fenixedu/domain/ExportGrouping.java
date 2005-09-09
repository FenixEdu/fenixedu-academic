/*
 * Created on 17/Ago/2004
 */
package net.sourceforge.fenixedu.domain;


/**
 * @author joaosa & rmalo
 */

public class ExportGrouping extends ExportGrouping_Base {

    public ExportGrouping() {
    }

    public ExportGrouping(IGrouping groupProperties,
            IExecutionCourse executionCourse) {
        super.setGrouping(groupProperties);
        super.setExecutionCourse(executionCourse);
    }

    public String toString() {
        String result = "[EXPORTGROUPING";
        result += ", groupProperties=" + getGrouping();
        result += ", executionCourse=" + getExecutionCourse();
        result += ", proposalState=" + getProposalState();
        result += "]";
        return result;
    }
    
    public void delete(){
        this.setExecutionCourse(null);
        this.setGrouping(null);
    }
}
