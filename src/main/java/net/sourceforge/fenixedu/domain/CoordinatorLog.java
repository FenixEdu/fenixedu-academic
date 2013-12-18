package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class CoordinatorLog extends CoordinatorLog_Base {

    public CoordinatorLog() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public CoordinatorLog(DateTime time, OperationType operation, Person person, ExecutionDegree executionDegree, Person personWho) {
        super();
        setRootDomainObject(Bennu.getInstance());
        super.setDate(time);
        super.setOperation(operation);
        super.setPerson(person);
        super.setPersonWho(personWho);
        super.setExecutionDegree(executionDegree);
    }

    @Atomic
    public static CoordinatorLog createCoordinatorLog(DateTime time, OperationType operation, Person personActing,
            Coordinator coordinator) {

        return new CoordinatorLog(time, operation, coordinator.getPerson(), coordinator.getExecutionDegree(), personActing);
    }

    @Deprecated
    public boolean hasOperation() {
        return getOperation() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDate() {
        return getDate() != null;
    }

    @Deprecated
    public boolean hasPersonWho() {
        return getPersonWho() != null;
    }

    @Deprecated
    public boolean hasExecutionDegree() {
        return getExecutionDegree() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
