package net.sourceforge.fenixedu.dataTransferObject.spaceManager;

import net.sourceforge.fenixedu.dataTransferObject.resourceManager.MaterialBean;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.material.Material;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;

public class MaterialTypeBean extends MaterialBean {

    private DomainReference<SpaceInformation> spaceInformationReference;
        
    private DomainReference<Material> materialReference;
    
    public MaterialTypeBean(SpaceInformation spaceInformationReference) {	
        setSpaceInformation(spaceInformationReference);
    }       
    
    public void setSpaceInformation(SpaceInformation spaceInformationReference) {
        this.spaceInformationReference = (spaceInformationReference != null) ? new DomainReference<SpaceInformation>(spaceInformationReference) : null;
    }
    
    public SpaceInformation getSpaceInformation() {
        return (this.spaceInformationReference != null) ? this.spaceInformationReference.getObject() : null;
    }
    
    public void setMaterial(Material material) {
        this.materialReference = (material != null) ? new DomainReference<Material>(material) : null;
    }
    
    public Material getMaterial() {
        return (this.materialReference != null) ? this.materialReference.getObject() : null;
    } 
}
