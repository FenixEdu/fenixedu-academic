package net.sourceforge.fenixedu.domain.messaging;


public abstract class PartyAnnouncementBoard extends PartyAnnouncementBoard_Base {

    public PartyAnnouncementBoard() {
        super();
    }
    
    @Override
    public String getFullName() {
	return getParty().getName();
    }

    @Override
    public String getQualifiedName() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.getName()).append(" - ").append(this.getParty().getName());
        return buffer.toString();
    }
    
    @Override
    public void delete() {
        removeParty();
        super.delete();
    }
}
