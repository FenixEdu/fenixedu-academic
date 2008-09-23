package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessExemption;

public class DeleteAssiduousnessExemption extends FenixService {

    public void run(AssiduousnessExemption assiduousnessExemption) {
	assiduousnessExemption.delete();
    }

}
