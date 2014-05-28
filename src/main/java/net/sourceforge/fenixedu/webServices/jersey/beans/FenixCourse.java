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
package net.sourceforge.fenixedu.webServices.jersey.beans;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

import org.fenixedu.bennu.core.util.CoreConfiguration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class FenixCourse {
    String id;
    String acronym;
    String name;
    String academicTerm;
    String url;

    public FenixCourse(ExecutionCourse course) {
        setId(course.getExternalId());
        setAcronym(course.getSigla());
        setName(course.getName());
        setAcademicTerm(course.getExecutionPeriod().getQualifiedName());
        setUrl(CoreConfiguration.getConfiguration().applicationUrl() + course.getSite().getReversePath());
    }

    public FenixCourse(String id, String acronym, String name) {
        this(id, acronym, name, null, null);
    }

    public FenixCourse(String id, String acronym, String name, String academicTerm, String url) {
        super();
        this.id = id;
        this.acronym = acronym;
        this.name = name;
        this.academicTerm = academicTerm;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonInclude(Include.NON_NULL)
    public String getAcademicTerm() {
        return academicTerm;
    }

    public void setAcademicTerm(String academicTerm) {
        this.academicTerm = academicTerm;
    }

    @JsonInclude(Include.NON_NULL)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}