package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;

/**
 * @author lmac1
 */

public class DeleteDegrees extends Service {

    // delete a set of degrees
    public List run(List degreesInternalIds) throws FenixServiceException, ExcepcaoPersistencia {
            ICursoPersistente persistentDegree = persistentSupport.getICursoPersistente();

            Iterator iter = degreesInternalIds.iterator();

            List undeletedDegreesNames = new ArrayList();

            while (iter.hasNext()) {

                Integer internalId = (Integer) iter.next();
                Degree degree = (Degree)persistentDegree.readByOID(Degree.class,internalId);
                
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