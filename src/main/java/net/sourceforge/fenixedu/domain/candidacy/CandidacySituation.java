package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.workflow.CandidacyOperation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.workflow.IState;
import net.sourceforge.fenixedu.domain.util.workflow.IStateWithOperations;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;
import net.sourceforge.fenixedu.domain.util.workflow.StateBean;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.Language;

// FIXME: Rename to CandidacyState
public abstract class CandidacySituation extends CandidacySituation_Base implements IStateWithOperations {

    public static Comparator<CandidacySituation> DATE_COMPARATOR = new Comparator<CandidacySituation>() {
        @Override
        public int compare(CandidacySituation leftCandidacySituation, CandidacySituation rightCandidacySituation) {
            int comparationResult = leftCandidacySituation.getSituationDate().compareTo(rightCandidacySituation.getSituationDate());
            if (comparationResult == 0) {
                final CandidacySituationType leftType = leftCandidacySituation.getCandidacySituationType();
                final CandidacySituationType rightType = rightCandidacySituation.getCandidacySituationType();
                return leftType.compareTo(rightType);               
            }
            return comparationResult;
        }
    };

    protected CandidacySituation() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setSituationDate(new DateTime());
    }

    protected final void init(final Candidacy candidacy, final Person person) {
        checkParameters(candidacy, person);
        super.setCandidacy(candidacy);
        super.setPerson(person);
        if (canExecuteOperationAutomatically()) {
            try {
                // This is to be able to save the candidacy situations dates
                // different along the candidacy workflow.
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new DomainException("error.candidacy.CandidacySituation.cannot.execute.operation");
            }
            getOperations().iterator().next().execute(person);
        }
    }

    private Set<Operation> getOperations() {
        return getCandidacy().getOperations(this);
    }

    public void executeSingleOperation() {

    }

    private void checkParameters(Candidacy candidacy, Person person) {
        if (candidacy == null) {
            throw new DomainException("error.candidacy.CandidacySituation.candidacy.cannot.be.null");
        }

        if (person == null) {
            throw new DomainException("error.candidacy.CandidacySituation.person.cannot.be.null");
        }
    }

    public String getDescription() {
        return ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale()).getString(
                getCandidacySituationType().getQualifiedName());
    }

    public boolean canChangePersonalData() {
        return false;
    }

    public boolean getCanCandidacyDataBeValidated() {
        return false;
    }

    public boolean getCanGeneratePass() {
        return true;
    }

    public boolean getCanRegister() {
        return false;
    }

    @Override
    public Collection<Operation> getOperationsForPerson(Person person) {
        final Collection<Operation> operationsForPerson = new HashSet<Operation>();
        for (final Operation operation : getOperations()) {
            if (operation.isAuthorized(person)) {
                operationsForPerson.add(operation);
            }
        }

        return operationsForPerson;
    }

    public Operation getOperationByTypeAndPerson(CandidacyOperationType type, Person person) {
        for (final Operation operation : getOperationsForPerson(person)) {
            if (((CandidacyOperation) operation).getType() == type) {
                return operation;
            }
        }

        return null;
    }

    @Override
    public void onOperationFinished(Operation operation, Person person) {
        getCandidacy().moveToNextState(((CandidacyOperation) operation).getType(), person);
    }

    @Override
    public IState nextState() {
        return this.getCandidacy().nextState();
    }

    @Override
    public IState nextState(final StateBean bean) {
        return this.getCandidacy().nextState(bean.getNextState());
    }

    @Override
    public Set<String> getValidNextStates() {
        return this.getCandidacy().getValidNextStates();
    }

    @Override
    public void checkConditionsToForward() {
        this.getCandidacy().checkConditionsToForward();
    }

    @Override
    public void checkConditionsToForward(final StateBean bean) {
        this.getCandidacy().checkConditionsToForward(bean.getNextState());
    }

    public abstract CandidacySituationType getCandidacySituationType();

    public abstract boolean canExecuteOperationAutomatically();

    public void delete() {
        setCandidacy(null);
        setPerson(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasCandidacy() {
        return getCandidacy() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSituationDate() {
        return getSituationDate() != null;
    }

    @Deprecated
    public boolean hasRemarks() {
        return getRemarks() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
