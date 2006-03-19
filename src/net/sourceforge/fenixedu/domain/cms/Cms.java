package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class Cms extends Cms_Base {
    
    public Cms() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
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
