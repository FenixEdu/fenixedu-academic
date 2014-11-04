package net.sourceforge.fenixedu.domain.accessControl.rules;

import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class AccessRuleSystem extends AccessRuleSystem_Base {
    private AccessRuleSystem() {
        super();
        setRoot(Bennu.getInstance());
    }

    public static AccessRuleSystem getInstance() {
        if (Bennu.getInstance().getAccessRuleSystem() == null) {
            return initialize();
        }
        return Bennu.getInstance().getAccessRuleSystem();
    }

    @Atomic(mode = TxMode.WRITE)
    private static AccessRuleSystem initialize() {
        if (Bennu.getInstance().getAccessRuleSystem() == null) {
            return new AccessRuleSystem();
        }
        return Bennu.getInstance().getAccessRuleSystem();
    }

    public static Stream<AccessRule> accessRules() {
        return getInstance().getAccessRuleSet().stream();
    }

    public static Stream<AccessRule> accessRules(DateTime when) {
        Stream<AccessRule> deleted =
                getInstance().getDeletedAccessRuleSet().stream()
                        .filter(r -> r.getCreated().isBefore(when) && r.getRevoked().isAfter(when));
        Stream<AccessRule> current = getInstance().getAccessRuleSet().stream().filter(r -> r.getCreated().isBefore(when));
        return Stream.concat(deleted, current);
    }
}
