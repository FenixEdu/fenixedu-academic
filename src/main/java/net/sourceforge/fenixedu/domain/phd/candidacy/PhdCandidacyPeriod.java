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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public abstract class PhdCandidacyPeriod extends PhdCandidacyPeriod_Base {

    protected PhdCandidacyPeriod() {
        super();
    }

    @Override
    protected void init(ExecutionInterval executionInterval, DateTime start, DateTime end) {
        throw new DomainException("call init(ExecutionYear, DateTime, DateTime)");
    }

    protected void init(final ExecutionYear executionInterval, final DateTime start, final DateTime end,
            PhdCandidacyPeriodType type) {
        if (type == null) {
            throw new DomainException("error.PhdCandidacyPeriod.type.is.required");
        }

        setType(type);

        super.init(executionInterval, start, end);
    }

    @Override
    public ExecutionYear getExecutionInterval() {
        return (ExecutionYear) super.getExecutionInterval();
    }

    @Override
    public boolean isPhdCandidacyPeriod() {
        return true;
    }

    public static List<PhdCandidacyPeriod> readPhdCandidacyPeriods() {
        List<PhdCandidacyPeriod> phdCandidacyPeriods = new ArrayList<PhdCandidacyPeriod>();

        for (CandidacyPeriod candidacyPeriod : Bennu.getInstance().getCandidacyPeriodsSet()) {
            if (candidacyPeriod.isPhdCandidacyPeriod()) {
                phdCandidacyPeriods.add((PhdCandidacyPeriod) candidacyPeriod);
            }
        }

        return phdCandidacyPeriods;
    }

    public static List<PhdCandidacyPeriod> readOrderedPhdCandidacyPeriods() {
        List<PhdCandidacyPeriod> phdCandidacyPeriods = readPhdCandidacyPeriods();

        Collections.sort(phdCandidacyPeriods, Collections.reverseOrder(CandidacyPeriod.LAST_CANDIDACY_PERIOD));

        return phdCandidacyPeriods;
    }

    protected void checkOverlapingDates(DateTime start, DateTime end, PhdCandidacyPeriodType type) {
        for (final CandidacyPeriod period : Bennu.getInstance().getCandidacyPeriodsSet()) {

            if (!period.isPhdCandidacyPeriod()) {
                continue;
            }

            PhdCandidacyPeriod phdCandidacyPeriod = (PhdCandidacyPeriod) period;

            if (!period.equals(this) && type.equals(phdCandidacyPeriod.getType()) && period.intercept(start, end)) {
                throw new DomainException("error.InstitutionPhdCandidacyPeriod.already.contains.candidacyPeriod.in.given.dates");
            }
        }
    }

    @Atomic
    @Override
    public void edit(final DateTime start, final DateTime end) {
        checkOverlapingDates(start, end, getType());
        super.edit(start, end);
    }

    public abstract String getEmailMessageBodyForRefereeForm(final PhdCandidacyReferee referee);

    public abstract MultiLanguageString getEmailMessageSubjectForMissingCandidacyValidation(
            final PhdIndividualProgramProcess process);

    public abstract MultiLanguageString getEmailMessageBodyForMissingCandidacyValidation(final PhdIndividualProgramProcess process);

    @Override
    public String getPresentationName() {
        return getType().getLocalizedName() + " - " + getExecutionInterval().getName() + " - " + super.getPresentationName();
    }

    public String getStartDateDescription() {
        return getStart().toString("dd/MM/yyyy");
    }

    public String getEndDateDescription() {

        if (getEnd() == null) {
            return "";
        }

        return getEnd().toString("dd/MM/yyyy");
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess> getPhdProgramCandidacyProcesses() {
        return getPhdProgramCandidacyProcessesSet();
    }

    @Deprecated
    public boolean hasAnyPhdProgramCandidacyProcesses() {
        return !getPhdProgramCandidacyProcessesSet().isEmpty();
    }

    @Deprecated
    public boolean hasType() {
        return getType() != null;
    }

}
