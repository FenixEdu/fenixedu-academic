package net.sourceforge.fenixedu.applicationTier.Servico.space;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.Space.SpaceAccessGroupType;

public class SpaceAccessGroupsManagement extends Service {

    public void run(Space space, SpaceAccessGroupType accessGroupType, Person person, boolean toAdd,
	    boolean isToMaintainElements) throws FenixServiceException {

	if (person != null) {
	    Set<Person> elements = null;
	    if (accessGroupType.equals(SpaceAccessGroupType.PERSON_OCCUPATION_ACCESS_GROUP)) {
		checkIfPersonAlreadyHasPermissions(space, person, toAdd);
		if (space.getPersonOccupationsAccessGroup() == null) {
		    space.setPersonOccupationsAccessGroup(new FixedSetGroup());
		}
		elements = space.getPersonOccupationsAccessGroup().getElements();
		if (isToMaintainElements) {
		    Group accessGroup = space.getPersonOccupationsAccessGroupWithChainOfResponsibility();
		    elements = (accessGroup != null) ? accessGroup.getElements() : elements;
		}

		Set<Person> newElements = manageAccessGroup(person, toAdd, elements);
		space.setPersonOccupationsAccessGroup(new FixedSetGroup(newElements));
		spaceManagerRoleManagement(person, toAdd);

	    } else if (accessGroupType.equals(SpaceAccessGroupType.EXTENSION_OCCUPATION_ACCESS_GROUP)) {
		checkIfPersonAlreadyHasPermissions(space, person, toAdd);
		if (space.getExtensionOccupationsAccessGroup() == null) {
		    space.setExtensionOccupationsAccessGroup(new FixedSetGroup());
		}
		elements = space.getExtensionOccupationsAccessGroup().getElements();
		if (isToMaintainElements) {
		    Group accessGroup = space
			    .getExtensionOccupationsAccessGroupWithChainOfResponsibility();
		    elements = (accessGroup != null) ? accessGroup.getElements() : elements;
		}

		Set<Person> newElements = manageAccessGroup(person, toAdd, elements);
		space.setExtensionOccupationsAccessGroup(new FixedSetGroup(newElements));
		spaceManagerRoleManagement(person, toAdd);

	    } else if (accessGroupType.equals(SpaceAccessGroupType.SPACE_MANAGEMENT_ACCESS_GROUP)) {
		if (space.getSpaceManagementAccessGroup() == null) {
		    space.setSpaceManagementAccessGroup(new FixedSetGroup());
		}
		elements = space.getSpaceManagementAccessGroup().getElements();
		if (isToMaintainElements) {
		    Group accessGroup = space.getSpaceManagementAccessGroupWithChainOfResponsibility();
		    elements = (accessGroup != null) ? accessGroup.getElements() : elements;
		}
		Set<Person> newElements = manageAccessGroup(person, toAdd, elements);
		space.setSpaceManagementAccessGroup(new FixedSetGroup(newElements));
		spaceManagerRoleManagement(person, toAdd);
	    }
	} else {
	    throw new FenixServiceException("error.space.access.groups.management.no.person");
	}
    }

    private void checkIfPersonAlreadyHasPermissions(Space space, Person person, boolean toAdd) throws FenixServiceException {
	if (toAdd && space.personHasPermissionsToManageSpace(person)) {
	    throw new FenixServiceException("error.space.access.groups.management.person.already.have.permission");
	}
    }

    private Set<Person> manageAccessGroup(Person person, boolean toAdd, Set<Person> elements) {
	Set<Person> newElements;
	if (toAdd) {
	    newElements = addPersonToAccessGroup(person, elements);
	} else {
	    newElements = removePersonFromAccessGroup(person, elements);
	}
	return newElements;
    }

    private Set<Person> addPersonToAccessGroup(Person person, Set<Person> elements) {
	Set<Person> newList = new TreeSet<Person>(Person.COMPARATOR_BY_NAME);
	newList.addAll(elements);
	newList.add(person);
	return newList;
    }

    private Set<Person> removePersonFromAccessGroup(Person person, Set<Person> elements) {
	Set<Person> newList = new TreeSet<Person>(Person.COMPARATOR_BY_NAME);
	newList.addAll(elements);
	newList.remove(person);
	return newList;
    }

    private void spaceManagerRoleManagement(Person person, boolean toAdd) {
	if (toAdd) {
	    if (!person.hasRole(RoleType.SPACE_MANAGER)) {
		person.addPersonRoleByRoleType(RoleType.SPACE_MANAGER);
	    }
	} else {
	    Set<Space> spacesSet = rootDomainObject.getSpacesSet();
	    for (Space space : spacesSet) {
		if (space.personHasPermissionToManageExtensionOccupations(person)
			|| space.personHasPermissionToManagePersonOccupations(person)
			|| space.personHasSpecialPermissionToManageSpace(person)) {
		    return;
		}
	    }
	    person.removeRoleByType(RoleType.SPACE_MANAGER);
	}
    }
}
