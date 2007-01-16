package net.sourceforge.fenixedu.domain.space;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.DomainObjectActionLog;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.material.Material;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public abstract class Space extends Space_Base {

    protected Space() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(this.getClass().getName());
	setCreatedOn(new YearMonthDay());
    }

    public SpaceInformation getMostRecentSpaceInformation() {
	SpaceInformation selectedSpaceInformation = null;
	for (final SpaceInformation spaceInformation : getSpaceInformations()) {
	    if (spaceInformation.getValidUntil() == null) {
		return spaceInformation;
	    } else if (selectedSpaceInformation == null
		    || spaceInformation.getValidUntil()
			    .isAfter(selectedSpaceInformation.getValidUntil())) {
		selectedSpaceInformation = spaceInformation;
	    }
	}
	return selectedSpaceInformation;
    }

    public SpaceInformation getSpaceInformation() {
	return getSpaceInformation(null);
    }

    public SpaceInformation getSpaceInformation(YearMonthDay when) {
	when = (when == null) ? new YearMonthDay() : when;
	for (final SpaceInformation spaceInformation : getSpaceInformations()) {
	    if (spaceInformation.isActive(when)) {
		return spaceInformation;
	    }
	}
	return getMostRecentSpaceInformation();
    }

    public SortedSet<SpaceInformation> getOrderedSpaceInformations() {
	return new TreeSet<SpaceInformation>(getSpaceInformations());
    }

    public Blueprint getSuroundingSpaceMostRecentBlueprint() {
	Space suroundingSpace = getSuroundingSpace();
	if (suroundingSpace != null) {
	    return suroundingSpace.getMostRecentBlueprint();
	}
	return null;
    }

    public Blueprint getMostRecentBlueprint() {
	SortedSet<Blueprint> orderedBlueprints = getOrderedBlueprints();
	return (!orderedBlueprints.isEmpty()) ? orderedBlueprints.last() : null;
    }

    public SortedSet<Blueprint> getOrderedBlueprints() {
	return new TreeSet<Blueprint>(getBlueprints());
    }

    public List<PersonSpaceOccupation> getPersonSpaceOccupations() {
	List<PersonSpaceOccupation> personSpaceOccupations = new ArrayList<PersonSpaceOccupation>();
	for (SpaceOccupation spaceOccupation : getSpaceOccupations()) {
	    if (spaceOccupation instanceof PersonSpaceOccupation) {
		personSpaceOccupations.add((PersonSpaceOccupation) spaceOccupation);
	    }
	}
	return personSpaceOccupations;
    }

    public List<MaterialSpaceOccupation> getMaterialSpaceOccupations() {
	List<MaterialSpaceOccupation> materialSpaceOccupations = new ArrayList<MaterialSpaceOccupation>();
	for (SpaceOccupation spaceOccupation : getSpaceOccupations()) {
	    if (spaceOccupation instanceof MaterialSpaceOccupation) {
		materialSpaceOccupations.add((MaterialSpaceOccupation) spaceOccupation);
	    }
	}
	return materialSpaceOccupations;
    }
    
    public List<UnitSpaceOccupation> getUnitSpaceOccupations() {
	List<UnitSpaceOccupation> unitSpaceOccupations = new ArrayList<UnitSpaceOccupation>();
	for (SpaceOccupation spaceOccupation : getSpaceOccupations()) {
	    if (spaceOccupation instanceof UnitSpaceOccupation) {
		unitSpaceOccupations.add((UnitSpaceOccupation) spaceOccupation);
	    }
	}
	return unitSpaceOccupations;
    }

    public SortedSet<PersonSpaceOccupation> getActivePersonSpaceOccupations() {
	return getPersonSpaceOccupationsByState(true);
    }

    public SortedSet<PersonSpaceOccupation> getInactivePersonSpaceOccupations() {
	return getPersonSpaceOccupationsByState(false);
    }

    private SortedSet<PersonSpaceOccupation> getPersonSpaceOccupationsByState(boolean state) {
	SortedSet<PersonSpaceOccupation> personSpaceOccupations = new TreeSet<PersonSpaceOccupation>(
		PersonSpaceOccupation.COMPARATOR_BY_PERSON_NAME_AND_OCCUPATION_INTERVAL);
	YearMonthDay current = new YearMonthDay();
	for (PersonSpaceOccupation personSpaceOccupation : getPersonSpaceOccupations()) {
	    if (personSpaceOccupation.contains(current) == state) {
		personSpaceOccupations.add(personSpaceOccupation);
	    }
	}
	return personSpaceOccupations;
    }

    public SortedSet<SpaceResponsibility> getActiveSpaceResponsibility() {
	return getSpaceResponsabilityByState(true);
    }

    public SortedSet<SpaceResponsibility> getInactiveSpaceResponsibility() {
	return getSpaceResponsabilityByState(false);
    }

    private SortedSet<SpaceResponsibility> getSpaceResponsabilityByState(boolean state) {
	SortedSet<SpaceResponsibility> spaceResponsibility = new TreeSet<SpaceResponsibility>(
		SpaceResponsibility.COMPARATOR_BY_UNIT_NAME_AND_RESPONSIBILITY_INTERVAL);
	YearMonthDay current = new YearMonthDay();
	for (SpaceResponsibility responsibility : getSpaceResponsibilitySet()) {
	    if (responsibility.isActive(current) == state) {
		spaceResponsibility.add(responsibility);
	    }
	}
	return spaceResponsibility;
    }
    
    public SortedSet<UnitSpaceOccupation> getActiveUnitSpaceOccupations() {
	return getUnitSpaceOccupationsByState(true);
    }

    public SortedSet<UnitSpaceOccupation> getInactiveUnitSpaceOccupations() {
	return getUnitSpaceOccupationsByState(false);
    }

    private SortedSet<UnitSpaceOccupation> getUnitSpaceOccupationsByState(boolean state) {
	SortedSet<UnitSpaceOccupation> unitSpaceOccupations = new TreeSet<UnitSpaceOccupation>(
		UnitSpaceOccupation.COMPARATOR_BY_OCCUPATION_INTERVAL_AND_UNIT);
	YearMonthDay current = new YearMonthDay();
	for (UnitSpaceOccupation unitSpaceOccupation : getUnitSpaceOccupations()) {
	    if (unitSpaceOccupation.isActive(current) == state) {
		unitSpaceOccupations.add(unitSpaceOccupation);
	    }
	}
	return unitSpaceOccupations;
    }

    public SortedSet<Material> getActiveSpaceMaterial() {
	SortedSet<Material> spaceMaterial = new TreeSet<Material>(Material.COMPARATOR_BY_CLASS_NAME);
	YearMonthDay current = new YearMonthDay();
	for (MaterialSpaceOccupation materialSpaceOccupation : getMaterialSpaceOccupations()) {
	    if (materialSpaceOccupation.isActive(current)) {
		spaceMaterial.add(materialSpaceOccupation.getMaterial());
	    }
	}
	return spaceMaterial;
    }

    public SortedSet<MaterialSpaceOccupation> getActiveMaterialSpaceOccupationsToLoggedPerson() {
	return getMaterialSpaceOccupationsToLoggedPersonByState(true);
    }

    public SortedSet<MaterialSpaceOccupation> getInactiveMaterialSpaceOccupationsToLoggedPerson() {
	return getMaterialSpaceOccupationsToLoggedPersonByState(false);
    }

    private SortedSet<MaterialSpaceOccupation> getMaterialSpaceOccupationsToLoggedPersonByState(
	    boolean state) {

	SortedSet<MaterialSpaceOccupation> materialOccupations = new TreeSet<MaterialSpaceOccupation>(
		MaterialSpaceOccupation.COMPARATOR_BY_CLASS_NAME);
	YearMonthDay current = new YearMonthDay();
	Person loggedPerson = AccessControl.getPerson();

	for (MaterialSpaceOccupation materialSpaceOccupation : getMaterialSpaceOccupations()) {
	    if (materialSpaceOccupation.isActive(current) == state
		    && (materialSpaceOccupation.getSpace().personHasPermissionsToManageSpace(
			    loggedPerson) || (materialSpaceOccupation.getAccessGroup() != null && materialSpaceOccupation
			    .getAccessGroup().isMember(loggedPerson)))) {
		materialOccupations.add(materialSpaceOccupation);
	    }
	}
	return materialOccupations;
    }

    public Set<? extends MaterialSpaceOccupation> getMaterialSpaceOccupationsByMaterialClass(Class clazz) {
	Set<MaterialSpaceOccupation> materialOccupations = new HashSet<MaterialSpaceOccupation>();
	for (MaterialSpaceOccupation occupation : getMaterialSpaceOccupations()) {
	    if (occupation.getClass().equals(clazz)) {
		materialOccupations.add(occupation);
	    }
	}
	return materialOccupations;
    }

    public static Set<DomainObjectActionLog> getListOfChangesInSpacesOrderedByInstant() {
	Set<Class> classs = new HashSet<Class>();
	Person loggedPerson = AccessControl.getPerson();
	if (personIsSpacesAdministrator(loggedPerson)) {
	    classs.add(Room.class);
	    classs.add(Floor.class);
	    classs.add(Campus.class);
	    classs.add(Building.class);
	    classs.add(RoomInformation.class);
	    classs.add(FloorInformation.class);
	    classs.add(CampusInformation.class);
	    classs.add(BuildingInformation.class);
	    classs.add(PersonSpaceOccupation.class);
	    classs.add(ExtensionSpaceOccupation.class);
	    classs.add(UnitSpaceOccupation.class);
	    classs.add(SpaceResponsibility.class);
	    classs.add(Blueprint.class);
	    return DomainObjectActionLog.readDomainObjectActionLogsOrderedByInstant(classs);
	}
	return new HashSet<DomainObjectActionLog>();
    }

    public Space readSubSpaceByBlueprintNumber(String blueprintNumber) {
	if (blueprintNumber != null && !StringUtils.isEmpty(blueprintNumber)) {
	    for (Space space : getContainedSpaces()) {
		if (space.getSpaceInformation() != null) {
		    String spaceBlueprint = space.getSpaceInformation().getBlueprintNumber();
		    if (spaceBlueprint != null && !StringUtils.isEmpty(spaceBlueprint)
			    && spaceBlueprint.equals(blueprintNumber)) {
			return space;
		    }
		}
	    }
	}
	return null;
    }

    public void delete() {
	if (!canBeDeleted()) {
	    throw new DomainException("error.space.cannot.be.deleted");
	}
	for (; !getBlueprints().isEmpty(); getBlueprints().get(0).delete())
	    ;
	for (; !getSpaceInformations().isEmpty(); getSpaceInformations().get(0)
		.deleteWithoutCheckNumberOfSpaceInformations())
	    ;

	removeSuroundingSpace();
	removeRootDomainObject();
	deleteDomainObject();
    }

    private boolean canBeDeleted() {
	if (hasAnyContainedSpaces()) {
	    return false;
	}
	if (hasAnySpaceOccupations()) {
	    return false;
	}
	if (hasAnySpaceResponsibility()) {
	    return false;
	}
	return true;
    }

    public boolean isActive() {
	return getMostRecentSpaceInformation().isActive(new YearMonthDay());
    }

    public static Set<Campus> getAllCampus() {
	Set<Campus> campus = new HashSet<Campus>();
	for (Space space : RootDomainObject.getInstance().getSpacesSet()) {
	    if (space instanceof Campus) {
		campus.add((Campus) space);
	    }
	}
	return campus;
    }

    public static boolean personIsSpacesAdministrator(Person person) {
	return person.hasRole(RoleType.MANAGER)
		|| (person.hasRole(RoleType.SPACE_MANAGER_SUPER_USER) && person
			.hasRole(RoleType.SPACE_MANAGER));
    }

    public void checkIfLoggedPersonHasPermissionsToManageSpace(Person person) {
	if (personHasPermissionsToManageSpace(person)) {
	    return;
	}
	throw new DomainException("error.logged.person.not.authorized.to.make.operation");
    }

    public boolean personHasPermissionsToManageSpace(Person person) {
	return personIsSpacesAdministrator(person) || personHasSpecialPermissionToManageSpace(person);
    }

    public boolean personHasSpecialPermissionToManageSpace(Person person) {
	Group spaceManagementAccessGroup = getSpaceManagementAccessGroupWithChainOfResponsibility();
	if (spaceManagementAccessGroup != null && spaceManagementAccessGroup.isMember(person)) {
	    return true;
	}
	return false;
    }

    public boolean personHasPermissionToManagePersonOccupations(Person person) {
	Group spaceManagementAccessGroup = getPersonOccupationsAccessGroupWithChainOfResponsibility();
	if (spaceManagementAccessGroup != null && spaceManagementAccessGroup.isMember(person)) {
	    return true;
	}
	return false;
    }

    public boolean personHasPermissionToManageExtensionOccupations(Person person) {
	Group spaceManagementAccessGroup = getExtensionOccupationsAccessGroupWithChainOfResponsibility();
	if (spaceManagementAccessGroup != null && spaceManagementAccessGroup.isMember(person)) {
	    return true;
	}
	return false;
    }

    public Group getPersonOccupationsAccessGroupWithChainOfResponsibility() {
	final Group thisGroup = getPersonOccupationsAccessGroup();
	if (thisGroup != null && !thisGroup.getElements().isEmpty()) {
	    return thisGroup;
	}
	final Space surroundingSpace = getSuroundingSpace();
	if (surroundingSpace != null) {
	    return surroundingSpace.getPersonOccupationsAccessGroupWithChainOfResponsibility();
	}
	return null;
    }

    public Group getExtensionOccupationsAccessGroupWithChainOfResponsibility() {
	final Group thisGroup = getExtensionOccupationsAccessGroup();
	if (thisGroup != null && !thisGroup.getElements().isEmpty()) {
	    return thisGroup;
	}
	final Space surroundingSpace = getSuroundingSpace();
	if (surroundingSpace != null) {
	    return surroundingSpace.getExtensionOccupationsAccessGroupWithChainOfResponsibility();
	}
	return null;
    }

    public Group getSpaceManagementAccessGroupWithChainOfResponsibility() {
	final Group thisGroup = getSpaceManagementAccessGroup();
	if (thisGroup != null && !thisGroup.getElements().isEmpty()) {
	    return thisGroup;
	}
	final Space surroundingSpace = getSuroundingSpace();
	if (surroundingSpace != null) {
	    return surroundingSpace.getSpaceManagementAccessGroupWithChainOfResponsibility();
	}
	return null;
    }

    public static enum SpaceAccessGroupType {

	PERSON_OCCUPATION_ACCESS_GROUP("personOccupationsAccessGroup"),

	EXTENSION_OCCUPATION_ACCESS_GROUP("extensionOccupationsAccessGroup"),

	SPACE_MANAGEMENT_ACCESS_GROUP("spaceManagementAccessGroup");

	private String spaceAccessGroupSlotName;

	private SpaceAccessGroupType(String spaceAccessGroupSlotName) {
	    this.spaceAccessGroupSlotName = spaceAccessGroupSlotName;
	}

	public String getName() {
	    return name();
	}

	public String getSpaceAccessGroupSlotName() {
	    return spaceAccessGroupSlotName;
	}
    }


    public boolean isCampus() {
	return false;
    }

    public boolean isBuilding() {
	return false;
    }

    public boolean isFloor() {
	return false;
    }

    public boolean isRoom() {
	return false;
    }

}
