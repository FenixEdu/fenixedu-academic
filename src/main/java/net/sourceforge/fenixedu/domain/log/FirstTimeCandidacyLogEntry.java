package net.sourceforge.fenixedu.domain.log;

import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.candidacy.FirstTimeCandidacyStage;

import org.joda.time.DateTime;

public class FirstTimeCandidacyLogEntry extends FirstTimeCandidacyLogEntry_Base {

    public FirstTimeCandidacyLogEntry() {
        super();
    }

    public FirstTimeCandidacyLogEntry(FirstTimeCandidacyStage stage, DateTime timestamp, FirstTimeCandidacyLog log) {
        this.setStage(stage);
        this.setEntryDate(timestamp);
        this.setFirstTimeCandidacyLog(log);
    }

    protected Bennu getRootDomainObject() {
        return Bennu.getInstance();
    }
    @Deprecated
    public boolean hasFirstTimeCandidacyLog() {
        return getFirstTimeCandidacyLog() != null;
    }

    @Deprecated
    public boolean hasEntryDate() {
        return getEntryDate() != null;
    }

    @Deprecated
    public boolean hasStage() {
        return getStage() != null;
    }

}
