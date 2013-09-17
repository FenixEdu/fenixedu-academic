package net.sourceforge.fenixedu.domain.log;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacy.FirstTimeCandidacyStage;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;

import org.joda.time.DateTime;

public class FirstTimeCandidacyLog extends FirstTimeCandidacyLog_Base {

    public FirstTimeCandidacyLog() {
        super();
    }

    public FirstTimeCandidacyLog(StudentCandidacy candidacy) {
        super();
        this.setStudentCandidacy(candidacy);
    }

    public void addEntry(FirstTimeCandidacyStage stage, DateTime timestamp) {
        this.addFirstTimeCandidacyLogEntry(new FirstTimeCandidacyLogEntry(stage, timestamp, this));
    }

    protected RootDomainObject getRootDomainObject() {
        return RootDomainObject.getInstance();
    }
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.log.FirstTimeCandidacyLogEntry> getFirstTimeCandidacyLogEntry() {
        return getFirstTimeCandidacyLogEntrySet();
    }

    @Deprecated
    public boolean hasAnyFirstTimeCandidacyLogEntry() {
        return !getFirstTimeCandidacyLogEntrySet().isEmpty();
    }

    @Deprecated
    public boolean hasStudentCandidacy() {
        return getStudentCandidacy() != null;
    }

}
