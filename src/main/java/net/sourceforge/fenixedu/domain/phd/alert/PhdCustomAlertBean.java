package net.sourceforge.fenixedu.domain.phd.alert;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.LocalDate;

public class PhdCustomAlertBean implements Serializable {

    static enum PhdAlertTargetGroupType {

        MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PERSONS,

        ONLY_FOR_ME;

    }

    /**
     * 
     */
    private static final long serialVersionUID = -3274906509546432695L;

    private PhdAlertTargetGroupType targetGroupType;

    private String subject;

    private String body;

    private boolean toSendEmail;

    private LocalDate fireDate;

    private Person personToAdd;

    private Boolean userDefined = true;

    private Boolean shared = false;

    private PhdIndividualProgramProcess process;

    private Group targetGroup;

    public PhdCustomAlertBean(PhdIndividualProgramProcess process) {
        setProcess(process);
    }

    public PhdCustomAlertBean(PhdIndividualProgramProcess process, Boolean sendEmail, Boolean userDefined, Boolean shared) {
        setProcess(process);
        setToSendEmail(sendEmail);
        setUserDefined(userDefined);
        setShared(shared);
    }

    public PhdAlertTargetGroupType getTargetGroupType() {
        return targetGroupType;
    }

    public void setTargetGroupType(PhdAlertTargetGroupType targetGroupType) {
        this.targetGroupType = targetGroupType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isToSendEmail() {
        return toSendEmail;
    }

    public void setToSendEmail(boolean toSendEmail) {
        this.toSendEmail = toSendEmail;
    }

    public LocalDate getFireDate() {
        return fireDate;
    }

    public void setFireDate(LocalDate fireDate) {
        this.fireDate = fireDate;
    }

    public void setTargetGroup(Group targetGroup) {
        this.targetGroup = targetGroup;
    }

    public Group getTargetGroup() {
        return this.targetGroup;
    }

    public Group calculateTargetGroup() {
        if (getTargetGroup() != null) {
            return getTargetGroup();
        }

        switch (getTargetGroupType()) {

        case MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PERSONS:
            return new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_PHD_PROCESSES, this.getProcess().getPhdProgram());
        case ONLY_FOR_ME:
            return new PersonGroup(AccessControl.getPerson());

        default:
            throw new RuntimeException("Target group type not supported");
        }
    }

    public Person getPersonToAdd() {
        return this.personToAdd;
    }

    public void setPersonToAdd(Person personToAdd) {
        this.personToAdd = personToAdd;
    }

    public Boolean getUserDefined() {
        return userDefined;
    }

    public void setUserDefined(Boolean userDefined) {
        this.userDefined = userDefined;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public PhdIndividualProgramProcess getProcess() {
        return this.process;
    }

    public void setProcess(PhdIndividualProgramProcess process) {
        this.process = process;
    }

}
