package net.sourceforge.fenixedu.domain;

public abstract class UnitSite extends UnitSite_Base {
    
    public UnitSite() {
        super();
    }
    
    @Override
    public String getAlternativeSite() {
        String address = super.getAlternativeSite();
        if (address != null) {
            return address;
        }
        else {
            return getUnit().getWebAddress();
        }
    }

}
