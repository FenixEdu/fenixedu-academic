package net.sourceforge.fenixedu.dataTransferObject.spaceManager;

import net.sourceforge.fenixedu.dataTransferObject.resourceManager.MaterialBean;
import net.sourceforge.fenixedu.domain.material.Material;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;

public class MaterialTypeBean extends MaterialBean {

    private SpaceInformation spaceInformationReference;

    private Material materialReference;

    public MaterialTypeBean(SpaceInformation spaceInformationReference) {
        setSpaceInformation(spaceInformationReference);
    }

    public void setSpaceInformation(SpaceInformation spaceInformationReference) {
        this.spaceInformationReference = spaceInformationReference;
    }

    public SpaceInformation getSpaceInformation() {
        return this.spaceInformationReference;
    }

    public void setMaterial(Material material) {
        this.materialReference = material;
    }

    public Material getMaterial() {
        return this.materialReference;
    }
}
