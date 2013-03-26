package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.AccountingEventsManager;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DfaRegistrationEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;

public class EventGenerator {

    public static void generateNecessaryEvents(StudentCurricularPlan studentCurricularPlan, Person person,
            ExecutionYear executionYear) {

        final AdministrativeOffice administrativeOffice = studentCurricularPlan.getDegree().getAdministrativeOffice();

        switch (studentCurricularPlan.getDegree().getDegreeType()) {

        case BOLONHA_ADVANCED_FORMATION_DIPLOMA:

            final AccountingEventsManager accountingEventsManager = new AccountingEventsManager();
            final ExecutionYear executionYearToCreateEvents =
                    executionYear != null ? executionYear : ExecutionYear.readCurrentExecutionYear();

            accountingEventsManager.createGratuityEvent(studentCurricularPlan, executionYearToCreateEvents, false);

            new DfaRegistrationEvent(administrativeOffice, person, studentCurricularPlan.getRegistration());

            accountingEventsManager.createInsuranceEvent(studentCurricularPlan, executionYearToCreateEvents, false);

            break;
        default:
            break;

        }

    }
}
