package net.sourceforge.fenixedu.domain;

import java.util.List;

public class ManagementGroups extends ManagementGroups_Base {

    public ManagementGroups() {
        super();
    }

    public static boolean isAssiduousnessManagerMember(Person person) {
        List<ManagementGroups> managementGroups = RootDomainObject.getInstance().getManagementGroups();
        return managementGroups == null || managementGroups.isEmpty() ? false
        	: managementGroups.iterator().next().getAssiduousnessManagers().isMember(person);
    }

    public static boolean isProtocolManagerMember(Person person) {
        List<ManagementGroups> managementGroups = RootDomainObject.getInstance().getManagementGroups();
        return managementGroups == null || managementGroups.isEmpty() ? false
        	: managementGroups.iterator().next().getProtocolManagers().isMember(person);
    }
}
