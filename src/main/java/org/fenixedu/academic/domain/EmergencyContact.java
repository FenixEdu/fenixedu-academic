package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.UserProfile;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class EmergencyContact extends EmergencyContact_Base {

    protected EmergencyContact() {
        super();
    }

    protected EmergencyContact(UserProfile profile, String contact) {
        super();
        setContact(contact);
        setProfile(profile);
    }

    @Atomic(mode = TxMode.WRITE)
    public static void updateEmergencyContact(UserProfile profile, String contact) {
        if (profile.getEmergencyContact() == null) {
            new EmergencyContact(profile, contact);
        } else {
            profile.getEmergencyContact().setContact(contact);
        }
    }
}
