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

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.phd.InstitutionPhdCandidacyProcessProperties;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class InstitutionPhdCandidacyPeriod extends InstitutionPhdCandidacyPeriod_Base {

    protected InstitutionPhdCandidacyPeriod() {
        super();
    }

    protected InstitutionPhdCandidacyPeriod(final ExecutionYear executionYear, final DateTime start, final DateTime end,
            final PhdCandidacyPeriodType type) {
        this();
        init(executionYear, start, end, type);
    }

    @Override
    protected void init(ExecutionYear executionYear, DateTime start, DateTime end, PhdCandidacyPeriodType type) {
        checkOverlapingDates(start, end, type);

        if (!PhdCandidacyPeriodType.INSTITUTION.equals(type)) {
            throw new DomainException("error.InstitutionPhdCandidacyPeriod.type.must.be.institution");
        }

        super.init(executionYear, start, end, type);
    }

    @Override
    public boolean isInstitutionCandidacyPeriod() {
        return true;
    }

    @Atomic
    public void addPhdProgramToPeriod(final PhdProgram phdProgram) {
        if (phdProgram == null) {
            throw new DomainException("phd.InstitutionPhdCandidacyPeriod.phdProgram.required");
        }

        super.addPhdPrograms(phdProgram);
    }

    @Atomic
    public void addPhdProgramListToPeriod(final List<PhdProgram> phdProgramList) {
        super.getPhdProgramsSet().addAll(phdProgramList);
    }

    @Atomic
    public void removePhdProgramInPeriod(final PhdProgram phdProgram) {
        if (phdProgram == null) {
            throw new DomainException("phd.InstitutionPhdCandidacyPeriod.phdProgram.required");
        }

        super.removePhdPrograms(phdProgram);
    }

    @Override
    public void addPhdPrograms(PhdProgram phdPrograms) {
        throw new DomainException("call addPhdProgramToPeriod()");
    }

    @Override
    public void removePhdPrograms(PhdProgram phdPrograms) {
        throw new DomainException("call removePhdProgramInPeriod()");
    }

    @Atomic
    public static InstitutionPhdCandidacyPeriod create(final PhdCandidacyPeriodBean phdCandidacyPeriodBean) {
        final ExecutionYear executionYear = phdCandidacyPeriodBean.getExecutionYear();
        final DateTime start = phdCandidacyPeriodBean.getStart();
        final DateTime end = phdCandidacyPeriodBean.getEnd();
        final PhdCandidacyPeriodType type = phdCandidacyPeriodBean.getType();

        return new InstitutionPhdCandidacyPeriod(executionYear, start, end, type);
    }

    public static InstitutionPhdCandidacyPeriod readInstitutionPhdCandidacyPeriodForDate(final DateTime date) {
        for (final CandidacyPeriod period : Bennu.getInstance().getCandidacyPeriodsSet()) {
            if (period.isInstitutionCandidacyPeriod() && period.contains(date)) {
                return (InstitutionPhdCandidacyPeriod) period;
            }
        }

        return null;
    }

    public static boolean isAnyInstitutionPhdCandidacyPeriodActive(final DateTime date) {
        return readInstitutionPhdCandidacyPeriodForDate(date) != null;
    }

    public static boolean isAnyInstitutionPhdCandidacyPeriodActive() {
        return isAnyInstitutionPhdCandidacyPeriodActive(new DateTime());
    }

    static public InstitutionPhdCandidacyPeriod getMostRecentCandidacyPeriod() {
        PhdCandidacyPeriod mostRecentCandidacyPeriod = null;

        for (CandidacyPeriod candidacyPeriod : Bennu.getInstance().getCandidacyPeriodsSet()) {
            if (!candidacyPeriod.isInstitutionCandidacyPeriod()) {
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

        return (InstitutionPhdCandidacyPeriod) mostRecentCandidacyPeriod;
    }

    @Override
    public String getEmailMessageBodyForRefereeForm(final PhdCandidacyReferee referee) {
        return MessageFormat.format(String.format(BundleUtil.getString(Bundle.PHD, "message.phd.institution.email.body.referee"),
                referee.getPhdProgramCandidacyProcess().getPhdProgram().getName().getContent(MultiLanguageString.en),
                InstitutionPhdCandidacyProcessProperties.getPublicCandidacyRefereeFormLink(new Locale("en", "EN")),
                referee.getValue(),
                referee.getPhdProgramCandidacyProcess().getPhdProgram().getName().getContent(MultiLanguageString.pt),
                InstitutionPhdCandidacyProcessProperties.getPublicCandidacyRefereeFormLink(new Locale("pt", "PT")),
                referee.getValue()), Unit.getInstitutionName().getContent());
    }

    public String getRefereeSubmissionFormLinkPt(final PhdCandidacyReferee referee) {
        return String.format("%s?hash=%s&locale=pt_PT",
                InstitutionPhdCandidacyProcessProperties.getPublicCandidacyRefereeFormLink(new Locale("pt", "PT")),
                referee.getValue());
    }

    public String getRefereeSubmissionFormLinkEn(final PhdCandidacyReferee referee) {
        return String.format("%s?hash=%s&locale=en_EN",
                InstitutionPhdCandidacyProcessProperties.getPublicCandidacyRefereeFormLink(new Locale("en", "EN")),
                referee.getValue());
    }

    @Override
    public MultiLanguageString getEmailMessageSubjectForMissingCandidacyValidation(PhdIndividualProgramProcess process) {
        return new MultiLanguageString().with(
                MultiLanguageString.pt,
                MessageFormat.format(BundleUtil.getString(Bundle.PHD, Locale.forLanguageTag("pt"),
                        "message.phd.institution.email.subject.missing.candidacy.validation"), Unit.getInstitutionAcronym()))
                .with(MultiLanguageString.en,
                        MessageFormat.format(BundleUtil.getString(Bundle.PHD, Locale.ENGLISH,
                                "message.phd.institution.email.subject.missing.candidacy.validation"), Unit
                                .getInstitutionAcronym()));
    }

    @Override
    public MultiLanguageString getEmailMessageBodyForMissingCandidacyValidation(PhdIndividualProgramProcess process) {
        final String englishBody =
                MessageFormat.format(String.format(BundleUtil.getString(Bundle.PHD, Locale.ENGLISH,
                        "message.phd.institution.email.body.missing.candidacy.validation"),
                        InstitutionPhdCandidacyProcessProperties.getPublicCandidacyAccessLink(new Locale("en", "EN")), process
                                .getCandidacyProcess().getCandidacyHashCode().getValue()), Unit.getInstitutionAcronym());
        final String portugueseBody =
                MessageFormat.format(String.format(BundleUtil.getString(Bundle.PHD, Locale.forLanguageTag("pt"),
                        "message.phd.institution.email.body.missing.candidacy.validation"),
                        InstitutionPhdCandidacyProcessProperties.getPublicCandidacyAccessLink(new Locale("en", "EN")), process
                                .getCandidacyProcess().getCandidacyHashCode().getValue()), Unit.getInstitutionAcronym());

        return new MultiLanguageString().with(MultiLanguageString.en, englishBody).with(MultiLanguageString.pt, portugueseBody);
    }

    public static InstitutionPhdCandidacyPeriod readNextCandidacyPeriod() {
        List<PhdCandidacyPeriod> readOrderedPhdCandidacyPeriods = readOrderedPhdCandidacyPeriods();

        for (PhdCandidacyPeriod phdCandidacyPeriod : readOrderedPhdCandidacyPeriods) {
            if (!phdCandidacyPeriod.isInstitutionCandidacyPeriod()) {
                continue;
            }

            if (phdCandidacyPeriod.getStart().isAfterNow()) {
                return (InstitutionPhdCandidacyPeriod) phdCandidacyPeriod;
            }
        }

        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdProgram> getPhdPrograms() {
        return getPhdProgramsSet();
    }

    @Deprecated
    public boolean hasAnyPhdPrograms() {
        return !getPhdProgramsSet().isEmpty();
    }

}
