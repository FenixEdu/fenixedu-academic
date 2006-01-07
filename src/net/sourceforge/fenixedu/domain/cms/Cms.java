package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.Person;
import relations.CmsContents;
import relations.CmsUsers;

public class Cms extends Cms_Base {
    
    public Cms() {
        super();
    }
    
    public void delete()
    {
    	for (Content content : this.getContents())
    	{
    		CmsContents.remove(this,content);
    		content.delete();
    	}
    	
    	for (Person person : this.getUsers())
		{
			CmsUsers.remove(this,person);
		}
    	
    	this.getConfiguration().delete();    	
    	super.deleteDomainObject();
    }
}
