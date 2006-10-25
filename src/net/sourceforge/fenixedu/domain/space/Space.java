package net.sourceforge.fenixedu.domain.space;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.material.Material;

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
	for (MaterialSpaceOccupation materialSpaceOccupation : getMaterialSpaceOccupations()) {
	    if (materialSpaceOccupation.isActive(current) == state
		    && materialSpaceOccupation.getAccessGroup() != null
		    && materialSpaceOccupation.getAccessGroup().isMember(
			    AccessControl.getUserView().getPerson())) {
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

    public void delete() {
	for (; !getContainedSpaces().isEmpty(); getContainedSpaces().get(0).delete())
	    ;
	for (; !getPersonSpaceOccupations().isEmpty(); getPersonSpaceOccupations().get(0).delete())
	    ;
	for (; !getBlueprints().isEmpty(); getBlueprints().get(0).delete())
	    ;
	for (; !getMaterialSpaceOccupations().isEmpty(); getMaterialSpaceOccupations().get(0).delete())
	    ;
	for (final SpaceInformation spaceInformation : getSpaceInformations()) {
	    spaceInformation.deleteMaintainingReferenceToSpace();
	}
	removeSuroundingSpace();
	removeRootDomainObject();
	deleteDomainObject();
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
    
    public static enum SpaceAccessGroupType {

	PERSON_OCCUPATION_ACCESS_GROUP("personOccupationsAccessGroup"),

	EXTENSION_OCCUPATION_ACCESS_GROUP("extensionOccupationsAccessGroup");

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
}
