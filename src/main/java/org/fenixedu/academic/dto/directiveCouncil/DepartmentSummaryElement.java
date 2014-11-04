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
package org.fenixedu.academic.dto.directiveCouncil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionSemester;

public class DepartmentSummaryElement implements Serializable {

    public enum SummaryControlCategory {
        BETWEEN_0_20, BETWEEN_20_40, BETWEEN_40_60, BETWEEN_60_80, BETWEEN_80_100;
    }

    public DepartmentSummaryElement(Department department, ExecutionSemester executionSemester) {
        setDepartment(department);
        setExecutionSemester(executionSemester);
    }

    private Department department;
    private ExecutionSemester executionSemester;
    private SummaryControlCategory summaryControlCategory;

    private Map<SummaryControlCategory, List<ExecutionCourseSummaryElement>> executionCoursesResume;

    public boolean getHasResumeData() {
        return getExecutionCoursesResume() != null && !getExecutionCoursesResume().isEmpty();
    }

    public List<ExecutionCourseSummaryElement> getExecutionCourses() {
        List<ExecutionCourseSummaryElement> returnList = new ArrayList<ExecutionCourseSummaryElement>();
        if (getSummaryControlCategory() == null) {
            for (List<ExecutionCourseSummaryElement> ecList : getExecutionCoursesResume().values()) {
                returnList.addAll(ecList);
            }
        } else {
            if (getExecutionCoursesResume().get(getSummaryControlCategory()) != null) {
                returnList = getExecutionCoursesResume().get(getSummaryControlCategory());
            }
        }
        return returnList;
    }

    public int getNumberOfExecutionCoursesWithin020() {
        if (getExecutionCoursesResume() != null) {
            List<ExecutionCourseSummaryElement> executionCourses =
                    getExecutionCoursesResume().get(SummaryControlCategory.BETWEEN_0_20);
            return executionCourses != null ? executionCourses.size() : 0;
        } else {
            return 0;
        }
    }

    public int getNumberOfExecutionCoursesWithin2040() {
        if (getExecutionCoursesResume() != null) {
            List<ExecutionCourseSummaryElement> executionCourses =
                    getExecutionCoursesResume().get(SummaryControlCategory.BETWEEN_20_40);
            return executionCourses != null ? executionCourses.size() : 0;
        } else {

            return 0;
        }
    }

    public int getNumberOfExecutionCoursesWithin4060() {
        if (getExecutionCoursesResume() != null) {
            List<ExecutionCourseSummaryElement> executionCourses =
                    getExecutionCoursesResume().get(SummaryControlCategory.BETWEEN_40_60);
            return executionCourses != null ? executionCourses.size() : 0;
        } else {
            return 0;
        }
    }

    public int getNumberOfExecutionCoursesWithin6080() {
        if (getExecutionCoursesResume() != null) {
            List<ExecutionCourseSummaryElement> executionCourses =
                    getExecutionCoursesResume().get(SummaryControlCategory.BETWEEN_60_80);
            return executionCourses != null ? executionCourses.size() : 0;
        } else {
            return 0;
        }
    }

    public int getNumberOfExecutionCoursesWithin80100() {
        if (getExecutionCoursesResume() != null) {
            List<ExecutionCourseSummaryElement> executionCourses =
                    getExecutionCoursesResume().get(SummaryControlCategory.BETWEEN_80_100);
            return executionCourses != null ? executionCourses.size() : 0;
        } else {
            return 0;
        }
    }

    public int getNumberOfExecutionCoursesWithin0100() {
        return getNumberOfExecutionCoursesWithin020() + getNumberOfExecutionCoursesWithin2040()
                + getNumberOfExecutionCoursesWithin4060() + getNumberOfExecutionCoursesWithin6080()
                + getNumberOfExecutionCoursesWithin80100();
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Map<SummaryControlCategory, List<ExecutionCourseSummaryElement>> getExecutionCoursesResume() {
        return executionCoursesResume;
    }

    public void setExecutionCoursesResume(Map<SummaryControlCategory, List<ExecutionCourseSummaryElement>> executionCoursesResume) {
        this.executionCoursesResume = executionCoursesResume;
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public String getSummaryControlCategoryString() {
        if (getSummaryControlCategory() != null) {
            return getSummaryControlCategory().toString();
        }
        return "";
    }

    public SummaryControlCategory getSummaryControlCategory() {
        return summaryControlCategory;
    }

    public void setSummaryControlCategory(SummaryControlCategory summaryControlCategory) {
        this.summaryControlCategory = summaryControlCategory;
    }

    public boolean isToDisplayCategoryLink020() {
        return getNumberOfExecutionCoursesWithin020() != 0 && getSummaryControlCategory() != SummaryControlCategory.BETWEEN_0_20;
    }

    public boolean isToDisplayCategoryLink2040() {
        return getNumberOfExecutionCoursesWithin2040() != 0
                && getSummaryControlCategory() != SummaryControlCategory.BETWEEN_20_40;
    }

    public boolean isToDisplayCategoryLink4060() {
        return getNumberOfExecutionCoursesWithin4060() != 0
                && getSummaryControlCategory() != SummaryControlCategory.BETWEEN_40_60;
    }

    public boolean isToDisplayCategoryLink6080() {
        return getNumberOfExecutionCoursesWithin6080() != 0
                && getSummaryControlCategory() != SummaryControlCategory.BETWEEN_60_80;
    }

    public boolean isToDisplayCategoryLink80100() {
        return getNumberOfExecutionCoursesWithin80100() != 0
                && getSummaryControlCategory() != SummaryControlCategory.BETWEEN_80_100;
    }

    public boolean isToDisplayCategoryLink0100() {
        return getNumberOfExecutionCoursesWithin0100() != 0 && getSummaryControlCategory() != null;
    }
}
