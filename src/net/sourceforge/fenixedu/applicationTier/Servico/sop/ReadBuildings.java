package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz
 * 
 */
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoBuilding;
import net.sourceforge.fenixedu.domain.IBuilding;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBuilding;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadBuildings implements IService {

    public List run() throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentBuilding persistentBuilding = persistentSupport.getIPersistentBuilding();

        return (List) CollectionUtils.collect(persistentBuilding.readAll(), new Transformer() {
            public Object transform(Object arg0) {
                final IBuilding building = (IBuilding) arg0;
                return InfoBuilding.newInfoFromDomain(building);
            }});
    }

}