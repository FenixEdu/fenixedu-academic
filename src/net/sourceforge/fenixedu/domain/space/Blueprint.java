package net.sourceforge.fenixedu.domain.space;

import java.text.Collator;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public class Blueprint extends Blueprint_Base {
    
    private static final Comparator BLUEPRINT_COMPARATOR_BY_VALID_UNTIL = new BeanComparator("validUntil", Collator.getInstance());
    
    private Blueprint() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public Blueprint(Space space, BlueprintFile blueprintFile, Person person) {
        this();
        checkParameters(space, blueprintFile, person);
        updateSpaceBlueprintsValidUntilDates(space);
        setSpace(space);
        setBlueprintFile(blueprintFile);
        setCreationPerson(person);        
        super.setValidFrom(new YearMonthDay());        
    }
    
    public void delete() {
        Space space = getSpace();
        removeSpace();               
        removeCreationPerson();          
        removeSpaceBlueprintsValidUntilDates(space);
        getBlueprintFile().delete();
        removeRootDomainObject();
        deleteDomainObject();        
    }

    private void removeSpaceBlueprintsValidUntilDates(Space space) {
        SortedSet<Blueprint> blueprints = new TreeSet<Blueprint>(BLUEPRINT_COMPARATOR_BY_VALID_UNTIL);
        blueprints.addAll(space.getBlueprints());
        if(!blueprints.isEmpty()) {
            blueprints.last().openBlueprint(null);
        }
    }

    private void updateSpaceBlueprintsValidUntilDates(Space space) {
        for (Blueprint blueprint : space.getBlueprints()) {
            if(blueprint.getValidUntil() == null) {
                blueprint.closeBlueprint(new YearMonthDay());
                break;
            }
        }
    }    
  
    private void closeBlueprint(YearMonthDay validFrom) {
        super.setValidFrom(validFrom);
    }
    
    private void openBlueprint(YearMonthDay validUntil) {
        super.setValidUntil(validUntil);
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
}
