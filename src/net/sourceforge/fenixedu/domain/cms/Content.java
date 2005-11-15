package net.sourceforge.fenixedu.domain.cms;

import java.util.Date;
public class Content extends Content_Base implements Comparable<Content>{
    
    public Content() {
        super();
        this.setCreationDate(new Date(System.currentTimeMillis()));
    }

	public int compareTo(Content arg0)
	{
		return this.getCreationDate().compareTo(arg0.getCreationDate());
	}
    
}
