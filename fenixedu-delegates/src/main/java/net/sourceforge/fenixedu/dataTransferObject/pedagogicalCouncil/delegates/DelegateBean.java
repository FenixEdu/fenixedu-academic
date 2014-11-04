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
package org.fenixedu.academic.dto.pedagogicalCouncil.delegates;

import java.io.Serializable;

import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.elections.DelegateElection;
import org.fenixedu.academic.domain.organizationalStructure.Function;
import org.fenixedu.academic.domain.organizationalStructure.FunctionType;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.student.Student;

import org.joda.time.LocalDate;

public class DelegateBean implements Serializable {
    private DegreeType degreeType;

    private ExecutionYear executionYear;

    private Degree degree;

    private CurricularYear curricularYear;

    private Student delegate;

    private Person ggaeDelegate;

    private FunctionType delegateType;

    private Integer studentNumber;

    private String personUsername;

    private Function ggaeDelegateFunction;

    private DelegateElection delegateElection;

    private PersonFunction personFunction;

    private LocalDate personFunctionNewEndDate;

    public DelegateBean() {
        setDegreeType(null);
        setDegree(null);
        setCurricularYear(null);
        setDelegate(null);
        setGgaeDelegate(null);
        setGgaeDelegateFunction(null);
    }

    public CurricularYear getCurricularYear() {
        return (curricularYear);
    }

    public void setCurricularYear(CurricularYear curricularYear) {
        this.curricularYear = curricularYear;
    }

    public Degree getDegree() {
        return (degree);
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public DegreeType getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
    }

    public ExecutionYear getExecutionYear() {
        return (executionYear);
    }

    public void setDelegateElection(DelegateElection election) {
        this.delegateElection = election;
    }

    public DelegateElection getDelegateElection() {
        return (delegateElection);
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public Student getDelegate() {
        return (delegate);
    }

    public void setDelegate(Student delegate) {
        this.delegate = delegate;
    }

    public Person getGgaeDelegate() {
        return (ggaeDelegate);
    }

    public void setGgaeDelegate(Person ggaeDelegate) {
        this.ggaeDelegate = ggaeDelegate;
    }

    public Integer getStudentNumber() {
        return ((getDelegate() == null) || (getDelegate().getLastActiveRegistration() == null)) ? studentNumber : getDelegate()
                .getLastActiveRegistration().getNumber();
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getPersonUsername() {
        return (getGgaeDelegate() == null ? personUsername : getGgaeDelegate().getUsername());
    }

    public void setPersonUsername(String personUsername) {
        this.personUsername = personUsername;
    }

    public String getPersonName() {
        return (getGgaeDelegate() != null ? getGgaeDelegate().getName() : null);
    }

    public String getStudentName() {
        return (getDelegate() != null ? getDelegate().getPerson().getName() : null);
    }

    public String getStudentEmail() {
        return (getDelegate() != null ? getDelegate().getPerson().getEmail() : null);
    }

    public FunctionType getDelegateType() {
        return delegateType;
    }

    public void setDelegateType(FunctionType delegateType) {
        this.delegateType = delegateType;
    }

    public Function getGgaeDelegateFunction() {
        return (ggaeDelegateFunction);
    }

    public void setGgaeDelegateFunction(Function delegateFunction) {
        this.ggaeDelegateFunction = delegateFunction;
    }

    /*
     * THE FOLLOWING METHODS ARE PRESENTATION LOGIC
     */
    public boolean getHasDelegate() {
        return hasDelegate();
    }

    public boolean getHasGgaeDelegate() {
        return hasGgaeDelegate();
    }

    public boolean isEmptyDelegateBean() {
        return (getDelegate() == null && !isYearDelegateTypeBean() ? true : false);
    }

    public boolean isEmptyYearDelegateBean() {
        return (getDelegate() == null && isYearDelegateTypeBean() ? true : false);
    }

    public boolean isEmptyYearDelegateBeanWithElection() {
        return (hasDelegateElection() && getDelegateElection().hasLastVotingPeriod() ? true : false);
    }

    public boolean isYearDelegateBeanWithElectedElection() {
        return (hasDelegateElection() && hasYearDelegate() && getDelegateElection().getElectedStudent() != null
                && getDelegateElection().getElectedStudent().equals(getDelegate()) ? true : false);
    }

    public boolean hasDelegate() {
        return (getDelegate() != null ? true : false);
    }

    public boolean hasGgaeDelegate() {
        return (getGgaeDelegate() != null ? true : false);
    }

    public boolean isYearDelegateTypeBean() {
        return (getDelegateType().equals(FunctionType.DELEGATE_OF_YEAR) ? true : false);
    }

    public boolean hasYearDelegate() {
        return (isYearDelegateTypeBean() && hasDelegate() ? true : false);
    }

    public boolean hasDelegateElection() {
        return (getDelegateElection() != null ? true : false);
    }

    public PersonFunction getPersonFunction() {
        return personFunction;
    }

    public void setPersonFunction(PersonFunction personFunction) {
        this.personFunction = personFunction;
    }

    public LocalDate getPersonFunctionNewEndDate() {
        return personFunctionNewEndDate;
    }

    public void setPersonFunctionNewEndDate(LocalDate personFunctionNewEndDate) {
        this.personFunctionNewEndDate = personFunctionNewEndDate;
    }
}