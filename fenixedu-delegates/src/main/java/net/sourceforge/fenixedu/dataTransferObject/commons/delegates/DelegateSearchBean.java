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
package org.fenixedu.academic.dto.commons.delegates;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.organizationalStructure.FunctionType;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.student.Registration;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DelegateSearchBean implements Serializable {
    private DegreeType degreeType;

    private Degree degree;

    private String delegateName;

    private Integer studentNumber;

    private Person delegate;

    private PersonFunction delegateFunction;

    private FunctionType delegateType;

    private CurricularYear curricularYear;

    private ExecutionYear executionYear;

    private DelegateSearchType delegateSearchType;

    static final public Comparator<DelegateSearchBean> YEAR_DELEGATE_COMPARATOR_BY_EXECUTION_YEAR_AND_CURRICULAR_YEAR =
            new ComparatorChain();
    static {
        ((ComparatorChain) YEAR_DELEGATE_COMPARATOR_BY_EXECUTION_YEAR_AND_CURRICULAR_YEAR).addComparator(new BeanComparator(
                "executionYear.year"));
        ((ComparatorChain) YEAR_DELEGATE_COMPARATOR_BY_EXECUTION_YEAR_AND_CURRICULAR_YEAR).addComparator(new BeanComparator(
                "curricularYear.year"));
    }

    static final public Comparator<DelegateSearchBean> DELEGATE_COMPARATOR_BY_EXECUTION_YEAR = new BeanComparator(
            "executionYear.year");

    public DelegateSearchBean() {
        setDegreeType(null);
        setDegree(null);
        setCurricularYear(null);
        setExecutionYear(null);
        setDelegate(null);
        setDelegateFunction(null);
        setDelegateSearchType(DelegateSearchType.ACTIVE_DELEGATES);
    }

    public DelegateSearchBean(Person person, PersonFunction delegateFunction) {
        this();
        setDelegate(person);
        if (person.getStudent() != null) {
            Registration registration = person.getStudent().getLastActiveRegistration();
            if (registration != null) {
                setDegree(registration.getDegree());
                setDegreeType(registration.getDegree().getDegreeType());
            }
        }
        setDelegateType(delegateFunction.getFunction().getFunctionType());
        setDelegateFunction(delegateFunction);
        setCurricularYear(delegateFunction.getCurricularYear());
        setExecutionYear(ExecutionYear.getExecutionYearByDate(delegateFunction.getBeginDate()));
        setDelegateSearchType(DelegateSearchType.ACTIVE_DELEGATES);
    }

    public DelegateSearchBean(Person person, FunctionType functionType, ExecutionYear executionYear) {
        this();
        setDelegate(person);
        Registration lastActiveRegistration = person.getStudent().getLastActiveRegistration();
        if (person.getStudent() != null) {
            setDegree(lastActiveRegistration != null ? lastActiveRegistration.getDegree() : null);
            setDegreeType(lastActiveRegistration != null ? lastActiveRegistration.getDegree().getDegreeType() : null);
        }
        setDelegateType(functionType);
        setExecutionYear(executionYear);
    }

    public DelegateSearchBean(Person person, FunctionType functionType, CurricularYear curricularYear, ExecutionYear executionYear) {
        this(person, functionType, executionYear);
        setCurricularYear(curricularYear);
    }

    public DelegateSearchBean(ExecutionYear currentExecutionYear, Degree degree, DegreeType degreeType) {
        setExecutionYear(currentExecutionYear);
        setDegree(degree);
        setDegreeType(degreeType);
    }

    public Degree getDegree() {
        return degree;
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

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getDelegateName() {
        return delegateName;
    }

    public void setDelegateName(String delegateName) {
        this.delegateName = delegateName;
    }

    public Person getDelegate() {
        return delegate;
    }

    public void setDelegate(Person delegate) {
        this.delegate = delegate;
    }

    public FunctionType getDelegateType() {
        return delegateType;
    }

    public void setDelegateType(FunctionType delegateType) {
        this.delegateType = delegateType;
    }

    public CurricularYear getCurricularYear() {
        return curricularYear;
    }

    public void setCurricularYear(CurricularYear curricularYear) {
        this.curricularYear = curricularYear;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public PersonFunction getDelegateFunction() {
        return delegateFunction;
    }

    public void setDelegateFunction(PersonFunction delegateFunction) {
        this.delegateFunction = delegateFunction;
    }

    public boolean getOnlyActiveDelegates() {
        return getDelegateSearchType().equals(DelegateSearchType.ACTIVE_DELEGATES);
    }

    public YearMonthDay getStartDate() {
        return getDelegateFunction().getBeginDate();
    }

    public YearMonthDay getEndDate() {
        PersonFunction delegateFunction = getDelegateFunction();
        return delegateFunction.isActive(new YearMonthDay()) ? null : delegateFunction.getEndDate();
    }

    public String getDelegateFunctionNameIfGgaeDelegate() {
        return (getDelegateType().equals(FunctionType.DELEGATE_OF_GGAE) ? getDelegateFunction().getFunction().getName() : null);
    }

    public static enum DelegateSearchType {
        ACTIVE_DELEGATES, ALL_DELEGATES;
    }

    public DelegateSearchType getDelegateSearchType() {
        return delegateSearchType;
    }

    public void setDelegateSearchType(DelegateSearchType delegateSearchType) {
        this.delegateSearchType = delegateSearchType;
    }

    public String getDegreeName() {
        return (getDegree() != null ? getDegree().getNameFor(getExecutionYear()).getContent() : null);
    }

    public static class DegreesGivenDegreeType implements DataProvider {

        @Override
        public Object provide(Object source, Object currentValue) {
            final Set<Degree> degrees = new TreeSet<Degree>();
            final DelegateSearchBean bean = (DelegateSearchBean) source;
            final ExecutionYear executionPeriod = bean.getExecutionYear();
            final DegreeType type = bean.getDegreeType();

            if (type != null && executionPeriod != null) {
                for (ExecutionDegree executionDegree : executionPeriod.getExecutionDegreesSet()) {
                    if (executionDegree.getDegreeType().equals(type)) {
                        degrees.add(executionDegree.getDegree());
                    }
                }
            }

            // Collections.sort(degrees, Degree.COMPARATOR_BY_NAME);
            return degrees;
        }

        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }

    }

    public static class DegreeTypesGivenExecutionYear implements DataProvider {

        @Override
        public Object provide(Object source, Object currentValue) {
            final DelegateSearchBean bean = (DelegateSearchBean) source;
            final ExecutionYear executionPeriod = bean.getExecutionYear();
            Set<DegreeType> degreeTypes = new TreeSet<DegreeType>();

            for (ExecutionDegree executionDegree : executionPeriod.getExecutionDegreesSet()) {
                degreeTypes.add(executionDegree.getDegreeType());
            }

            return degreeTypes;
        }

        @Override
        public Converter getConverter() {
            return new Converter() {

                @Override
                public Object convert(Class type, Object value) {
                    return DegreeType.valueOf((String) value);
                }

            };
        }
    }
}