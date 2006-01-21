package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz
 * 
 */
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoBuilding;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadBuildings extends Service {

    public List run() throws ExcepcaoPersistencia {
        return (List) CollectionUtils.collect(persistentSupport.getIPersistentObject().readAll(Building.class), new Transformer() {
            public Object transform(Object arg0) {
                final Building building = (Building) arg0;
                return InfoBuilding.newInfoFromDomain(building);
            }});
    }

}