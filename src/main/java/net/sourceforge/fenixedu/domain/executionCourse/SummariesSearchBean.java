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
package net.sourceforge.fenixedu.domain.executionCourse;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Summary;

public class SummariesSearchBean implements Serializable {

    private final ExecutionCourse executionCourseDomainReference;

    private ShiftType shiftType;
    private Shift shiftDomainReference;
    private Professorship professorshipDomainReference;
    private Boolean showOtherProfessors;
    private Boolean ascendant = false;

    public SummariesSearchBean(final ExecutionCourse executionCourse) {
        this.executionCourseDomainReference = executionCourse;
    }

    public ExecutionCourse getExecutionCourse() {
        return executionCourseDomainReference;
    }

    public Professorship getProfessorship() {
        return professorshipDomainReference;
    }

    public void setProfessorship(final Professorship professorship) {
        professorshipDomainReference = professorship;
    }

    public Shift getShift() {
        return shiftDomainReference;
    }

    public void setShift(final Shift shift) {
        shiftDomainReference = shift;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }

    public Boolean getShowOtherProfessors() {
        return showOtherProfessors;
    }

    public void setShowOtherProfessors(final Boolean showOtherProfessors) {
        this.showOtherProfessors = showOtherProfessors;
    }

    public SortedSet<Summary> search() {

        final SortedSet<Summary> summaries;
        if (isAscendant()) {
            summaries = new TreeSet<Summary>(Summary.COMPARATOR_BY_DATE_AND_HOUR_ASC);
        } else {
            summaries = new TreeSet<Summary>(Summary.COMPARATOR_BY_DATE_AND_HOUR);
        }

        for (final Summary summary : getExecutionCourse().getAssociatedSummariesSet()) {
            final Shift shift = summary.getShift();
            if (getShift() == null || getShift() == shift) {
                if (getShiftType() == null || getShiftType() == summary.getSummaryType()) {
                    final Professorship professorship = summary.getProfessorship();
                    if (getProfessorship() == null && showOtherProfessors == null) {
                        summaries.add(summary);
                    } else if (professorship == null && showOtherProfessors != null && showOtherProfessors.booleanValue()) {
                        summaries.add(summary);
                    } else if (showOtherProfessors == null
                            && ((getProfessorship() == null && professorship != null) || getProfessorship() == professorship)) {
                        summaries.add(summary);
                    }
                }
            }
        }
        return summaries;
    }

    public SortedSet<Summary> getSummaries() {
        return search();
    }

    public SortedSet<Summary> getSummariesInverted() {
        final SortedSet<Summary> summaries = new TreeSet<Summary>(Summary.COMPARATOR_BY_DATE_AND_HOUR_ASC);
        summaries.addAll(search());
        return summaries;
    }

    public SortedSet<ShiftType> getShiftTypes() {
        final SortedSet<ShiftType> shiftTypes = new TreeSet<ShiftType>();
        for (final Shift shift : getExecutionCourse().getAssociatedShifts()) {
            shiftTypes.addAll(shift.getTypes());
        }
        return shiftTypes;
    }

    public void setAscendant(Boolean descendant) {
        this.ascendant = descendant;
    }

    public Boolean isAscendant() {
        return ascendant;
    }

}
