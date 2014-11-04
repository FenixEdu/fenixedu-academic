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
package org.fenixedu.academic.dto.teacher;

import java.io.Serializable;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.dto.teacher.executionCourse.ImportContentBean;

public class ImportLessonPlanningsBean extends ImportContentBean implements Serializable {

    private ImportType importType;

    private Shift shiftReference;

    private ExecutionCourse executionCourseToReference;

    public ImportLessonPlanningsBean(ExecutionCourse executionCourse) {
        super();
        setExecutionCourseTo(executionCourse);
        setShift(null);
    }

    public ExecutionCourse getExecutionCourseTo() {
        return this.executionCourseToReference;
    }

    public void setExecutionCourseTo(ExecutionCourse executionCourse) {
        this.executionCourseToReference = executionCourse;
    }

    public ImportType getImportType() {
        return importType;
    }

    public void setImportType(ImportType importType) {
        this.importType = importType;
    }

    public Shift getShift() {
        return this.shiftReference;
    }

    public void setShift(Shift shift) {
        this.shiftReference = shift;
    }

    public static enum ImportType {

        SUMMARIES,

        PLANNING;

        public String getName() {
            return name();
        }
    }

}
