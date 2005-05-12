package net.sourceforge.fenixedu.domain.gratuity;

public enum GratuitySituationType {

    DEBTOR,

    CREDITOR,

    REGULARIZED;
    
    public String getName(){
        return name();
    }

}
