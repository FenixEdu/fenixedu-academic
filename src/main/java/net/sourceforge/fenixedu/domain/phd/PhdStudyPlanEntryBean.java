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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Degree;

public class PhdStudyPlanEntryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Degree degree;

    private List<CompetenceCourse> competenceCourses;

    private String courseName;

    private PhdStudyPlanEntryType entryType;

    private PhdStudyPlan studyPlan;

    private Boolean internalEntry;

    public PhdStudyPlanEntryBean(PhdStudyPlan studyPlan) {
        setStudyPlan(studyPlan);
        setInternalEntry(true);
        setEntryType(PhdStudyPlanEntryType.NORMAL);
        setCompetenceCourses(new ArrayList<CompetenceCourse>());
        setDegree(!studyPlan.getDegree().isEmpty() ? studyPlan.getDegree() : null);

    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public PhdStudyPlanEntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(PhdStudyPlanEntryType entryType) {
        this.entryType = entryType;
    }

    public Boolean getInternalEntry() {
        return internalEntry;
    }

    public void setInternalEntry(Boolean internalEntry) {
        this.internalEntry = internalEntry;
    }

    public PhdStudyPlan getStudyPlan() {
        return this.studyPlan;
    }

    public void setStudyPlan(PhdStudyPlan studyPlan) {
        this.studyPlan = studyPlan;
    }

    public Degree getDegree() {
        return this.degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public List<CompetenceCourse> getCompetenceCourses() {
        final List<CompetenceCourse> result = new ArrayList<CompetenceCourse>();
        for (final CompetenceCourse each : this.competenceCourses) {
            result.add(each);
        }

        return result;
    }

    public void setCompetenceCourses(List<CompetenceCourse> competenceCourses) {
        final List<CompetenceCourse> result = new ArrayList<CompetenceCourse>();
        for (final CompetenceCourse each : competenceCourses) {
            result.add(each);
        }

        this.competenceCourses = result;
    }

}
