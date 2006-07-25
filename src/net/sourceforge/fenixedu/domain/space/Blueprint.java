package net.sourceforge.fenixedu.domain.space;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class Blueprint extends Blueprint_Base implements Comparable<Blueprint> {

    private Blueprint() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public Blueprint(Space space, BlueprintFile blueprintFile, Person person) {
        this();
        checkParameters(space, blueprintFile, person);
        closeCurrentSpaceBlueprint(space);
        setSpace(space);
        setBlueprintFile(blueprintFile);
        setCreationPerson(person);        
        super.setValidFrom(new YearMonthDay());        
    }
    
    public void delete() {
        Space space = getSpace();
        refreshBlueprintsDates(space);        
        removeSpace();               
        removeCreationPerson();                  
        removeBlueprintFile();
        removeRootDomainObject();
        deleteDomainObject();        
    }

    private void refreshBlueprintsDates(Space space) {
        SortedSet<Blueprint> blueprints = new TreeSet<Blueprint>(space.getBlueprints());                
        if(!blueprints.isEmpty() && blueprints.last() != this) {
            for (Iterator<Blueprint> iter = blueprints.iterator(); iter.hasNext(); ) {
                Blueprint blueprint = iter.next();
                if(blueprint == this) {
                    Blueprint nextBlueprint = iter.next();
                    nextBlueprint.updateValidFromDate(blueprint.getValidFrom());
                    break;
                }                
            }
        }
    }

    private void closeCurrentSpaceBlueprint(Space space) {
        SortedSet<Blueprint> blueprints = new TreeSet<Blueprint>(space.getBlueprints());
        if(!blueprints.isEmpty()) {
            blueprints.last().closeBlueprint();
        }
    }    
    
    private void closeBlueprint() {
        super.setValidUntil(new YearMonthDay());
    }
    
    private void updateValidFromDate(YearMonthDay yearMonthDay) {
        super.setValidFrom(yearMonthDay);        
    }
    
    @Override
    public void setValidFrom(YearMonthDay validFrom) {
        throw new DomainException("error.blueprint.invalid.validFrom.date");
    }

    @Override
    public void setValidUntil(YearMonthDay validUntil) {
        throw new DomainException("error.blueprint.invalid.validUntil.date");
    }

    private void checkParameters(Space space, BlueprintFile blueprintFile, Person person) {
       if(space == null) {
           throw new DomainException("error.blueprint.no.space");
       }
       if(blueprintFile == null) {
           throw new DomainException("error.blueprint.no.blueprintFile");
       } 
       if(person == null) {
           throw new DomainException("error.blueprint.no.person");
       } 
    }

    public int compareTo(Blueprint blueprint) {
        if (getValidUntil() == null) {
            return 1;
        } else if (blueprint.getValidUntil() == null) {
            return -1;
        } else {
            return getValidUntil().compareTo(blueprint.getValidUntil());
        }        
    }    
}
