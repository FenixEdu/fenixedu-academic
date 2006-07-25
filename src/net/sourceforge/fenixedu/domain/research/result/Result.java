package net.sourceforge.fenixedu.domain.research.result;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation.ResultEventAssociationRole;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation.ResultUnitAssociationRole;

import org.joda.time.DateTime;


public class Result extends Result_Base {
    
    static { ResultParticipationResult.addListener(new ResultParticipationListener()); }

    
    public  Result() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(getClass().getName());
    }
    
    /**
     * This method is used when the result data is changed. Keeps record of the last modification 
     * date and author name.
     */
    public void setModificationDateAndAuthor(String personName) {
        setModifyedBy(personName);
        setLastModificationDate(new DateTime());
    }

    
    /**
     * Given a person, creates a result participation with him.
     */ 
    public void setParticipation(Person author){
        ResultParticipation resultParticipation = new ResultParticipation();
        
        resultParticipation.setPerson(author);
        resultParticipation.setResult(this);
        int order = this.getResultParticipationsCount();
        resultParticipation.setPersonOrder(new Integer(order));
    }

    /**
     * Given a list of persons, creates associations between result and person (authorships)
     */ 
    public void setParticipations(List<Person> authors) {
        /* Remove all references to old authors */
        for (;this.hasAnyResultParticipations(); this.getResultParticipations().get(0).delete());
        
	    for (Person person : authors) {
	        final ResultParticipation resultParticipation = new ResultParticipation();
            
            resultParticipation.setPerson(person);
            resultParticipation.setResult(this);
            resultParticipation.setPersonOrder(new Integer(authors.indexOf(person)));	    }
	}

    /**
     * Returns a list of persons (authors of result)
     */ 
    public List<Person> getParticipations(){
        List<Person> personsList = new ArrayList<Person>();
        
        for (ResultParticipation resultParticipation : getResultParticipations()) {
            personsList.add(resultParticipation.getPerson());
        }
        return personsList;
    }
    
    /**
     * Returns true if already exists a result participation with the given person.
     */   
    public boolean hasPersonParticipation(Person person) {
        for(ResultParticipation participation : this.getResultParticipations()) {
            if(participation.getPerson().equals(person)) {
                return true;    
            }
        }
        return false;
    }    

    /**
     * Returns true if exists an association between result and the given event and role.
     */   
    public boolean hasAssociationWithEventRole(Event event, ResultEventAssociationRole role) {
        if (event != null && role!=null) {
            final List<ResultEventAssociation> list = this.getResultEventAssociations();
        
            for(ResultEventAssociation association : list) {
                if(association.getEvent().equals(event) && association.getRole().equals(role)) {
                    return true;    
                }
            }
        }
        return false;
    }
    
    /**
     * Returns true if exists an association between result and the given unit and role.
     */   
    public boolean hasAssociationWithUnitRole(Unit unit, ResultUnitAssociationRole role) {
        if (unit!=null && role!=null){
            final List<ResultUnitAssociation> list = this.getResultUnitAssociations();
        
            for(ResultUnitAssociation association : list) {
                if(association.getUnit().equals(unit) && association.getRole().equals(role)) {
                    return true;    
                }
            }
        }
        return false;
    }
    
    /**
     * This method is responsible for deleting the object and all its references.
     */
    public void delete(){
        for (;this.hasAnyResultParticipations(); this.getResultParticipations().get(0).delete());
        for (;this.hasAnyResultEventAssociations(); this.getResultEventAssociations().get(0).delete());
        for (;this.hasAnyResultUnitAssociations(); this.getResultUnitAssociations().get(0).delete());
        removeRootDomainObject();
        deleteDomainObject();
    }
    
    private static class ResultParticipationListener extends dml.runtime.RelationAdapter<Result,ResultParticipation> {
        /*
         * This method is responsible for, after removing a participation from a result, having all 
         * the others participations associated with the same result have their order rearranged.
         * @param removedParticipation: the participation being removed from the result
         * @param result: the result from whom the participation will be removed
         * @see relations.ResultAuthorship_Base#remove(net.sourceforge.fenixedu.domain.research.result.ResultParticipation, net.sourceforge.fenixedu.domain.research.result.Result)
         */
        @Override
        public void afterRemove(Result result, ResultParticipation removedParticipation) {
            if ((removedParticipation!= null) && (result != null)) {
                int removedOrder = removedParticipation.getPersonOrder();
                for(ResultParticipation participation : result.getResultParticipations()) {
                    if (participation.getPersonOrder() > removedOrder) {
                        participation.setPersonOrder(participation.getPersonOrder()-1);
                    }
                }
            }
        }
    }
}
