package org.fenixedu.academic.domain.serviceRequests;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

public class ServiceRequestTypeOption extends ServiceRequestTypeOption_Base {
    
    protected ServiceRequestTypeOption() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }
    
    protected ServiceRequestTypeOption(final String code, final LocalizedString name, final boolean detailedOption, final boolean numberOfUnitsOption) {
        this();
        
        super.setCode(code);
        super.setName(name);
        super.setDetailedOption(detailedOption);
        super.setNumberOfUnitsOption(numberOfUnitsOption);
        
        checkRules();
    }

    private void checkRules() {
        if(Strings.isNullOrEmpty(getCode())) {
            throw new DomainException("error.ServiceRequestTypeOption.code.required");
        }
        
        if(isTrimmedEmpty(getName())) {
            throw new DomainException("error.ServiceRequestTypeOption.name.required");            
        }
    }
    
    @Atomic
    public void edit(final String code, final LocalizedString name) {
        super.setCode(code);
        super.setName(name);
        
        checkRules();
    }
    
    public static boolean isTrimmedEmpty(final LocalizedString localizedString) {
        if(localizedString == null || localizedString.getLocales().isEmpty()) {
            return true;
        }
        
        boolean empty = true;
        for (final Locale locale : localizedString.getLocales()) {
            empty &= Strings.isNullOrEmpty(localizedString.getContent(locale));
        }
        
        return empty;
    }
    
    public boolean isDetailedOption() {
        return getDetailedOption();
    }
    
    public boolean isNumberOfUnitsOption() {
        return getNumberOfUnitsOption();
    }
    
    /*---------
     * SERVICES
     * --------
     */
    
    public static Map<ServiceRequestTypeOption, Boolean> optionValuesMap(final AcademicServiceRequest academicServiceRequest) {
        final Set<ServiceRequestTypeOptionBooleanValue> values = academicServiceRequest.getServiceRequestTypeOptionBooleanValuesSet();
        
        final Map<ServiceRequestTypeOption, Boolean> result = Maps.newHashMap();
        for (ServiceRequestTypeOptionBooleanValue serviceRequestTypeOptionBooleanValue : values) {
            result.put(serviceRequestTypeOptionBooleanValue.getServiceRequestTypeOption(), serviceRequestTypeOptionBooleanValue.getValue());
        }
        
        return result;
    }
    
    public static Stream<ServiceRequestTypeOption> findAll() {
        return Bennu.getInstance().getServiceRequestTypeOptionsSet().stream();
    }
    
    public static Optional<ServiceRequestTypeOption> findDetailedOption() {
        return findAll().filter(s -> s.isDetailedOption()).findFirst();
    }
    
    public static Optional<ServiceRequestTypeOption> findNumberOfUnitsOption() {
        return findAll().filter(ServiceRequestTypeOption::isNumberOfUnitsOption).findFirst();
    }
    
    @Atomic
    public static ServiceRequestTypeOption create(final String code, final LocalizedString name, final boolean detailedOption, final boolean numberOfUnitsOption) {
        return new ServiceRequestTypeOption(code, name, detailedOption, numberOfUnitsOption);
    }
    
}
