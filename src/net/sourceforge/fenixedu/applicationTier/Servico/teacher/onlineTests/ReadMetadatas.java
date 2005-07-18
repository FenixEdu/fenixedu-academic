/*
 * Created on 23/Jul/2003
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoMetadata;
import net.sourceforge.fenixedu.domain.onlineTests.IMetadata;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadMetadatas implements IService {

    public List<InfoMetadata> run(Integer executionCourseId, Integer testId, Boolean distributedTest) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();
            List<IMetadata> metadatas = null;
            if (testId == null) {
                metadatas = persistentMetadata.readByExecutionCourseAndVisibility(executionCourseId);
            } else if (distributedTest == false) {
                metadatas = persistentSuport.getIPersistentMetadata().readByExecutionCourseAndNotTest(executionCourseId, testId);
            } else if (distributedTest == true) {
                metadatas = persistentSuport.getIPersistentMetadata().readByExecutionCourseAndNotDistributedTest(executionCourseId, testId);
            }
            List<InfoMetadata> infoMetadataList = new ArrayList<InfoMetadata>();
            for (IMetadata metadata : metadatas) {
                infoMetadataList.add(InfoMetadata.newInfoFromDomain(metadata));
            }
            return infoMetadataList;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}