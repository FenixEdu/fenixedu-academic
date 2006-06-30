package net.sourceforge.fenixedu.domain.research.result;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation.ResultEventAssociationRole;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation.ResultUnitAssociationRole;


public class Result extends Result_Base {
    
    static { ResultParticipationResult.addListener(new ResultParticipationListener()); }

    
    public  Result() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(getClass().getName());
    }
    
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
     * Returns true if exists an association between result and the given event and role.
     */   
    public boolean hasAssociationWithEventRole(Event event, ResultEventAssociationRole role) {
        List<ResultEventAssociation> list = this.getResultEventAssociations();
        for(ResultEventAssociation association : list) {
            if(association.getEvent().equals(event) && association.getRole().equals(role)) {
                return true;    
            }
        }
        return false;
    }
    
    /**
     * Returns true if exists an association between result and the given unit and role.
     */   
    public boolean hasAssociationWithUnitRole(Unit unit, ResultUnitAssociationRole role) {
        List<ResultUnitAssociation> list = this.getResultUnitAssociations();
        for(ResultUnitAssociation association : list) {
            if(association.getUnit().equals(unit) && association.getRole().equals(role)) {
                return true;    
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
    
    /**
     * This method is responsible for checking if the object still has active connections.
     * If not, the object is deleted.
     */
    public void sweep(){
        if (!(this.hasAnyResultParticipations() || this.hasAnyResultEventAssociations() || this.hasAnyResultUnitAssociations())){
            delete();
        }
    }
    
    private static class ResultParticipationListener extends dml.runtime.RelationAdapter<Result,ResultParticipation> {
        
        /*
         * This method is responsible for, after removing an authorship from a result, having all 
         * the others authorships associated with the same result have their order rearranged.
         * @param removedAuthorship: the authorship being removed from the result
         * @param result: the result from whom the authorship will be removed
         * @see relations.ResultAuthorship_Base#remove(net.sourceforge.fenixedu.domain.research.result.Authorship, net.sourceforge.fenixedu.domain.research.result.Result)
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
