package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;

public class DeleteAssiduousnessStatusHistory extends Service {

    public void run(AssiduousnessStatusHistory assiduousnessStatusHistory) {
	if (assiduousnessStatusHistory.isDeletable()) {
	    assiduousnessStatusHistory.delete();
	}
    }

}
