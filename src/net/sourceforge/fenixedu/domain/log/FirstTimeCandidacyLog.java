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

    @Override
    protected RootDomainObject getRootDomainObject() {
        return RootDomainObject.getInstance();
    }
}
