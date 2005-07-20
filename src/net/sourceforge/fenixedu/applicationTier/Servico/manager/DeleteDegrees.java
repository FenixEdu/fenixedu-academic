package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class DeleteDegrees implements IService {

    // delete a set of degrees
    public List run(List degreesInternalIds) throws FenixServiceException, ExcepcaoPersistencia {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            ICursoPersistente persistentDegree = sp.getICursoPersistente();

            Iterator iter = degreesInternalIds.iterator();

            List undeletedDegreesNames = new ArrayList();

            while (iter.hasNext()) {

                Integer internalId = (Integer) iter.next();
                IDegree degree = (IDegree)persistentDegree.readByOID(Degree.class,internalId);
                
                if (degree != null) {
                
                    try {
						degree.delete();
					} catch (DomainException e) {
						undeletedDegreesNames.add(degree.getNome());
					}
                }
            }

            return undeletedDegreesNames;

    }
}