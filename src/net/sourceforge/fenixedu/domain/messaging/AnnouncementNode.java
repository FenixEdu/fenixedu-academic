package net.sourceforge.fenixedu.domain.messaging;

import net.sourceforge.fenixedu.domain.contents.Node;


public class AnnouncementNode extends AnnouncementNode_Base {
    
    public AnnouncementNode(final AnnouncementBoard announcementBoard, final Announcement announcement) {
        super();
        init(announcementBoard, announcement, Boolean.TRUE);
    }

    public int compareTo(Node o) {
	// TODO Auto-generated method stub
	return 0;
    }

}
