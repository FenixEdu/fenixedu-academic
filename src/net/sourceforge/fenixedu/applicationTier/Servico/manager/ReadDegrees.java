package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */
public class ReadDegrees implements IService {

    public List run() throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List degrees = sp.getICursoPersistente().readAll();

        return (List) CollectionUtils.collect(degrees, new Transformer() {

            public Object transform(Object arg0) {
                IDegree degree = (IDegree) arg0;
                InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
                return infoDegree;
            }
        });

    }
}