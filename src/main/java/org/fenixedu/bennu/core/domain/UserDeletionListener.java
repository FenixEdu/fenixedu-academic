package org.fenixedu.bennu.core.domain;

import org.fenixedu.academic.domain.accessControl.rules.AccessRule;

public class UserDeletionListener {

    public static void deleteUser(final User u) {

        UserProfile profile = u.getProfile();
        if (profile != null && profile.getEmergencyContact() != null) {
            profile.getEmergencyContact().delete();
        }

        for (AccessRule rule : u.getAccessRuleSet()) {
            rule.delete();
        }
    }

}
