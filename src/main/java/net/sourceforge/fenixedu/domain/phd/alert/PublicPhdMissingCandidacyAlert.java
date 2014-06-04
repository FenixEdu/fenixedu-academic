/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.phd.alert;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PublicPhdMissingCandidacyAlert extends PublicPhdMissingCandidacyAlert_Base {

    static private final int INTERVAL = 15; // number of days

    private PublicPhdMissingCandidacyAlert() {
        super();
    }

    public PublicPhdMissingCandidacyAlert(final PhdProgramPublicCandidacyHashCode candidacyHashCode) {
        this();
        String[] args = {};
        if (candidacyHashCode == null) {
            throw new DomainException("error.PublicPhdMissingCandidacyAlert.invalid.candidacy.hash.code", args);
        }
        init(generateSubject(candidacyHashCode), generateBody(candidacyHashCode));
        setCandidacyHashCode(candidacyHashCode);
    }

    private MultiLanguageString generateSubject(final PhdProgramPublicCandidacyHashCode candidacyHashCode) {
        // TODO: if collaboration type change, then message must be different
        return new MultiLanguageString().with(MultiLanguageString.en, BundleUtil.getString(Bundle.PHD, "message.phd.email.subject.missing.candidacy"));
    }

    private MultiLanguageString generateBody(final PhdProgramPublicCandidacyHashCode hashCode) {
        // TODO: if collaboration type change, then message must be different
        String submissionAccessURL = FenixConfigurationManager.getConfiguration().getPhdPublicCandidacySubmissionLink();
        final String body =
                String.format(BundleUtil.getString(Bundle.PHD, "message.phd.email.body.missing.candidacy"), submissionAccessURL,
                        hashCode.getValue());
        return new MultiLanguageString().with(MultiLanguageString.en, body);
    }

    @Override
    protected boolean isToFire() {
        int days = Days.daysBetween(calculateStartDate().toDateMidnight(), new LocalDate().toDateMidnight()).getDays();
        return days >= INTERVAL;
    }

    private LocalDate calculateStartDate() {
        return hasFireDate() ? getFireDate().toLocalDate() : getCandidacyHashCode().getWhenCreated().toLocalDate();
    }

    @Override
    protected boolean isToDiscard() {
        return getCandidacyHashCode().hasCandidacyProcess() || candidacyPeriodIsOver();
    }

    /*
     * Must exist a candidacy period, otherwise candidacy hash code could not be
     * previously created
     */
    private boolean candidacyPeriodIsOver() {
        return new DateTime().isAfter(getCandidacyHashCode().getPhdProgramCandidacyProcess().getPublicPhdCandidacyPeriod()
                .getEnd());
    }

    @Override
    protected void generateMessage() {
        new Message(getSender(), null, Collections.<Recipient> emptyList(), buildMailSubject(), buildMailBody(), getEmail());
    }

    private Set<String> getEmail() {
        return Collections.singleton(getCandidacyHashCode().getEmail());
    }

    @Override
    public String getDescription() {
        return BundleUtil.getString(Bundle.PHD, String.format("message.phd.missing.candidacy.alert", INTERVAL));
    }

    @Override
    public boolean isToSendMail() {
        return true;
    }

    @Override
    public boolean isSystemAlert() {
        return true;
    }

    @Override
    protected void disconnect() {
        setCandidacyHashCode(null);
        super.disconnect();
    }

    @Deprecated
    public boolean hasCandidacyHashCode() {
        return getCandidacyHashCode() != null;
    }

}
