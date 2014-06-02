package org.fenixedu.core.service;

import java.util.Set;
import java.util.stream.Collectors;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.contacts.Phone;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.spaces.ui.services.UserInformationService;
import org.springframework.stereotype.Service;

@Service
public class FenixUserInformationService implements UserInformationService {

    @Override
    public Set<Group> getGroups(User user) {
        Set<Role> personRolesSet = user.getPerson().getMainPersonRoles();
        return personRolesSet.stream().map(Role::getRoleGroup).map(PersistentGroup::toGroup).collect(Collectors.toSet());
    }

    @Override
    public String getEmail(User user) {
        return user.getPerson().getEmailForSendingEmails();
    }

    @Override
    public String getContacts(User user) {
        return user.getPerson().getPartyContacts(Phone.class).stream()
                .filter(p -> p.getVisibleToEmployees() != null && p.getVisibleToEmployees()).map(c -> ((Phone) c).getNumber())
                .collect(Collectors.joining(","));
    }
}
