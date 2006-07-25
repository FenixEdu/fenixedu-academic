package net.sourceforge.fenixedu.domain.research.result;

import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ResultParticipation extends ResultParticipation_Base {
    
    public  ResultParticipation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public ResultParticipation(Result result, Person person, Integer personOrder, ResultParticipationRole role, String modifyedBy) {
        this();
        checkParameters(result,person,personOrder,modifyedBy);
        setResult(result);
        setPerson(person);
        setPersonOrder(personOrder);
        setResultParticipationRole(role);
        setChangedBy(modifyedBy);
    }
    
    private void checkParameters(Result result, Person person, Integer personOrder, String modifyedBy) {
        if (result == null) {
            throw new DomainException("error.Result.not.found");
        }
        if (person == null) {
            throw new DomainException("error.ResultParticipation.person.cannot.be.null");
        }
        if (personOrder == null || personOrder < 0) {
            throw new DomainException("error.ResultParticipation.personOrder.cannot.be.null");
        }
        if (modifyedBy == null || modifyedBy.equals("")) {
            throw new DomainException("error.ResultAssociations.changedBy.null");
        }
        if (result.hasPersonParticipation(person)) {
            throw new DomainException("error.ResultParticipation.participation.exists");
        }
    }

    /** 
     * This method is responsible for updating the last modification date and author of result.
     */
    public void setChangedBy(String personName) {
        this.getResult().setModificationDateAndAuthor(personName);
    }
    
    /**
     * Returns true if there is more then one ResultAuthorship (last one in the list).
     */
    public boolean getIsNotLastResultParticipation() {
        return this.getResult().getResultParticipationsCount()>1;
    }
    
    /**
     * Change result participation order.
     */
    public boolean changeOrder(int offset, String personName) {
        final List<ResultParticipation> resultParticipations = this.getResult().getResultParticipations();
        
        if (offset == 1 || offset == -1) {
            final int oldOrder = this.getPersonOrder();
            final int newOrder = oldOrder + offset;
            
            if (newOrder >= 0 && newOrder < resultParticipations.size()) {
                for (ResultParticipation participation : resultParticipations) {
                    if(participation.getPersonOrder().intValue() == newOrder){
                        participation.setPersonOrder(oldOrder);
                        this.setPersonOrder(newOrder);
                        this.setChangedBy(personName);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /** 
     * This method is responsible for deleting the object.
     */
    public void delete() {
        this.removePerson();
        this.removeResult();

        this.removeRootDomainObject();
        deleteDomainObject();
    }
    
    public void delete(String personName) {
        final Result result = this.getResult();
        
        this.delete();
        
        result.setModificationDateAndAuthor(personName);
    }
    
    public enum ResultParticipationRole {
        AUTHOR,
        EDITOR;

        public static ResultParticipationRole getDefaultResultParticipationRole() {
            return AUTHOR;
        }
    }
}
