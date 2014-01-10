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

    public FenixCourseEvaluation(String name, FenixPeriod evaluationPeriod) {
        setName(name);
        setEvaluationPeriod(evaluationPeriod);
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

        Boolean isInEnrolmentPeriod;
        FenixInterval enrollmentPeriod;

        List<Room> rooms;

        public WrittenEvaluation(String name, FenixPeriod evaluationPeriod, Boolean isInEnrolmentPeriod,
                String enrollmentPeriodStart, String enrolmentPeriodEnd, List<Room> rooms) {
            super(name, evaluationPeriod);
            setEvaluationPeriod(evaluationPeriod);
            this.isInEnrolmentPeriod = isInEnrolmentPeriod;
            enrollmentPeriod = new FenixInterval(enrollmentPeriodStart, enrolmentPeriodEnd);
            this.rooms = rooms;
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


        public Test(String name, FenixPeriod evaluationPeriod, Boolean isEnrolmentPeriod, String enrollmentPeriodStart,
                String enrolmentPeriodEnd, List<Room> rooms) {
            super(name, evaluationPeriod, isEnrolmentPeriod, enrollmentPeriodStart, enrolmentPeriodEnd, rooms);
        }
    }

    public static class Exam extends WrittenEvaluation {

        public Exam(String name, FenixPeriod evaluationPeriod, Boolean isEnrolmentPeriod, String enrollmentPeriodStart,
                String enrolmentPeriodEnd, List<Room> rooms) {
            super(name, evaluationPeriod, isEnrolmentPeriod, enrollmentPeriodStart, enrolmentPeriodEnd, rooms);
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
