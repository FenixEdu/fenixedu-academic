package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz
 * 
 */
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoBuilding;
import net.sourceforge.fenixedu.domain.space.OldBuilding;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadBuildings extends Service {

    public List run() throws ExcepcaoPersistencia {
        return (List) CollectionUtils.collect(persistentObject.readAll(OldBuilding.class), new Transformer() {
            public Object transform(Object arg0) {
                final OldBuilding building = (OldBuilding) arg0;
                return InfoBuilding.newInfoFromDomain(building);
            }});
    }

}