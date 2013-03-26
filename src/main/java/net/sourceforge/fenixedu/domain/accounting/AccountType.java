package net.sourceforge.fenixedu.domain.accounting;

public enum AccountType {

    INTERNAL, EXTERNAL;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return AccountType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return AccountType.class.getName() + "." + name();
    }
}
