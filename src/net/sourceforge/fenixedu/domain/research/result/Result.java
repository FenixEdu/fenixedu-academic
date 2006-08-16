package net.sourceforge.fenixedu.domain.research.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation.ResultEventAssociationRole;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
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
    public void setModificationDateAndAuthor() {
        setModifyedBy(AccessControl.getUserView().getPerson().getName());
        setLastModificationDate(new DateTime());
    }

    /**
     * Given a person, creates a result participation with him.
     */ 
    public void setParticipation(Person author){
        ResultParticipationRole role = ResultParticipationRole.getDefaultResultParticipationRole();
        new ResultParticipation(this, author, role, author);
    }
    
    public void setParticipation(Person participator, ResultParticipationRole participatorRole){
        if((participator == null) || (participatorRole == null))
            throw new DomainException("error.publication.neededResultParticipationRole");
        new ResultParticipation(this, participator, participatorRole, participator);
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
     * Returns true if already exists a result participation with the given person and role.
     */   
    public boolean hasPersonParticipationWithRole(Person person, ResultParticipationRole role) {
        for(ResultParticipation participation : this.getResultParticipations()) {
            if(participation.getPerson().equals(person) && participation.getResultParticipationRole().equals(role)) {
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
        removeCountry();
        removeRootDomainObject();
        deleteDomainObject();
    }
    
    /**
     * Returns participations list ordered. 
     */
    public List<ResultParticipation> getOrderedParticipations() {
        
        return sort(this.getResultParticipations());
    }
    
    /**
     * Order result participations by person order.
     */
    public static <T extends ResultParticipation> List<T> sort(Collection<T> resultParticipations) {
        List<T> sorted = new ArrayList<T>(resultParticipations);
        Collections.sort(sorted, new ResultParticipation.OrderComparator());
        
        return sorted;
    }
    
    /**
     * Method responsible for updates on result participations order.
     *  
     * @param result
     * @param newParticipationsOrder - Participations List sorted by new order.
     */
    public void setResultParticipationsOrder(List<ResultParticipation> newParticipationsOrder) {
        int order = 0;
        for (ResultParticipation participation : newParticipationsOrder) {
            int index = this.getResultParticipations().indexOf(participation);
            ResultParticipation aux = this.getResultParticipations().get(index);
            aux.setPersonOrder(order++);
        }
    }
    
    private static class ResultParticipationListener extends dml.runtime.RelationAdapter<Result,ResultParticipation> {
        /**
         * This method is responsible for, after removing a participation from a result, having all 
         * the others participations associated with the same result have their order rearranged.
         * @param removedParticipation: the participation being removed from the result
         * @param result: the result from whom the participation will be removed
         * @see relations.ResultAuthorship_Base#remove(net.sourceforge.fenixedu.domain.research.result.ResultParticipation, net.sourceforge.fenixedu.domain.research.result.Result)
         */
        @Override
        public void afterRemove(Result result, ResultParticipation removedParticipation) {
            if ((removedParticipation!= null) && (result != null)) {
                result.setResultParticipationsOrder(result.getOrderedParticipations());
            }
        }
    }
}
