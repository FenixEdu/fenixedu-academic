package net.sourceforge.fenixedu.domain.research.result;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

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

    /**
     * Default constructor
     */
    public ResultParticipation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    /**
     * Constructor used to create a ResultParticipation at Result creation
     * time.
     */
    public ResultParticipation(Result result, Person participator, ResultParticipationRole role) {
	this();
	checkParameters(result, participator, role);
	fillAllAttributes(result, participator, role);
    }

    @Override
    @Checked("ResultPredicates.participationWritePredicate")
    public void setPersonOrder(Integer personOrder) {
	super.setPersonOrder(personOrder);
    }

    @Checked("ResultPredicates.participationWritePredicate")
    public void setPersonOrder(OrderChange orderChange) {
	if (orderChange == null) {
	    throw new DomainException("error.researcher.ResultParticipation.orderChange.null");
	}
	move(orderChange);
	setChangedBy();
    }

    @Checked("ResultPredicates.participationWritePredicate")
    public void setEditAll(Result result, Person participator, ResultParticipationRole role) {
	fillAllAttributes(result, participator, role);
	setChangedBy();
    }

    public static ResultParticipation readByOid(Integer oid) {
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
    private void checkParameters(Result result, Person participator, ResultParticipationRole role) {
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
	    throw new DomainException("error.researcher.ResultParticipation.result.invalid.participation.role");
	}
	if (result.hasPersonParticipationWithRole(participator, role)) {
	    throw new DomainException("error.researcher.ResultParticipation.participation.exists");
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

    private void fillAllAttributes(Result result, Person participator, ResultParticipationRole role) {
	super.setResult(result);
	super.setPerson(participator);
	super.setPersonOrder(result.getResultParticipationsCount());
	super.setResultParticipationRole(role);
    }

    private void setPersonOrderAttribute(Integer personOrder) {
	super.setPersonOrder(personOrder);
    }

    private void removeAssociations() {
	super.setResult(null);
	super.setPerson(null);
    }

    @Checked("ResultPredicates.participationWritePredicate")
    public void delete() {
	removeAssociations();
	removeRootDomainObject();
	deleteDomainObject();
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
    public void setResultParticipationRole(ResultParticipationRole role) {
	throw new DomainException("error.researcher.ResultParticipation.call",
		"setResultParticipationRole");
    }

    @Override
    public void removePerson() {
	throw new DomainException("error.researcher.ResultParticipation.call", "removePerson");
    }

    @Override
    public void removeResult() {
	throw new DomainException("error.researcher.ResultParticipation.call", "removeResult");
    }
}
