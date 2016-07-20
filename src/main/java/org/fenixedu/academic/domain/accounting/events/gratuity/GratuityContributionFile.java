package org.fenixedu.academic.domain.accounting.events.gratuity;

import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.bennu.core.domain.User;

public class GratuityContributionFile extends GratuityContributionFile_Base {

    public GratuityContributionFile(Party creditor, GratuityEvent event, String fileName, byte[] content) {
        Student debtor = event.getPerson().getStudent();
        String displayName =
                event.getDescription().appendLabel(" - Transfer from " + debtor.getNumber() + " to " + creditor.getName())
                        .toString();
        init(displayName, fileName, content);
    }

    @Override
    public boolean isAccessible(User user) {
        return false;
    }

}
