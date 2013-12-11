package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupDifference;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.collections.Predicate;

public class TeacherServiceDistribution extends TeacherServiceDistribution_Base {

    private TeacherServiceDistribution() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public TeacherServiceDistribution(TSDProcessPhase tsdProcessPhase, String name, TeacherServiceDistribution parent,
            List<TSDTeacher> tsdTeacherList, List<TSDCourse> courseList, Group courseValuationGroup, Group teacherValuationGroup,
            Group courseManagementGroup, Group teacherManagementGroup) {
        this();

        if (tsdProcessPhase == null) {
            throw new DomainException("TSDProcessPhase.required");
        }

        this.setTSDProcessPhase(tsdProcessPhase);
        this.setName(name);
        this.setParent(parent);
        this.getTSDTeachers().addAll(tsdTeacherList);
        this.getTSDCourses().addAll(courseList);
        this.setTeachersManagementGroup(teacherManagementGroup);
        this.setCoursesManagementGroup(courseManagementGroup);
        this.setCoursesValuationManagers(courseValuationGroup);
        this.setTeachersValuationManagers(teacherValuationGroup);
    }

    public Boolean getIsRoot() {
        return getParent() == null;
    }

    public TeacherServiceDistribution getRootTSD() {
        if (getIsRoot()) {
            return this;
        } else {
            return getParent().getRootTSD();
        }
    }

    public void delete() {
        for (TSDCourse tsdCourse : getTSDCourses()) {
            if (tsdCourse.getTeacherServiceDistributionsSet().size() == 1) {
                tsdCourse.delete();
            } else {
                removeTSDCourses(tsdCourse);
            }
        }

        for (TSDTeacher teacher : getTSDTeachers()) {
            if (teacher.getTeacherServiceDistributionsSet().size() == 1) {
                teacher.delete();
            } else {
                removeTSDTeachers(teacher);
            }
        }

        setParent(null);
        setTSDProcessPhase(null);

        for (TeacherServiceDistribution childTSD : getChilds()) {
            removeChilds(childTSD);
            childTSD.delete();
        }

        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public Set<CompetenceCourse> getCompetenceCourses() {
        Set<CompetenceCourse> courseList = new HashSet<CompetenceCourse>();
        CompetenceCourse course = null;

        for (TSDCourse tsdCourse : getTSDCourses()) {
            if ((course = tsdCourse.getCompetenceCourse()) != null) {
                courseList.add(course);
            }
        }

        return courseList;
    }

    public List<TSDCourse> getTSDCompetenceAndVirtualCourses() {
        return getTSDCompetenceAndVirtualCoursesByExecutionPeriod(getTSDProcessPhase().getTSDProcess().getExecutionPeriods());
    }

    public List<TSDCourse> getTSDCompetenceAndVirtualCoursesByExecutionPeriod(ExecutionSemester period) {
        List<ExecutionSemester> periods = new ArrayList<ExecutionSemester>();
        periods.add(period);

        return getTSDCompetenceAndVirtualCoursesByExecutionPeriod(periods);
    }

    private List<TSDCourse> getTSDCompetenceAndVirtualCoursesByExecutionPeriod(Collection<ExecutionSemester> periods) {
        Set<TSDCourse> courseList = new HashSet<TSDCourse>();
        Set<CompetenceCourse> competenceCourseSet = new HashSet<CompetenceCourse>();

        for (ExecutionSemester period : periods) {
            for (TSDCourse tsdCourse : getTSDCoursesByExecutionPeriod(period)) {
                if (tsdCourse instanceof TSDCompetenceCourse && tsdCourse.getCompetenceCourse().isBolonha()) {
                    courseList.add(tsdCourse);
                    competenceCourseSet.add(tsdCourse.getCompetenceCourse());
                }
                if (tsdCourse instanceof TSDVirtualCourseGroup) {
                    courseList.add(tsdCourse);
                }
            }
        }

        return new ArrayList<TSDCourse>(courseList);
    }

    public List<TSDCourse> getTSDCoursesByCompetenceCourse(CompetenceCourse course) {
        List<TSDCourse> courseList = new ArrayList<TSDCourse>();

        for (TSDCourse tsdCourse : getTSDCourses()) {
            CompetenceCourse competenceCourse = tsdCourse.getCompetenceCourse();
            if (competenceCourse != null && competenceCourse.equals(course)) {
                courseList.add(tsdCourse);
            }
        }

        return courseList;
    }

    public List<TSDCourse> getTSDCoursesByExecutionPeriod(ExecutionSemester period) {
        List<TSDCourse> courseList = new ArrayList<TSDCourse>();

        for (TSDCourse tsdCourse : getTSDCourses()) {
            if (tsdCourse.getExecutionPeriod().equals(period)) {
                courseList.add(tsdCourse);
            }
        }

        return courseList;
    }

    @SuppressWarnings("unchecked")
    public List<CompetenceCourse> getCompetenceCoursesByExecutionPeriod(final ExecutionSemester executionSemester) {
        List<CompetenceCourse> competenceCourseList = new ArrayList<CompetenceCourse>();

        competenceCourseList.addAll(CollectionUtils.select(getCompetenceCourses(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                CompetenceCourse competenceCourse = (CompetenceCourse) arg0;

                return competenceCourse.getCurricularCoursesWithActiveScopesInExecutionPeriod(executionSemester).size() > 0;
            }
        }));

        return competenceCourseList;
    }

    public TSDTeacher getTSDTeacherByTeacher(final Teacher teacher) {
        return (TSDTeacher) CollectionUtils.find(getTSDTeachers(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                if (arg0 instanceof TSDRealTeacher) {
                    TSDRealTeacher tsdTeacher = (TSDRealTeacher) arg0;

                    return tsdTeacher.getTeacher() == teacher;
                } else {
                    return false;
                }
            }
        });
    }

    public void removeTSDTeacherFromAllChilds(TSDTeacher tsdTeacher) {
        removeTSDTeachers(tsdTeacher);
        for (TeacherServiceDistribution childGrouping : getChilds()) {
            childGrouping.removeTSDTeacherFromAllChilds(tsdTeacher);
        }

    }

    public void removeTSDCourseFromAllChilds(TSDCourse course) {
        removeTSDCourses(course);
        for (TeacherServiceDistribution childGrouping : getChilds()) {
            childGrouping.removeTSDCourseFromAllChilds(course);
        }
    }

    public void addTSDCourseToTSDTree(TSDCourse course) {
        getRootTSD().addTSDCourseToChildsTree(course);
    }

    private void addTSDCourseToChildsTree(TSDCourse course) {
        if (getCompetenceCourses().contains(course.getCompetenceCourse())) {
            addTSDCourses(course);

            for (TeacherServiceDistribution childTSD : getChilds()) {
                childTSD.addTSDCourseToChildsTree(course);
            }
        }
    }

    protected void addTSDCourseToParentTree(TSDCourse course) {
        addTSDCourses(course);

        if (getParent() != null) {
            getParent().addTSDCourseToParentTree(course);
        }
    }

    public void mergeWithGrouping(TeacherServiceDistribution peerGrouping) {

        if (peerGrouping == this) {
            return;
        }

        Set<TSDTeacher> mergedTSDTeachers = new HashSet<TSDTeacher>(this.getTSDTeachers());
        mergedTSDTeachers.addAll(peerGrouping.getTSDTeachers());

        Set<TSDCourse> mergedCourses = new HashSet<TSDCourse>(this.getTSDCourses());
        mergedCourses.addAll(peerGrouping.getTSDCourses());

        this.getTSDCoursesSet().addAll(mergedCourses);
        this.getTSDTeachersSet().addAll(mergedTSDTeachers);

        if (!(getParent().getTSDTeachers().containsAll(this.getTSDTeachers()) && getParent().getTSDCourses().containsAll(
                this.getTSDCourses()))) {
            this.setParent(getRootTSD());
        }
    }

    public List<Teacher> getDepartmentTeachersNotInGrouping(Department department) {
        TSDProcess distribution = this.getTSDProcessPhase().getTSDProcess();
        List<Teacher> departmentTeachers =
                department.getAllTeachers(distribution.getFirstExecutionPeriod().getBeginDateYearMonthDay(), distribution
                        .getLastExecutionPeriod().getEndDateYearMonthDay());

        List<Teacher> teachersList = new ArrayList<Teacher>();
        for (Teacher departmentTeacher : departmentTeachers) {
            if (this.getTSDTeacherByTeacher(departmentTeacher) == null) {
                teachersList.add(departmentTeacher);
            }
        }

        return teachersList;
    }

    public List<CompetenceCourse> getDepartmentCompetenceCoursesNotInGrouping(Department department) {
        TSDProcess distribution = this.getTSDProcessPhase().getTSDProcess();
        Set<CompetenceCourse> departmentCourses = distribution.getCompetenceCoursesByDepartment(department);
        Set<CompetenceCourse> groupCourses = getCompetenceCourses();
        List<CompetenceCourse> availableCoursesList = new ArrayList<CompetenceCourse>();

        for (CompetenceCourse course : departmentCourses) {
            if (!groupCourses.contains(course)) {
                availableCoursesList.add(course);
            }
        }

        return availableCoursesList;
    }

    @SuppressWarnings("unchecked")
    public List<TSDCourse> getCompetenceCoursesWithoutActiveTSDCourses(List<ExecutionSemester> executionPeriodList) {
        Set<CompetenceCourse> tsdCoursesList = getCompetenceCourses();
        Set<CompetenceCourse> activeCoursesSet = new HashSet<CompetenceCourse>();
        List<TSDCourse> returnList = new ArrayList<TSDCourse>();

        for (ExecutionSemester period : executionPeriodList) {
            for (CompetenceCourse course : tsdCoursesList) {
                for (TSDCourse tsdCourse : getTSDCoursesByCompetenceCourse(course)) {
                    if (tsdCourse.getExecutionPeriod().equals(period) && tsdCourse.getIsActive()) {
                        activeCoursesSet.add(course);
                    }
                }
            }
        }

        for (ExecutionSemester period : executionPeriodList) {
            for (TSDCourse tsdCourse : getTSDCoursesByExecutionPeriod(period)) {
                if (tsdCourse instanceof TSDCompetenceCourse) {
                    if (!activeCoursesSet.contains(tsdCourse.getCompetenceCourse())) {
                        returnList.add(tsdCourse);
                        activeCoursesSet.add(tsdCourse.getCompetenceCourse());
                    }
                }
            }
        }

        return returnList;
    }

    private boolean isUserMemberOfGroup(Person person, Group group) {
        return group == null ? false : group.isMember(person);
    }

    public boolean isMemberOfCoursesValuationManagers(Person person) {
        return isUserMemberOfGroup(person, getCoursesValuationManagers());
    }

    public boolean isMemberOfTeachersValuationManagers(Person person) {
        return isUserMemberOfGroup(person, getTeachersValuationManagers());
    }

    public boolean isMemberOfCoursesManagementGroup(Person person) {
        return isUserMemberOfGroup(person, getCoursesManagementGroup());
    }

    public boolean isMemberOfTeachersManagementGroup(Person person) {
        return isUserMemberOfGroup(person, getTeachersManagementGroup());
    }

    public boolean hasCourseValuationPermission(Person person) {
        return getTSDProcessPhase().getTSDProcess().getHasSuperUserPermission(person)
                || isMemberOfCoursesValuationManagers(person)
                || ((getParent() != null) ? getParent().isMemberOfCoursesValuationManagers(person) : false);
    }

    public boolean hasTeachersValuationPermission(Person person) {
        return getTSDProcessPhase().getTSDProcess().getHasSuperUserPermission(person)
                || isMemberOfTeachersValuationManagers(person)
                || ((getParent() != null) ? getParent().isMemberOfTeachersValuationManagers(person) : false);
    }

    public boolean hasCourseManagementPermission(Person person) {
        return getTSDProcessPhase().getTSDProcess().getHasSuperUserPermission(person) || isMemberOfCoursesManagementGroup(person)
                || ((getParent() != null) ? getParent().hasCourseManagementPermission(person) : false);
    }

    public boolean hasTeachersManagementPermission(Person person) {
        return getTSDProcessPhase().getTSDProcess().getHasSuperUserPermission(person)
                || isMemberOfTeachersManagementGroup(person)
                || ((getParent() != null) ? getParent().hasTeachersManagementPermission(person) : false);
    }

    private Group addPersonToGroup(Group group, Person person) {
        return group != null ? new GroupUnion(group, new PersonGroup(person)) : new PersonGroup(person);
    }

    private Group removePersonFromGroup(Group group, Person person) {
        return group != null ? new GroupDifference(group, new PersonGroup(person)) : null;
    }

    public void addCourseValuationPermission(Person person) {
        setCoursesValuationManagers(addPersonToGroup(getCoursesValuationManagers(), person));
    }

    public void removeCourseValuationPermission(Person person) {
        setCoursesValuationManagers(removePersonFromGroup(getCoursesValuationManagers(), person));
    }

    public void addTeacherValuationPermission(Person person) {
        setTeachersValuationManagers(addPersonToGroup(getTeachersValuationManagers(), person));
    }

    public void removeTeacherValuationPermission(Person person) {
        setTeachersValuationManagers(removePersonFromGroup(getTeachersValuationManagers(), person));
    }

    public void addCourseManagersPermission(Person person) {
        setCoursesManagementGroup(addPersonToGroup(getCoursesManagementGroup(), person));
    }

    public void removeCourseManagersPermission(Person person) {
        setCoursesManagementGroup(removePersonFromGroup(getCoursesManagementGroup(), person));
    }

    public void addTeachersManagersPermission(Person person) {
        setTeachersManagementGroup(addPersonToGroup(getTeachersManagementGroup(), person));
    }

    public void removeTeachersManagersPermission(Person person) {
        setTeachersManagementGroup(removePersonFromGroup(getTeachersManagementGroup(), person));
    }

    public Double getAllActiveTSDCourseTotalHoursByExecutionPeriods(List<ExecutionSemester> executionPeriodList) {
        double totalHours = 0d;

        for (TSDCourse tsdCourse : getActiveTSDCourseByExecutionPeriods(executionPeriodList)) {
            totalHours += tsdCourse.getTotalHours();
        }

        return totalHours;
    }

    public List<TSDCourse> getActiveTSDCourses() {
        List<TSDCourse> tsdCourseList = new ArrayList<TSDCourse>();
        for (TSDCourse course : getTSDCourses()) {
            if (course.getIsActive()) {
                tsdCourseList.add(course);
            }
        }

        return tsdCourseList;
    }

    public List<TSDCourse> getActiveTSDCourseByExecutionPeriods(List<ExecutionSemester> executionPeriodList) {
        List<TSDCourse> tsdCourseList = new ArrayList<TSDCourse>();
        for (ExecutionSemester executionSemester : executionPeriodList) {
            for (TSDCourse course : getTSDCourses()) {
                if (course.getExecutionPeriod().equals(executionSemester) && course.getIsActive()) {
                    tsdCourseList.add(course);
                }
            }
        }

        return tsdCourseList;
    }

    public Double getAllActiveTSDCourseTotalStudentsByExecutionPeriods(List<ExecutionSemester> executionPeriodList) {
        double totalStudents = 0d;

        for (TSDCourse tsdCourse : getActiveTSDCourseByExecutionPeriods(executionPeriodList)) {
            totalStudents += tsdCourse.getFirstTimeEnrolledStudents() + tsdCourse.getSecondTimeEnrolledStudents();
        }

        return totalStudents;
    }

    public Integer getAllActiveTSDCourseFirstTimeEnrolledStudentsByExecutionPeriods(List<ExecutionSemester> executionPeriodList) {
        int totalStudents = 0;

        for (TSDCourse tsdCourse : getActiveTSDCourseByExecutionPeriods(executionPeriodList)) {
            totalStudents += tsdCourse.getFirstTimeEnrolledStudents();
        }

        return totalStudents;
    }

    public Integer getAllActiveTSDCourseSecondTimeEnrolledStudentsByExecutionPeriods(List<ExecutionSemester> executionPeriodList) {
        int totalStudents = 0;

        for (TSDCourse tsdCourse : getActiveTSDCourseByExecutionPeriods(executionPeriodList)) {
            totalStudents += tsdCourse.getSecondTimeEnrolledStudents();
        }

        return totalStudents;
    }

    public List<TSDCurricularCourse> getTSDCurricularCoursesWithoutTSDCourseGroup(CompetenceCourse course,
            ExecutionSemester executionSemester) {
        List<TSDCurricularCourse> tsdCurricularCourseList = new ArrayList<TSDCurricularCourse>();

        for (TSDCourse tsdCourse : getTSDCoursesByCompetenceCourse(course)) {
            if (tsdCourse instanceof TSDCurricularCourse
                    && ((TSDCurricularCourse) tsdCourse).getTSDCurricularCourseGroup() == null
                    && tsdCourse.getExecutionPeriod().equals(executionSemester)) {
                tsdCurricularCourseList.add((TSDCurricularCourse) tsdCourse);
            }
        }

        return tsdCurricularCourseList;
    }

    public TSDCourseType getTSDCourseType(TSDCourse course, ExecutionSemester executionSemester) {

        if (course instanceof TSDVirtualCourseGroup) {
            return TSDCourseType.COMPETENCE_COURSE_VALUATION;
        }

        for (TSDCourse tsdCourse : getTSDCoursesByCompetenceCourse(course.getCompetenceCourse())) {
            if (tsdCourse.getIsActive()) {
                if (tsdCourse instanceof TSDCurricularCourse) {
                    return TSDCourseType.CURRICULAR_COURSE_VALUATION;
                }
                if (tsdCourse instanceof TSDCurricularCourseGroup) {
                    return TSDCourseType.CURRICULAR_COURSE_VALUATION_GROUP;
                }

                if (tsdCourse instanceof TSDCompetenceCourse) {
                    return TSDCourseType.COMPETENCE_COURSE_VALUATION;
                }
            }
        }

        return TSDCourseType.NOT_DETERMINED;
    }

    public void setTSDCourseType(CompetenceCourse course, ExecutionSemester executionSemester, TSDCourseType courseType) {

        List<TSDCourse> tsdCoursesList = getTSDCoursesByCompetenceCourse(course);

        for (TSDCourse tsdCourse : tsdCoursesList) {
            tsdCourse.setIsActive(Boolean.FALSE);

            if (courseType == TSDCourseType.CURRICULAR_COURSE_VALUATION && tsdCourse instanceof TSDCurricularCourse) {
                tsdCourse.setIsActive(Boolean.TRUE);
            }

            if (courseType == TSDCourseType.CURRICULAR_COURSE_VALUATION_GROUP && tsdCourse instanceof TSDCurricularCourseGroup) {
                tsdCourse.setIsActive(Boolean.TRUE);
            }

            if (courseType == TSDCourseType.COMPETENCE_COURSE_VALUATION && tsdCourse instanceof TSDCompetenceCourse) {
                tsdCourse.setIsActive(Boolean.TRUE);
            }
        }
    }

    public TSDCompetenceCourse getTSDCompetenceCourse(CompetenceCourse course, ExecutionSemester executionSemester) {
        for (TSDCourse tsdCourse : getTSDCoursesByCompetenceCourse(course)) {
            if (tsdCourse instanceof TSDCompetenceCourse && tsdCourse.getExecutionPeriod().equals(executionSemester)) {
                return (TSDCompetenceCourse) tsdCourse;
            }
        }

        return null;
    }

    public List<TSDCurricularCourse> getTSDCurricularCourses(CompetenceCourse course, ExecutionSemester executionSemester) {
        List<TSDCurricularCourse> tsdCoursesList = new ArrayList<TSDCurricularCourse>();

        for (TSDCourse tsdCourse : getTSDCoursesByCompetenceCourse(course)) {
            if (tsdCourse instanceof TSDCurricularCourse && tsdCourse.getExecutionPeriod().equals(executionSemester)) {
                tsdCoursesList.add((TSDCurricularCourse) tsdCourse);
            }
        }

        return tsdCoursesList;
    }

    public List<TSDCurricularCourseGroup> getTSDCurricularCourseGroups(CompetenceCourse course,
            ExecutionSemester executionSemester) {
        List<TSDCurricularCourseGroup> tsdCoursesList = new ArrayList<TSDCurricularCourseGroup>();

        for (TSDCourse tsdCourse : getTSDCoursesByCompetenceCourse(course)) {
            if (tsdCourse instanceof TSDCurricularCourseGroup && tsdCourse.getExecutionPeriod().equals(executionSemester)) {
                tsdCoursesList.add((TSDCurricularCourseGroup) tsdCourse);
            }
        }

        return tsdCoursesList;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse> getTSDCourses() {
        return getTSDCoursesSet();
    }

    @Deprecated
    public boolean hasAnyTSDCourses() {
        return !getTSDCoursesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution> getChilds() {
        return getChildsSet();
    }

    @Deprecated
    public boolean hasAnyChilds() {
        return !getChildsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher> getTSDTeachers() {
        return getTSDTeachersSet();
    }

    @Deprecated
    public boolean hasAnyTSDTeachers() {
        return !getTSDTeachersSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasTeachersValuationManagers() {
        return getTeachersValuationManagers() != null;
    }

    @Deprecated
    public boolean hasParent() {
        return getParent() != null;
    }

    @Deprecated
    public boolean hasCoursesValuationManagers() {
        return getCoursesValuationManagers() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasTeachersManagementGroup() {
        return getTeachersManagementGroup() != null;
    }

    @Deprecated
    public boolean hasCoursesManagementGroup() {
        return getCoursesManagementGroup() != null;
    }

    @Deprecated
    public boolean hasTSDProcessPhase() {
        return getTSDProcessPhase() != null;
    }

}
