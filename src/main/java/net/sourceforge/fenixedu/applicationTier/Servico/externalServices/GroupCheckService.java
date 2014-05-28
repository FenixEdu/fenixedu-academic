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
package net.sourceforge.fenixedu.applicationTier.Servico.externalServices;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.accessControl.StudentGroup;
import net.sourceforge.fenixedu.domain.accessControl.TeacherGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author naat
 * 
 */
public class GroupCheckService {

    private static final String PAIR_SEPARATOR = ";";

    private static final String NAME_VALUE_SEPARATOR = "=";

    private enum QueryType {
        DEPARTMENT, DEGREE, CURRICULAR_COURSE, EXECUTION_COURSE, ROLE
    }

    private static class GroupCheckQuery {
        public String username;

        public RoleType roleType;

        public QueryType queryType;

        public String unitFullPath;

        public String year;

        public Integer semester;

    }

    /**
     * Checks if the user belongs to the group specified in query
     * 
     * Generic query format:
     * user=ISTnnnn;role=ROLE;type=TYPE;unit=IST.<TheUnit>{
     * .<SubUnit>}[;year=<TheYear>][;semester=<TheSemester>]
     * 
     * Available queries:
     * user=ISTnnnn;role=ROLE;type=DEPARTMENT;unit=IST.<DepartmentAcronym
     * >[;year=<TheYear>]
     * user=ISTnnnn;role=ROLE;type=DEGREE;unit=IST.<DegreeAcronym> (Uses the
     * active degree curricular plans)
     * user=ISTnnnn;role=ROLE;type=CURRICULAR_COURSE
     * ;unit=IST.<DegreeAcronym>.<CurricularCourseAcronym
     * >[;year=<TheYear>;semester=<TheSemester>]
     * user=ISTnnnn;role=ROLE;type=EXECUTION_COURSE
     * ;unit=IST.<DegreeAcronym>.<CurricularCourseAcronym
     * >[;year=<TheYear>;semester=<TheSemester>]
     * 
     * Queries that will be available in future:
     * user=ISTnnnn;role=ROLE;type=SCIENTIFIC_AREA
     * ;unit=IST.<TheUnit>{.<SubUnit>}[;year=<TheYear>][;semester=<TheSemester>]
     * user
     * =ISTnnnn;role=ROLE;type=SECTION;unit=IST.<TheUnit>{.<SubUnit>}[;year=<
     * TheYear>][;semester=<TheSemester>]
     * 
     */
    @Atomic
    public static Boolean run(String query) throws FenixServiceException {

        GroupCheckQuery groupCheckQuery = parseQuery(query);

        Person person = Person.readPersonByUsername(groupCheckQuery.username);

        if (person == null) {
            return false;
        }

        if (groupCheckQuery.queryType == QueryType.DEPARTMENT) {
            return checkDepartmentGroup(person, groupCheckQuery);
        } else if (groupCheckQuery.queryType == QueryType.DEGREE) {
            return checkDegreeGroup(person, groupCheckQuery);
        } else if (groupCheckQuery.queryType == QueryType.CURRICULAR_COURSE) {
            return checkCurricularCourseGroup(person, groupCheckQuery);
        } else if (groupCheckQuery.queryType == QueryType.EXECUTION_COURSE) {
            return checkExecutionCourseGroup(person, groupCheckQuery);
        } else if (groupCheckQuery.queryType == QueryType.ROLE) {
            return checkRoleGroup(person, groupCheckQuery);
        } else {
            throw new NonExistingServiceException();
        }

    }

    /**
     * Checks if person has role.
     * 
     * Accepted roles: TEACHER and EMPLOYEE
     * 
     * @throws NonExistingServiceException
     * 
     * 
     */
    private static Boolean checkRoleGroup(Person person, GroupCheckQuery groupCheckQuery) throws NonExistingServiceException {
        if (groupCheckQuery.roleType == RoleType.TEACHER || groupCheckQuery.roleType == RoleType.EMPLOYEE) {
            return RoleGroup.get(groupCheckQuery.roleType).isMember(person.getUser());
        } else {
            throw new NonExistingServiceException();
        }

    }

    /**
     * Checks if person belongs to curricular course. The unit path format
     * should be:
     * IST{.<SubUnitAcronym>}.<DegreeAcronym>.<CurricularCourseAcronym>
     * 
     * Accepted roles: STUDENT and TEACHER
     * 
     * 
     * @param person
     * @param groupCheckQuery
     * @return
     * @throws NonExistingServiceException
     * @throws ExcepcaoPersistencia
     */
    private static Boolean checkExecutionCourseGroup(Person person, GroupCheckQuery groupCheckQuery)
            throws NonExistingServiceException {
        String[] unitAcronyms = groupCheckQuery.unitFullPath.split("\\.");

        if (groupCheckQuery.roleType != RoleType.TEACHER && groupCheckQuery.roleType != RoleType.STUDENT) {
            throw new NonExistingServiceException();
        }

        Degree degree = getDegree(unitAcronyms);
        for (DegreeCurricularPlan degreeCurricularPlan : degree.getActiveDegreeCurricularPlans()) {

            ExecutionSemester executionSemester = getExecutionPeriod(groupCheckQuery.year, groupCheckQuery.semester);

            CurricularCourse curricularCourse = degreeCurricularPlan.getCurricularCourseByAcronym(unitAcronyms[4]);

            if (curricularCourse != null) {
                List<ExecutionCourse> executionCourses = curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester);

                for (ExecutionCourse executionCourse : executionCourses) {
                    Group group;

                    if (groupCheckQuery.roleType == RoleType.TEACHER) {
                        group = TeacherGroup.get(executionCourse);
                    } else {
                        group = StudentGroup.get(executionCourse);
                    }

                    if (group.isMember(person.getUser())) {
                        return true;
                    }
                }
            }
        }

        return false;

    }

    /**
     * Checks if person belongs to curricular course. The unit path format
     * should be:
     * IST{.<SubUnitAcronym>}.<DegreeAcronym>.<CurricularCourseAcronym>
     * 
     * Accepted roles: STUDENT
     * 
     * @param person
     * @param groupCheckQuery
     * @return
     * @throws NonExistingServiceException
     * @throws ExcepcaoPersistencia
     */
    private static Boolean checkCurricularCourseGroup(Person person, GroupCheckQuery groupCheckQuery)
            throws NonExistingServiceException {
        final String[] unitAcronyms = groupCheckQuery.unitFullPath.split("\\.");

        if (groupCheckQuery.roleType != RoleType.STUDENT) {
            throw new NonExistingServiceException();
        }

        for (final DegreeCurricularPlan degreeCurricularPlan : getDegree(unitAcronyms).getActiveDegreeCurricularPlans()) {

            final CurricularCourse curricularCourse = degreeCurricularPlan.getCurricularCourseByAcronym(unitAcronyms[4]);

            if (curricularCourse != null) {
                List<Enrolment> enrolments =
                        curricularCourse.getEnrolmentsByExecutionPeriod(getExecutionPeriod(groupCheckQuery.year,
                                groupCheckQuery.semester));
                for (Enrolment enrolment : enrolments) {
                    if (enrolment.getStudentCurricularPlan().getRegistration().getPerson().equals(person)) {
                        return true;
                    }
                }
            }
        }

        return false;

    }

    /**
     * Checks if person belongs to degree. The unit path format should be:
     * IST.<DegreeAcronym>
     * 
     * Accepted roles: TEACHER and STUDENT
     * 
     * @param person
     * @param groupCheckQuery
     * @return
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException
     */
    private static Boolean checkDegreeGroup(Person person, GroupCheckQuery groupCheckQuery) throws NonExistingServiceException {
        String[] unitAcronyms = groupCheckQuery.unitFullPath.split("\\.");

        if (groupCheckQuery.roleType != RoleType.TEACHER && groupCheckQuery.roleType != RoleType.STUDENT) {
            throw new NonExistingServiceException();
        }

        Degree degree = getDegree(unitAcronyms);

        Group group;
        if (groupCheckQuery.roleType == RoleType.STUDENT) {
            group = StudentGroup.get(degree, null);
        } else {
            group = TeacherGroup.get(degree);
        }

        return group.isMember(person.getUser());

    }

    private static Degree getDegree(String[] unitAcronyms) throws NonExistingServiceException {
        Unit unit = getUnit(unitAcronyms, 3);

        if (!unit.isDegreeUnit()) {
            throw new NonExistingServiceException();
        }

        Degree degree = unit.getDegree();
        return degree;
    }

    private static Department getDepartment(String[] unitAcronyms) throws NonExistingServiceException {
        final Unit unit = getUnit(unitAcronyms, 2);
        if (!unit.isDepartmentUnit()) {
            throw new NonExistingServiceException();
        }

        return unit.getDepartment();
    }

    private static Unit getUnit(String[] unitAcronyms, int maxIndex) throws NonExistingServiceException {
        Unit unit = UnitUtils.readInstitutionUnit();
        if (unit == null || StringUtils.isEmpty(unit.getAcronym()) || !unit.getAcronym().equals(unitAcronyms[0])) {
            throw new NonExistingServiceException();
        }

        for (int i = 1; i <= maxIndex; i++) {
            unit = unit.getChildUnitByAcronym(unitAcronyms[i]);
            if (unit == null) {
                throw new NonExistingServiceException();
            }
        }

        return unit;
    }

    /**
     * Checks if person belongs to deparment on a specified execution year. The
     * unit path format should be: IST.<DepartmentAcronym>
     * 
     * Accepted roles: STUDENT, TEACHER and EMPLOYEE
     * 
     * @param person
     * @param groupCheckQuery
     * @return
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException
     */
    private static Boolean checkDepartmentGroup(Person person, GroupCheckQuery groupCheckQuery)
            throws NonExistingServiceException {

        final String[] unitAcronyms = groupCheckQuery.unitFullPath.split("\\.");
        if (groupCheckQuery.roleType != RoleType.TEACHER && groupCheckQuery.roleType != RoleType.STUDENT
                && groupCheckQuery.roleType != RoleType.EMPLOYEE) {
            throw new NonExistingServiceException();
        }

        if (groupCheckQuery.roleType == RoleType.TEACHER) {
            return TeacherGroup.get(getDepartment(unitAcronyms), getExecutionYear(groupCheckQuery.year)).isMember(
                    person.getUser());
        } else if (groupCheckQuery.roleType == RoleType.EMPLOYEE) {
            if (person != null && person.hasEmployee()) {
                final Department lastDepartmentWorkingPlace =
                        person.getEmployee().getLastDepartmentWorkingPlace(
                                getExecutionYear(groupCheckQuery.year).getBeginDateYearMonthDay(),
                                getExecutionYear(groupCheckQuery.year).getEndDateYearMonthDay());
                return (lastDepartmentWorkingPlace != null && lastDepartmentWorkingPlace.equals(getDepartment(unitAcronyms)));
            }
            return false;
        } else {
            if (person != null && person.hasStudent()) {
                for (final Registration registration : person.getStudent().getRegistrationsSet()) {
                    for (final Enrolment enrolment : registration.getLastStudentCurricularPlan().getEnrolmentsByExecutionYear(
                            getExecutionYear(groupCheckQuery.year))) {
                        if (enrolment.getCurricularCourse().hasCompetenceCourse()) {
                            final CompetenceCourse competenceCourse = enrolment.getCurricularCourse().getCompetenceCourse();
                            if (competenceCourse.getDepartmentsSet().contains(getDepartment(unitAcronyms))) {
                                return true;

                            }

                            if (competenceCourse.hasDepartmentUnit()
                                    && competenceCourse.getDepartmentUnit().getDepartment() == getDepartment(unitAcronyms)) {
                                return true;
                            }
                        }
                    }

                }
            }
            return false;
        }

    }

    private static ExecutionSemester getExecutionPeriod(String year, Integer semester) throws NonExistingServiceException {

        if (year != null && semester != null) {
            return ExecutionSemester.readBySemesterAndExecutionYear(semester, year);
        } else if (year == null && semester == null) {
            return ExecutionSemester.readActualExecutionSemester();
        } else {
            throw new NonExistingServiceException();
        }

    }

    private static ExecutionYear getExecutionYear(String year) {
        if (year != null) {
            return ExecutionYear.readExecutionYearByName(year);
        } else {
            return ExecutionYear.readCurrentExecutionYear();
        }

    }

    private static GroupCheckQuery parseQuery(String query) {
        GroupCheckQuery groupCheckQuery = new GroupCheckQuery();

        String[] nameValuePairs = query.split(PAIR_SEPARATOR);

        for (String nameValuePair : nameValuePairs) {
            String[] nameValueParts = nameValuePair.split(NAME_VALUE_SEPARATOR);
            String fieldName = nameValueParts[0];
            String fieldValue = nameValueParts[1];

            if (fieldName.equals("user")) {
                groupCheckQuery.username = fieldValue;
            } else if (fieldName.equals("role")) {
                groupCheckQuery.roleType = RoleType.valueOf(fieldValue);
            } else if (fieldName.equals("type")) {
                groupCheckQuery.queryType = QueryType.valueOf(fieldValue);
            } else if (fieldName.equals("unit")) {
                groupCheckQuery.unitFullPath = fieldValue;
            } else if (fieldName.equals("year")) {
                groupCheckQuery.year = fieldValue;
            } else if (fieldName.equals("semester")) {
                groupCheckQuery.semester = Integer.valueOf(fieldValue);
            }
        }

        return groupCheckQuery;
    }

}