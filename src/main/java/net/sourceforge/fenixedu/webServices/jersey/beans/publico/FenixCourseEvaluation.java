package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = FenixCourseEvaluation.Test.class, name = "TEST"),
        @JsonSubTypes.Type(value = FenixCourseEvaluation.Exam.class, name = "EXAM"),
        @JsonSubTypes.Type(value = FenixCourseEvaluation.OnlineTest.class, name = "ONLINE_TEST"),
        @JsonSubTypes.Type(value = FenixCourseEvaluation.Project.class, name = "PROJECT"),
        @JsonSubTypes.Type(value = FenixCourseEvaluation.AdHocEvaluation.class, name = "AD_HOC"), })
public abstract class FenixCourseEvaluation {

    public FenixCourseEvaluation(String name) {
        super();
        this.name = name;
    }

    public abstract static class WrittenEvaluation extends FenixCourseEvaluation {

        public static class Room {
            String id;
            String name;
            String description;

            public Room(String id, String name, String description) {
                super();
                this.id = id;
                this.name = name;
                this.description = description;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

        }

        String day;
        String beginningTime;
        String endTime;
        Boolean isInEnrolmentPeriod;
        FenixInterval enrollmentPeriod;

        List<Room> rooms;

        public WrittenEvaluation(String name, String day, String beginningTime, String endTime, Boolean isInEnrolmentPeriod,
                String enrollmentPeriodStart, String enrolmentPeriodEnd, List<Room> rooms) {
            super(name);
            this.day = day;
            this.beginningTime = beginningTime;
            this.endTime = endTime;
            this.isInEnrolmentPeriod = isInEnrolmentPeriod;
            enrollmentPeriod = new FenixInterval(enrollmentPeriodStart, enrolmentPeriodEnd);
            this.rooms = rooms;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getBeginningTime() {
            return beginningTime;
        }

        public void setBeginningTime(String beginningTime) {
            this.beginningTime = beginningTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
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

        public List<Room> getRooms() {
            return rooms;
        }

        public void setRooms(List<Room> rooms) {
            this.rooms = rooms;
        }

    }

    public static class Test extends WrittenEvaluation {

        public Test(String name, String day, String beginningTime, String endTime, Boolean isEnrolmentPeriod,
                String enrollmentPeriodStart, String enrolmentPeriodEnd, List<Room> rooms) {
            super(name, day, beginningTime, endTime, isEnrolmentPeriod, enrollmentPeriodStart, enrolmentPeriodEnd, rooms);
        }
    }

    public static class Exam extends WrittenEvaluation {

        public Exam(String name, String day, String beginningTime, String endTime, Boolean isEnrolmentPeriod,
                String enrollmentPeriodStart, String enrolmentPeriodEnd, List<Room> rooms) {
            super(name, day, beginningTime, endTime, isEnrolmentPeriod, enrollmentPeriodStart, enrolmentPeriodEnd, rooms);
        }

    }

    public static class Project extends FenixCourseEvaluation {

        String beginningDay;
        String beginningTime;
        String endDay;
        String endTime;

        private Project(String name) {
            super(name);
        }

        public Project(String name, String beginningDay, String beginningTime, String endDay, String endTime) {
            this(name);
            this.beginningDay = beginningDay;
            this.beginningTime = beginningTime;
            this.endDay = endDay;
            this.endTime = endTime;
        }

        public String getBeginningDay() {
            return beginningDay;
        }

        public void setBeginningDay(String beginningDay) {
            this.beginningDay = beginningDay;
        }

        public String getBeginningTime() {
            return beginningTime;
        }

        public void setBeginningTime(String beginningTime) {
            this.beginningTime = beginningTime;
        }

        public String getEndDay() {
            return endDay;
        }

        public void setEndDay(String endDay) {
            this.endDay = endDay;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

    }

    public static class OnlineTest extends FenixCourseEvaluation {

        public OnlineTest(String name) {
            super(name);
        }

    }

    public static class AdHocEvaluation extends FenixCourseEvaluation {
        String description;

        public AdHocEvaluation(String name, String description) {
            super(name);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
