package ServidorAplicacao.Servico.sop;

/**
 * @author Luis Cruz
 * 
 */
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoBuilding;
import Dominio.IBuilding;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBuilding;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadBuildings implements IService {

    public List run() throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        final IPersistentBuilding persistentBuilding = persistentSupport.getIPersistentBuilding();

        return (List) CollectionUtils.collect(persistentBuilding.readAll(), new Transformer() {
            public Object transform(Object arg0) {
                final IBuilding building = (IBuilding) arg0;
                return InfoBuilding.newInfoFromDomain(building);
            }});
    }

}