package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.PartialList;

public class AssiduousnessExemptionShift extends AssiduousnessExemptionShift_Base {

    public AssiduousnessExemptionShift(AssiduousnessExemption assiduousnessExemption, PartialList partialShift) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousnessExemption(assiduousnessExemption);
	setPartialShift(partialShift);
    }

    public void delete() {
	removeAssiduousnessExemption();
	removeRootDomainObject();
	deleteDomainObject();
    }

}
