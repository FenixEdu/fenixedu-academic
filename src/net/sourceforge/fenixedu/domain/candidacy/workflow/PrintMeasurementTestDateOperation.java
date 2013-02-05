package net.sourceforge.fenixedu.domain.candidacy.workflow;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class PrintMeasurementTestDateOperation extends CandidacyOperation {

    static private final long serialVersionUID = 1L;

    public PrintMeasurementTestDateOperation(Set<RoleType> roleTypes, Candidacy candidacy) {
        super(roleTypes, candidacy);
    }

    public PrintMeasurementTestDateOperation(final RoleType roleType, final Candidacy candidacy) {
        this(Collections.singleton(roleType), candidacy);
    }

    @Override
    protected void internalExecute() {
        // nothing to be done
    }

    @Override
    public CandidacyOperationType getType() {
        return CandidacyOperationType.PRINT_MEASUREMENT_TEST_DATE;
    }

    @Override
    public boolean isInput() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean isAuthorized(final Person person) {
        return super.isAuthorized(person) && person == getCandidacy().getPerson();
    }

}