package net.sourceforge.fenixedu.domain;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.NoOneGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;

public class ManagementGroups extends ManagementGroups_Base {

    public ManagementGroups() {
        super();
    }

    public static boolean isAssiduousnessSectionStaffMember(Person person) {
        Collection<ManagementGroups> managementGroups = RootDomainObject.getInstance().getManagementGroups();
        return managementGroups == null || managementGroups.isEmpty() ? false : ((managementGroups.iterator().next()
                .getAssiduousnessSectionStaff() != null && managementGroups.iterator().next().getAssiduousnessSectionStaff()
                .isMember(person)) || isAssiduousnessManagerMember(person));
    }

    public static boolean isAssiduousnessManagerMember(Person person) {
        Collection<ManagementGroups> managementGroups = RootDomainObject.getInstance().getManagementGroups();
        return managementGroups == null || managementGroups.isEmpty() ? false : managementGroups.iterator().next()
                .getAssiduousnessManagers() != null
                && managementGroups.iterator().next().getAssiduousnessManagers().isMember(person);
    }

    public static boolean isPayrollSectionStaff(Person person) {
        Collection<ManagementGroups> managementGroups = RootDomainObject.getInstance().getManagementGroups();
        return managementGroups == null || managementGroups.isEmpty() ? false : managementGroups.iterator().next()
                .getPayrollSectionStaff() != null
                && managementGroups.iterator().next().getPayrollSectionStaff().isMember(person);
    }

    public static class AssiduousnessManagerGroupBuilder implements GroupBuilder {
        @Override
        public Group build(Object[] arguments) {
            Collection<ManagementGroups> managementGroups = RootDomainObject.getInstance().getManagementGroups();
            return managementGroups == null || managementGroups.isEmpty() ? new NoOneGroup() : (managementGroups.iterator()
                    .next().getAssiduousnessManagers() == null ? new NoOneGroup() : managementGroups.iterator().next()
                    .getAssiduousnessManagers());
        }

        @Override
        public int getMinArguments() {
            return 0;
        }

        @Override
        public int getMaxArguments() {
            return 0;
        }
    }

    public static class AssiduousnessSectionStaffGroupBuilder implements GroupBuilder {
        @Override
        public Group build(Object[] arguments) {
            Collection<ManagementGroups> managementGroups = RootDomainObject.getInstance().getManagementGroups();
            return managementGroups == null || managementGroups.isEmpty() ? new NoOneGroup() : (managementGroups.iterator()
                    .next().getAssiduousnessSectionStaff() == null ? new NoOneGroup() : managementGroups.iterator().next()
                    .getAssiduousnessSectionStaff());
        }

        @Override
        public int getMinArguments() {
            return 0;
        }

        @Override
        public int getMaxArguments() {
            return 0;
        }
    }

    public static class PayrollSectionStaffGroupBuilder implements GroupBuilder {
        @Override
        public Group build(Object[] arguments) {
            Collection<ManagementGroups> managementGroups = RootDomainObject.getInstance().getManagementGroups();
            return managementGroups == null || managementGroups.isEmpty() ? new NoOneGroup() : (managementGroups.iterator()
                    .next().getPayrollSectionStaff() == null ? new NoOneGroup() : managementGroups.iterator().next()
                    .getPayrollSectionStaff());
        }

        @Override
        public int getMinArguments() {
            return 0;
        }

        @Override
        public int getMaxArguments() {
            return 0;
        }
    }
    @Deprecated
    public boolean hasAssiduousnessManagers() {
        return getAssiduousnessManagers() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasPayrollSectionStaff() {
        return getPayrollSectionStaff() != null;
    }

    @Deprecated
    public boolean hasAssiduousnessSectionStaff() {
        return getAssiduousnessSectionStaff() != null;
    }

}
