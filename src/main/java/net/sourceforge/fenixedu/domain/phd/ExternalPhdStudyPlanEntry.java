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

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ExternalPhdStudyPlanEntry extends ExternalPhdStudyPlanEntry_Base {

    protected ExternalPhdStudyPlanEntry() {
        super();
    }

    public ExternalPhdStudyPlanEntry(PhdStudyPlanEntryType type, PhdStudyPlan studyPlan, String courseName) {
        this();
        init(type, studyPlan, courseName);
    }

    protected void init(PhdStudyPlanEntryType type, PhdStudyPlan studyPlan, String courseName) {

        String[] args = {};
        if (courseName == null || courseName.isEmpty()) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.phd.ExternalPhdStudyPlanEntry.courseName.cannot.be.null", args);
        }

        super.setCourseName(courseName);

        super.init(type, studyPlan);

    }

    @Override
    public void setCourseName(String courseName) {
        throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.ExternalPhdStudyPlanEntry.cannot.modify.courseName");
    }

    @Override
    public boolean isExternalEntry() {
        return true;
    }

    @Override
    public boolean isSimilar(PhdStudyPlanEntry entry) {
        if (entry.isExternalEntry()) {
            final ExternalPhdStudyPlanEntry externalPhdStudyPlanEntry = (ExternalPhdStudyPlanEntry) entry;
            return getCourseName().equals(externalPhdStudyPlanEntry.getCourseName());
        }

        return false;

    }

    @Override
    public String getCourseDescription() {
        return getCourseName();
    }

    @Deprecated
    public boolean hasCourseName() {
        return getCourseName() != null;
    }

}
