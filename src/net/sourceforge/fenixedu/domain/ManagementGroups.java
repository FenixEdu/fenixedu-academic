package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.NoOneGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;

public class ManagementGroups extends ManagementGroups_Base {

    public ManagementGroups() {
	super();
    }

    public static boolean isAssiduousnessSectionStaffMember(Person person) {
	List<ManagementGroups> managementGroups = RootDomainObject.getInstance().getManagementGroups();
	return managementGroups == null || managementGroups.isEmpty() ? false : managementGroups
		.iterator().next().getAssiduousnessSectionStaff() != null
		&& managementGroups.iterator().next().getAssiduousnessSectionStaff().isMember(person)
		|| isAssiduousnessManagerMember(person);
    }

    public static boolean isAssiduousnessManagerMember(Person person) {
	List<ManagementGroups> managementGroups = RootDomainObject.getInstance().getManagementGroups();
	return managementGroups == null || managementGroups.isEmpty() ? false : managementGroups
		.iterator().next().getAssiduousnessManagers() != null
		&& managementGroups.iterator().next().getAssiduousnessManagers().isMember(person);
    }

    public static boolean isPayrollSectionStaff(Person person) {
	List<ManagementGroups> managementGroups = RootDomainObject.getInstance().getManagementGroups();
	return managementGroups == null || managementGroups.isEmpty() ? false : managementGroups
		.iterator().next().getPayrollSectionStaff() != null
		&& managementGroups.iterator().next().getPayrollSectionStaff().isMember(person);
    }

    public static boolean isProtocolManagerMember(Person person) {
	List<ManagementGroups> managementGroups = RootDomainObject.getInstance().getManagementGroups();
	return managementGroups == null || managementGroups.isEmpty() ? false : managementGroups
		.iterator().next().getProtocolManagers() != null
		&& managementGroups.iterator().next().getProtocolManagers().isMember(person);
    }

    public static class AssiduousnessManagerGroupBuilder implements GroupBuilder {
	public Group build(Object[] arguments) {
	    List<ManagementGroups> managementGroups = RootDomainObject.getInstance()
		    .getManagementGroups();
	    return managementGroups == null || managementGroups.isEmpty() ? new NoOneGroup()
		    : (managementGroups.iterator().next().getAssiduousnessManagers() == null ? new NoOneGroup()
			    : managementGroups.iterator().next().getAssiduousnessManagers());
	}

	public int getMinArguments() {
	    return 0;
	}

	public int getMaxArguments() {
	    return 0;
	}
    }

    public static class AssiduousnessSectionStaffGroupBuilder implements GroupBuilder {
	public Group build(Object[] arguments) {
	    List<ManagementGroups> managementGroups = RootDomainObject.getInstance()
		    .getManagementGroups();
	    return managementGroups == null || managementGroups.isEmpty() ? new NoOneGroup()
		    : (managementGroups.iterator().next().getAssiduousnessSectionStaff() == null ? new NoOneGroup()
			    : managementGroups.iterator().next().getAssiduousnessSectionStaff());
	}

	public int getMinArguments() {
	    return 0;
	}

	public int getMaxArguments() {
	    return 0;
	}
    }

    public static class PayrollSectionStaffGroupBuilder implements GroupBuilder {
	public Group build(Object[] arguments) {
	    List<ManagementGroups> managementGroups = RootDomainObject.getInstance()
		    .getManagementGroups();
	    return managementGroups == null || managementGroups.isEmpty() ? new NoOneGroup()
		    : (managementGroups.iterator().next().getPayrollSectionStaff() == null ? new NoOneGroup()
			    : managementGroups.iterator().next().getPayrollSectionStaff());
	}

	public int getMinArguments() {
	    return 0;
	}

	public int getMaxArguments() {
	    return 0;
	}
    }
}
