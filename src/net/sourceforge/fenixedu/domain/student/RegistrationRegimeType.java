package net.sourceforge.fenixedu.domain.student;


public enum RegistrationRegimeType {
    
    FULL_TIME,
    
    PART_TIME;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return this.getClass().getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return getClass().getName() + "." + name();
    }

    final static public RegistrationRegimeType defaultType() {
	return FULL_TIME;
    }
}
