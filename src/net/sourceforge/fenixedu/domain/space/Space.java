package net.sourceforge.fenixedu.domain.space;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.DomainObjectActionLog;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroup;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.material.Material;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.resource.Resource;
import net.sourceforge.fenixedu.domain.resource.ResourceAllocation;
import net.sourceforge.fenixedu.domain.resource.ResourceResponsibility;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.persistenceTier.Conversores.Group2StringConverter;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

public abstract class Space extends Space_Base {

    public final static Comparator<Space> COMPARATOR_BY_PRESENTATION_NAME = new ComparatorChain();   
    static {
	((ComparatorChain) COMPARATOR_BY_PRESENTATION_NAME).addComparator(new BeanComparator("spaceInformation.presentationName", Collator.getInstance()));
	((ComparatorChain) COMPARATOR_BY_PRESENTATION_NAME).addComparator(DomainObject.COMPARATOR_BY_ID);	
    }
    
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
		    || spaceInformation.getValidUntil().isAfter(selectedSpaceInformation.getValidUntil())) {
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
    
    public int getSpaceResponsibilityCount() {
	return getSpaceResponsibility().size();
    }
    
    public List<SpaceResponsibility> getSpaceResponsibility(){
	List<SpaceResponsibility> result = new ArrayList<SpaceResponsibility>();
	for (ResourceResponsibility responsibility : getResourceResponsibility()) {
	    if(responsibility.isSpaceResponsibility()) {
		result.add((SpaceResponsibility) responsibility);
	    }
	}
	return result;
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
	for (ResourceResponsibility responsibility : getResourceResponsibilitySet()) {
	    if (responsibility.isSpaceResponsibility() && responsibility.isActive(current) == state) {
		spaceResponsibility.add((SpaceResponsibility) responsibility);
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
	    classs.add(Blueprint.class);
	    classs.add(RoomSubdivision.class);
	    classs.add(RoomInformation.class);	    
	    classs.add(FloorInformation.class);
	    classs.add(CampusInformation.class);
	    classs.add(RoomClassification.class);
	    classs.add(BuildingInformation.class);
	    classs.add(UnitSpaceOccupation.class);
	    classs.add(SpaceResponsibility.class);
	    classs.add(PersonSpaceOccupation.class);
	    classs.add(ExtensionSpaceOccupation.class);
	    classs.add(RoomSubdivisionInformation.class);	    	    	   	    	 
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

    @Override
    public void delete() {
	if (!canBeDeleted()) {
	    throw new DomainException("error.space.cannot.be.deleted");
	}

	for (; !getBlueprints().isEmpty(); getBlueprints().get(0).delete());
	for (; !getSpaceInformations().isEmpty(); getSpaceInformations().get(0).deleteWithoutCheckNumberOfSpaceInformations());

	super.setSuroundingSpace(null);
	super.delete();
    }
    
    private boolean canBeDeleted() {
        return !hasAnyContainedSpaces();
    }

    public boolean isActive() {
	return getMostRecentSpaceInformation().isActive(new YearMonthDay());
    }

    public static List<Space> getAllSpacesByPresentationName(String name) {
	List<Space> result = new ArrayList<Space>();	
	String[] identificationWords = getIdentificationWords(name);
	for (Resource resource : RootDomainObject.getInstance().getResources()) {	    
	    if (resource.isSpace() && ((Space)resource).verifyNameEquality(identificationWords)) {
		result.add((Space) resource);
	    }
	}
	return result;
    }
    
    private boolean verifyNameEquality(String[] nameWords) {
	if (nameWords != null) {	
	    String spacePresentationName = getSpaceInformation().getPresentationName();
	    if (spacePresentationName != null) {
		String[] spaceIdentificationWords = spacePresentationName.trim().split(" ");
		StringNormalizer.normalize(spaceIdentificationWords);
		int j, i;
		for (i = 0; i < nameWords.length; i++) {
		    if (!nameWords[i].equals("")) {
			for (j = 0; j < spaceIdentificationWords.length; j++) {
			    if (spaceIdentificationWords[j].equals(nameWords[i])) {
				break;
			    }
			}
			if (j == spaceIdentificationWords.length) {
			    return false;
			}
		    }
		}
		if (i == nameWords.length) {
		    return true;
		}
	    }
	}
	return false;
    }
    
    private static String[] getIdentificationWords(String name) {
	String[] identificationWords = null;
	if (name != null && !StringUtils.isEmpty(name.trim())) {
	    identificationWords = name.trim().split(" ");
	    StringNormalizer.normalize(identificationWords);
	}
	return identificationWords;
    }
    
    public static List<Campus> getAllCampus() {
	return (List<Campus>) getAllSpacesByClass(Campus.class, null);
    }

    public static List<Campus> getAllActiveCampus() {
	return (List<Campus>) getAllSpacesByClass(Campus.class, Boolean.TRUE);
    }

    public static List<Building> getAllActiveBuildings(){
	return (List<Building>) getAllSpacesByClass(Building.class, Boolean.TRUE);
    }

    private static List<? extends Space> getAllSpacesByClass(Class<? extends Space> clazz, Boolean active){
	List<Space> result = new ArrayList<Space>();
	for (Resource space : RootDomainObject.getInstance().getResources()) {
	    if (space.isSpace() && space.getClass().equals(clazz) && 
		    (active == null || ((Space)space).isActive() == active.booleanValue())) {
		result.add((Space) space);
	    }
	}
	return result;
    }

    public List<AllocatableSpace> getAllActiveSubRoomsForEducation(){
	List<AllocatableSpace> result = new ArrayList<AllocatableSpace>();
	List<Space> containedSpaces = getContainedSpaces();
	for (Space space : containedSpaces) {
	    if(space.isAllocatableSpace() && space.isActive() && ((AllocatableSpace)space).isForEducation()) {
		result.add((AllocatableSpace) space);
	    }
	}
	for (Space subSpace : containedSpaces) {
	    result.addAll(subSpace.getAllActiveSubRoomsForEducation());
	}
	return result;
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

    public boolean personHasPermissionToManageLessonOccupations(Person person) {
	Group accessGroup = getLessonOccupationsAccessGroupWithChainOfResponsibility();
	if (accessGroup != null && accessGroup.isMember(person)) {
	    return true;
	}
	return false;
    }

    public boolean personHasPermissionToManageWrittenEvaluationOccupations(Person person) {
	Group accessGroup = getWrittenEvaluationOccupationsAccessGroup();
	if (accessGroup != null && accessGroup.isMember(person)) {
	    return true;
	}
	return false;
    }

    public boolean personHasPermissionToGenericEventOccupations(Person person) {
	Group accessGroup = getGenericEventOccupationsAccessGroup();
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

    public Group getLessonOccupationsAccessGroupWithChainOfResponsibility() {
	final Group thisGroup = getLessonOccupationsAccessGroup();
	if (thisGroup != null && !thisGroup.getElements().isEmpty()) {
	    return thisGroup;
	}
	final Space surroundingSpace = getSuroundingSpace();
	if (surroundingSpace != null) {
	    return surroundingSpace.getLessonOccupationsAccessGroupWithChainOfResponsibility();
	}
	return null;
    }

    public Group getWrittenEvaluationOccupationsAccessGroupWithChainOfResponsibility() {
	final Group thisGroup = getWrittenEvaluationOccupationsAccessGroup();
	if (thisGroup != null && !thisGroup.getElements().isEmpty()) {
	    return thisGroup;
	}
	final Space surroundingSpace = getSuroundingSpace();
	if (surroundingSpace != null) {
	    return surroundingSpace.getWrittenEvaluationOccupationsAccessGroupWithChainOfResponsibility();
	}
	return null;
    }

    public Group getGenericEventOccupationsAccessGroupWithChainOfResponsibility() {
	final Group thisGroup = getGenericEventOccupationsAccessGroup();
	if (thisGroup != null && !thisGroup.getElements().isEmpty()) {
	    return thisGroup;
	}
	final Space surroundingSpace = getSuroundingSpace();
	if (surroundingSpace != null) {
	    return surroundingSpace.getGenericEventOccupationsAccessGroupWithChainOfResponsibility();
	}
	return null;
    }

    @Checked("SpacePredicates.checkIfLoggedPersonIsSpaceAdministrator")
    @FenixDomainObjectActionLogAnnotation(actionName = "Add or remove person from access group", parameters = {"this",
	    "accessGroupType", "toAdd", "isToMaintainElements", "expression" })
    public void addOrRemovePersonFromAccessGroup(SpaceAccessGroupType accessGroupType, Boolean toAdd, Boolean isToMaintainElements,
	    String expression) throws DomainException {

	if(StringUtils.isEmpty(expression)) {
	    throw new DomainException("error.space.access.groups.management.no.person");
	}
	
	if(!toAdd) {
	    byte[] encodeHex;
	    try {
		encodeHex = Hex.decodeHex(expression.toCharArray());
	    } catch (DecoderException e) {
		throw new DomainException("error.space.access.groups.invalid.expression");
	    }	
	    expression = new String(encodeHex);
	}
	
	Group groupToAddOrRemove = (Group) new Group2StringConverter().sqlToJava(expression);	
	
	Set<Person> elementsToAddOrRemove = null;
	Group existentGroup = null, newGroupUnion = null;

	switch (accessGroupType) {

	case PERSON_OCCUPATION_ACCESS_GROUP:

	    elementsToAddOrRemove = groupToAddOrRemove.getElements();
	    checkIfPersonAlreadyHasPermissions(elementsToAddOrRemove, toAdd);

	    if(isToMaintainElements) {		
		existentGroup = getPersonOccupationsAccessGroupWithChainOfResponsibility();			
	    } else {
		existentGroup = getPersonOccupationsAccessGroup();		
	    }

	    newGroupUnion = manageGroups(toAdd, groupToAddOrRemove, existentGroup);
	    setPersonOccupationsAccessGroup(newGroupUnion);
	    
	    spaceManagerRoleManagement(elementsToAddOrRemove, toAdd);	   
	    break;

	case EXTENSION_OCCUPATION_ACCESS_GROUP:

	    elementsToAddOrRemove = groupToAddOrRemove.getElements();
	    checkIfPersonAlreadyHasPermissions(elementsToAddOrRemove, toAdd);

	    if(isToMaintainElements) {		
		existentGroup = getExtensionOccupationsAccessGroupWithChainOfResponsibility();			
	    } else {
		existentGroup = getExtensionOccupationsAccessGroup();		
	    }

	    newGroupUnion = manageGroups(toAdd, groupToAddOrRemove, existentGroup);
	    setExtensionOccupationsAccessGroup(newGroupUnion);
	    
	    spaceManagerRoleManagement(elementsToAddOrRemove, toAdd);	    
	    break;

	case UNIT_OCCUPATION_ACCESS_GROUP:

	    elementsToAddOrRemove = groupToAddOrRemove.getElements();
	    checkIfPersonAlreadyHasPermissions(elementsToAddOrRemove, toAdd);

	    if(isToMaintainElements) {		
		existentGroup = getUnitOccupationsAccessGroupWithChainOfResponsibility();			
	    } else {
		existentGroup = getUnitOccupationsAccessGroup();	
	    }

	    newGroupUnion = manageGroups(toAdd, groupToAddOrRemove, existentGroup);
	    setUnitOccupationsAccessGroup(newGroupUnion);
	    
	    spaceManagerRoleManagement(elementsToAddOrRemove, toAdd);	    
	    break;

	case SPACE_MANAGEMENT_ACCESS_GROUP:

	    elementsToAddOrRemove = groupToAddOrRemove.getElements();
	    checkIfPersonAlreadyHasPermissions(elementsToAddOrRemove, toAdd);

	    if(isToMaintainElements) {		
		existentGroup = getSpaceManagementAccessGroupWithChainOfResponsibility();			
	    } else {
		existentGroup = getSpaceManagementAccessGroup();		
	    }

	    newGroupUnion = manageGroups(toAdd, groupToAddOrRemove, existentGroup);
	    setSpaceManagementAccessGroup(newGroupUnion);
	    
	    spaceManagerRoleManagement(elementsToAddOrRemove, toAdd);	    
	    break;

	case LESSON_OCCUPATION_ACCESS_GROUP:

	    if(isToMaintainElements) {		
		existentGroup = getLessonOccupationsAccessGroupWithChainOfResponsibility();			
	    } else {
		existentGroup = getLessonOccupationsAccessGroup();
	    }

	    newGroupUnion = manageGroups(toAdd, groupToAddOrRemove, existentGroup);
	    setLessonOccupationsAccessGroup(newGroupUnion);	    
	    break;

	case WRITTEN_EVALUATION_OCCUPATION_ACCESS_GROUP:

	    if(isToMaintainElements) {		
		existentGroup = getWrittenEvaluationOccupationsAccessGroupWithChainOfResponsibility();			
	    } else {
		existentGroup = getWrittenEvaluationOccupationsAccessGroup();
	    }

	    newGroupUnion = manageGroups(toAdd, groupToAddOrRemove, existentGroup);
	    setWrittenEvaluationOccupationsAccessGroup(newGroupUnion);
	    break;

	case GENERIC_EVENT_SPACE_OCCUPATION_ACCESS_GROUP:

	    if(isToMaintainElements) {		
		existentGroup = getGenericEventOccupationsAccessGroupWithChainOfResponsibility();			
	    } else {
		existentGroup = getGenericEventOccupationsAccessGroup();		
	    }

	    newGroupUnion = manageGroups(toAdd, groupToAddOrRemove, existentGroup);
	    setGenericEventOccupationsAccessGroup(newGroupUnion);
	    break;

	default:
	    break;
	}
    }

    private Group manageGroups(Boolean toAdd, Group groupToAddOrRemove, Group existentGroup) {

	List<IGroup> existentGroups = new ArrayList<IGroup>();
	if(existentGroup != null) {	   
	    if(existentGroup instanceof GroupUnion) {
		existentGroups.addAll(((GroupUnion)existentGroup).getChildren());
	    } else {		
		existentGroups.add(existentGroup);
	    }
	} 

	if(toAdd) {	    
	    for (Iterator<IGroup> iter = existentGroups.iterator(); iter.hasNext();) {
		IGroup existentGroup_ = (IGroup) iter.next();
		if(existentGroup_.getClass().equals(groupToAddOrRemove.getClass()) 
			&& existentGroup_.getElements().containsAll(groupToAddOrRemove.getElements())) {
		    iter.remove();
		    existentGroups.remove(groupToAddOrRemove);
		}
	    }
	    existentGroups.add(groupToAddOrRemove);

	} else {		
	    if(groupToAddOrRemove instanceof PersonGroup) {		    
		for (Iterator<IGroup> iter = existentGroups.iterator(); iter.hasNext();) {
		    IGroup existentGroup_ = (IGroup) iter.next();
		    if(existentGroup_.getClass().equals(groupToAddOrRemove.getClass()) 
			    && ((PersonGroup)existentGroup_).getPerson().equals(((PersonGroup)groupToAddOrRemove).getPerson())) {
			iter.remove();
			existentGroups.remove(groupToAddOrRemove);
		    }
		}
	    } else if(groupToAddOrRemove instanceof PersistentGroup) {
		for (Iterator<IGroup> iter = existentGroups.iterator(); iter.hasNext();) {
		    IGroup existentGroup_ = (IGroup) iter.next();
		    if(existentGroup_.getClass().equals(groupToAddOrRemove.getClass()) 
			    && ((PersistentGroup)existentGroup_).getPersistentGroupMembers().equals(((PersistentGroup)groupToAddOrRemove).getPersistentGroupMembers())) {
			iter.remove();
			existentGroups.remove(groupToAddOrRemove);
		    }
		}		    
	    } else if(groupToAddOrRemove instanceof RoleGroup) {
		for (Iterator<IGroup> iter = existentGroups.iterator(); iter.hasNext();) {
		    IGroup existentGroup_ = (IGroup) iter.next();
		    if(existentGroup_.getClass().equals(groupToAddOrRemove.getClass()) 
			    && ((RoleGroup)existentGroup_).getRole().equals(((RoleGroup)groupToAddOrRemove).getRole())) {
			iter.remove();
			existentGroups.remove(groupToAddOrRemove);
		    }
		}		    
	    }			    
	}	
	
	return (Group) (existentGroups.isEmpty() ? null : existentGroups.size() == 1 ? existentGroups.get(0) : new GroupUnion(existentGroups)); 
    }

    private void checkIfPersonAlreadyHasPermissions(Set<Person> persons, boolean toAdd) throws DomainException {
	if (toAdd) {	   
	    for (Person person : persons) {
		if(personHasPermissionsToManageSpace(person)) {
		    throw new DomainException("error.space.access.groups.management.person.already.have.permission");    
		}
	    }	    		  
	}
    }

    private void spaceManagerRoleManagement(Set<Person> elementsToManage, boolean toAdd) {	
	if (toAdd) {
	    for (Person person : elementsToManage) {
		if (!person.hasRole(RoleType.SPACE_MANAGER)) {
		    person.addPersonRoleByRoleType(RoleType.SPACE_MANAGER);
		}
	    }	   
	} else {	    
	    for (Resource resource : RootDomainObject.getInstance().getResources()) {
		if (resource.isSpace()) {
		    Space space = (Space) resource;
		    for (Person person : elementsToManage) {
			if (!space.personHasPermissionToManageExtensionOccupations(person)
				&& !space.personHasPermissionToManagePersonOccupations(person)
				&& !space.personHasPermissionToManageUnitOccupations(person)
				&& !space.personHasSpecialPermissionToManageSpace(person)) {
			    person.removeRoleByType(RoleType.SPACE_MANAGER);
			}			
		    }				    
		}
	    }	    
	}
    }

    public static enum SpaceAccessGroupType {

	PERSON_OCCUPATION_ACCESS_GROUP("personOccupationsAccessGroup"),

	UNIT_OCCUPATION_ACCESS_GROUP("unitOccupationsAccessGroup"),

	EXTENSION_OCCUPATION_ACCESS_GROUP("extensionOccupationsAccessGroup"),

	SPACE_MANAGEMENT_ACCESS_GROUP("spaceManagementAccessGroup"),

	LESSON_OCCUPATION_ACCESS_GROUP("lessonOccupationsAccessGroup"),

	WRITTEN_EVALUATION_OCCUPATION_ACCESS_GROUP("writtenEvaluationOccupationsAccessGroup"),

	GENERIC_EVENT_SPACE_OCCUPATION_ACCESS_GROUP("genericEventOccupationsAccessGroup");

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
	    if(getSuroundingSpace() == null) {
		return (Floor) this;
	    } else if(getSuroundingSpace().isFloor()) {	    
		return getSuroundingSpace().getSpaceFloor();
	    } else {
		return (Floor) this;
	    }		
	}	
	if(getSuroundingSpace() == null) {
	    return null;
	}	
	return getSuroundingSpace().getSpaceFloor();
    }

    public Campus getSpaceCampus() {
	if(isCampus()) {
	    return (Campus) this;
	}	
	if(getSuroundingSpace() == null) {
	    return null;
	}	
	return getSuroundingSpace().getSpaceCampus();
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
    
    public String getResourceAllocationsResume() {	
	StringBuilder builder = new StringBuilder();	
	int eventOccupations = 0, personOccupations = 0, unitOccupations = 0, materialOccupations = 0;	
	for (ResourceAllocation resourceAllocation : getResourceAllocations()) {
	    if(resourceAllocation.isEventSpaceOccupation()) {
		eventOccupations++;
	    } else if(resourceAllocation.isPersonSpaceOccupation()) {
		personOccupations++;
	    } else if(resourceAllocation.isUnitSpaceOccupation()) {
		unitOccupations++;
	    } else if(resourceAllocation.isMaterialSpaceOccupation()) {
		materialOccupations++;
	    }	    
	}
	
	builder.append(eventOccupations).append(" (Events)").append(", ");
	builder.append(personOccupations).append(" (Persons)").append(", ");
	builder.append(unitOccupations).append(" (Units)").append(", ");
	builder.append(materialOccupations).append(" (Material)");
	return builder.toString();
    }  
}

