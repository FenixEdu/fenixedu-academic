package net.sourceforge.fenixedu.domain.messaging;

import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.Node;

public class AnnouncementNode extends AnnouncementNode_Base {

    public AnnouncementNode(final AnnouncementBoard announcementBoard, final Content child) {
        super();
        init(announcementBoard, child, Boolean.TRUE);
    }

    @Override
    public int compareTo(final Node o) {
        final String oid = o == null ? null : o.getExternalId();
        return getExternalId().compareTo(oid);
    }

}
