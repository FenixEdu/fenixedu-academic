package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.resource.Resource;

public class ResourceAllocationBean implements Serializable {

    private Resource resourceReference;

    public ResourceAllocationBean() {

    }

    public Resource getResource() {
        return resourceReference;
    }

    public void setResource(Resource resource) {
        resourceReference = resource;
    }
}
