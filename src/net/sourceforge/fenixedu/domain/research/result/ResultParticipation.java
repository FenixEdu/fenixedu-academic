package net.sourceforge.fenixedu.domain.research.result;

import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.research.result.ChangeResultParticipationsOrder.OrderChange;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationCreationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

public class ResultParticipation extends ResultParticipation_Base {

    /**
     * Roles available for participation
     */
    public enum ResultParticipationRole {
        Author,
        Editor;

        public static ResultParticipationRole getDefaultResultParticipationRole() {
            return Author;
        }
    }

    /**
     * Comparator than can be used to order participations. 
     */
    public static class OrderComparator implements Comparator<ResultParticipation> {

        public int compare(ResultParticipation rP1, ResultParticipation rP2) {
            Integer order1 = rP1.getPersonOrder();
            Integer order2 = rP2.getPersonOrder();
            
            return order1.compareTo(order2);
        }
    }

    /**
     * Default constructor
     */
    public  ResultParticipation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    /**
     * Creates a ResultParticipations with required fields
     */
    public ResultParticipation(Result result, Person person, ResultParticipationRole role, Person changedBy) {
        this();
        checkParameters(result,person, role, changedBy);
        setResult(result);
        setPerson(person);
        setPersonOrder(result.getResultParticipationsCount());
        if(isValidRoleForResult(result,role)) {
            setResultParticipationRole(role);    
        }
        setChangedBy();
    }
    
    /**
     * Verify required fields for constructor.
     */
    private void checkParameters(Result result, Person person, ResultParticipationRole role, Person modifyedBy) {
        if (result == null) {
            throw new DomainException("error.Result.not.found");
        }
        if (person == null) {
            throw new DomainException("error.ResultParticipation.person.cannot.be.null");
        }
        if (modifyedBy == null) {
            throw new DomainException("error.Result.person.not.found");
        }
        if (isValidRoleForResult(result,role)) {
            if(result.hasPersonParticipationWithRole(person, role)) {
                throw new DomainException("error.ResultParticipation.participation.exists");    
            }
        }
        else {
            if(result.hasPersonParticipation(person)) {
                throw new DomainException("error.ResultParticipation.participation.exists");
            }
        }
    }

    /**
     * Verify if role is required for the participation
     */
    private boolean isValidRoleForResult(Result result, ResultParticipationRole role) {
        if((result instanceof ResultPublication) && ((ResultPublication)result).hasResultPublicationRole()) {
            return true;
        }
        return false;
    }
    
    /**
     * Update the last modification date and author name.
     */
    public void setChangedBy() {
        this.getResult().setModificationDateAndAuthor();
    }
    
    /**
     * Returns true if there are more than one participation. 
     */
    public boolean getIsNotLastResultParticipation() {
        return this.getResult().getResultParticipationsCount()>1;
    }

    /**
     * Method used to call the service responsible for creating a ResultParticipation
     * @throws FenixServiceException 
     * @throws FenixFilterException 
     */
    public static void createResultParticipation(ResultParticipationCreationBean bean) throws FenixFilterException, FenixServiceException {
        ServiceUtils.executeService(AccessControl.getUserView(), "CreateResultParticipation", bean);
    }
    
    /**
     * Method used to call the service responsible for changing the participation order
     * @throws FenixServiceException 
     * @throws FenixFilterException 
     */
    public static void changeOrder(ResultParticipation participation, OrderChange orderChange) throws FenixFilterException, FenixServiceException {
        ServiceUtils.executeService(AccessControl.getUserView(), "ChangeResultParticipationsOrder", participation, orderChange);
    }
    
    /**
     * Method used to call the service responsible for saving ResultParticipations order
     * @throws FenixServiceException 
     * @throws FenixFilterException 
     */
    public static void saveResultParticipationsOrder(Result result, List<ResultParticipation> participations) throws FenixFilterException, FenixServiceException {
        ServiceUtils.executeService(AccessControl.getUserView(), "SaveResultParticipationsOrder", result, participations);
    }
    
    /**
     * Method used to call the service responsible for deleting a ResultParticipation
     * @throws FenixServiceException 
     * @throws FenixFilterException 
     */ 
    public static void deleteResultParticipation(ResultParticipation participation) throws FenixFilterException, FenixServiceException {
        ServiceUtils.executeService(AccessControl.getUserView(), "DeleteResultParticipation", participation);
    }
    
    /**
     * Change result participation order.
     */
    protected void orderChange(int offset) {
        final List<ResultParticipation> orderedParticipations = this.getResult().getOrderedParticipations();
        final int newOrder = this.getPersonOrder() + offset;
        
        orderedParticipations.remove(this);
        
        //Participation will be the first element in list.
        if(newOrder < 0){
            orderedParticipations.add(0,this);
        }
        //Participation will be the last element in list.
        else if (newOrder > orderedParticipations.size()) {
            orderedParticipations.add(orderedParticipations.size(), this);
        }
        //Participation will be on the newOrder in list.
        else {
            orderedParticipations.add(newOrder, this);
        }
        
        //ResultParticipation list was re-arranged. Update personOrder slot (0,1,2...).
        int index = 0;
        for (ResultParticipation participation : orderedParticipations) {
            participation.setPersonOrder(index++);
        }
    }
    
    /**
     * Available changes/moves for Result Participations order.
     */
    public void moveUp() {
        orderChange(-1);
    }

    public void moveDown() {
        orderChange(1);
    }

    public void moveTop() {
        orderChange(-Integer.MAX_VALUE);
    }

    public void moveBottom() {
        orderChange(Integer.MAX_VALUE);
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
}
