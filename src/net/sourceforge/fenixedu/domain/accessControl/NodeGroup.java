package net.sourceforge.fenixedu.domain.accessControl;

import relations.GroupComposition;

public abstract class NodeGroup extends NodeGroup_Base {
    
    public NodeGroup() {
        super();
    }
    
    public void removePart(UserGroup group)
    {
        for (UserGroup currentGroup : this.getParts()) {
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
    	for (UserGroup part : this.getParts())
		{
			GroupComposition.remove(part,this);
		}    	
    	super.delete();
    }
        
}
