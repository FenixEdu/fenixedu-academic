package net.sourceforge.fenixedu.domain.space;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

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
    	final TreeSet<SpaceInformation> spaceInformations = new TreeSet<SpaceInformation>(getSpaceInformations());
    	YearMonthDay previousValidUntil = getCreatedOn();
    	for (final SpaceInformation spaceInformation : spaceInformations) {
    		spaceInformation.setValidFrom(previousValidUntil);
    		previousValidUntil = spaceInformation.getValidUntil();
    	}
    	return spaceInformations;
    }
    
    public SortedSet<PersonSpaceOccupation> getActivePersonSpaceOccupations(){
        SortedSet<PersonSpaceOccupation> personSpaceOccupations = new TreeSet<PersonSpaceOccupation>(PersonSpaceOccupation.COMPARATOR_BY_PERSON_NAME_AND_OCCUPATION_INTERVAL);
        YearMonthDay current = new YearMonthDay();
        for (SpaceOccupation spaceOccupation : getSpaceOccupations()) {
            if(spaceOccupation instanceof PersonSpaceOccupation && ((PersonSpaceOccupation)spaceOccupation).contains(current)) {
                personSpaceOccupations.add((PersonSpaceOccupation)spaceOccupation);
            }
        }        
        return personSpaceOccupations;
    }
    
    public SortedSet<PersonSpaceOccupation> getInactivePersonSpaceOccupations(){
        SortedSet<PersonSpaceOccupation> personSpaceOccupations = new TreeSet<PersonSpaceOccupation>(PersonSpaceOccupation.COMPARATOR_BY_PERSON_NAME_AND_OCCUPATION_INTERVAL);
        YearMonthDay current = new YearMonthDay();
        for (SpaceOccupation spaceOccupation : getSpaceOccupations()) {
            if(spaceOccupation instanceof PersonSpaceOccupation && !((PersonSpaceOccupation)spaceOccupation).contains(current)) {
                personSpaceOccupations.add((PersonSpaceOccupation)spaceOccupation);
            }
        }  
        return personSpaceOccupations;
    }
    
    public SortedSet<SpaceResponsibility> getActiveSpaceResponsibility(){
        SortedSet<SpaceResponsibility> spaceResponsibility = new TreeSet<SpaceResponsibility>(SpaceResponsibility.COMPARATOR_BY_UNIT_NAME_AND_RESPONSIBILITY_INTERVAL);
        YearMonthDay current = new YearMonthDay();
        for (SpaceResponsibility responsibility : getSpaceResponsibilitySet()) {
            if(responsibility.isActive(current)) {
                spaceResponsibility.add(responsibility);
            }
        }        
        return spaceResponsibility;
    }
    
    public SortedSet<SpaceResponsibility> getInactiveSpaceResponsibility(){
        SortedSet<SpaceResponsibility> spaceResponsibility = new TreeSet<SpaceResponsibility>(SpaceResponsibility.COMPARATOR_BY_UNIT_NAME_AND_RESPONSIBILITY_INTERVAL);
        YearMonthDay current = new YearMonthDay();
        for (SpaceResponsibility responsibility : getSpaceResponsibilitySet()) {
            if(!responsibility.isActive(current)) {
                spaceResponsibility.add(responsibility);
            }
        }        
        return spaceResponsibility;
    }
    
    public void delete() {
        for (final List<Space> containedSpaces = getContainedSpaces();
                !containedSpaces.isEmpty(); containedSpaces.get(0).delete());
        for (final SpaceInformation spaceInformation : getSpaceInformations()) {
            spaceInformation.deleteMaintainingReferenceToSpace();
        }
        setSuroundingSpace(null);
        removeRootDomainObject();
        deleteDomainObject();
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
                    final SpaceInformation nextMostRecentSpaceInformation = findMostRecentSpaceInformation(space.getSpaceInformations());
                    if (nextMostRecentSpaceInformation != null) {
                        nextMostRecentSpaceInformation.setValidUntil(null);
                    }
                }
            }
        }

        private static SpaceInformation findMostRecentSpaceInformation(final List<SpaceInformation> spaceInformations) {
            SpaceInformation selectedSpaceInformation = null;

            for (final SpaceInformation spaceInformation : spaceInformations) {
                final YearMonthDay validUntil = spaceInformation.getValidUntil();

                if (selectedSpaceInformation == null || validUntil.isAfter(selectedSpaceInformation.getValidUntil())) {
                    selectedSpaceInformation = spaceInformation;
                }
            }

            return selectedSpaceInformation;
        }

    }

}
