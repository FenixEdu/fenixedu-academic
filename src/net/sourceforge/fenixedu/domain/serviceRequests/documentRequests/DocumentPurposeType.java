package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

public enum DocumentPurposeType {
    FAMILY_ALLOWANCE, PUBLIC_TRANSPORTS, MILITARY, PROFESSIONAL, OTHER;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return DocumentPurposeType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return DocumentPurposeType.class.getName() + "." + name();
    }
}
