package net.sourceforge.fenixedu.domain.log;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

public class FirstYearShiftsCapacityToggleLog extends FirstYearShiftsCapacityToggleLog_Base {

    public FirstYearShiftsCapacityToggleLog(ExecutionSemester executionSemester, User creator) {
        super();
        setExecutionSemester(executionSemester);
        setCreator(creator);
        setCreationDate(new DateTime());
    }
    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCreator() {
        return getCreator() != null;
    }

    @Deprecated
    public boolean hasCreationDate() {
        return getCreationDate() != null;
    }

    @Deprecated
    public boolean hasExecutionSemester() {
        return getExecutionSemester() != null;
    }

}
