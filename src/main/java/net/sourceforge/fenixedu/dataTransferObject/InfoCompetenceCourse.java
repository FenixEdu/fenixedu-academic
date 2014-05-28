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
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;

public class InfoCompetenceCourse extends InfoObject {
    private String name;
    private String code;
    private List<InfoDepartment> departments;
    private List<InfoCurricularCourse> associatedCurricularCourses;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<InfoDepartment> getDepartments() {
        return departments;
    }

    public void setDepartment(List<InfoDepartment> departments) {
        this.departments = departments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<InfoCurricularCourse> getAssociatedCurricularCourses() {
        return associatedCurricularCourses;
    }

    public void setAssociatedCurricularCourses(List<InfoCurricularCourse> associatedCurricularCourses) {
        this.associatedCurricularCourses = associatedCurricularCourses;
    }

    public void copyFromDomain(CompetenceCourse competenceCourse) {
        super.copyFromDomain(competenceCourse);
        if (competenceCourse != null) {
            setCode(competenceCourse.getCode());
            setName(competenceCourse.getName());
            List<InfoDepartment> infoDepartments = new ArrayList<InfoDepartment>();
            for (Department department : competenceCourse.getDepartments()) {
                infoDepartments.add(InfoDepartment.newInfoFromDomain(department));
            }
            setDepartment(infoDepartments);
        }
    }

    public static InfoCompetenceCourse newInfoFromDomain(CompetenceCourse competenceCourse) {
        InfoCompetenceCourse infoCompetenceCourse = null;
        if (competenceCourse != null) {
            infoCompetenceCourse = new InfoCompetenceCourse();
            infoCompetenceCourse.copyFromDomain(competenceCourse);
        }
        return infoCompetenceCourse;
    }

}
