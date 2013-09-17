package net.sourceforge.fenixedu.domain.research.result;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.BundleUtil;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.ResultPredicates;
import pt.ist.fenixframework.FenixFramework;

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
     * Comparator than can be used to order participations. Order: Ascending by
     * personOrder.
     */
    public static class OrderComparator implements Comparator<ResultParticipation> {

        @Override
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
    ResultParticipation(ResearchResult result, Person participator, ResultParticipationRole role, Integer order) {
        this();
        checkParameters(result, participator, role, order);
        fillAllAttributes(result, participator, role, order);
    }

    public void setEditAll(ResearchResult result, Person participator, ResultParticipationRole role, Integer order) {
        check(this, ResultPredicates.participationWritePredicate);
        fillAllAttributes(result, participator, role, order);
        setChangedBy();
    }

    public final void movePersonToDesiredOrder(OrderChange orderChange) {
        check(this, ResultPredicates.participationWritePredicate);
        if (orderChange == null) {
            throw new DomainException("error.researcher.ResultParticipation.orderChange.null");
        }
        move(orderChange);
        setChangedBy();
    }

    @Override
    public void setRole(ResultParticipationRole role) {
        check(this, ResultPredicates.participationWritePredicate);
        if (role == null) {
            throw new DomainException("error.researcher.ResultEventAssociation.role.null");
        }
        if (!this.getRole().equals(role)) {
            if (!this.getResult().hasPersonParticipationWithRole(this.getPerson(), role)) {
                super.setRole(role);
                this.getResult().setModifiedByAndDate();
            } else {
                throw new DomainException("error.researcher.ResultEventAssociation.association.exists", this.getPerson()
                        .getName(), this.getRole().toString());
            }
        }
    }

    public boolean getIsLastParticipation() {
        return (this.getResult().getResultParticipationsSet().size() == 1);
    }

    public boolean getCanBeRemoved() {
        return !(getIsLastParticipation() || this.getPerson().equals(this.getResult().getCreator()));
    }

    public final static ResultParticipation readByOid(String oid) {
        final ResultParticipation participation = FenixFramework.getDomainObject(oid);

        if (participation == null) {
            throw new DomainException("error.researcher.ResultParticipation.null");
        }

        return participation;
    }

    /**
     * Verify required fields for constructor.
     */
    private void checkParameters(ResearchResult result, Person participator, ResultParticipationRole role, Integer order) {
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
        if (order == 0 && result.getResultParticipationsSet().size() > 1) {
            throw new DomainException("error.researcher.ResultParticipation.invalid.order");
        }
    }

    /**
     * Update the last modification date and author name.
     */
    private void setChangedBy() {
        this.getResult().setModifiedByAndDate();
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

    private void fillAllAttributes(ResearchResult result, Person participator, ResultParticipationRole role, Integer order) {
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
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public final void deleteIfNotLast() {
        if (getIsLastParticipation()) {
            throw new DomainException("error.researcher.ResultParticipation.last.one");
        }
        delete();
    }

    /**
     * Blocked setters.
     */
    @Override
    public void setResult(ResearchResult Result) {
        throw new DomainException("error.researcher.ResultParticipation.call", "setResult");
    }

    @Override
    public void setPerson(Person Person) {
        throw new DomainException("error.researcher.ResultParticipation.call", "setPerson");
    }

    /**
     * TODO: Not domain logic!! WARNING
     * 
     * @return
     */
    public Integer getVisualOrder() {
        return this.getPersonOrder() + 1;
    }

    public String getAditionalInfo() {
        Person person = this.getPerson();
        final StringBuilder text = new StringBuilder();
        String unit = BundleUtil.getStringFromResourceBundle("resources.ResearcherResources", "label.unit");

        Employee employee = person.getEmployee();
        if (employee != null && employee.getLastWorkingPlace() != null) {

            text.append(unit + ": " + employee.getLastWorkingPlace().getName());
        } else {
            if (person.hasExternalContract()) {
                text.append(unit + ": " + person.getExternalContract().getInstitutionUnit().getName());
            } else {
                text.append("username: " + person.getUsername());
            }
        }
        return text.toString();
    }
    @Deprecated
    public boolean hasResult() {
        return getResult() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasRole() {
        return getRole() != null;
    }

    @Deprecated
    public boolean hasPersonOrder() {
        return getPersonOrder() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
