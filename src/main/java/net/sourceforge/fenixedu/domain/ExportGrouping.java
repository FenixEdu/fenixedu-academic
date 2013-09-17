/*
 * Created on 17/Ago/2004
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author joaosa & rmalo
 */

public class ExportGrouping extends ExportGrouping_Base {

    public ExportGrouping() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public ExportGrouping(Grouping groupProperties, ExecutionCourse executionCourse) {
        this();
        super.setGrouping(groupProperties);
        super.setExecutionCourse(executionCourse);
    }

    public void delete() {
        setExecutionCourse(null);
        setGrouping(null);
        setReceiverPerson(null);
        setSenderExecutionCourse(null);
        setSenderPerson(null);
        setRootDomainObject(null);

        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasSenderPerson() {
        return getSenderPerson() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSenderExecutionCourse() {
        return getSenderExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasProposalState() {
        return getProposalState() != null;
    }

    @Deprecated
    public boolean hasReceiverPerson() {
        return getReceiverPerson() != null;
    }

    @Deprecated
    public boolean hasGrouping() {
        return getGrouping() != null;
    }

}
