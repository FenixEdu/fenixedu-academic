package net.sourceforge.fenixedu.domain.accessControl;

import relations.GroupComposition;

public abstract class NodeGroup extends NodeGroup_Base {
    
    public NodeGroup() {
        super();
    }
    
    public void removePart(IUserGroup group)
    {
        for (IUserGroup currentGroup : this.getParts()) {
            if (currentGroup.equals(group))
            {
              GroupComposition.remove(group, this);
              
              if(group.getAggregatorsCount() == 0){
                  group.delete();
              }
              
            }
        }
    }
    
    @Override
    public void delete()
    {
    	for (IUserGroup part : this.getParts())
		{
			GroupComposition.remove(part,this);
		}    	
    	super.delete();
    }
        
}
