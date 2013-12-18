package net.sourceforge.fenixedu.predicates;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class WrittenTestPredicates {

    public static final AccessControlPredicate<WrittenTest> changeDatePredicate = new AccessControlPredicate<WrittenTest>() {
        @Override
        public boolean evaluate(WrittenTest writtenTest) {
            final User requestor = Authenticate.getUser();
            return requestor != null;
        }
    };

}
