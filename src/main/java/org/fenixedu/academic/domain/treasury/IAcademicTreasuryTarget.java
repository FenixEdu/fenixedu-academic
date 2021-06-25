package org.fenixedu.academic.domain.treasury;

import java.util.Map;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.DomainObject;

public interface IAcademicTreasuryTarget extends DomainObject {
    
    // Required, must not return null
    public Person getAcademicTreasuryTargetPerson();
    public LocalizedString getAcademicTreasuryTargetDescription();
    
    
    // Optional, may return null
    public Registration getAcademicTreasuryTargetRegistration();
    public ExecutionYear getAcademicTreasuryTargetExecutionYear();
    public ExecutionInterval getAcademicTreasuryTargetExecutionSemester();
    public Degree getAcademicTreasuryTargetDegree();
    
    public Map<String, String> getAcademicTreasuryTargetPropertiesMap();
    
    public LocalDate getAcademicTreasuryTargetEventDate();
    
    public void handleTotalPayment(final IAcademicTreasuryEvent e);
    
    public default boolean isEventAccountedAsTuition() {
        return false;
    }
    
    public default boolean isEventDiscountInTuitionFee() {
        return false;
    }
    
    public default void handleSettlement(final IAcademicTreasuryEvent e) {
        //TODO: considerar isenções sobre dívidas não facturadas
        if(e.isPayed()) {
            handleTotalPayment(e);
        }
    }
    
}
