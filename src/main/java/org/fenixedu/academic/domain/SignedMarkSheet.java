package org.fenixedu.academic.domain;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;

public class SignedMarkSheet extends SignedMarkSheet_Base {
    
    public SignedMarkSheet(final MarkSheet markSheet, final byte[] content) {
        setMarkSheet(markSheet);
        init("MarkSheet_" + markSheet.getExternalId(), "MarkSheet_" + markSheet.getExternalId() + ".pdf", content);
    }

    @Override
    public boolean isAccessible(final User user) {
        final MarkSheet markSheet = getMarkSheet();
        return markSheet.getResponsibleTeacher().getPerson().getUser() == user
                || Group.parse("academic(MANAGE_MARKSHEETS)").isMember(user);
    }

    public void delete() {
        setMarkSheet(null);
        super.delete();
    }

}
