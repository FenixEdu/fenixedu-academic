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
package org.fenixedu.academic.dto.teacher.executionCourse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.Attends.StudentAttendsStateType;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.WorkingStudentSelectionType;
import org.fenixedu.academic.util.predicates.AndPredicate;
import org.fenixedu.academic.util.predicates.InlinePredicate;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class SearchExecutionCourseAttendsBean implements Serializable {

    private ExecutionCourse executionCourse;
    private Boolean viewPhoto;
    private Collection<StudentAttendsStateType> attendsStates;
    private Collection<WorkingStudentSelectionType> workingStudentTypes;
    private Collection<DegreeCurricularPlan> degreeCurricularPlans;
    private Collection<Shift> shifts;
    private Collection<Attends> attendsResult;
    private transient Map<Integer, Integer> enrolmentsNumberMap;

    public String getEnumerationResourcesString(String name) {
        return BundleUtil.getString(Bundle.ENUMERATION, name);
    }

    public String getApplicationResourcesString(String name) {
        return BundleUtil.getString(Bundle.APPLICATION, name);
    }

    public SearchExecutionCourseAttendsBean(ExecutionCourse executionCourse) {
        setExecutionCourse(executionCourse);
        setViewPhoto(false);
        setAttendsStates(Arrays.asList(StudentAttendsStateType.values()));
        setWorkingStudentTypes(Arrays.asList(WorkingStudentSelectionType.values()));
        setShifts(getExecutionCourse().getAssociatedShifts());
        setDegreeCurricularPlans(getAttendsDegreeCurricularPlans(getExecutionCourse()));
        attendsResult = new ArrayList<Attends>();
    }

    public ExecutionCourse getExecutionCourse() {
        return this.executionCourse;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    public Boolean getViewPhoto() {
        return viewPhoto;
    }

    public void setViewPhoto(Boolean viewPhoto) {
        this.viewPhoto = viewPhoto;
    }

    public Collection<StudentAttendsStateType> getAttendsStates() {
        return attendsStates;
    }

    public void setAttendsStates(Collection<StudentAttendsStateType> attendsStates) {
        this.attendsStates = attendsStates;
    }

    public Collection<DegreeCurricularPlan> getDegreeCurricularPlans() {
        Collection<DegreeCurricularPlan> dcps = new ArrayList<DegreeCurricularPlan>();
        for (DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
            dcps.add(degreeCurricularPlan);
        }
        return dcps;
    }

    public void setShifts(Collection<Shift> shifts) {
        Collection<Shift> drShifts = new ArrayList<Shift>();
        for (Shift shift : shifts) {
            drShifts.add(shift);
        }
        this.shifts = drShifts;
    }

    public Collection<Shift> getShifts() {
        Collection<Shift> shifts = new ArrayList<Shift>();
        for (Shift shift : this.shifts) {
            shifts.add(shift);
        }
        return shifts;
    }

    public void setDegreeCurricularPlans(Collection<DegreeCurricularPlan> degreeCurricularPlans) {
        Collection<DegreeCurricularPlan> dcps = new ArrayList<DegreeCurricularPlan>();
        for (DegreeCurricularPlan dcp : degreeCurricularPlans) {
            dcps.add(dcp);
        }
        this.degreeCurricularPlans = dcps;
    }

    public Collection<WorkingStudentSelectionType> getWorkingStudentTypes() {
        return workingStudentTypes;
    }

    public void setWorkingStudentTypes(Collection<WorkingStudentSelectionType> workingStudentTypes) {
        this.workingStudentTypes = workingStudentTypes;
    }

    public Collection<Attends> getAttendsResult() {
        Collection<Attends> attends = new ArrayList<Attends>();
        for (Attends attendRef : attendsResult) {
            attends.add(attendRef);
        }
        return attends;
    }

    public void setAttendsResult(Collection<Attends> atts) {
        ArrayList<Attends> results = new ArrayList<Attends>();
        for (Attends attend : atts) {
            results.add(attend);
        }
        this.attendsResult = results;
    }

    public Map<Integer, Integer> getEnrolmentsNumberMap() {
        return enrolmentsNumberMap;
    }

    public void setEnrolmentsNumberMap(Map<Integer, Integer> enrolmentsNumberMap) {
        this.enrolmentsNumberMap = enrolmentsNumberMap;
    }

    public Predicate<Attends> getFilters() {

        Collection<Predicate<Attends>> filters = new ArrayList<Predicate<Attends>>();

        if (getAttendsStates().size() < StudentAttendsStateType.values().length) {
            filters.add(new InlinePredicate<Attends, Collection<StudentAttendsStateType>>(getAttendsStates()) {

                @Override
                public boolean test(Attends attends) {
                    return getValue().contains(attends.getAttendsStateType());
                }

            });
        }

        if (getWorkingStudentTypes().size() < WorkingStudentSelectionType.values().length) {
            filters.add(new InlinePredicate<Attends, Collection<WorkingStudentSelectionType>>(getWorkingStudentTypes()) {

                @Override
                public boolean test(Attends attends) {
                    return getValue().contains(getWorkingStudentType(attends));
                }

                private WorkingStudentSelectionType getWorkingStudentType(Attends attends) {
                    if (attends.getRegistration().getStudent().hasWorkingStudentStatuteInPeriod(attends.getExecutionInterval())) {
                        return WorkingStudentSelectionType.WORKING_STUDENT;
                    } else {
                        return WorkingStudentSelectionType.NOT_WORKING_STUDENT;
                    }
                }
            });
        }

        if (shifts.size() < getExecutionCourse().getAssociatedShifts().size()) {
            filters.add(new InlinePredicate<Attends, Collection<Shift>>(getShifts()) {

                @Override
                public boolean test(Attends attends) {
                    for (Shift shift : getValue()) {
                        if (shift.getStudentsSet().contains(attends.getRegistration())) {
                            return true;
                        }
                    }
                    return false;
                }
            });
        }

        if (degreeCurricularPlans.size() < getAttendsDegreeCurricularPlans(getExecutionCourse()).size()) {
            filters.add(new InlinePredicate<Attends, Collection<DegreeCurricularPlan>>(getDegreeCurricularPlans()) {

                @Override
                public boolean test(Attends attends) {
                    return getValue().contains(attends.getStudentCurricularPlanFromAttends().getDegreeCurricularPlan());
                }

            });

        }

        return new AndPredicate<Attends>(filters);
    }

    public Group getAttendsGroup() {
        List<Person> persons = new ArrayList<Person>();
        for (Attends attends : getAttendsResult()) {
            persons.add(attends.getRegistration().getStudent().getPerson());
        }
        return Person.convertToUserGroup(persons);
    }

    public String getLabel() {

        String attendTypeValues = "", degreeNameValues = "", shiftsValues = "", workingStudentsValues = "";

        for (StudentAttendsStateType attendType : attendsStates) {
            if (!attendTypeValues.isEmpty()) {
                attendTypeValues += ", ";
            }
            attendTypeValues += getEnumerationResourcesString(attendType.getQualifiedName());
        }

        for (DegreeCurricularPlan degree : degreeCurricularPlans) {
            if (!degreeNameValues.isEmpty()) {
                degreeNameValues += ", ";
            }
            degreeNameValues += degree.getName();
        }

        for (Shift shift : shifts) {
            if (!shiftsValues.isEmpty()) {
                shiftsValues += ", ";
            }
            shiftsValues += shift.getPresentationName();
        }

        for (WorkingStudentSelectionType workingStudent : workingStudentTypes) {
            if (!workingStudentsValues.isEmpty()) {
                workingStudentsValues += ", ";
            }
            workingStudentsValues += getEnumerationResourcesString(workingStudent.getQualifiedName());
        }

        return String.format("%s : %s \n%s : %s \n%s : %s \n%s", getApplicationResourcesString("label.selectStudents"),
                attendTypeValues, getApplicationResourcesString("label.attends.courses"), degreeNameValues,
                getApplicationResourcesString("label.selectShift"), shiftsValues, workingStudentsValues);

    }

    public String getSearchElementsAsParameters() {
        String parameters = "";

        parameters += "&amp;executionCourse=" + getExecutionCourse().getExternalId();
        if (viewPhoto) {
            parameters += "&amp;viewPhoto=true";
        }
        if (getAttendsStates() != null) {
            parameters += "&amp;attendsStates=";
            for (StudentAttendsStateType attendsStateType : getAttendsStates()) {
                parameters += attendsStateType.toString() + ":";
            }
        }
        if (getWorkingStudentTypes() != null) {
            parameters += "&amp;workingStudentTypes=";
            for (WorkingStudentSelectionType workingStudentType : getWorkingStudentTypes()) {
                parameters += workingStudentType.toString() + ":";
            }
        }
        if (getDegreeCurricularPlans() != null) {
            parameters += "&amp;degreeCurricularPlans=";
            for (DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlans()) {
                parameters += degreeCurricularPlan.getExternalId() + ":";
            }
        }
        if (getShifts() != null) {
            parameters += "&amp;shifts=";
            for (Shift shift : getShifts()) {
                parameters += shift.getExternalId() + ":";
            }
        }

        return parameters;
    }

    private static Set<DegreeCurricularPlan> getAttendsDegreeCurricularPlans(final ExecutionCourse executionCourse) {
        final Set<DegreeCurricularPlan> dcps = new HashSet<DegreeCurricularPlan>();
        for (final Attends attends : executionCourse.getAttendsSet()) {
            dcps.add(attends.getStudentCurricularPlanFromAttends().getDegreeCurricularPlan());
        }
        return dcps;
    }

}
