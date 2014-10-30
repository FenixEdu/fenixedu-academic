package net.sourceforge.fenixedu.domain.accessControl.rules;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;

public interface AccessOperation<R extends AccessRule, T extends AccessTarget> extends Serializable {

    public String exportAsString();

    public static AccessOperation<?, ?> importFromString(String serialized) {
        try {
            String[] parts = serialized.split(":");
            Class<? extends Enum> type = (Class<? extends Enum>) Class.forName(parts[0]);
            return (AccessOperation<?, ?>) Enum.valueOf(type, parts[1]);
        } catch (ClassNotFoundException e) {
            throw new Error("Could not parse AcademicOperation:" + serialized);
        }
    }

    public String getLocalizedName();

    public Optional<R> grant(Group whoCanAccess, Set<T> whatCanAffect);

    public Optional<R> grant(User user);

    public Optional<R> revoke(User user);
}
