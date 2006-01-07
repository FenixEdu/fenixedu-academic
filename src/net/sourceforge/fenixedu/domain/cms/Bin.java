package net.sourceforge.fenixedu.domain.cms;

import relations.ContentHierarchy;
public abstract class Bin extends Bin_Base {
    
    public Bin() {
        super();
    }
    
    @Override
    public void delete()
    {
    	for (Content child : this.getChildren())
		{
			ContentHierarchy.remove(child,this);
		}
    	for(Bin parent : this.getParents())
    	{
    		ContentHierarchy.remove(this,parent);
    	}
    	
    	super.delete();
    }
    
}
