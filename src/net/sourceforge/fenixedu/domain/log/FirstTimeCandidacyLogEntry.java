package net.sourceforge.fenixedu.domain.log;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacy.FirstTimeCandidacyStage;

import org.joda.time.DateTime;

public class FirstTimeCandidacyLogEntry extends FirstTimeCandidacyLogEntry_Base {
    
    public  FirstTimeCandidacyLogEntry() {
        super();
    }

    public FirstTimeCandidacyLogEntry(FirstTimeCandidacyStage stage, DateTime timestamp, FirstTimeCandidacyLog log) {
	this.setStage(stage);
	this.setEntryDate(timestamp);
	this.setFirstTimeCandidacyLog(log);
    }

    @Override
    protected RootDomainObject getRootDomainObject() {
	return RootDomainObject.getInstance();
    }
}
