package net.sourceforge.fenixedu.applicationTier.Servico.candidacy;

import net.sourceforge.fenixedu.domain.candidacy.FirstTimeCandidacyStage;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.log.FirstTimeCandidacyLog;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class LogFirstTimeCandidacyTimestamp {
    @Atomic
    public static void logTimestamp(StudentCandidacy candidacy, FirstTimeCandidacyStage stage) {
        FirstTimeCandidacyLog log = candidacy.getFirstTimeCandidacyLog();
        if (log == null) {
            log = new FirstTimeCandidacyLog(candidacy);
            candidacy.setFirstTimeCandidacyLog(log);
        }

        log.addEntry(stage, new DateTime());
    }
}
