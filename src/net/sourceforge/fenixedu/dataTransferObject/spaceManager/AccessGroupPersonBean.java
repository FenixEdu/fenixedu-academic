package net.sourceforge.fenixedu.dataTransferObject.spaceManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Space.SpaceAccessGroupType;

public class AccessGroupPersonBean implements Serializable {

	private PersistentGroupMembers persistentGroupMembersReference;
	private SpaceAccessGroupType accessGroupType;
	private Person personReference;
	private Boolean maintainElements;
	private RoleType roleType;

	public AccessGroupPersonBean() {
	}

	public Boolean getMaintainElements() {
		return maintainElements;
	}

	public void setMaintainElements(Boolean maintainElements) {
		this.maintainElements = maintainElements;
	}

	public Person getPerson() {
		return this.personReference;
	}

	public void setPerson(Person Person) {
		this.personReference = Person;
	}

	public PersistentGroupMembers getPersistentGroupMembers() {
		return this.persistentGroupMembersReference;
	}

	public void setPersistentGroupMembers(PersistentGroupMembers persistentGroupMembers) {
		this.persistentGroupMembersReference = persistentGroupMembers;
	}

	public SpaceAccessGroupType getAccessGroupType() {
		return accessGroupType;
	}

	public void setAccessGroupType(SpaceAccessGroupType accessGroupType) {
		this.accessGroupType = accessGroupType;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}
}
