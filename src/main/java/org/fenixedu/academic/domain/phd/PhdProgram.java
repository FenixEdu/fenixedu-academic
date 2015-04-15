/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.phd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.MultiLanguageString;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.commons.i18n.I18N;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

public class PhdProgram extends PhdProgram_Base {

    static public Comparator<PhdProgram> COMPARATOR_BY_NAME = new Comparator<PhdProgram>() {
        @Override
        public int compare(final PhdProgram p1, final PhdProgram p2) {
            int res = p1.getName().compareTo(p2.getName());
            return res != 0 ? res : DomainObjectUtil.COMPARATOR_BY_ID.compare(p1, p2);
        }
    };

    private PhdProgram() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setWhenCreated(new DateTime());
        setCreator(Authenticate.getUser().getUsername());
        new PhdProgramServiceAgreementTemplate(this);
    }

    private PhdProgram(final Degree degree, final MultiLanguageString name, final String acronym) {
        this();

        checkDegree(degree);
        checkAcronym(acronym);

        setDegree(degree);
        setName(name);
        setAcronym(acronym);
    }

    private PhdProgram(final Degree degree, final MultiLanguageString name, final String acronym, final Unit parentProgramUnit) {
        this(degree, name, acronym);
        PhdProgramUnit.create(this, getName(), getWhenCreated().toYearMonthDay(), null, parentProgramUnit);
    }

    private void checkDegree(final Degree degree) {
        String[] args = {};
        if (degree == null) {
            throw new DomainException("error.PhdProgram.invalid.degree", args);
        }
        if (!degree.getDegreeType().isAdvancedSpecializationDiploma()) {
            throw new DomainException("error.PhdProgram.invalid.degree");
        }
    }

    private void checkAcronym(final String acronym) {
        String[] args = {};
        if (acronym == null || acronym.isEmpty()) {
            throw new DomainException("error.PhdProgram.invalid.acronym", args);
        }
        final PhdProgram program = readByAcronym(acronym);
        if (program != null && program != this) {
            throw new DomainException("error.PhdProgram.acronym.already.exists", acronym);
        }
    }

    @Override
    public DegreeType getDegreeType() {
        return null;
    }

    @Override
    public Collection<CycleType> getCycleTypes() {
        return Collections.singletonList(CycleType.THIRD_CYCLE);
    }

    private boolean hasAcronym(final String acronym) {
        return getAcronym() != null && getAcronym().equalsIgnoreCase(acronym);
    }

    public String getPresentationName() {
        return getPresentationName(I18N.getLocale());
    }

    private String getPresentationName(final Locale locale) {
        return getPrefix(locale) + getNameFor(locale);
    }

    private String getNameFor(final Locale locale) {
        return getName().hasContent(locale) ? getName().getContent(locale) : getName().getPreferedContent();
    }

    private String getPrefix(final Locale locale) {
        return BundleUtil.getString(Bundle.PHD, locale, "label.php.program") + " "
                + BundleUtil.getString(Bundle.APPLICATION, "label.in") + " ";
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!getIndividualProgramProcessesSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION,
                    "error.PhdProgram.cannot.delete.has.individual.php.program.processes"));
        }
    }

    @Override
    protected void disconnect() {
        getPhdProgramUnit().delete();
        setDegree(null);
        setServiceAgreementTemplate(null);
        setRootDomainObject(null);
        super.disconnect();
    }

    public Set<Person> getCoordinatorsFor(ExecutionYear executionYear) {
        if (getDegree() == null) {
            return Collections.emptySet();
        }

        final ExecutionDegree executionDegree =
                getDegree().getLastActiveDegreeCurricularPlan().getExecutionDegreeByYear(executionYear);

        final Set<Person> result = new HashSet<Person>();
        if (executionDegree != null) {
            for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
                result.add(coordinator.getPerson());
            }
        }

        return result;
    }

    public Set<Person> getResponsibleCoordinatorsFor(ExecutionYear executionYear) {
        if (getDegree() == null) {
            return new HashSet<Person>();
        }

        final ExecutionDegree executionDegree =
                getDegree().getLastActiveDegreeCurricularPlan().getExecutionDegreeByYear(executionYear);

        final Set<Person> result = new HashSet<Person>();
        if (executionDegree != null) {
            for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
                if (coordinator.isResponsible()) {
                    result.add(coordinator.getPerson());
                }
            }
        }

        return result;
    }

    public boolean isCoordinatorFor(Person person, ExecutionYear executionYear) {
        return getCoordinatorsFor(executionYear).contains(person);
    }

    @Atomic
    static public PhdProgram create(final Degree degree, final MultiLanguageString name, final String acronym) {
        return new PhdProgram(degree, name, acronym);
    }

    @Atomic
    static public PhdProgram create(final Degree degree, final MultiLanguageString name, final String acronym, final Unit parent) {
        return new PhdProgram(degree, name, acronym, parent);
    }

    static public PhdProgram readByAcronym(final String acronym) {
        for (final PhdProgram program : Bennu.getInstance().getPhdProgramsSet()) {
            if (program.hasAcronym(acronym)) {
                return program;
            }
        }
        return null;
    }

    public PhdProgramContextPeriod getMostRecentPeriod() {
        List<PhdProgramContextPeriod> periods = new ArrayList<PhdProgramContextPeriod>();
        periods.addAll(getPhdProgramContextPeriodsSet());

        Collections.sort(periods, Collections.reverseOrder(PhdProgramContextPeriod.COMPARATOR_BY_BEGIN_DATE));

        if (periods.isEmpty()) {
            return null;
        }

        return periods.iterator().next();
    }

    public boolean isActiveNow() {
        return isActive(new DateTime());
    }

    public boolean isActive(DateTime date) {
        for (PhdProgramContextPeriod period : getPhdProgramContextPeriodsSet()) {
            if (period.contains(date)) {
                return true;
            }
        }

        return false;
    }

    public boolean isActive(final ExecutionYear executionYear) {
        PhdProgramContextPeriod mostRecentPeriod = getMostRecentPeriod();

        if (mostRecentPeriod.getEndDate() == null) {
            DateTime beginDate = mostRecentPeriod.getBeginDate();
            return beginDate.isBefore(executionYear.getBeginDateYearMonthDay().toDateMidnight())
                    || executionYear.containsDate(beginDate);
        }

        return mostRecentPeriod.getInterval().overlaps(executionYear.getAcademicInterval());
    }

    public PhdProgramInformation getMostRecentPhdProgramInformation() {
        return getPhdProgramInformationByDate(new LocalDate());
    }

    public PhdProgramInformation getPhdProgramInformationByDate(LocalDate localDate) {
        PhdProgramInformation mostRecent = null;

        for (PhdProgramInformation phdProgramInformation : getPhdProgramInformationsSet()) {
            if (phdProgramInformation.getBeginDate().isAfter(localDate)) {
                continue;
            }

            if (mostRecent == null) {
                mostRecent = phdProgramInformation;
                continue;
            }

            if (phdProgramInformation.getBeginDate().isAfter(mostRecent.getBeginDate())) {
                mostRecent = phdProgramInformation;
            }
        }

        return mostRecent;
    }

    @Atomic
    public PhdProgramContextPeriod create(PhdProgramContextPeriodBean bean) {
        return PhdProgramContextPeriod.create(bean);
    }

}
