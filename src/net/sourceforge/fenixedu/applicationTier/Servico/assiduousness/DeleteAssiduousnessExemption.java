package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessExemption;

public class DeleteAssiduousnessExemption extends Service {

    public void run(AssiduousnessExemption assiduousnessExemption) {
	assiduousnessExemption.delete();
    }

}
