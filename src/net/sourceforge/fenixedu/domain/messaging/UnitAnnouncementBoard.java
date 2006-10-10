package net.sourceforge.fenixedu.domain.messaging;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class UnitAnnouncementBoard extends UnitAnnouncementBoard_Base {

    public UnitAnnouncementBoard() {
        super();
    }

    @Override
    public void setParty(Party party) {
        super.setParty((Unit) party);
    }

    public void setUnit(Unit unit) {
        this.setParty(unit);
    }

}
