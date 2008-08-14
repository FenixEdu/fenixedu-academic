package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.resource.Resource;

public class ResourceAllocationBean implements Serializable {

    private DomainReference<Resource> resourceReference;

    public ResourceAllocationBean() {

    }

    public Resource getResource() {
	return resourceReference != null ? resourceReference.getObject() : null;
    }

    public void setResource(Resource resource) {
	resourceReference = resource != null ? new DomainReference<Resource>(resource) : null;
    }
}
