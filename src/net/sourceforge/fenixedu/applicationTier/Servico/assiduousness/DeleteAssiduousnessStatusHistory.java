package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;

public class DeleteAssiduousnessStatusHistory extends FenixService {

    public void run(AssiduousnessStatusHistory assiduousnessStatusHistory) {
	if (assiduousnessStatusHistory.isDeletable()) {
	    assiduousnessStatusHistory.delete();
	}
    }

}
