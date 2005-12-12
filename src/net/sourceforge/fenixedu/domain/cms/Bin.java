package net.sourceforge.fenixedu.domain.cms;

import relations.ContentHierarchy;
public abstract class Bin extends Bin_Base {
    
    public Bin() {
        super();
    }
    
    @Override
    public void delete()
    {
    	for (IContent child : this.getChildren())
		{
			ContentHierarchy.remove(child,this);
		}
    	for(IBin parent : this.getParents())
    	{
    		ContentHierarchy.remove(this,parent);
    	}
    	
    	super.delete();
    }
    
}
