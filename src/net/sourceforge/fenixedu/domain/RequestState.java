package net.sourceforge.fenixedu.domain;

public enum RequestState {

    NEW, OPEN, RESOLVED;
    
    public String getName() {
	return name();
    }
}
