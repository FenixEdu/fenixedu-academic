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
package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CycleCourseGroup extends CycleCourseGroup_Base {

    protected CycleCourseGroup() {
        super();
    }

    public CycleCourseGroup(final RootCourseGroup parentCourseGroup, final String name, final String nameEn,
            final CycleType cycleType, final ExecutionSemester begin, final ExecutionSemester end) {
        if (cycleType == null) {
            throw new DomainException("error.degreeStructure.CycleCourseGroup.cycle.type.cannot.be.null");
        }
        init(parentCourseGroup, name, nameEn, begin, end);
        setCycleType(cycleType);
    }

    @Override
    public void delete() {
        getSourceAffinitiesSet().clear();
        getDestinationAffinitiesSet().clear();
        super.delete();
    }

    @Override
    public boolean isCycleCourseGroup() {
        return true;
    }

    final public String getGraduateTitle() {
        return getGraduateTitle(ExecutionYear.readCurrentExecutionYear(), I18N.getLocale());
    }

    final public String getGraduateTitle(final ExecutionYear executionYear, final Locale locale) {

        if (getMostRecentCycleCourseGroupInformation(executionYear) != null) {
            return getMostRecentCycleCourseGroupInformation(executionYear).getGraduatedTitle().getContent(locale);
        } else {
            final StringBuilder result = new StringBuilder();

            result.append(getDegreeType().getGraduateTitle(getCycleType(), locale));

            final String degreeFilteredName = getDegree().getFilteredName(executionYear, locale);
            result.append(" ").append(BundleUtil.getString(Bundle.APPLICATION, locale, "label.in"));

            final MultiLanguageString mls = getGraduateTitleSuffix();
            final String suffix = mls == null ? null : mls.getContent(locale);
            if (!StringUtils.isEmpty(suffix) && !degreeFilteredName.contains(suffix.trim())) {
                result.append(" ").append(suffix);
                result.append(" ").append("-");
            }

            result.append(" ").append(degreeFilteredName);

            return result.toString();
        }

    }

    public boolean isFirstCycle() {
        return getCycleType() == CycleType.FIRST_CYCLE;
    }

    public boolean isSecondCycle() {
        return getCycleType() == CycleType.SECOND_CYCLE;
    }

    public boolean isThirdCycle() {
        return getCycleType() == CycleType.THIRD_CYCLE;
    }

    public boolean isSpecializationCycle() {
        return getCycleType() == CycleType.SPECIALIZATION_CYCLE;
    }

    @Override
    public Collection<CycleCourseGroup> getParentCycleCourseGroups() {
        return Collections.singletonList(this);
    }

    public Double getCurrentDefaultEcts() {
        return getDefaultEcts(ExecutionYear.readCurrentExecutionYear());
    }

    public Double getDefaultEcts(final ExecutionYear executionYear) {
        final CreditsLimit creditsLimit =
                (CreditsLimit) getMostRecentActiveCurricularRule(CurricularRuleType.CREDITS_LIMIT, null, executionYear);
        if (creditsLimit != null) {
            return creditsLimit.getMinimumCredits();
        }

        if (getDegreeType().hasExactlyOneCycleType()) {
            if (getDegree().hasEctsCredits()) {
                return getDegree().getEctsCredits();
            }

            return getDegreeType().getDefaultEctsCredits();
        }

        throw new DomainException("error.CycleCourseGroup.cannot.calculate.default.ects.credits");
    }

    public List<CycleCourseGroupInformation> getCycleCourseGroupInformationOrderedByExecutionYear() {
        List<CycleCourseGroupInformation> groupInformationList =
                new ArrayList<CycleCourseGroupInformation>(getCycleCourseGroupInformation());
        Collections.sort(groupInformationList, CycleCourseGroupInformation.COMPARATOR_BY_EXECUTION_YEAR);

        return groupInformationList;
    }

    public CycleCourseGroupInformation getCycleCourseGroupInformationByExecutionYear(final ExecutionYear executionYear) {
        for (CycleCourseGroupInformation cycleInformation : getCycleCourseGroupInformation()) {
            if (cycleInformation.getExecutionYear() == executionYear) {
                return cycleInformation;
            }
        }

        return null;
    }

    public CycleCourseGroupInformation getMostRecentCycleCourseGroupInformation(final ExecutionYear executionYear) {
        CycleCourseGroupInformation mostRecent = null;

        for (CycleCourseGroupInformation cycleInformation : getCycleCourseGroupInformation()) {
            if (cycleInformation.getExecutionYear().isAfter(executionYear)) {
                continue;
            }

            if ((mostRecent == null) || cycleInformation.getExecutionYear().isAfter(mostRecent.getExecutionYear())) {
                mostRecent = cycleInformation;
            }
        }

        return mostRecent;
    }

    @Atomic
    public CycleCourseGroupInformation createCycleCourseGroupInformation(final ExecutionYear executionYear,
            String graduatedTitle, String graduatedTitleEn) {
        if (getCycleCourseGroupInformationByExecutionYear(executionYear) != null) {
            throw new DomainException("cycle.course.group.information.exists.in.execution.year");
        }

        return new CycleCourseGroupInformation(this, executionYear, graduatedTitle, graduatedTitleEn);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroupInformation> getCycleCourseGroupInformation() {
        return getCycleCourseGroupInformationSet();
    }

    @Deprecated
    public boolean hasAnyCycleCourseGroupInformation() {
        return !getCycleCourseGroupInformationSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup> getSourceAffinities() {
        return getSourceAffinitiesSet();
    }

    @Deprecated
    public boolean hasAnySourceAffinities() {
        return !getSourceAffinitiesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup> getDestinationAffinities() {
        return getDestinationAffinitiesSet();
    }

    @Deprecated
    public boolean hasAnyDestinationAffinities() {
        return !getDestinationAffinitiesSet().isEmpty();
    }

    @Deprecated
    public boolean hasGraduateTitleSuffix() {
        return getGraduateTitleSuffix() != null;
    }

    @Deprecated
    public boolean hasCycleType() {
        return getCycleType() != null;
    }

}
