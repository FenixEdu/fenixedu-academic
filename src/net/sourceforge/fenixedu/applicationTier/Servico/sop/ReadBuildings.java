package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoBuilding;
import net.sourceforge.fenixedu.domain.space.OldBuilding;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadBuildings extends Service {

    public List run() throws ExcepcaoPersistencia {
    	final List<InfoBuilding> result = new ArrayList<InfoBuilding>();
    	for (final OldBuilding oldBuilding : OldBuilding.getOldBuildings()) {
    		result.add(InfoBuilding.newInfoFromDomain(oldBuilding));
    	}
    	return result;
    }

}