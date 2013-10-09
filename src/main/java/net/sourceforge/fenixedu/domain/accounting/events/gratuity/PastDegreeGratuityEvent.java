package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.Money;

public class PastDegreeGratuityEvent extends PastDegreeGratuityEvent_Base {

    protected PastDegreeGratuityEvent() {
        super();
    }

    public PastDegreeGratuityEvent(final AdministrativeOffice administrativeOffice, final Person person,
            final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear,
            final Money pastDegreeGratuityAmount) {
        this();

        init(administrativeOffice, person, studentCurricularPlan, executionYear, pastDegreeGratuityAmount);

    }

    private void init(AdministrativeOffice administrativeOffice, Person person, StudentCurricularPlan studentCurricularPlan,
            ExecutionYear executionYear, Money pastDegreeGratuityAmount) {
        super.init(administrativeOffice, person, studentCurricularPlan, executionYear);
        checkParameters(studentCurricularPlan, pastDegreeGratuityAmount);
        super.setPastDegreeGratuityAmount(pastDegreeGratuityAmount);
    }

    private void checkParameters(StudentCurricularPlan studentCurricularPlan, Money pastDegreeGratuityAmount) {
        if (studentCurricularPlan.getDegreeType() != DegreeType.DEGREE) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.PastDegreeGratuityEvent.invalid.degree.type.for.student.curricular.plan");
        }

        if (pastDegreeGratuityAmount == null || pastDegreeGratuityAmount.isZero()) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.PastDegreeGratuityEvent.pastDegreeGratuityAmount.cannot.be.null.and.must.be.greather.than.zero");
        }

    }

    @Override
    public void setPastDegreeGratuityAmount(Money pastDegreeGratuityAmount) {
        check(this, RolePredicates.MANAGER_PREDICATE);
        super.setPastDegreeGratuityAmount(pastDegreeGratuityAmount);
    }

    @Override
    public boolean isExemptionAppliable() {
        return true;
    }

    @Override
    public Set<EntryType> getPossibleEntryTypesForDeposit() {
        return Collections.singleton(EntryType.GRATUITY_FEE);
    }

    @Deprecated
    public boolean hasPastDegreeGratuityAmount() {
        return getPastDegreeGratuityAmount() != null;
    }

}
