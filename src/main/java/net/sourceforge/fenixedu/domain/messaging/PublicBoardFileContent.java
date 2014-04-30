package net.sourceforge.fenixedu.domain.messaging;

import org.fenixedu.bennu.core.groups.AnyoneGroup;

public class PublicBoardFileContent extends PublicBoardFileContent_Base {

    protected PublicBoardFileContent() {
        super();
    }

    public PublicBoardFileContent(String fileName, byte[] content, String creatorName, AnnouncementBoard board) {
        super();
        init(fileName, fileName, content, AnyoneGroup.get());
        board.addFileContent(this);
    }
}
