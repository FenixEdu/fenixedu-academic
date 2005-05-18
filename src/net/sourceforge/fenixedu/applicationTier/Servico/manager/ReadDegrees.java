package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Luis Cruz
 */
public class ReadDegrees implements IService {

    public List run() throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final List<IDegree> degrees = persistentSupport.getICursoPersistente().readAll();
        final List<InfoDegree> infoDegrees = new ArrayList<InfoDegree>(degrees.size());
        for (final IDegree degree : degrees) {
            infoDegrees.add(InfoDegree.newInfoFromDomain(degree));
        }
        return infoDegrees;
    }

}