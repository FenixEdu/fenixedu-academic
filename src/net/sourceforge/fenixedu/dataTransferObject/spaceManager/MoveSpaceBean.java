package net.sourceforge.fenixedu.dataTransferObject.spaceManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.space.Space;

public class MoveSpaceBean implements Serializable {

    private DomainReference<Space> selectedParentSpaceReference;
        
    private DomainReference<Space> spaceReference;
  
    
    public MoveSpaceBean(Space thisSpace) {
	setSpace(thisSpace);
    }     
       
    public void setSelectedParentSpace(Space parentSpace) {
        this.selectedParentSpaceReference = (parentSpace != null) ? new DomainReference<Space>(parentSpace) : null;
    }
    
    public Space getSelectedParentSpace() {
        return (this.selectedParentSpaceReference != null) ? this.selectedParentSpaceReference.getObject() : null;
    }
    
    public void setSpace(Space thisSpace) {
        this.spaceReference = (thisSpace != null) ? new DomainReference<Space>(thisSpace) : null;
    }
    
    public Space getSpace() {
        return (this.spaceReference != null) ? this.spaceReference.getObject() : null;
    }
}
