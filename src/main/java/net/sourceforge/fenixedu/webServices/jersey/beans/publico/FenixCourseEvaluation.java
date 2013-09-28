package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.List;

import net.sourceforge.fenixedu.util.EvaluationType;

public abstract class FenixCourseEvaluation {

    public FenixCourseEvaluation(String name, EvaluationType type) {
        super();
        this.name = name;
        this.type = type;
    }

    public static class WrittenEvaluation extends FenixCourseEvaluation {

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
        Boolean isEnrolmentPeriod;
        String enrollmentBeginDay;
        String enrollmentBeginTime;
        String enrollmentEndDay;
        String enrollmentEndTime;

        List<Room> rooms;

        public WrittenEvaluation(String name, EvaluationType type, String day, String beginningTime, String endTime,
                Boolean isEnrolmentPeriod, String enrollmentBeginDay, String enrollmentBeginTime, String enrollmentEndDay,
                String enrollmentEndTime, List<Room> rooms) {
            super(name, type);
            this.day = day;
            this.beginningTime = beginningTime;
            this.endTime = endTime;
            this.isEnrolmentPeriod = isEnrolmentPeriod;
            this.enrollmentBeginDay = enrollmentBeginDay;
            this.enrollmentBeginTime = enrollmentBeginTime;
            this.enrollmentEndDay = enrollmentEndDay;
            this.enrollmentEndTime = enrollmentEndTime;
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

        public Boolean getIsEnrolmentPeriod() {
            return isEnrolmentPeriod;
        }

        public void setIsEnrolmentPeriod(Boolean isEnrolmentPeriod) {
            this.isEnrolmentPeriod = isEnrolmentPeriod;
        }

        public String getEnrollmentBeginDay() {
            return enrollmentBeginDay;
        }

        public void setEnrollmentBeginDay(String enrollmentBeginDay) {
            this.enrollmentBeginDay = enrollmentBeginDay;
        }

        public String getEnrollmentBeginTime() {
            return enrollmentBeginTime;
        }

        public void setEnrollmentBeginTime(String enrollmentBeginTime) {
            this.enrollmentBeginTime = enrollmentBeginTime;
        }

        public String getEnrollmentEndDay() {
            return enrollmentEndDay;
        }

        public void setEnrollmentEndDay(String enrollmentEndDay) {
            this.enrollmentEndDay = enrollmentEndDay;
        }

        public String getEnrollmentEndTime() {
            return enrollmentEndTime;
        }

        public void setEnrollmentEndTime(String enrollmentEndTime) {
            this.enrollmentEndTime = enrollmentEndTime;
        }

        public List<Room> getRooms() {
            return rooms;
        }

        public void setRooms(List<Room> rooms) {
            this.rooms = rooms;
        }

    }

    public static class Project extends FenixCourseEvaluation {

        String beginningDay;
        String beginningTime;
        String endDay;
        String endTime;

        private Project(String name) {
            super(name, EvaluationType.PROJECT_TYPE);
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
            super(name, EvaluationType.ONLINE_TEST_TYPE);
        }

    }

    public static class AdHocEvaluation extends FenixCourseEvaluation {
        String description;

        public AdHocEvaluation(String name, String description) {
            super(name, EvaluationType.AD_HOC_TYPE);
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
    EvaluationType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EvaluationType getType() {
        return type;
    }

    public void setType(EvaluationType type) {
        this.type = type;
    }

}
