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
package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixCourse;

import org.fenixedu.spaces.domain.Space;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = FenixCourseEvaluation.Test.class, name = "TEST"),
        @JsonSubTypes.Type(value = FenixCourseEvaluation.Exam.class, name = "EXAM"),
        @JsonSubTypes.Type(value = FenixCourseEvaluation.OnlineTest.class, name = "ONLINE_TEST"),
        @JsonSubTypes.Type(value = FenixCourseEvaluation.Project.class, name = "PROJECT"),
        @JsonSubTypes.Type(value = FenixCourseEvaluation.AdHocEvaluation.class, name = "AD_HOC"), })
public abstract class FenixCourseEvaluation {

    public abstract static class WrittenEvaluation extends FenixCourseEvaluation {

        private static FenixSpace.Room noSpaceRoom = new FenixSpace.Room();

        Boolean isInEnrolmentPeriod;
        FenixInterval enrollmentPeriod;
        Boolean isEnrolled;
        Set<FenixCourse> courses;
        Set<FenixSpace.Room> rooms;
        FenixSpace.Room assignedRoom = noSpaceRoom;
        String id;

        protected WrittenEvaluation() {
            super(null, null);
        }

        public WrittenEvaluation(String id, String name, FenixPeriod evaluationPeriod, Boolean isInEnrolmentPeriod,
                String enrollmentPeriodStart, String enrolmentPeriodEnd, List<Space> rooms, Boolean isEnrolled,
                Set<ExecutionCourse> courses) {
            super(name, evaluationPeriod);
            init(id, isInEnrolmentPeriod, enrollmentPeriodStart, enrolmentPeriodEnd, rooms, isEnrolled, courses);
        }

        private void init(String id, Boolean isInEnrolmentPeriod, String enrollmentPeriodStart, String enrolmentPeriodEnd,
                List<Space> rooms, Boolean isEnrolled, Set<ExecutionCourse> courses) {
            setIsInEnrolmentPeriod(isInEnrolmentPeriod);
            setEnrollmentPeriod(new FenixInterval(enrollmentPeriodStart, enrolmentPeriodEnd));
            setRooms(rooms);
            setIsEnrolled(isEnrolled);
            setCourses(FluentIterable.from(courses).transform(new Function<ExecutionCourse, FenixCourse>() {

                @Override
                public FenixCourse apply(ExecutionCourse input) {
                    return new FenixCourse(input);
                }

            }).toSet());
            setId(id);
        }

        public WrittenEvaluation(String id, String name, FenixPeriod evaluationPeriod, Boolean isInEnrolmentPeriod,
                String enrollmentPeriodStart, String enrolmentPeriodEnd, List<Space> rooms, Boolean isEnrolled,
                Set<ExecutionCourse> courses, Space assignedRoom) {
            super(name, evaluationPeriod);
            init(id, isInEnrolmentPeriod, enrollmentPeriodStart, enrolmentPeriodEnd, rooms, isEnrolled, courses);
            setAssignedRoom(assignedRoom);
        }

        public Boolean getIsInEnrolmentPeriod() {
            return isInEnrolmentPeriod;
        }

        public void setIsInEnrolmentPeriod(Boolean isInEnrolmentPeriod) {
            this.isInEnrolmentPeriod = isInEnrolmentPeriod;
        }

        public FenixInterval getEnrollmentPeriod() {
            return enrollmentPeriod;
        }

        public void setEnrollmentPeriod(FenixInterval enrollmentPeriod) {
            this.enrollmentPeriod = enrollmentPeriod;
        }

        public Set<FenixSpace.Room> getRooms() {
            return rooms;
        }

        public void setRooms(List<Space> rooms) {
            this.rooms =
                    rooms == null ? new HashSet<FenixSpace.Room>() : (FluentIterable.from(rooms).transform(
                            new Function<Space, FenixSpace.Room>() {

                                @Override
                                public FenixSpace.Room apply(Space input) {
                                    return new FenixSpace.Room(input,false, true, null);
                                }
                            }).toSet());
        }

        @JsonInclude(Include.NON_NULL)
        public Boolean getIsEnrolled() {
            return isEnrolled;
        }

        public void setIsEnrolled(Boolean isEnrolled) {
            this.isEnrolled = isEnrolled;
        }

        @JsonInclude(Include.NON_EMPTY)
        public Set<FenixCourse> getCourses() {
            return courses;
        }

        public void setCourses(Set<FenixCourse> courses) {
            this.courses = courses;
        }

        @JsonInclude(Include.NON_DEFAULT)
        public FenixSpace.Room getAssignedRoom() {
            return assignedRoom;
        }

        public void setAssignedRoom(Space assignedRoom) {
            this.assignedRoom = assignedRoom == null ? null : new FenixSpace.Room(assignedRoom, false, true, null);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    public static class Test extends WrittenEvaluation {

        protected Test() {
            super();
        }

        public Test(String id, String name, FenixPeriod evaluationPeriod, Boolean isInEnrolmentPeriod,
                String enrollmentPeriodStart, String enrolmentPeriodEnd, List<Space> rooms, Boolean isEnrolled,
                Set<ExecutionCourse> courses) {
            super(id, name, evaluationPeriod, isInEnrolmentPeriod, enrollmentPeriodStart, enrolmentPeriodEnd, rooms, isEnrolled,
                    courses);
        }

        public Test(String externalId, String name, FenixPeriod evaluationPeriod, Boolean isEnrolmentPeriod,
                String enrollmentPeriodStart, String enrolmentPeriodEnd, List<Space> rooms, Boolean isEnroled,
                Set<ExecutionCourse> courses, Space assignedRoom) {
            super(externalId, name, evaluationPeriod, isEnrolmentPeriod, enrollmentPeriodStart, enrolmentPeriodEnd, rooms,
                    isEnroled, courses, assignedRoom);
        }

    }

    public static class Exam extends WrittenEvaluation {

        protected Exam() {
            super();
        }

        public Exam(String id, String name, FenixPeriod evaluationPeriod, Boolean isInEnrolmentPeriod,
                String enrollmentPeriodStart, String enrolmentPeriodEnd, List<Space> rooms, Boolean isEnrolled,
                Set<ExecutionCourse> courses) {
            super(id, name, evaluationPeriod, isInEnrolmentPeriod, enrollmentPeriodStart, enrolmentPeriodEnd, rooms, isEnrolled,
                    courses);
        }

        public Exam(String externalId, String name, FenixPeriod evaluationPeriod, Boolean isEnrolmentPeriod,
                String enrollmentPeriodStart, String enrolmentPeriodEnd, List<Space> rooms, Boolean isEnroled,
                Set<ExecutionCourse> courses, Space assignedRoom) {
            super(externalId, name, evaluationPeriod, isEnrolmentPeriod, enrollmentPeriodStart, enrolmentPeriodEnd, rooms,
                    isEnroled, courses, assignedRoom);
        }

    }

    public static class Project extends FenixCourseEvaluation {

        public Project(String name, FenixPeriod evaluationPeriod) {
            super(name, evaluationPeriod);
        }

    }

    public static class OnlineTest extends FenixCourseEvaluation {

        public OnlineTest(String name) {
            this(name, new FenixPeriod());
        }

        public OnlineTest(String name, FenixPeriod evaluationPeriod) {
            super(name, evaluationPeriod);
        }

    }

    public static class AdHocEvaluation extends FenixCourseEvaluation {
        String description;

        public AdHocEvaluation(String name, String description) {
            this(name, description, new FenixPeriod());
        }

        public AdHocEvaluation(String name, String description, FenixPeriod evaluationPeriod) {
            super(name, evaluationPeriod);
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }

    String name;
    FenixPeriod evaluationPeriod;

    public FenixCourseEvaluation(String name, FenixPeriod evaluationPeriod) {
        setName(name);
        setEvaluationPeriod(evaluationPeriod);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FenixPeriod getEvaluationPeriod() {
        return evaluationPeriod;
    }

    public void setEvaluationPeriod(FenixPeriod evaluationPeriod) {
        this.evaluationPeriod = evaluationPeriod;
    }

}
