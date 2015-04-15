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
package org.fenixedu.academic.ui.struts.action.scientificCouncil.thesis;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.service.services.person.SearchPerson;
import org.fenixedu.academic.service.services.person.SearchPerson.SearchParameters;
import org.fenixedu.academic.service.services.person.SearchPerson.SearchPersonPredicate;
import org.fenixedu.academic.ui.struts.action.coordinator.thesis.ThesisPresentationState;
import org.fenixedu.academic.util.CollectionPager;

public class ManageSecondCycleThesisSearchBean implements Serializable {

    public static class Counter implements Serializable {

        private static final long serialVersionUID = 1L;

        private int count = 1;

        public int getCount() {
            return count;
        }

    }

    public static class ThesisPresentationStateCountMap extends TreeMap<ThesisPresentationState, Counter> {

        private void count(final ThesisPresentationState thesisPresentationState) {
            if (containsKey(thesisPresentationState)) {
                get(thesisPresentationState).count++;
            } else {
                put(thesisPresentationState, new Counter());
            }
        }

    }

    private static final long serialVersionUID = 1L;

    private ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();

    private ThesisPresentationState presentationState = ThesisPresentationState.CONFIRMED;

    private String searchString;

    private transient ThesisPresentationStateCountMap thesisPresentationStateCountMap;

    public ManageSecondCycleThesisSearchBean() {
        this(null);
    }

    public ManageSecondCycleThesisSearchBean(final ExecutionYear executionYear) {
        if (executionYear == null) {
            setExecutionYear(ExecutionYear.readCurrentExecutionYear());
        } else {
            setExecutionYear(executionYear);
        }
    }

    public ExecutionYear getExecutionYear() {
        return this.executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public ThesisPresentationState getPresentationState() {
        return presentationState;
    }

    public void setPresentationState(ThesisPresentationState presentationState) {
        this.presentationState = presentationState;
    }

    public ThesisPresentationStateCountMap getThesisPresentationStateCountMap() {
        return thesisPresentationStateCountMap;
    }

    public SortedSet<Person> findPersonBySearchString() {
        final SortedSet<Person> result = new TreeSet<Person>(Person.COMPARATOR_BY_NAME_AND_ID);
        if (searchString != null && !searchString.isEmpty()) {
            result.addAll(searchName(searchString));
            result.addAll(searchUsername(searchString));
            result.addAll(searchStudentNumber(searchString));
        }
        return result;
    }

    private Collection<Person> searchName(final String name) {
        final SearchParameters searchParameters = new SearchParameters();
        searchParameters.setName(name);
        return search(searchParameters);
    }

    private Collection<Person> searchUsername(final String username) {
        final SearchParameters searchParameters = new SearchParameters();
        searchParameters.setUsername(username);
        return search(searchParameters);
    }

    private Collection<Person> searchStudentNumber(final String number) {
        if (StringUtils.isNumeric(number)) {
            final SearchParameters searchParameters = new SearchParameters();
            searchParameters.setStudentNumber(new Integer(number));
            return search(searchParameters);
        }
        return Collections.emptySet();
    }

    private Collection<Person> search(final SearchParameters searchParameters) {
        final SearchPersonPredicate searchPersonPredicate = new SearchPerson.SearchPersonPredicate(searchParameters);
        SearchPerson searchPerson = new SearchPerson();
        final CollectionPager<Person> people = searchPerson.run(searchParameters, searchPersonPredicate);
        return people.getCollection();
    }

    public SortedSet<Enrolment> findEnrolments() {
        thesisPresentationStateCountMap = new ThesisPresentationStateCountMap();

        final SortedSet<Enrolment> result = new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_STUDENT_NUMBER);

        final Set<CurricularCourse> curricularCourses = new HashSet<CurricularCourse>();

        for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            degreeCurricularPlan.applyToCurricularCourses(executionYear, new org.apache.commons.collections.Predicate() {
                @Override
                public boolean evaluate(final Object arg0) {
                    final CurricularCourse curricularCourse = (CurricularCourse) arg0;
                    if (curricularCourse.isDissertation()) {
                        if (!curricularCourses.contains(curricularCourse)) {
                            curricularCourses.add(curricularCourse);
                            for (final CurriculumModule curriculumModule : curricularCourse.getCurriculumModulesSet()) {
                                if (curriculumModule.isEnrolment()) {
                                    final Enrolment enrolment = (Enrolment) curriculumModule;
                                    if (enrolment.getExecutionYear() == executionYear) {
                                        final ThesisPresentationState state =
                                                ThesisPresentationState.getThesisPresentationState(enrolment);
                                        if (presentationState == null || state == presentationState) {
                                            result.add(enrolment);
                                        }
                                        thesisPresentationStateCountMap.count(state);
                                    }
                                }
                            }
                        }
                    }
                    return false;
                }
            });
        }

        return result;
    }

}
