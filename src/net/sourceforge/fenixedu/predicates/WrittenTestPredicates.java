package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class WrittenTestPredicates {

    public static final AccessControlPredicate<WrittenTest> changeDatePredicate = new AccessControlPredicate<WrittenTest>() {
        public boolean evaluate(WrittenTest writtenTest) {
            final IUserView requestor = AccessControl.getUserView();
            return requestor != null && (writtenTest.hasTimeTableManagerPrivledges(requestor) || writtenTest.hasCoordinatorPrivledges(requestor));
        }
    };

}
