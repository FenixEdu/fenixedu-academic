package net.sourceforge.fenixedu.domain.messaging;

import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.Node;

public class AnnouncementNode extends AnnouncementNode_Base {

    public AnnouncementNode(final AnnouncementBoard announcementBoard, final Content child) {
	super();
	init(announcementBoard, child, Boolean.TRUE);
    }

    public int compareTo(Node o) {
	// TODO Auto-generated method stub
	return 0;
    }

}
