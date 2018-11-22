package org.fenixedu.academic.ui.spring.controller.student;

import org.fenixedu.commons.i18n.LocalizedString;

public class RegistrationProtocolBean {

    private String code;
    private LocalizedString description;
    private boolean enrolmentByStudentAllowed;
    private boolean payGratuity;
    private boolean allowsIDCard;
    private boolean onlyAllowedDegreeEnrolment;
    private boolean isAlien;
    private boolean exempted;
    private boolean mobility;
    private boolean military;
    private boolean forOfficialMobilityReporting;
    private boolean attemptAlmaMatterFromPrecedent;

    public String getCode() {
        return code;
    }

    public LocalizedString getDescription() {
        return description;
    }

    public boolean getEnrolmentByStudentAllowed() {
        return enrolmentByStudentAllowed;
    }

    public boolean getPayGratuity() {
        return payGratuity;
    }

    public boolean getAllowsIDCard() {
        return allowsIDCard;
    }

    public boolean getOnlyAllowedDegreeEnrolment() {
        return onlyAllowedDegreeEnrolment;
    }

    public boolean getAlien() {
        return isAlien;
    }

    public boolean getExempted() {
        return exempted;
    }

    public boolean getMobility() {
        return mobility;
    }

    public boolean getMilitary() {
        return military;
    }

    public boolean getForOfficialMobilityReporting() {
        return forOfficialMobilityReporting;
    }

    public boolean getAttemptAlmaMatterFromPrecedent() {
        return attemptAlmaMatterFromPrecedent;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(LocalizedString description) {
        this.description = description;
    }

    public void setEnrolmentByStudentAllowed(boolean enrolmentByStudentAllowed) {
        this.enrolmentByStudentAllowed = enrolmentByStudentAllowed;
    }

    public void setPayGratuity(boolean payGratuity) {
        this.payGratuity = payGratuity;
    }

    public void setAllowsIDCard(boolean allowsIDCard) {
        this.allowsIDCard = allowsIDCard;
    }

    public void setOnlyAllowedDegreeEnrolment(boolean onlyAllowedDegreeEnrolment) {
        this.onlyAllowedDegreeEnrolment = onlyAllowedDegreeEnrolment;
    }

    public void setAlien(boolean alien) {
        isAlien = alien;
    }

    public void setExempted(boolean exempted) {
        this.exempted = exempted;
    }

    public void setMobility(boolean mobility) {
        this.mobility = mobility;
    }

    public void setMilitary(boolean military) {
        this.military = military;
    }

    public void setForOfficialMobilityReporting(boolean forOfficialMobilityReporting) {
        this.forOfficialMobilityReporting = forOfficialMobilityReporting;
    }

    public void setAttemptAlmaMatterFromPrecedent(boolean attemptAlmaMatterFromPrecedent) {
        this.attemptAlmaMatterFromPrecedent = attemptAlmaMatterFromPrecedent;
    }
}
