package org.fenixedu.academic.domain.accounting.events;

import java.util.Collection;
import java.util.Set;

import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;

public class SpecialSeasonEnrolmentEvent extends SpecialSeasonEnrolmentEvent_Base {
    
    protected SpecialSeasonEnrolmentEvent() {
        super();
    }

    public SpecialSeasonEnrolmentEvent(final AdministrativeOffice administrativeOffice, final Person person,
            final Collection<EnrolmentEvaluation> enrolmentEvaluations) {
        this();
        init(administrativeOffice, EventType.SPECIAL_SEASON_ENROLMENT, person, enrolmentEvaluations);
    }

    @Override
    protected void addAll(Collection<EnrolmentEvaluation> enrolmentEvaluations) {
        getSpecialSeasonEnrolmentEvaluationsSet().addAll(enrolmentEvaluations);
    }

    @Override
    protected Set<EnrolmentEvaluation> getAllEnrolmentEvaluationsSet() {
        return getSpecialSeasonEnrolmentEvaluationsSet();
    }

    @Override
    public boolean isExemptionAppliable() {
        return false;
    }

}
