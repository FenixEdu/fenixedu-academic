package net.sourceforge.fenixedu.domain.contents;

import org.joda.time.DateTime;

public class DateOrderedNode extends DateOrderedNode_Base {
    
    public  DateOrderedNode(Container parent, Content child, Boolean isAscending) {
        super();
        init(parent,child, isAscending);
    }

    public DateTime getCreationDate() {
	return ((IDateContent) getChild()).getContentDate();
    }
    
    public int compareTo(Node node) {
	DateOrderedNode dateNode = (DateOrderedNode)node;
	return (getAscending() ? 1 : -1) * this.getCreationDate().compareTo(dateNode.getCreationDate());
    }

    
}
