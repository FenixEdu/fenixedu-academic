package net.sourceforge.fenixedu.domain.messaging;

import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;

public class PublicBoardFileContent extends PublicBoardFileContent_Base {

    protected PublicBoardFileContent() {
        super();
    }

    public PublicBoardFileContent(String fileName, byte[] content, String creatorName, AnnouncementBoard board) {
        super();
        init(fileName, fileName, content, new EveryoneGroup());
        board.addFile(this);
    }
}
