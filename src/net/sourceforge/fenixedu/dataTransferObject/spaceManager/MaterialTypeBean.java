package net.sourceforge.fenixedu.dataTransferObject.spaceManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;

public class MaterialTypeBean implements Serializable {

    private DomainReference<SpaceInformation> spaceInformationReference;
    
    private Class materialType;

    public MaterialTypeBean(SpaceInformation spaceInformationReference) {
        super();
        this.spaceInformationReference = new DomainReference<SpaceInformation>(spaceInformationReference);
    }       
    
    public Class getMaterialType() {
        return materialType;
    }

    public void setMaterialType(Class materialType) {
        this.materialType = materialType;
    }       
    
    public void setSpaceInformation(SpaceInformation spaceInformationReference) {
        this.spaceInformationReference = (spaceInformationReference != null) ? new DomainReference<SpaceInformation>(spaceInformationReference) : null;
    }
    
    public SpaceInformation getSpaceInformation() {
        return (this.spaceInformationReference != null) ? this.spaceInformationReference.getObject() : null;
    }
}
