package org.fenixedu.academic.domain.serviceRequests;

import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import org.fenixedu.academic.dto.serviceRequests.RegistrationAcademicServiceRequestCreateBean;

public class CustomServiceRequestRequest extends CustomServiceRequestRequest_Base implements IAcademicServiceRequest {
    
    protected CustomServiceRequestRequest() {
        super();
    }

    protected CustomServiceRequestRequest(final RegistrationAcademicServiceRequestCreateBean bean) {
        this();
        super.init(bean);
        
        setNumberOfUnits(bean.getNumberOfUnits());
        setServiceRequestType(bean.getChosenServiceRequestType());
        setRequestedCycle(bean.getRequestedCycle());
    }
    
    @Override
    public boolean isAvailableForTransitedRegistrations() {
        return false;
    }

    @Override
    public boolean isPayedUponCreation() {
        return false;
    }

    @Override
    public boolean isToPrint() {
        return false;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
        return false;
    }

    @Override
    public boolean isManagedWithRectorateSubmissionBatch() {
        return false;
    }

    @Override
    public EventType getEventType() {
        return null;
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return null;
    }

    @Override
    public boolean hasPersonalInfo() {
        return false;
    }
    
    
    public static CustomServiceRequestRequest create(final RegistrationAcademicServiceRequestCreateBean bean) {
        return new CustomServiceRequestRequest(bean);
    }
    
}
