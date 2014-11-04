package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.presentationTier.Action.person.UpdateEmergencyContactDA.EmergencyContactBean;
import pt.ist.fenixframework.Atomic;

public class EmergencyContact extends EmergencyContact_Base {

    protected EmergencyContact() {
        super();
    }

    public EmergencyContact(String contact, Person person) {
        super();
        super.setContact(contact);
        person.ensureUserProfile();
        person.getProfile().setEmergencyContact(this);
    }

    @Atomic
    public static void updateEmergencyContact(Person person, EmergencyContactBean bean) {
        person.ensureUserProfile();
        if (person.getProfile().getEmergencyContact() == null) {
            new EmergencyContact(bean.getContact(), person);
        } else {
            person.getProfile().getEmergencyContact().setContact(bean.getContact());
        }
    }
}
