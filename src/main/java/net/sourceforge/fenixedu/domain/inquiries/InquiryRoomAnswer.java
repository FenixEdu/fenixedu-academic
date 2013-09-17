package net.sourceforge.fenixedu.domain.inquiries;

public class InquiryRoomAnswer extends InquiryRoomAnswer_Base {

    public InquiryRoomAnswer() {
        super();
    }

    @Deprecated
    public boolean hasRoom() {
        return getRoom() != null;
    }

}
