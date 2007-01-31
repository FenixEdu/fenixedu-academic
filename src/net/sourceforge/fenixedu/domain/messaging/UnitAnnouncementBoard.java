package net.sourceforge.fenixedu.domain.messaging;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class UnitAnnouncementBoard extends UnitAnnouncementBoard_Base {

    public UnitAnnouncementBoard(Unit unit) {
        super();
        
        setUnit(unit);
    }

    @Override
    public void setParty(Party party) {
        super.setParty((Unit) party);
    }

    @Override
    public Unit getParty() {
        return (Unit) super.getParty();
    }
    
    public void setUnit(Unit unit) {
        this.setParty(unit);
    }

    public Unit getUnit() {
        return getParty();
    }
}
