package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.IPerson;
import relations.CmsContents;
import relations.CmsUsers;

public class Cms extends Cms_Base {
    
    public Cms() {
        super();
    }
    
    public void delete()
    {
    	for (IContent content : this.getContents())
    	{
    		CmsContents.remove(this,content);
    		content.delete();
    	}
    	
    	for (IPerson person : this.getUsers())
		{
			CmsUsers.remove(this,person);
		}
    	
    	this.getConfiguration().delete();    	
    	super.deleteDomainObject();
    }
}
