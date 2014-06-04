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
package net.sourceforge.fenixedu.dataTransferObject.department;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.curriculum.IGrade;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public abstract class CourseStatisticsDTO implements Serializable, Comparable<CourseStatisticsDTO> {

    private static final long serialVersionUID = 1L;

    private String externalId;

    private String name;

    private int firstEnrolledCount;

    private int firstApprovedCount;

    private IGrade firstApprovedAverage;

    private int restEnrolledCount;

    private int restApprovedCount;

    private IGrade restApprovedAverage;

    private int totalEnrolledCount;

    private int totalApprovedCount;

    private IGrade totalApprovedAverage;

    private String NOT_AVAILABLE;

    public static final Comparator<CourseStatisticsDTO> COURSE_STATISTICS_COMPARATOR_BY_NAME =
            new Comparator<CourseStatisticsDTO>() {

                @Override
                public int compare(CourseStatisticsDTO o1, CourseStatisticsDTO o2) {
                    return Collator.getInstance(I18N.getLocale()).compare(o1.getName(), o2.getName());
                }

            };

    public CourseStatisticsDTO() {
        NOT_AVAILABLE = BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.common.notAvailable");
    }

    public CourseStatisticsDTO(String externalId, String name, int firstEnrolledCount, int firstApprovedCount,
            IGrade firstApprovedAverage, int restEnrolledCount, int restApprovedCount, IGrade restApprovedAverage,
            int totalEnrolledCount, int totalApprovedCount, IGrade totalApprovedAverage) {
        super();

        this.externalId = externalId;
        this.name = name;

        this.firstEnrolledCount = firstEnrolledCount;
        this.firstApprovedCount = firstApprovedCount;
        this.firstApprovedAverage = firstApprovedAverage;

        this.restEnrolledCount = restEnrolledCount;
        this.restApprovedCount = restApprovedCount;
        this.restApprovedAverage = restApprovedAverage;

        this.totalEnrolledCount = totalEnrolledCount;
        this.totalApprovedCount = totalApprovedCount;
        this.totalApprovedAverage = totalApprovedAverage;
    }

    public IGrade getFirstApprovedAverage() {
        return firstApprovedAverage;
    }

    public String getFirstApprovedAveragex() {
        return (firstApprovedCount != 0) ? firstApprovedAverage.getGradeValue().toString() : NOT_AVAILABLE;
    }

    public void setFirstApprovedAverage(IGrade firstApprovedAverage) {
        this.firstApprovedAverage = firstApprovedAverage;
    }

    public int getFirstApprovedCount() {
        return firstApprovedCount;
    }

    public void setFirstApprovedCount(int firstApprovedCount) {
        this.firstApprovedCount = firstApprovedCount;
    }

    public int getFirstEnrolledCount() {
        return firstEnrolledCount;
    }

    public void setFirstEnrolledCount(int firstEnrolledCount) {
        this.firstEnrolledCount = firstEnrolledCount;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IGrade getRestApprovedAverage() {
        return restApprovedAverage;
    }

    public String getRestApprovedAveragex() {
        return (restApprovedCount != 0) ? restApprovedAverage.getGradeValue().toString() : NOT_AVAILABLE;
    }

    public void setRestApprovedAverage(IGrade restApprovedAverage) {
        this.restApprovedAverage = restApprovedAverage;
    }

    public int getRestApprovedCount() {
        return restApprovedCount;
    }

    public void setRestApprovedCount(int restApprovedCount) {
        this.restApprovedCount = restApprovedCount;
    }

    public int getRestEnrolledCount() {
        return restEnrolledCount;
    }

    public void setRestEnrolledCount(int restEnrolledCount) {
        this.restEnrolledCount = restEnrolledCount;
    }

    public IGrade getTotalApprovedAverage() {
        return totalApprovedAverage;
    }

    public String getTotalApprovedAveragex() {
        return (totalApprovedCount != 0) ? totalApprovedAverage.getGradeValue().toString() : NOT_AVAILABLE;
    }

    public void setTotalApprovedAverage(IGrade totalApprovedAverage) {
        this.totalApprovedAverage = totalApprovedAverage;
    }

    public int getTotalApprovedCount() {
        return totalApprovedCount;
    }

    public void setTotalApprovedCount(int totalApprovedCount) {
        this.totalApprovedCount = totalApprovedCount;
    }

    public int getTotalEnrolledCount() {
        return totalEnrolledCount;
    }

    public void setTotalEnrolledCount(int totalEnrolledCount) {
        this.totalEnrolledCount = totalEnrolledCount;
    }

    public String getApprovedPercentage() {
        if (totalEnrolledCount == 0) {
            return NOT_AVAILABLE;
        }

        String result = "";
        result = String.format("%.2f%%", totalApprovedCount * 100.0 / totalEnrolledCount);
        return result;
    }

    @Override
    public int compareTo(CourseStatisticsDTO o) {
        return Collator.getInstance(I18N.getLocale()).compare(this.getName(), o.getName());
    }
}
