package net.sourceforge.fenixedu.domain.research.result;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class ResultParticipation extends ResultParticipation_Base {

    public enum ResultParticipationRole {
	Author, Editor;

	public static ResultParticipationRole getDefaultRole() {
	    return Author;
	}
    }

    public static enum OrderChange {
	MoveUp, MoveDown, MoveTop, MoveBottom;

	public static int getOffset(OrderChange order) {
	    switch (order) {
	    case MoveUp:
		return -1;
	    case MoveDown:
		return 1;
	    case MoveTop:
		return -Integer.MAX_VALUE;
	    case MoveBottom:
		return Integer.MAX_VALUE;
	    default:
		return 0;
	    }
	}
    }

    /**
     * Comparator than can be used to order participations. Order: Ascending
     * by personOrder.
     */
    public static class OrderComparator implements Comparator<ResultParticipation> {

	public int compare(ResultParticipation rP1, ResultParticipation rP2) {
	    Integer order1 = rP1.getPersonOrder();
	    Integer order2 = rP2.getPersonOrder();

	    return order1.compareTo(order2);
	}
    }

    private ResultParticipation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    /**
     * Constructor used to create a ResultParticipation.
     */
    ResultParticipation(Result result, Person participator, ResultParticipationRole role, Integer order) {
	this();
	checkParameters(result, participator, role, order);
	fillAllAttributes(result, participator, role, order);
    }

    @Checked("ResultPredicates.participationWritePredicate")
    public void setEditAll(Result result, Person participator, ResultParticipationRole role, Integer order) {
	fillAllAttributes(result, participator, role, order);
	setChangedBy();
    }
    
    @Checked("ResultPredicates.participationWritePredicate")
    public final void movePersonToDesiredOrder(OrderChange orderChange) {
	if (orderChange == null) {
	    throw new DomainException("error.researcher.ResultParticipation.orderChange.null");
	}
	move(orderChange);
	setChangedBy();
    }
    
    @Override
    @Checked("ResultPredicates.participationWritePredicate")
    public void setRole(ResultParticipationRole role) {
	if (role==null) {
	    throw new DomainException("error.researcher.ResultEventAssociation.role.null");
	}
	if(!this.getRole().equals(role)) {
	    if (!this.getResult().hasPersonParticipationWithRole(this.getPerson(), role)) {
		super.setRole(role);
		this.getResult().setModifyedByAndDate();
	    } else {
		throw new DomainException("error.researcher.ResultEventAssociation.association.exists",
			this.getPerson().getName(), this.getRole().toString());
	    }
	}
    }
    
    public boolean getIsLastParticipation() {
	return (this.getResult().getResultParticipationsCount() == 1);
    }

    public final static ResultParticipation readByOid(Integer oid) {
	final ResultParticipation participation = RootDomainObject.getInstance()
		.readResultParticipationByOID(oid);

	if (participation == null) {
	    throw new DomainException("error.researcher.ResultParticipation.null");
	}

	return participation;
    }

    /**
     * Verify required fields for constructor.
     */
    private void checkParameters(Result result, Person participator, ResultParticipationRole role, Integer order) {
	if (result == null) {
	    throw new DomainException("error.researcher.ResultParticipation.result.null");
	}
	if (participator == null) {
	    throw new DomainException("error.researcher.ResultParticipation.person.null");
	}
	if (role == null) {
	    throw new DomainException("error.researcher.ResultParticipation.role.null");
	}
	if (!result.acceptsParticipationRole(role)) {
	    throw new DomainException(
		    "error.researcher.ResultParticipation.result.invalid.participation.role");
	}
	if (result.hasPersonParticipationWithRole(participator, role)) {
	    throw new DomainException("error.researcher.ResultParticipation.participation.exists");
	}
	if(order==0 && result.getResultParticipationsCount()>1) {
	    throw new DomainException("error.researcher.ResultParticipation.invalid.order");
	}
    }

    /**
     * Update the last modification date and author name.
     */
    private void setChangedBy() {
	this.getResult().setModifyedByAndDate();
    }

    private void move(OrderChange change) {
	final int newOrder = this.getPersonOrder() + OrderChange.getOffset(change);
	final List<ResultParticipation> originalList = this.getResult().getOrderedResultParticipations();
	List<ResultParticipation> orderedParticipations = new ArrayList<ResultParticipation>();
	orderedParticipations.addAll(originalList);

	orderedParticipations.remove(this);

	// Participation will be the first element in list.
	if (newOrder < 0) {
	    orderedParticipations.add(0, this);
	}
	// Participation will be the last element in list.
	else if (newOrder > orderedParticipations.size()) {
	    orderedParticipations.add(orderedParticipations.size(), this);
	}
	// Participation will be on the newOrder in list.
	else {
	    orderedParticipations.add(newOrder, this);
	}

	// ResultParticipation list was re-arranged. Update personOrder slot
	// (0,1,2...).
	int index = 0;
	for (ResultParticipation participation : orderedParticipations) {
	    originalList.get(originalList.indexOf(participation)).setPersonOrderAttribute(index++);
	}
    }

    private void fillAllAttributes(Result result, Person participator, ResultParticipationRole role, Integer order) {
	super.setResult(result);
	super.setPerson(participator);
	super.setPersonOrder(order);
	super.setRole(role);
    }

    private void setPersonOrderAttribute(Integer personOrder) {
	super.setPersonOrder(personOrder);
    }

    private void removeAssociations() {
	super.setResult(null);
	super.setPerson(null);
    }

    public final void delete() {
	removeAssociations();
	removeRootDomainObject();
	deleteDomainObject();
    }
    
    public final void deleteIfNotLast() {
	if(getIsLastParticipation()) {
	    throw new DomainException("error.researcher.ResultParticipation.last.one");
	}
	delete();
    }

    /**
     * Blocked setters.
     */
    @Override
    public void setResult(Result Result) {
	throw new DomainException("error.researcher.ResultParticipation.call", "setResult");
    }

    @Override
    public void setPerson(Person Person) {
	throw new DomainException("error.researcher.ResultParticipation.call", "setPerson");
    }

    @Override
    public void removePerson() {
	throw new DomainException("error.researcher.ResultParticipation.call", "removePerson");
    }

    @Override
    public void removeResult() {
	throw new DomainException("error.researcher.ResultParticipation.call", "removeResult");
    }
 
    /**
     * TODO: Not domain logic!! WARNING
     * @return
     */
    public Integer getVisualOrder() {
	return this.getPersonOrder() + 1;
    }

    public String getAditionalInfo() {
	final Person person = this.getPerson();
	final ExternalPerson externalPerson = person.getExternalPerson();
	final String username = person.getUsername();
	
	if (externalPerson != null) {
	    final Unit unit = externalPerson.getInstitutionUnit();
	    
	    return (unit != null && unit.getName()!=null && !unit.getName().equals("")) ? 
		    "(Org: " + unit.getName() + ")" : "(Org: ---)";
	}
	return ((username!=null && !username.equals(""))) ? "(User:" + username + ")" : "(User: ---)";
    }
}
