package net.sourceforge.fenixedu.domain.space;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.DomainObjectActionLog;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.material.Material;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.resource.Resource;
import net.sourceforge.fenixedu.domain.resource.ResourceAllocation;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public abstract class Space extends Space_Base {

    protected Space() {
	super();	
	setCreatedOn(new YearMonthDay());
    }

    @Checked("SpacePredicates.checkPermissionsToManageSpace")
    @FenixDomainObjectActionLogAnnotation(actionName = "Set new Parent space", parameters = {"this", "newParentSpace" })
    public void setNewPossibleParentSpace(Space newParentSpace) {
	if(newParentSpace != null) {
	    setSuroundingSpace(newParentSpace);
	}
    }
    
    @Override
    public boolean isSpace() {
        return true;
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
	for (ResourceAllocation allocation : getResourceAllocations()) {
	    if (allocation.isPersonSpaceOccupation()) {
		personSpaceOccupations.add((PersonSpaceOccupation) allocation);
	    }
	}
	return personSpaceOccupations;
    }

    public List<MaterialSpaceOccupation> getMaterialSpaceOccupations() {
	List<MaterialSpaceOccupation> materialSpaceOccupations = new ArrayList<MaterialSpaceOccupation>();
	for (ResourceAllocation	allocation : getResourceAllocations()) {
	    if (allocation.isMaterialSpaceOccupation()) {
		materialSpaceOccupations.add((MaterialSpaceOccupation) allocation);
	    }
	}
	return materialSpaceOccupations;
    }
    
    public List<UnitSpaceOccupation> getUnitSpaceOccupations() {
	List<UnitSpaceOccupation> unitSpaceOccupations = new ArrayList<UnitSpaceOccupation>();
	for (ResourceAllocation	allocation : getResourceAllocations()) {
	    if (allocation.isUnitSpaceOccupation()) {
		unitSpaceOccupations.add((UnitSpaceOccupation) allocation);
	    }
	}
	return unitSpaceOccupations;
    }
    
    public List<Space> getContainedSpacesByState(SpaceState spaceState){
	List<Space> result = new ArrayList<Space>();
	for (Space space : getContainedSpaces()) {
	    if((spaceState.equals(SpaceState.ACTIVE) && space.isActive()) 
		    || spaceState.equals(SpaceState.INACTIVE) && !space.isActive()) {
		result.add(space);
	    }
	}
	return result;
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
		    && (materialSpaceOccupation.getSpace().personHasPermissionsToManageSpace(loggedPerson)
			    || (materialSpaceOccupation.getAccessGroup() != null 
				    && materialSpaceOccupation.getAccessGroup().isMember(loggedPerson)))) {
		materialOccupations.add(materialSpaceOccupation);
	    }
	}
	return materialOccupations;
    }

    public Set<? extends MaterialSpaceOccupation> getMaterialSpaceOccupationsByMaterialClass(Class<? extends MaterialSpaceOccupation> clazz) {
	Set<MaterialSpaceOccupation> materialOccupations = new HashSet<MaterialSpaceOccupation>();
	for (MaterialSpaceOccupation occupation : getMaterialSpaceOccupations()) {
	    if (occupation.getClass().equals(clazz)) {
		materialOccupations.add(occupation);
	    }
	}
	return materialOccupations;
    }

    public static Set<DomainObjectActionLog> getListOfChangesInSpacesOrderedByInstant() {
	Set<Class<? extends DomainObject>> classs = new HashSet<Class<? extends DomainObject>>();
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
	    classs.add(RoomClassification.class);
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
	
	for (; !getBlueprints().isEmpty(); getBlueprints().get(0).delete());
	for (; !getSpaceInformations().isEmpty(); getSpaceInformations().get(0).deleteWithoutCheckNumberOfSpaceInformations());
	
	removeSuroundingSpace();
	removeRootDomainObject();
	deleteDomainObject();
    }

    private boolean canBeDeleted() {
	if (hasAnyContainedSpaces() 
		|| hasAnyResourceAllocations()
		|| hasAnySpaceResponsibility()) {
	    return false;
	}
	return true;
    }

    public boolean isActive() {
	return getMostRecentSpaceInformation().isActive(new YearMonthDay());
    }

    public static Set<Campus> getAllCampus() {
	Set<Campus> campus = new HashSet<Campus>();
	for (Resource resource : RootDomainObject.getInstance().getResources()) {
	    if (resource.isCampus()) {
		campus.add((Campus) resource);
	    }
	}
	return campus;
    }

    public static boolean personIsSpacesAdministrator(Person person) {
	return (person.hasRole(RoleType.MANAGER) || person.hasRole(RoleType.SPACE_MANAGER_SUPER_USER)) && person.hasRole(RoleType.SPACE_MANAGER);
    }

    public void checkIfLoggedPersonHasPermissionsToManageSpace(Person person) {
	if (personHasPermissionsToManageSpace(person)) {
	    return;
	}
	throw new DomainException("error.logged.person.not.authorized.to.make.operation");
    }
    
    public void checkIfLoggedPersonIsSpacesAdministrator(Person person) {
	if (personIsSpacesAdministrator(person)) {
	    return;
	}
	throw new DomainException("error.logged.person.not.authorized.to.make.operation");
    }

    public boolean personHasPermissionsToManageSpace(Person person) {
	return personIsSpacesAdministrator(person) || personHasSpecialPermissionToManageSpace(person);
    }

    public boolean personHasSpecialPermissionToManageSpace(Person person) {
	Group accessGroup = getSpaceManagementAccessGroupWithChainOfResponsibility();
	if (accessGroup != null && accessGroup.isMember(person)) {
	    return true;
	}
	return false;
    }

    public boolean personHasPermissionToManagePersonOccupations(Person person) {
	Group accessGroup = getPersonOccupationsAccessGroupWithChainOfResponsibility();
	if (accessGroup != null && accessGroup.isMember(person)) {
	    return true;
	}
	return false;
    }

    public boolean personHasPermissionToManageExtensionOccupations(Person person) {
	Group accessGroup = getExtensionOccupationsAccessGroupWithChainOfResponsibility();
	if (accessGroup != null && accessGroup.isMember(person)) {
	    return true;
	}
	return false;
    }
    
    public boolean personHasPermissionToManageUnitOccupations(Person person) {
	Group accessGroup = getUnitOccupationsAccessGroupWithChainOfResponsibility();
	if (accessGroup != null && accessGroup.isMember(person)) {
	    return true;
	}
	return false;
    }

    public Group getPersonOccupationsAccessGroupWithChainOfResponsibility() {
	final Group accessGroup = getPersonOccupationsAccessGroup();
	if (accessGroup != null && !accessGroup.getElements().isEmpty()) {
	    return accessGroup;
	}
	final Space surroundingSpace = getSuroundingSpace();
	if (surroundingSpace != null) {
	    return surroundingSpace.getPersonOccupationsAccessGroupWithChainOfResponsibility();
	}
	return null;
    }

    public Group getUnitOccupationsAccessGroupWithChainOfResponsibility() {
	final Group accessGroup = getUnitOccupationsAccessGroup();
	if (accessGroup != null && !accessGroup.getElements().isEmpty()) {
	    return accessGroup;
	}
	final Space surroundingSpace = getSuroundingSpace();
	if (surroundingSpace != null) {
	    return surroundingSpace.getUnitOccupationsAccessGroupWithChainOfResponsibility();
	}
	return null;
    }
    
    public Group getExtensionOccupationsAccessGroupWithChainOfResponsibility() {
	final Group accessGroup = getExtensionOccupationsAccessGroup();
	if (accessGroup != null && !accessGroup.getElements().isEmpty()) {
	    return accessGroup;
	}
	final Space surroundingSpace = getSuroundingSpace();
	if (surroundingSpace != null) {
	    return surroundingSpace.getExtensionOccupationsAccessGroupWithChainOfResponsibility();
	}
	return null;
    }

    public Group getSpaceManagementAccessGroupWithChainOfResponsibility() {
	final Group accessGroup = getSpaceManagementAccessGroup();
	if (accessGroup != null && !accessGroup.getElements().isEmpty()) {
	    return accessGroup;
	}
	final Space surroundingSpace = getSuroundingSpace();
	if (surroundingSpace != null) {
	    return surroundingSpace.getSpaceManagementAccessGroupWithChainOfResponsibility();
	}
	return null;
    }
    
    @Checked("SpacePredicates.checkIfLoggedPersonIsSpaceAdministrator")
    @FenixDomainObjectActionLogAnnotation(actionName = "Add or remove person from access group", parameters = {"this", "accessGroupType", "person", "toAdd", "isToMaintainElements" })
    public void addOrRemovePersonFromAccessGroup(SpaceAccessGroupType accessGroupType, Person person, Boolean toAdd, Boolean isToMaintainElements) throws DomainException {
	
	if(person == null) {
	    throw new DomainException("error.space.access.groups.management.no.person");
	}
	
	Set<Person> elements = null, newElements = null;
	
	switch (accessGroupType) {
	
	case PERSON_OCCUPATION_ACCESS_GROUP:
	    
	    checkIfPersonAlreadyHasPermissions(person, toAdd);
	    if (getPersonOccupationsAccessGroup() == null) {
		setPersonOccupationsAccessGroup(new FixedSetGroup());
	    }
	    
	    elements = getPersonOccupationsAccessGroup().getElements();
	    if (isToMaintainElements.booleanValue()) {
		Group accessGroup = getPersonOccupationsAccessGroupWithChainOfResponsibility();
		elements = (accessGroup != null) ? accessGroup.getElements() : elements;
	    }

	    newElements = manageAccessGroup(person, toAdd, elements);
	    setPersonOccupationsAccessGroup(new FixedSetGroup(newElements));
	    spaceManagerRoleManagement(person, toAdd);
	    break;

	case EXTENSION_OCCUPATION_ACCESS_GROUP:
	    
	    checkIfPersonAlreadyHasPermissions(person, toAdd);
	    if (getExtensionOccupationsAccessGroup() == null) {
		setExtensionOccupationsAccessGroup(new FixedSetGroup());
	    }
	    
	    elements = getExtensionOccupationsAccessGroup().getElements();
	    if (isToMaintainElements.booleanValue()) {
		Group accessGroup = getExtensionOccupationsAccessGroupWithChainOfResponsibility();
		elements = (accessGroup != null) ? accessGroup.getElements() : elements;
	    }

	    newElements = manageAccessGroup(person, toAdd, elements);
	    setExtensionOccupationsAccessGroup(new FixedSetGroup(newElements));
	    spaceManagerRoleManagement(person, toAdd);
	    break;
	    
	case UNIT_OCCUPATION_ACCESS_GROUP:
	   
	    checkIfPersonAlreadyHasPermissions(person, toAdd);
	    if (getUnitOccupationsAccessGroup() == null) {
		setUnitOccupationsAccessGroup(new FixedSetGroup());
	    }
	    
	    elements = getUnitOccupationsAccessGroup().getElements();
	    if (isToMaintainElements.booleanValue()) {
		Group accessGroup = getUnitOccupationsAccessGroupWithChainOfResponsibility();
		elements = (accessGroup != null) ? accessGroup.getElements() : elements;
	    }

	    newElements = manageAccessGroup(person, toAdd, elements);
	    setUnitOccupationsAccessGroup(new FixedSetGroup(newElements));
	    spaceManagerRoleManagement(person, toAdd);
	    break;
	    
	case SPACE_MANAGEMENT_ACCESS_GROUP:
	   
	    checkIfPersonAlreadyHasPermissions(person, toAdd);
	    if (getSpaceManagementAccessGroup() == null) {
		setSpaceManagementAccessGroup(new FixedSetGroup());
	    }
	    
	    elements = getSpaceManagementAccessGroup().getElements();
	    if (isToMaintainElements.booleanValue()) {
		Group accessGroup = getSpaceManagementAccessGroupWithChainOfResponsibility();
		elements = (accessGroup != null) ? accessGroup.getElements() : elements;
	    }
	    
	    newElements = manageAccessGroup(person, toAdd, elements);
	    setSpaceManagementAccessGroup(new FixedSetGroup(newElements));
	    spaceManagerRoleManagement(person, toAdd);
	    break;
	    
	default:
	    break;
	}
    }
    
    private void checkIfPersonAlreadyHasPermissions(Person person, boolean toAdd) throws DomainException {
	if (toAdd && personHasPermissionsToManageSpace(person)) {
	    throw new DomainException("error.space.access.groups.management.person.already.have.permission");
	}
    }

    private Set<Person> manageAccessGroup(Person person, boolean toAdd, Set<Person> elements) {	
	Set<Person> newList = new TreeSet<Person>(Person.COMPARATOR_BY_NAME_AND_ID);
	newList.addAll(elements);	
	if(toAdd) {	   
	    newList.add(person);
	} else {	    
	    newList.remove(person);
	}
	return newList;
    }

    private void spaceManagerRoleManagement(Person person, boolean toAdd) {
	if (toAdd) {
	    if (!person.hasRole(RoleType.SPACE_MANAGER)) {
		person.addPersonRoleByRoleType(RoleType.SPACE_MANAGER);
	    }
	} else {
	    for (Resource resource : RootDomainObject.getInstance().getResources()) {
		if (resource.isSpace()) {
		    Space space = (Space) resource;
		    if (space.personHasPermissionToManageExtensionOccupations(person)
			    || space.personHasPermissionToManagePersonOccupations(person)
			    || space.personHasPermissionToManageUnitOccupations(person)
			    || space.personHasSpecialPermissionToManageSpace(person)) {
			return;
		    }
		}
	    }
	    person.removeRoleByType(RoleType.SPACE_MANAGER);
	}
    }
    
    public static enum SpaceAccessGroupType {
		
	PERSON_OCCUPATION_ACCESS_GROUP("personOccupationsAccessGroup"),

	UNIT_OCCUPATION_ACCESS_GROUP("unitOccupationsAccessGroup"),
	
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

    public Building getSpaceBuilding() {
	if(isBuilding()) {
	    return (Building) this;
	}	
	if(getSuroundingSpace() == null) {
	    return null;
	}	
	return getSuroundingSpace().getSpaceBuilding();
    }
    
    public Floor getSpaceFloor() {
	if(isFloor()) {
	    return (Floor) this;
	}	
	if(getSuroundingSpace() == null) {
	    return null;
	}	
	return getSuroundingSpace().getSpaceFloor();
    }

    public Set<Space> getPossibleParentSpacesToMoveSpaceUp() {
	Set<Space> result = new HashSet<Space>();
	if(!(isCampus())) {
	    result = getPossibleParentSpacesToMoveSpaceUp(result);			
	    result.addAll(Space.getAllCampus());    	
            if(getSuroundingSpace() != null) {
                result.remove(getSuroundingSpace());
            }
	}
	return result;
    }
    
    private Set<Space> getPossibleParentSpacesToMoveSpaceUp(Set<Space> result) {
	if(getSuroundingSpace() != null) {
	    result.add(getSuroundingSpace());
	    getSuroundingSpace().getPossibleParentSpacesToMoveSpaceUp(result);
	}		
	return result;
    } 
    
    public List<Space> getPossibleParentSpacesToMoveSpaceDown() {
	List<Space> result = new ArrayList<Space>();
	if(!(isCampus())) {
            if(getSuroundingSpace() != null) {
                result.addAll(getSuroundingSpace().getContainedSpaces());	
            }
            result.remove(this);
	}
	return result;
    }    
}
