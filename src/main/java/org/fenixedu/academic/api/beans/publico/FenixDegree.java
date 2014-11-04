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
package org.fenixedu.academic.api.beans.publico;

import java.util.List;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class FenixDegree {
    protected String id;
    protected String name;
    protected String acronym;
    protected List<String> academicTerms;

    private static List<String> getAcademicTerms(final Degree degree) {
        List<String> academicTerms =
                FluentIterable
                        .from(FluentIterable.from(degree.getExecutionDegrees())
                                .transform(new Function<ExecutionDegree, ExecutionYear>() {

                                    @Override
                                    public ExecutionYear apply(ExecutionDegree input) {
                                        return input.getExecutionYear();
                                    }
                                }).toSortedSet(ExecutionYear.REVERSE_COMPARATOR_BY_YEAR))
                        .transform(new Function<ExecutionYear, String>() {

                            @Override
                            public String apply(ExecutionYear input) {
                                return input.getQualifiedName();
                            }
                        }).toList();
        return academicTerms;
    }

    public FenixDegree(Degree degree) {
        this(degree, false);
    }

    public FenixDegree(Degree degree, Boolean withAcademicTerms) {
        setId(degree.getExternalId());
        setName(degree.getName());
        setAcronym(degree.getSigla());
        setAcademicTerms(withAcademicTerms ? getAcademicTerms(degree) : null);
    }

    public FenixDegree(Degree degree, String name, Boolean withAcademicTerms) {
        setId(degree.getExternalId());
        setName(name);
        setAcronym(degree.getSigla());
        setAcademicTerms(withAcademicTerms ? getAcademicTerms(degree) : null);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    @JsonInclude(Include.NON_NULL)
    public List<String> getAcademicTerms() {
        return academicTerms;
    }

    public void setAcademicTerms(List<String> academicTerms) {
        this.academicTerms = academicTerms;
    }

}
