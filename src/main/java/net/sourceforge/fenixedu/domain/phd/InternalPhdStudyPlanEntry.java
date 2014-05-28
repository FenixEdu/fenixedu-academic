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
package net.sourceforge.fenixedu.domain.phd;

import java.text.MessageFormat;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class InternalPhdStudyPlanEntry extends InternalPhdStudyPlanEntry_Base {

    protected InternalPhdStudyPlanEntry() {
        super();
    }

    public InternalPhdStudyPlanEntry(PhdStudyPlanEntryType type, PhdStudyPlan studyPlan, CompetenceCourse competenceCourse) {
        this();
        init(type, studyPlan, competenceCourse);
    }

    protected void init(PhdStudyPlanEntryType type, PhdStudyPlan studyPlan, CompetenceCourse competenceCourse) {

        String[] args = {};
        if (competenceCourse == null) {
            throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.enclosing_type.competenceCourse.cannot.be.null",
                    args);
        }

        super.setCompetenceCourse(competenceCourse);

        super.init(type, studyPlan);

    }

    @Override
    public void setCompetenceCourse(CompetenceCourse competenceCourse) {
        throw new DomainException(
                "error.net.sourceforge.fenixedu.domain.phd.InternalPhdStudyPlanEntry.cannot.modify.competenceCourse");
    }

    @Override
    public String getCourseDescription() {
        return MessageFormat.format("{0} ({1}) - {2} ects", getCompetenceCourse().getName(), getCompetenceCourse()
                .getDepartmentUnit().getName(), getCompetenceCourse().getEctsCredits());
    }

    @Override
    public boolean isInternalEntry() {
        return true;
    }

    @Override
    public boolean isSimilar(PhdStudyPlanEntry entry) {
        if (entry.isInternalEntry()) {
            return ((InternalPhdStudyPlanEntry) entry).getCompetenceCourse() == getCompetenceCourse();
        }

        return false;
    }

    @Override
    public void delete() {
        super.setCompetenceCourse(null);

        super.delete();
    }

    @Deprecated
    public boolean hasCompetenceCourse() {
        return getCompetenceCourse() != null;
    }

}
