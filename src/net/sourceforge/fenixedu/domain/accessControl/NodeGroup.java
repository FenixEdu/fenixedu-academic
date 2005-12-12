package net.sourceforge.fenixedu.domain.accessControl;

import relations.GroupComposition;

public abstract class NodeGroup extends NodeGroup_Base {
    
    public NodeGroup() {
        super();
    }
    
    @Override
    public void delete()
    {
    	for (IUserGroup part : this.getParts())
		{
			GroupComposition.remove(part,this);
		}
    	for(INodeGroup aggregator: this.getAggregators())
    	{
    		GroupComposition.remove(this,aggregator);
    	}
    	
    	super.delete();
    }
        
}
