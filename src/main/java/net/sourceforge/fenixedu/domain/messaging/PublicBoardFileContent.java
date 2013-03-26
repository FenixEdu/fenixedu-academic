package net.sourceforge.fenixedu.domain.messaging;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class PublicBoardFileContent extends PublicBoardFileContent_Base {

    public PublicBoardFileContent() {
        super();
    }

    public PublicBoardFileContent(String fileName, byte[] content, String creatorName, AnnouncementBoard board) {
        super();
        init(getVirtualPath(board), fileName, fileName, createMetaData(creatorName, fileName), content, new EveryoneGroup());
        board.addFile(this);
    }

    private List<FileSetMetaData> createMetaData(String author, String title) {
        List<FileSetMetaData> metaData = new ArrayList<FileSetMetaData>();
        metaData.add(FileSetMetaData.createAuthorMeta(author));
        metaData.add(FileSetMetaData.createTitleMeta(title));
        return metaData;
    }

    private VirtualPath getVirtualPath(AnnouncementBoard board) {

        final VirtualPath filePath = new VirtualPath();
        filePath.addNode(0, new VirtualPathNode("B" + board.getIdInternal(), board.getName().getContent()));
        filePath.addNode(0, new VirtualPathNode("Announcements", "Announcements"));
        return filePath;
    }

}
