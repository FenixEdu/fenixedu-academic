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
package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import static org.apache.commons.lang.StringUtils.isEmpty;

import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class CurricularCourseMarksheetManagementBean implements Serializable {

    private static final long serialVersionUID = -7798826573352027291L;

    static final public Comparator<CurricularCourseMarksheetManagementBean> COMPARATOR_BY_NAME =
            new Comparator<CurricularCourseMarksheetManagementBean>() {
                @Override
                public int compare(CurricularCourseMarksheetManagementBean o1, CurricularCourseMarksheetManagementBean o2) {
                    if (isEmpty(o1.getName()) && isEmpty(o2.getName())) {
                        return 0;
                    }
                    if (isEmpty(o1.getName())) {
                        return -1;
                    }
                    if (isEmpty(o2.getName())) {
                        return 1;
                    }
                    return o1.getName().compareTo(o2.getName());
                }
            };

    private CurricularCourse curricularCourse;
    private ExecutionSemester executionSemester;

    public CurricularCourseMarksheetManagementBean(final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester) {
        setCurricularCourse(curricularCourse);
        setgetExecutionSemester(executionSemester);
    }

    public CurricularCourse getCurricularCourse() {
        return this.curricularCourse;
    }

    public void setCurricularCourse(CurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }

    public ExecutionSemester getExecutionSemester() {
        return this.executionSemester;
    }

    public void setgetExecutionSemester(ExecutionSemester attribute) {
        this.executionSemester = attribute;
    }

    public String getKey() {
        return String.valueOf(getCurricularCourse().getExternalId() + ":" + getExecutionSemester().getExternalId());
    }

    public String getName() {
        return getCurricularCourse().getName(getExecutionSemester());
    }

    public String getCode() {
        return getCurricularCourse().getCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CurricularCourseMarksheetManagementBean) {
            return getCurricularCourse().equals(((CurricularCourseMarksheetManagementBean) obj).getCurricularCourse());
        }
        return false;
    }
}
