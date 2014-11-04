package org.fenixedu.academic.domain.contacts;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.NobodyGroup;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class ContactRoot extends ContactRoot_Base {
    private ContactRoot() {
        super();
        setRoot(Bennu.getInstance());
        setStudentVisibility(NobodyGroup.get());
        setStaffVisibility(NobodyGroup.get());
        setManagementVisibility(NobodyGroup.get());
        setEmergencyContactVisibility(NobodyGroup.get());
    }

    public static ContactRoot getInstance() {
        if (Bennu.getInstance().getContactRoot() == null) {
            return initialize();
        }
        return Bennu.getInstance().getContactRoot();
    }

    @Atomic(mode = TxMode.WRITE)
    private static ContactRoot initialize() {
        if (Bennu.getInstance().getContactRoot() == null) {
            return new ContactRoot();
        }
        return Bennu.getInstance().getContactRoot();
    }

    public Group getStudentVisibility() {
        return super.getStudentVisibilityGroup().toGroup();
    }

    public void setStudentVisibility(Group studentVisibility) {
        super.setStudentVisibilityGroup(studentVisibility.toPersistentGroup());
    }

    public Group getStaffVisibility() {
        return super.getStaffVisibilityGroup().toGroup();
    }

    public void setStaffVisibility(Group staffVisibility) {
        super.setStaffVisibilityGroup(staffVisibility.toPersistentGroup());
    }

    public Group getManagementVisibility() {
        return super.getManagementVisibilityGroup().toGroup();
    }

    public void setManagementVisibility(Group managementVisibility) {
        super.setManagementVisibilityGroup(managementVisibility.toPersistentGroup());
    }

    public Group getEmergencyContactVisibility() {
        return super.getEmergencyContactVisibilityGroup().toGroup();
    }

    public void setEmergencyContactVisibility(Group emergencyContactVisibility) {
        super.setEmergencyContactVisibilityGroup(emergencyContactVisibility.toPersistentGroup());
    }
}
