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
package org.fenixedu.academic.ui.faces.bean.departmentMember;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.fenixedu.academic.service.services.commons.ReadCurrentExecutionYear;
import org.fenixedu.academic.service.services.commons.ReadNotClosedExecutionYears;
import org.fenixedu.academic.service.services.department.ReadDepartmentTeachersByDepartmentIDAndExecutionYearID;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.person.function.ReadPersonFunctionsByPersonIDAndExecutionYearID;
import org.fenixedu.academic.service.services.teacher.ReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType;
import org.fenixedu.academic.service.services.teacher.ReadTeacherByOID;
import org.fenixedu.academic.service.services.teacher.advise.ReadTeacherAdvisesByTeacherIDAndAdviseTypeAndExecutionYearID;
import org.fenixedu.academic.dto.InfoExecutionYear;
import org.fenixedu.academic.dto.InfoTeacher;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.teacher.Advise;
import org.fenixedu.academic.domain.teacher.AdviseType;
import org.fenixedu.academic.ui.faces.bean.base.FenixBackingBean;
import org.fenixedu.academic.util.Bundle;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author naat
 * 
 */
public class ViewDepartmentTeachers extends FenixBackingBean {

    private String selectedExecutionYearID;

    private InfoTeacher selectedTeacher;

    private List<ExecutionCourse> lecturedDegreeExecutionCourses;

    private Map<String, String> lecturedDegreeExecutionCourseDegreeNames;

    private List<ExecutionCourse> lecturedMasterDegreeExecutionCourses;

    private Map<String, String> lecturedMasterDegreeExecutionCourseDegreeNames;

    private List<SelectItem> executionYearItems;

    private List<Advise> finalDegreeWorkAdvises;

    private List<PersonFunction> teacherFunctions;

    private static final String ALL_EXECUTION_YEARS_KEY = "label.common.allExecutionYears";

    public String getSelectedTeacherID() {

        return (String) this.getViewState().getAttribute("selectedTeacherID");

    }

    public void setSelectedTeacherID(String selectedTeacherId) {
        this.getViewState().setAttribute("selectedTeacherId", selectedTeacherId);
    }

    public String getSelectedExecutionYearID() throws FenixServiceException {

        if (this.selectedExecutionYearID == null) {

            InfoExecutionYear infoExecutionYear = ReadCurrentExecutionYear.run();

            if (infoExecutionYear != null) {
                this.selectedExecutionYearID = infoExecutionYear.getExternalId();
            } else {
                this.selectedExecutionYearID = null;
            }
        }

        return this.selectedExecutionYearID;
    }

    public void setSelectedExecutionYearID(String selectedExecutionYearID) {
        this.selectedExecutionYearID = selectedExecutionYearID;
    }

    public List<Teacher> getDepartmentTeachers() throws FenixServiceException {
        String executionYearID = getSelectedExecutionYearID();

        List<Teacher> result =
                new ArrayList<Teacher>(
                        ReadDepartmentTeachersByDepartmentIDAndExecutionYearID
                                .runReadDepartmentTeachersByDepartmentIDAndExecutionYearID(getDepartment().getExternalId(),
                                        executionYearID));

        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("teacherId"));

        Collections.sort(result, comparatorChain);

        return result;
    }

    public Department getDepartment() {
        return getUserView().getPerson().getTeacher().getLastDepartment();

    }

    public void selectTeacher(ActionEvent event) throws NumberFormatException, FenixServiceException {

        String teacherId = getRequestParameter("teacherId");

        setSelectedTeacherID(teacherId);
    }

    public InfoTeacher getSelectedTeacher() throws FenixServiceException {

        if (this.selectedTeacher == null) {
            this.selectedTeacher = (InfoTeacher) ReadTeacherByOID.runReadTeacherByOID(getSelectedTeacherID());
        }

        return this.selectedTeacher;
    }

    public List<SelectItem> getExecutionYears() throws FenixServiceException {

        if (this.executionYearItems == null) {

            List<InfoExecutionYear> executionYears = ReadNotClosedExecutionYears.run();

            List<SelectItem> result = new ArrayList<SelectItem>(executionYears.size());
            for (InfoExecutionYear executionYear : executionYears) {
                result.add(new SelectItem(executionYear.getExternalId(), executionYear.getYear()));
            }

            result.add(0, new SelectItem(0, BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, ALL_EXECUTION_YEARS_KEY)));

            this.executionYearItems = result;
        }

        return this.executionYearItems;

    }

    public List<ExecutionCourse> getLecturedDegreeExecutionCourses() throws FenixServiceException {

        if (this.lecturedDegreeExecutionCourses == null && this.getSelectedExecutionYearID() != null) {
            this.lecturedDegreeExecutionCourses = readLecturedExecutionCourses(DegreeType.DEGREE);
            this.lecturedDegreeExecutionCourseDegreeNames =
                    computeExecutionCoursesDegreeAcronyms(this.lecturedDegreeExecutionCourses);

        }

        return this.lecturedDegreeExecutionCourses;
    }

    public List<ExecutionCourse> getLecturedMasterDegreeExecutionCourses() throws FenixServiceException {

        if (this.lecturedMasterDegreeExecutionCourses == null && this.getSelectedExecutionYearID() != null) {
            this.lecturedMasterDegreeExecutionCourses = readLecturedExecutionCourses(DegreeType.MASTER_DEGREE);
            this.lecturedMasterDegreeExecutionCourseDegreeNames =
                    computeExecutionCoursesDegreeAcronyms(this.lecturedMasterDegreeExecutionCourses);

        }

        return this.lecturedMasterDegreeExecutionCourses;
    }

    public Map<String, String> getLecturedDegreeExecutionCourseDegreeNames() {
        return lecturedDegreeExecutionCourseDegreeNames;
    }

    public Map<String, String> getLecturedMasterDegreeExecutionCourseDegreeNames() {
        return lecturedMasterDegreeExecutionCourseDegreeNames;
    }

    private List<ExecutionCourse> readLecturedExecutionCourses(DegreeType degreeType) throws FenixServiceException {

        String executionYearID = getSelectedExecutionYearID();

        List<ExecutionCourse> lecturedExecutionCourses =
                ReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType
                        .runReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType(getSelectedTeacherID(),
                                executionYearID, degreeType);

        List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();

        result.addAll(lecturedExecutionCourses);

        ComparatorChain comparatorChain = new ComparatorChain();
        BeanComparator executionYearComparator = new BeanComparator("executionPeriod.executionYear.year");
        BeanComparator semesterComparator = new BeanComparator("executionPeriod.semester");

        comparatorChain.addComparator(executionYearComparator);
        comparatorChain.addComparator(semesterComparator);

        Collections.sort(result, comparatorChain);

        return result;
    }

    private Map<String, String> computeExecutionCoursesDegreeAcronyms(List<ExecutionCourse> executionCourses) {
        Map<String, String> result = new HashMap<String, String>();

        for (ExecutionCourse executionCourse : executionCourses) {
            String degreeAcronyns = computeDegreeAcronyms(executionCourse);
            result.put(executionCourse.getExternalId(), degreeAcronyns);
        }

        return result;
    }

    private String computeDegreeAcronyms(ExecutionCourse executionCourse) {
        StringBuilder degreeAcronyms = new StringBuilder();

        Collection<CurricularCourse> curricularCourses = executionCourse.getAssociatedCurricularCoursesSet();
        Set<String> processedAcronyns = new HashSet<String>();

        for (CurricularCourse curricularCourse : curricularCourses) {
            String degreeAcronym = curricularCourse.getDegreeCurricularPlan().getDegree().getSigla();

            if (!processedAcronyns.contains(degreeAcronym)) {
                degreeAcronyms.append(degreeAcronym).append(",");
                processedAcronyns.add(degreeAcronym);

            }
        }

        if (degreeAcronyms.toString().endsWith(",")) {
            degreeAcronyms.deleteCharAt(degreeAcronyms.length() - 1);
        }

        return degreeAcronyms.toString();

    }

    public List<Advise> getFinalDegreeWorkAdvises() throws FenixServiceException {

        if (this.finalDegreeWorkAdvises == null && this.getSelectedExecutionYearID() != null) {
            String executionYearID = this.getSelectedExecutionYearID();

            List<Advise> result =
                    new ArrayList<Advise>(
                            ReadTeacherAdvisesByTeacherIDAndAdviseTypeAndExecutionYearID
                                    .runReadTeacherAdvisesByTeacherIDAndAdviseTypeAndExecutionYearID(
                                            AdviseType.FINAL_WORK_DEGREE, getSelectedTeacherID(), executionYearID));

            ComparatorChain comparatorChain = new ComparatorChain();
            BeanComparator executionYearComparator = new BeanComparator("student.number");

            comparatorChain.addComparator(executionYearComparator);

            Collections.sort(result, comparatorChain);

            this.finalDegreeWorkAdvises = result;

        }
        return this.finalDegreeWorkAdvises;
    }

    public List<PersonFunction> getTeacherFunctions() throws FenixServiceException {
        if (this.teacherFunctions == null && this.getSelectedExecutionYearID() != null) {
            String executionYearID = this.getSelectedExecutionYearID();

            Teacher teacher = FenixFramework.getDomainObject(getSelectedTeacherID());

            List<PersonFunction> result =
                    new ArrayList<PersonFunction>(ReadPersonFunctionsByPersonIDAndExecutionYearID.run(teacher.getPerson()
                            .getExternalId(), executionYearID));

            ComparatorChain comparatorChain = new ComparatorChain();
            BeanComparator beginDateComparator = new BeanComparator("beginDate");

            comparatorChain.addComparator(beginDateComparator);

            Collections.sort(result, comparatorChain);

            this.teacherFunctions = result;
        }

        return this.teacherFunctions;

    }

    public void onSelectedExecutionYearChanged(ValueChangeEvent valueChangeEvent) {
        setSelectedExecutionYearID((String) valueChangeEvent.getNewValue());
        this.lecturedDegreeExecutionCourses = null;
        this.lecturedMasterDegreeExecutionCourses = null;
        this.finalDegreeWorkAdvises = null;
        this.teacherFunctions = null;
    }

}