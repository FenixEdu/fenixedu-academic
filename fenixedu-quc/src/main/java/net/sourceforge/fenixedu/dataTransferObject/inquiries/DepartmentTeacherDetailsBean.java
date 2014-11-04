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
package org.fenixedu.academic.dto.inquiries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.inquiries.InquiryGlobalComment;
import org.fenixedu.academic.domain.inquiries.InquiryResult;
import org.fenixedu.academic.domain.inquiries.InquiryResultComment;
import org.fenixedu.academic.domain.inquiries.ResultPersonCategory;

import org.apache.commons.beanutils.BeanComparator;

public class DepartmentTeacherDetailsBean extends GlobalCommentsResultsBean {

    private static final long serialVersionUID = 1L;
    private Person teacher;
    private ExecutionSemester executionSemester;
    private Map<Professorship, List<TeacherShiftTypeResultsBean>> teachersResultsToImproveMap;

    public DepartmentTeacherDetailsBean(Person teacher, ExecutionSemester executionSemester, Person president,
            boolean backToResume) {
        super(null, president, backToResume);
        setTeacher(teacher);
        setExecutionSemester(executionSemester);
        initTeacherResults(teacher);
    }

    private void initTeacherResults(Person teacher) {
        setTeachersResultsMap(new HashMap<Professorship, List<TeacherShiftTypeResultsBean>>());
        setTeachersResultsToImproveMap(new HashMap<Professorship, List<TeacherShiftTypeResultsBean>>());
        for (Professorship teacherProfessorship : getProfessorships()) {
            ArrayList<TeacherShiftTypeResultsBean> teachersResults = new ArrayList<TeacherShiftTypeResultsBean>();
            Collection<InquiryResult> professorshipResults = teacherProfessorship.getInquiryResultsSet();
            if (!professorshipResults.isEmpty()) {
                for (ShiftType shiftType : getShiftTypes(professorshipResults)) {
                    List<InquiryResult> teacherShiftResults = InquiryResult.getInquiryResults(teacherProfessorship, shiftType);
                    if (!teacherShiftResults.isEmpty()) {
                        teachersResults.add(new TeacherShiftTypeResultsBean(teacherProfessorship, shiftType, teacherProfessorship
                                .getExecutionCourse().getExecutionPeriod(), teacherShiftResults, teacher, getPersonCategory()));
                    }
                }
                Collections.sort(teachersResults, new BeanComparator("professorship.person.name"));
                Collections.sort(teachersResults, new BeanComparator("shiftType"));
                if (InquiryResult.hasResultsToImprove(teacherProfessorship)) {
                    getTeachersResultsToImproveMap().put(teacherProfessorship, teachersResults);
                } else {
                    getTeachersResultsMap().put(teacherProfessorship, teachersResults);
                }
            }
        }
    }

    public List<InquiryResultComment> getAllTeacherComments() {
        List<InquiryResultComment> resultComments = new ArrayList<InquiryResultComment>();
        for (InquiryGlobalComment globalComment : getExecutionSemester().getInquiryGlobalCommentsSet()) {
            if (getTeacher() == globalComment.getTeacher()) {
                resultComments.addAll(globalComment.getInquiryResultCommentsSet());
            }
        }
        Collections.sort(resultComments, new BeanComparator("person.name"));
        return resultComments;
    }

    @Override
    protected InquiryGlobalComment createGlobalComment() {
        return new InquiryGlobalComment(getTeacher(), getExecutionSemester());
    }

    @Override
    protected ResultPersonCategory getPersonCategory() {
        return ResultPersonCategory.DEPARTMENT_PRESIDENT;
    }

    @Override
    protected List<Professorship> getProfessorships() {
        if (getTeacher() == null) {
            return new ArrayList<Professorship>();
        }
        return getTeacher().getProfessorships(getExecutionSemester());
    }

    @Override
    public InquiryGlobalComment getInquiryGlobalComment() {
        return InquiryGlobalComment.getInquiryGlobalComment(getTeacher(), getExecutionSemester());
    }

    public void setTeacher(Person teacher) {
        this.teacher = teacher;
    }

    public Person getTeacher() {
        return teacher;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }

    public void setTeachersResultsToImproveMap(Map<Professorship, List<TeacherShiftTypeResultsBean>> teachersResultsToImproveMap) {
        this.teachersResultsToImproveMap = teachersResultsToImproveMap;
    }

    public Map<Professorship, List<TeacherShiftTypeResultsBean>> getTeachersResultsToImproveMap() {
        return teachersResultsToImproveMap;
    }
}
