package org.fenixedu.academic.domain.serviceRequests.documentRequests;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.serviceRequests.ServiceRequestType;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;

public class DocumentPurposeTypeInstance extends DocumentPurposeTypeInstance_Base {
    
    public static Comparator<DocumentPurposeTypeInstance> COMPARE_BY_NAME = new Comparator<DocumentPurposeTypeInstance>(){
        @Override
        public int compare(DocumentPurposeTypeInstance dpti1, DocumentPurposeTypeInstance dpti2) {
            return dpti1.getName().compareTo(dpti2.getName());
        }
    };
    
    public static Comparator<DocumentPurposeTypeInstance> COMPARE_BY_LEGACY = new Comparator<DocumentPurposeTypeInstance>(){
        @Override
        public int compare(DocumentPurposeTypeInstance dpti1, DocumentPurposeTypeInstance dpti2) {
            if (dpti1.getDocumentPurposeType() != null && dpti1.getDocumentPurposeType() == DocumentPurposeType.OTHER) {
                return 1;
            }
            if (dpti2.getDocumentPurposeType() != null && dpti2.getDocumentPurposeType() == DocumentPurposeType.OTHER) {
                return -1;
            }
            return COMPARE_BY_NAME.compare(dpti1, dpti2);
        }
    };

    protected DocumentPurposeTypeInstance() {
        super();
        setBennu(Bennu.getInstance());
    }

    public void delete() {
        setBennu(null);
        deleteDomainObject();
    }
    
    @Atomic
    public void edit(String code, LocalizedString name, DocumentPurposeType type, boolean active, List<ServiceRequestType> serviceRequestTypes) {
        setCode(code);
        setName(name);
        setDocumentPurposeType(type);
        setActive(active);
        for (ServiceRequestType srt : getServiceRequestTypesSet()) {
            removeServiceRequestTypes(srt);
        }
        if (serviceRequestTypes != null) {
            for (ServiceRequestType srt : serviceRequestTypes) {
                addServiceRequestTypes(srt);
            }
        }
    }

    public static DocumentPurposeTypeInstance create(String code, LocalizedString name) {
        if (findUnique(code) != null) {
            throw new IllegalStateException(
                    "DocumentPurposeTypeInstance: could not create new instance because already exists one for the provided code ["
                            + code + "]");
        }
        DocumentPurposeTypeInstance documentPurposeType = new DocumentPurposeTypeInstance();
        documentPurposeType.setCode(code);
        documentPurposeType.setName(name);
        return documentPurposeType;
    }

    public static DocumentPurposeTypeInstance create(String code, LocalizedString name, DocumentPurposeType type) {
        if (findUnique(code) != null) {
            throw new IllegalStateException(
                    "DocumentPurposeTypeInstance: could not create new instance because already exists one for the provided code ["
                            + code + "]");
        }
        if (type != null && findUnique(type) != null) {
            throw new IllegalStateException(
                    "DocumentPurposeTypeInstance: could not create new instance because already exists one for the provided type ["
                            + type.getName() + "]");
        }
        DocumentPurposeTypeInstance documentPurposeType = new DocumentPurposeTypeInstance();
        documentPurposeType.setCode(code);
        documentPurposeType.setName(name);
        documentPurposeType.setDocumentPurposeType(type);
        return documentPurposeType;
    }

    public static Stream<DocumentPurposeTypeInstance> findAll() {
        return Bennu.getInstance().getDocumentPurposeTypeInstancesSet().stream();
    }

    public static DocumentPurposeTypeInstance findUnique(DocumentPurposeType type) {
        return findAll().filter((dpti -> dpti.getDocumentPurposeType() == type)).findFirst().orElse(null);
    }
    
    public static Stream<DocumentPurposeTypeInstance> findActives() {
        return findAll().filter((DocumentPurposeTypeInstance::getActive));
    }
    
    public static DocumentPurposeTypeInstance findUnique(String code) {
        return findAll().filter(dpti -> dpti.getCode().equals(code)).findFirst().orElse(null);
    }
    
    public static Stream<DocumentPurposeTypeInstance> findActivesFor(ServiceRequestType type) {
        return findActives().filter(dpti -> dpti.getServiceRequestTypesSet().contains(type));
    }
}
