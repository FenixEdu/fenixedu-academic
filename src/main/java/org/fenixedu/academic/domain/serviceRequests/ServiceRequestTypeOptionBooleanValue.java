package org.fenixedu.academic.domain.serviceRequests;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class ServiceRequestTypeOptionBooleanValue extends ServiceRequestTypeOptionBooleanValue_Base {
    
    protected ServiceRequestTypeOptionBooleanValue() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }
    
    protected ServiceRequestTypeOptionBooleanValue(final ServiceRequestTypeOption option, final boolean value) {
        this();
        setServiceRequestTypeOption(option);
        setValue(value);
        
        checkRules();
    }

    private boolean isDeletable() {
        return true;
    }

    private void checkRules() {
        if(getServiceRequestTypeOption() == null) {
            throw new DomainException("error.ServiceRequestTypeOptionBooleanValue.serviceRequestTypeOption.required");
        }
    }
    
    public void delete() {
        if(!isDeletable()) {
            throw new DomainException("error.ServiceRequestTypeOptionBooleanValue.delete.impossible");
        }
        
        setRootDomainObject(null);
        setServiceRequestTypeOption(null);
        
        super.deleteDomainObject();
    }
    
    /*---------
     * SERVICES
     * --------
     */
    
    @Atomic
    public static ServiceRequestTypeOptionBooleanValue create(final ServiceRequestTypeOption option, final boolean value) {
        return new ServiceRequestTypeOptionBooleanValue(option, value);
    }

}
