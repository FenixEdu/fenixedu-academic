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
package net.sourceforge.fenixedu.dataTransferObject.teacher.tutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher.TeacherDepartmentDegreesProvider;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher.TutorshipEntryExecutionYearProvider;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher.TutorshipEntryExecutionYearProvider.TutorshipEntryExecutionYearProviderByTeacher;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher.TutorshipEntryExecutionYearProvider.TutorshipEntryExecutionYearProviderForSingleStudent;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher.TutorshipMonitoringExecutionYearProvider;

public class StudentsPerformanceInfoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    public static class StudentsPerformanceInfoNullEntryYearBean extends StudentsPerformanceInfoBean {
        private static final long serialVersionUID = 1L;

        public static StudentsPerformanceInfoNullEntryYearBean create(Teacher teacher) {
            StudentsPerformanceInfoNullEntryYearBean bean = new StudentsPerformanceInfoNullEntryYearBean();
            bean.setTeacher(teacher);
            bean.setStudentsEntryYear(null);
            return bean;
        }

        @Override
        public void setTeacher(Teacher teacher) {
            this.teacher = teacher;
        }

        @Override
        public void setStudentsEntryYear(ExecutionYear studentsEntryYear) {
            if (!checkStudentsEntryYearMatchesTeacher(studentsEntryYear)) {
                return;
            }
            this.studentsEntryYear = studentsEntryYear;
        }

        protected boolean checkStudentsEntryYearMatchesTeacher(ExecutionYear studentsEntryYear) {
            List<ExecutionYear> entryYears = TutorshipEntryExecutionYearProviderByTeacher.getExecutionYears(this);
            return ((studentsEntryYear == null) || entryYears.contains(studentsEntryYear));
        }

    }

    protected Teacher teacher;
    protected Student student;
    protected Degree degree;
    protected ExecutionYear studentsEntryYear;
    protected ExecutionYear currentMonitoringYear;
    protected Integer degreeCurricularPeriod = 5; // default
    protected boolean activeTutorships;

    public StudentsPerformanceInfoBean() {
        degree = null;
        studentsEntryYear = null;
        currentMonitoringYear = null;
    }

    public static StudentsPerformanceInfoBean create(Teacher teacher) {
        StudentsPerformanceInfoBean bean = new StudentsPerformanceInfoBean();
        bean.setTeacher(teacher);
        bean.initActiveTutorships();
        return bean;
    }

    public static StudentsPerformanceInfoBean create(Student student) {
        StudentsPerformanceInfoBean bean = new StudentsPerformanceInfoBean();
        bean.setStudent(student);
        return bean;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
        refreshStudentsEntryYear();
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
        refreshDegree();
    }

    protected void refreshDegree() {
        Set<Degree> degrees = TeacherDepartmentDegreesProvider.getDegrees(this);
        if (!checkDegreeMatchesTeacher(getDegree())) {
            setDegree(degrees.iterator().next());
        }
    }

    protected boolean checkDegreeMatchesTeacher(Degree degree) {
        return ((degree != null) && (TeacherDepartmentDegreesProvider.getDegrees(this).contains(degree)));
    }

    public Degree getDegree() {
        return (degree);
    }

    public void setDegree(Degree degree) {
        if (!checkDegreeMatchesTeacher(degree)) {
            return;
        }
        this.degree = degree;
        this.degreeCurricularPeriod =
                (degree != null ? (int) degree.getDegreeType().getAcademicPeriod().getWeight() : getDegreeCurricularPeriod());
        refreshStudentsEntryYear();
    }

    protected void refreshStudentsEntryYear() {
        List<ExecutionYear> entryYears;
        if (getStudent() != null) {
            if ((!getTutorshipsFromStudent().isEmpty()) && (!checkStudentsEntryYearMatchesStudent(getStudentsEntryYear()))) {
                entryYears = TutorshipEntryExecutionYearProviderForSingleStudent.getExecutionYears(this);
                setStudentsEntryYear(entryYears.iterator().next());
            }
        } else {
            if ((!getTutorships().isEmpty()) && (!checkStudentsEntryYearMatchesDegree(getStudentsEntryYear()))) {
                entryYears = TutorshipEntryExecutionYearProvider.getExecutionYears(this);
                setStudentsEntryYear(entryYears.iterator().next());
            }
        }
    }

    protected boolean checkStudentsEntryYearMatchesStudent(ExecutionYear studentsEntryYear) {
        List<ExecutionYear> entryYears = TutorshipEntryExecutionYearProviderForSingleStudent.getExecutionYears(this);
        return ((studentsEntryYear != null) && entryYears.contains(studentsEntryYear));
    }

    protected boolean checkStudentsEntryYearMatchesDegree(ExecutionYear studentsEntryYear) {
        List<ExecutionYear> entryYears = TutorshipEntryExecutionYearProvider.getExecutionYears(this);
        return ((studentsEntryYear != null) && entryYears.contains(studentsEntryYear));
    }

    public ExecutionYear getStudentsEntryYear() {
        return (studentsEntryYear);
    }

    public void setStudentsEntryYear(ExecutionYear studentsEntryYear) {
        if (getStudent() != null) {
            if (!checkStudentsEntryYearMatchesStudent(studentsEntryYear)) {
                return;
            }
        } else {
            if (!checkStudentsEntryYearMatchesDegree(studentsEntryYear)) {
                return;
            }
        }
        this.studentsEntryYear = studentsEntryYear;
        refreshCurrentMonitoringYear();
    }

    protected void refreshCurrentMonitoringYear() {
        List<ExecutionYear> monitoringYears = TutorshipMonitoringExecutionYearProvider.getExecutionYears(this);
        if (!checkCurrentMonitoringYearMatchesStudentsEntryYear(getCurrentMonitoringYear())) {
            setCurrentMonitoringYear(monitoringYears.iterator().next());
        }
    }

    protected boolean checkCurrentMonitoringYearMatchesStudentsEntryYear(ExecutionYear currentMonitoringYear) {
        List<ExecutionYear> monitoringYears = TutorshipMonitoringExecutionYearProvider.getExecutionYears(this);
        return ((currentMonitoringYear != null) && monitoringYears.contains(currentMonitoringYear));
    }

    public ExecutionYear getCurrentMonitoringYear() {
        return (currentMonitoringYear);
    }

    public void setCurrentMonitoringYear(ExecutionYear currentMonitoringYear) {
        if (!checkCurrentMonitoringYearMatchesStudentsEntryYear(currentMonitoringYear)) {
            return;
        }
        this.currentMonitoringYear = currentMonitoringYear;
    }

    public void setStudentsEntryYearFromList(List<ExecutionYear> studentsEntryYears) {
        if (!studentsEntryYears.contains(this.studentsEntryYear)) {
            setStudentsEntryYear(studentsEntryYears.iterator().next());
        }
    }

    public void setStudentsEntryYearFromSet(Set<ExecutionYear> studentsEntryYears) {
        if (!studentsEntryYears.contains(this.studentsEntryYear)) {
            setStudentsEntryYear(studentsEntryYears.iterator().next());
        }
    }

    public void setCurrentMonitoringYearFromList(List<ExecutionYear> monitoringYears) {
        if (!monitoringYears.contains(this.currentMonitoringYear)) {
            setCurrentMonitoringYear(monitoringYears.iterator().next());
        }
    }

    public Integer getDegreeCurricularPeriod() {
        return degreeCurricularPeriod;
    }

    public void setActiveTutorships(boolean activeTutorships) {
        this.activeTutorships = activeTutorships;
    }

    public boolean getActiveTutorships() {
        return this.activeTutorships;
    }

    public List<Tutorship> getTutorships() {
        List<Tutorship> result = new ArrayList<Tutorship>();
        result.addAll(getTeacher().getActiveTutorships());
        result.addAll(getTeacher().getPastTutorships());
        return result;
    }

    public List<Tutorship> getTutorshipsFromStudent() {
        List<Tutorship> result = new ArrayList<Tutorship>();
        result.addAll(getStudent().getActiveTutorships());
        return result;
    }

    private void initActiveTutorships() {
        if (getTeacher().getNumberOfActiveTutorships() != 0) {
            setActiveTutorships(true);
        } else {
            setActiveTutorships(false);
        }
    }
}
