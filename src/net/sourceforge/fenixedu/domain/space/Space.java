package net.sourceforge.fenixedu.domain.space;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.material.Material;

import org.joda.time.YearMonthDay;

import dml.runtime.RelationAdapter;

public abstract class Space extends Space_Base {

    static {
        SpaceSpaceInformation.addListener(new SpaceSpaceInformationListener());
    }

    protected Space() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(this.getClass().getName());
        setCreatedOn(new YearMonthDay());
    }

    public SpaceInformation getSpaceInformation() {
        return getSpaceInformation(null);
    }

    public SpaceInformation getSpaceInformation(final YearMonthDay when) {
        SpaceInformation selectedSpaceInformation = null;

        for (final SpaceInformation spaceInformation : getSpaceInformations()) {
            final YearMonthDay validUntil = spaceInformation.getValidUntil();

            if (validUntil == null) {
                if (selectedSpaceInformation == null) {
                    selectedSpaceInformation = spaceInformation;
                }
            } else {
                if (validUntil.isAfter((when != null) ? when : new YearMonthDay())) {
                    if (selectedSpaceInformation == null
                            || selectedSpaceInformation.getValidUntil() == null
                            || selectedSpaceInformation.getValidUntil().isAfter(validUntil)) {
                        selectedSpaceInformation = spaceInformation;
                    }
                }
            }
        }

        return selectedSpaceInformation;
    }

    public SortedSet<SpaceInformation> getOrderedSpaceInformations() {
        final TreeSet<SpaceInformation> spaceInformations = new TreeSet<SpaceInformation>(
                getSpaceInformations());
        YearMonthDay previousValidUntil = getCreatedOn();
        for (final SpaceInformation spaceInformation : spaceInformations) {
            spaceInformation.setValidFrom(previousValidUntil);
            previousValidUntil = spaceInformation.getValidUntil();
        }
        return spaceInformations;
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
    
    public SortedSet<MaterialSpaceOccupation> getActiveMaterialSpaceOccupations() {
        return getMaterialSpaceOccupationsByState(true);
    }

    public SortedSet<MaterialSpaceOccupation> getInactiveMaterialSpaceOccupations() {
        return getMaterialSpaceOccupationsByState(false);
    }  
    
    private SortedSet<MaterialSpaceOccupation> getMaterialSpaceOccupationsByState(boolean state) {
        SortedSet<MaterialSpaceOccupation> materialOccupations = new TreeSet<MaterialSpaceOccupation>(Material.COMPARATOR_BY_CLASS_NAME);
        YearMonthDay current = new YearMonthDay();
        for (MaterialSpaceOccupation materialSpaceOccupation : getMaterialSpaceOccupations()) {
            if (materialSpaceOccupation.isActive(current) == state) {
                materialOccupations.add(materialSpaceOccupation);
            }
        }
        return materialOccupations;
    }    

    public void delete() {
        for ( ; !getContainedSpaces().isEmpty(); getContainedSpaces().get(0).delete());
        for ( ; !getPersonSpaceOccupations().isEmpty(); getPersonSpaceOccupations().get(0).delete());
        for ( ; !getBlueprints().isEmpty(); getBlueprints().get(0).delete());
        for (final SpaceInformation spaceInformation : getSpaceInformations()) {
            spaceInformation.deleteMaintainingReferenceToSpace();
        }                    
        setSuroundingSpace(null);
        removeRootDomainObject();
        deleteDomainObject();
    }

    public SpaceInformation getMostRecentSpaceInformation() {
        final List<SpaceInformation> spaceInformations = getSpaceInformations();
        SpaceInformation selectedSpaceInformation = null;
        for (final SpaceInformation spaceInformation : spaceInformations) {
            final YearMonthDay validUntil = spaceInformation.getValidUntil();

            if (selectedSpaceInformation == null
                    || validUntil.isAfter(selectedSpaceInformation.getValidUntil())) {
                selectedSpaceInformation = spaceInformation;
            }
        }
        return selectedSpaceInformation;
    }

    public Blueprint getMostRecentBlueprint() {
        SortedSet<Blueprint> orderedBlueprints = getOrderedBlueprints();
        return (!orderedBlueprints.isEmpty()) ? orderedBlueprints.last() : null;
    }

    public static class SpaceSpaceInformationListener extends RelationAdapter<Space, SpaceInformation> {

        @Override
        public void beforeAdd(Space space, SpaceInformation spaceInformation) {
            if (space != null) {
                final YearMonthDay validUntil = new YearMonthDay();
                for (final SpaceInformation otherSpaceInformation : space.getSpaceInformations()) {
                    final YearMonthDay otherValidUntil = otherSpaceInformation.getValidUntil();
                    if (otherValidUntil == null) {
                        otherSpaceInformation.setValidUntil(validUntil);
                    } else if (otherSpaceInformation.getValidUntil().equals(validUntil)) {
                        throw new DomainException("error.existing.space.information.for.current.day");
                    }
                }
            }
        }

        @Override
        public void afterRemove(Space space, SpaceInformation spaceInformation) {
            if (space != null) {
                if (spaceInformation.getValidUntil() == null) {
                    final SpaceInformation nextMostRecentSpaceInformation = space
                            .getMostRecentSpaceInformation();
                    if (nextMostRecentSpaceInformation != null) {
                        nextMostRecentSpaceInformation.setValidUntil(null);
                    }
                }
            }
        }

    }

}
