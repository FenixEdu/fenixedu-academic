package net.sourceforge.fenixedu.domain.administrativeOffice;

public enum AdministrativeOfficeType {
    
    DEGREE, MASTER_DEGREE;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return AdministrativeOfficeType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return AdministrativeOfficeType.class.getName() + "." + name();
    }

}
