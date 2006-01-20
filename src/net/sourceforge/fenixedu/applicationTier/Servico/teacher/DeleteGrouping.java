/*
 * Created on 2/Abr/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Tânia Pousão
 * 
 */
public class DeleteGrouping extends Service {

    public Boolean run(Integer executionCourseId, Integer groupPropertiesId)
            throws FenixServiceException, ExcepcaoPersistencia {

        if (groupPropertiesId == null) {
            return Boolean.FALSE;
        }

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
     
        Grouping groupProperties = (Grouping) persistentSupport.getIPersistentObject().readByOID(Grouping.class,
                groupPropertiesId);

        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        groupProperties.delete();
               
        return Boolean.TRUE;
    }

}
