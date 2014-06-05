package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;
import java.util.stream.Collectors;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.SpaceUtils;

import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.spaces.domain.Space;

@GroupOperator("specificSpaceOccupationManagersGroup")
public class SpecificSpaceOccupationManagersGroup extends GroupStrategy {

    private static final long serialVersionUID = 1L;

    @Override
    public Set<User> getMembers() {
        return SpaceUtils.allocatableSpaces().map(s -> s.getOccupationsGroupWithChainOfResponsability()).filter(g -> g != null)
                .flatMap(g -> g.getMembers().stream()).collect(Collectors.toSet());
    }

    @Override
    public boolean isMember(User user) {
        Person person = user.getPerson();
        if (person != null && person.hasPersonRoles(Role.getRoleByRoleType(RoleType.RESOURCE_ALLOCATION_MANAGER))) {
            return true;
        }
        for (Space space : SpaceUtils.allocatableSpaces().collect(Collectors.toSet())) {
            if (space.isOccupationMember(user)) {
                return true;
            }
        }
        return false;
    }
}
