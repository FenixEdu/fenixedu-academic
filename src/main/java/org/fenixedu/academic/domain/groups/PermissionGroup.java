package org.fenixedu.academic.domain.groups;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.CustomGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.joda.time.DateTime;

import com.qubit.terra.qubAccessControl.domain.AccessControlPermission;
import com.qubit.terra.qubAccessControl.domain.AccessControlProfile;

@GroupOperator("permission")
public class PermissionGroup extends CustomGroup {

    @GroupArgument(value = "")
    private String code;

    PermissionGroup() {
    }

    public PermissionGroup(String code) {
        this();
        this.code = code;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof PermissionGroup) {
            return this.code.equals(((PermissionGroup) object).code);
        }
        return false;
    }

    public Stream<User> getDirectMembers() {
        Set<User> members = new HashSet<>();
        AccessControlPermission.findByCode(code).getProfileSet().stream()
                .forEach(profile -> members.addAll(profile.getFenixMemberSet()));
        return members.stream();

    }

    @Override
    public Stream<User> getMembers() {

        Set<AccessControlProfile> profiles = AccessControlPermission.findByCode(code).getProfileSet();
        Set<User> members = new HashSet<>();

        profiles.forEach(profile -> {

            if (!(profile.getCustomExpression() == null || StringUtils.isEmpty(profile.getCustomExpression()))) {
                members.addAll(Group.parse(profile.getCustomExpression()).getMembers().collect(Collectors.toSet()));
            }

            members.addAll(profile.getFenixMemberSet());
            profile.findAllParents().forEach(parent -> members.addAll(parent.getFenixMemberSet()));
        });

        return members.stream();
    }

    @Override
    public Stream<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public String getPresentationName() {
        return code;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isMember(User user) {

        if (user == null) {
            return false;
        }

        return PermissionService.hasAccess(code, user);
//
//        return getMembers().collect(Collectors.toSet()).contains(user);
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    @pt.ist.fenixframework.Atomic
    public PersistentPermissionGroup toPersistentGroup() {
        return PersistentPermissionGroup.getInstance(this.code).orElseGet(() -> new PersistentPermissionGroup(this.code));
    }

}
