package net.sourceforge.fenixedu.domain.contacts;


public enum PartyContactType {

    PERSONAL,
    
    WORK,
    
    INSTITUTIONAL;
    
    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return PartyContactType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return PartyContactType.class.getName() + "." + name();
    }
    
}
