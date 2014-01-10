package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

public class WrittenTestPredicates {

    public static final AccessControlPredicate<WrittenTest> changeDatePredicate = new AccessControlPredicate<WrittenTest>() {
        @Override
        public boolean evaluate(WrittenTest writtenTest) {
            final User requestor = Authenticate.getUser();
            return requestor != null;
        }
    };

}
