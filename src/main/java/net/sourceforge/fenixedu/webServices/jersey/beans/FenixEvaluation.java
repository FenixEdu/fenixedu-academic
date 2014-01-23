package net.sourceforge.fenixedu.webServices.jersey.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class FenixEvaluation {

    private String name;
    private String type;
    private String id;
    private Boolean isEnrollmentPeriod;
    private String day;
    private String startHour;
    private String endHour;
    private String rooms;
    private String enrollmentBeginDay;
    private String enrollmentEndDay;
    private Boolean isEnrolled;
    private String course;

    public FenixEvaluation(String name, String type, String id, Boolean isEnrollmentPeriod, String day, String startHour,
            String endHour, String rooms, String enrollmentBeginDay, String enrollmentEndDay, Boolean isEnrolled, String course) {
        super();
        this.name = name;
        this.type = type;
        this.id = id;
        this.isEnrollmentPeriod = isEnrollmentPeriod;
        this.day = day;
        this.startHour = startHour;
        this.endHour = endHour;
        this.rooms = rooms;
        this.enrollmentBeginDay = enrollmentBeginDay;
        this.enrollmentEndDay = enrollmentEndDay;
        this.isEnrolled = isEnrolled;
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsEnrollmentPeriod() {
        return isEnrollmentPeriod;
    }

    public void setIsEnrollmentPeriod(Boolean isEnrollmentPeriod) {
        this.isEnrollmentPeriod = isEnrollmentPeriod;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

    public String getEnrollmentBeginDay() {
        return enrollmentBeginDay;
    }

    public void setEnrollmentBeginDay(String enrollmentBeginDay) {
        this.enrollmentBeginDay = enrollmentBeginDay;
    }

    public String getEnrollmentEndDay() {
        return enrollmentEndDay;
    }

    public void setEnrollmentEndDay(String enrollmentEndDay) {
        this.enrollmentEndDay = enrollmentEndDay;
    }

    @JsonInclude(Include.NON_NULL)
    public Boolean getIsEnrolled() {
        return isEnrolled;
    }

    public void setIsEnrolled(Boolean isEnrolled) {
        this.isEnrolled = isEnrolled;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
