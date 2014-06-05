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
package net.sourceforge.fenixedu.domain.phd.candidacy;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.phd.EPFLPhdCandidacyProcessProperties;
import net.sourceforge.fenixedu.util.phd.PhdProperties;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class EPFLPhdCandidacyPeriod extends EPFLPhdCandidacyPeriod_Base {

    protected EPFLPhdCandidacyPeriod() {
        super();
    }

    protected EPFLPhdCandidacyPeriod(final ExecutionYear executionYear, final DateTime start, final DateTime end,
            PhdCandidacyPeriodType type) {
        this();

        init(executionYear, start, end, type);
    }

    @Override
    protected void init(final ExecutionYear executionYear, final DateTime start, final DateTime end, PhdCandidacyPeriodType type) {
        checkIfCanCreate(start, end);

        if (!PhdCandidacyPeriodType.EPFL.equals(type)) {
            throw new DomainException("error.EPFLPhdCandidacyPeriod.type.must.be.epfl");
        }

        super.init(executionYear, start, end, type);
    }

    private void checkIfCanCreate(final DateTime start, final DateTime end) {
        for (final CandidacyPeriod period : Bennu.getInstance().getCandidacyPeriodsSet()) {
            if (!period.equals(this) && period.isEpflCandidacyPeriod() && period.intercept(start, end)) {
                throw new DomainException(
                        "error.EPFLInstitutionPhdCandidacyPeriod.already.contains.candidacyPeriod.in.given.dates");
            }
        }
    }

    @Override
    public boolean isEpflCandidacyPeriod() {
        return true;
    }

    @Atomic
    public static EPFLPhdCandidacyPeriod create(final PhdCandidacyPeriodBean phdCandidacyPeriodBean) {
        final ExecutionYear executionYear = phdCandidacyPeriodBean.getExecutionYear();
        final DateTime start = phdCandidacyPeriodBean.getStart();
        final DateTime end = phdCandidacyPeriodBean.getEnd();
        final PhdCandidacyPeriodType type = phdCandidacyPeriodBean.getType();

        return new EPFLPhdCandidacyPeriod(executionYear, start, end, type);
    }

    public static boolean isAnyEPFLPhdCandidacyPeriodActive() {
        return isAnyEPFLPhdCandidacyPeriodActive(new DateTime());
    }

    public static boolean isAnyEPFLPhdCandidacyPeriodActive(final DateTime date) {
        return readEPFLPhdCandidacyPeriodForDateTime(date) != null;
    }

    public static EPFLPhdCandidacyPeriod readEPFLPhdCandidacyPeriodForDateTime(final DateTime date) {
        for (final CandidacyPeriod period : Bennu.getInstance().getCandidacyPeriodsSet()) {
            if (period.isEpflCandidacyPeriod() && period.contains(date)) {
                return (EPFLPhdCandidacyPeriod) period;
            }
        }

        return null;
    }

    static public EPFLPhdCandidacyPeriod getMostRecentCandidacyPeriod() {
        PhdCandidacyPeriod mostRecentCandidacyPeriod = null;

        for (CandidacyPeriod candidacyPeriod : Bennu.getInstance().getCandidacyPeriodsSet()) {
            if (!candidacyPeriod.isEpflCandidacyPeriod()) {
                continue;
            }

            if (candidacyPeriod.getStart().isAfterNow()) {
                continue;
            }

            if (mostRecentCandidacyPeriod == null) {
                mostRecentCandidacyPeriod = (PhdCandidacyPeriod) candidacyPeriod;
                continue;
            }

            if (candidacyPeriod.getStart().isAfter(mostRecentCandidacyPeriod.getStart())) {
                mostRecentCandidacyPeriod = (PhdCandidacyPeriod) candidacyPeriod;
            }
        }

        return (EPFLPhdCandidacyPeriod) mostRecentCandidacyPeriod;
    }

    @Override
    public String getEmailMessageBodyForRefereeForm(final PhdCandidacyReferee referee) {
        return String.format(BundleUtil.getString(Bundle.PHD, "message.phd.epfl.email.body.referee"), referee.getPhdProgramCandidacyProcess()
                .getIndividualProgramProcess().getPhdProgramFocusArea().getName().getContent(), EPFLPhdCandidacyProcessProperties
                .getConfiguration().getPublicCandidacyRefereeFormLink(), referee.getValue(), referee
                .getPhdProgramCandidacyProcess().getPublicPhdCandidacyPeriod().getEnd().toString("yyyy-MM-dd HH:mm"));
    }

    @Override
    public MultiLanguageString getEmailMessageSubjectForMissingCandidacyValidation(PhdIndividualProgramProcess process) {
        return new MultiLanguageString().with(MultiLanguageString.en,
                BundleUtil.getString(Bundle.PHD, "message.phd.epfl.email.subject.missing.candidacy.validation"));
    }

    @Override
    public MultiLanguageString getEmailMessageBodyForMissingCandidacyValidation(PhdIndividualProgramProcess process) {
        final String body =
                String.format(BundleUtil.getString(Bundle.PHD, "message.phd.epfl.email.body.missing.candidacy.validation"),
                        PhdProperties.getPublicCandidacyAccessLink(), process.getCandidacyProcess().getCandidacyHashCode()
                                .getValue());
        return new MultiLanguageString().with(MultiLanguageString.en, body);
    }

}
