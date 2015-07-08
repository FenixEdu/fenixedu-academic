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
package org.fenixedu.academic.dto.resourceAllocationManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.fenixedu.academic.domain.CurricularYearList;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.OccupationPeriod;
import org.fenixedu.academic.domain.OccupationPeriodReference;
import org.fenixedu.academic.domain.OccupationPeriodType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.date.IntervalTools;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class OccupationPeriodBean implements Serializable, Comparable<OccupationPeriodBean> {

    /**
     * 
     */
    private static final long serialVersionUID = -9196229454898126127L;

    private List<Interval> intervals = new ArrayList<Interval>();

    private OccupationPeriodType occupationPeriodType;

    private OccupationPeriod occupationPeriod;

    private Integer semester;

    private List<OccupationPeriodReference> references = new ArrayList<OccupationPeriodReference>();

    private final int id;

    public OccupationPeriodBean(int id) {

        this.id = id;

        this.semester = ExecutionSemester.readActualExecutionSemester().getSemester();

        this.occupationPeriodType = OccupationPeriodType.LESSONS;

        this.intervals = Lists.newArrayList(new Interval(new DateTime(), new DateTime().plusDays(1)));

    }

    public OccupationPeriodBean(OccupationPeriodReference reference, int id) {

        this.occupationPeriod = reference.getOccupationPeriod();

        reloadIntervals();

        this.semester = reference.getSemester();

        this.occupationPeriodType = reference.getPeriodType();

        this.id = id;
    }

    private void reloadIntervals() {
        this.intervals = occupationPeriod.getIntervals();
    }

    public int getId() {
        return id;
    }

    public List<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<Interval> intervals) {
        this.intervals = intervals;
    }

    public OccupationPeriodType getOccupationPeriodType() {
        return occupationPeriodType;
    }

    public void setOccupationPeriodType(OccupationPeriodType occupationPeriodType) {
        this.occupationPeriodType = occupationPeriodType;
    }

    public OccupationPeriod getOccupationPeriod() {
        return occupationPeriod;
    }

    public void setOccupationPeriod(OccupationPeriod occupationPeriod) {
        this.occupationPeriod = occupationPeriod;
    }

    public List<OccupationPeriodReference> getReferences() {
        return references;
    }

    public void setReferences(List<OccupationPeriodReference> references) {
        this.references = references;
    }

    public void addReference(OccupationPeriodReference reference) {
        references.add(reference);
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    // Note: If there ever is a need to create more than one semester, this is
    // the place it should be done!
    public Collection<Integer> getPossibleSemesters() {
        return Lists.newArrayList(1, 2);
    }

    public boolean getNewObject() {
        return occupationPeriod == null;
    }

    // Presentation Utility Methods

    public String getDatesString() {
        if (intervals.size() == 0 || occupationPeriod == null) {
            return BundleUtil.getString(Bundle.RESOURCE_ALLOCATION, "label.periods.no.dates");
        }

        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMM").withLocale(I18N.getLocale());

        StringBuilder builder = new StringBuilder();

        for (Interval interval : getIntervals()) {

            if (builder.length() > 0) {
                builder.append(", ");
            }

            builder.append(formatter.print(interval.getStart()));

            builder.append(" - ");

            builder.append(formatter.print(interval.getEnd()));

        }

        return builder.toString();

    }

    public String getName() {
        if (occupationPeriod == null) {
            return "Novo período";
        }

        return "Período de " + occupationPeriodType.getLocalizedName()
                + (semester == null ? "" : " - " + semester + "º Semestre");
    }

    @Override
    public int compareTo(OccupationPeriodBean other) {
        return ComparisonChain
                .start()
                .compare(this.getOccupationPeriod().getPeriodInterval().getStartMillis(),
                        other.getOccupationPeriod().getPeriodInterval().getStartMillis())
                .compare(this.getReferences().size(), this.getReferences().size()).result();
    }

    // Actual bean operations

    @Atomic
    public void updateDates(String parameter) {

        Iterable<Interval> intervals = extractIntervals(parameter);

        consolidateReferences();

        // The occupation period is shared by multiple types, a new one must be
        // created!
        if (this.occupationPeriod.getExecutionDegreesSet().size() != getReferences().size()) {
            this.occupationPeriod = new OccupationPeriod(intervals.iterator());

            // Period has changed, lets change the references so they point to
            // the new period.
            consolidateReferences();
        } else {
            this.occupationPeriod.editDates(intervals.iterator());
        }

        reloadIntervals();

    }

    @Atomic
    public void updateCourses(String parameter) {

        Map<ExecutionDegree, CurricularYearList> degreeMap = extractCourses(parameter);

        // Step 1, Remove non-existing references
        for (Iterator<OccupationPeriodReference> references = getReferences().iterator(); references.hasNext();) {
            OccupationPeriodReference reference = references.next();
            if (!degreeMap.containsKey(reference.getExecutionDegree())) {
                reference.delete();
                references.remove();
            }
        }

        // Step 2, Add new references, and update old ones
        for (final Entry<ExecutionDegree, CurricularYearList> entry : degreeMap.entrySet()) {
            OccupationPeriodReference reference = null;

            for (OccupationPeriodReference ref : getReferences()) {
                if (ref.getExecutionDegree().equals(entry.getKey())) {
                    reference = ref;
                    break;
                }
            }

            if (reference == null) {
                references.add(new OccupationPeriodReference(occupationPeriod, entry.getKey(), occupationPeriodType, semester,
                        entry.getValue()));
            } else {
                reference.setOccupationPeriod(occupationPeriod);
                reference.setPeriodType(occupationPeriodType);
                reference.setSemester(semester);
                reference.setCurricularYears(entry.getValue());
            }

        }

    }

    @Atomic
    public void deletePeriod() {
        for (OccupationPeriodReference ref : getReferences()) {
            ref.delete();
        }
    }

    @Atomic
    public void create(String intervalsStr, String courses) {

        Iterable<Interval> intervals = extractIntervals(intervalsStr);

        this.occupationPeriod = new OccupationPeriod(intervals.iterator());

        updateCourses(courses);

        reloadIntervals();
    }

    // Private Utility Methods

    private static final Splitter SPLITTER = Splitter.on(';').trimResults().omitEmptyStrings();

    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("dd/MM/yyyy");

    private void consolidateReferences() {
        Preconditions.checkNotNull(occupationPeriod);

        for (OccupationPeriodReference reference : references) {
            reference.setOccupationPeriod(occupationPeriod);
        }

    }

    private Iterable<Interval> extractIntervals(String parameter) {
        Iterable<Interval> intervals = Iterables.transform(SPLITTER.split(parameter), new Function<String, Interval>() {

            @Override
            public Interval apply(String string) {

                String[] dates = string.split(",");

                if (dates.length != 2) {
                    throw new RuntimeException("Error while recreating intervals, '" + string + "' cannot be parsed!");
                }

                LocalDate start = FORMATTER.parseDateTime(dates[0]).toLocalDate();
                LocalDate end = FORMATTER.parseDateTime(dates[1]).toLocalDate();

                return IntervalTools.getInterval(start, end);

            }

        });

        Iterator<Interval> iter = intervals.iterator();

        Interval last = iter.next();

        while (iter.hasNext()) {
            Interval current = iter.next();
            if (!current.isAfter(last)) {
                throw new DomainException("label.occupation.period.invalid.dates");
            }
            last = current;
        }

        return intervals;
    }

    private Map<ExecutionDegree, CurricularYearList> extractCourses(String parameter) {
        Map<ExecutionDegree, CurricularYearList> degreeMap = new HashMap<ExecutionDegree, CurricularYearList>();

        for (String string : SPLITTER.split(parameter)) {
            String[] parts = string.split(":");

            if (parts.length != 2) {
                throw new RuntimeException("Error while recreating execution degree, '" + string + "' cannot be parsed!");
            }

            String oid = parts[0];

            ExecutionDegree degree = FenixFramework.getDomainObject(oid);

            degreeMap.put(degree, CurricularYearList.internalize(parts[1]));

        }
        return degreeMap;
    }

    @Atomic
    public OccupationPeriodBean duplicate(int newId, OccupationPeriodType newPeriodType) {

        if (newPeriodType == null) {
            throw new DomainException("label.occupation.period.duplicate.message");
        }

        OccupationPeriodBean newBean = new OccupationPeriodBean(newId);

        newBean.setOccupationPeriodType(newPeriodType);
        newBean.setIntervals(intervals);
        newBean.setOccupationPeriod(occupationPeriod);
        newBean.setSemester(semester);

        for (OccupationPeriodReference reference : references) {
            newBean.addReference(new OccupationPeriodReference(reference.getOccupationPeriod(), reference.getExecutionDegree(),
                    newPeriodType, semester, reference.getCurricularYears()));
        }

        return newBean;
    }

}
